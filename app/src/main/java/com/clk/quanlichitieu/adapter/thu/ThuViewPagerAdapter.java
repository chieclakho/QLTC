package com.clk.quanlichitieu.adapter.thu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.clk.quanlichitieu.view.fragment.thu.KhoanThuFragment;
import com.clk.quanlichitieu.view.fragment.thu.LoaiThuFragment;


public class ThuViewPagerAdapter extends FragmentStateAdapter {
    public ThuViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new KhoanThuFragment();
        } else {
            fragment = new LoaiThuFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
