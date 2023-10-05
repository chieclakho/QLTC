package com.clk.quanlichitieu.dao.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LoaiThu {

    @PrimaryKey()
    @ColumnInfo(name = "tenLoai")
    public String tenLoai;

    @ColumnInfo(name = "ducumnetId")
    public String ducumnetId;
    public LoaiThu() {
    }

    public LoaiThu(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}
