package com.example.mobileencryption;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "encryption_app.db";
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private static final String TABLE_TASK = "task";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_TASK_CONTENT = "task_content";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME  + " TEXT,"
            + COLUMN_USER_EMAIL+ " TEXT," + COLUMN_USER_PASSWORD  + " TEXT" + ")";

    private String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("+ COLUMN_TASK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TASK_NAME  + " TEXT,"
            + COLUMN_TASK_CONTENT+ " TEXT" + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_TASK_TABLE = "DROP TABLE IF EXISTS " + TABLE_TASK;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        //String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL,%s TEXT NOT NULL);",TABLE_TASK,COLUMN_TASK_NAME,COLUMN_TASK_CONTENT);
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        //String query = String.format("DELETE TABLE IF EXISTS %s",TABLE_TASK);
        db.execSQL(DROP_TASK_TABLE);
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();;
    }

    public boolean checkUser(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " =?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }
    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " =?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public void insertNewTask(String task, String task_content){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME,task);
        values.put(COLUMN_TASK_CONTENT,task_content);
        db.insertWithOnConflict(TABLE_TASK,null,values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        Log.e("Saved", "Record Saved");
    }

    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK,COLUMN_TASK_NAME + " = ?",new String[]{task});
        db.close();
    }

    public Cursor getData(String task) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM `"+TABLE_TASK+"` WHERE `"+COLUMN_TASK_NAME+"` = '"+task+"'";
        Cursor res =  db.rawQuery( selectQuery, null );
        return res;
    }

    public ArrayList<String> getTaskList(){
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASK,new String[]{COLUMN_TASK_ID,COLUMN_TASK_NAME},null,null,null,null,null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(COLUMN_TASK_NAME);
            //int id = cursor.getColumnIndex(COLUMN_TASK_ID);
            taskList.add(cursor.getString(index));
            //taskList.add(cursor.getString(id));
        }
        cursor.close();
        db.close();
        return taskList;
    }

}
