package com.clk.quanlichitieu.dao.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LoaiChi {

    @PrimaryKey()
    @ColumnInfo(name = "tenLoai")
    public String tenLoai;


    @ColumnInfo(name = "ducumnetId")
    public String ducumnetId;

    public LoaiChi() {

    }

    public LoaiChi(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}
