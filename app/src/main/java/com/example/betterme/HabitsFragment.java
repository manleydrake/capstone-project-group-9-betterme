package com.example.betterme;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.betterme.Adapter.HabitAdapter;
import com.example.betterme.Model.HabitModel;
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

public class HabitsFragment extends Fragment implements DialogCloseListener {

    //define variables
    public static RecyclerView habitsRecyclerView;
    private HabitAdapter habitsAdapter;

    private List<HabitModel> habitList;
    private DatabaseHandler db;

    private FloatingActionButton habitAddButton;

    private TextView habitDateDisplay;
    private ImageButton habitDatePrev;
    private ImageButton habitDateNext;

    public static DataHelper dataHelper = MainActivity.dataHelper;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ((MainActivity) getActivity()).setActionBarTitle("Habits");
        View v = inflater.inflate(R.layout.fragment_habits, container, false);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        habitDateDisplay = v.findViewById(R.id.habitDateDisplay);
        habitDateDisplay.setText(formatter.format(date));
        dataHelper.setHabitCurrDate((String) habitDateDisplay.getText());

        habitDatePrev = v.findViewById(R.id.habitDatePrevious);
        habitDateNext = v.findViewById(R.id.habitDateNext);

        db = new DatabaseHandler(this.getContext());
        db.openDatabase();

        habitList = new ArrayList<>();

        habitsRecyclerView = v.findViewById(R.id.habitsRecyclerView);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitsAdapter = new HabitAdapter(db, (MainActivity) this.getActivity());
        habitsRecyclerView.setAdapter(habitsAdapter);

        habitAddButton = v.findViewById(R.id.newHabitButton);

        try {
            habitList = db.getAllHabits();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Collections.reverse(habitList);
        habitsAdapter.setHabits(habitList);

        habitAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AddNewHabit.newInstance().show(getFragmentManager(), AddNewHabit.TAG);
            }
        });

        habitDatePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date currDate = formatter.parse((String) habitDateDisplay.getText());
                    LocalDateTime ldt = LocalDateTime.ofInstant(currDate.toInstant(), ZoneId.systemDefault()).minusDays(1);
                    Date newDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                    habitDateDisplay.setText(formatter.format(newDate));
                    dataHelper.setHabitCurrDate((String) habitDateDisplay.getText());
                    updateHabits();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        habitDateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date currDate = formatter.parse((String) habitDateDisplay.getText());
                    LocalDateTime ldt = LocalDateTime.ofInstant(currDate.toInstant(), ZoneId.systemDefault()).plusDays(1);
                    Date newDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                    habitDateDisplay.setText(formatter.format(newDate));
                    dataHelper.setHabitCurrDate((String) habitDateDisplay.getText());
                    updateHabits();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });



        return v;
    }

    public void updateHabits() throws ParseException {
        habitsAdapter = new HabitAdapter(db, (MainActivity) this.getActivity());
        habitsRecyclerView.setAdapter(habitsAdapter);
        habitList = db.getAllHabits();
        Collections.reverse(habitList);
        habitsAdapter.setHabits(habitList);
        habitsAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) throws ParseException {
        habitList = db.getAllHabits();
        Collections.reverse(habitList);
        habitsAdapter.setHabits(habitList);
        habitsAdapter.notifyDataSetChanged();
    }
}
