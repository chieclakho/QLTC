package com.clk.quanlichitieu.view.fragment.thongke;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.thongke.ThongKeAdapter;
import com.clk.quanlichitieu.databinding.FragmentThongKeBinding;
import com.clk.quanlichitieu.view.fragment.BaseFragment;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.android.material.tabs.TabLayoutMediator;

public class ThongkeFragment extends BaseFragment<FragmentThongKeBinding, CommonVM> {
    public static final String TAG = ThongkeFragment.class.getName();

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {
        binding.menu.ivMenu.setImageResource(R.drawable.ic_back);
        binding.menu.tvTitle.setText(R.string.txt_tk);
        binding.menu.ivMenu.setOnClickListener(this);
        initTablayout();
    }

    private void initTablayout() {
        ThongKeAdapter adapter = new ThongKeAdapter((FragmentActivity) context);
        binding.viewPageThongKe.setAdapter(adapter);
        binding.tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        new TabLayoutMediator(binding.tabLayout, binding.viewPageThongKe, (tab, position) -> {
            if (position == 0) {
                tab.setText("Thống Kê Chi");
            } else if (position == 1) {
                tab.setText("Thống Kê Thu");
            }
        }).attach();
    }

    protected void clickView(View v) {
        if (v.getId() == R.id.iv_menu) {
            callBack.backToPrevious();
        }
    }

    @Override
    protected FragmentThongKeBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentThongKeBinding.inflate(inflater, container, false);
    }
}
