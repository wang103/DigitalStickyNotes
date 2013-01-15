package edu.illinois.bluetooth;

import java.util.ArrayList;

import edu.illinois.digitalstickynotes.MainActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author tianyiw
 */
public class BTBroadcastReceiver extends BroadcastReceiver {

	private ArrayList<BluetoothDevice> devices;
	private MainActivity activity;

	public BTBroadcastReceiver(MainActivity activity) {
		super();
		
		this.activity = activity;
		devices = new ArrayList<BluetoothDevice>();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
			switch (state) {
			case BluetoothAdapter.STATE_OFF:
				activity.setIsBTEnabled(false);
				break;
			case BluetoothAdapter.STATE_ON:
				activity.setIsBTEnabled(true);
				break;
			}
		}
		else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			devices.add(device);
		}
	}
}