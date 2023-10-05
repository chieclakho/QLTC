package com.clk.quanlichitieu.view.fragment.thu;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.thu.KhoanThuAdapter;
import com.clk.quanlichitieu.dao.entities.KhoanThu;
import com.clk.quanlichitieu.databinding.FragmentKhoanThuBinding;
import com.clk.quanlichitieu.view.dialog.AppKhoanThuDialog;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.KhoanThuViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KhoanThuFragment extends BaseFragment<FragmentKhoanThuBinding, KhoanThuViewModel> {
    private List<KhoanThu> khoanThuList;
    private String userId;
    private CollectionReference usersRef;
    private FirebaseFirestore db;
    private AlertDialog alertDialog;

    @Override
    protected Class<KhoanThuViewModel> initClassModel() {
        return KhoanThuViewModel.class;
    }

    @Override
    protected void initViews() {
        khoanThuList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)  userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(userId);
        binding.btnAddKhoanThu.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progressbar);
        alertDialog = builder.create();
        loadData();
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
                loadData();
                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show());
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
                    loadData();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    protected void clickView(View v) {
        if (v.getId() == R.id.btn_add_khoan_thu) {
            addKhoanThu();
        }
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
                loadData();
            });
        });
        dialog.show();
    }


    private void loadData() {
        alertDialog.show();
        usersRef.whereEqualTo("loai", "LOAIKHOANTHU").get().addOnCompleteListener(task -> {
            khoanThuList.clear();
            QuerySnapshot snapshot = task.getResult();
            for (DocumentSnapshot doc : snapshot) {
                KhoanThu khoanThu = doc.toObject(KhoanThu.class);
                assert khoanThu != null;
                khoanThu.ducumnetId = doc.getId();
                khoanThu.ngayThu = doc.getString("ngayThu");
                khoanThu.note = doc.getString("note");
                khoanThu.soTien = doc.getLong("soTien");
                khoanThu.tenKhoanThu = doc.getString("tenKhoanThu");
                khoanThu.tenLoai = doc.getString("tenLoai");
                khoanThuList.add(khoanThu);
            }
            alertDialog.dismiss();
            initAdapter();
        }).addOnFailureListener(e -> {
            alertDialog.dismiss();
            Toast.makeText(context, "Load data fail", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    protected FragmentKhoanThuBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentKhoanThuBinding.inflate(inflater, container, false);
    }
}
