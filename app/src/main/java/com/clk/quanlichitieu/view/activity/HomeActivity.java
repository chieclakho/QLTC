package com.clk.quanlichitieu.view.activity;



import com.clk.quanlichitieu.CommonUtils;
import com.clk.quanlichitieu.databinding.ActivityHomeBinding;
import com.clk.quanlichitieu.view.fragment.AboutFragment;
import com.clk.quanlichitieu.view.fragment.LoginFragment;
import com.clk.quanlichitieu.view.fragment.MenuFragment;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, CommonVM> {
    private static final String TAG = HomeActivity.class.getName();

    @Override
    protected ActivityHomeBinding initViewBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<CommonVM> initModelClass() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        String about = CommonUtils.getInstance().getPref("ABOUT");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (about == null) {
            showFragment(AboutFragment.TAG, null, false);
        } else if (user == null) {
            showFragment(LoginFragment.TAG, null, false);
        } else {
            showFragment(MenuFragment.TAG, null, false);
        }
    }

    @Override
    public void backToPrevious() {
        super.onBackPressed();
    }
}