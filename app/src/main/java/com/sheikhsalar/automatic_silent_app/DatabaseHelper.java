package com.sheikhsalar.automatic_silent_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="Wifi.db";
    public static final String TABLE_NAME ="wifi_table";
    public static final String TABLE_NAME2 ="interval_table";
    public static final String COL_12 ="ID";
    public static final String COL_22 ="INTERVAL";

    public static final String COL_1 ="ID";
    public static final String COL_2 ="NAME";



    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT)");
        db.execSQL("create table " + TABLE_NAME2 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,INTERVAL INT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);

        onCreate(db);
    }
    public boolean insertData(String name){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(COL_2,name);
        long result=db.insert(TABLE_NAME,null,contentValues);

        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    public boolean intervalinsertData(int interval){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues2=new ContentValues();
        contentValues2.put(COL_22,interval);
        long result2=db.insert(TABLE_NAME2,null,contentValues2);

        if (result2 == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }

    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;

    }

    public Cursor intervalData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor intervalResult=db.rawQuery("select * from "+TABLE_NAME2,null);
        return intervalResult;
    }

    public boolean updateIntervalData(int interval){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues2=new ContentValues();
        contentValues2.put(COL_22,interval);
//        db.update(TABLE_NAME2, contentValues2, "ID = (SELECT MIN(ID)",new String[] { });
//        return true;
        db.execSQL("UPDATE " +TABLE_NAME2+" set col = " + COL_22 + " WHERE id = (SELECT MIN(id) FROM "+TABLE_NAME2+")");
        return true;
    }


    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{id});
    }


}
