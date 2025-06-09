package com.example.uas_praktikummobileprogramming.notifikasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_praktikummobileprogramming.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private List<NotifikasiModel> list;

    public NotifikasiAdapter(List<NotifikasiModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifikasi, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotifikasiModel notif = list.get(position);
        holder.judul.setText(notif.getJudul());
        holder.jenis.setText("Jenis: " + notif.getJenis());

        if (notif.getTimestamp() != null) {
            Date date = notif.getTimestamp().toDate();
            holder.waktu.setText(DateFormat.getDateTimeInstance().format(date));
        } else {
            holder.waktu.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul, jenis, waktu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.textJudulNotifikasi);
            jenis = itemView.findViewById(R.id.textJenisNotifikasi);
            waktu = itemView.findViewById(R.id.textWaktuNotifikasi);
        }
    }
}