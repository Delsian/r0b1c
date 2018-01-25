package com.krashtan.eug.r0b1c_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView bleDeviceListView;
    private BLEDeviceListAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothHandler = new BluetoothHandler(this);
        bluetoothHandler.setOnConnectedListener(new OnConnectedListener() {

            @Override
            public void onConnected(boolean isConnected) {
                // TODO Auto-generated method stub
                setConnectStatus(isConnected);
            }
        });
    }
}
