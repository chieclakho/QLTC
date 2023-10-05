package com.clk.quanlichitieu.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.OnActCallBack;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.dao.entities.LoaiThu;
import com.clk.quanlichitieu.databinding.DialogAddThuBinding;

public class AppLoaiThuDialog extends Dialog {
    public static final String KEY_APP_LOAI_THU = "KEY_APP_LOAI_THU";
    private final OnActCallBack callBack;

    private final DialogAddThuBinding binding;

    public AppLoaiThuDialog(@NonNull Context context, OnActCallBack callBack) {
        super(context, R.style.Theme_QuanLiThuChi_Dialog);
        this.callBack = callBack;
        binding = DialogAddThuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        LoaiThu loaiThu = App.getInstance().getStorage().getLoaiThu();
        if (loaiThu != null) {
            binding.edtName.setText(loaiThu.tenLoai);
            App.getInstance().getStorage().setLoaiThu(null);
        }

        binding.btnExit.setOnClickListener(view -> dismiss());
        binding.btnAdd.setOnClickListener(view -> appKhoanChi());
    }

    private void appKhoanChi() {
        String tvName = binding.edtName.getText().toString().trim();
        if (tvName.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tên loại thu", Toast.LENGTH_SHORT).show();
        } else {
            LoaiThu loaiThu = new LoaiThu();
            loaiThu.tenLoai = tvName;
            callBack.callBack(KEY_APP_LOAI_THU, loaiThu);
            dismiss();
        }
    }
}
