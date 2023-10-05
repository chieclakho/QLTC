package com.clk.quanlichitieu.view.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.OnActCallBack;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.chi.SpinnerLoaiChiAdapter;
import com.clk.quanlichitieu.dao.entities.KhoanChi;
import com.clk.quanlichitieu.dao.entities.LoaiChi;
import com.clk.quanlichitieu.databinding.DialogKhoanChiBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppKhoanChiDialog extends Dialog {
    public static final String KEY_APP_KHOAN_CHI = "KEY_APP_KHOAN_CHI";
    private final OnActCallBack callBack;

    private final DialogKhoanChiBinding binding;
    private List<LoaiChi> list;
    private SpinnerLoaiChiAdapter adapter;
    private Calendar calendar;
    private KhoanChi khoanChi;
    private String userId;

    public AppKhoanChiDialog(@NonNull Context context, OnActCallBack callBack) {
        super(context, R.style.Theme_QuanLiThuChi_Dialog);
        this.callBack = callBack;
        binding = DialogKhoanChiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        list = new ArrayList<>();
        adapter = new SpinnerLoaiChiAdapter(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(userId);
        usersRef.whereEqualTo("loai", "LOAICHI").get().addOnCompleteListener(task -> {
            QuerySnapshot snapshot = task.getResult();
            if (snapshot == null) return;
            list.clear();
            for (QueryDocumentSnapshot doc : snapshot) {
                LoaiChi loaiChi = doc.toObject(LoaiChi.class);
                loaiChi.ducumnetId = doc.getId();
                list.add(loaiChi);
            }
            setAdapterSpinner();
        });
        calendar = Calendar.getInstance();
        binding.tvDate.setText(String.format("%s/%s/%s", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        khoanChi = App.getInstance().getStorage().getKhoanChi();
        if (khoanChi != null) {
            binding.tvDate.setText(khoanChi.ngayChi);
            binding.edtNote.setText(khoanChi.note);
            binding.edtMoney.setText(String.format("%s", khoanChi.soTien));
            binding.edtName.setText(khoanChi.tenKhoanChi);
            binding.spLoai.setSelection(list.indexOf(new LoaiChi(khoanChi.tenLoai)));
            App.getInstance().getStorage().setKhoanChi(null);
        }
        binding.btnExit.setOnClickListener(view -> dismiss());
        binding.btnAdd.setOnClickListener(view -> appKhoanChi());
        binding.datePicker.setOnClickListener(view -> showPickDialogDate());
    }

    private void setAdapterSpinner() {
        binding.spLoai.setAdapter(adapter);
        binding.spLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiChi loaiChi = (LoaiChi) parent.getItemAtPosition(position);
                binding.tvLoaiChi.setText(loaiChi.tenLoai);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showPickDialogDate() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                R.style.CustomDatePickerDialog, (view, year, month, dayOfMonth) ->
                binding.tvDate.setText(String.format("%s/%s/%s", dayOfMonth, month + 1, year)),
                initialYear, initialMonth, initialDay);
        datePickerDialog.show();
    }

    private void appKhoanChi() {
        String tvName = binding.edtName.getText().toString().trim();
        String tvDate = binding.tvDate.getText().toString().trim();
        String tvMoney = binding.edtMoney.getText().toString().trim();
        String tvNote = binding.edtNote.getText().toString().trim();
        String tvTenLoai = binding.tvLoaiChi.getText().toString().trim().replace("Chọn loại chi =>", "");
        String txtDate = tvDate.replace("Ngày chi", "");
        if (txtDate.isEmpty() || tvMoney.isEmpty() || tvName.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        khoanChi = new KhoanChi();
        khoanChi.tenKhoanChi = tvName;
        khoanChi.soTien = Long.parseLong(tvMoney);
        khoanChi.note = tvNote;
        khoanChi.ngayChi = tvDate;
        khoanChi.tenLoai = tvTenLoai;
        callBack.callBack(KEY_APP_KHOAN_CHI, khoanChi);
        dismiss();
    }
}
