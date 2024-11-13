package com.example.vetoapp.models;

public class Dossier {
    private String nomAnimal;
    private String sexe;
    private String date_naissance ;
    private  String espece ;
    private double poids ;

    public Dossier(String nomAnimal, String sexe, String date_naissance, String espece, double poids) {
        this.nomAnimal = nomAnimal;
        this.sexe = sexe;
        this.date_naissance = date_naissance;
        this.espece = espece;
        this.poids = poids;



    }
    public String getNomAnimal() {
        return nomAnimal;
    }

    public void setNomAnimal(String nomAnimal) {
        this.nomAnimal = nomAnimal;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance (String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getEspece() {
        return espece;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }



}
