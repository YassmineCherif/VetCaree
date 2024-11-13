package com.example.vetoapp.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.vetoapp.R;
import com.example.vetoapp.ui.activities.HomeRendezvousActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Créer le canal de notification
        createNotificationChannel(context);

        // Récupérer les détails du rendez-vous à partir de l'intent
        String appointmentDetails = intent.getStringExtra("appointmentDetails");

        // Envoyer la notification
        sendNotification(context, appointmentDetails);
        HomeRendezvousActivity.sendNotification(context, appointmentDetails);

    }

    private void createNotificationChannel(Context context) {
        // Créer le canal de notification pour Android 8.0 et supérieur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Appointment Channel";
            String description = "Channel for appointment reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("appointment_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(Context context, String appointmentDetails) {
        // Créer la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "appointment_channel")
                .setSmallIcon(R.drawable.notif)  // Remplacer avec votre icône
                .setContentTitle("Rappel de rendez-vous")
                .setContentText(appointmentDetails)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Vérification des permissions
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Si la permission n'est pas accordée, vous pouvez choisir de ne pas afficher la notification
            return;
        }

        // Afficher la notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}