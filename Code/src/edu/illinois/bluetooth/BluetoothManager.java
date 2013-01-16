package edu.illinois.bluetooth;

import edu.illinois.digitalstickynotes.MainActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author tianyiw
 */
public class BluetoothManager {
	private final IntentFilter intentFilter = new IntentFilter();

	private BluetoothAdapter mBluetoothAdapter;
	private BTBroadcastReceiver broadcastReceiver;
	
	private void initIntentFilter() {
		// Indicates a change in the Bluetooth status.
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		
		// Indicates when a Bluetooth server is found.
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
	}
	
	private void initBroadcastReceiver(MainActivity activity) {
		broadcastReceiver = new BTBroadcastReceiver(activity);
		activity.registerReceiver(broadcastReceiver, intentFilter);
		
		//TODO: need to cancelDiscovery before connection.
	}
	
	public void unregisterBroadcastReceiver(MainActivity activity) {
		activity.unregisterReceiver(broadcastReceiver);
	}
	
	private void initialize(MainActivity activity) {
		initIntentFilter();
		initBroadcastReceiver(activity);
	}
	
	public void startDiscovery() {
		
	}
	
	public void stopDiscovery() {
		
	}
	
	/**
	 * Constructor.
	 * Enable the Bluetooth.
	 * 
	 * @param activity {@link MainActivity} object.
	 */
	public BluetoothManager(MainActivity activity) {
		super();
		
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth.
			activity.setIsBTEnabled(false);
		} else {
			// Device supports Bluetooth. Ask user to enable it if not enabled.
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(enableBtIntent, MainActivity.CODE_REQUEST_ENABLE_BT);
			} else {
				activity.setIsBTEnabled(true);
			}
		}
		
		initialize(activity);
	}
}