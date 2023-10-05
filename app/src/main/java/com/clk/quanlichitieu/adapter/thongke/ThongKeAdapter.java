package com.clk.quanlichitieu.adapter.thongke;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.clk.quanlichitieu.view.fragment.thongke.ThongKeChiFragment;
import com.clk.quanlichitieu.view.fragment.thongke.ThongKeThuFragment;


public class ThongKeAdapter extends FragmentStateAdapter {
    public ThongKeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new ThongKeChiFragment();
        } else {
            fragment = new ThongKeThuFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
