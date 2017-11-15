package com.example.jlazaro.bulb;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    BluetoothConnectionService mBluetoothConnecttion;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    ImageButton btnBackgroundColor;
    ImageButton btnLightColor;
    ImageButton btnBrightnessUp;
    ImageButton btnBrightnessDown;
    ImageButton btnAlarm;
    ImageButton btnMusic;
    ImageButton btnModes;
    Button btnBluetooth;
    ImageButton btnOn;
    ImageButton btnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started.");
        
//        Color Picker Code, couldn't figure you Problem With Layout
//        mLayout = (ConstraintLayout) findViewById.(R.id.Layout);
//        mDefaultColor = ContextCompat.getColor(MainActivity.this,R.color.colorPrimary);

        btnBackgroundColor = (ImageButton) findViewById(R.id.btnBackgroundColor);
        btnLightColor = (ImageButton) findViewById(R.id.btnLightColor);
        btnBrightnessUp = (ImageButton) findViewById(R.id.btnBrightnessUp);
        btnBrightnessDown = (ImageButton) findViewById(R.id.btnBrightnessDown);
        btnAlarm = (ImageButton) findViewById(R.id.btnAlarm);
        btnMusic = (ImageButton) findViewById(R.id.btnMusic);
        btnModes = (ImageButton) findViewById(R.id.btnMode);
        btnBluetooth = (Button) findViewById(R.id.btnBluetooth);
        btnOn = (ImageButton) findViewById(R.id.btnOn);
        btnOff = (ImageButton) findViewById(R.id.btnOff);

//        Intent incomingIntent = getIntent();
//        Log.d(TAG, "onCreate: " + incomingIntent.toString());
//        if(incomingIntent.getExtras().getParcelable("connection")){
//            BluetoothDevice incomingDevice = incomingIntent.getExtras().getParcelable("connection");
//            mBluetoothConnecttion.startClient(incomingDevice, MY_UUID_INSECURE);
//        }

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ON Button Clicked");
//                byte[] bytes = "ON".getBytes(Charset.defaultCharset());
//                mBluetoothConnecttion.write(bytes);
                makeToastMessage("Lights: ON");
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: OFF Button Clicked");
//                byte[] bytes = "OFF".getBytes(Charset.defaultCharset());
//                mBluetoothConnecttion.write(bytes);
                makeToastMessage("Lights: OFF");
            }
        });

        btnBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnBackgroundColor Clicked");
                //openColorPicker();
                laterVersion();
            }
        });
        btnLightColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnLightColor Clicked");
                laterVersion();
            }
        });
        btnBrightnessUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnBrightnessUp Clicked");
                laterVersion();
            }
        });
        btnBrightnessDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnBrightnessDown Clicked");
                laterVersion();
            }
        });
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnAlarm Clicked");
                laterVersion();
            }
        });
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnMusicClicked");
                laterVersion();
            }
        });
        btnModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnModes Clicked");
                laterVersion();
            }
        });

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnBluetooth Clicked");

                Intent intent = new Intent(MainActivity.this, BluetoothMenuActivity.class);
                startActivity(intent);
            }
        });

    }


    //Color Picker - Couldn't figure it out
//    public void openColorPicker(){
//        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
//            @Override
//            public void onCancel(AmbilWarnaDialog dialog) {
//
//            }
//
//            @Override
//            public void onOk(AmbilWarnaDialog dialog, int color) {
//                mDefaultColor = color;
//                mLayout.setBackgroundColor(mDefaultColor);
//            }
//        });
//    }

    private void laterVersion(){
        makeToastMessage("Will Be Added in Future Version");
    }

    private void makeToastMessage(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
