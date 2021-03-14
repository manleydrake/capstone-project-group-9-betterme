package com.example.betterme.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.AddNewHabit;
import com.example.betterme.MainActivity;
import com.example.betterme.Model.HabitModel;
import com.example.betterme.R;
import com.example.betterme.Utils.DatabaseHandler;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private List<HabitModel> habitList;
    private MainActivity activity;
    private DatabaseHandler db;

    public HabitAdapter(DatabaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        HabitModel item = habitList.get(position);
        holder.habit.setText(item.getHabit());
        holder.habit.setChecked(toBoolean(item.getStatus()));
        //anytime we check a habit complete or not we need to update the database
        //ToDo: in the symptom version we can do a listener and check if the text value of a
        // box isn't ""
        holder.habit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateHabitStatus(item.getId(), 1);
                }
                else{
                    db.updateHabitStatus(item.getId(), 0);
                }
            }
        });
    }

    public int getItemCount(){
        return habitList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setHabits(List<HabitModel> habitList){
        this.habitList = habitList;
        //this forces the recycler view to update
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void setHabitList(List<HabitModel> habitList){
        this.habitList = habitList;
    }

    public void editItem(int position){
        HabitModel item = habitList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("habitID", item.getId());
        bundle.putString("habitName", item.getHabit());
        AddNewHabit fragment = new AddNewHabit();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewHabit.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox habit;
        ViewHolder(View view){
            super(view);
            habit = view.findViewById(R.id.habitCheckBox);
        }
    }
}
