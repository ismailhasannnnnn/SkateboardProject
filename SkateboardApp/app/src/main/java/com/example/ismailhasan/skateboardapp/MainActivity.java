package com.example.ismailhasan.skateboardapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    ArrayList list = new ArrayList();
    boolean btPossible = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connectToBoard = (Button)findViewById(R.id.connectToBoard);
        Button getPairedDevices = (Button)findViewById(R.id.getPairedDevices);


        if(btAdapter == null){
            easyToast("This device does not support Bluetooth features");
            btPossible = false;
        }else if(!btAdapter.isEnabled()){
            btPossible = true;
            Intent enableBT = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 1);
        }

        if(!isBluetoothConnected()){
            connectToBoard.setText("Connect To Board");
        }else{
            connectToBoard.setText("Control Board");
        }

        connectToBoard.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings",
                        "com.android.settings.bluetooth.BluetoothSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(!btPossible){
                    easyToast("This device does not support Bluetooth features");
                }else if(!isBluetoothConnected()){
                    startActivity(intent);
                }else{
                    easyToast("Connected");
                    startActivity(new Intent(MainActivity.this, ControlActivity.class));
                }
            }
        });

        getPairedDevices.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                pairedDevices(v);
            }

        });

    }

    public boolean isBluetoothConnected() {
        return btAdapter != null && btAdapter.isEnabled()
                && btAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
    }

    public void easyToast(String s){
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
    }

    public AdapterView.OnItemClickListener deviceListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            Intent i = new Intent(MainActivity.this, ControlActivity.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };

    public void pairedDevices(View v) {
        ListView deviceList = (ListView)findViewById(R.id.deviceList);
        pairedDevices =  btAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice bt : pairedDevices){
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
            final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
            deviceList.setAdapter(adapter);
            deviceList.setOnItemClickListener(deviceListClickListener);
        }
    }

}
