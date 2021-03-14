package com.example.betterme;

import android.content.DialogInterface;
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
import com.example.betterme.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HabitsFragment extends Fragment implements DialogCloseListener {

    //define variables
    private RecyclerView habitsRecyclerView;
    private HabitAdapter habitsAdapter;

    private List<HabitModel> habitList;
    private DatabaseHandler db;

    private FloatingActionButton habitAddButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ((MainActivity) getActivity()).setActionBarTitle("Habits");
        View v = inflater.inflate(R.layout.fragment_habits, container, false);

        db = new DatabaseHandler(this.getContext());
        db.openDatabase();

        habitList = new ArrayList<>();

        habitsRecyclerView = v.findViewById(R.id.habitsRecyclerView);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitsAdapter = new HabitAdapter(db, (MainActivity) this.getActivity());
        habitsRecyclerView.setAdapter(habitsAdapter);

        habitAddButton = v.findViewById(R.id.newHabitButton);

        //ToDo: check if this works with getFragmentManager instead of getSupportFragmentManager
        habitAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AddNewHabit.newInstance().show(getFragmentManager(), AddNewHabit.TAG);
            }
        });

        habitList = db.getAllHabits();
        Collections.reverse(habitList);
        habitsAdapter.setHabits(habitList);


        /**define some dummy data for now
        HabitModel habit = new HabitModel();
        habit.setHabit("This is our test habit");
        habit.setStatus(0);
        habit.setId(1);
        habitList.add(habit);
        habitsAdapter.setHabits(habitList);**/

        return v;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        habitList = db.getAllHabits();
        Collections.reverse(habitList);
        habitsAdapter.setHabits(habitList);
        habitsAdapter.notifyDataSetChanged();
    }
}
