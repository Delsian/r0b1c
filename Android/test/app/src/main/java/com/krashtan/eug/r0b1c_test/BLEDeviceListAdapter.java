package com.krashtan.eug.r0b1c_test;


import java.util.ArrayList;
import android.widget.BaseAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * package com.elecfreaks.ble;
 */

public class BLEDeviceListAdapter extends BaseAdapter {

    private ArrayList<BluetoothHandler.BluetoothScanInfo> bleArrayList;
    private Context context;

    public BLEDeviceListAdapter(Context context) {
        this.context = context;
        bleArrayList = new ArrayList<BluetoothHandler.BluetoothScanInfo>();
    }
}
