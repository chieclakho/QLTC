package com.clk.quanlichitieu;

import android.net.Uri;

import com.clk.quanlichitieu.dao.entities.KhoanChi;
import com.clk.quanlichitieu.dao.entities.KhoanThu;
import com.clk.quanlichitieu.dao.entities.LoaiChi;
import com.clk.quanlichitieu.dao.entities.LoaiThu;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Storage {
    public Uri uriAvt;
    private LoaiChi loaiChi;
    private LoaiThu loaiThu;
    private KhoanChi khoanChi;
    private KhoanThu khoanThu;
    private GoogleSignInAccount account;

    public void setUri(Uri uri) {
        this.uriAvt = uri;
    }

    public void setLoaiChi(LoaiChi loaiChi) {
        this.loaiChi = loaiChi;
    }

    public LoaiChi getLoaiChi() {
        return loaiChi;
    }

    public void setKhoanChi(KhoanChi khoanChi) {
        this.khoanChi = khoanChi;
    }

    public KhoanChi getKhoanChi() {
        return khoanChi;
    }

    public LoaiThu getLoaiThu() {
        return loaiThu;
    }

    public void setLoaiThu(LoaiThu loaiThu) {
        this.loaiThu = loaiThu;
    }

    public KhoanThu getKhoanThu() {
        return khoanThu;
    }

    public void setKhoanThu(KhoanThu khoanThu) {
        this.khoanThu = khoanThu;
    }

    public void setAccount(GoogleSignInAccount account) {
        this.account = account;
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }
}
