package com.example.betterme.Model;

public class HabitModel {
    private int id, status;
    private String habit;
    private String habitTrackDate, habitStartDate, habitEndDate;

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

    public String getHabitTrackDate(){
        return habitTrackDate;
    }

    public void setHabitTrackDate(String habitTrackDate){
        this.habitTrackDate = habitTrackDate;
    }

    public String getHabitStartDate(){
        return habitStartDate;
    }

    public void setHabitStartDate(String habitStartDate){
        this.habitStartDate = habitStartDate;
    }

    public String getHabitEndDate(){
        return habitEndDate;
    }

    public void setHabitEndDate(String habitEndDate){
        this.habitEndDate = habitEndDate;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }
}

