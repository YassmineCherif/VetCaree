package com.example.vetoapp.ui.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.vetoapp.R;
import com.example.vetoapp.models.Appointment;
import com.example.vetoapp.Adapters.AppointmentAdapter;
import com.example.vetoapp.models.NotificationReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeRendezvousActivity extends AppCompatActivity {
    private ArrayList<Appointment> appointments;
    private AppointmentAdapter adapter;
    private static final String CHANNEL_ID = "appointment_channel";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rendezvous_main);

        // Initialiser la liste des rendez-vous et l'adaptateur
        appointments = new ArrayList<>();
        adapter = new AppointmentAdapter(this, appointments);

        // Récupérer la référence au ListView et l'associer à l'adaptateur
        ListView lvAppointments = findViewById(R.id.lvAppointments);
        lvAppointments.setAdapter(adapter);

        // Bouton pour ajouter un nouveau rendez-vous
        Button btnAddAppointment = findViewById(R.id.btnAddAppointment);
        btnAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité pour ajouter un rendez-vous
                startActivityForResult(new Intent(HomeRendezvousActivity.this, AddAppointmentActivity.class), 1);
            }
        });

        // Gérer les clics sur un élément de la liste pour modifier un rendez-vous
        lvAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // L'utilisateur a cliqué sur un rendez-vous existant
                Appointment selectedAppointment = appointments.get(position);
                Intent intent = new Intent(HomeRendezvousActivity.this, EditAppointmentActivity.class);

                // Passer les informations du rendez-vous sélectionné
                intent.putExtra("date", selectedAppointment.getDate());
                intent.putExtra("time", selectedAppointment.getTime());
                intent.putExtra("serviceType", selectedAppointment.getServiceType());
                intent.putExtra("position", position); // Passer la position pour la mise à jour

                // Lancer l'activité de modification
                startActivityForResult(intent, 2);  // 2 est un code de requête pour identifier la modification
            }
        });

        loadAppointments(); // Charger les rendez-vous fictifs
        createNotificationChannel(this); // Créer le canal de notification
        checkNotificationPermission();
       // Intent intent = getIntent();
     //   String appointmentDetails = intent.getStringExtra("appointmentDetails");
      // sendNotification(this,appointmentDetails);
       // String date = ""; // Remplacez par une date valide
        //String time = "";      // Remplacez par une heure valide
       // String serviceType = "";
      //  Appointment newAppointment = new Appointment(date, time, serviceType);
       // scheduleNotification(newAppointment, 10);
    }

    private void loadAppointments() {
        // Ajout de rendez-vous fictifs pour la démonstration
        appointments.add(new Appointment("2024-11-10", "10:00", "Consultation"));
        appointments.add(new Appointment("2024-11-12", "18:46", "Dentiste"));
        adapter.notifyDataSetChanged();
    }

    // Créez un canal de notification pour les versions Android >= 8.0
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Appointment Notifications";
            String description = "Channel for appointment reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // Créer le canal de notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Obtenir le NotificationManager à partir du contexte passé
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void sendNotification(Context context, String appointmentDetails) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Rendez-vous à venir")
                .setContentText(appointmentDetails)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    // Planifier une notification avant un rendez-vous
    // Planifier une notification avant un rendez-vous
    private void scheduleNotification(Appointment appointment, int minutesBefore) {
        Calendar calendar = Calendar.getInstance();

        String date = appointment.getDate();
        String time = appointment.getTime();

        if (date == null || date.isEmpty() || time == null || time.isEmpty()) {
            Toast.makeText(this, "Date ou heure du rendez-vous invalide.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification du format de la date et de l'heure
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}") || !time.matches("\\d{2}:\\d{2}")) {
            Toast.makeText(this, "Format de date ou d'heure invalide.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] dateParts = date.split("-");
        String[] timeParts = time.split(":");

        try {
            // Configurer l'heure et la date du rendez-vous
            calendar.set(
                    Integer.parseInt(dateParts[0]), // Année
                    Integer.parseInt(dateParts[1]) - 1, // Mois (0-11)
                    Integer.parseInt(dateParts[2]), // Jour
                    Integer.parseInt(timeParts[0]), // Heure
                    Integer.parseInt(timeParts[1]), // Minute
                    0 // Seconde
            );

            // Log pour vérifier la date et l'heure configurées
            Log.d("Notification", "Appointment set for: " + calendar.getTime());

            // Réduire le temps du rendez-vous de "minutesBefore"
            calendar.add(Calendar.MINUTE, -2);

            // Créer un intent pour déclencher la notification
            Intent notificationIntent = new Intent(this, NotificationReceiver.class);
            notificationIntent.putExtra("appointmentDetails", date + " " + time + " - " + appointment.getServiceType());

            // Utiliser un PendingIntent avec un ID unique basé sur le rendez-vous
            int requestCode = appointment.hashCode();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Utiliser AlarmManager pour envoyer la notification à la bonne heure
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(this, "Notification planifiée pour : " + calendar.getTime(), Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            Toast.makeText(this, "Erreur lors de la planification de la notification.", Toast.LENGTH_SHORT).show();
            Log.e("NotificationError", "Erreur lors de la conversion de la date/heure : ", e);
            e.printStackTrace();
        }
    }


    private void checkNotificationPermission() {
        // Vérifier si la permission pour les notifications a été accordée
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Demander la permission si ce n'est pas encore fait
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Vérifier si l'activité a renvoyé des données
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Ajouter un nouveau rendez-vous
            String date = data.getStringExtra("date");
            String time = data.getStringExtra("time");
            String serviceType = data.getStringExtra("serviceType");

            // Vérification des valeurs reçues
            if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(serviceType)) {
                Appointment newAppointment = new Appointment(date, time, serviceType);
                appointments.add(newAppointment);
                adapter.notifyDataSetChanged(); // Rafraîchir la liste des rendez-vous

                // Planifier la notification pour ce rendez-vous, par exemple 10 minutes avant
                scheduleNotification(newAppointment, 10);
            } else {
                // Afficher un message d'erreur si une donnée est manquante
                Toast.makeText(this, "Erreur : Les informations du rendez-vous sont incomplètes", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // Modifier un rendez-vous existant
            String date = data.getStringExtra("date");
            String time = data.getStringExtra("time");
            String serviceType = data.getStringExtra("serviceType");
            int position = data.getIntExtra("position", -1);

            if (position != -1 && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(serviceType)) {
                // Récupérer l'élément de rendez-vous à la position spécifiée
                Appointment updatedAppointment = appointments.get(position);

                // Mettre à jour les informations du rendez-vous
                updatedAppointment.setDate(date);
                updatedAppointment.setTime(time);
                updatedAppointment.setServiceType(serviceType);

                // Notifier l'adaptateur que les données ont changé pour mettre à jour l'affichage
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Erreur : Les informations du rendez-vous sont incorrectes", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void launchEditAppointmentActivity(Appointment appointment, int position) {
        Intent intent = new Intent(this, EditAppointmentActivity.class);
        intent.putExtra("date", appointment.getDate());
        intent.putExtra("time", appointment.getTime());
        intent.putExtra("serviceType", appointment.getServiceType());
        intent.putExtra("position", position);

        // Lancer l'activité de modification avec startActivityForResult
        startActivityForResult(intent, 2); // Utilisez un code de requête défini pour l'édition, ici 2
    }


}
