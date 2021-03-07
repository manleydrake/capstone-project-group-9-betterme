package com.example.betterme;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    Button newHabit;
    ListView habitListView;
    ArrayAdapter<String> habitAdapter;
    ArrayList<String> habitList;
    Button newSymptom;
    ListView symptomListView;
    ArrayAdapter<String> symptomAdapter;
    ArrayList<String> symptomList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("BetterMe");

        newHabit = (Button) v.findViewById(R.id.add_habit_button);
        newHabit.setOnClickListener((View.OnClickListener) this);

        habitListView = v.findViewById(R.id.habit_list);
        habitList = new ArrayList<>();

        habitAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, habitList);
        habitListView.setAdapter(habitAdapter);

        newSymptom = (Button) v.findViewById(R.id.add_symptom_button);
        newSymptom.setOnClickListener((View.OnClickListener) this);

        symptomListView = v.findViewById(R.id.symptom_list);
        symptomList = new ArrayList<>();

        symptomAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, symptomList);
        symptomListView.setAdapter(symptomAdapter);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_habit_button:
                Log.d("HomeFragment", "Add a new habit");
                final EditText habitEditText = new EditText(this.getContext());
                AlertDialog dialog = new AlertDialog.Builder(this.getContext())
                        .setTitle("Add a new habit")
                        .setMessage("Which habit would you like to implement next?")
                        .setView(habitEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String habit = String.valueOf(habitEditText.getText());
                                Log.d("HomeFragment", "Habit to add: " + habit);
                                habitList.add(habit);
                                habitAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            case R.id.add_symptom_button:
                Log.d("HomeFragment", "Add a new symptom");
                final EditText symptomEditText = new EditText(this.getContext());
                AlertDialog dialog2 = new AlertDialog.Builder(this.getContext())
                        .setTitle("Add a new symptom")
                        .setMessage("Which symptom would you like to log next?")
                        .setView(symptomEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String symptom = String.valueOf(symptomEditText.getText());
                                Log.d("HomeFragment", "Symptom to add: " + symptom);
                                symptomList.add(symptom);
                                symptomAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog2.show();
             //ToDo: add case here for if an add symptom button is clicked

            //ToDo: add case here for if an add note button is clicked
        }
    }

}
