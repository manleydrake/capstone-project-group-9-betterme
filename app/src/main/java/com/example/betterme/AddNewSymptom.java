package com.example.betterme;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.Adapter.HabitAdapter;
import com.example.betterme.Adapter.SymptomAdapter;
import com.example.betterme.Model.SymptomModel;
import com.example.betterme.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Collections;
import java.util.List;


public class AddNewSymptom extends BottomSheetDialogFragment{

    public static final String TAG = "ActionBottomDialog";

    private EditText newSymtpomText;
    private Button newSymtpomSaveButton;
    private DatabaseHandler db;
    private List<SymptomModel> symptomList;
    private SymptomAdapter symtpomsAdapter;
    public static RecyclerView symptomsRecyclerView = SymptomsFragment.symptomsRecyclerView;

    public static AddNewSymptom newInstance(){
        return new AddNewSymptom();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_new_symptom, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newSymtpomText = getView().findViewById(R.id.newSymptomText);
        newSymtpomSaveButton = getView().findViewById(R.id.newSymptomButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        //checks if we are adding or updating symptom
        boolean isUpdate = false;

        //getArguments to pass any data from adapters to fragments
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String symptom = bundle.getString("symptomName");
            newSymtpomText.setText(symptom);
            //>0 then text exists and we want to save to be valid
            if(symptom.length()>0){
                //ToDo: change color to be theme
                newSymtpomSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.LightBlue));
            }

        }
    newSymtpomText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //check if the habit is empty or not
            //do not enable button if it is empty
            if(s.toString().equals("")){
                newSymtpomSaveButton.setEnabled(false);
                newSymtpomSaveButton.setTextColor(Color.GRAY);
            }
            else{
                newSymtpomSaveButton.setEnabled(true);
                newSymtpomSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.LightBlue));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });

    boolean finalIsUpdate = isUpdate;
    newSymtpomSaveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //check if we are trying to update and existing symptom or enter a new one
            String text = newSymtpomText.getText().toString();
            if(finalIsUpdate){
                db.updateSymptom(bundle.getInt("symptomID"), text);
            }
            else{
                SymptomModel symptom = new SymptomModel();
                symptom.setSymptom(text);
                symptom.setStatus(0);
                db.insertSymptom(symptom);
                updateSymptoms();
            }
            dismiss();
        }

    });
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }

    public void updateSymptoms(){
        symtpomsAdapter = new SymptomAdapter(db, (MainActivity) this.getActivity());
        symptomsRecyclerView.setAdapter(symtpomsAdapter);
        symptomList = db.getAllSymptoms();
        Collections.reverse(symptomList);
        symtpomsAdapter.setSymptoms(symptomList);
        symtpomsAdapter.notifyDataSetChanged();
    }
}
