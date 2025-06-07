package com.example.uas_praktikummobileprogramming.notifikasi;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.uas_praktikummobileprogramming.R;

public class NotifikasiHelper {
    private static final String CHANNEL_ID = "kegiatan_channel";
    private static final String CHANNEL_NAME = "Notifikasi Kegiatan Baru";
    private static final String CHANNEL_DESC = "Untuk pemberitahuan kegiatan baru";

    private Context context;

    public NotifikasiHelper(Context context) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public void tampilkanNotifikasi(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications) // Ganti dengan icon kamu
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permission belum diberikan
            return;
        }

        int id = (int) System.currentTimeMillis(); // ID unik agar tidak menimpa notifikasi lain
        manager.notify(id, builder.build());
    }
}

