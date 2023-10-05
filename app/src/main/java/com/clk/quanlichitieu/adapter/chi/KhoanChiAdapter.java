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
import com.clk.quanlichitieu.dao.entities.KhoanChi;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class KhoanChiAdapter extends RecyclerView.Adapter<KhoanChiAdapter.KhoanChiViewHolder> {
    private final Context context;
    private List<KhoanChi> listKhoanChi;
    private IClickOnItem iClickOnItem;

    public void setData(List<KhoanChi> khoanChiList) {
        this.listKhoanChi = khoanChiList;
    }

    public interface IClickOnItem {
        void update(KhoanChi khoanChi);

        void delete(KhoanChi khoanChi);
    }

    public KhoanChiAdapter(Context context, List<KhoanChi> listKhoanChi, IClickOnItem iClickOnItem) {
        this.context = context;
        this.listKhoanChi = listKhoanChi;
        this.iClickOnItem = iClickOnItem;
    }
    public KhoanChiAdapter(Context context, List<KhoanChi> listKhoanChi) {
        this.context = context;
        this.listKhoanChi = listKhoanChi;
    }

    @NonNull
    @Override
    public KhoanChiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khoan_chi, parent, false);
        return new KhoanChiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhoanChiViewHolder holder, int position) {
        KhoanChi khoanChi = listKhoanChi.get(position);
        holder.tvName.setText(String.format("Khoản chi: %s", khoanChi.tenKhoanChi));
        holder.tvSoTien.setText(String.format("Số tiền: %s", initMoney(khoanChi.soTien)));
        holder.tvDate.setText(String.format("Ngày chi: %s", khoanChi.ngayChi));
        holder.tvNote.setText(String.format("Ghi chú: %s", khoanChi.note));
        holder.tvMaLoai.setText(String.format("Loại khoản chi: %s", khoanChi.tenLoai));

        holder.ivUpdate.setOnClickListener(v -> iClickOnItem.update(khoanChi));
        holder.ivDelete.setOnClickListener(v -> iClickOnItem.delete(khoanChi));
    }

    private String initMoney(long money) {
        try {
            Locale locale = new Locale("vi", "VN");
            Currency currency = Currency.getInstance("VND");
            DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
            df.setCurrency(currency);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            numberFormat.setCurrency(currency);
            return numberFormat.format(money);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        if (listKhoanChi != null) {
            return listKhoanChi.size();
        }
        return 0;
    }

    public static class KhoanChiViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvSoTien, tvMaLoai, tvNote;
        ImageView ivDelete, ivUpdate;

        public KhoanChiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_kc);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvSoTien = itemView.findViewById(R.id.tv_money);
            tvMaLoai = itemView.findViewById(R.id.tv_ten_loai);
            tvNote = itemView.findViewById(R.id.tv_note);

            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivUpdate = itemView.findViewById(R.id.iv_update);

        }
    }
}
