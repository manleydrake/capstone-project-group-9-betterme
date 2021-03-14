package com.example.betterme.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.MainActivity;
import com.example.betterme.Model.SymptomModel;
import com.example.betterme.R;

import java.util.List;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ViewHolder> {

    private List<SymptomModel> symptomList;
    private MainActivity activity;

    public SymptomAdapter(MainActivity activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.symptom_layout, parent, false);
        return new ViewHolder(itemView);
    }
    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setSymptoms(List<SymptomModel> symptomList){
        this.symptomList = symptomList;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ViewHolder holder, int postion){
        SymptomModel symptom = symptomList.get(postion);
        holder.symptom.setText(symptom.getSymptom());
        holder.symptom.setChecked(toBoolean(symptom.getStatus()));
    }


    public int getItemCount(){
        int size = symptomList.size();
        return size;
    }
   
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox symptom;

        ViewHolder(View view){
            super(view);
            symptom = view.findViewById(R.id.symptomsCheckBox);
        }
    }


}
