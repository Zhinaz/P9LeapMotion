
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.*;
import javax.microedition.io.*;

/**
 * Class that implements an SPP Server which accepts single line of message from
 * an SPP client and sends a single line of response to the client.
 */
public class BluetoothServer {
	// start server
	private void startServer() throws IOException {

		// Create a UUID for SPP and server URL
		UUID uuid = new UUID("951753", true);
		String url = "btspp://localhost:" + uuid + ";name=P9 LeapMotion";

		// Open server url
		StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(url);

		// Wait for client connection
		System.out.println("\nServer Started. Waiting for clients to connect…");
		StreamConnection connection = streamConnNotifier.acceptAndOpen();

		RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
		System.out.println("Remote device address: " + dev.getBluetoothAddress());
		System.out.println("Remote device name: " + dev.getFriendlyName(true));

		// read string from client
		InputStream inStream = connection.openInputStream();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		String lineRead = bReader.readLine();
		System.out.println(lineRead);

		// send response to client
		OutputStream outStream = connection.openOutputStream();
		PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
		pWriter.write("Response String from SPP Server\r\n");
		pWriter.flush();

		pWriter.close();
		streamConnNotifier.close();

	}

	public static void main(String[] args) throws IOException {

		// display local device address and name
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		System.out.println("Address: " + localDevice.getBluetoothAddress());
		System.out.println("Name: " + localDevice.getFriendlyName());

		BluetoothServer sampleSPPServer = new BluetoothServer();
		sampleSPPServer.startServer();

	}
}
