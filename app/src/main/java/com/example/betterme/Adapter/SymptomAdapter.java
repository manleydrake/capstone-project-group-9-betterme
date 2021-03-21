package com.example.betterme.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.AddNewSymptom;
import com.example.betterme.MainActivity;
import com.example.betterme.Model.SymptomModel;
import com.example.betterme.R;
import com.example.betterme.Utils.DatabaseHandler;
import com.google.android.material.slider.Slider;

import java.util.List;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ViewHolder> {

    private List<SymptomModel> symptomList;
    private MainActivity activity;
    private DatabaseHandler db;
    private TextView symptomsText;


    public SymptomAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.symptom_layout, parent, false);
        symptomsText = itemView.findViewById(R.id.symptomsTextView);

        return new ViewHolder(itemView);
    }
    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setSymptoms(List<SymptomModel> symptomList){
        this.symptomList = symptomList;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        SymptomModel symptom = symptomList.get(position);
        symptomsText.setText(symptom.getSymptom());

        holder.symptom.setValue((float) symptom.getRating());
        holder.symptom.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(Slider slider) {
                // Responds to when slider's touch event is being started
            }

            @Override
            public void onStopTrackingTouch(Slider slider) {
                // Responds to when slider's touch event is being stopped
                db.updateSymptomRating(symptom.getId(), (int) slider.getValue());
            }
        });

    }

    public Context getContext(){
        return activity;
    }

    public void setSymptomList(List<SymptomModel> symptomList){
        this.symptomList = symptomList;
    }

    public void editSymptoms(int position){
        SymptomModel symptom = symptomList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("symptomID", symptom.getId());
        bundle.putString("symptomName", symptom.getSymptom());
        AddNewSymptom fragment = new AddNewSymptom();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewSymptom.TAG);
    }
    public int getItemCount(){
        int size = symptomList.size();
        return size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Slider symptom;

        ViewHolder(View view){
            super(view);
            symptom = view.findViewById(R.id.symptomSlider);
        }
    }



}
