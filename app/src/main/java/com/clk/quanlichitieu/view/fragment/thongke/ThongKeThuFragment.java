package com.clk.quanlichitieu.view.fragment.thongke;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.thu.KhoanThuAdapter;
import com.clk.quanlichitieu.dao.entities.KhoanThu;
import com.clk.quanlichitieu.databinding.FragmentThongkeThuBinding;
import com.clk.quanlichitieu.view.dialog.AppKhoanThuDialog;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ThongKeThuFragment extends BaseFragment<FragmentThongkeThuBinding, CommonVM> {
    private List<KhoanThu> khoanThuList;
    private long sumMoney;
    private Calendar calendar;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private String userId;
    private AlertDialog alertDialog;

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        khoanThuList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(userId);
        binding.btnSearch.setOnClickListener(this);
        binding.datePickStart.setOnClickListener(this);
        binding.datePickEnd.setOnClickListener(this);
        calendar = Calendar.getInstance();
        binding.tvStart.setText(String.format("%s/%s/%s", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        binding.tvEnd.setText(String.format("%s/%s/%s", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialog = builder.setView(R.layout.progressbar).create();
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.btn_search) {
            thongKe();
        } else if (v.getId() == R.id.date_pick_start) {
            showPickDialogDate("START");
        } else if (v.getId() == R.id.date_pick_end) {
            showPickDialogDate("END");
        } else if (v.getId() == R.id.btn_add_khoan_thu) {
            addKhoanThu();
        }
    }

    private void thongKe() {
        alertDialog.show();
        String startDate = binding.tvStart.getText().toString().trim();
        String endDate = binding.tvEnd.getText().toString().trim();
        Query query = db.collection(userId).whereGreaterThanOrEqualTo("ngayThu", startDate).whereLessThanOrEqualTo("ngayThu", endDate);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                khoanThuList.clear();
                sumMoney = 0;
                for (DocumentSnapshot document : documents) {
                    KhoanThu khoanThu = document.toObject(KhoanThu.class);
                    assert khoanThu != null;
                    khoanThu.ducumnetId = document.getId();
                    khoanThuList.add(khoanThu);
                    sumMoney += khoanThu.soTien;
                }
                alertDialog.dismiss();
                initAdapter();
            }
        });
    }

    private void initAdapter() {
        KhoanThuAdapter adapter = new KhoanThuAdapter(context, khoanThuList, new KhoanThuAdapter.IClickOnItem() {
            @Override
            public void update(KhoanThu khoanThu) {
                App.getInstance().getStorage().setKhoanThu(khoanThu);
                updateKhoanThu(khoanThu);
            }

            @Override
            public void delete(KhoanThu khoanThu) {
                deleteKhoanThu(khoanThu);
            }
        });
        binding.rcvKhoanThu.setAdapter(adapter);
        binding.tvSumMoney.setText(String.format("Tổng số tiền thu là : %s", initMoney(sumMoney)));
    }

    private void updateKhoanThu(KhoanThu khoanThu) {
        AppKhoanThuDialog dialog = new AppKhoanThuDialog(context, (key, data) -> {
            KhoanThu item = (KhoanThu) data;
            DocumentReference doc = usersRef.document(khoanThu.ducumnetId);
            WriteBatch batch = db.batch();
            batch.update(doc, "tenLoai", item.tenLoai);
            batch.update(doc, "ngayThu", item.ngayThu);
            batch.update(doc, "tenKhoanThu", item.tenKhoanThu);
            batch.update(doc, "soTien", item.soTien);
            batch.update(doc, "note", item.note);
            batch.commit().addOnSuccessListener(unused -> {
                clickView(binding.btnSearch);
                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show());
        });
        dialog.show();
    }

    private void addKhoanThu() {
        AppKhoanThuDialog dialog = new AppKhoanThuDialog(context, (key, data) -> {
            KhoanThu khoanThu = (KhoanThu) data;
            Map<String, Object> user = new HashMap<>();
            user.put("loai", "LOAIKHOANTHU");
            user.put("tenLoai", khoanThu.tenLoai);
            user.put("ngayThu", khoanThu.ngayThu);
            user.put("tenKhoanThu", khoanThu.tenKhoanThu);
            user.put("soTien", khoanThu.soTien);
            user.put("note", khoanThu.note);
            usersRef.add(user).addOnSuccessListener(documentReference -> {
                Toast.makeText(context, "Add success", Toast.LENGTH_SHORT).show();
                clickView(binding.btnSearch);
            });
        });
        dialog.show();
    }

    private void deleteKhoanThu(KhoanThu khoanThu) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa Khoản Thu")
                .setIcon(android.R.drawable.ic_delete)
                .setMessage("Bạn có chắc chắn xoá không ?")
                .setPositiveButton("Có", (dialog, which) -> {
                    DocumentReference doc = usersRef.document(khoanThu.ducumnetId);
                    WriteBatch batch = db.batch();
                    batch.delete(doc).commit().addOnSuccessListener(unused -> Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show());
                    clickView(binding.btnSearch);
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private String initMoney(long money) {
        try {
            Locale locale = new Locale("vi", "VN");
            Currency currency = Currency.getInstance("VND");
            DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
            df.setCurrency(currency);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            numberFormat.setCurrency(currency);
            return numberFormat.format(money);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showPickDialogDate(String key) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.CustomDatePickerDialog,
                (view, year, month, dayOfMonth) -> {
                    if (key.equals("START")) {
                        binding.tvStart.setText(String.format("%s/%s/%s", dayOfMonth, month + 1, year));
                    } else if (key.equals("END")) {
                        binding.tvEnd.setText(String.format("%s/%s/%s", dayOfMonth, month + 1, year));
                    }
                }, initialYear, initialMonth, initialDay);
        datePickerDialog.show();
    }

    @Override
    protected FragmentThongkeThuBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentThongkeThuBinding.inflate(inflater, container, false);
    }
}
