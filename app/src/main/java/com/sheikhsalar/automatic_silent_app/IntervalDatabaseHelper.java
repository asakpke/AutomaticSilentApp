package com.sheikhsalar.automatic_silent_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IntervalDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="Interval.db";
    public static final String TABLE_NAME ="interval_table";
    public static final String COL_1 ="ID";
    public static final String COL_2 ="INTERVAL";


    public IntervalDatabaseHelper( Context context) {

        super(context, DATABASE_NAME, null,1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, INTERVAL INT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean intervalinsertData(int interval){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,interval);
        long result=db.insert(TABLE_NAME,null,contentValues);

        if (result == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }

    public Cursor intervalData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor intervalResult=db.rawQuery("select * from "+TABLE_NAME,null);
        return intervalResult;
    }

    public boolean updateIntervalData(int i){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,i);
        db.execSQL("UPDATE interval_table set col = " + COL_2 + " WHERE id = (SELECT MIN(id) FROM interval_table)");
        return true;
    }

}
