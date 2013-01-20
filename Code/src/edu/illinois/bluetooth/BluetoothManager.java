package edu.illinois.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

/**
 * @author tianyiw
 */
public class BluetoothManager extends ConnectionManager {

	final private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	final static private int MAX_MSG_LENGTH = 1024;

	private ArrayList<BluetoothDevice> devices;

	private BluetoothAdapter mBluetoothAdapter;
	private BTBroadcastReceiver broadcastReceiver;
	
	@Override
	public void talkToServers() {
		for (BluetoothDevice device : devices) {
			BluetoothSocket bluetoothSocket;
			try {
				bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
				bluetoothSocket.connect();

				// Communicate, receive messages.
				InputStream inputStream = bluetoothSocket.getInputStream();
				OutputStream outputStream = bluetoothSocket.getOutputStream();
				
				// First send identification information, then wait for
				// server's messages.
				String outputMessage = "Hello from client A.\n";
				byte[] outputBuffer = outputMessage.getBytes();
				outputStream.write(outputBuffer);
				
				byte[] inputBuffer = new byte[MAX_MSG_LENGTH];
				int bytes;		// bytes read from inputStream
				bytes = inputStream.read(inputBuffer);
				String inputMessage = new String(inputBuffer);
				
				Log.d("TIANYI", bytes + " bytes of message read: " + inputMessage);
				
				//TODO: do something with this information, update the UI.
				
				// Done with this connection, clean up.
				outputStream.close();
				inputStream.close();
				bluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
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