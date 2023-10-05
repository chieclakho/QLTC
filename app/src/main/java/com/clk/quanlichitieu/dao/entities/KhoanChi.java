package com.clk.quanlichitieu.dao.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class KhoanChi {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "ducumnetId")
    public  String ducumnetId;
    @ColumnInfo(name = "tenKhoanChi")
    public String tenKhoanChi;

    @ColumnInfo(name = "soTien")
    public Long soTien;

    @ColumnInfo(name = "ngayChi")
    public String ngayChi;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "tenLoai")
    public String tenLoai;
    @ColumnInfo(name = "idLoai")
    public int idLoai;
    @ColumnInfo(name = "LOAIKHOANCHI")
    public int loaiKhoanChi;
}
