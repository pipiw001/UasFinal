package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uas_praktikummobileprogramming.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    ImageView img;
    TextView judul, deskripsi, lokasi, tanggal, kuota;
    private Button daftarButton;
    private String idKegiatan;
    private String jenis; // "seminar" atau "lomba"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        img = findViewById(R.id.imgDetail);
        judul = findViewById(R.id.txtJudul);
        deskripsi = findViewById(R.id.txtDeskripsi);
        lokasi = findViewById(R.id.txtLokasi);
        tanggal = findViewById(R.id.txtTanggal);
        kuota = findViewById(R.id.txtKuota);
        daftarButton = findViewById(R.id.register_button);

        Intent i = getIntent();

        // Ambil data dari intent
        idKegiatan = i.getStringExtra("id_kegiatan");
        jenis = i.getStringExtra("jenis");

        judul.setText(i.getStringExtra("judul"));
        deskripsi.setText(i.getStringExtra("deskripsi"));
        lokasi.setText(i.getStringExtra("lokasi"));
        tanggal.setText(i.getStringExtra("tanggal"));
        kuota.setText("Kuota: " + i.getIntExtra("kuota", 0));
        Glide.with(this).load(i.getStringExtra("gambar")).into(img);

        daftarButton.setOnClickListener(v -> {
            Intent daftarIntent = new Intent(DetailActivity.this, PendaftaranActivity.class);
            daftarIntent.putExtra("id_kegiatan", idKegiatan);
            daftarIntent.putExtra("jenis", jenis);
            startActivity(daftarIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ambil ulang kuota terbaru sesuai jenis kegiatan
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(jenis) // ← dinamis: bisa "seminar" atau "lomba"
                .document(idKegiatan)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Long kuotaBaru = doc.getLong("kuota");
                        if (kuotaBaru != null) {
                            kuota.setText("Kuota: " + kuotaBaru.intValue());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal memuat data kuota", Toast.LENGTH_SHORT).show();
                });
    }
}