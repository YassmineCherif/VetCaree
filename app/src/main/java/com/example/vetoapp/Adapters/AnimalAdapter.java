package com.example.vetoapp.Adapters;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vetoapp.R;
import com.example.vetoapp.dao.AnimalDao;
import com.example.vetoapp.models.Animal;
import com.example.vetoapp.ui.activities.UpdateAnimalActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private List<Animal> animals;
    private AnimalDao animalDao;
    private ExecutorService executorService; // Use a thread pool for background tasks
    private static final int USER_ID = 1; // Static user ID for filtering and adding animals

    public AnimalAdapter(List<Animal> animals, AnimalDao animalDao) {
        this.animals = animals;
        this.animalDao = animalDao;
        this.executorService = Executors.newFixedThreadPool(3); // Using a fixed thread pool
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = animals.get(position);
        holder.nameTextView.setText(animal.getName());
        holder.typeTextView.setText(animal.getType());
        holder.ageTextView.setText(animal.getAge());
        holder.sexTextView.setText(animal.getSex());
        holder.weightTextView.setText(animal.getWeight());
        holder.behaviorTextView.setText(animal.getBehavior());

        // Set up the delete button click listener
        holder.deleteButton.setOnClickListener(v -> deleteAnimal(animal, position));

        // Set up the edit button click listener
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), UpdateAnimalActivity.class);
            intent.putExtra("ANIMAL_ID", animal.getId());
            holder.itemView.getContext().startActivity(intent);
            Log.d("AnimalAdapter", "Edit button clicked for animal ID: " + animal.getId());
        });
    }

    @Override
    public int getItemCount() {
        return animals != null ? animals.size() : 0;
    }

    public void updateData(List<Animal> allAnimals) {
        this.animals = new ArrayList<>();
        for (Animal animal : allAnimals) {
            if (animal.getUserid() == USER_ID) {
                this.animals.add(animal); // Only add animals for the specific user ID
            }
        }
        notifyDataSetChanged();
    }


    private void deleteAnimal(Animal animal, int position) {
        executorService.execute(() -> {
            try {
                if (animal.getUserid() == USER_ID) { // Ensure only user-specific animals are deleted
                    animalDao.delete(animal);
                    animals.remove(position);
                    new Handler(Looper.getMainLooper()).post(() -> notifyItemRemoved(position));
                }
            } catch (Exception e) {
                Log.e("AnimalAdapter", "Error deleting animal", e);
            }
        });
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, typeTextView, ageTextView, sexTextView, weightTextView, behaviorTextView;
        FloatingActionButton deleteButton, editButton;

        public AnimalViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.animalName);
            typeTextView = itemView.findViewById(R.id.animalType);
            ageTextView = itemView.findViewById(R.id.animalAge);
            sexTextView = itemView.findViewById(R.id.animalSex);
            weightTextView = itemView.findViewById(R.id.animalWeight);
            behaviorTextView = itemView.findViewById(R.id.animalBehavior);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton); // Link the edit button
        }
    }

    // Optional: Shutdown the executor service when it's no longer needed
    public void shutdownExecutor() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
