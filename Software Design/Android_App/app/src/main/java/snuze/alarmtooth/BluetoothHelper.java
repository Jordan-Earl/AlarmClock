//package snuze.alarmtooth;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothServerSocket;
//import android.bluetooth.BluetoothSocket;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.os.Handler;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Set;
//import java.util.UUID;
//
//
//public class BluetoothHelper {
//
//    public static final int STATE_NONE = 0;       // we're doing nothing
//    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
//    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
//    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
//
//    private static final String NAME = "BlueToothThread";
//    private static final String TAG = "BlueToothService";
//    private final Handler mHandler;
//    private final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
//
//    private ArrayAdapter<String> knownDevices = null;
//    private BluetoothAdapter Adapter;
//    public String someText;
//    private Activity mainActivity;
//    private int mState;
//    private ConnectThread mConnectThread;
//    private ConnectedThread mConnectedThread;
//
//    BluetoothHelper(Activity activity, Handler handler){
//
//        final int ACTION_REQUEST_BT = 1;
//        mHandler = handler;
//        Adapter = BluetoothAdapter.getDefaultAdapter();
//        mainActivity = activity;
//        knownDevices = new ArrayAdapter<>(activity, 0);
//        mState = STATE_NONE;
//
//        someText = "";
//
//        if (Adapter == null){
//            System.out.print("Device does not support Bluetooth!");
//        }
//
//        if (!Adapter.isEnabled()) {
//            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            mainActivity.startActivityForResult(enableBT, ACTION_REQUEST_BT);
//        }
//
//
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        mainActivity.registerReceiver(BT_Receiver, filter);
//        IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        mainActivity.registerReceiver(discoveryReceiver, discoveryFilter);
//
//        Set<BluetoothDevice> Devices = Adapter.getBondedDevices();
//        if (Devices.size() > 0){
//            for (BluetoothDevice device : Devices){
//                knownDevices.add( device.getName() );
//            }
//        }
//
//
//        Adapter.startDiscovery();
//    }
//
//    public int getState() {
//
//        return mState;
//    }
//
//    public void getState(int state) {
//
//        mState = state;
//    }
//
//    private BroadcastReceiver BT_Receiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Add the name and address to an array adapter to show in a ListView
//                someText = someText + device.getName() + " : " + device.getAddress();
//                knownDevices.add(device.getName() + "\n" + device.getAddress());
//            }
//        }
//    };
//
//    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            // When discovery finds a device
//            mainActivity.unregisterReceiver(BT_Receiver);
//            Adapter.cancelDiscovery();
//            mainActivity.unregisterReceiver(discoveryReceiver);
//        }
//    };
//
//    private class AcceptThread extends Thread {
//        // The local server socket
//        private final BluetoothServerSocket mmServerSocket;
//        private String mSocketType;
//
//        public AcceptThread(boolean secure) {
//            BluetoothServerSocket tmp = null;
//            mSocketType = secure ? "Secure" : "Insecure";
//
//            // Create a new listening server socket
//            try {
//
//                    tmp = Adapter.listenUsingRfcommWithServiceRecord(NAME,
//                            UUID.randomUUID());
//
//            } catch (IOException e) {
//                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
//            }
//            mmServerSocket = tmp;
//        }
//
//        public void run() {
//            Log.d(TAG, "Socket Type: " + mSocketType +
//                    "BEGIN mAcceptThread" + this);
//            setName("AcceptThread" + mSocketType);
//
//            BluetoothSocket socket = null;
//
//            // Listen to the server socket if we're not connected
//            while (mState != STATE_CONNECTED) {
//                try {
//                    // This is a blocking call and will only return on a
//                    // successful connection or an exception
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
//                    break;
//                }
//
//                // If a connection was accepted
//                if (socket != null) {
//                    synchronized (BluetoothHelper.this) {
//                        switch (mState) {
//                            case STATE_LISTEN:
//                            case STATE_CONNECTING:
//                                // Situation normal. Start the connected thread.
//                                connected(socket, socket.getRemoteDevice(),
//                                        mSocketType);
//                                break;
//                            case STATE_NONE:
//                            case STATE_CONNECTED:
//                                // Either not ready or already connected. Terminate new socket.
//                                try {
//                                    socket.close();
//                                } catch (IOException e) {
//                                    Log.e(TAG, "Could not close unwanted socket", e);
//                                }
//                                break;
//                        }
//                    }
//                }
//            }
//            Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
//
//        }
//
//        public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, final String socketType){
//
//        }
//
//        public void cancel() {
//            Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
//            try {
//                mmServerSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
//            }
//        }
//    }
//
//    private class ConnectThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//        private String mSocketType;
//
//        public ConnectThread(BluetoothDevice device) {
//            mmDevice = device;
//            BluetoothSocket tmp = null;
//            mSocketType = "insecure";
//
//            // Get a BluetoothSocket for a connection with the
//            // given BluetoothDevice
//            try {
//                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//            } catch (IOException e) {
//                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
//            }
//            mmSocket = tmp;
//        }
//
//        public void run() {
//            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
//            setName("ConnectThread" + mSocketType);
//
//            // Always cancel discovery because it will slow down a connection
//            Adapter.cancelDiscovery();
//
//            // Make a connection to the BluetoothSocket
//            try {
//                // This is a blocking call and will only return on a
//                // successful connection or an exception
//                mmSocket.connect();
//            } catch (IOException e) {
//                // Close the socket
//                try {
//                    mmSocket.close();
//                } catch (IOException e2) {
//                    Log.e(TAG, "unable to close() " + mSocketType +
//                            " socket during connection failure", e2);
//                }
//
//                return;
//            }
//
//            // Reset the ConnectThread because we're done
//            synchronized (BluetoothHelper.this) {
//                mConnectThread = null;
//            }
//
//            // Start the connected thread
//            connected(mmSocket, mmDevice, mSocketType);
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
//            }
//        }
//    }
//
//    private class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket, String socketType) {
//            Log.d(TAG, "create ConnectedThread: " + socketType);
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the BluetoothSocket input and output streams
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                Log.e(TAG, "temp sockets not created", e);
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run() {
//            Log.i(TAG, "BEGIN mConnectedThread");
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            // Keep listening to the InputStream while connected
//            while (mState == STATE_CONNECTED) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//
//                    // Send the obtained bytes to the UI Activity
//                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
//                } catch (IOException e) {
//                    Log.e(TAG, "disconnected", e);
//                    connectionLost();
//                    // Start the service over to restart listening mode
//                    BluetoothHelper.this.start();
//                    break;
//                }
//            }
//        }
//
//        /**
//         * Write to the connected OutStream.
//         *
//         * @param buffer The bytes to write
//         */
//        public void write(byte[] buffer) {
//            try {
//                mmOutStream.write(buffer);
//
//                // Share the sent message back to the UI Activity
//                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
//                        .sendToTarget();
//            } catch (IOException e) {
//                Log.e(TAG, "Exception during write", e);
//            }
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "close() of connect socket failed", e);
//            }
//        }
//    }
//}
//
//}
