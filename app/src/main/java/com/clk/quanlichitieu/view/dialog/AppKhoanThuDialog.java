package com.clk.quanlichitieu.view.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.OnActCallBack;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.thu.SpinnerLoaiThuAdapter;
import com.clk.quanlichitieu.dao.entities.KhoanThu;
import com.clk.quanlichitieu.dao.entities.LoaiThu;
import com.clk.quanlichitieu.databinding.DialogKhoanThuBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppKhoanThuDialog extends Dialog {
    public static final String KEY_APP_KHOAN_THU = "KEY_APP_KHOAN_THU";
    private final OnActCallBack callBack;
    private final DialogKhoanThuBinding binding;
    private Calendar calendar;
    private KhoanThu khoanThu;
    private List<LoaiThu> list;
    private SpinnerLoaiThuAdapter adapter;
    private String userId;

    public AppKhoanThuDialog(@NonNull Context context, OnActCallBack callBack) {
        super(context, R.style.Theme_QuanLiThuChi_Dialog);
        this.callBack = callBack;
        binding = DialogKhoanThuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        list = new ArrayList<>();
        adapter = new SpinnerLoaiThuAdapter(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(userId);
        usersRef.whereEqualTo("loai", "LOAITHU").get().addOnCompleteListener(task -> {
            QuerySnapshot snapshot = task.getResult();
            if (snapshot == null) return;
            list.clear();
            for (QueryDocumentSnapshot doc : snapshot) {
                LoaiThu loaiThu = doc.toObject(LoaiThu.class);
                loaiThu.ducumnetId = doc.getId();
                list.add(loaiThu);
            }
            setAdapterSpinner();
        });
        calendar = Calendar.getInstance();
        binding.tvDate.setText(String.format("%s/%s/%s", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        khoanThu = App.getInstance().getStorage().getKhoanThu();
        if (khoanThu != null) {
            binding.edtName.setText(khoanThu.tenKhoanThu);
            binding.tvDate.setText(khoanThu.ngayThu);
            binding.edtMoney.setText(String.format("%s", khoanThu.soTien));
            binding.edtNote.setText(khoanThu.note);
            binding.spLoai.setSelection(list.indexOf(new LoaiThu( khoanThu.tenLoai)));
            App.getInstance().getStorage().setKhoanThu(null);
        }
        binding.btnExit.setOnClickListener(view -> dismiss());
        binding.btnAdd.setOnClickListener(view -> appKhoanThu());
        binding.datePicker.setOnClickListener(view -> showPickDialogDate());
    }

    private void setAdapterSpinner() {
        binding.spLoai.setAdapter(adapter);
        binding.spLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiThu loaiThu = (LoaiThu) parent.getItemAtPosition(position);
                binding.tvLoaiThu.setText(loaiThu.tenLoai);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.CustomDatePickerDialog,
                (view, year, month, dayOfMonth) -> binding.tvDate.setText(String.format("%s/%s/%s",
                        dayOfMonth, month + 1, year)), initialYear, initialMonth, initialDay);
        datePickerDialog.show();
    }

    private void appKhoanThu() {
        String tvName = binding.edtName.getText().toString().trim();
        String tvDate = binding.tvDate.getText().toString().trim();
        String tvMoney = binding.edtMoney.getText().toString().trim();
        String tvNote = binding.edtNote.getText().toString().trim();
        String tvTenLoai = binding.tvLoaiThu.getText().toString().trim().replace("Chọn loại thu =>","");
        String txtDate = tvDate.replace("Ngày chi", "");
        if (txtDate.isEmpty() || tvMoney.isEmpty() || tvName.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        khoanThu = new KhoanThu();
        khoanThu.tenKhoanThu = tvName;
        khoanThu.soTien = Long.parseLong(tvMoney);
        khoanThu.note = tvNote;
        khoanThu.ngayThu = tvDate;
        khoanThu.tenLoai = tvTenLoai;
        callBack.callBack(KEY_APP_KHOAN_THU, khoanThu);
        dismiss();
    }
}
