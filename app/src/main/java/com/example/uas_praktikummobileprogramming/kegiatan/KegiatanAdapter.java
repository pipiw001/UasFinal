package com.example.uas_praktikummobileprogramming.kegiatan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.dashboard.DetailActivity;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class KegiatanAdapter extends RecyclerView.Adapter<KegiatanAdapter.ViewHolder> {

    private Context context;
    private List<Kegiatan> list;

    public KegiatanAdapter(Context context, List<Kegiatan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kegiatan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kegiatan kegiatan = list.get(position);

        holder.judul.setText(kegiatan.getJudul());

        // Format tanggal dari Timestamp ke String
        Timestamp waktu = kegiatan.getTanggal();
        String tanggalFormatted = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(waktu.toDate());
        holder.tanggal.setText(tanggalFormatted);

        Glide.with(context).load(kegiatan.getGambar()).into(holder.gambar);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id_kegiatan", kegiatan.getId());
            intent.putExtra("jenis", kegiatan.getJenis());
            intent.putExtra("judul", kegiatan.getJudul());
            intent.putExtra("deskripsi", kegiatan.getDeskripsi());
            intent.putExtra("gambar", kegiatan.getGambar());
            intent.putExtra("lokasi", kegiatan.getLokasi());
            intent.putExtra("kuota", kegiatan.getKuota());
            intent.putExtra("tanggal", tanggalFormatted);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ✅ Method untuk memperbarui data berdasarkan hasil pencarian
    public void setFilteredList(List<Kegiatan> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView judul, tanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.imgKegiatan);
            judul = itemView.findViewById(R.id.judulKegiatan);
            tanggal = itemView.findViewById(R.id.txtTanggalKegiatan);
        }
    }
}
