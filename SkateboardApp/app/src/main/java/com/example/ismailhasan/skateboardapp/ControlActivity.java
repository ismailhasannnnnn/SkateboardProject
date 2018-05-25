package com.example.ismailhasan.skateboardapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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
        btnForward = (Button)findViewById(R.id.forwardButton);
        btnBackward = (Button)findViewById(R.id.backwardButton);


        btnForward.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                forward();
            }

        });

        btnBackward.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                backward();
            }

        });

        new ConnectBT().execute();

    }

    public void forward(){
        if (btSocket!=null)
        {
            try
            {
//                btSocket.getOutputStream().write("TO".getBytes());
                btSocket.getOutputStream().write("TO".getBytes());
                TextView dataLabel = (TextView)findViewById(R.id.dataLabel);
//                dataLabel.setText(btSocket.getInputStream().read());
            }
            catch (IOException e)
            {
                easyToast("Error");
            }
        }
    }

    public void backward(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TF".getBytes());
                TextView dataLabel = (TextView)findViewById(R.id.dataLabel);
//                dataLabel.setText(btSocket.getInputStream().read());
            }
            catch (IOException e)
            {
                easyToast("Error");
            }
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>{

        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(ControlActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                easyToast("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                easyToast("Connected.");
                isBtConnected = true;
            }
            progressDialog.dismiss();
        }

    }

    public void easyToast(String s){
        Toast.makeText(ControlActivity.this, s, Toast.LENGTH_LONG).show();
    }


}
