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
        //connect variables to UI
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
        // if the bundle is not null then we are updating a symptom instead of adding a new one
        if(bundle != null){
            Log.d("AddNewSymptom", "Bundle not null");
            isUpdate = true;
            String symptom = bundle.getString("symptomName");
            newSymptomText.setText(symptom);
            Log.d("AddNewSymptom", "edit symptom name: " + symptom);

            //assert symptom != null;

            //>0 then text exists and we want the save to be a valid option
            if(symptom.length()>0){
                newSymptomSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.LightBlue));
            }

        }

        //format dates to MM/DD/YYYY
        newSymptomStartDate.addTextChangedListener(new DateTextWatcher());
        newSymptomEndDate.addTextChangedListener(new DateTextWatcher());

        //listen if the user will alter the symptom name text
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

    //isUpdate dictates if we are adding a new symptom (False) or updating an existing (True)
    boolean finalIsUpdate = isUpdate;

    //listen for the user to press Save
    newSymptomSaveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = newSymptomText.getText().toString();
            String startDate = newSymptomStartDate.getText().toString();
            String endDate = newSymptomEndDate.getText().toString();

            //check if we are trying to update and existing symptom or enter a new one
            //give information to database
            if(finalIsUpdate){
                db.updateSymptom(bundle.getInt("symptomID"), text, startDate, endDate);
                try {
                    updateSymptoms();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //if not updating, add a new symptom list item
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

    //forces the UI to refresh and show any new symptoms/delete old ones
    public void updateSymptoms() throws ParseException {
        symptomsAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        symptomsRecyclerView.setAdapter(symptomsAdapter);
        symptomList = db.getAllSymptoms();
        Collections.reverse(symptomList);
        symptomsAdapter.setSymptoms(symptomList);
        symptomsAdapter.notifyDataSetChanged();
    }
}
