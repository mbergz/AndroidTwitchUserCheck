package com.example.martin.twitchcheckonlineapitest1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class MyDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "usernames.db";
    private static final String TABLE_NAMES = "streamers";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAMES +" (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT " +
                ");";
        db.execSQL(query);
    }

    public void addUser(String username){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, username);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAMES, null, values);
        db.close();
    }

    public void removeUser(String username){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAMES + " WHERE " + COLUMN_NAME + "=\"" + username + "\";";
        db.execSQL(query);
        db.close();
    }

    /*
     *Checks if user is already in database
     *
     * Returns true if user is in database, false if not
     */
    public boolean checkUser(String username){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " +COLUMN_NAME + " FROM " + TABLE_NAMES +
                " WHERE " + COLUMN_NAME + "=\"" + username + "\";";

        Cursor c = db.rawQuery(query,null);
        if(c.getCount() <= 0){
            c.close();
            db.close();
            return false;
        }
        c.close();
        db.close();
        return true;
    }

    public ArrayList<String> getUsers(){
        ArrayList<String> list = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_NAME + " FROM " + TABLE_NAMES + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            list.add(c.getString(c.getColumnIndex(COLUMN_NAME)).trim());
            c.moveToNext();
        }
        db.close();
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES);
        onCreate(db);
    }
}
