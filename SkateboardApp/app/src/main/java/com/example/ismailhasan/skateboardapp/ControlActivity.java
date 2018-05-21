package com.example.ismailhasan.skateboardapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.UUID;

public class ControlActivity extends AppCompatActivity {

    Button btnForward, btnBackward;
    String address = null;
    ProgressDialog progressDialog;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Intent newInt = getIntent();
        address = newInt.getStringExtra(MainActivity.EXTRA_ADDRESS);

    }
}
