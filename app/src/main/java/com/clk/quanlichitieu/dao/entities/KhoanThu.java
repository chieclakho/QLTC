package com.clk.quanlichitieu.dao.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class KhoanThu {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "ducumnetId")
    public  String ducumnetId;
     @ColumnInfo(name = "tenKhoanThu")
    public String tenKhoanThu;

    @ColumnInfo(name = "soTien")
    public Long soTien;

    @ColumnInfo(name = "ngayThu")
    public String ngayThu;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "tenLoai")
    public String tenLoai;
    @ColumnInfo(name = "idLoai")
    public int idLoai;
    @ColumnInfo(name = "LOAIKHOANTHU")
    public int loaiKhoanThu;
}
