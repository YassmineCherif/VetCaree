package com.example.vetoapp.ui.activities;
// src/AddAppointmentActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.content.Intent;

import com.example.vetoapp.R;


public class AddAppointmentActivity extends AppCompatActivity {
    private EditText etDate, etTime, etServiceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etServiceType = findViewById(R.id.etServiceType);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAppointment();
            }
        });
    }

    private void saveAppointment() {
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String serviceType = etServiceType.getText().toString();
        Toast.makeText(this, "Rendez-vous enregistré : " + date + " " + time + " " + serviceType, Toast.LENGTH_SHORT).show();
        // Logique pour enregistrer le rendez-vous dans la base de données
        Intent resultIntent = new Intent();
        resultIntent.putExtra("date", date);
        resultIntent.putExtra("time", time);
        resultIntent.putExtra("serviceType", serviceType);
        setResult(RESULT_OK, resultIntent);
        finish(); // Ferme l'activité
    }
}
