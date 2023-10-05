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
import com.clk.quanlichitieu.dao.entities.KhoanThu;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class KhoanThuAdapter extends RecyclerView.Adapter<KhoanThuAdapter.KhoanThuViewHolder> {
    private final Context context;
    private List<KhoanThu> listKhoanThu;
    private IClickOnItem iClickOnItem;

    public void setData(List<KhoanThu> listKhoanThu) {
        this.listKhoanThu = listKhoanThu;
    }

    public interface IClickOnItem {
        void update(KhoanThu khoanThu);

        void delete(KhoanThu khoanThu);
    }

    public KhoanThuAdapter(Context context, List<KhoanThu> listKhoanThu, IClickOnItem iClickOnItem) {
        this.context = context;
        this.listKhoanThu = listKhoanThu;
        this.iClickOnItem = iClickOnItem;
    }

    public KhoanThuAdapter(Context context, List<KhoanThu> listKhoanThu) {
        this.context = context;
        this.listKhoanThu = listKhoanThu;
    }

    @NonNull
    @Override
    public KhoanThuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khoan_thu, parent, false);
        return new KhoanThuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhoanThuViewHolder holder, int position) {
        KhoanThu khoanThu = listKhoanThu.get(position);
        holder.tvName.setText(String.format("Khoản thu: %s", khoanThu.tenKhoanThu));
        holder.tvSoTien.setText(String.format("Số tiền: %s", initMoney(khoanThu.soTien)));
        holder.tvDate.setText(String.format("Ngày thu: %s", khoanThu.ngayThu));
        holder.tvNote.setText(String.format("Ghi chú: %s", khoanThu.note));
        holder.tvMaLoai.setText(String.format("Loại khoản thu: %s", khoanThu.tenLoai));

        holder.ivUpdate.setOnClickListener(v -> iClickOnItem.update(khoanThu));
        holder.ivDelete.setOnClickListener(v -> iClickOnItem.delete(khoanThu));
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
        if (listKhoanThu != null) {
            return listKhoanThu.size();
        }
        return 0;
    }

    public static class KhoanThuViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvSoTien, tvMaLoai, tvNote;
        ImageView ivDelete, ivUpdate;

        public KhoanThuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_kt);
            tvDate = itemView.findViewById(R.id.tv_date_kt);
            tvSoTien = itemView.findViewById(R.id.tv_money_kt);
            tvMaLoai = itemView.findViewById(R.id.tv_khoan_thu);
            tvNote = itemView.findViewById(R.id.tv_note_kt);

            ivDelete = itemView.findViewById(R.id.iv_delete_kt);
            ivUpdate = itemView.findViewById(R.id.iv_update_kt);

        }
    }
}
