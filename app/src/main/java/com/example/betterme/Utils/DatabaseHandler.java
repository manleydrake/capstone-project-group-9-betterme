package com.example.betterme.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.betterme.MainActivity;
import com.example.betterme.Model.HabitModel;
import com.example.betterme.Model.SymptomModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static DataHelper dataHelper = MainActivity.dataHelper;

    public static final String TAG = "Database";

    private static final int VERSION = 20;
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
    private static final String CREATE_HABIT_TRACKING_TABLE = "CREATE TABLE " + HABIT_TRACKING_TABLE + "(" + HABIT_NAME + " TEXT, " + HABIT_COMPLETE + " INTEGER, "
            + HABIT_TRACK_DATE + " TEXT, PRIMARY KEY (" + HABIT_NAME + ", " + HABIT_TRACK_DATE + "))";

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

    //Create symptom tracking table
    private static final String CREATE_SYMPTOM_TRACKING_TABLE = "CREATE TABLE " + SYMPTOM_TRACKING_TABLE + "(" + SYMPTOM_NAME + " TEXT, " + SYMPTOM_RATING + " INTEGER, "
            + SYMPTOM_TRACK_DATE + " TEXT, PRIMARY KEY (" + SYMPTOM_NAME + ", " + SYMPTOM_TRACK_DATE + "))";


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
        //ToDo: change this to be inserting into habit and habit tracking tables separately (remove unneccessary columns from habit tracking table)
        ContentValues cv = new ContentValues();
        cv.put(HABIT_NAME, habit.getHabit());
        cv.put(HABIT_START_DATE, habit.getHabitStartDate());
        cv.put(HABIT_END_DATE, habit.getHabitEndDate());
        Log.d(TAG, "passing " + HABIT_NAME);
        Log.d(TAG, "passing start date " + HABIT_START_DATE);
        Log.d(TAG, "passing end date " + HABIT_END_DATE);
        Log.d(TAG, "cv is " + cv);
        db.insert(HABIT_TABLE,null,cv);

        ContentValues cv2 = new ContentValues();
        cv2.put(HABIT_NAME, habit.getHabit());
        cv2.put(HABIT_COMPLETE, habit.getStatus());
        cv2.put(HABIT_TRACK_DATE, habit.getHabitTrackDate());
        Log.d(TAG, "passing " + HABIT_COMPLETE);
        Log.d(TAG, "passing " + HABIT_TRACK_DATE);
        db.insert(HABIT_TRACKING_TABLE, null, cv2);
        Log.d(TAG, "database insertion successful" );
    }

    public void insertSymptom(SymptomModel symptom) {
        ContentValues cv = new ContentValues();
        cv.put(SYMPTOM_NAME, symptom.getSymptom());
        cv.put(SYMPTOM_START_DATE, symptom.getSymptomStartDate());
        cv.put(SYMPTOM_END_DATE, symptom.getSymptomEndDate());
        Log.d(TAG, "passing " + SYMPTOM_NAME);
        Log.d(TAG, "passing " + SYMPTOM_START_DATE + " to " + SYMPTOM_END_DATE);
        Log.d(TAG, "cv is " + cv);
        db.insert(SYMPTOM_TABLE, null, cv);

        ContentValues cv2 = new ContentValues();
        cv2.put(SYMPTOM_NAME, symptom.getSymptom());
        cv2.put(SYMPTOM_RATING, symptom.getRating());
        cv2.put(SYMPTOM_TRACK_DATE, symptom.getSymptomTrackDate());
        Log.d(TAG, "passing " + SYMPTOM_RATING);
        Log.d(TAG, "passing " + SYMPTOM_TRACK_DATE);
        db.insert(SYMPTOM_TRACKING_TABLE, null, cv2);
        Log.d(TAG, "database insertion successful" );

    }
    public List<HabitModel> getAllHabits() throws ParseException {
        List<HabitModel> habitList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(HABIT_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do {
                        Log.d(TAG, "habit date display: " + dataHelper.getHabitCurrDate());
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        Date startDate = sdf.parse(cur.getString(cur.getColumnIndex(HABIT_START_DATE)));
                        Date endDate = sdf.parse(cur.getString(cur.getColumnIndex(HABIT_END_DATE)));
                        if ((endDate.after(sdf.parse(dataHelper.getHabitCurrDate())) || endDate.equals(sdf.parse(dataHelper.getHabitCurrDate())))
                                && (startDate.equals(sdf.parse(dataHelper.getHabitCurrDate())) || startDate.before(sdf.parse(dataHelper.getHabitCurrDate())))) {
                            HabitModel habit = new HabitModel();
                            habit.setId(cur.getInt(cur.getColumnIndex(HABIT_ID)));
                            habit.setHabit(cur.getString(cur.getColumnIndex(HABIT_NAME)));
                            String habitTrackQuery = HABIT_TRACK_DATE + " = \'" + dataHelper.getHabitCurrDate() + "\' and " + HABIT_NAME + " = \'" + cur.getString(cur.getColumnIndex(HABIT_NAME)) + "\'";
                            Log.d(TAG, habitTrackQuery);
                            Cursor habitTrackQueryResults = db.query(HABIT_TRACKING_TABLE, null, habitTrackQuery, null, null, null, null, null);
                            if(habitTrackQueryResults != null){
                                Log.d(TAG, "query results");
                                if(habitTrackQueryResults.moveToFirst()){
                                    Log.d(TAG, String.valueOf(habitTrackQueryResults.getInt(habitTrackQueryResults.getColumnIndex(HABIT_COMPLETE))));
                                    Log.d(TAG, habitTrackQueryResults.getString(habitTrackQueryResults.getColumnIndex(HABIT_TRACK_DATE)));
                                    habit.setStatus(habitTrackQueryResults.getInt(habitTrackQueryResults.getColumnIndex(HABIT_COMPLETE)));
                                    habit.setHabitTrackDate(habitTrackQueryResults.getString(habitTrackQueryResults.getColumnIndex(HABIT_TRACK_DATE)));
                                }
                            }
                            habit.setHabitStartDate(cur.getString(cur.getColumnIndex(HABIT_START_DATE)));
                            habit.setHabitEndDate(cur.getString(cur.getColumnIndex(HABIT_END_DATE)));
                            habitList.add(habit);
                        }
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

    public List<SymptomModel> getAllSymptoms() throws ParseException {
        List<SymptomModel> symptomList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(SYMPTOM_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        SymptomModel symptom = new SymptomModel();
                        Log.d(TAG, "symptom date display: " + dataHelper.getSymptomCurrDate());
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        Date startDate = sdf.parse(cur.getString(cur.getColumnIndex(SYMPTOM_START_DATE)));
                        Date endDate = sdf.parse(cur.getString(cur.getColumnIndex(SYMPTOM_END_DATE)));
                        if ((endDate.after(sdf.parse(dataHelper.getSymptomCurrDate())) || endDate.equals(sdf.parse(dataHelper.getSymptomCurrDate())))
                                && (startDate.equals(sdf.parse(dataHelper.getSymptomCurrDate())) || startDate.before(sdf.parse(dataHelper.getSymptomCurrDate())))) {
                            symptom.setId(cur.getInt(cur.getColumnIndex(SYMPTOM_ID)));
                            symptom.setSymptom(cur.getString(cur.getColumnIndex(SYMPTOM_NAME)));
                            String symptomTrackQuery = SYMPTOM_TRACK_DATE + " = \'" + dataHelper.getSymptomCurrDate() + "\' and " + SYMPTOM_NAME + " = \'" + cur.getString(cur.getColumnIndex(SYMPTOM_NAME)) + "\'";
                            Log.d(TAG, symptomTrackQuery);
                            Cursor symptomTrackQueryResults = db.query(SYMPTOM_TRACKING_TABLE, null, symptomTrackQuery, null, null, null, null, null);
                            if(symptomTrackQueryResults != null){
                                Log.d(TAG, "query results");
                                if(symptomTrackQueryResults.moveToFirst()){
                                    Log.d(TAG, String.valueOf(symptomTrackQueryResults.getInt(symptomTrackQueryResults.getColumnIndex(SYMPTOM_RATING))));
                                    Log.d(TAG, symptomTrackQueryResults.getString(symptomTrackQueryResults.getColumnIndex(SYMPTOM_TRACK_DATE)));
                                    symptom.setRating(symptomTrackQueryResults.getInt(symptomTrackQueryResults.getColumnIndex(SYMPTOM_RATING)));
                                    symptom.setSymptomTrackDate(symptomTrackQueryResults.getString(symptomTrackQueryResults.getColumnIndex(SYMPTOM_TRACK_DATE)));
                                }
                            }

                            symptom.setSymptomStartDate(cur.getString(cur.getColumnIndex(SYMPTOM_START_DATE)));
                            symptom.setSymptomEndDate(cur.getString(cur.getColumnIndex(SYMPTOM_END_DATE)));
                            symptomList.add(symptom);
                        }
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

    public List<HabitModel> getDailyHabits(String day){
        List<HabitModel> dailyHabitList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            String habitByDayQuery = HABIT_TRACK_DATE + " = \'" + day + "\'";
            Log.d(TAG, habitByDayQuery);
            cur = db.query(HABIT_TRACKING_TABLE, null, habitByDayQuery, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()) {
                    do{
                        HabitModel habit = new HabitModel();
                        habit.setHabit(cur.getString(cur.getColumnIndex(HABIT_NAME)));
                        habit.setStatus(cur.getInt(cur.getColumnIndex(HABIT_COMPLETE)));
                        Log.d(TAG, cur.getString(cur.getColumnIndex(HABIT_NAME)));
                        dailyHabitList.add(habit);
                    }
                    while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }

        return dailyHabitList;
    }

    public List<SymptomModel> getDailySymptoms(String day){
        List<SymptomModel> dailySymptomList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            String symptomByDayQuery = SYMPTOM_TRACK_DATE + " = \'" + day + "\'";
            cur = db.query(SYMPTOM_TRACKING_TABLE, null, symptomByDayQuery, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()) {
                    do{
                        SymptomModel symptom = new SymptomModel();
                        symptom.setSymptom(cur.getString(cur.getColumnIndex(SYMPTOM_NAME)));
                        symptom.setRating(cur.getInt(cur.getColumnIndex(SYMPTOM_RATING)));
                        Log.d(TAG, cur.getString(cur.getColumnIndex(SYMPTOM_NAME)));
                        dailySymptomList.add(symptom);
                    }
                    while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }

        return dailySymptomList;
    }

    public void updateHabitStatus(String habitName, int status){
        ContentValues cv = new ContentValues();
        cv.put(HABIT_COMPLETE, status);
        cv.put(HABIT_TRACK_DATE, dataHelper.getHabitCurrDate());
        cv.put(HABIT_NAME, habitName);
        db.replace(HABIT_TRACKING_TABLE, null, cv);
        Log.d(TAG, "cv is " + cv);
    }

    public void updateSymptomRating(String symptomName, int rating) {
        ContentValues cv = new ContentValues();
        cv.put(SYMPTOM_RATING, rating);
        cv.put(SYMPTOM_TRACK_DATE, dataHelper.getSymptomCurrDate());
        cv.put(SYMPTOM_NAME, symptomName);
        db.replace(SYMPTOM_TRACKING_TABLE, null, cv);
    }

    public void updateHabit(int id, String habit, String startDate, String endDate) {
        ContentValues cv = new ContentValues();
        cv.put(HABIT_NAME, habit);
        cv.put(HABIT_START_DATE, startDate);
        cv.put(HABIT_END_DATE, endDate);
        db.update(HABIT_TABLE, cv, HABIT_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateSymptom(int id, String symptom, String startDate, String endDate) {
        ContentValues cv = new ContentValues();
        cv.put(SYMPTOM_NAME, symptom);
        cv.put(SYMPTOM_START_DATE, startDate);
        cv.put(SYMPTOM_END_DATE, endDate);
        db.update(SYMPTOM_TABLE, cv, SYMPTOM_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteHabit(int id){
        db.delete(HABIT_TABLE, HABIT_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteSymptom(int id) {
        db.delete(SYMPTOM_TABLE, SYMPTOM_ID + "= ?", new String[] {String.valueOf(id)});
    }
}
