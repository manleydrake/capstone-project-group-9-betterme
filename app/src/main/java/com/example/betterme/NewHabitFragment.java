package com.example.betterme;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewHabitFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ((MainActivity) getActivity()).setActionBarTitle("New Habit");

        return inflater.inflate(R.layout.fragment_new_habit, container, false);
    }
}
