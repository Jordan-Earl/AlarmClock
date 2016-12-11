package snuze.alarmtooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BluetoothHelper {

    private final int ACTION_REQUEST_BT = 1;
    private List<String> knownDevices = new ArrayList<>();
    private BluetoothAdapter Adapter;
    public String someText;
    private UUID MY_UUID;
    private Activity mainActivity;

    BluetoothHelper(Activity activity){

        Adapter = BluetoothAdapter.getDefaultAdapter();
        mainActivity = activity;
        someText = "";
        if (Adapter == null){
            System.out.print("Device does not support Bluetooth!");
        }

        if (!Adapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivity.startActivityForResult(enableBT, ACTION_REQUEST_BT);
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(BT_Receiver, filter);
        IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mainActivity.registerReceiver(discoveryReceiver, discoveryFilter);

        Set<BluetoothDevice> Devices = Adapter.getBondedDevices();
        if (Devices.size() > 0){
            for (BluetoothDevice device : Devices){
                knownDevices.add( device.getName() );
            }
        }


        Adapter.startDiscovery();
    }

    private BroadcastReceiver BT_Receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                someText = someText + device.getName() + " : " + device.getAddress();
                knownDevices.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            // When discovery finds a device
            mainActivity.unregisterReceiver(BT_Receiver);
            Adapter.cancelDiscovery();
        }
    };

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {System.out.print(e + "Did not create RF Socket"); }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            Adapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                System.out.print("Could not connect to device. . .");
                try {
                    mmSocket.close();
                } catch (IOException closeException) {System.out.print(closeException + ": Socket did not close . . ."); }
            }

            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { System.out.print(e + ": Socket did not close . . .");}
        }
    }

}
