package com.clk.quanlichitieu.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.clk.quanlichitieu.App;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.databinding.FragmentMyprofileBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateProfileFrg extends BaseFragment<FragmentMyprofileBinding, CommonVM> {
    public static final String TAG = UpdateProfileFrg.class.getName();
    private FirebaseUser user;
    private ActivityResultLauncher<String> imagePickerLauncher;


    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            binding.edtEmail.setText(user.getEmail());
            binding.edtFullName.setText(user.getDisplayName());
            Glide.with(context).load(user.getPhotoUrl()).into(binding.ivAvt);
        }
        binding.menu.ivMenu.setImageResource(R.drawable.ic_back);
        binding.menu.tvTitle.setText(R.string.txt_my_profile);
        binding.menu.ivMenu.setOnClickListener(this);
        binding.btnUpdateProfile.setOnClickListener(this);
        binding.btnUpdateEmail.setOnClickListener(this);
        binding.ivAvt.setOnClickListener(this);
        initLauncher();
        onClickRequestPermission();

    }

    private void initLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), uri -> {
                    if (uri != null) {
                        Glide.with(context).load(uri).into(binding.ivAvt);
                        App.getInstance().getStorage().setUri(uri);
                    }
                }
        );
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.iv_avt) {
            imagePickerLauncher.launch("image/*");
        } else if (v.getId() == R.id.btn_update_profile) {
            updateProfile();
        } else if (v.getId() == R.id.btn_update_email) {
            updatePEmail();
        } else if (v.getId() == R.id.iv_menu) {
            callBack.backToPrevious();
        }
    }

    private void updatePEmail() {
        String email = binding.edtEmail.getText().toString().trim();
        user.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "User email address updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Email updated failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateProfile() {
        String fullName = binding.edtFullName.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .setPhotoUri(App.getInstance().getStorage().uriAvt)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Update profile successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Update profile failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onClickRequestPermission() {
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }

    @Override
    protected FragmentMyprofileBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMyprofileBinding.inflate(inflater, container, false);
    }
}
