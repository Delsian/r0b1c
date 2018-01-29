package com.eug.r0b1c.setconnector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {
    public static final UUID serviceUuid = UUID.fromString("00001523-1212-efde-1523-785feabcd123");
    private static final String LOG_TAG = "r0b1c.MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10*1000L;

    private ImageButton connButton;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt gatt;
    private BluetoothGattService gattService;
    private BluetoothGattCharacteristic sendCharacteristic;
    private TextView connStatusTV;
    private boolean scanning = false;
    private Handler handler;
    static ConnSurface connSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connStatusTV = (TextView) findViewById(R.id.connstatus);
        connButton = (ImageButton)findViewById(R.id.ib_conn);
        handler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        connButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if( gattService != null )
                    devForward();
            }
        });
    }

    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!bluetoothAdapter.isEnabled()) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnectDevice();
        scanLeDevice(false);
    }

    private void disconnectDevice() {
        if( gatt != null ) {
            gatt.disconnect();
            gatt = null;
            gattService = null;
        }
    }

    private void devForward() {
        sendCharacteristic.setValue(5, BluetoothGattCharacteristic.FORMAT_UINT8,0);
        boolean initiated = gatt.writeCharacteristic(sendCharacteristic);
        Log.d( LOG_TAG, "Forward: "+initiated);
    }

    private void scanLeDevice(final boolean enable) {
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        ScanFilter filter = new ScanFilter.Builder().setServiceUuid(
                    new ParcelUuid(serviceUuid)).build();
        filters.add(filter);

        final BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        Log.d(LOG_TAG, "scanLeDevice: "+enable);
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;

                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(filters, settings, leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    // Device scan callback.
    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    if (callbackType != ScanSettings.CALLBACK_TYPE_ALL_MATCHES) {
                        // Should not happen.
                        Log.e(LOG_TAG, "LE Scan has already started");
                        return;
                    }
                    ScanRecord scanRecord = result.getScanRecord();
                    if (scanRecord == null) {
                        return;
                    }
                    List<ParcelUuid> scanServiceUuids = scanRecord.getServiceUuids();
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
}
