package com.clk.quanlichitieu.view.fragment.chi;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;


import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.chi.ChiViewPagerAdapter;
import com.clk.quanlichitieu.databinding.FragmentChiBinding;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChiFragment extends BaseFragment<FragmentChiBinding, CommonVM> {
    public static final String TAG = ChiFragment.class.getName();

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        binding.menu.ivMenu.setImageResource(R.drawable.ic_back);
        binding.menu.ivMenu.setOnClickListener(this);
        initAdapter();
    }

    private void initAdapter() {
        ChiViewPagerAdapter adapter = new ChiViewPagerAdapter((FragmentActivity) context);
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Khoản Chi");
            } else {
                tab.setText("Loại Khoản Chi");
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
    protected FragmentChiBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentChiBinding.inflate(inflater, container, false);
    }
}
