package com.clk.quanlichitieu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.clk.quanlichitieu.OnMainCallBack;
import com.clk.quanlichitieu.viewmodel.BaseViewModel;


public abstract class BaseFragment<T extends ViewBinding, VM extends BaseViewModel> extends Fragment implements View.OnClickListener {
    protected T binding;
    protected VM model;
    protected Object data;
    protected Context context;
    protected OnMainCallBack callBack;

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setCallBack(OnMainCallBack callBack) {
        this.callBack = callBack;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public final void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = initViewBinding(inflater, container);
        model = new ViewModelProvider(this).get(initClassModel());
        initViews();
        return binding.getRoot();
    }

    protected abstract Class<VM> initClassModel();


    protected abstract void initViews();

    protected abstract T initViewBinding(LayoutInflater inflater, ViewGroup container);


    @Override
    public final void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
        clickView(v);
    }

    protected void clickView(View v) {
        // do Nothing
    }
}
