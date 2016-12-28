package snuze.alarmtooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;


public class DeviceSearchActivity extends Activity {

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesAdapter;
    public static String DEVICE_ADDRESS = "device_address";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);

        Button scanButton = (Button) findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<String> mPairedDevicesAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        mNewDevicesAdapter = new ArrayAdapter<>(this, R.layout.device_name);

        ListView mPairedListView = (ListView) findViewById(R.id.paired_devices);
        mPairedListView.setAdapter(mPairedDevicesAdapter);
        mPairedListView.setOnItemClickListener(mDeviceClickListener);

        ListView newDevicesListView = (ListView) findViewById(R.id.found_devices);
        newDevicesListView.setAdapter(mNewDevicesAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> mPairedDevices = mBtAdapter.getBondedDevices();

        if (mPairedDevices != null){
            for ( BluetoothDevice device : mPairedDevices){
                mPairedDevicesAdapter.add(device.getName() + ":" + device.getAddress());
            }
        }else{
            String nothing = "No Paired Devices Found";
            mPairedDevicesAdapter.add(nothing + "\n");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }



    //Method for grabbing the MAC address of Bluetooth Device from List
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3){

                mBtAdapter.cancelDiscovery();

                String info = ((TextView) v).getText().toString();
                String MAC = info.substring(info.length()-17);
                Intent intent = new Intent();
                intent.putExtra(DEVICE_ADDRESS, MAC);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
    };

    private void doDiscovery(){
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        findViewById(R.id.new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        if (mNewDevicesAdapter != null)
        mNewDevicesAdapter.clear();
        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                Button scanButton = (Button) findViewById(R.id.scan_button);
                scanButton.setVisibility(View.VISIBLE);
                if (mNewDevicesAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesAdapter.add(noDevices);
                }
            }
        }
    };
}
