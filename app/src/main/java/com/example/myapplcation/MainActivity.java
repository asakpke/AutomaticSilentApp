
package com.example.myapplcation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    WifiManager wifi;
    String wifiName = "Keep Quite";
    AudioManager audioManager;
//    ListView lv;
//    TextView textStatus;
//    Button buttonScan;
//    int size = 0;
//    List<ScanResult> results;
//
//    String ITEM_KEY = "key";
//    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
//    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (wifiName == "Keep Quite"){

        }
//
//        textStatus = (TextView) findViewById(R.id.textStatus);
//        buttonScan = (Button) findViewById(R.id.buttonScan);
//        buttonScan.setOnClickListener(this);
//        lv = (ListView)findViewById(R.id.list);

//        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (wifi.isWifiEnabled() == false)
//        {
//            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
//            wifi.setWifiEnabled(true);
//        }
//        this.adapter = new SimpleAdapter(MainActivity.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
//        lv.setAdapter(this.adapter);
//
//        registerReceiver(new BroadcastReceiver()
//        {
//            @Override
//            public void onReceive(Context c, Intent intent)
//            {
//                results = wifi.getScanResults();
//                size = results.size();
//            }
//        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

//    @Override
//    public void onClick(View v) {
//        arraylist.clear();
//        wifi.startScan();
//        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
//        try
//        {
//            size = size - 1;
//            while (size >= 0)
//            {
//                HashMap<String, String> item = new HashMap<String, String>();
//                item.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);
//
//                arraylist.add(item);
//                size--;
//                adapter.notifyDataSetChanged();
//            }
//        }
//        catch (Exception e)
//        { }
//    }
}
