package com.example.uas_praktikummobileprogramming.notifikasi;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.R;

import java.util.ArrayList;

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
    }

    private void initializeNotifications() {
        notifikasiList = new ArrayList<>();

        // Add sample notifications with real-time text and placeholder images
        notifikasiList.add(new NotificationItem("notif : real time", "Pembaruan sistem telah tersedia", "2 menit yang lalu"));
        notifikasiList.add(new NotificationItem("notif : real time", "Pesan baru dari admin", "5 menit yang lalu"));
        notifikasiList.add(new NotificationItem("notif : real time", "Update profil berhasil", "10 menit yang lalu"));
        notifikasiList.add(new NotificationItem("notif : real time", "Jadwal kuliah telah diperbarui", "15 menit yang lalu"));
        notifikasiList.add(new NotificationItem("notif : real time", "Reminder: Tugas deadline besok", "1 jam yang lalu"));
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