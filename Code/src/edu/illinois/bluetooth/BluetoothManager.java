package edu.illinois.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Manage the Bluetooth connections.
 * 
 * @author tianyiw
 */
public class BluetoothManager extends ConnectionManager {

	final private int DISCOVERY_LENGTH = 12;		// Each discovery lasts 12 seconds.
	final private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	final static private int MAX_MSG_LENGTH = 1024;	// Maximum message length.

	private ArrayList<BluetoothDevice> devices;		// Devices discovered.

	private BluetoothAdapter mBluetoothAdapter;
	private BTBroadcastReceiver broadcastReceiver;
	
	@Override
	public boolean startDiscovery() {
		this.devices.clear();
		return mBluetoothAdapter.startDiscovery();
	}
	
	@Override
	public boolean startDiscoveryAndWait() {
		this.devices.clear();
		boolean status = mBluetoothAdapter.startDiscovery();
		
		try {
			Thread.sleep(DISCOVERY_LENGTH * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		return status;
	}
	
	@Override
	public boolean stopDiscovery() {
		return mBluetoothAdapter.cancelDiscovery();
	}
	
	@Override
	public boolean connectionEnabled() {
		return mBluetoothAdapter != null & mBluetoothAdapter.isEnabled();
	}
	
	/**
	 * Unregister the {@link BroadcastReceiver}.
	 * 
	 * @param activity the {@link Activity} object.
	 */
	private void unregisterBroadcastReceiver(Activity activity) {
		activity.unregisterReceiver(broadcastReceiver);
	}
	
	@Override
	public void destroy(Activity activity) {
		unregisterBroadcastReceiver(activity);
	};
	
	@Override
	public List<String> talkToServers(String s, boolean talkToOneServer, boolean startDiscovery) {
		
		Log.d("TIANYI", "Sending " + s + "to the local server.");
	
		if (startDiscovery) {
			startDiscoveryAndWait();
			stopDiscovery();
		}
		
		List<String> result = new ArrayList<String>();
		
		for (BluetoothDevice device : devices) {
			BluetoothSocket bluetoothSocket;
			try {
				bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
				bluetoothSocket.connect();

				// Communicate, receive messages.
				InputStream inputStream = bluetoothSocket.getInputStream();
				OutputStream outputStream = bluetoothSocket.getOutputStream();
				
				// First send request to the local server, then wait for
				// server's response.
				byte[] outputBuffer = s.getBytes();
				outputStream.write(outputBuffer);

				byte[] inputBuffer = new byte[MAX_MSG_LENGTH];
				int bytes;		// bytes read from inputStream
				bytes = inputStream.read(inputBuffer);
				String inputMessage = new String(inputBuffer, 0, bytes);
				
				Log.d("TIANYI", bytes + " bytes of message read: " + inputMessage);
				
				result.add(inputMessage);
				
				// Done with this connection, clean up.
				outputStream.close();
				inputStream.close();
				bluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (talkToOneServer) {
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Initialize the {@link BroadcastReceiver} for Bluetooth connections.
	 * 
	 * @param activity the {@link MainActivity} object.
	 */
	private void initBroadcastReceiver(MainActivity activity) {
		broadcastReceiver = new BTBroadcastReceiver(activity, devices);
		activity.registerReceiver(broadcastReceiver, intentFilter);
	}
	
	/**
	 * Initialize the {@link IntentFilter} for Bluetooth connections.
	 */
	private void initIntentFilter() {
		// Indicates a change in the Bluetooth status.
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		
		// Indicates when a Bluetooth server is found.
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
	}
	
	/**
	 * Initialize the Bluetooth settings.
	 * 
	 * @param activity the {@link MainActivity} object.
	 */
	private void initialize(MainActivity activity) {
		initIntentFilter();
		initBroadcastReceiver(activity);
	}
	
	/**
	 * Constructor.
	 * Enable the Bluetooth Manager.
	 * 
	 * @param activity the {@link MainActivity} object.
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
				activity.startActivityForResult(enableBtIntent, MainActivity.ACTIVITY_CODE_BLUETOOTH);
			} else {
				activity.setIsBTEnabled(true, false);
			}
		}
		
		initialize(activity);
	}
}