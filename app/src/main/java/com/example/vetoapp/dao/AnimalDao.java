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

    @Query("SELECT * FROM animalTable")
    List<Animal> getAllAnimals();

    @Query("SELECT * FROM animalTable WHERE id = :id LIMIT 1")
    Animal getAnimalById(int id);
}
