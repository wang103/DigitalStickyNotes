package edu.illinois.bluetooth;

import java.util.ArrayList;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

/**
 * @author tianyiw
 */
public class BluetoothManager extends ConnectionManager {

	private ArrayList<BluetoothDevice> devices;

	private BluetoothAdapter mBluetoothAdapter;
	private BTBroadcastReceiver broadcastReceiver;
	
	private void initIntentFilter() {
		// Indicates a change in the Bluetooth status.
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		
		// Indicates when a Bluetooth server is found.
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
	}
	
	private void initBroadcastReceiver(MainActivity activity) {
		broadcastReceiver = new BTBroadcastReceiver(activity, devices);
		activity.registerReceiver(broadcastReceiver, intentFilter);
	}
	
	@Override
	public void unregisterBroadcastReceiver(MainActivity activity) {
		activity.unregisterReceiver(broadcastReceiver);
	}
	
	private void initialize(MainActivity activity) {
		initIntentFilter();
		initBroadcastReceiver(activity);
	}
	
	@Override
	public boolean startDiscovery() {
		this.devices.clear();
		return mBluetoothAdapter.startDiscovery();
	}
	
	@Override
	public boolean stopDiscovery() {
		return mBluetoothAdapter.cancelDiscovery();
	}
	
	public boolean connectionEnabled() {
		return mBluetoothAdapter != null & mBluetoothAdapter.isEnabled();
	}
	
	/**
	 * Constructor.
	 * Enable the Bluetooth.
	 * 
	 * @param activity {@link MainActivity} object.
	 */
	public BluetoothManager(MainActivity activity) {
		super();
		
		devices = new ArrayList<BluetoothDevice>();
		
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth.
			activity.setIsBTEnabled(false, false);
		} else {
			// Device supports Bluetooth. Ask user to enable it if not enabled.
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(enableBtIntent, MainActivity.CODE_REQUEST_ENABLE_BT);
			} else {
				activity.setIsBTEnabled(true, false);
			}
		}
		
		initialize(activity);
	}
}