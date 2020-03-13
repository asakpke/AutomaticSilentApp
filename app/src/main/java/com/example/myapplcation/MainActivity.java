
package com.example.myapplcation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    WifiManager wifiManager;
    WifiReciever wifiReciever;
    ListAdapter listAdapter;
    ListView wifiList;
    List myWifiList;

//    TextView toolbarText;

    NotificationManager mnotificationManager;

    DatabaseHelper myDb;

    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb=new DatabaseHelper(this);
        wifiList =(ListView) findViewById(R.id.list);

//        toolbarText =findViewById(R.id.toolbarText);

        wifiManager =(WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiReciever();

        registerReceiver(wifiReciever,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        else
        {
            ScanWifiList();
        }

//        view();

   }

//   public void view()
//   {
//
//       Cursor res=myDb.getAllData();
//       if (res.getCount()==0){
//
//           Toast.makeText(this, "nothing found", Toast.LENGTH_SHORT).show();
//           return;
//
//       }
//
//       StringBuffer buffer = new StringBuffer();
//       while(res.moveToNext())
//       {
////           buffer.append("ID :"+res.getString(0)+"\n\n");
////           buffer.append("Name :"+res.getString(1)+"\n\n");
//
//           toolbarText.setText(res.getString(1));
//
//       }
//
//       Toast.makeText(this, "Data", Toast.LENGTH_SHORT).show();
//   }

    private void ScanWifiList() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wifiManager.startScan();
                ScanWifiList();
                myWifiList = wifiManager.getScanResults();
                setAdapter();

            }
        }, 5*1000);

        mnotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Cursor res=myDb.getAllData();
        if (res.getCount()==0){

            Toast.makeText(this, "nothing found", Toast.LENGTH_SHORT).show();
            return;

        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext())
        {
           buffer.append("ID :"+res.getString(0)+"\n\n");
           buffer.append("Name :"+res.getString(1)+"\n\n");

//            toolbarText.setText(res.getString(1));

            if (res.getString(1).contains("AndroidWifi") && String.valueOf(myWifiList).contains("AndroidWifi"))
            {
                changeInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            }
            else
            {
                changeInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }

        }
        showMessage("Data",buffer.toString());

//        if (String.valueOf(myWifiList).contains("AndroidWifi"))
//        if (res.getString(1).contains("AndroidWifi"))
//        {
//            changeInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
//        }
//        else
//        {
//            changeInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
//        }
    }

    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

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
    private void setAdapter(){
        listAdapter = new com.example.myapplcation.ListAdapter(getApplicationContext(),myWifiList);
        wifiList.setAdapter(listAdapter);
    }

    public void setting(View view) {

        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,Setting.class);
        startActivity(intent);
    }


    class WifiReciever extends BroadcastReceiver
   {

       @Override
       public void onReceive(Context context, Intent intent) {

       }
   }
}
