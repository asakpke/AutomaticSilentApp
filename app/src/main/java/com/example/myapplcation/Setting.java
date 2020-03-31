package com.example.myapplcation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.myapplcation.DatabaseHelper.COL_2;

public class Setting extends AppCompatActivity {

    EditText wifiName;
    EditText delayEditText;
    ImageButton backButton;
    EditText idEdittext;
    DatabaseHelper myDb;
    IntervalDatabaseHelper intervaldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        myDb=new DatabaseHelper(this);
        intervaldb =new IntervalDatabaseHelper(this);

        Cursor data=myDb.getAllData();
        if (data.getCount() == 0)
        {
            myDb.insertData("AndroidWifi");
            Toast.makeText(this, "inserted data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "not inserted", Toast.LENGTH_SHORT).show();
        }

        idEdittext = findViewById(R.id.idEditText);
        wifiName = (EditText) findViewById(R.id.etWifi);
        delayEditText = (EditText) findViewById(R.id.delayEdittext);
        backButton =findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Cursor res=intervaldb.intervalData();
        if (res.getCount()==0){
            showMessage("Error","Nothing Found");
            return;

        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            buffer.append("Interval :"+res.getString(0)+"\n\n");
        }
        showMessage("Data",buffer.toString());
    }

    public void Save(View view) {

        String w = wifiName.getText().toString();
        boolean isInserted=myDb.insertData(w);

        if (isInserted){
            Toast toast = Toast.makeText(Setting.this,"Your Wifi Name is Set", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else
        {
            Toast.makeText(this, "Data is not inserted", Toast.LENGTH_SHORT).show();
        }
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }


    public void Get(View view) {

        Cursor res=myDb.getAllData();
        if (res.getCount()==0)
        {
            showMessage("Error","Nothing Found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            buffer.append("ID :"+res.getString(0)+"\n\n");
            buffer.append("Name :"+res.getString(1)+"\n\n");
        }
        showMessage("Data",buffer.toString());
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clear(View view) {

          Integer deletedRows=  myDb.deleteData(idEdittext.getText().toString());
          if (deletedRows > 0)
          {
              Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show();
          }
          else
          {
              Toast.makeText(this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
              idEdittext.setError("Please Enter Id to Delete Data");
              idEdittext.findFocus();
          }
    }

    public void setTimeInterval(View view) {

//        int res = Integer.parseInt(delayEditText.getText().toString());
//        boolean isInserted=intervaldb.intervalinsertData(res);
//
//        if (isInserted)
//        {
//            Toast toast = Toast.makeText(Setting.this,"Your Interval Time is Set", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }else if (res == 0)
//        {
//            Toast.makeText(this, "Not set! Enter the time interval", Toast.LENGTH_SHORT).show();
//            delayEditText.setError("This field is required");
//        }

        if (delayEditText.toString().isEmpty())
        {
            Toast.makeText(this, "Please enter Interval Time", Toast.LENGTH_SHORT).show();
        }

        else if (intervaldb.intervalData().getCount() == 0){
            intervaldb.intervalinsertData(10);
            Toast.makeText(this, "Time Interval Set", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int delayTime =Integer.parseInt(delayEditText.toString());
            intervaldb.updateIntervalData(delayTime);
        }


    }
}
