package com.clk.quanlichitieu.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.clk.quanlichitieu.dao.entities.KhoanThu;

import java.util.List;

public class KhoanThuViewModel extends BaseViewModel {
    private final MutableLiveData<List<KhoanThu>> listKhoanThu = new MutableLiveData<>();


    public MutableLiveData<List<KhoanThu>> getListKhoanThu() {
        return listKhoanThu;
    }
}
