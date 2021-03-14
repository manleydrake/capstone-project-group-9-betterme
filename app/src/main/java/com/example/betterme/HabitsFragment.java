package com.example.betterme;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.betterme.Adapter.HabitAdapter;
import com.example.betterme.Model.HabitModel;

import java.util.ArrayList;
import java.util.List;

public class HabitsFragment extends Fragment {

    //define variables
    private RecyclerView habitsRecyclerView;
    private HabitAdapter habitsAdapter;

    private List<HabitModel> habitList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ((MainActivity) getActivity()).setActionBarTitle("Habits");
        View v = inflater.inflate(R.layout.fragment_habits, container, false);

        habitList = new ArrayList<>();

        habitsRecyclerView = v.findViewById(R.id.habitsRecyclerView);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitsAdapter = new HabitAdapter((MainActivity) this.getActivity());
        habitsRecyclerView.setAdapter(habitsAdapter);

        //define some dummy data for now
        HabitModel habit = new HabitModel();
        habit.setHabit("This is our test habit");
        habit.setStatus(0);
        habit.setId(1);
        habitList.add(habit);
        habitsAdapter.setHabits(habitList);

        return v;
    }
}
