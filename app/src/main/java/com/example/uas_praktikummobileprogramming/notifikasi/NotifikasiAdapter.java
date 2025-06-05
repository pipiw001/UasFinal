package com.example.uas_praktikummobileprogramming.notifikasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uas_praktikummobileprogramming.R;

import java.util.ArrayList;

public class NotifikasiAdapter extends ArrayAdapter<NotifikasiActivity.NotificationItem> {

    private Context context;
    private ArrayList<NotifikasiActivity.NotificationItem> notifications;

    public NotifikasiAdapter(Context context, ArrayList<NotifikasiActivity.NotificationItem> notifications) {
        super(context, 0, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotifikasiActivity.NotificationItem notification = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notifikasi, parent, false);
        }

        // Bind views
        ImageView imageViewNotification = convertView.findViewById(R.id.imageViewNotification);
        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewMessage = convertView.findViewById(R.id.textViewMessage);
        TextView textViewTime = convertView.findViewById(R.id.textViewTime);

        // Set data
        if (notification != null) {
            // Use ic_launcher_background as placeholder
            imageViewNotification.setImageResource(R.drawable.ic_launcher_background);
            textViewTitle.setText(notification.getTitle());
            textViewMessage.setText(notification.getMessage());
            textViewTime.setText(notification.getTime());
        }

        return convertView;
    }
}