package com.krashtan.eug.r0b1c;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothHandler {
    private Context context;
    private boolean mEnabled = false;
    private BluetoothAdapter mBluetoothAdapter;
    private String mCurrentConnectedBLEAddr;

    public BluetoothHandler(Context context) {
        this.context = context;
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
}
