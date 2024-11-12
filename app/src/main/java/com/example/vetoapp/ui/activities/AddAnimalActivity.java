package com.example.vetoapp.ui.activities;

import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vetoapp.dao.AnimalDao;
import com.example.vetoapp.databinding.ActivityAddAnimal2Binding;
import com.example.vetoapp.models.Animal;
import com.example.vetoapp.utils.MyDataBase;

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
            // Get input values
            String name = binding.addAnimalTitle.getText().toString().trim();
            String type = binding.addAnimalType.getText().toString().trim();
            String age = binding.addAnimalAge.getText().toString().trim();
            String sex = binding.addAnimalSex.getText().toString().trim();
            String weight = binding.addAnimalWeight.getText().toString().trim();
            String behavior = binding.addAnimalBehavior.getText().toString().trim();

            // Check if any field is empty
            if (name.isEmpty() || type.isEmpty() || age.isEmpty() || sex.isEmpty() || weight.isEmpty() || behavior.isEmpty()) {
                // Show a toast message or display an error dialog if any field is empty
                Toast.makeText(AddAnimalActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return; // Exit the method if validation fails
            }

            // Check if the animal with the same name already exists in the database
            Executors.newSingleThreadExecutor().execute(() -> {
                Animal existingAnimal = animalDao.getAnimalByName(name);  // Assume getAnimalByName is implemented in the DAO

                if (existingAnimal != null) {
                    // If animal already exists, show a toast message
                    runOnUiThread(() -> Toast.makeText(AddAnimalActivity.this, "Animal already exists", Toast.LENGTH_SHORT).show());
                } else {
                    // Create a new Animal object if the name is unique
                    Animal animal = new Animal(name, type, age, sex, weight, behavior);

                    // Insert the animal into the database using a background thread
                    Executors.newSingleThreadExecutor().execute(() -> {
                        animalDao.insert(animal);
                        runOnUiThread(() -> {
                            Toast.makeText(AddAnimalActivity.this, "Animal Saved", Toast.LENGTH_SHORT).show();

                            // Navigate to ReadAnimalActivity
                            Intent intent = new Intent(AddAnimalActivity.this, ReadAnimalActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                }
            });
        });
    }
}
