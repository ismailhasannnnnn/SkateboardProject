package com.example.ismailhasan.skateboardapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connectToBoard = (Button)findViewById(R.id.connectToBoard);
        connectToBoard.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings",
                        "com.android.settings.bluetooth.BluetoothSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(isBluetoothConnected()){
                    startActivity(intent);
                }else{
                    easyToast("You're already connected to a device");
                }
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
}
