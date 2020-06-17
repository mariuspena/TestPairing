package com.flycatcher.testpairing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int SELECT_DEVICE_REQUEST_CODE = 123;

    private CompanionDeviceManager deviceManager;
    private BluetoothDeviceFilter deviceFilter;
    private AssociationRequest pairingRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            test();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void test() {
        // ...
        deviceManager = getSystemService(CompanionDeviceManager.class);

        // To skip filtering based on name and supported feature flags (UUIDs),
        // don't include calls to setNamePattern() and addServiceUuid(),
        // respectively. This example uses Bluetooth.

        UUID uuid = new UUID(0x123abcL, -1L);
        ParcelUuid parcelUuid = new ParcelUuid(uuid);

        deviceFilter = new BluetoothDeviceFilter.Builder()
                //.setNamePattern(Pattern.compile("smART_s"))
                //.addServiceUuid(parcelUuid, null)
//                .addServiceUuid(ParcelUuid.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"), null)
//                .addServiceUuid(ParcelUuid.fromString("0000fff0-0000-1000-8000-00805f9b34fb"), null)
//                .addServiceUuid(ParcelUuid.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"), null)
                .build();

        // The argument provided in setSingleDevice() determines whether a single
        // device name or a list of device names is presented to the user as
        // pairing options.
        pairingRequest = new AssociationRequest.Builder()
                .addDeviceFilter(deviceFilter)
                .setSingleDevice(true)
                .build();

        // When the app tries to pair with the Bluetooth device, show the
        // appropriate pairing request dialog to the user.
        deviceManager.associate(pairingRequest,
                new CompanionDeviceManager.Callback() {
                    @Override
                    public void onDeviceFound(IntentSender chooserLauncher) {
                        try {
                            startIntentSenderForResult(chooserLauncher,
                                    SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(CharSequence error) {
                        Log.d(TAG, error.toString());
                    }
                },
                new AsyncQueryHandler(getContentResolver()) {

                    @Override
                    protected Handler createHandler(Looper looper) {
                        Log.d(TAG,"createHandler");
                        return super.createHandler(looper);
                    }

                    @Override
                    public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
                        Log.d(TAG,"startQuery");
                        super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
                    }

                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        Log.d(TAG,"onInsertComplete");
                        super.onInsertComplete(token, cookie, uri);
                    }

                    @Override
                    protected void onDeleteComplete(int token, Object cookie, int result) {
                        Log.d(TAG,"onDeleteComplete");
                        super.onDeleteComplete(token, cookie, result);
                    }

                    @Override
                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                        Log.d(TAG,"onQueryComplete");
                        super.onQueryComplete(token, cookie, cursor);
                    }

                    @Override
                    protected void onUpdateComplete(int token, Object cookie, int result) {
                        Log.d(TAG,"onUpdateComplete");
                        super.onUpdateComplete(token, cookie, result);
                    }

                    @Override
                    public void handleMessage(Message msg) {
                        Log.d(TAG,"handleMessage");
                        super.handleMessage(msg);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult");
        if (requestCode == SELECT_DEVICE_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {
            // User has chosen to pair with the Bluetooth device.
            BluetoothDevice deviceToPair =
                    data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);

            String name = deviceToPair.getName();
            int i = 0;
            i++;
            Log.d(TAG, i+"");

            //deviceToPair.createBond();

            // ... Continue interacting with the paired device.
        }
    }




}