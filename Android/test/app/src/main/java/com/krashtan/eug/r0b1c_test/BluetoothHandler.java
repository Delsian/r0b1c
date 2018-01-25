package com.krashtan.eug.r0b1c_test;

/**
 * package com.elecfreaks.ble;
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

public class BluetoothHandler {
    // scan bluetooth device
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mEnabled = false;
    private boolean mScanning = false;
    private static final long SCAN_PERIOD = 2000;
    private BLEDeviceListAdapter mDevListAdapter;
    private String mCurrentConnectedBLEAddr;

    // connect bluetooth device
    private String mDeviceAddress = null;
    private boolean mConnected = false;
    private OnRecievedDataListener onRecListener;
    private OnConnectedListener onConnectedListener;
    private OnScanListener onScanListener;
    private Context context;

    public interface OnRecievedDataListener{
        public void onRecievedData(byte[] bytes);
    };

    public interface OnConnectedListener{
        public void onConnected(boolean isConnected);
    };

    public interface OnScanListener{
        public void onScan(BluetoothDevice device, int rssi, byte[] scanRecord);
        public void onScanFinished();
    };

    public void setOnScanListener(OnScanListener l){
        onScanListener = l;
    }

    public BluetoothHandler(Context context) {
        // TODO Auto-generated constructor stub
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

    public BLEDeviceListAdapter getDeviceListAdapter(){
        return mDevListAdapter;
    }



    public class BluetoothScanInfo{
        public BluetoothDevice device;
        public int rssi;
        public byte[] scanRecord;
    };
}

