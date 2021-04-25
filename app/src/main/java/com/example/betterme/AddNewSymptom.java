package com.example.betterme;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.Adapter.SymptomAdapter;
import com.example.betterme.Model.SymptomModel;
import com.example.betterme.Utils.DataHelper;
import com.example.betterme.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AddNewSymptom extends BottomSheetDialogFragment{

    public static final String TAG = "ActionBottomDialog";

    private EditText newSymptomText, newSymptomStartDate, newSymptomEndDate;
    private Button newSymptomSaveButton;
    private DatabaseHandler db;
    private List<SymptomModel> symptomList;
    private SymptomAdapter symptomsAdapter;
    public static RecyclerView symptomsRecyclerView = SymptomsFragment.symptomsRecyclerView;
    public static DataHelper dataHelper = MainActivity.dataHelper;
    //private TimePicker simpleTimePicker;

    public static AddNewSymptom newInstance(){
        return new AddNewSymptom();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_new_symptom, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        symptomList = new ArrayList<>();
        symptomsAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        //simpleTimePicker =getView().findViewById(R.id.timePicker1);
        //simpleTimePicker.setIs24HourView(false);
        newSymptomText = getView().findViewById(R.id.newSymptomText);
        newSymptomStartDate = getView().findViewById(R.id.newSymptomStartDate);
        newSymptomEndDate = getView().findViewById(R.id.newSymptomEndDate);
        newSymptomSaveButton = getView().findViewById(R.id.newSymptomSave);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        //checks if we are adding or updating symptom
        boolean isUpdate = false;

        //getArguments to pass any data from adapters to fragments
        final Bundle bundle = getArguments();
        if(bundle != null){
            Log.d("AddNewSymptom", "Bundle not null");
            isUpdate = true;
            String symptom = bundle.getString("symptomName");
            newSymptomText.setText(symptom);
            assert symptom != null;
            //>0 then text exists and we want to save to be valid
            if(symptom.length()>0){
                //ToDo: change color to be theme
                newSymptomSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.LightBlue));
            }

        }

        newSymptomText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check if the symptom is empty or not
                //do not enable button if it is empty
                if(s.toString().equals("")){
                    newSymptomSaveButton.setEnabled(false);
                    newSymptomSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newSymptomSaveButton.setEnabled(true);
                    newSymptomSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.LightBlue));
                }

            }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });

    boolean finalIsUpdate = isUpdate;
    newSymptomSaveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //check if we are trying to update and existing symptom or enter a new one
            String text = newSymptomText.getText().toString();
            String startDate = newSymptomStartDate.getText().toString();
            String endDate = newSymptomEndDate.getText().toString();
            if(finalIsUpdate){
                db.updateSymptom(bundle.getInt("symptomID"), text);
            }
            else{
                SymptomModel symptom = new SymptomModel();
                symptom.setSymptom(text);
                symptom.setRating(0);
                symptom.setSymptomTrackDate(dataHelper.getSymptomCurrDate());
                symptom.setSymptomStartDate(startDate);
                symptom.setSymptomEndDate(endDate);
                db.insertSymptom(symptom);
                try {
                    updateSymptoms();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
        }

    });
    /**
        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                Toast.makeText(getContext(), hourOfDay + "  " + minute, Toast.LENGTH_SHORT).show();

            }
        });**/
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            try {
                ((DialogCloseListener)activity).handleDialogClose(dialog);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateSymptoms() throws ParseException {
        symptomsAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        symptomsRecyclerView.setAdapter(symptomsAdapter);
        symptomList = db.getAllSymptoms();
        Collections.reverse(symptomList);
        symptomsAdapter.setSymptoms(symptomList);
        symptomsAdapter.notifyDataSetChanged();
    }
}
