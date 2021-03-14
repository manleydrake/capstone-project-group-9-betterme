package com.example.betterme.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.betterme.Model.HabitModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String TAG = "Database";

    private static final int VERSION = 5;
    private static final String NAME = "habitDB";  //Database name

    //User Table variables
    private static final String USER_TABLE = "user";
    private static final String userID = "userID";
    private static final String userName = "username";
    private static final String userPassword = "password";
    //Creates table
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" + userID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + userName + " TEXT, " + userPassword + " TEXT)";

    //Habit table variables
    private static final String HABIT_TABLE = "habitTable";
    private static final String HABITID = "habitID";
    private static final String HABITNAME = "habitName";
    private static final String habitStartDate = "habitStart";
    private static final String habitEndDate = "habitEnd";

    //Create habit table
    private static final String CREATE_HABIT_TABLE = "CREATE TABLE " + HABIT_TABLE + "(" + HABITID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + userID + " INTEGER, " + HABITNAME + " TEXT, " + habitStartDate + " TEXT, " + habitEndDate + " TEXT)";

    //Habit Tracking Table variables
    private static final String HABIT_TRACKING_TABLE = "habit_tracking";
    private static final String HABITTRACKDATE = "habitTrackDate";
    private static final String ISCOMPLETE = "habitStatus";

    //Create habit tracking table
    private static final String CREATE_HABIT_TRACKING_TABLE = "CREATE TABLE " + HABIT_TRACKING_TABLE + "(" + HABITID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HABITNAME + " TEXT, " + HABITTRACKDATE + " TEXT, " + ISCOMPLETE + " INTEGER)";

    //Symptom Table variables
    private static final String SYMPTOM_TABLE = "symptomTable";
    private static final String symptomID = "symptomID";
    private static final String symptomName = "symptomName";
    private static final String symptomStartDate = "symptomStart";
    private static final String symptomEndDate = "symptomEnd";

    // Create symptom table
    private static final String CREATE_SYMPTOM_TABLE = "CREATE TABLE " + SYMPTOM_TABLE + "(" + symptomID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + userID + " INTEGER, " + symptomName + " TEXT, " + symptomStartDate + " TEXT, " + symptomEndDate + " TEXT)";

    //Symptom tracking table variables
    private static final String SYMPTOM_TRACKING_TABLE = "symptomTracking";
    private static final String symptomTrackDate = "symptomTrackDate";
    private static final String symptomRating = "symptomRating";

    //Create symptom tracking table variables
    private static final String CREATE_SYMPTOM_TRACKING_TABLE = "CREATE TABLE " + SYMPTOM_TRACKING_TABLE + "(" + HABITID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + symptomTrackDate + " TEXT, " + symptomRating + " INTEGER)";


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
        cv.put(HABITNAME, habit.getHabit());
        cv.put(ISCOMPLETE, 0);
        Log.d(TAG, "passing " + HABITNAME);
        Log.d(TAG, "passing " + ISCOMPLETE);
        Log.d(TAG, "cv is " + cv);
        db.insert(HABIT_TRACKING_TABLE, null, cv);
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
                        habit.setId(cur.getInt(cur.getColumnIndex(HABITID)));
                        habit.setHabit(cur.getString(cur.getColumnIndex(HABITNAME)));
                        habit.setStatus(cur.getInt(cur.getColumnIndex(ISCOMPLETE)));
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

    public void updateHabitStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(ISCOMPLETE, status);
        db.update(HABIT_TRACKING_TABLE, cv, HABITID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateHabit(int id, String habit) {
        ContentValues cv = new ContentValues();
        cv.put(HABITNAME, habit);
        db.update(HABIT_TRACKING_TABLE, cv, HABITID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteHabit(int id){
        db.delete(HABIT_TABLE, HABITID + "= ?", new String[] {String.valueOf(id)});
    }
}
