package com.clk.quanlichitieu.view.fragment.chi;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.chi.KhoanChiAdapter;
import com.clk.quanlichitieu.dao.entities.KhoanChi;
import com.clk.quanlichitieu.databinding.FragmentKhoanChiBinding;
import com.clk.quanlichitieu.view.dialog.AppKhoanChiDialog;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.KhoanChiViewModel;
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


public class KhoanChiFragment extends BaseFragment<FragmentKhoanChiBinding, KhoanChiViewModel> {
    private List<KhoanChi> khoanChiList;
    private String userId;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private AlertDialog alertDialog;

    @Override
    protected Class<KhoanChiViewModel> initClassModel() {
        return KhoanChiViewModel.class;
    }

    @Override
    protected void initViews() {
        khoanChiList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(userId);
        binding.btnAddKhoanChi.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialog = builder.setView(R.layout.progressbar).create();
        loadData();
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
                loadData();
                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show());
        });
        dialog.show();
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
                    loadData();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    protected void clickView(View v) {
        if (v.getId() == R.id.btn_add_khoan_chi) {
            addKhoanChi();
        }
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
                loadData();
            });
        });
        dialog.show();
    }


    private void loadData() {
        alertDialog.show();
        usersRef.whereEqualTo("loai", "LOAIKHOANCHI").get().addOnCompleteListener(task -> {
            khoanChiList.clear();
            QuerySnapshot snapshot = task.getResult();
            for (DocumentSnapshot doc : snapshot) {
                KhoanChi khoanChi = doc.toObject(KhoanChi.class);
                assert khoanChi != null;
                khoanChi.ducumnetId = doc.getId();
                khoanChi.ngayChi = doc.getString("ngayChi");
                khoanChi.note = doc.getString("note");
                khoanChi.soTien = doc.getLong("soTien");
                khoanChi.tenKhoanChi = doc.getString("tenKhoanChi");
                khoanChi.tenLoai = doc.getString("tenLoai");
                khoanChiList.add(khoanChi);
            }
            alertDialog.dismiss();
            initAdapter();
        }).addOnFailureListener(e ->{
            alertDialog.dismiss();
            Toast.makeText(context, "Load data fail", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected FragmentKhoanChiBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentKhoanChiBinding.inflate(inflater, container, false);
    }
}
