package com.example.vetoapp.Adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ArrayAdapter;

import com.example.vetoapp.R;
import com.example.vetoapp.models.Dossier;
import com.example.vetoapp.ui.activities.HomeDossier;

import java.util.ArrayList;

public class DossierAdapter extends ArrayAdapter<Dossier> {
    private ArrayList<Dossier> dossiers;
    private Context context;

    public DossierAdapter(Context context, ArrayList<Dossier> dossiers) {
        super(context, 0, dossiers);
        this.dossiers = dossiers;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Gonfler la vue uniquement depuis item_dossier
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_dossier, parent, false);
        }

        // Récupérer l'élément `Dossier` à afficher
        final Dossier dossier = getItem(position);

        // Initialiser les vues
        TextView tvNomAnimal = convertView.findViewById(R.id.tvNomAnimal);
        TextView tvSexe = convertView.findViewById(R.id.tvSexe);
        TextView tvDateNaissance = convertView.findViewById(R.id.tvDateNaissance);
        TextView tvEspece = convertView.findViewById(R.id.tvEspece);
        TextView tvPoids = convertView.findViewById(R.id.tvPoids);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnSave = convertView.findViewById(R.id.btnSave);

        // Affichage des informations du dossier dans les `TextView`
        tvNomAnimal.setText(dossier.getNomAnimal());
        tvSexe.setText(dossier.getSexe());
        tvDateNaissance.setText(dossier.getDate_naissance());
        tvEspece.setText(dossier.getEspece());
        tvPoids.setText(String.valueOf(dossier.getPoids()) + " kg");

        // Déléguer l'action de modification au clic sur le bouton "Enregistrer"
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof HomeDossier) {
                    ((HomeDossier) context).launchEditDossierMedicalActivity(dossier, position);
                }
            }
        });

        // Supprimer l'élément de la liste au clic sur le bouton "Supprimer"
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dossiers.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
