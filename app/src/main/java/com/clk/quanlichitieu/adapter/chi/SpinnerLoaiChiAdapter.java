package com.clk.quanlichitieu.adapter.chi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.clk.quanlichitieu.dao.entities.LoaiChi;

import java.util.List;

public class SpinnerLoaiChiAdapter extends ArrayAdapter<LoaiChi> {
    List<LoaiChi> dataList;

    public SpinnerLoaiChiAdapter(@NonNull Context context, int resource, @NonNull List<LoaiChi> dataList) {
        super(context, resource, dataList);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        LoaiChi item = dataList.get(position);
        textView.setText(item.tenLoai);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        LoaiChi item = dataList.get(position);
        textView.setText(item.tenLoai);
        return textView;
    }
}
