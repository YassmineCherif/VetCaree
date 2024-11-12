package com.example.vetoapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vetoapp.databinding.ActivityMainBinding;
import com.example.vetoapp.ui.activities.AddAnimalActivity;
import com.example.vetoapp.ui.activities.ReadAnimalActivity;

public  class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a local variable for binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.readButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReadAnimalActivity.class);
            startActivity(intent);
        });

        binding.addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAnimalActivity.class);
            startActivity(intent);
        });
    }
}