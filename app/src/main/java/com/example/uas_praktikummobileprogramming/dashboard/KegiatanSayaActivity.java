package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.kegiatan.Kegiatan;
import com.example.uas_praktikummobileprogramming.kegiatan.KegiatanAdapter;
import com.example.uas_praktikummobileprogramming.notifikasi.NotifikasiActivity;
import com.example.uas_praktikummobileprogramming.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class KegiatanSayaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Kegiatan> kegiatanList;
    private KegiatanAdapter adapter;

    private FirebaseFirestore db;
    private String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan_saya);

        // Ambil email dari FirebaseAuth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailUser = user.getEmail();
        }

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewKegiatanSaya);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        kegiatanList = new ArrayList<>();
        adapter = new KegiatanAdapter(this, kegiatanList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Ambil kegiatan user dari seminar & lomba
        ambilPendaftaranUser("seminar");
        ambilPendaftaranUser("lomba");

        // Bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_kegiatan);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_kegiatan) {
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

    private void ambilPendaftaranUser(String jenis) {
        db.collection("pendaftaran_" + jenis)
                .whereEqualTo("email", emailUser)
                .get()
                .addOnSuccessListener(docs -> {
                    for (DocumentSnapshot doc : docs) {
                        String idKegiatan = doc.getString("id_kegiatan");

                        // Ambil detail kegiatan
                        db.collection(jenis)
                                .document(idKegiatan)
                                .get()
                                .addOnSuccessListener(kegiatanDoc -> {
                                    if (kegiatanDoc.exists()) {
                                        Kegiatan kegiatan = kegiatanDoc.toObject(Kegiatan.class);
                                        kegiatan.setId(kegiatanDoc.getId());
                                        kegiatan.setJenis(jenis); // bisa dipakai untuk info tambahan
                                        kegiatanList.add(kegiatan);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }
}
