package com.example.myapplcation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.myapplcation.DatabaseHelper.COL_2;

public class Setting extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    EditText wifiName;
    TextView nameTV;
    ImageButton backButton;
    EditText idEdittext;

//    public static final String mypreference = "mypref";
//    public static final String Wifi = "wifiNameKey";

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb=new DatabaseHelper(this);

        idEdittext = findViewById(R.id.idEditText);
        wifiName = (EditText) findViewById(R.id.etWifi);
        nameTV = (TextView) findViewById(R.id.nameText);
        backButton =findViewById(R.id.backBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Setting.this,MainActivity.class);
                startActivity(intent);
            }
        });

//        sharedpreferences = getSharedPreferences(mypreference,
//                Context.MODE_PRIVATE);


//        if (sharedpreferences.contains(Wifi)) {
//            wifiName.setText(sharedpreferences.getString(Wifi, ""));
//        }
    }

    public void Save(View view) {

        String w = wifiName.getText().toString();
        boolean isInserted=myDb.insertData(w);

        if (isInserted == true){
            Toast toast = Toast.makeText(Setting.this,"Your Wifi Name is Set", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else
        {
            Toast.makeText(this, "Data is not inserted", Toast.LENGTH_SHORT).show();
        }
//        wifiName.setText("");
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void Get(View view) {

        Cursor res=myDb.getAllData();
        if (res.getCount()==0){

//            Toast.makeText(this, "nothing found", Toast.LENGTH_SHORT).show();
            showMessage("Error","Nothing Found");
            return;

        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
            buffer.append("ID :"+res.getString(0)+"\n\n");
            buffer.append("Name :"+res.getString(1)+"\n\n");

//            toolbarText.setText(res.getString(1));

        }
        showMessage("Data",buffer.toString());


    }

    public void showMessage(String title,String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }


    public void clear(View view) {

//        Integer deletedRows=myDb.deletedata(idEdittext.getText().toString());
        
//        if (deletedRows > 0)
//        {
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//        }
       wifiName.setText("");

    }

    public void back(View view) {
    }
}
