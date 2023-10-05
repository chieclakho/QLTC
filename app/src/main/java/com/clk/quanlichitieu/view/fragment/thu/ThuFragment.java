package com.clk.quanlichitieu.view.fragment.thu;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.thu.ThuViewPagerAdapter;
import com.clk.quanlichitieu.databinding.FragmentThuBinding;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.android.material.tabs.TabLayoutMediator;

public class ThuFragment extends BaseFragment<FragmentThuBinding, CommonVM> {
    public static final String TAG = ThuFragment.class.getName();

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        binding.menu.ivMenu.setImageResource(R.drawable.ic_back);
        binding.menu.tvTitle.setText(R.string.txt_khoan_thu);
        binding.menu.ivMenu.setOnClickListener(this);
        initAdapter();
    }
    private void initAdapter() {
        ThuViewPagerAdapter adapter = new ThuViewPagerAdapter((FragmentActivity) context);
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Khoản Thu");
            } else {
                tab.setText("Loại Khoản Thu");
            }
        }).attach();
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.iv_menu) {
            callBack.backToPrevious();
        }
    }

    @Override
    protected FragmentThuBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentThuBinding.inflate(inflater, container, false);
    }
}
