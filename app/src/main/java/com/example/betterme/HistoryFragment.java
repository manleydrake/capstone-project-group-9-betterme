package com.example.betterme;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.betterme.Utils.DataHelper;

public class HistoryFragment extends Fragment {

    private CalendarView calendar;
    public static DataHelper dataHelper = MainActivity.dataHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v =  inflater.inflate(R.layout.fragment_history, container, false);

        calendar = v.findViewById(R.id.history_calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Log.d("History", "month:" + String.format("%02d", month+1));
                Log.d("History", "day:" + String.format("%02d", dayOfMonth));
                Log.d("History", "year:" + year);
                dataHelper.setHistorySelectedDate(String.format("%02d", month+1) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryDayFragment()).commit();
            }
        });

        return v;
    }
}
