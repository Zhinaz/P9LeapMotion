import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * A simple SPP client that connects with an SPP server
 */
public class BluetoothClient implements DiscoveryListener {

	// object used for waiting
	private static Object lock = new Object();

	// vector containing the devices discovered
	private static Vector vecDevices = new Vector();

	private static String connectionURL = null;

	// The connection
	StreamConnection streamConnection;

	private boolean connected = false;
	
	private ConnectedThread mConnectedThread;

	public BluetoothClient() {
	}
	
	public boolean isConnected() {
		return connected;
	}

	public void initialise() throws IOException {

		BluetoothClient client = new BluetoothClient();
		// display local device address and name
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		// System.out.println("Address: " + localDevice.getBluetoothAddress());
		// System.out.println("Name: " + localDevice.getFriendlyName());

		// find devices
		DiscoveryAgent agent = localDevice.getDiscoveryAgent();

		System.out.println("Starting device inquiry...");
		agent.startInquiry(DiscoveryAgent.GIAC, client);

		try {
			synchronized (lock) {
				// Wait for inquiry to finish
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Device Inquiry Completed. ");

		// print all devices in vecDevices
		int deviceCount = vecDevices.size();

		if (deviceCount <= 0) {
			System.out.println("No Devices Found .");
			System.exit(0);
		} else {
			// print bluetooth device addresses and names in the format [
			// No.address (name) ]
			System.out.println("Bluetooth Devices: ");
			for (int i = 0; i < deviceCount; i++) {
				RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
				System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " ("
						+ remoteDevice.getFriendlyName(true) + ")");
			}
		}
		System.out.print("Choose Device index: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		String chosenIndex = bReader.readLine();
		int index = Integer.parseInt(chosenIndex.trim());

		
		// check for spp service
		RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(index - 1);
		UUID[] uuidSet = new UUID[1];
		uuidSet[0] = new UUID("fa87c0d0afac11de8a390800200c9a67", false);
		System.out.println("\nSearching for service (uuid match)...");
		agent.searchServices(null, uuidSet, remoteDevice, client);
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (connectionURL == null) {
			System.out.println("Device does not support Simple SPP Service.");
			System.exit(0);
		}

		// connect to the server
		streamConnection = (StreamConnection) Connector.open(connectionURL);
		connected = true;

		mConnectedThread = new ConnectedThread(streamConnection);
	}
	
	public void sendMessage(String message) {
		if (connected) {
			mConnectedThread.write(message); 
		}
	}	
	

	// Required methods, what happens when a new device is discovered
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		// add the device to the vector
		if (!vecDevices.contains(btDevice)) {
			vecDevices.addElement(btDevice);
		}
	}

	// Discovering services
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		if (servRecord != null && servRecord.length > 0) {
			connectionURL = servRecord[0].getConnectionURL(0, false);
		}
		synchronized (lock) {
			lock.notify();
		}
	}

	// Implement this method since services are not being discovered
	public void serviceSearchCompleted(int transID, int respCode) {
		synchronized (lock) {
			lock.notify();
		}
	}

	// Done with the inquiry, release lock
	public void inquiryCompleted(int discType) {
		synchronized (lock) {
			lock.notify();
		}
	}
	
	private class ConnectedThread extends Thread {
		private final StreamConnection mStreamConnection;
		private final InputStream inStream;
		private final OutputStream outStream;
		
		public ConnectedThread(StreamConnection streamConnection) {
			
			System.out.println("----- ConnectedThread Startet! -----");
			
			mStreamConnection = streamConnection;
			InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = streamConnection.openInputStream();
                tmpOut = streamConnection.openOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inStream = tmpIn;
            outStream = tmpOut;
		}
		
		public void run() {
			BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
			while (true) {
				try {
					String lineRead = bReader.readLine();
					System.out.println(lineRead);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Disconnected?");
					break;
				}
			}
		}
		
		public void write(String message) {
			if (connected) {
				PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
				pWriter.write(message);
				pWriter.flush();
			}
		}
		
		public void cancel() {
			try {
				mStreamConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
