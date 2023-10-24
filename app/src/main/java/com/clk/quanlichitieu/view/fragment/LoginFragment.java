package com.clk.quanlichitieu.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.CommonUtils;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.databinding.FragmentLoginBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.concurrent.Executor;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, CommonVM> {
    public static final String TAG = LoginFragment.class.getName();
    private static final int REQUEST_CODE = 102;
    private static final int REQUEST_CODE_ACCESS = 103;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> launcher;
    private AlertDialog dialog;
    private CallbackManager mCallbackManager;

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        FacebookSdk.sdkInitialize(App.getInstance());
        FacebookSdk.setApplicationId("1479821226167301");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            binding.edtEmail.setText(user.getEmail());
        }
        binding.btnLogin.setOnClickListener(this);
        binding.btnLoginGoogle.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        binding.tvForGotPassWord.setOnClickListener(this);
        binding.btnLoginFb.setOnClickListener(this);
        checkPermissionAPI();
        currentregistrationtoken();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progressbar);
        dialog = builder.create();
        CommonUtils.getInstance().savePref("ABOUT", TAG);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDialog(boolean show) {
        if (show) dialog.show();
        else dialog.dismiss();
    }

    private void currentregistrationtoken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.i(TAG, "Fetching", task.getException());
                return;
            }
            String token = task.getResult();
            Log.i(TAG, "Fetching" + token);
        });
    }

    private void checkPermissionAPI() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_ACCESS);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_CODE);
        }
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.btn_login_google) {
            setDialog(true);
            signInWithGoogle();
        } else if (v.getId() == R.id.btn_login) {
            login();
        } else if (v.getId() == R.id.tv_sign_up) {
            callBack.showFragment(SignupFragment.TAG, null, true);
        } else if (v.getId() == R.id.tv_for_got_pass_word) {
            callBack.showFragment(ForgotPasswordFrg.TAG, null, true);
        } else if (v.getId() == R.id.btn_login_fb) {
            loginFacebook();
        }
    }

    private void loginFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, facebookCallback());
    }

    private FacebookCallback<LoginResult> facebookCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }
            @Override
            public void onError(FacebookException error) {Log.d(TAG, "facebook:onError", error);}
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener((Executor) this, task -> {
            if (task.isSuccessful()) {
                callBack.showFragment(MenuFragment.TAG, null, false);
            } else {
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        launcher.launch(signInIntent);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, task -> {
                    setDialog(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        callBack.showFragment(MenuFragment.TAG, null, false);
                    } else {
                        Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void login() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassWord.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập Email/password để đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        setDialog(true);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, task -> {
            setDialog(false);
            if (task.isSuccessful()) {
                callBack.showFragment(MenuFragment.TAG, null, false);
            } else {
                Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected FragmentLoginBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentLoginBinding.inflate(inflater, container, false);
    }
}

