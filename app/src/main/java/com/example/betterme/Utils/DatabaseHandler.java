package com.example.betterme.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.betterme.Model.HabitModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "habitDB";  //Database name

    //User Table variables
    private static final String USER_TABLE = "User";
    private static final String userID = "userID";
    private static final String userName = "name";
    private static final String userPassword = "password";
    //Creates table
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" + userID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + userName + "TEXT, " + userPassword + " TEXT)";

    //Habit table variables
    private static final String HABIT_TABLE = "habit";
    private static final String habitID = "habitID";
    private static final String habitName = "name";
    private static final String habitStartDate = "start";
    private static final String habitEndDate = "end";

    //Create habit table
    private static final String CREATE_HABIT_TABLE = "CREATE TABLE " + HABIT_TABLE + "(" + habitID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + userID + "INTEGER" + habitName + "TEXT, " + habitStartDate + "TEXT, " + habitEndDate + "TEXT)";

    //Habit Tracking Table variables
    private static final String HABIT_TRACKING_TABLE = "habit_tracking";
    private static final String Date = "date";
    private static final String IsComplete = "false";

    //Create habit tracking table
    private static final String CREATE_HABIT_TRACKING_TABLE = "CREATE TABLE " + HABIT_TRACKING_TABLE + "(" + habitID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Date + "TEXT, " + IsComplete + " TEXT)";

    //Symptom Table variables
    private static final String SYMPTOM_TABLE = "symptom";
    private static final String symptomID = "symptomID";
    private static final String SymptomName = "name";
    private static final String symptomStartDate = "date";
    private static final String symptomEndDate = "date";

    // Create symptom table
    private static final String CREATE_SYMPTOM_TABLE = "CREATE TABLE " + SYMPTOM_TABLE + "(" + symptomID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + userID + "INTEGER" + SymptomName + "TEXT, " + symptomStartDate + "TEXT, " + symptomEndDate + " TEXT)";

    //Symptom tracking table variables
    private static final String SYMPTOM_TRACKING_TABLE = "symptom tracking";
    private static final String symptomTrackDate = "date";
    private static final String symptomRating = "rating";

    //Create symptom tracking table variables
    private static final String CREATE_SYMPTOM_TRACKING_TABLE = "CREATE TABLE " + SYMPTOM_TRACKING_TABLE + "(" + symptomID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + symptomTrackDate + "TEXT, " + symptomRating + " INTEGER)";


    private SQLiteDatabase db;

    private DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_HABIT_TABLE);
        db.execSQL(CREATE_HABIT_TRACKING_TABLE);
        db.execSQL(CREATE_SYMPTOM_TABLE);
        db.execSQL(CREATE_SYMPTOM_TRACKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drops older tables
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HABIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HABIT_TRACKING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SYMPTOM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SYMPTOM_TRACKING_TABLE);

        //Recreate the tables
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertHabit(HabitModel habit) {
        ContentValues cv = new ContentValues();
        cv.put(habitName, habit.getHabit());
        cv.put(IsComplete, 0);
        db.insert(HABIT_TABLE, null, cv);
    }
    public List<HabitModel> getAllHabits(){
        List<HabitModel> habitList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(HABIT_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        HabitModel habit = new HabitModel();
                        habit.setId(cur.getInt(cur.getColumnIndex(habitID)));
                        habit.setHabit(cur.getString(cur.getColumnIndex(habitName)));
                        habit.setStatus(cur.getInt(cur.getColumnIndex(IsComplete)));
                        habitList.add(habit);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return habitList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(IsComplete, status);
        db.update(HABIT_TABLE, cv, habitID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String habit) {
        ContentValues cv = new ContentValues();
        cv.put(habitName, habit);
        db.update(HABIT_TABLE, cv, habitID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(HABIT_TABLE, habitID + "= ?", new String[] {String.valueOf(id)});
    }
}
