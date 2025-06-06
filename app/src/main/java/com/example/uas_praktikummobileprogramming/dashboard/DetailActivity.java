package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uas_praktikummobileprogramming.R;

public class DetailActivity extends AppCompatActivity {

    ImageView img;
    TextView judul, deskripsi, lokasi, tanggal, kuota;
    private Button daftarButton;
    private String idSeminar; // untuk menyimpan ID seminar

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
        idSeminar = i.getStringExtra("id_seminar"); // ← tambahkan ini
        judul.setText(i.getStringExtra("judul"));
        deskripsi.setText(i.getStringExtra("deskripsi"));
        lokasi.setText(i.getStringExtra("lokasi"));
        tanggal.setText(i.getStringExtra("tanggal"));
        kuota.setText("Kuota: " + i.getIntExtra("kuota", 0));
        Glide.with(this).load(i.getStringExtra("gambar")).into(img);

        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kirim id_seminar ke activity pendaftaran
                Intent daftarIntent = new Intent(DetailActivity.this, PendaftaranActivity.class);
                daftarIntent.putExtra("id_seminar", idSeminar); // ← kirim ID-nya
                startActivity(daftarIntent);
            }
        });
    }
}