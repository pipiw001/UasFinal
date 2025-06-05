package com.example.uas_praktikummobileprogramming;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.dashboard.DashboardActivity;
import com.example.uas_praktikummobileprogramming.kegiatan.Kegiatan;
import com.example.uas_praktikummobileprogramming.notifikasi.NotifikasiActivity;
import com.example.uas_praktikummobileprogramming.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private final Class[] activities = {
            DashboardActivity.class,    // Position 0 (Now the default)
            Kegiatan.class,            // Position 1
            NotifikasiActivity.class,  // Position 2
            ProfileActivity.class      // Position 3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set Dashboard as default (position 0)
        if (savedInstanceState == null) {
            startNewActivity(0);  // Changed from 1 to 0 for Dashboard
            bottomNav.setSelectedItemId(R.id.nav_dashboard);  // Make sure this matches your menu ID
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_dashboard) {
                    startNewActivity(0);
                    return true;
                } else if (itemId == R.id.nav_events) {
                    startNewActivity(1);
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startNewActivity(2);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startNewActivity(3);
                    return true;
                }
                return false;
            };

    private void startNewActivity(int position) {
        Intent intent = new Intent(this, activities[position]);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBottomNavSelection();
    }

    private void updateBottomNavSelection() {
        try {
            String currentActivity = this.getClass().getName();

            if (currentActivity.equals(DashboardActivity.class.getName())) {
                bottomNav.setSelectedItemId(R.id.nav_dashboard);
            } else if (currentActivity.equals(Kegiatan.class.getName())) {
                bottomNav.setSelectedItemId(R.id.nav_events);
            } else if (currentActivity.equals(NotifikasiActivity.class.getName())) {
                bottomNav.setSelectedItemId(R.id.nav_notifications);
            } else if (currentActivity.equals(ProfileActivity.class.getName())) {
                bottomNav.setSelectedItemId(R.id.nav_profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}