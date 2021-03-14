package com.example.betterme.Adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.MainActivity;
import com.example.betterme.Model.HabitModel;
import com.example.betterme.R;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private List<HabitModel> habitList;
    private MainActivity activity;

    public HabitAdapter(MainActivity activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        HabitModel item = habitList.get(position);
        holder.habit.setText(item.getHabit());
        holder.habit.setChecked(toBoolean(item.getStatus()));
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox habit;
        ViewHolder(View view){
            super(view);
            habit = view.findViewById(R.id.habitCheckBox);
        }
    }
}
