package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.kegiatan.Kegiatan;
import com.example.uas_praktikummobileprogramming.kegiatan.KegiatanAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private KegiatanAdapter adapter;
    private List<Kegiatan> kegiatanList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recyclerViewKegiatan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        kegiatanList = new ArrayList<>();
        adapter = new KegiatanAdapter(this, kegiatanList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Ubah ini ke "berita", "seminar", atau "lomba" sesuai kebutuhan
        ambilDataDariFirestore("lomba");
    }

    private void ambilDataDariFirestore(String koleksi) {
        db.collection(koleksi)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    kegiatanList.clear(); // Bersihkan list sebelumnya
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Kegiatan kegiatan = doc.toObject(Kegiatan.class);
                        kegiatanList.add(kegiatan);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DashboardActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                });
    }
}