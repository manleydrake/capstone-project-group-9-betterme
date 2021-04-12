package com.example.betterme.Model;

public class SymptomModel {
    private int id, status, rating;
    private String symptom;
    private String symptomTrackDate, symptomStartDate, symptomEndDate;

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

    public String getSymptomStartDate(){
        return symptomStartDate;
    }

    public void setSymptomStartDate(String symptomStartDate){
        this.symptomStartDate = symptomStartDate;
    }

    public String getSymptomEndDate(){
        return symptomEndDate;
    }

    public void setSymptomEndDate(String symptomEndDate){
        this.symptomEndDate = symptomEndDate;
    }
}