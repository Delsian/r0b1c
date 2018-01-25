package com.elecfreaks.ble;

import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.elecfreaks.bleexample.MainActivity;

public class BluetoothHandler {
	// scan bluetooth device
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mEnabled = false;
	private boolean mScanning = false;
	private static final long SCAN_PERIOD = 2000;
	private BLEDeviceListAdapter mDevListAdapter;
	private String mCurrentConnectedBLEAddr;
	
	// connect bluetooth device
	private BluetoothLeService mBluetoothLeService;
	private String mDeviceAddress = null;
	private boolean mConnected = false;
	private OnRecievedDataListener onRecListener;
	private OnConnectedListener onConnectedListener;
	private OnScanListener onScanListener;
	
	private List<BluetoothGattService> gattServices = null; 
	private UUID targetServiceUuid =
            UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    private UUID targetCharacterUuid =
            UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private UUID readUUID =
            UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");
    private BluetoothGattCharacteristic targetGattCharacteristic = null;
    
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
	
	public void connect(String deviceAddress){
		mDeviceAddress = deviceAddress;	
		Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
		
		if(!((MainActivity)context).bindService(gattServiceIntent, mServiceConnection, ((MainActivity)context).BIND_AUTO_CREATE)){
			System.out.println("bindService failed!");
		} 
		
		((MainActivity)context).registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
        }else{
        	System.out.println("mBluetoothLeService = null");
        }
	}
	
	private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
	
	// Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
        	mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("onServiceConnected", "Unable to initialize Bluetooth");
                ((MainActivity) context).finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        	mBluetoothLeService = null;
        }
    };
    
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mCurrentConnectedBLEAddr = mDeviceAddress;
                if(onConnectedListener != null){
                	onConnectedListener.onConnected(true);
                }
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                mCurrentConnectedBLEAddr = null;
                if(onConnectedListener != null){
                	onConnectedListener.onConnected(false);
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLEService.getSupportedGattServices());
            	if(mBluetoothLeService != null)
            		getCharacteristic(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                // 接收到数据了
            	byte[] bytes = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
            	//System.out.println("len:"+dataString.length()+"data:"+dataString);
            	if(onRecListener != null)
            		onRecListener.onRecievedData(bytes);
            }
        }
    };
    
    public void setOnRecievedDataListener(OnRecievedDataListener l){
    	onRecListener = l;
    }
    
    public void setOnConnectedListener(OnConnectedListener l){
    	onConnectedListener = l;
    }
    
	public void getCharacteristic(List<BluetoothGattService> gattServices){
    	this.gattServices = gattServices;
        String uuid = null;
        BluetoothGattCharacteristic characteristic = null;
        BluetoothGattService targetGattService = null;
        // get target gattservice
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            if(uuid.equals(targetServiceUuid.toString())){
                targetGattService = gattService;
                break;
            }
        }
        if(targetGattService != null){
            //Toast.makeText(DeviceControlActivity.this, "get service ok", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "not support this BLE module", Toast.LENGTH_SHORT).show();
            return ;
        }
        List<BluetoothGattCharacteristic> gattCharacteristics =
            targetGattService.getCharacteristics();
        // get targetGattCharacteristic
        /*for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            uuid = gattCharacteristic.getUuid().toString();
            if(uuid.equals(targetCharacterUuid.toString())){
                targetGattCharacteristic = gattCharacteristic;
                break;
            }
        }*/
        targetGattCharacteristic = targetGattService.getCharacteristic(targetCharacterUuid);
		BluetoothGattCharacteristic readGattCharacteristic = targetGattService.getCharacteristic(readUUID);
		if(readGattCharacteristic != null)
			mBluetoothLeService.setCharacteristicNotification(readGattCharacteristic, true);
        
        if(targetGattCharacteristic != null){
            //Toast.makeText(context, "get character ok", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "not support this BLE module", Toast.LENGTH_SHORT).show();
            return ;
        }
    }  
    
	public void onPause() {
		// TODO Auto-generated method stub
		if(mConnected){
			((MainActivity) context).unregisterReceiver(mGattUpdateReceiver);
		}
	}
	
	public void onDestroy(){
		if(mConnected){
			mDevListAdapter.clearDevice();
			mDevListAdapter.notifyDataSetChanged();
			((MainActivity) context).unbindService(mServiceConnection);
			mBluetoothLeService = null;
			mConnected = false;
        }
	}
	
	public void onResume(){
		if(!mConnected || mBluetoothLeService == null)
			return ;
		((MainActivity)context).registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d("registerReceiver", "Connect request result=" + result);
        }
	}
	
	public synchronized void sendData(byte[] value){
    	if(targetGattCharacteristic != null && mBluetoothLeService != null && mConnected == true){
    		int targetLen = 0;
    		int offset=0;
    		for(int len = (int)value.length; len > 0; len -= 20){
    		  	if(len < 20)
    		  		targetLen = len;
    			else
    				targetLen = 20;
    		  	byte[] targetByte = new byte[targetLen];
    		  	System.arraycopy(value, offset, targetByte, 0, targetLen);
    			offset += 20;
    			targetGattCharacteristic.setValue(targetByte);
    			mBluetoothLeService.writeCharacteristic(targetGattCharacteristic);
    			try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
	
	public synchronized void sendData(String value){
    	if(targetGattCharacteristic != null && mBluetoothLeService != null && mConnected == true){
    		targetGattCharacteristic.setValue(value);
    		mBluetoothLeService.writeCharacteristic(targetGattCharacteristic);  
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
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.obj != null){
				mDevListAdapter.addDevice((BluetoothScanInfo) msg.obj);
				mDevListAdapter.notifyDataSetChanged();	
			}
		}
    };
    
    // scan device
 	public void scanLeDevice(boolean enable) {
 		if (enable) {
 			mDevListAdapter.clearDevice();
 			mDevListAdapter.notifyDataSetChanged();
 			mHandler.postDelayed(new Runnable() {
 			@Override
 				public void run() {
 					mScanning = false;
 					mBluetoothAdapter.stopLeScan(mLeScanCallback);
 					if(onScanListener != null){
 		        		onScanListener.onScanFinished();
 		        	}
 				}
 			}, SCAN_PERIOD);

 			mScanning = true;
 			mBluetoothAdapter.startLeScan(mLeScanCallback);
 		} else {
 			mScanning = false;
 			mBluetoothAdapter.stopLeScan(mLeScanCallback);
 		}
 	}
 	
 	public boolean isScanning(){
 		return mScanning;
 	}
 	
 	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                final byte[] scanRecord) {
        	
        	if(onScanListener != null){
        		onScanListener.onScan(device, rssi, scanRecord);
        	}
        	
        	System.out.println("scan info:");
        	System.out.println("rssi="+rssi);
        	System.out.println("ScanRecord:");
        	for(byte b:scanRecord)
        		System.out.printf("%02X ", b);
        	System.out.println("");
        	
            ((MainActivity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	Message msg = new Message();
                	BluetoothScanInfo info = new BluetoothScanInfo();
                	info.device = device;
                	info.rssi = rssi;
                	info.scanRecord = scanRecord;
                	msg.obj = info;
                	mHandler.sendMessage(msg);
                }
            });      
        }
    };
    
    public class BluetoothScanInfo{
    	public BluetoothDevice device;
    	public int rssi;
    	public byte[] scanRecord;
    };
    
    public static double calculateAccuracy(int txPower, double rssi) {
    	if (rssi == 0) {
    		return -1.0; // if we cannot determine accuracy, return -1.
    	}

    	double ratio = rssi*1.0/txPower;
    	if (ratio < 1.0) {
    		return Math.pow(ratio,10);
    	}
    	else {
    		double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;    
    		return accuracy;
    	}
    }  
}
