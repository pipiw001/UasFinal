package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PendaftaranActivity extends AppCompatActivity {

    private EditText inputNama, inputNim, inputEmail, inputTelepon, inputProdi;
    private Button btnDaftar;
    private String idKegiatan;
    private String jenis; // "seminar" atau "lomba"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran);

        // Ambil data dari intent
        idKegiatan = getIntent().getStringExtra("id_kegiatan");
        jenis = getIntent().getStringExtra("jenis");

        // Inisialisasi komponen input
        inputNama = findViewById(R.id.input_nama);
        inputNim = findViewById(R.id.input_nim);
        inputEmail = findViewById(R.id.input_email);
        inputTelepon = findViewById(R.id.input_telepon);
        inputProdi = findViewById(R.id.input_prodi);
        btnDaftar = findViewById(R.id.btn_daftar);

        btnDaftar.setOnClickListener(v -> {
            String nama = inputNama.getText().toString().trim();
            String nim = inputNim.getText().toString().trim();
            String telepon = inputTelepon.getText().toString().trim();
            String prodi = inputProdi.getText().toString().trim();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user != null ? user.getEmail() : "";

            if (nama.isEmpty() || nim.isEmpty() || email.isEmpty() || telepon.isEmpty() || prodi.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            simpanPendaftaran(nama, nim, email, telepon, prodi);
        });
    }

    private void simpanPendaftaran(String nama, String nim, String email, String telepon, String prodi) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("id_kegiatan", idKegiatan);
        data.put("jenis", jenis);
        data.put("nama", nama);
        data.put("nim", nim);
        data.put("email", email);
        data.put("telepon", telepon);
        data.put("prodi", prodi);
        data.put("timestamp", FieldValue.serverTimestamp()); // Optional: waktu daftar

        // Kirim ke koleksi berdasarkan jenis
        String namaKoleksi = "pendaftaran_" + jenis;

        db.collection(namaKoleksi)
                .add(data)
                .addOnSuccessListener(docRef -> kurangiKuotaKegiatan(db))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal menyimpan pendaftaran", Toast.LENGTH_SHORT).show();
                });
    }

    private void kurangiKuotaKegiatan(FirebaseFirestore db) {
        // Kurangi kuota dari koleksi 'seminar' atau 'lomba'
        DocumentReference kegiatanRef = db.collection(jenis).document(idKegiatan);

        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(kegiatanRef);
            Long kuota = snapshot.getLong("kuota");

            if (kuota != null && kuota > 0) {
                transaction.update(kegiatanRef, "kuota", kuota - 1);
            }

            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PendaftaranActivity.this, KegiatanSayaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // agar stack bersih
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Gagal mengurangi kuota", Toast.LENGTH_SHORT).show();
        });
    }
}