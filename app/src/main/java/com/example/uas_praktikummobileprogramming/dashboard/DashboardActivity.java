package com.example.uas_praktikummobileprogramming.dashboard;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.kegiatan.Kegiatan;
import com.example.uas_praktikummobileprogramming.kegiatan.KegiatanAdapter;
import com.example.uas_praktikummobileprogramming.notifikasi.NotifikasiActivity;
import com.example.uas_praktikummobileprogramming.notifikasi.NotifikasiHelper;
import com.example.uas_praktikummobileprogramming.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private KegiatanAdapter adapter;
    private List<Kegiatan> kegiatanList;
    private FirebaseFirestore db;
    private ListenerRegistration seminarListener;
    private ListenerRegistration lombaListener;
    private Set<String> idNotifikasiTerkirim = new HashSet<>();
    private NotifikasiHelper notifikasiHelper;

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

        notifikasiHelper = new NotifikasiHelper(this);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();
        ambilDataKegiatanGabungan();
        pantauKegiatanBaru("seminar");
        pantauKegiatanBaru("lomba");

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Kegiatan Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifikasi untuk kegiatan baru");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void ambilDataKegiatanGabungan() {
        kegiatanList.clear();

        db.collection("lomba")
                .get()
                .addOnSuccessListener(lombaSnapshots -> {
                    for (DocumentSnapshot doc : lombaSnapshots) {
                        Kegiatan kegiatan = doc.toObject(Kegiatan.class);
                        kegiatan.setId(doc.getId());
                        kegiatan.setJenis("lomba");
                        kegiatanList.add(kegiatan);
                    }

                    // Setelah ambil data lomba, lanjut ambil seminar
                    db.collection("seminar")
                            .get()
                            .addOnSuccessListener(seminarSnapshots -> {
                                for (DocumentSnapshot doc : seminarSnapshots) {
                                    Kegiatan kegiatan = doc.toObject(Kegiatan.class);
                                    kegiatan.setId(doc.getId());
                                    kegiatan.setJenis("seminar");
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

    private void pantauKegiatanBaru(String jenis) {
        ListenerRegistration listener = db.collection(jenis)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String idDoc = dc.getDocument().getId();

                            if (!idNotifikasiTerkirim.contains(idDoc)) {
                                idNotifikasiTerkirim.add(idDoc);

                                Kegiatan kegiatan = dc.getDocument().toObject(Kegiatan.class);
                                kegiatan.setId(idDoc);
                                kegiatan.setJenis(jenis);

                                simpanNotifikasiInbox(kegiatan);
                                notifikasiHelper.tampilkanNotifikasi("Kegiatan Baru: " + jenis, kegiatan.getJudul());
                            }
                        }
                    }
                });

        // Simpan listener supaya bisa dihapus nanti
        if (jenis.equals("seminar")) {
            seminarListener = listener;
        } else if (jenis.equals("lomba")) {
            lombaListener = listener;
        }


    }

    private void simpanNotifikasiInbox(Kegiatan kegiatan) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("judul", kegiatan.getJudul());
        data.put("jenis", kegiatan.getJenis());
        data.put("timestamp", FieldValue.serverTimestamp());

        db.collection("notifikasi_user")
                .document(user.getUid())
                .collection("inbox")
                .document(kegiatan.getId()) // gunakan ID kegiatan agar tidak duplikat
                .set(data);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (seminarListener != null) seminarListener.remove();
        if (lombaListener != null) lombaListener.remove();
    }
}