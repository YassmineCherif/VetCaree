package com.example.vetoapp.ui.activities;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vetoapp.R;

public class EditDossierActivity extends AppCompatActivity {

    private EditText etNomAnimal;
    private EditText etSexe;
    private EditText etDateNaissance;
    private EditText etEspece;
    private EditText etPoids;
    private Button btnSave;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dossier);

        // Initialiser les vues
        etNomAnimal = findViewById(R.id.etNomAnimal);
        etSexe = findViewById(R.id.etSexe);
        etDateNaissance = findViewById(R.id.etDateNaissance);
        etEspece = findViewById(R.id.etEspece);
        etPoids = findViewById(R.id.etPoids);
        btnSave = findViewById(R.id.btnSave);

        // Récupérer les données du dossier depuis l'intent
        Intent intent = getIntent();
        String nomAnimal = intent.getStringExtra("nomAnimal");
        String sexe = intent.getStringExtra("sexe");
        String dateNaissance = intent.getStringExtra("date_naissance");
        String espece = intent.getStringExtra("espece");
        double poids = intent.getDoubleExtra("poids", 0.0);
        position = intent.getIntExtra("position", -1);  // Position du dossier dans la liste

        // Remplir les champs avec les données actuelles du dossier
        etNomAnimal.setText(nomAnimal);
        etSexe.setText(sexe);
        etDateNaissance.setText(dateNaissance);
        etEspece.setText(espece);
        etPoids.setText(String.valueOf(poids));

        // Assigner un focus au premier champ
        etNomAnimal.requestFocus();

        // Configurer le bouton pour sauvegarder les modifications
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUpdatedDossier();
            }
        });
    }

    private void saveUpdatedDossier() {
        // Récupérer les valeurs des champs
        String updatedNomAnimal = etNomAnimal.getText().toString();
        String updatedSexe = etSexe.getText().toString();
        String updatedDateNaissance = etDateNaissance.getText().toString();
        String updatedEspece = etEspece.getText().toString();
        String updatedPoids = etPoids.getText().toString();

        // Vérifier si tous les champs sont remplis
        if (!TextUtils.isEmpty(updatedNomAnimal) && !TextUtils.isEmpty(updatedSexe) &&
                !TextUtils.isEmpty(updatedDateNaissance) && !TextUtils.isEmpty(updatedEspece) &&
                !TextUtils.isEmpty(updatedPoids)) {

            // Préparer les données de résultat à renvoyer
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nomAnimal", updatedNomAnimal);
            resultIntent.putExtra("sexe", updatedSexe);
            resultIntent.putExtra("date_naissance", updatedDateNaissance);
            resultIntent.putExtra("espece", updatedEspece);
            resultIntent.putExtra("poids", Double.parseDouble(updatedPoids));
            resultIntent.putExtra("position", position);  // Inclure la position pour identifier le dossier à modifier

            // Renvoyer les données à l'activité principale avec RESULT_OK
            setResult(RESULT_OK, resultIntent);
            finish();  // Fermer l'activité
        } else {
            // Afficher un message si des champs sont vides
            Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        }
    }
}

