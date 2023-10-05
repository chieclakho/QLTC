package com.clk.quanlichitieu.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.OnActCallBack;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.dao.entities.LoaiChi;
import com.clk.quanlichitieu.databinding.DialogAddChiBinding;

public class AppLoaiChiDialog extends Dialog {
    public static final String KEY_APP_LOAI_CHI = "KEY_APP_LOAI_CHI";
    private final OnActCallBack callBack;

    private final DialogAddChiBinding binding;
    private LoaiChi loaiChi;

    public AppLoaiChiDialog(@NonNull Context context, OnActCallBack callBack) {
        super(context, R.style.Theme_QuanLiThuChi_Dialog);
        this.callBack = callBack;
        binding = DialogAddChiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
         loaiChi = App.getInstance().getStorage().getLoaiChi();
        if (loaiChi != null) {
            binding.edtName.setText(loaiChi.tenLoai);
            App.getInstance().getStorage().setLoaiChi(null);
        }
        binding.btnExit.setOnClickListener(view -> dismiss());
        binding.btnAdd.setOnClickListener(view -> appKhoanChi());
    }

    private void appKhoanChi() {
        String tvName = binding.edtName.getText().toString().trim();
        if (tvName.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tên loại chi", Toast.LENGTH_SHORT).show();
        } else {
            loaiChi = new LoaiChi();
            loaiChi.tenLoai = tvName;
            callBack.callBack(KEY_APP_LOAI_CHI, loaiChi);
            dismiss();
        }
    }
}
