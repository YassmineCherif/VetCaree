package com.example.vetoapp.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vetoapp.Adapters.DossierAdapter;
import com.example.vetoapp.R;
import com.example.vetoapp.models.Dossier;

import java.util.ArrayList;

public class HomeDossier extends AppCompatActivity {
    private ArrayList<Dossier> dossiers;
    private DossierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dossier_main);

        // Initialiser la liste des dossiers et l'adaptateur
        dossiers = new ArrayList<>();
        adapter = new DossierAdapter(this, dossiers);

        // Récupérer la référence au ListView et l'associer à l'adaptateur
        ListView lvDossiers = findViewById(R.id.lvDossiers);
        lvDossiers.setAdapter(adapter);

        // Bouton pour ajouter un nouveau dossier
        Button btnAddDossier = findViewById(R.id.btnAddDossier);
        btnAddDossier.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Lancer l'activité pour ajouter un dossier
                startActivityForResult(new Intent(HomeDossier.this, AddDossierActivity.class), 1);
            }
        });

        // Gérer les clics sur un élément de la liste pour modifier un dossier
        lvDossiers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // L'utilisateur a cliqué sur un dossier existant
                Dossier selectedDossier = dossiers.get(position);
                Intent intent = new Intent(HomeDossier.this, EditDossierActivity.class);

                // Passer les informations du dossier sélectionné
                intent.putExtra("nomAnimal", selectedDossier.getNomAnimal());
                intent.putExtra("sexe", selectedDossier.getSexe());
                intent.putExtra("date_naissance", selectedDossier.getDate_naissance());
                intent.putExtra("espece", selectedDossier.getEspece());
                intent.putExtra("poids", selectedDossier.getPoids());
                intent.putExtra("position", position); // Passer la position pour la mise à jour

                // Lancer l'activité de modification
                startActivityForResult(intent, 2);  // 2 est un code de requête pour identifier la modification
            }
        });

        loadDossiers(); // Charger les dossiers fictifs
    }

    private void loadDossiers() {
        // Ajout de dossiers fictifs pour la démonstration
        dossiers.add(new Dossier("Chat", "M", "2020-01-15", "Félin", 4.5));
        dossiers.add(new Dossier("Chien", "F", "2018-07-23", "Canin", 10.0));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Vérifier si l'activité a renvoyé des données
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Ajouter un nouveau dossier
            String nomAnimal = data.getStringExtra("nomAnimal");
            String sexe = data.getStringExtra("sexe");
            String date_naissance = data.getStringExtra("date_naissance");
            String espece = data.getStringExtra("espece");
            double poids = data.getDoubleExtra("poids", 0.0);

            // Vérification des valeurs reçues
            if (!TextUtils.isEmpty(nomAnimal) && !TextUtils.isEmpty(sexe) &&
                    !TextUtils.isEmpty(date_naissance) && !TextUtils.isEmpty(espece) && poids > 0) {
                Dossier newDossier = new Dossier(nomAnimal, sexe, date_naissance, espece, poids);
                dossiers.add(newDossier);
                adapter.notifyDataSetChanged(); // Rafraîchir la liste des dossiers
            } else {
                // Afficher un message d'erreur si une donnée est manquante
                Toast.makeText(this, "Erreur : Les informations du dossier sont incomplètes", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // Modifier un dossier existant
            String nomAnimal = data.getStringExtra("nomAnimal");
            String sexe = data.getStringExtra("sexe");
            String dateNaissance = data.getStringExtra("date_naissance");
            String espece = data.getStringExtra("espece");
            double poids = data.getDoubleExtra("poids", 0.0);
            int position = data.getIntExtra("position", -1);

            if (position != -1 && !TextUtils.isEmpty(nomAnimal) && !TextUtils.isEmpty(sexe) &&
                    !TextUtils.isEmpty(dateNaissance) && !TextUtils.isEmpty(espece) && poids > 0) {
                // Récupérer l'élément de dossier à la position spécifiée
                Dossier updatedDossier = dossiers.get(position);

                // Mettre à jour les informations du dossier
                updatedDossier.setNomAnimal(nomAnimal);
                updatedDossier.setSexe(sexe);
                updatedDossier.setDate_naissance(dateNaissance);
                updatedDossier.setEspece(espece);
                updatedDossier.setPoids(poids);

                // Notifier l'adaptateur que les données ont changé pour mettre à jour l'affichage
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Erreur : Les informations du dossier sont incorrectes", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void launchEditDossierMedicalActivity(Dossier dossierMed, int position) {
        Intent intent = new Intent(this, EditDossierActivity.class);

        // Passer les données à l'activité de modification
        intent.putExtra("animalName", dossierMed.getNomAnimal());
        intent.putExtra("sex", dossierMed.getSexe());
        intent.putExtra("birthDate", dossierMed.getDate_naissance());
        intent.putExtra("species", dossierMed.getEspece());
        intent.putExtra("weight", dossierMed.getPoids());
        intent.putExtra("position", position); // Passer la position si nécessaire pour un traitement ultérieur

        // Lancer l'activité de modification avec startActivityForResult
        startActivityForResult(intent, 2); // Utilisez un code de requête pour l'édition, ici 2
    }

}
