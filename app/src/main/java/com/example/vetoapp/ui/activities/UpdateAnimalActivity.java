package com.example.vetoapp.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.vetoapp.R;
import com.example.vetoapp.models.Animal;
import com.example.vetoapp.dao.AnimalDao;
import com.example.vetoapp.utils.MyDataBase;

import java.util.concurrent.Executors;

public class UpdateAnimalActivity extends AppCompatActivity {

    private EditText nameEditText, typeEditText, ageEditText, sexEditText, weightEditText, behaviorEditText;
    private Button saveButton;
    private AnimalDao animalDao;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_animal);

        // Initialize views
        nameEditText = findViewById(R.id.editAnimalName);
        typeEditText = findViewById(R.id.editAnimalType);
        ageEditText = findViewById(R.id.editAnimalAge);
        sexEditText = findViewById(R.id.editAnimalSex);
        weightEditText = findViewById(R.id.editAnimalWeight);
        behaviorEditText = findViewById(R.id.editAnimalBehavior);
        saveButton = findViewById(R.id.saveButton);

        // Initialize DAO
        MyDataBase db = MyDataBase.getInstance(this);
        animalDao = db.animalDao();

        // Get the animal data passed from the adapter
        int animalId = getIntent().getIntExtra("ANIMAL_ID", -1);
        if (animalId != -1) {
            loadAnimalData(animalId);
        } else {
            // Handle the case where animal ID is not passed
            Toast.makeText(this, "Animal ID not found", Toast.LENGTH_SHORT).show();
        }

        // Save button listener
        saveButton.setOnClickListener(v -> saveAnimalData());
    }

    private void loadAnimalData(int animalId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            animal = animalDao.getAnimalById(animalId);

            // Update UI with animal details on the main thread
            runOnUiThread(() -> {
                if (animal != null) {
                    nameEditText.setText(animal.getName());
                    typeEditText.setText(animal.getType());
                    ageEditText.setText(animal.getAge());
                    sexEditText.setText(animal.getSex());
                    weightEditText.setText(animal.getWeight());
                    behaviorEditText.setText(animal.getBehavior());
                }
            });
        });
    }

    private void saveAnimalData() {
        // Check if any of the fields are empty
        if (isAnyFieldEmpty()) {
            // Display a message if any field is empty
            Toast.makeText(this, "Please fill all fields before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the name entered by the user
        String newName = nameEditText.getText().toString();

        // Check if the name already exists in the database
        Executors.newSingleThreadExecutor().execute(() -> {
            Animal existingAnimal = animalDao.getAnimalByName(newName);

            runOnUiThread(() -> {
                if (existingAnimal != null && existingAnimal.getId() != animal.getId()) {
                    // If an animal with the same name exists and it's not the same animal
                    Toast.makeText(UpdateAnimalActivity.this, "Name already exists. Please choose another one.", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed to update the animal if the name is unique
                    animal.setName(newName);
                    animal.setType(typeEditText.getText().toString());
                    animal.setAge(ageEditText.getText().toString());
                    animal.setSex(sexEditText.getText().toString());
                    animal.setWeight(weightEditText.getText().toString());
                    animal.setBehavior(behaviorEditText.getText().toString());

                    // Save the updated animal in a background thread
                    Executors.newSingleThreadExecutor().execute(() -> {
                        animalDao.update(animal);
                        finish(); // Go back to the previous activity
                    });
                }
            });
        });
    }

    private boolean isAnyFieldEmpty() {
        return nameEditText.getText().toString().isEmpty() ||
                typeEditText.getText().toString().isEmpty() ||
                ageEditText.getText().toString().isEmpty() ||
                sexEditText.getText().toString().isEmpty() ||
                weightEditText.getText().toString().isEmpty() ||
                behaviorEditText.getText().toString().isEmpty();
    }
}
