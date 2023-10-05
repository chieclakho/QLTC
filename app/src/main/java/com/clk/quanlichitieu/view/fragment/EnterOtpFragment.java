package com.clk.quanlichitieu.view.fragment;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.databinding.FragmentEnterOtpBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtpFragment extends BaseFragment<FragmentEnterOtpBinding, CommonVM> {
    public static final String TAG = EnterOtpFragment.class.getName();
    private FirebaseAuth auth;
    private PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        Object[] objects = (Object[]) data;
        auth = FirebaseAuth.getInstance();
        binding.btnSendOtp.setOnClickListener(v -> onClickSendOtpCode());
        binding.requestOtp.setOnClickListener(v -> onClickVerifyPhone((String) objects[0]));
    }

    private void onClickVerifyPhone(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity((Activity) context)
                        .setForceResendingToken(token)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verifycationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifycationID, forceResendingToken);
                                token = forceResendingToken;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void onClickSendOtpCode() {
        String otp = binding.edtOtp.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(binding.edtOtp.getText().toString().trim(), otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        callBack.showFragment(MenuFragment.TAG, user, false);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(context, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected FragmentEnterOtpBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentEnterOtpBinding.inflate(inflater, container, false);
    }
}
