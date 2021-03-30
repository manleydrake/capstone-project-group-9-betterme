package com.example.betterme.Model;

public class SymptomModel {
    private int id, status, rating;
    private String symptom;
    private String symptomTrackDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSymptomTrackDate(){
        return symptomTrackDate;
    }

    public void setSymptomTrackDate(String symptomTrackDate){
        this.symptomTrackDate = symptomTrackDate;
    }
}