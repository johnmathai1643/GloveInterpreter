package com.example.john.gloveinterpreter;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    private Switch BluetoothSwitch;
    private BluetoothAdapter myBluetoothAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Settings");
        setContentView(R.layout.activity_settings);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSwitch = (Switch) findViewById(R.id.bluetooth_switch);
        BluetoothSwitch.setChecked(false);
        BluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    switch_on_bluetooth();
                else
                    switch_off_bluetooth();
            }
        });

        //check the current state before we display the screen
        if(BluetoothSwitch.isChecked()){
            switch_on_bluetooth();
        }
        else {
           switch_off_bluetooth();
        }

    }

    private void switch_on_bluetooth(){
        if (!myBluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Bluetooth on", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Bluetooth on", Toast.LENGTH_LONG).show();
        }
    }

    private void switch_off_bluetooth(){
        myBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }
}
