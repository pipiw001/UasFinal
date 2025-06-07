package com.example.uas_praktikummobileprogramming.notifikasi;

import com.google.firebase.Timestamp;

public class NotifikasiModel {
    private String judul;
    private String jenis;
    private Timestamp timestamp;

    public NotifikasiModel() {
        // Diperlukan untuk Firestore
    }

    public String getJudul() {
        return judul;
    }

    public String getJenis() {
        return jenis;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}