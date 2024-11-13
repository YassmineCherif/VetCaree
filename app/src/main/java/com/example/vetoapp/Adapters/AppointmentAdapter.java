package com.example.vetoapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ArrayAdapter;

import com.example.vetoapp.R;
import com.example.vetoapp.models.Appointment;
import com.example.vetoapp.ui.activities.HomeRendezvousActivity;

import java.util.ArrayList;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    private ArrayList<Appointment> appointments;
    private Context context;

    public AppointmentAdapter(Context context, ArrayList<Appointment> appointments) {
        super(context, 0, appointments);
        this.appointments = appointments;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Gonfler la vue uniquement depuis item_appointment
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        }

        // Récupérer le rendez-vous à afficher
        final Appointment appointment = getItem(position);

        // Initialiser les vues
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvServiceType = convertView.findViewById(R.id.tvServiceType);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnSave = convertView.findViewById(R.id.btnSave);

        // Affichage des informations du rendez-vous
        tvDate.setText(appointment.getDate() + " - " + appointment.getTime());
        tvServiceType.setText(appointment.getServiceType());

        // Déléguer l'action de modification au clic sur le bouton "Enregistrer"
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Déléguer l'appel de la modification à MainActivity
                if (context instanceof HomeRendezvousActivity) {
                    ((HomeRendezvousActivity) context).launchEditAppointmentActivity(appointment, position);
                }
            }
        });

        // Supprimer l'élément de la liste au clic sur le bouton "Supprimer"
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointments.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
