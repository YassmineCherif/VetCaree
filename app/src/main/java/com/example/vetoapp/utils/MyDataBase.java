package com.example.vetoapp.utils;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.vetoapp.dao.AnimalDao;
import com.example.vetoapp.models.Animal;

import androidx.room.Database;

@Database(entities = {Animal.class}, version = 1, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    public abstract AnimalDao animalDao();

    private static volatile MyDataBase instance;

    public static MyDataBase getInstance(final Context context) {
        if (instance == null) {
            synchronized (MyDataBase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    MyDataBase.class, "vet-animal_db")
                            .allowMainThreadQueries() // Consider using background threads for long-running operations
                            .build();
                }
            }
        }
        return instance;
    }
}

