package com.clk.quanlichitieu.adapter.chi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.dao.entities.LoaiChi;

import java.util.List;

public class LoaiChiAdapter extends RecyclerView.Adapter<LoaiChiAdapter.LoaiChiViewHolder> {
    private final Context context;
    private List<LoaiChi> listLoaiChi;
    private IClickOnItem iClickOnItem;

    public void setData(List<LoaiChi> listLoaiChi) {
        this.listLoaiChi = listLoaiChi;
    }

    public interface IClickOnItem {
        void update(LoaiChi loaiChi);

        void delete(LoaiChi loaiChi);
    }

    public LoaiChiAdapter(Context context, List<LoaiChi> listLoaiChi, IClickOnItem iClickOnItem) {
        this.context = context;
        this.listLoaiChi = listLoaiChi;
        this.iClickOnItem = iClickOnItem;
    }
    public void removeLoaiChi(LoaiChi loaiChi) {
        listLoaiChi.remove(loaiChi);
    }

    @NonNull
    @Override
    public LoaiChiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loai_chi, parent, false);
        return new LoaiChiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiChiViewHolder holder, int position) {
        LoaiChi loaiChi = listLoaiChi.get(position);
        holder.tvName.setText(String.format("Loại chi: %s", loaiChi.tenLoai));
        holder.ivUpdate.setOnClickListener(v -> iClickOnItem.update(loaiChi));
        holder.ivDelete.setOnClickListener(v -> iClickOnItem.delete(loaiChi));
    }

    @Override
    public int getItemCount() {
        if (listLoaiChi != null) {
            return listLoaiChi.size();
        }
        return 0;
    }

    public static class LoaiChiViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDelete, ivUpdate;
        TextView tvName;

        public LoaiChiViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDelete = itemView.findViewById(R.id.iv_delete_loai_chi);
            ivUpdate = itemView.findViewById(R.id.iv_edit_loai_chi);
            tvName = itemView.findViewById(R.id.tv_loai_chi);
        }
    }
}
