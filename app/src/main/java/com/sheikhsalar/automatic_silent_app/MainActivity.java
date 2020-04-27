
package com.sheikhsalar.automatic_silent_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    WifiManager wifiManager;
    WifiReciever wifiReciever;
    ListAdapter listAdapter;
    ListView wifiList;
    List myWifiList;
    NotificationManager mnotificationManager;

    EditText wifiName;
    EditText delayEditText;
    ImageButton backButton;
    EditText idEdittext;
    Button timeSet;
    DatabaseHelper myDb;
//    IntervalDatabaseHelper intervaldb;
//    ImageView settingBtn;
    ImageView aboutBtn;

    int intervalTime;

    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MyService.class));
        setContentView(R.layout.activity_main);

//        wifiList =(ListView) findViewById(R.id.list);
//        settingBtn =findViewById(R.id.settings);


        idEdittext = findViewById(R.id.idEditText);
        wifiName = (EditText) findViewById(R.id.etWifi);
        delayEditText = (EditText) findViewById(R.id.delayEdittext);
        timeSet = findViewById(R.id.delayBtn);
        timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = delayEditText.getText().toString();
                boolean isInserted=myDb.intervalinsertData(t);

                if(t.equals("")){
                    Toast.makeText(MainActivity.this, "Please enter values", Toast.LENGTH_SHORT).show();
                }else  if (isInserted){
                    Toast toast = Toast.makeText(MainActivity.this,"Your Time Interval is Set", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }

            }
        });

        aboutBtn =findViewById(R.id.aboutMenu);

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

//        settingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast toast = Toast.makeText(MainActivity.this, "hello bhai chal ja", Toast.LENGTH_SHORT);
////                toast.setGravity(Gravity.CENTER, 0, 0);
////                toast.show();
//                Intent i=new Intent(MainActivity.this,Setting.class);
//                startActivity(i);
//            }
//        });

        myDb=new DatabaseHelper(this);
//        intervaldb =new IntervalDatabaseHelper(this);

        if (myDb.getAllData().getCount() == 0 && myDb.intervalData().getCount() == 0)
        {
            myDb.insertData("keepquiet");
            myDb.intervalinsertData("10");
//            Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();

        }
        else
        {
//            Toast.makeText(this, "No Values Inserted", Toast.LENGTH_SHORT).show();
        }

        wifiManager =(WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiReciever();

        registerReceiver(wifiReciever,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        else {
            ScanWifiList();
//            Toast.makeText(this, "wifilist", Toast.LENGTH_SHORT).show();
        }
   }


    private void ScanWifiList() {

        Cursor result =myDb.intervalData();
        result.moveToNext();
        intervalTime = Integer.parseInt(result.getString(1));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wifiManager.startScan();
                ScanWifiList();
                myWifiList = wifiManager.getScanResults();
                //setAdapter();

            }
        }, intervalTime*1000);

        mnotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Cursor res=myDb.getAllData();
        if (res.getCount()==0){

//            Toast.makeText(this, "nothing found", Toast.LENGTH_SHORT).show();
            return;
        }
        while(res.moveToNext())
        {
            if (String.valueOf(myWifiList).contains(res.getString(1).toString()))
            {
                changeInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            }
            else
            {
                changeInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }

        }
    }


    protected void changeInterruptionFilter(int interruptionFilter){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ // If api level minimum 23

            // If notification policy access granted for this package
            if(mnotificationManager.isNotificationPolicyAccessGranted()){

                // Set the interruption filter
                mnotificationManager.setInterruptionFilter(interruptionFilter);
            }else {

                // If notification policy access not granted for this package
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }
//setting activity data ------------------------------------------------------------------------------------------------
    public void Save(View view) {

        String w = wifiName.getText().toString();
        boolean isInserted = myDb.insertData(w);

        if (w.equals("")) {
            Toast.makeText(this, "Please enter values", Toast.LENGTH_SHORT).show();
        } else if (isInserted) {
            Toast toast = Toast.makeText(MainActivity.this, "Your Wifi Name is Set", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
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
    private void setAdapter(){
        listAdapter = new com.sheikhsalar.automatic_silent_app.ListAdapter(getApplicationContext(),myWifiList);
        wifiList.setAdapter(listAdapter);
    }
//
//    public void setting(View view) {
//
////        Intent intent = new Intent(this,Setting.class);
////        startActivity(intent);
//        Toast.makeText(this, "setting ok", Toast.LENGTH_SHORT).show();
//    }

    class WifiReciever extends BroadcastReceiver
   {

       @Override
       public void onReceive(Context context, Intent intent) {

       }
   }
}
