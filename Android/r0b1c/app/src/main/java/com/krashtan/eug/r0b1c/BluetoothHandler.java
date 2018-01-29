package com.krashtan.eug.r0b1c;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BluetoothHandler {
    private Context context;
    private boolean mEnabled = false;
    private BluetoothAdapter mBluetoothAdapter;
    private String mCurrentConnectedBLEAddr;
    private BLEDeviceListAdapter mDevListAdapter;

    public BluetoothHandler(Context context) {
        this.context = context;
        mDevListAdapter = new BLEDeviceListAdapter(context);
        mBluetoothAdapter = null;
        mCurrentConnectedBLEAddr = null;

        if(!isSupportBle()){
            Toast.makeText(context, "your device not support BLE!", Toast.LENGTH_SHORT).show();
            ((MainActivity)context).finish();
            return ;
        }
        // open bluetooth
        if (!getBluetoothAdapter().isEnabled()) {
            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((MainActivity)context).startActivityForResult(mIntent, 1);
        }else{
            setEnabled(true);
        }
    }


    public boolean isSupportBle(){
        // is support 4.0 ?
        final BluetoothManager bluetoothManager = (BluetoothManager)
                context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null)
            return false;
        else
            return true;
    }

    public BluetoothAdapter getBluetoothAdapter(){
        return mBluetoothAdapter;
    }

    public void setEnabled(boolean enabled){
        mEnabled = enabled;
    }

    public boolean isEnabled(){
        return mEnabled;
    }

    public BLEDeviceListAdapter getDeviceListAdapter(){
        return mDevListAdapter;
    }

    // scan device
    public void scanLeDevice(boolean enable) {
        if (enable) {

        } else {

        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {

        }
    };

    public class BluetoothScanInfo{
        public BluetoothDevice device;
        public int rssi;
    };

    public class BLEDeviceListAdapter extends BaseAdapter {
        private Context context;
        private TextView devNameTextView, devRssiTextView;
        private ArrayList<BluetoothScanInfo> bleArrayList;

        public BLEDeviceListAdapter(Context context) {
            this.context = context;
            bleArrayList = new ArrayList<BluetoothScanInfo>();
        }

        @Override
        public int getCount() {
            return bleArrayList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public BluetoothScanInfo getItem(int position) {
            return bleArrayList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout = null;

            if (convertView == null) {
                layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_list, null);
                devNameTextView = (TextView) layout.findViewById(R.id.textViewDevName);
                devRssiTextView = (TextView) layout.findViewById(R.id.textViewRssi);
                convertView = layout;
            }

            BluetoothDevice device = bleArrayList.get(position).device;
            String devName = device.getName();
            int rssi = bleArrayList.get(position).rssi;

            if (devName != null && devName.length() > 0) {
                devNameTextView.setText(devName);
            } else {
                devNameTextView.setText("unknow-device");
            }
            devRssiTextView.setText(String.valueOf(rssi));
            return convertView;
        }
    }
}
