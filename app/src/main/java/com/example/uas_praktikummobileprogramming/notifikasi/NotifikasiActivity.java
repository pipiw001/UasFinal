package com.example.uas_praktikummobileprogramming.notifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotifikasiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotifikasiAdapter adapter;
    private List<NotifikasiModel> notifikasiList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notifikasi");
        }

        recyclerView = findViewById(R.id.recyclerViewNotifikasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notifikasiList = new ArrayList<>();
        adapter = new NotifikasiAdapter(notifikasiList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("notifikasi_user")
                    .document(user.getUid())
                    .collection("inbox")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        if (error != null || value == null) return;

                        notifikasiList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            NotifikasiModel model = doc.toObject(NotifikasiModel.class);
                            notifikasiList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    });
        }
    }
}