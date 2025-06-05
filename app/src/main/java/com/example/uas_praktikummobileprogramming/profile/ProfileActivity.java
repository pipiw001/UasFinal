package com.example.uas_praktikummobileprogramming.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewNama, textViewEmail, textViewNIM, textViewProdi, textViewDeskripsi;
    private TextView nimValue, prodiValue, deskripsiValue;
    private Button buttonEditProfil, buttonLogout;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Bind views
        textViewNama = findViewById(R.id.textViewNama);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewNIM = findViewById(R.id.textViewNIM);
        textViewProdi = findViewById(R.id.textViewProdi);
        textViewDeskripsi = findViewById(R.id.textViewDeskripsi);
        nimValue = findViewById(R.id.nimValue);
        prodiValue = findViewById(R.id.prodiValue);
        deskripsiValue = findViewById(R.id.deskripsiValue);
        buttonEditProfil = findViewById(R.id.buttonEditProfil);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Ambil data pengguna
        loadUserData();

        // Logout action
        buttonLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        // Edit Profil action
        buttonEditProfil.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning from EditProfileActivity
        loadUserData();
    }

    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nama = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String nim = dataSnapshot.child("nim").getValue(String.class);
                        String prodi = dataSnapshot.child("prodi").getValue(String.class);
                        String deskripsi = dataSnapshot.child("deskripsi").getValue(String.class);

                        textViewNama.setText(nama != null ? nama : "Nama");
                        textViewEmail.setText(email != null ? email : "Email");

                        // Set values or default "..." if empty
                        nimValue.setText(nim != null && !nim.isEmpty() ? nim : "...");
                        prodiValue.setText(prodi != null && !prodi.isEmpty() ? prodi : "...");
                        deskripsiValue.setText(deskripsi != null && !deskripsi.isEmpty() ? deskripsi : "...");
                    } else {
                        Toast.makeText(ProfileActivity.this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}