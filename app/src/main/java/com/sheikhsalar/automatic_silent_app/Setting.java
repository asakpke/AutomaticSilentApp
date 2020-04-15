package com.sheikhsalar.automatic_silent_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    EditText wifiName;
    EditText delayEditText;
    ImageButton backButton;
    EditText idEdittext;
    DatabaseHelper myDb;
//    IntervalDatabaseHelper intervaldb;
    Button timeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        timeSet = findViewById(R.id.delayBtn);
        timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (delayEditText.getText().toString().length() > 0){

                    int value =Integer.parseInt(delayEditText.getText().toString());
                    if(value > 2) {
                        Toast.makeText(getApplicationContext(), "Enter a value between 0 and 2!", Toast.LENGTH_SHORT).show();
                    }
                    else if(value < 0) {
                        Toast.makeText(getApplicationContext(), "Enter a value between 0 and 10!", Toast.LENGTH_SHORT).show();
                    }
                    else if(value >= 0 && value <= 2)
                    {
                        myDb.updateIntervalData(value);
                    }

                }
                else
                {
                    Toast.makeText(Setting.this, "Enter values", Toast.LENGTH_SHORT).show();
                }
                boolean isUpdate = myDb.updateIntervalData(Integer.parseInt(delayEditText.getText().toString()));

                if (isUpdate)
                {
                    Toast.makeText(Setting.this, "Time interval Updated", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Setting.this, "time inetrval not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myDb=new DatabaseHelper(this);
//        intervaldb =new IntervalDatabaseHelper(this);

//        Cursor data=myDb.getAllData();
//        if (data.getCount() == 0)
//        {
//            myDb.insertData("AndroidWifi");
//            Toast.makeText(this, "inserted data", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(this, "not inserted", Toast.LENGTH_SHORT).show();
//        }

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

//        Cursor res=myDb.intervalData();
//        if (res.getCount()==0){
//            showMessage("Error","Nothing Found");
//            return;
//
//        }
//
//        StringBuffer buffer = new StringBuffer();
//        while(res.moveToNext())
//        {
//            buffer.append("Interval :"+res.getString(1)+"\n\n");
//        }
//        showMessage("Data",buffer.toString());
    }

    public void Save(View view) {

        String w = wifiName.getText().toString();
        boolean isInserted=myDb.insertData(w);

        if (isInserted){
            Toast toast = Toast.makeText(Setting.this,"Your Wifi Name is Set", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
        else if(w.equals("")){
            Toast.makeText(this, "Please enter values", Toast.LENGTH_SHORT).show();
        }
        else
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

}
