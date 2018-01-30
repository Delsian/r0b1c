package com.krashtan.eug.r0b1c.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krashtan.eug.r0b1c.R;

import java.util.ArrayList;

public class BLEDeviceListAdapter extends BaseAdapter {
    private static final String LOG_TAG = "DeviceListAdapter";

    private Context context;
    private TextView devNameTextView, devRssiTextView;
    private ArrayList<BluetoothHandler.BluetoothScanInfo> bleArrayList;
    private BluetoothHandler.BluetoothScanInfo findResult;

    public BLEDeviceListAdapter(Context context) {
        this.context = context;
        bleArrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return bleArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addDevice(BluetoothHandler.BluetoothScanInfo device) {
        if (!isContains(device)) {
            bleArrayList.add(device);
        }else{
            if(findResult != null){
                int index = bleArrayList.indexOf(findResult);
                bleArrayList.set(index, device);
            }
        }
    }

    public void clearDevice(){
        bleArrayList.clear();
    }

    public boolean isContains(BluetoothHandler.BluetoothScanInfo dstDevice){
        boolean val = false;
        findResult = null;
        for(BluetoothHandler.BluetoothScanInfo d:bleArrayList){
            if(d.device.getAddress().equals(dstDevice.device.getAddress())){
                val = true;
                findResult = d;
                break;
            }
        }
        return val;
    }

    @Override
    public BluetoothHandler.BluetoothScanInfo getItem(int position) {
        return bleArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;

        if (convertView == null) {
            layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_list, null);
            devNameTextView = layout.findViewById(R.id.textViewDevName);
            devRssiTextView = layout.findViewById(R.id.textViewRssi);
            convertView = layout;
        }

        BluetoothDevice device = bleArrayList.get(position).device;
        String devName = device.getName();
        int rssi = bleArrayList.get(position).rssi;

        if (devName != null && devName.length() > 0) {
            devNameTextView.setText(devName);
        } else {
            devNameTextView.setText("unknown-device");
        }
        devRssiTextView.setText(String.valueOf(rssi));
        return convertView;
    }
}
