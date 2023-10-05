package com.clk.quanlichitieu.adapter.thu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.clk.quanlichitieu.dao.entities.LoaiThu;

import java.util.List;

public class SpinnerLoaiThuAdapter extends ArrayAdapter<LoaiThu> {
private final List<LoaiThu> dataList;

    public SpinnerLoaiThuAdapter(@NonNull Context context, int resource, @NonNull List<LoaiThu> dataList) {
        super(context, resource, dataList);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        LoaiThu item = dataList.get(position);
        textView.setText(item.tenLoai);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        LoaiThu item = dataList.get(position);
        textView.setText(item.tenLoai);
        return textView;
    }
}
