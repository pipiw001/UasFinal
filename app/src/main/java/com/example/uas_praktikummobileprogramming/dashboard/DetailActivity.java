package com.example.uas_praktikummobileprogramming.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uas_praktikummobileprogramming.R;

public class DetailActivity extends AppCompatActivity {

    private TextView title, subtitle, body;
    private Button daftarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.detail_title);
        subtitle = findViewById(R.id.detail_subtitle);
        body = findViewById(R.id.detail_body);
        daftarButton = findViewById(R.id.register_button);

        // Terima data dari DashboardActivity
        Intent intent = getIntent();
        String judul = intent.getStringExtra("judul");
        String subjudul = intent.getStringExtra("subjudul");
        String isi = intent.getStringExtra("isi");

        // Tampilkan data
        title.setText(judul);
        subtitle.setText(subjudul);
        body.setText(isi);

        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daftarIntent = new Intent(DetailActivity.this, PendaftaranActivity.class);
                startActivity(daftarIntent);
            }
        });
    }
}
