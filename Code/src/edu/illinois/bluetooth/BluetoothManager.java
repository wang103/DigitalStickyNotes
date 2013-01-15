package edu.illinois.bluetooth;

import edu.illinois.digitalstickynotes.MainActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * @author tianyiw
 */
public class BluetoothManager {
	private BluetoothAdapter mBluetoothAdapter;
	
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
	}
}