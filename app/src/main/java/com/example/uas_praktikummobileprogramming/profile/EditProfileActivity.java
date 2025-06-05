package com.example.uas_praktikummobileprogramming.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextNIM, editTextProdi, editTextDeskripsi;
    private Button buttonSave;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Profile");
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Bind views
        editTextNIM = findViewById(R.id.editTextNIM);
        editTextProdi = findViewById(R.id.editTextProdi);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        buttonSave = findViewById(R.id.buttonSave);

        // Load current data
        loadCurrentData();

        // Get current user
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            buttonSave.setOnClickListener(v -> {
                String nim = editTextNIM.getText().toString().trim();
                String prodi = editTextProdi.getText().toString().trim();
                String deskripsi = editTextDeskripsi.getText().toString().trim();

                if (nim.isEmpty() || prodi.isEmpty() || deskripsi.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update user data
                databaseReference.child(userId).child("nim").setValue(nim);
                databaseReference.child(userId).child("prodi").setValue(prodi);
                databaseReference.child(userId).child("deskripsi").setValue(deskripsi)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                finish(); // Tutup activity
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        }
    }

    private void loadCurrentData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nim = dataSnapshot.child("nim").getValue(String.class);
                        String prodi = dataSnapshot.child("prodi").getValue(String.class);
                        String deskripsi = dataSnapshot.child("deskripsi").getValue(String.class);

                        if (nim != null && !nim.isEmpty()) {
                            editTextNIM.setText(nim);
                        }
                        if (prodi != null && !prodi.isEmpty()) {
                            editTextProdi.setText(prodi);
                        }
                        if (deskripsi != null && !deskripsi.isEmpty()) {
                            editTextDeskripsi.setText(deskripsi);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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