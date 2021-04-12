package com.example.betterme;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
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
import com.example.betterme.Model.HabitModel;
import com.example.betterme.Utils.DataHelper;
import com.example.betterme.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddNewHabit extends BottomSheetDialogFragment {

    //tags are used for debugging
    public static final String TAG = "ActionBottomDialog";

    private EditText newHabitText;
    private EditText newHabitStartDate;
    private EditText newHabitEndDate;
    private Button newHabitSaveButton;
    private DatabaseHandler db;
    private List<HabitModel> habitList;
    private HabitAdapter habitsAdapter;
    public static RecyclerView habitsRecyclerView = HabitsFragment.habitsRecyclerView;
    public static DataHelper dataHelper = MainActivity.dataHelper;

    public static AddNewHabit newInstance(){
        return new AddNewHabit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_new_habit, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        habitList = new ArrayList<>();
        habitsAdapter = new HabitAdapter(db, (MainActivity) this.getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newHabitText = getView().findViewById(R.id.newHabitText);
        newHabitStartDate = getView().findViewById(R.id.newHabitStartDate);
        newHabitEndDate = getView().findViewById(R.id.newHabitEndDate);
        newHabitSaveButton = getView().findViewById(R.id.newHabitSave);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        //checks if we are trying to update a habit or trying to create a new habit
        boolean isUpdate = false;
        //getArguments is used to pass any data from adapters to fragments
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String habit = bundle.getString("habitName");
            newHabitText.setText(habit);
            assert habit != null;
            //if the habit is greater than 0 then text exists and we want save to be a valid option
            if(habit.length()>0){
                //ToDo: change this color to be in line with a theme
                newHabitSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.LightBlue));
            }

        }

        //listen to changes when we are writing a new habit
        //we want to change the color of the save button according to if there has been text written
        //we do not want to enter empty tasks into database, so button will not be enabled if the text
        //length is 0
        newHabitText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check if the habit is empty or not
                //do not enable button if it is empty
                if(s.toString().equals("")){
                    newHabitSaveButton.setEnabled(false);
                    newHabitSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newHabitSaveButton.setEnabled(true);
                    newHabitSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.LightBlue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newHabitStartDate.addTextChangedListener(new DateTextWatcher());
        newHabitEndDate.addTextChangedListener(new DateTextWatcher());

        boolean finalIsUpdate = isUpdate;
        newHabitSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if we are trying to update an existing habit or enter a new habit
                String text = newHabitText.getText().toString();
                String startDate = newHabitStartDate.getText().toString();
                String endDate = newHabitEndDate.getText().toString();
                if(finalIsUpdate){
                    //ToDo: make this update the start and end date as well potentially
                    db.updateHabit(bundle.getInt("habitID"), text);
                }
                else{
                    HabitModel habit = new HabitModel();
                    habit.setHabit(text);
                    habit.setStatus(0);
                    habit.setHabitTrackDate(dataHelper.getHabitCurrDate());
                    habit.setHabitStartDate(startDate);
                    habit.setHabitEndDate(endDate);
                    db.insertHabit(habit);
                    try {
                        updateHabits();
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

    public void updateHabits() throws ParseException {
        habitsAdapter = new HabitAdapter(db, (MainActivity) this.getActivity());
        habitsRecyclerView.setAdapter(habitsAdapter);
        habitList = db.getAllHabits();
        Collections.reverse(habitList);
        habitsAdapter.setHabits(habitList);
        habitsAdapter.notifyDataSetChanged();
    }
}
