package com.example.vetoapp.ui.activities;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vetoapp.R;
import com.example.vetoapp.dao.AnimalDao;
import com.example.vetoapp.models.Animal;
import com.example.vetoapp.utils.MyDataBase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ReadAnimalActivity extends AppCompatActivity {
//search
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private AnimalDao animalDao;
    private List<Animal> allAnimals;

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

        // Fetch all animals and update the adapter
        fetchAnimals();

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
    }


    private void fetchAnimals() {
        Executors.newSingleThreadExecutor().execute(() -> {
            allAnimals = animalDao.getAllAnimals();  // Store the full list for later filtering
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

    @Override
    protected void onResume() {
        super.onResume();
        fetchAnimals();
    }
}
