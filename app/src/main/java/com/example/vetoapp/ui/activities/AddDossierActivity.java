package com.example.vetoapp.ui.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.content.Intent;

import com.example.vetoapp.R;

public class AddDossierActivity extends AppCompatActivity {
    private EditText etNomAnimal, etSexe, etDateNaissance, etEspece, etPoids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dossier);

        etNomAnimal = findViewById(R.id.etNomAnimal);
        etSexe = findViewById(R.id.etSexe);
        etDateNaissance = findViewById(R.id.etDateNaissance);
        etEspece = findViewById(R.id.etEspece);
        etPoids = findViewById(R.id.etPoids);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimal();
            }
        });
    }

    private void saveAnimal() {
        String nomAnimal = etNomAnimal.getText().toString();
        String sexe = etSexe.getText().toString();
        String date_naissance = etDateNaissance.getText().toString();
        String espece = etEspece.getText().toString();
        String poidsText = etPoids.getText().toString();
        double poids;
        try {
            poids = Double.parseDouble(poidsText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un poids valide", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(this, "Animal enregistré : " + nomAnimal + ", " + sexe + ", " + date_naissance + ", " + espece + ", " + poids + " kg", Toast.LENGTH_SHORT).show();

        // Logique pour enregistrer l'animal dans la base de données
        Intent resultIntent = new Intent();
        resultIntent.putExtra("nomAnimal", nomAnimal);
        resultIntent.putExtra("sexe", sexe);
        resultIntent.putExtra("date_naissance", date_naissance);
        resultIntent.putExtra("espece", espece);
        resultIntent.putExtra("poids", poids);
        setResult(RESULT_OK, resultIntent);
        finish(); // Ferme l'activité
    }
}
