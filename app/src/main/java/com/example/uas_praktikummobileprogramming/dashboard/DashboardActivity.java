package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        ambilDataKegiatanGabungan();

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

    private void ambilDataKegiatanGabungan() {
        kegiatanList.clear();

        db.collection("lomba")
                .get()
                .addOnSuccessListener(lombaSnapshots -> {
                    for (DocumentSnapshot doc : lombaSnapshots) {
                        Kegiatan kegiatan = doc.toObject(Kegiatan.class);
                        kegiatan.setId(doc.getId());
                        kegiatanList.add(kegiatan);
                    }

                    // Setelah ambil data lomba, lanjut ambil seminar
                    db.collection("seminar")
                            .get()
                            .addOnSuccessListener(seminarSnapshots -> {
                                for (DocumentSnapshot doc : seminarSnapshots) {
                                    Kegiatan kegiatan = doc.toObject(Kegiatan.class);
                                    kegiatan.setId(doc.getId());
                                    kegiatanList.add(kegiatan);
                                }

                                adapter.notifyDataSetChanged(); // Refresh setelah semua data masuk
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(DashboardActivity.this, "Gagal mengambil data seminar", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DashboardActivity.this, "Gagal mengambil data lomba", Toast.LENGTH_SHORT).show();
                });
    }
}