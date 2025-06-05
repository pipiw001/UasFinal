package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.notifikasi.NotifikasiActivity;
import com.example.uas_praktikummobileprogramming.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class KegiatanSayaActivity extends AppCompatActivity {

    private CardView card1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan_saya);

        card1 = findViewById(R.id.card_1);

        card1.setOnClickListener(v -> {
            // Kirim data ke DetailActivity (opsional)
            Intent intent = new Intent(KegiatanSayaActivity.this, DetailActivity.class);
            intent.putExtra("judul", "Judul Berita 1");
            intent.putExtra("subjudul", "Subjudul Berita 1");
            intent.putExtra("isi", "Ini adalah isi lengkap dari Berita 1.");
            startActivity(intent);
        });

        // Inisialisasi BottomNavigationView dan set listener
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_kegiatan);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0,0);
                return true;
            } else if (id == R.id.nav_kegiatan) {
                // Sudah di kegiatan, tidak perlu pindah
                return true;
            } else if (id == R.id.nav_notifikasi) {
                startActivity(new Intent(this, NotifikasiActivity.class));
                overridePendingTransition(0,0);
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });
    }
}
