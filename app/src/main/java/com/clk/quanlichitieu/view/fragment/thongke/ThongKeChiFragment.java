package com.clk.quanlichitieu.view.fragment.thongke;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.chi.KhoanChiAdapter;
import com.clk.quanlichitieu.dao.entities.KhoanChi;
import com.clk.quanlichitieu.databinding.FragmentThongkeChiBinding;
import com.clk.quanlichitieu.view.dialog.AppKhoanChiDialog;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class ThongKeChiFragment extends BaseFragment<FragmentThongkeChiBinding, CommonVM> {
    private static final String TAG = ThongKeChiFragment.class.getName();
    private Calendar calendar;
    private FirebaseFirestore db;
    private String userId;
    private CollectionReference usersRef;
    private AlertDialog alertDialog;

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    private List<KhoanChi> khoanChiList;
    private long sumMoney;

    @Override
    protected void initViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(userId);
        khoanChiList = new ArrayList<>();
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
        }else if (v.getId() == R.id.btn_add_khoan_chi) {
            addKhoanChi();
        }
    }

    private void thongKe() {
        alertDialog.show();
        String startDate = binding.tvStart.getText().toString().trim();
        String endDate = binding.tvEnd.getText().toString().trim();
        Query query = usersRef.whereGreaterThanOrEqualTo("ngayChi", startDate).whereLessThanOrEqualTo("ngayChi", endDate);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                khoanChiList.clear();
                sumMoney = 0;
                for (DocumentSnapshot document : documents) {
                    KhoanChi khoanChi = document.toObject(KhoanChi.class);
                    assert khoanChi != null;
                    khoanChi.ducumnetId = document.getId();
                    khoanChiList.add(khoanChi);
                    sumMoney += khoanChi.soTien;
                }
                alertDialog.dismiss();
                initAdapter();
            }
        });
    }

    private void initAdapter() {
        KhoanChiAdapter adapter = new KhoanChiAdapter(context, khoanChiList, new KhoanChiAdapter.IClickOnItem() {
            @Override
            public void update(KhoanChi khoanChi) {
                App.getInstance().getStorage().setKhoanChi(khoanChi);
                upDateKhoanChi(khoanChi);
            }

            @Override
            public void delete(KhoanChi khoanChi) {
                deleteKhoanChi(khoanChi);
            }
        });
        binding.rcvKhoanChi.setAdapter(adapter);
        binding.tvSumMoney.setText(String.format("Tổng số tiền chi là : %s", initMoney(sumMoney)));
    }

    private void deleteKhoanChi(KhoanChi khoanChi) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa Khoản Chi")
                .setIcon(android.R.drawable.ic_delete)
                .setMessage("Bạn có chắc chắn xoá không ?")
                .setPositiveButton("Có", (dialog, which) -> {
                    DocumentReference doc = usersRef.document(khoanChi.ducumnetId);
                    WriteBatch batch = db.batch();
                    batch.delete(doc).commit().addOnSuccessListener(unused -> Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show());
                    clickView(binding.btnSearch);
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void upDateKhoanChi(KhoanChi khoanChi) {
        AppKhoanChiDialog dialog = new AppKhoanChiDialog(context, (key, data) -> {
            KhoanChi item = (KhoanChi) data;
            DocumentReference doc = usersRef.document(khoanChi.ducumnetId);
            WriteBatch batch = db.batch();
            batch.update(doc, "tenLoai", item.tenLoai);
            batch.update(doc, "ngayChi", item.ngayChi);
            batch.update(doc, "tenKhoanChi", item.tenKhoanChi);
            batch.update(doc, "soTien", item.soTien);
            batch.update(doc, "note", item.note);
            batch.commit().addOnSuccessListener(unused -> {
                clickView(binding.btnSearch);
                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show());
        });
        dialog.show();
    }

    private void addKhoanChi() {
        AppKhoanChiDialog dialog = new AppKhoanChiDialog(context, (key, data) -> {
            KhoanChi khoanChi = (KhoanChi) data;
            Map<String, Object> user = new HashMap<>();
            user.put("loai", "LOAIKHOANCHI");
            user.put("tenLoai", khoanChi.tenLoai);
            user.put("ngayChi", khoanChi.ngayChi);
            user.put("tenKhoanChi", khoanChi.tenKhoanChi);
            user.put("soTien", khoanChi.soTien);
            user.put("note", khoanChi.note);
            usersRef.add(user).addOnSuccessListener(documentReference -> {
                Toast.makeText(context, "Add success", Toast.LENGTH_SHORT).show();
                clickView(binding.btnSearch);
            });
        });
        dialog.show();
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
    protected FragmentThongkeChiBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentThongkeChiBinding.inflate(inflater, container, false);
    }
}
