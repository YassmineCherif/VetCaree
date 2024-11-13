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

public class EditAppointmentActivity extends AppCompatActivity {

    private EditText etDate;
    private EditText etTime;
    private EditText etServiceType;
    private Button btnSave;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editappointmentactivity);

        // Initialiser les vues
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etServiceType = findViewById(R.id.etServiceType);
        btnSave = findViewById(R.id.btnSave);

        // Récupérer les données du rendez-vous de l'intent
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String serviceType = intent.getStringExtra("serviceType");
        position = intent.getIntExtra("position", -1);  // Position du rendez-vous dans la liste

        // Remplir les champs avec les données actuelles du rendez-vous
        etDate.setText(date);
        etTime.setText(time);
        etServiceType.setText(serviceType);

        // Assigner un focus au premier champ
        etDate.requestFocus();

        // Configurer le bouton pour sauvegarder les modifications
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUpdatedAppointment();
            }
        });
    }

    private void saveUpdatedAppointment() {
        // Récupérer les valeurs des champs
        String updatedDate = etDate.getText().toString();
        String updatedTime = etTime.getText().toString();
        String updatedServiceType = etServiceType.getText().toString();

        // Vérifier si tous les champs sont remplis
        if (!TextUtils.isEmpty(updatedDate) && !TextUtils.isEmpty(updatedTime) && !TextUtils.isEmpty(updatedServiceType)) {
            // Préparer les données de résultat à renvoyer
            Intent resultIntent = new Intent();
            resultIntent.putExtra("date", updatedDate);
            resultIntent.putExtra("time", updatedTime);
            resultIntent.putExtra("serviceType", updatedServiceType);
            resultIntent.putExtra("position", position);  // Inclure la position pour identifier le rendez-vous à modifier

            // Renvoyer les données à l'activité principale avec RESULT_OK
            setResult(RESULT_OK, resultIntent);
            finish();  // Fermer l'activité
        } else {
            // Afficher un message si des champs sont vides
            Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        }
    }
}
