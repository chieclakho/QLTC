package com.clk.quanlichitieu.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.databinding.FragmentSignupBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class SignupFragment extends BaseFragment<FragmentSignupBinding, CommonVM> {
    public static final String TAG = SignupFragment.class.getName();
    private String email;
    private String passWord;
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
        binding.btnSignup.setOnClickListener(this);
    }
    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.btn_signup) {
            email = binding.edtEmail.getText().toString().trim();
            passWord = binding.edtPassWord.getText().toString().trim();
            String confirmPassWord = binding.edtConfirmPassWord.getText().toString().trim();
            if (email.isEmpty() || passWord.isEmpty()) {
                Toast.makeText(context, "User/password trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!passWord.equals(confirmPassWord)) {
                Toast.makeText(context, "Password không khớp", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();
            dialog.show();
            auth.createUserWithEmailAndPassword(email, passWord)
                    .addOnCompleteListener((Activity) context, task -> {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            callBack.showFragment(LoginFragment.TAG, null, false);
                        } else {
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    protected FragmentSignupBinding initViewBinding(LayoutInflater inflater, ViewGroup
            container) {
        return FragmentSignupBinding.inflate(inflater, container, false);
    }
}
