package com.clk.quanlichitieu.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.CommonUtils;
import com.clk.quanlichitieu.OnActCallBack;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.databinding.DialogUpdateMoneyBinding;

public class UpDateMoneyDialog extends Dialog {
    public static final String KEY_UPDATE_MONEY = "KEY_UPDATE_MONEY";
    private static final String KEY_MONEY = "KEY_MONEY";
    private final OnActCallBack callBack;

    private final DialogUpdateMoneyBinding binding;

    public UpDateMoneyDialog(@NonNull Context context, OnActCallBack callBack) {
        super(context, R.style.Theme_QuanLiThuChi_Dialog);
        this.callBack = callBack;
        binding = DialogUpdateMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        binding.btnExit.setOnClickListener(view -> dismiss());
        binding.btnAdd.setOnClickListener(view -> upDateMoney());
    }

    private void upDateMoney() {
        String money = binding.edtMoney.getText().toString().trim().replaceAll("^0+", "");
        if (money.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập số tiền cảu bạn", Toast.LENGTH_SHORT).show();
        } else {
            callBack.callBack(KEY_UPDATE_MONEY, money);
            dismiss();
        }
    }
}
