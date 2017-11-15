package com.example.jlazaro.bulb;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by J.Lazaro on 11/11/2017.
 */

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    private final BluetoothAdapter mBluetoothAdapter;

    Context mContext;

    public BluetoothConnectionService(Context context) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mContext = context;
        start();
    }



    //thread that runs while listening for incoming connections
    //it behaves like a server-side client
    //it runs until a connection is accepted (or until cancelled)
    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;
            //create a new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName,MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: "+  e.getMessage());
            }
            mmServerSocket = tmp;
        }
        
        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;
            try {
                //This is a blocking call and will olnly return on a
                //successfull connection or an exception
                Log.d(TAG, "run: RFCOM server socet start.....");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection");

            } catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException: "+  e.getMessage());
            }

            //In Third Tutorial
            if(socket != null){
                connected(socket,mmDevice);
            }

            Log.i(TAG, "run: END mAcceptThread");
        }
        
        public void cancel(){
            Log.d(TAG, "cancel: Canceling AcceptThread");
            try{
                mmServerSocket.close();
            } catch(IOException e){
                Log.e(TAG, "cancel: Close AcceptThread ServerSocket failed. " +e.getMessage() );
            }

        }
    }

    public class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG, "ConnectThread: started");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "run: mConnectThread");

            //Get a BluetoothSocket for a connection with the
            //given BluetoothDevice
            try {
                Log.d(TAG, "run: Trying to create InsecureRfcommSocket using UUID: " + MY_UUID_INSECURE);
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "run: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;

            mBluetoothAdapter.cancelDiscovery();

            try {
                //This is a blocking call and will olnly return on a
                //successfull connection or an exception
                mmSocket.connect();
                Log.d(TAG, "run: Connection Successful");
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket");
                } catch (IOException e1) {
                    Log.e(TAG, "run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: Could not connect to UUID: " + MY_UUID_INSECURE);
            }
            //In third Tutorial
            connected(mmSocket,mmDevice);
        }

        public void cancel(){
            Log.d(TAG, "cancel: Closing Client Socket");
            try{
                mmSocket.close();
            } catch(IOException e){
                Log.e(TAG, "cancel: close() of mmSocket in ConnectThread " + e.getMessage() );
            }
        }
    }



    //start the chat service.
    //Start the AcceptThread to begin a session in listening(server) mode.
    //Called by the Activity onResume()
    public synchronized void start(){
        Log.d(TAG, "start");

        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mInsecureAcceptThread == null){
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    //AcceptThread starts and waits for a connection
    //Then ConnectThread starts and tries to make a connection with the other devices AcceptThread
    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: Started");

        //Initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth", "Please Wait...", true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    //Connection Thread is responsible for maintaining the BT Connection, Sending the data, and
    //receiving incoming data through input/output streams respectively
    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG, "ConnectedThread: Starting");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            //dismiss process dialog when connection is established
            try{
                mProgressDialog.dismiss();
            }catch(NullPointerException e){
                e.printStackTrace();
            }




            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024]; //buffer store for the stream
            int bytes; //bytes reutrned from read

            //Keep listening to the InputStream until an Exception Occurs
            while(true){
                //Read from the Input Stream
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "run: InputStream: " + incomingMessage);

                    Intent incomingMessageIntent = new Intent("incomingMessage");
                    incomingMessageIntent.putExtra("theMessage", incomingMessage);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingMessageIntent);

                } catch (IOException e) {
                    Log.e(TAG, "run: Error reading input. " + e.getMessage());
                    break;
                }
            }
        }

        //Call this from MainActivity to send data to the remote device
        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Wrting to OutputStream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to OutputStream. " + e.getMessage());
            }
        }

        //call this from MainActivity to end connection
        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice){
        Log.d(TAG, "connected: Starting");

        //Start the thread to manage the connection and to perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }
    /*
    * write() in the ConnectedThread is unsynchronized
    * @param out The bytes to write
    * @see ConnectedThread#write(byte[])
    */
    public void write(byte[] out){
        //Create temporary object
        ConnectedThread r;
        
        //Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        mConnectedThread.write(out);
    }
}
