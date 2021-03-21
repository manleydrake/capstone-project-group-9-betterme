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
import com.example.betterme.Adapter.SymptomAdapter;
import com.example.betterme.Model.HabitModel;
import com.example.betterme.Model.SymptomModel;
import com.example.betterme.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SymptomsFragment extends Fragment implements DialogCloseListener{

    public static RecyclerView symptomsRecyclerView;
    private SymptomAdapter symptomAdapter;

    private List<SymptomModel> symptomList;
    private DatabaseHandler db;

    private FloatingActionButton symptomAddButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ((MainActivity) getActivity()).setActionBarTitle("Symptoms");
        View v = inflater.inflate(R.layout.fragment_symptoms, container, false);

        db = new DatabaseHandler(this.getContext());
        db.openDatabase();

        symptomList = new ArrayList<>();

        symptomsRecyclerView = v.findViewById(R.id.symptomsRecyclerView);
        symptomsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        symptomAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        symptomsRecyclerView.setAdapter(symptomAdapter);

        symptomAddButton = v.findViewById(R.id.newSymptomButton);

        symptomList = db.getAllSymptoms();
        Collections.reverse(symptomList);
        symptomAdapter.setSymptoms(symptomList);

        symptomAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AddNewSymptom.newInstance().show(getFragmentManager(), AddNewSymptom.TAG);
            }
        });
        return v;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        symptomList = db.getAllSymptoms();
        Collections.reverse(symptomList);
        symptomAdapter.setSymptoms(symptomList);
        symptomAdapter.notifyDataSetChanged();
    }
}
