package com.example.uas_praktikummobileprogramming.notifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.dashboard.DashboardActivity;
import com.example.uas_praktikummobileprogramming.dashboard.KegiatanSayaActivity;
import com.example.uas_praktikummobileprogramming.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;

public class NotifikasiActivity extends AppCompatActivity {

    private ListView listViewNotifikasi;
    private ArrayList<NotificationItem> notifikasiList;
    private NotifikasiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notifikasi");
        }

        listViewNotifikasi = findViewById(R.id.listViewNotifikasi);

        // Initialize notification list
        initializeNotifications();

        // Set adapter untuk ListView
        adapter = new NotifikasiAdapter(this, notifikasiList);
        listViewNotifikasi.setAdapter(adapter);

        // Inisialisasi BottomNavigationView dan set listener
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_notifikasi);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_kegiatan) {
                startActivity(new Intent(this, KegiatanSayaActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_notifikasi) {
                // Sudah di notifikasi
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void initializeNotifications() {
        notifikasiList = new ArrayList<>();
        adapter = new NotifikasiAdapter(this, notifikasiList);
        listViewNotifikasi.setAdapter(adapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance()
                .collection("notifikasi_user")
                .document(user.getUid())
                .collection("inbox")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String judul = doc.getString("judul");
                        String jenis = doc.getString("jenis");
                        Timestamp waktu = doc.getTimestamp("timestamp");

                        String waktuStr = waktu != null ? getWaktuRelative(waktu.toDate()) : "Baru saja";

                        notifikasiList.add(new NotificationItem("Notifikasi " + jenis, judul, waktuStr));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private String getWaktuRelative(Date date) {
        long now = System.currentTimeMillis();
        long diff = now - date.getTime();

        long minutes = diff / (1000 * 60);
        if (minutes < 1) return "Baru saja";
        if (minutes < 60) return minutes + " menit yang lalu";
        long hours = minutes / 60;
        if (hours < 24) return hours + " jam yang lalu";
        long days = hours / 24;
        return days + " hari yang lalu";
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Inner class untuk notification item
    public static class NotificationItem {
        private String title;
        private String message;
        private String time;

        public NotificationItem(String title, String message, String time) {
            this.title = title;
            this.message = message;
            this.time = time;
        }

        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getTime() { return time; }
    }
}
