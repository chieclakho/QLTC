package com.clk.quanlichitieu.view.fragment.chi;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.chi.LoaiChiAdapter;
import com.clk.quanlichitieu.dao.entities.LoaiChi;
import com.clk.quanlichitieu.databinding.FragmentLoaiChiBinding;
import com.clk.quanlichitieu.view.dialog.AppLoaiChiDialog;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.LoaiChiVM;
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

public class LoaiChiFragment extends BaseFragment<FragmentLoaiChiBinding, LoaiChiVM> {
    private List<LoaiChi> loaiChiList;
    private String userId;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private AlertDialog dialog;

    @Override
    protected Class<LoaiChiVM> initClassModel() {
        return LoaiChiVM.class;
    }

    @Override
    protected void initViews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progressbar);
        dialog = builder.create();
        loaiChiList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)  userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(userId);
        loadData();
        binding.btnAddLoaiChi.setOnClickListener(this);
    }

    private void initAdapter() {
        LoaiChiAdapter adapter = new LoaiChiAdapter(context, loaiChiList, new LoaiChiAdapter.IClickOnItem() {
            @Override
            public void update(LoaiChi loaiChi) {
                App.getInstance().getStorage().setLoaiChi(loaiChi);
                updateLoaiChi(loaiChi);
            }

            @Override
            public void delete(LoaiChi loaiChi) {
                deleteLoaiChi(loaiChi);
            }
        });
        binding.rcvLoaiChi.setAdapter(adapter);
    }

    private void updateLoaiChi(LoaiChi loaiChi) {
        AppLoaiChiDialog dialog = new AppLoaiChiDialog(context, (key, data) -> {
            LoaiChi item = (LoaiChi) data;
            DocumentReference doc = usersRef.document(loaiChi.ducumnetId);
            WriteBatch batch = db.batch();
            batch.update(doc, "tenLoai", item.tenLoai);
            batch.commit().addOnSuccessListener(unused -> {
                loadData();
                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show());
        });
        dialog.show();
    }

    private void deleteLoaiChi(LoaiChi loaiChi) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa Khoản Chi")
                .setIcon(android.R.drawable.ic_delete)
                .setMessage("Bạn có chắc chắn xoá không ?")
                .setPositiveButton("Có", (dialog, which) -> {
                    DocumentReference doc = usersRef.document(loaiChi.ducumnetId);
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
        if (v.getId() == R.id.btn_add_loai_chi) {
            addLoaiChi();
        }
    }

    private void addLoaiChi() {
        AppLoaiChiDialog dialog = new AppLoaiChiDialog(context, (key, data) -> {
            LoaiChi loaiChi = (LoaiChi) data;
            Map<String, Object> user = new HashMap<>();
            user.put("loai", "LOAICHI");
            user.put("tenLoai", loaiChi.tenLoai);
            usersRef.add(user).addOnSuccessListener(documentReference -> {
                Toast.makeText(context, "Add success", Toast.LENGTH_SHORT).show();
                loadData();
            });
        });
        dialog.show();
    }

    private void loadData() {
        dialog.show();
        usersRef.whereEqualTo("loai", "LOAICHI").get().addOnCompleteListener(task -> {
            loaiChiList.clear();
            QuerySnapshot snapshot = task.getResult();
            for (DocumentSnapshot doc : snapshot) {
                LoaiChi loaiChi = doc.toObject(LoaiChi.class);
                assert loaiChi != null;
                loaiChi.ducumnetId = doc.getId();
                loaiChi.tenLoai = doc.getString("tenLoai");
                loaiChiList.add(loaiChi);
            }
            dialog.dismiss();
            initAdapter();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Load data fail", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected FragmentLoaiChiBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentLoaiChiBinding.inflate(inflater, container, false);
    }
}
