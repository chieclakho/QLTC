package com.clk.quanlichitieu.adapter.chi;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.clk.quanlichitieu.view.fragment.chi.KhoanChiFragment;
import com.clk.quanlichitieu.view.fragment.chi.LoaiChiFragment;


public class ChiViewPagerAdapter extends FragmentStateAdapter {
    public ChiViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new KhoanChiFragment();
        } else {
            fragment = new LoaiChiFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
