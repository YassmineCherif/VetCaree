package com.example.vetoapp.models;

public class Appointment {
    private String date;
    private String time;
    private String serviceType;

    public Appointment(String date, String time, String serviceType) {
        this.date = date;
        this.time = time;
        this.serviceType = serviceType;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
