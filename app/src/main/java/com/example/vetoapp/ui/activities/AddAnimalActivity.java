package com.example.vetoapp.ui.activities;

import android.os.Bundle;
import android.widget.Toast;
import com.example.vetoapp.dao.AnimalDao;
import com.example.vetoapp.databinding.ActivityAddAnimal2Binding;
import com.example.vetoapp.models.Animal;
import com.example.vetoapp.utils.MyDataBase;

import androidx.appcompat.app.AppCompatActivity;


import java.util.concurrent.Executors;

public class AddAnimalActivity extends AppCompatActivity {

    private ActivityAddAnimal2Binding binding;
    private AnimalDao animalDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAnimal2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the instance of the database and DAO
        MyDataBase db = MyDataBase.getInstance(this);
        animalDao = db.animalDao();

        binding.saveButton.setOnClickListener(v -> {
            String name = binding.addAnimalTitle.getText().toString();
            String type = binding.addAnimalType.getText().toString();
            String age = binding.addAnimalAge.getText().toString();
            String sex = binding.addAnimalSex.getText().toString();
            String weight = binding.addAnimalWeight.getText().toString();
            String behavior = binding.addAnimalBehavior.getText().toString();

            // Create a new Animal object
            Animal animal = new Animal(name, type, age, sex, weight, behavior);

            // Insert the animal into the database using a background thread
            Executors.newSingleThreadExecutor().execute(() -> {
                animalDao.insert(animal);
                runOnUiThread(() -> {
                    Toast.makeText(AddAnimalActivity.this, "Animal Saved", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}
