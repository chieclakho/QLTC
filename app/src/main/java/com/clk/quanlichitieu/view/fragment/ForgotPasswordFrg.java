package com.clk.quanlichitieu.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.databinding.FragmentForgotPasswordBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFrg extends BaseFragment<FragmentForgotPasswordBinding, CommonVM> {
    public static final String TAG = ForgotPasswordFrg.class.getName();
    private AlertDialog dialog;

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progressbar);
        dialog = builder.create();
        binding.btnNext.setOnClickListener(this);
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.btn_next) {
            String email = binding.edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập Email của bạn", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                dialog.show();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Mật khẩu đã được gửi vào Email của bạn", Toast.LENGTH_LONG).show();
                                callBack.backToPrevious();
                            }else {
                                Toast.makeText(context, "Email không đúng/vui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }

    @Override
    protected FragmentForgotPasswordBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentForgotPasswordBinding.inflate(inflater, container, false);
    }
}
