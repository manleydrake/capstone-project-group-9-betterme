package com.example.betterme.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.betterme.Model.HabitModel;
import com.example.betterme.Model.SymptomModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String TAG = "Database";

    private static final int VERSION = 8;
    private static final String NAME = "habitDB";  //Database name

    //User Table variables
    private static final String USER_TABLE = "user";
    private static final String USER_ID = "userID";
    private static final String USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    //Creates table
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + USERNAME + " TEXT, " + USER_PASSWORD + " TEXT)";

    //Habit table variables
    private static final String HABIT_TABLE = "habitTable";
    private static final String HABIT_ID = "habitID";
    private static final String HABIT_NAME = "habitName";
    private static final String HABIT_START_DATE = "habitStart";
    private static final String HABIT_END_DATE = "habitEnd";

    //Create habit table
    private static final String CREATE_HABIT_TABLE = "CREATE TABLE " + HABIT_TABLE + "(" + HABIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " INTEGER, " + HABIT_NAME + " TEXT, " + HABIT_START_DATE + " TEXT, " + HABIT_END_DATE + " TEXT)";

    //Habit Tracking Table variables
    private static final String HABIT_TRACKING_TABLE = "habit_tracking";
    private static final String HABIT_TRACK_DATE = "habitTrackDate";
    private static final String HABIT_COMPLETE = "habitStatus";

    //Create habit tracking table
    private static final String CREATE_HABIT_TRACKING_TABLE = "CREATE TABLE " + HABIT_TRACKING_TABLE + "(" + HABIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HABIT_NAME + " TEXT, " + HABIT_TRACK_DATE + " TEXT, " + HABIT_COMPLETE + " INTEGER)";

    //Symptom Table variables
    private static final String SYMPTOM_TABLE = "symptomTable";
    private static final String SYMPTOM_ID = "symptomID";
    private static final String SYMPTOM_NAME = "symptomName";
    private static final String SYMPTOM_START_DATE = "symptomStart";
    private static final String SYMPTOM_END_DATE = "symptomEnd";

    // Create symptom table
    private static final String CREATE_SYMPTOM_TABLE = "CREATE TABLE " + SYMPTOM_TABLE + "(" + SYMPTOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " INTEGER, " + SYMPTOM_NAME + " TEXT, " + SYMPTOM_START_DATE + " TEXT, " + SYMPTOM_END_DATE + " TEXT)";

    //Symptom tracking table variables
    private static final String SYMPTOM_TRACKING_TABLE = "symptomTracking";
    private static final String SYMPTOM_TRACK_DATE = "symptomTrackDate";
    private static final String SYMPTOM_RATING = "symptomRating";

    //Create symptom tracking table variables
    private static final String CREATE_SYMPTOM_TRACKING_TABLE = "CREATE TABLE " + SYMPTOM_TRACKING_TABLE + "(" + SYMPTOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SYMPTOM_NAME + " TEXT, " + SYMPTOM_TRACK_DATE + " TEXT, " + SYMPTOM_RATING + " INTEGER)";


    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
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
        Log.d(TAG, "Dropping old tables");
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
        cv.put(HABIT_NAME, habit.getHabit());
        cv.put(HABIT_COMPLETE, 0);
        Log.d(TAG, "passing " + HABIT_NAME);
        Log.d(TAG, "passing " + HABIT_COMPLETE);
        Log.d(TAG, "cv is " + cv);
        db.insert(HABIT_TRACKING_TABLE, null, cv);
        Log.d(TAG, "database insertion successful" );
    }

    public void insertSymptom(SymptomModel symptom) {
        ContentValues cv = new ContentValues();
        cv.put(SYMPTOM_NAME, symptom.getSymptom());
        cv.put(SYMPTOM_RATING, 0);
        Log.d(TAG, "passing " + SYMPTOM_NAME);
        Log.d(TAG, "passing " + SYMPTOM_RATING);
        Log.d(TAG, "cv is " + cv);
        db.insert(SYMPTOM_TRACKING_TABLE, null, cv);
        Log.d(TAG, "database insertion successful" );

    }
    public List<HabitModel> getAllHabits(){
        List<HabitModel> habitList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(HABIT_TRACKING_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        HabitModel habit = new HabitModel();
                        habit.setId(cur.getInt(cur.getColumnIndex(HABIT_ID)));
                        habit.setHabit(cur.getString(cur.getColumnIndex(HABIT_NAME)));
                        habit.setStatus(cur.getInt(cur.getColumnIndex(HABIT_COMPLETE)));
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

    public List<SymptomModel> getAllSymptoms() {
        List<SymptomModel> symptomList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(SYMPTOM_TRACKING_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        SymptomModel symptom = new SymptomModel();
                        symptom.setId(cur.getInt(cur.getColumnIndex(SYMPTOM_ID)));
                        symptom.setSymptom(cur.getString(cur.getColumnIndex(SYMPTOM_NAME)));
                        symptom.setRating(cur.getInt(cur.getColumnIndex(SYMPTOM_RATING)));
                        symptomList.add(symptom);
                    }
                    while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return symptomList;
    }
    public void updateHabitStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(HABIT_COMPLETE, status);
        db.update(HABIT_TRACKING_TABLE, cv, HABIT_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateSymptomRating(int id, int rating) {
        ContentValues cv = new ContentValues();
        cv.put(SYMPTOM_RATING, rating);
        db.update(SYMPTOM_TRACKING_TABLE, cv, SYMPTOM_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateHabit(int id, String habit) {
        ContentValues cv = new ContentValues();
        cv.put(HABIT_NAME, habit);
        db.update(HABIT_TRACKING_TABLE, cv, HABIT_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateSymptom(int id, String symptom) {
        ContentValues cv = new ContentValues();
        cv.put(SYMPTOM_NAME, symptom);
        db.update(SYMPTOM_TRACKING_TABLE, cv, SYMPTOM_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteHabit(int id){
        db.delete(HABIT_TABLE, HABIT_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteSymptom(int id) {
        db.delete(SYMPTOM_TABLE, SYMPTOM_ID + "= ?", new String[] {String.valueOf(id)});
    }
}
