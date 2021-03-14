package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.Adapter.SymptomAdapter;
import com.example.betterme.Model.SymptomModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView symptomsRecyclerView;
    private SymptomAdapter symAdapter;

    private List<SymptomModel> symList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //remove the header for now
        getSupportActionBar().hide();

        /**
       symAdapter = new SymptomAdapter(this);
       symptomsRecyclerView.setAdapter(symAdapter);

       SymptomModel sym = new SymptomModel();
       sym.setSymptom("This is a new symptom");
       sym.setStatus(0);
       sym.setId(1);

//This is an example list
       symList.add(sym);
       symList.add(sym);
       symAdapter.setSymptoms(symList);**/

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HabitsFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_habits:
                            selectedFragment = new HabitsFragment();
                            break;
                        case R.id.nav_symptoms:
                            selectedFragment = new SymptomsFragment();
                            break;
                        case R.id.nav_history:
                            selectedFragment = new HistoryFragment();
                            break;
                    }

                    //replace the fragment view
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    //true indicates we want to select the menu item
                    return true;
                }
            };

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

}