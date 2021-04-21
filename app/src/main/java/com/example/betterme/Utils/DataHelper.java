package com.example.betterme.Utils;

public class DataHelper {
    private String habitCurrDate, symptomCurrDate, historySelectedDate;

    public String getHabitCurrDate(){
        return habitCurrDate;
    }

    public void setHabitCurrDate(String habitCurrDate){
        this.habitCurrDate = habitCurrDate;
    }

    public String getSymptomCurrDate(){
        return symptomCurrDate;
    }

    public void setSymptomCurrDate(String symptomCurrDate){
        this.symptomCurrDate = symptomCurrDate;
    }

    public String getHistorySelectedDate() {
        return historySelectedDate;
    }

    public void setHistorySelectedDate(String historySelectedDate){
        this.historySelectedDate = historySelectedDate;
    }
}
