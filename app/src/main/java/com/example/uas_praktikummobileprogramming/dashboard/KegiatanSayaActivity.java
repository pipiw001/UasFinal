package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.uas_praktikummobileprogramming.R;
import java.util.Arrays;
import java.util.List;

public class KegiatanSayaActivity extends AppCompatActivity {

    private CardView card1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan_saya);

        card1 = findViewById(R.id.card_1);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Kirim data ke DetailActivity (opsional)
                Intent intent = new Intent(KegiatanSayaActivity.this, DetailActivity.class);
                intent.putExtra("judul", "Judul Berita 1");
                intent.putExtra("subjudul", "Subjudul Berita 1");
                intent.putExtra("isi", "Ini adalah isi lengkap dari Berita 1.");
                startActivity(intent);
            }
        });
    }
}