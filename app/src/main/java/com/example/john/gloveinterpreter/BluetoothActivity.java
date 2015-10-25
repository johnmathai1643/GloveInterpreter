package com.example.john.gloveinterpreter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class BluetoothActivity extends Activity{

    private Set<BluetoothDevice> pairedDevices;
    private BluetoothAdapter myBluetoothAdapter;
    private ArrayAdapter<String> btArrayAdapter;
    private ListView listDevicesFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(BluetoothActivity.this, android.R.layout.simple_list_item_1);
        registerReceiver(myBluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        btArrayAdapter.clear();
        myBluetoothAdapter.startDiscovery();

        pairedDevices = myBluetoothAdapter.getBondedDevices();
        @SuppressWarnings("rawtypes")
        ArrayList list = new ArrayList();
        for(BluetoothDevice bt : pairedDevices)
            list.add(bt.getName());

        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
        @SuppressWarnings("rawtypes")
        final ArrayAdapter adapter = new ArrayAdapter(BluetoothActivity.this,android.R.layout.simple_list_item_1, list);
        listDevicesFound.setAdapter(adapter);

        setContentView(R.layout.activity_bluetooth);
        super.onCreate(savedInstanceState);
    }

    private final BroadcastReceiver myBluetoothReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                btArrayAdapter.notifyDataSetChanged();
            }
        }};

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(myBluetoothReceiver);
    }
}
