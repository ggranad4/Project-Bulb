package com.example.jlazaro.bulb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by J.Lazaro on 11/14/2017.
 */

public class BluetoothMenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "BluetoothMenuActivity";

    BluetoothConnectionService mBluetoothConnection;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothAdapter mBluetoothAdapter;
    ArrayList<BluetoothDevice> mBTDevices;
    DeviceListAdapter mDeviceListAdapter;
    BluetoothDevice mBTDevice;

    ListView lvDevices;
    Button btnDiscover;
    Button btnConnect;
    Button btnBack;

    //Broadcast Reciever for listing devices that arent paired
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND");
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra((BluetoothDevice.EXTRA_DEVICE));
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases
                //case 1: already bonded
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "onReceive: BOND_BONDED");
                    mBTDevice = mDevice;
                }
                //case 2: creating bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "onReceive: BOND_BONDING");
                }
                //case 3: bond broken
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "onReceive: BOND_NONE");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mBroadcastReceiver2);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_menu_layout);

        btnDiscover = (Button) findViewById(R.id.btnDiscover);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnBack = (Button) findViewById(R.id.btnBack);
        lvDevices = (ListView) findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lvDevices.setOnItemClickListener(BluetoothMenuActivity.this);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver2, filter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BluetoothMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Looking for Devices");

                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, "onClick: Canceling Discovery");

                    checkBTPermissions();

                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
                }
                if(!mBluetoothAdapter.isDiscovering()){
                    checkBTPermissions();

                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiver, discoverDevicesIntent);

                }
            }
        });
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection();
            }
        });

    }

    public void startConnection(){
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
    }
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device, uuid);

        Intent intent = new Intent(BluetoothMenuActivity.this, MainActivity.class);
        intent.putExtra("connection", mBTDevice);

        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES. LOLLIPOP){
            int permissionsCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionsCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionsCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
            else{
                Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK < LOLLIPOP");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothAdapter.cancelDiscovery();
        Log.d(TAG, "onItemClick: You clicked a device");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName: " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress: " + deviceAddress);

        //create bond
        //NOTE: Requires API 17+?
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "onItemClick: Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection = new BluetoothConnectionService(BluetoothMenuActivity.this);
        }
    }

}
