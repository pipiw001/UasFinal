package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.kegiatan.Kegiatan;
import com.example.uas_praktikummobileprogramming.kegiatan.KegiatanAdapter;
import com.example.uas_praktikummobileprogramming.notifikasi.NotifikasiActivity;
import com.example.uas_praktikummobileprogramming.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        // Inisialisasi RecyclerView dan adapter
        recyclerView = findViewById(R.id.recyclerViewKegiatan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        kegiatanList = new ArrayList<>();
        adapter = new KegiatanAdapter(this, kegiatanList);
        recyclerView.setAdapter(adapter);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();
        ambilDataDariFirestore("lomba"); // ambil data koleksi "lomba"

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_dashboard);

        // Pasang listener untuk navigasi
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                // Sudah di dashboard, tidak perlu pindah
                return true;
            } else if (id == R.id.nav_kegiatan) {
                startActivity(new Intent(this, KegiatanSayaActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_notifikasi) {
                startActivity(new Intent(this, NotifikasiActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void ambilDataDariFirestore(String koleksi) {
        db.collection(koleksi)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    kegiatanList.clear();
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
