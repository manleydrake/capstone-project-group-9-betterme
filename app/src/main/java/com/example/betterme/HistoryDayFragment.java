package com.example.betterme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.Adapter.HabitAdapter;
import com.example.betterme.Adapter.SymptomAdapter;
import com.example.betterme.Model.HabitModel;
import com.example.betterme.Model.SymptomModel;
import com.example.betterme.Utils.DataHelper;
import com.example.betterme.Utils.DatabaseHandler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryDayFragment extends Fragment {

    //initialize variables
    public static RecyclerView historyHabitRecyclerView;
    private HabitAdapter habitsAdapter;

    public static RecyclerView historySymptomsRecyclerView;
    private SymptomAdapter symptomAdapter;

    private List<HabitModel> habitList;
    private List<SymptomModel> symptomList;
    private DatabaseHandler db;
    private ImageButton backButton;

    public static DataHelper dataHelper = MainActivity.dataHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v =  inflater.inflate(R.layout.fragment_history_day, container, false);

        db = new DatabaseHandler(this.getContext());
        db.openDatabase();

        habitList = new ArrayList<>();

        backButton = v.findViewById(R.id.historyDayBack);

        historyHabitRecyclerView = v.findViewById(R.id.historyHabitRecyclerView);
        historyHabitRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitsAdapter = new HabitAdapter(db, (MainActivity) this.getActivity());
        historyHabitRecyclerView.setAdapter(habitsAdapter);

        symptomList = new ArrayList<>();

        historySymptomsRecyclerView = v.findViewById(R.id.historySymptomsRecyclerView);
        historySymptomsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        symptomAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        historySymptomsRecyclerView.setAdapter(symptomAdapter);

        try {
            habitList = db.getAllHabits(dataHelper.getHistorySelectedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Collections.reverse(habitList);
        habitsAdapter.setHabits(habitList);

        try {
            symptomList = db.getAllSymptoms(dataHelper.getHistorySelectedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Collections.reverse(symptomList);
        symptomAdapter.setSymptoms(symptomList);

        //move back to History screen when back button is pressed
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
            }
        });

        return v;
    }

}
