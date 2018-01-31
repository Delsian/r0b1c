package com.krashtan.eug.r0b1c.ble;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.krashtan.eug.r0b1c.MainActivity;
import com.krashtan.eug.r0b1c.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothHandler {
    private static final String LOG_TAG = "BluetoothHandler";
    private static final long SCAN_PERIOD = 2000;
    private static final UUID targetServiceUuid =
            UUID.fromString("00001523-1212-efde-1523-785feabcd123");

    private Context context;
    private boolean mEnabled = false;
    private BluetoothAdapter mBluetoothAdapter;
    private String mCurrentConnectedBLEAddr;
    private BleService mBleService;
    private BLEDeviceListAdapter mDevListAdapter;
    private AddHandler mAddHandler;
    private Dialog mBleDialog;

    public BluetoothHandler(Context context) {
        this.context = context;
        mDevListAdapter = new BLEDeviceListAdapter(context);
        mBluetoothAdapter = null;
        mCurrentConnectedBLEAddr = null;
        mAddHandler = new AddHandler();
        mBleDialog = new Dialog(context);

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

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {

        }
    };

    public class BluetoothScanInfo{
        public BluetoothDevice device;
        public int rssi;
    }

    private class AddHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj != null){
                mDevListAdapter.addDevice((BluetoothScanInfo) msg.obj);
                mDevListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void scanLeDevice(final boolean enable) {
        final ScanCallback mLeScanCallback = new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult result) {

                super.onScanResult(callbackType, result);

                final BluetoothDevice bluetoothDevice = result.getDevice();
                final int rssi = result.getRssi();

                Log.i(LOG_TAG, "result "+bluetoothDevice.getName());
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        BluetoothScanInfo info = new BluetoothScanInfo();
                        info.device = bluetoothDevice;
                        info.rssi = rssi;
                        msg.obj = info;
                        mAddHandler.sendMessage(msg);
                    }
                });
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };

        final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        Log.d(LOG_TAG, "scanLeDevice: "+enable);
        if (enable) {
            mDevListAdapter.clearDevice();
            mDevListAdapter.notifyDataSetChanged();
            // Stops scanning after a pre-defined scan period.
            mAddHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothLeScanner.stopScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            SelectBleDevice();
            bluetoothLeScanner.startScan(mLeScanCallback);
        } else {
            bluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }

    private void SelectBleDevice() {
        mBleDialog.setContentView(R.layout.ble_dialog);
        mBleDialog.setTitle("BLE devices");
        ListView bleDeviceListView = mBleDialog.findViewById(R.id.bleDeviceListView);
        bleDeviceListView.setAdapter(getDeviceListAdapter());
        bleDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BluetoothDevice device = getDeviceListAdapter().getItem(position).device;
                // connect
                connect(device.getAddress());
                mBleDialog.dismiss();
            }
        });
        mBleDialog.show();
    }

    public void connect(String deviceAddress){
        mCurrentConnectedBLEAddr = deviceAddress;
        Intent gattServiceIntent = new Intent(context, BleService.class);

        if(!context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)){
            System.out.println("bindService failed!");
        }

        //context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBleService != null) {
            final boolean result = mBleService.connect(mCurrentConnectedBLEAddr);
            Log.i(LOG_TAG, "Connected to "+mCurrentConnectedBLEAddr+" "+result);
            if (!result) {
                mCurrentConnectedBLEAddr = null;
            }
        }else{
            System.out.println("mBleService = null");
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBleService = ((BleService.LocalBinder) service).getService();
            if (!mBleService.initialize()) {
                Log.e("onServiceConnected", "Unable to initialize Bluetooth");
                ((MainActivity) context).finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBleService.connect(mCurrentConnectedBLEAddr);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBleService = null;
        }
    };

}
