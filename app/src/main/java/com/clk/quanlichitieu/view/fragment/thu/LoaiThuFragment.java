package com.clk.quanlichitieu.view.fragment.thu;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.thu.LoaiThuAdapter;
import com.clk.quanlichitieu.dao.entities.LoaiThu;
import com.clk.quanlichitieu.databinding.FragmentLoaiThuBinding;
import com.clk.quanlichitieu.view.dialog.AppLoaiThuDialog;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.LoaiThuVM;
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

public class LoaiThuFragment extends BaseFragment<FragmentLoaiThuBinding, LoaiThuVM> {
    private List<LoaiThu> loaiThuList;
    private String userId;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private AlertDialog dialog;

    @Override
    protected Class<LoaiThuVM> initClassModel() {
        return LoaiThuVM.class;
    }

    @Override
    protected void initViews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progressbar);
        dialog = builder.create();
        loaiThuList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)  userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(userId);
        binding.btnAddLoaiThu.setOnClickListener(this);
        loadData();
    }

    private void initAdapter() {
        LoaiThuAdapter adapter = new LoaiThuAdapter(context, loaiThuList, new LoaiThuAdapter.IClickOnItem() {
            @Override
            public void update(LoaiThu loaiThu) {
                App.getInstance().getStorage().setLoaiThu(loaiThu);
                updateLoaiThu(loaiThu);
            }

            @Override
            public void delete(LoaiThu loaiThu) {
                deleteLoaiThu(loaiThu);
            }
        });
        binding.rcvLoaiThu.setAdapter(adapter);
    }

    private void updateLoaiThu(LoaiThu loaiThu) {
        AppLoaiThuDialog dialog = new AppLoaiThuDialog(context, (key, data) -> {
            LoaiThu item = (LoaiThu) data;
            DocumentReference doc = usersRef.document(loaiThu.ducumnetId);
            WriteBatch batch = db.batch();
            batch.update(doc, "tenLoai", item.tenLoai);
            batch.commit().addOnSuccessListener(unused -> {
                loadData();
                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show());
        });
        dialog.show();
    }

    private void deleteLoaiThu(LoaiThu loaiThu) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa Loại Thu")
                .setIcon(android.R.drawable.ic_delete)
                .setMessage("Bạn có chắc chắn xoá không ?")
                .setPositiveButton("Có", (dialog, which) -> {
                    DocumentReference doc = usersRef.document(loaiThu.ducumnetId);
                    WriteBatch batch = db.batch();
                    batch.delete(doc).commit().addOnSuccessListener(unused -> Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show());
                    loadData();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.btn_add_loai_thu) {
            addLoaiThu();
        }
    }

    private void addLoaiThu() {
        AppLoaiThuDialog dialog = new AppLoaiThuDialog(context, (key, data) -> {
            LoaiThu loaiThu = (LoaiThu) data;
            Map<String, Object> user = new HashMap<>();
            user.put("loai", "LOAITHU");
            user.put("tenLoai", loaiThu.tenLoai);
            usersRef.add(user).addOnSuccessListener(documentReference -> {
                Toast.makeText(context, "Add success", Toast.LENGTH_SHORT).show();
                loadData();
            });
        });
        dialog.show();
    }

    private void loadData() {
        dialog.show();
        usersRef.whereEqualTo("loai", "LOAITHU").get().addOnCompleteListener(task -> {
            loaiThuList.clear();
            QuerySnapshot snapshot = task.getResult();
            for (DocumentSnapshot doc : snapshot) {
                LoaiThu loaiThu = doc.toObject(LoaiThu.class);
                assert loaiThu != null;
                loaiThu.ducumnetId = doc.getId();
                loaiThu.tenLoai = doc.getString("tenLoai");
                loaiThuList.add(loaiThu);
            }
            dialog.dismiss();
            initAdapter();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Load data fail", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    protected FragmentLoaiThuBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentLoaiThuBinding.inflate(inflater, container, false);
    }
}
