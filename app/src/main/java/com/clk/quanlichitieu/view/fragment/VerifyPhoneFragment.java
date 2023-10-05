package com.clk.quanlichitieu.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clk.quanlichitieu.databinding.FragmentVerifyPhoneBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneFragment extends BaseFragment<FragmentVerifyPhoneBinding, CommonVM> {
    public static final String TAG = VerifyPhoneFragment.class.getName();
    private FirebaseAuth auth;
    private ProgressDialog dialog ;
    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        dialog = new ProgressDialog(context);
        auth = FirebaseAuth.getInstance();
        binding.verifyPhoneNumber.setOnClickListener(v -> onClickVerifyPhone());
    }

    private void onClickVerifyPhone() {
        dialog.show();
        String phone = binding.edtPhone.getText().toString().trim();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity((Activity) context)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                                dialog.dismiss();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                dialog.dismiss();
                                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verifycationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifycationID, forceResendingToken);
                                callBack.showFragment(EnterOtpFragment.TAG, new Object[]{phone, verifycationID}, false);
                                dialog.dismiss();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
    protected FragmentVerifyPhoneBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentVerifyPhoneBinding.inflate(inflater, container, false);
    }
}
