package com.example.vetoapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "animalTable")
public class Animal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id; // ID will auto-generate

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "age")
    private String age;

    @ColumnInfo(name = "sex")
    private String sex;

    @ColumnInfo(name = "weight")
    private String weight;

    @ColumnInfo(name = "behavior")
    private String behavior;

    // Default constructor required by Room
    public Animal() {
    }

    @Ignore
    // Constructor with parameters
    public Animal(String name, String type, String age, String sex, String weight, String behavior) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.sex = sex;
        this.weight = weight;
        this.behavior = behavior;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
