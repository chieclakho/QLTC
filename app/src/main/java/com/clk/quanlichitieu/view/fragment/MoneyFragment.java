package com.clk.quanlichitieu.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.CommonUtils;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.databinding.FragmentMoneyBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;


public class MoneyFragment extends BaseFragment<FragmentMoneyBinding, CommonVM> {
    public static final String TAG = MoneyFragment.class.getName();
    public static final String KEY_MONEY = "KEY_MONEY";

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        binding.btnOk.setOnClickListener(this);
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.btn_ok) {
            String money = binding.edtMoney.getText().toString().trim().replaceAll("^0+", "");
            if (money.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            } else {
                CommonUtils.getInstance().savePref(KEY_MONEY, money);
                Toast.makeText(context, "Tạo số tiền thành công", Toast.LENGTH_SHORT).show();
                callBack.backToPrevious();
            }
        }
    }

    @Override
    protected FragmentMoneyBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMoneyBinding.inflate(inflater, container, false);
    }
}
