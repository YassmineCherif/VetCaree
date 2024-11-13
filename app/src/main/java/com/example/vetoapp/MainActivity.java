package com.example.vetoapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vetoapp.databinding.ActivityMainBinding;
import com.example.vetoapp.ui.activities.HomeDossier;
import com.example.vetoapp.ui.activities.HomeRendezvousActivity;
import com.example.vetoapp.ui.activities.AddAnimalActivity;
import com.example.vetoapp.ui.activities.ReadAnimalActivity;

public  class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a local variable for binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addrendezvous.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeRendezvousActivity.class);
            startActivity(intent);
        });



        binding.addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAnimalActivity.class);
            startActivity(intent);
        });

        binding.adddossier.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeDossier.class);
            startActivity(intent);
        });

    }
}