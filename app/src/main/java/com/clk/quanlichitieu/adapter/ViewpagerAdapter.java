package com.clk.quanlichitieu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.clk.quanlichitieu.Model.IntroModel;
import com.clk.quanlichitieu.R;

import java.util.ArrayList;

public class ViewpagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<IntroModel> dataintro;

    public ViewpagerAdapter(Context context, ArrayList<IntroModel> dataintro) {
        this.context = context;
        this.dataintro = dataintro;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.introitem, null);
        TextView tilte = view.findViewById(R.id.intro_title);
        TextView intro = view.findViewById(R.id.intro_description);
        ImageView img = view.findViewById(R.id.imgintro);
        tilte.setText(dataintro.get(position).getTilte());
        intro.setText(dataintro.get(position).getLoinoi());
        img.setImageResource(dataintro.get(position).getHinh());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return dataintro.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
