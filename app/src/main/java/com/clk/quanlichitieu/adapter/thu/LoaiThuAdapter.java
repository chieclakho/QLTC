package com.clk.quanlichitieu.adapter.thu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.dao.entities.LoaiThu;

import java.util.List;

public class LoaiThuAdapter extends RecyclerView.Adapter<LoaiThuAdapter.LoaiThuViewHolder> {
    private final Context context;
    private List<LoaiThu> listLoaiThu;
    private IClickOnItem iClickOnItem;

    public void setData(List<LoaiThu> listLoaiThu) {
        this.listLoaiThu = listLoaiThu;
    }

    public interface IClickOnItem {
        void update(LoaiThu loaiThu);

        void delete(LoaiThu loaiThu);
    }

    public LoaiThuAdapter(Context context, List<LoaiThu> listLoaiThu, IClickOnItem iClickOnItem) {
        this.context = context;
        this.listLoaiThu = listLoaiThu;
        this.iClickOnItem = iClickOnItem;
    }
    public void removeLoaiThu(LoaiThu loaiThu) {
        listLoaiThu.remove(loaiThu);
    }

    @NonNull
    @Override
    public LoaiThuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loai_thu, parent, false);
        return new LoaiThuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiThuViewHolder holder, int position) {
        LoaiThu loaiThu = listLoaiThu.get(position);
        holder.tvName.setText(String.format("Loại chi: %s", loaiThu.tenLoai));
        holder.ivUpdate.setOnClickListener(v -> iClickOnItem.update(loaiThu));
        holder.ivDelete.setOnClickListener(v -> iClickOnItem.delete(loaiThu));
    }

    @Override
    public int getItemCount() {
        if (listLoaiThu != null) {
            return listLoaiThu.size();
        }
        return 0;
    }

    public static class LoaiThuViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDelete, ivUpdate;
        TextView tvName;

        public LoaiThuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDelete = itemView.findViewById(R.id.iv_delete_loai_thu);
            ivUpdate = itemView.findViewById(R.id.iv_edit_loai_thu);
            tvName = itemView.findViewById(R.id.tv_loai_thu);
        }
    }
}
