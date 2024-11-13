package com.example.vetoapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetoapp.Adapters.AnimalAdapter;
import com.example.vetoapp.R;
import com.example.vetoapp.dao.AnimalDao;
import com.example.vetoapp.models.Animal;
import com.example.vetoapp.models.PDFExportUtility;
import com.example.vetoapp.utils.MyDataBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ReadAnimalActivity extends AppCompatActivity {
    // Declare the RecyclerView and Adapter
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private AnimalDao animalDao;
    private List<Animal> allAnimals;

    // Static user ID for now (change this later to dynamic user ID)
    private int userId = 1; // Replace this with the actual logged-in user's ID when authentication is implemented

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_animal);

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.animalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the instance of the database and DAO
        MyDataBase db = MyDataBase.getInstance(this);
        animalDao = db.animalDao();

        // Initialize the adapter with animalDao for delete functionality
        animalAdapter = new AnimalAdapter(new ArrayList<>(), animalDao);
        recyclerView.setAdapter(animalAdapter);

        // Fetch animals by user ID
        fetchAnimalsByUserId(userId);

        // Set up the SearchView to filter the animals
        SearchView searchView = findViewById(R.id.searchView); // Correct SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAnimalsByName(newText);
                return true;
            }
        });

        // Set up the Download PDF button
        FloatingActionButton downloadPdfButton = findViewById(R.id.downloadPdfButton);
        downloadPdfButton.setOnClickListener(view -> downloadAnimalInfoAsPdf());
    }

    private void fetchAnimalsByUserId(int userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Fetch only animals associated with the current userId
            allAnimals = animalDao.getAnimalsByUserId(userId);  // Assuming you implement this in the DAO
            runOnUiThread(() -> animalAdapter.updateData(allAnimals));
        });
    }

    private void filterAnimalsByName(String query) {
        if (allAnimals == null || allAnimals.isEmpty()) return;

        List<Animal> filteredList = new ArrayList<>();
        for (Animal animal : allAnimals) {
            if (animal.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(animal);
            }
        }
        animalAdapter.updateData(filteredList);  // Update the adapter with the filtered list
    }

    private void downloadAnimalInfoAsPdf() {
        if (allAnimals == null || allAnimals.isEmpty()) {
            Toast.makeText(this, "No animal data available for export", Toast.LENGTH_SHORT).show();
            return;
        }

        File pdfFile = PDFExportUtility.createAnimalPDF(this, allAnimals);
        if (pdfFile != null) {
            Uri uri = Uri.fromFile(pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(Intent.createChooser(intent, "Open PDF"));
        } else {
            Toast.makeText(this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAnimalsByUserId(userId);
    }
}
