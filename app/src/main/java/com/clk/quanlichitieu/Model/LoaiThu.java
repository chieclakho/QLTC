package com.clk.quanlichitieu.Model;

public class LoaiThu {
    private int id;
    private String tenLoai;

    public LoaiThu() {
    }

    public LoaiThu(int id, String tenLoai) {
        this.id = id;
        this.tenLoai = tenLoai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}
