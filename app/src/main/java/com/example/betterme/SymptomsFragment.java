package com.example.betterme;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.Adapter.SymptomAdapter;
import com.example.betterme.Model.SymptomModel;
import com.example.betterme.Utils.DataHelper;
import com.example.betterme.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SymptomsFragment extends Fragment implements DialogCloseListener{

    public static RecyclerView symptomsRecyclerView;
    private SymptomAdapter symptomAdapter;

    private List<SymptomModel> symptomList;
    private DatabaseHandler db;

    private FloatingActionButton symptomAddButton;

    private TextView symptomDateDisplay;
    private ImageButton symptomDatePrev;
    private ImageButton symptomDateNext;

    public static DataHelper dataHelper = MainActivity.dataHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ((MainActivity) getActivity()).setActionBarTitle("Symptoms");
        View v = inflater.inflate(R.layout.fragment_symptoms, container, false);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        symptomDateDisplay = (TextView) v.findViewById(R.id.symptomDateDisplay);
        symptomDateDisplay.setText(formatter.format(date));
        dataHelper.setSymptomCurrDate((String) symptomDateDisplay.getText());

        Log.d("SymptomsFragment", "date display: " + symptomDateDisplay.getText());

        symptomDatePrev = v.findViewById(R.id.symptomDatePrevious);
        symptomDateNext = v.findViewById(R.id.symptomDateNext);

        db = new DatabaseHandler(this.getContext());
        db.openDatabase();

        if(symptomList != null && symptomList.size() !=0) {
            symptomList = new ArrayList<>();
        }

        symptomsRecyclerView = v.findViewById(R.id.symptomsRecyclerView);
        symptomsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        symptomAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        symptomsRecyclerView.setAdapter(symptomAdapter);

        symptomAddButton = v.findViewById(R.id.newSymptomButton);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperSymptom(symptomAdapter));
        itemTouchHelper.attachToRecyclerView(symptomsRecyclerView);

        try {
            symptomList = db.getAllSymptoms(dataHelper.getSymptomCurrDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Collections.reverse(symptomList);
        symptomAdapter.setSymptoms(symptomList);

        symptomAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AddNewSymptom.newInstance().show(getFragmentManager(), AddNewSymptom.TAG);
            }
        });

        symptomDatePrev.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    Date currDate = formatter.parse((String) symptomDateDisplay.getText());
                    LocalDateTime ldt = LocalDateTime.ofInstant(currDate.toInstant(), ZoneId.systemDefault()).minusDays(1);
                    Date newDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                    symptomDateDisplay.setText(formatter.format(newDate));
                    dataHelper.setSymptomCurrDate((String) symptomDateDisplay.getText());

                    updateSymptoms();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        symptomDateNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    Date currDate = formatter.parse((String) symptomDateDisplay.getText());
                    LocalDateTime ldt = LocalDateTime.ofInstant(currDate.toInstant(), ZoneId.systemDefault()).plusDays(1);
                    Date newDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                    symptomDateDisplay.setText(formatter.format(newDate));
                    dataHelper.setSymptomCurrDate((String) symptomDateDisplay.getText());
                    updateSymptoms();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        return v;
    }

    public void updateSymptoms() throws ParseException {
        symptomAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        symptomsRecyclerView.setAdapter(symptomAdapter);
        symptomList = db.getAllSymptoms(dataHelper.getSymptomCurrDate());
        Collections.reverse(symptomList);
        symptomAdapter.setSymptoms(symptomList);
        symptomAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) throws ParseException {
        symptomList = db.getAllSymptoms(dataHelper.getSymptomCurrDate());
        Collections.reverse(symptomList);
        symptomAdapter.setSymptoms(symptomList);
        symptomAdapter.notifyDataSetChanged();
    }
}
