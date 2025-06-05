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

    private TextView textViewNama, textViewEmail, nimValue, prodiValue, deskripsiValue;
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

        // Cek user sudah login atau belum
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // User belum login, arahkan ke LoginActivity
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;  // hentikan eksekusi onCreate selanjutnya
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Bind views
        textViewNama = findViewById(R.id.textViewNama);
        textViewEmail = findViewById(R.id.textViewEmail);
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
                        // Ambil data, pakai if-else untuk setiap field
                        String nama = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String nim = dataSnapshot.child("nim").getValue(String.class);
                        String prodi = dataSnapshot.child("prodi").getValue(String.class);
                        String deskripsi = dataSnapshot.child("deskripsi").getValue(String.class);

                        if (nama != null && !nama.isEmpty()) {
                            textViewNama.setText(nama);
                        } else {
                            textViewNama.setText("Nama belum diisi");
                        }

                        if (email != null && !email.isEmpty()) {
                            textViewEmail.setText(email);
                        } else {
                            textViewEmail.setText("Email belum diisi");
                        }

                        if (nim != null && !nim.isEmpty()) {
                            nimValue.setText(nim);
                        } else {
                            nimValue.setText("NIM belum diisi");
                        }

                        if (prodi != null && !prodi.isEmpty()) {
                            prodiValue.setText(prodi);
                        } else {
                            prodiValue.setText("Prodi belum diisi");
                        }

                        if (deskripsi != null && !deskripsi.isEmpty()) {
                            deskripsiValue.setText(deskripsi);
                        } else {
                            deskripsiValue.setText("Deskripsi belum diisi");
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User null, biasanya terjadi logout tiba2
            Toast.makeText(this, "User tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
