package com.clk.quanlichitieu.view.fragment;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.databinding.FragmentChangePasswordBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends BaseFragment<FragmentChangePasswordBinding, CommonVM> {
    private ProgressDialog dialog;
    public static final String TAG = ChangePassword.class.getName();

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        dialog = new ProgressDialog(context);
        binding.menu.ivMenu.setOnClickListener(this);
        binding.menu.ivMenu.setImageResource(R.drawable.ic_back);
        binding.menu.tvTitle.setText(R.string.txt_change_Password);
        binding.btnChangePassworg.setOnClickListener(this);
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.btn_change_passworg) {
            onLickChangePassword();
        } else if (v.getId() == R.id.iv_menu) {
            callBack.backToPrevious();
        }
    }

    private void onLickChangePassword() {

        String passwordNew = binding.edtPassWordNew.getText().toString().trim();
        String passwordVerify = binding.edtPassWordVerify.getText().toString().trim();
        if (passwordVerify.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
        } else if (!passwordNew.equals(passwordVerify)) {
            Toast.makeText(context, "Mật khẩu không khớp/vui lòng nhập lại", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.updatePassword(passwordNew).addOnCompleteListener(task -> Toast.makeText(context, "User password updated", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(context, "User password updated", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

    @Override
    protected FragmentChangePasswordBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentChangePasswordBinding.inflate(inflater, container, false);
    }
}
