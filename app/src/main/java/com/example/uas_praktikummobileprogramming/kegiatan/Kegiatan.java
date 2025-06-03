package com.example.uas_praktikummobileprogramming.kegiatan;

import com.google.firebase.Timestamp;

public class Kegiatan {
    private String judul, deskripsi, gambar, lokasi;
    private int kuota;
    private Timestamp tanggal;

    public Kegiatan() {} // Diperlukan untuk Firebase

    public Kegiatan(String judul, String deskripsi, String gambar, String lokasi, int kuota, Timestamp tanggal) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
        this.kuota = kuota;
    }

    // Getter
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public String getGambar() { return gambar; }
    public String getLokasi() { return lokasi; }
    public int getKuota() { return kuota; }
    public Timestamp getTanggal() { return tanggal; }
}
