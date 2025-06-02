package com.example.uas_praktikummobileprogramming.dashboard;

import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.R;

public class PendaftaranActivity extends AppCompatActivity {

    private Button btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran);

        btnDaftar = findViewById(R.id.btn_daftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PendaftaranActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                // Bisa ditambahkan logika untuk simpan data nanti
            }
        });
    }
}
