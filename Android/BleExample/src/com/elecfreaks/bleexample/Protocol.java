package com.elecfreaks.bleexample;

import org.json.JSONException;
import org.json.JSONObject;

import com.elecfreaks.ble.BluetoothHandler.OnRecievedDataListener;

import android.content.Context;

public class Protocol {
	
	public static final int DIGITAL = 0;
	public static final int ANALOG = 1;
	public static final int OLED = 2;
	public static final int RGBLED = 3;
	public static final int JOYSTICK = 4;
	public static final int MPU6050 = 5;
	public static final int BUZZER = 6;
	public static final int RELAY = 7;
	public static final int TEMPERATURE = 8;
	public static final int HUMIDITY = 9;
	public static final int INFORMATION = 10;
	
	public static final int WRITE = 0;	
	public static final int READ = 1;
	
	private Context context;
	private Transmitter transmitter;
    private OnReceivedRightDataListener recListener = null;
    
    private static final int SUCCESS = 0;
    private static final int FAILED = -1;
	
	public Protocol(Context context, Transmitter transmitter) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.transmitter = transmitter;
		transmitter.getBluetoothHandler().setOnRecievedDataListener(new OnRecievedDataListener() {
			private byte[] data = null;
			private String str = null;
			@Override
			public void onRecievedData(byte[] bytes) {
				data = MyArray.arrayCat(data, bytes);
				str = new String(data);
				// get data between '{' and '}', example: srcStr="xdgf{"T":0, "V":1}gagha" targetStr="{"T":0, "V":1}"
				if(str.contains("{") && str.contains("}")){
					int start = str.indexOf("{");
					int end = str.indexOf("}")+1;
					if(end <= start)
						return ;
					
					byte[] targetData = new byte[end-start];
					System.arraycopy(data, start, targetData, 0, end-start);
					recListener.onReceivedData(new String(targetData));
					data = null;
				}
			}
		});
	}
	
	public void readPinCommand(int type, int pin){
		JSONObject writeJsonObj = new JSONObject();
		try {
			writeJsonObj.put("T", type);	// type
			writeJsonObj.put("V", 0);		// value
			writeJsonObj.put("P", pin);		// pin
			writeJsonObj.put("M", READ);	// mode
			String targetString = writeJsonObj.toString();
			//System.out.println("targetString: "+targetString);
			transmitter.sendData(targetString.getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readCommand(int type){
		JSONObject writeJsonObj = new JSONObject();
		try {
			writeJsonObj.put("T", type);	// type
			writeJsonObj.put("V", 0);		// value
			String targetString = writeJsonObj.toString();
			//System.out.println("targetString: "+targetString);
			transmitter.sendData(targetString.getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readDigitalDataCommand(int pin){
		readPinCommand(DIGITAL, pin);
	}
	
	public void readAnalogDataCommand(int pin){
		readPinCommand(ANALOG, pin);
	}
	
	public void writePinValue(int type, int pin, int val){
		JSONObject writeJsonObj = new JSONObject();
		try {
			writeJsonObj.put("T", type);	// type
			writeJsonObj.put("V", val);		// value
			writeJsonObj.put("P", pin);		// pin
			writeJsonObj.put("M", WRITE);	// mode
			String targetString = writeJsonObj.toString();
			//System.out.println("targetString: "+targetString);
			transmitter.sendData(targetString.getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeDigitalData(int pin, int val){
		writePinValue(DIGITAL, pin, val);
	}
	
	public void writeAnalogData(int pin, int val){
		writePinValue(ANALOG, pin, val);
	}
	
	public void writeOledString(String str){
		writeValue(OLED, str);
	}
	
	public void writeColor(final int color){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				writeRGBValue(RGBLED, color);
			}
		}).start();
	}
	
	public void writeBuzzer(boolean val){
		writeValue(BUZZER, val);
	}
	
	public void writeValue(int type, boolean val){
		JSONObject writeJsonObj = new JSONObject();
		try {
			writeJsonObj.put("T", type);
			writeJsonObj.put("V", val?1:0);
			String targetString = writeJsonObj.toString();
			//System.out.println("targetString: "+targetString);
			transmitter.sendData(targetString.getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeValue(int type, String str){
		JSONObject writeJsonObj = new JSONObject();
		try {
			writeJsonObj.put("T", type);	// type
			writeJsonObj.put("V", 0);		// value
			writeJsonObj.put("S", str);		// string
			String targetString = writeJsonObj.toString();
			//System.out.println("targetString: "+targetString);
			transmitter.sendData(targetString.getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeRGBValue(int type, int val){
		JSONObject writeJsonObj = new JSONObject();
		try {
			writeJsonObj.put("T", type);	// type
			writeJsonObj.put("V", 0);		// value
			writeJsonObj.put("R", (val>>16)&0xff);// R
			writeJsonObj.put("G", (val>>8)&0xff); // G
			writeJsonObj.put("B", val&0xff);	  // B
			String targetString = writeJsonObj.toString();
			//System.out.println("targetString: "+targetString);
			transmitter.sendData(targetString.getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeValue(int type, int val){
		JSONObject writeJsonObj = new JSONObject();
		try {
			writeJsonObj.put("T", type);	// type
			writeJsonObj.put("V", val);		// value
			String targetString = writeJsonObj.toString();
			//System.out.println("targetString: "+targetString);
			transmitter.sendData(targetString.getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeRelay(boolean val){
		writeValue(RELAY, val);
	}

	
	public interface OnReceivedRightDataListener{
		public int onReceivedData(String strData);
	};
	
	public void setOnReceivedDataListener(OnReceivedRightDataListener l){
		recListener = l;
	}
}
