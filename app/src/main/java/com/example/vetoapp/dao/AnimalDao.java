package com.example.vetoapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.example.vetoapp.models.Animal;

@Dao
public interface AnimalDao {
    @Insert
    void insert(Animal animal);

    @Update
    void update(Animal animal);

    @Delete
    void delete(Animal animal);

    @Query("DELETE FROM animalTable WHERE id = :animalId")
    void deleteById(int animalId);  // Suppression par ID

    @Query("SELECT * FROM animalTable WHERE id = :animalId")
    Animal getAnimalById(int animalId);  // Récupération d'un animal par ID

    @Query("SELECT * FROM animalTable")
    List<Animal> getAllAnimals();

    // Get animal by name
    @Query("SELECT * FROM animalTable WHERE name = :name LIMIT 1")
    Animal getAnimalByName(String name);

}
