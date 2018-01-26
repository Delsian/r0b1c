package hu.uw.pallergabor.motorboat;

import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String LOG_TAG = "MotorBoat.MainActivity";
	private static final long SCAN_PERIOD = 10*1000L;
	private static final long HEARTBEAT_RESPONSE_DELAY = 100L;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int MASK_MOTOR_LEFT = 0x01;
	private static final int MASK_MOTOR_RIGHT = 0x02;
	private static final int MASK_HEARTBEAT = 0x04;

	// RFduino 2220, 2221, 2222, 2223
	public static final UUID RFDUINO_SERVICE_UUID = UUID.fromString("00002220-0000-1000-8000-00805F9B34FB");
	public static final UUID RECEIVE_CHARACTERISTIC_UUID = UUID.fromString("00002221-0000-1000-8000-00805F9B34FB");
	public static final UUID SEND_CHARACTERISTIC_UUID = UUID.fromString("00002222-0000-1000-8000-00805F9B34FB");
	public static final UUID DISCONNECT_CHARACTERISTIC_UUID = UUID.fromString("00002223-0000-1000-8000-00805F9B34FB");
	// 0x2902 org.bluetooth.descriptor.gatt.client_characteristic_configuration.xml
	public final static UUID CLIENT_CHARACTERISTIC_CONFIGURATION_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

	private TextView connStatusTV;
	private ImageButton doubleForwardButton;
	private ImageButton leftForwardButton;
	private ImageButton rightForwardButton;
	private ImageButton stopButton;
	private Handler handler;
	private StopStartLEScanTask leScanTask;
	private GattExplorerFoundTask geFoundTask;
	private GattExplorerConnectedTask geConnectedTask;
	private GattExplorerDisconnectedTask geDisconnectedTask;
	private GattExplorerDiscoveredTask geDiscoveredTask;
	private SendHeartbeatTask heartBeatTask;
	private BluetoothAdapter bluetoothAdapter;
	private boolean scanning = false;
	private BluetoothDevice gattExplorerDevice;
	private BluetoothGatt gatt;
	private BluetoothGattService gattService;
	private BluetoothGattCharacteristic receiveCharacteristic;
	private BluetoothGattCharacteristic sendCharacteristic;
	private BluetoothGattCharacteristic disconnectCharacteristic;
	static BoatSurface boatSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connStatusTV = (TextView) findViewById(R.id.connstatus);
		doubleForwardButton = (ImageButton)findViewById(R.id.ib_doubleforward);
		leftForwardButton = (ImageButton)findViewById(R.id.ib_leftforward);
		rightForwardButton = (ImageButton)findViewById(R.id.ib_rightforward);
		stopButton = (ImageButton)findViewById(R.id.ib_stop);
		handler = new Handler();
		leScanTask = new StopStartLEScanTask();
		geFoundTask = new GattExplorerFoundTask();
		geConnectedTask = new GattExplorerConnectedTask();
		geDisconnectedTask = new GattExplorerDisconnectedTask();
		geDiscoveredTask = new GattExplorerDiscoveredTask();
		heartBeatTask = new SendHeartbeatTask();
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
		doubleForwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if( gattService != null )
            		boatForward();
            }
        });
		rightForwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if( gattService != null )
            		boatRightForward();
            }
        });
		leftForwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if( gattService != null )
            		boatLeftForward();
            }
        });
		stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if( gattService != null )
            		boatStop();
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
		disconnectBoat();
		scanLeDevice(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu)	{
		MenuItem rescanItem = menu.findItem(R.id.rescan);
		rescanItem.setEnabled( gatt == null );
		MenuItem disconnectItem = menu.findItem(R.id.disconnect);
		disconnectItem.setEnabled( gatt != null );
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if( id == R.id.rescan) {
			scanLeDevice(true);
		} else
			if( id == R.id.disconnect) {
				disconnectBoat();
			}
		return super.onOptionsItemSelected(item);
	}

	private void disconnectBoat() {
		if( gatt != null ) {
			disconnectCharacteristic.setValue("");
            disconnectCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            gatt.writeCharacteristic(disconnectCharacteristic);			
			gatt.disconnect();
			gatt = null;
			gattService = null;
		}
	}

	private void scanLeDevice(final boolean enable) {
		Log.d(LOG_TAG, "scanLeDevice: "+enable);
		if (enable) {
			gattExplorerDevice = null;
			gatt = null;
			gattService = null;
			searchingUI();
			Log.d( LOG_TAG, "(scanLEDevice) scan stop scheduled after "+Long.toString(SCAN_PERIOD)+" msec");
			handler.postDelayed(leScanTask, SCAN_PERIOD);

			scanning = true;
			bluetoothAdapter.startLeScan(leScanCallback);
		} else {
			scanning = false;
			idleUI();
			handler.removeCallbacks(leScanTask);
			bluetoothAdapter.stopLeScan(leScanCallback);
		}
	}

	private void disableButtons() {
		doubleForwardButton.setEnabled(false);
		rightForwardButton.setEnabled(false);
		leftForwardButton.setEnabled(false);
		stopButton.setEnabled(false);
	}

	private void enableButtons() {
		doubleForwardButton.setEnabled(true);
		rightForwardButton.setEnabled(true);
		leftForwardButton.setEnabled(true);
		stopButton.setEnabled(true);
	}

	private void searchingUI() {
		connStatusTV.setText(R.string.searching);
		disableButtons();
	}

	private void idleUI() {
		connStatusTV.setText(R.string.idle);
		disableButtons();
	}
	
	private void boatForward() {
		sendCharacteristic.setValue(MASK_MOTOR_LEFT|MASK_MOTOR_RIGHT,BluetoothGattCharacteristic.FORMAT_UINT8,0);
	    boolean initiated = gatt.writeCharacteristic(sendCharacteristic);
	    Log.d( LOG_TAG, "boatForward: "+initiated);
	}

	private void boatRightForward() {
		sendCharacteristic.setValue(MASK_MOTOR_RIGHT,BluetoothGattCharacteristic.FORMAT_UINT8,0);
	    boolean initiated = gatt.writeCharacteristic(sendCharacteristic);
	    Log.d( LOG_TAG, "boatRightForward: "+initiated);
	}

	private void boatLeftForward() {
		sendCharacteristic.setValue(MASK_MOTOR_LEFT,BluetoothGattCharacteristic.FORMAT_UINT8,0);
	    boolean initiated = gatt.writeCharacteristic(sendCharacteristic);
	    Log.d( LOG_TAG, "boatLeftForward: "+initiated);
	}
	
	private void boatStop() {
		sendCharacteristic.setValue(0,BluetoothGattCharacteristic.FORMAT_UINT8,0);
	    boolean initiated = gatt.writeCharacteristic(sendCharacteristic);
	    Log.d( LOG_TAG, "boatStop: "+initiated);
	}
		
	private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			if( newState == BluetoothProfile.STATE_DISCONNECTED) {
				MainActivity.this.gatt = null;
				MainActivity.this.runOnUiThread(geDisconnectedTask);
			} else
				if( newState == BluetoothProfile.STATE_CONNECTED) {
					MainActivity.this.gatt = gatt;
					MainActivity.this.runOnUiThread(geConnectedTask);
					boolean success = gatt.discoverServices();
					Log.d( LOG_TAG, "discoverServices: "+success);
				}
		}

		// New services discovered
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.d( LOG_TAG, "services discovered");
				gattService = gatt.getService(RFDUINO_SERVICE_UUID);
				receiveCharacteristic = gattService.getCharacteristic(RECEIVE_CHARACTERISTIC_UUID);
				sendCharacteristic = gattService.getCharacteristic(SEND_CHARACTERISTIC_UUID);
				disconnectCharacteristic = gattService.getCharacteristic(DISCONNECT_CHARACTERISTIC_UUID);
				gatt.setCharacteristicNotification(receiveCharacteristic, true);
				BluetoothGattDescriptor receiveConfigDescriptor = receiveCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION_UUID);
				if (receiveConfigDescriptor != null) {
					receiveConfigDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
					gatt.writeDescriptor(receiveConfigDescriptor);
				} else {
					Log.e(LOG_TAG, "Receive Characteristic can not be configured.");
				}
				MainActivity.this.runOnUiThread(geDiscoveredTask);
			} else {
				Log.w(LOG_TAG, "onServicesDiscovered received: " + status);
			}
		}

		public void onCharacteristicWrite (
				BluetoothGatt gatt, 
				BluetoothGattCharacteristic characteristic, 
				int status) {
			String success = status == BluetoothGatt.GATT_SUCCESS ?
					"Success" :
						"Failure";
			String message = 
					"Write operation on "+
							characteristic.getUuid().toString()+
							" : "+success;
			Log.d( LOG_TAG, "status: "+status+" : "+message);
		}

		public void onCharacteristicChanged (
				BluetoothGatt gatt, 
				BluetoothGattCharacteristic characteristic) {
			Object o = characteristic.getValue();
			String message = 
					"Characteristic changed: "+
							characteristic.getUuid().toString();
			if( o instanceof byte[] ) {
				byte b[] = (byte[])o;
				if( b.length == 1 ) {
					if( ( b[0] & MASK_HEARTBEAT ) != 0 ) {
						handler.postDelayed(heartBeatTask, HEARTBEAT_RESPONSE_DELAY);
					} else
					if( ( b[0] >= 0 ) && 
						( b[0] <= 3 ) ) {
						Log.d( LOG_TAG, "motor status: "+b[0]);
						BoatSurfaceUpdateTask bsut = new BoatSurfaceUpdateTask( b[0]);
						MainActivity.this.runOnUiThread(bsut);
					}
				}
			} else
				message = message + o.toString();
			Log.d( LOG_TAG, message);
		}
	};

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback leScanCallback =
			new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			String deviceName = device.getName();
			Log.d(LOG_TAG, "device found: '"+deviceName+"'");
			if( "motor_boat".equals( deviceName ) ) {
				gattExplorerDevice = device;
				MainActivity.this.runOnUiThread( geFoundTask ); 
				gattExplorerDevice.connectGatt(
						MainActivity.this, 
						false, 
						gattCallback);
			}
		}
	};

	class GattExplorerFoundTask implements Runnable {
		public void run() {
			connStatusTV.setText(R.string.ge_found);
		}

	}

	class GattExplorerConnectedTask implements Runnable {
		public void run() {
			connStatusTV.setText(R.string.ge_connected);
		}    	
	}

	class GattExplorerDiscoveredTask implements Runnable {
		public void run() {
			connStatusTV.setText(R.string.ge_discovered);
			enableButtons();
		}    	
	}



	class GattExplorerDisconnectedTask implements Runnable {
		public void run() {
			connStatusTV.setText(R.string.ge_disconnected);
			disableButtons();
			BoatSurface.setMotors(false, false);
			if( boatSurface != null )
				boatSurface.invalidate();
		}    	
	}

	class StopStartLEScanTask implements Runnable {
		public void run() {
			Log.d( LOG_TAG, "StopStartLEScanTask.run() at "+Long.toString( System.currentTimeMillis()));
			bluetoothAdapter.stopLeScan(leScanCallback);
			if( gattExplorerDevice == null ) {
				bluetoothAdapter.startLeScan(leScanCallback);    		
				Log.d( LOG_TAG, "(run) scan stop scheduled after "+Long.toString(SCAN_PERIOD)+" msec");
				handler.postDelayed(leScanTask, SCAN_PERIOD);
			}
		}
	}
	
	class SendHeartbeatTask implements Runnable {

		@Override
		public void run() {
			if( gatt != null ) {
				sendCharacteristic.setValue(MASK_HEARTBEAT,BluetoothGattCharacteristic.FORMAT_UINT8,0);
	            sendCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
				boolean initiated = gatt.writeCharacteristic(sendCharacteristic);
				Log.d( LOG_TAG, "heartbeat: "+initiated);
			}
		}
		
	}
	
	class BoatSurfaceUpdateTask implements Runnable {
		byte b;
		
		public BoatSurfaceUpdateTask( byte b ) {
			this.b = b;
		}
		

		@Override
		public void run() {
			boolean leftMotor = ( b & MASK_MOTOR_LEFT ) != 0;
			boolean rightMotor = ( b & MASK_MOTOR_RIGHT ) != 0;
	    	BoatSurface.setMotors( leftMotor,rightMotor);
	    	if( boatSurface != null )
	    		boatSurface.invalidate();
		}
		
	}

}

