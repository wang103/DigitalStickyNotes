package edu.illinois.bluetooth;

import java.util.ArrayList;

import edu.illinois.digitalstickynotes.MainActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receiver for Bluetooth broadcast.
 * 
 * @author tianyiw
 */
public class BTBroadcastReceiver extends BroadcastReceiver {

	private ArrayList<BluetoothDevice> devices;
	private MainActivity activity;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
			switch (state) {
			case BluetoothAdapter.STATE_OFF:
				activity.setIsBTEnabled(false, false);
				break;
			case BluetoothAdapter.STATE_ON:
				activity.setIsBTEnabled(true, true);
				break;
			}
		}
		else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			// New nearby devices found.
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			devices.add(device);
			
			Log.d("TIANYI", "New device added: " + device.getName() + " @ " + device.getAddress());
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param activity the {@link MainActivity} object.
	 * @param devices a List to put nearby devices in.
	 */
	public BTBroadcastReceiver(MainActivity activity, ArrayList<BluetoothDevice> devices) {
		super();
		
		this.activity = activity;
		this.devices = devices;
	}
}