package com.clk.quanlichitieu.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.clk.quanlichitieu.dao.entities.KhoanChi;

import java.util.List;

public class KhoanChiViewModel extends BaseViewModel {

    private final MutableLiveData<List<KhoanChi>> listKhoanChi = new MutableLiveData<>();

    public MutableLiveData<List<KhoanChi>> getListKhoanChi() {
        return listKhoanChi;
    }
}
