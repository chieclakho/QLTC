package com.clk.quanlichitieu.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.clk.quanlichitieu.Model.IntroModel;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.adapter.ViewpagerAdapter;
import com.clk.quanlichitieu.databinding.FragmentAboutBinding;
import com.clk.quanlichitieu.viewmodel.CommonVM;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AboutFragment extends BaseFragment<FragmentAboutBinding, CommonVM> {
    public static final String TAG = AboutFragment.class.getName();
    ArrayList<IntroModel> dataintro;

    @Override
    protected Class<CommonVM> initClassModel() {
        return CommonVM.class;
    }

    @Override
    protected void initViews() {

        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.white));
            dataintro = new ArrayList<>();
            dataintro.add(new IntroModel("Quản Lí Khoản Thu", " Có phải bạn luôn tự hỏi tiền đã thu bao nhiêu?\n \uD83D\uDC49App sẽ quản lí số tiền mà bạn đã thu lại", R.drawable.intro1));
            dataintro.add(new IntroModel("Quản Lí Khoản Chi", " Bạn luôn hỏi: tiền lương bay đi đâu hết rồi?\n \uD83D\uDC49App sẽ quản lí số tiền mà bạn đã chi ra", R.drawable.intro2));
            dataintro.add(new IntroModel("Thống Kê ", " Bạn đau đầu với việc cân đối thu chi trong tháng?\n \uD83D\uDC49App sẽ thống kê lại cả khoản thu và chi giúp bạn", R.drawable.intro3));
            ViewpagerAdapter adapter = new ViewpagerAdapter(context, dataintro);
            binding.screenViewpager.setAdapter(adapter);
            binding.tabLayout.setupWithViewPager(binding.screenViewpager);
            binding.tvNext.setOnClickListener(this);
            binding.btnBegin.setOnClickListener(this);
            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == dataintro.size() - 1) {
                        binding.tvNext.setVisibility(View.INVISIBLE);
                    } else {
                        binding.tvNext.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.tv_next) {
            int position = binding.screenViewpager.getCurrentItem();
            if (position < dataintro.size() - 1) {
                position++;
                binding.screenViewpager.setCurrentItem(position);
                binding.tvNext.setVisibility(View.VISIBLE);
            }
            if (position == dataintro.size() - 1) {
                binding.tvNext.setVisibility(View.INVISIBLE);
            }
        } else if (v.getId() == R.id.btn_begin) {
            callBack.showFragment(LoginFragment.TAG, null, false);
        }
    }

    @Override
    protected FragmentAboutBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAboutBinding.inflate(inflater, container, false);
    }
}
