package com.tmj.timberwolf;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.nlopez.smartlocation.SmartLocation;

public class LandingActivity extends AppCompatActivity {

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_SMS
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int SMS_PERMISSION_CODE=1567;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);
        Button showLocationButton = findViewById(R.id.button);
        final EditText source = findViewById(R.id.source);
        final EditText destination = findViewById(R.id.destination);

        // TODO: Are these both necessary? Neither? So many questions...
        if (Build.VERSION.SDK_INT >= 23 && !canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
        if (!SmartLocation.with(getApplicationContext()).location().state().isGpsAvailable()) {
            showSettingsAlert(LandingActivity.this);
        }

        //if (!isSmsPermissionGranted()) {
            //requestReadAndSendSmsPermission();
        //}

        showLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent intent = new Intent(LandingActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putDouble("latN", 43.5801473056);
                b.putDouble("latS", 39.46986558640);
                b.putDouble("latE", -70.9867357813);
                b.putDouble("latW", -74.2826342188);
                intent.putExtras(b);

                startActivity(intent);

//                String s = source.getText() + "";
//                String d = destination.getText() + "";
//                if (d.equals("") || s.equals("")) {
//                    AlertDialog alertDialog = new AlertDialog.Builder(LandingActivity.this).create();
//                    alertDialog.setTitle("Error");
//                    alertDialog.setMessage("Source and destination cannot be empty");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();
//                    return;
//                }
//                Log.v("c", "cddff");
//                Intent intent = new Intent(LandingActivity.this, LoadingActivity.class);
//                intent.putExtra("source", s);
//                intent.putExtra("destination", d);
//                startActivity(intent);
            }
        });

        source.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (source.getText().toString().equals("Your location")) {
                        source.setText("");
                    }
                } else {
                    if (source.getText().toString().equals("")) {
                        source.setText("Your location");
                    }
                }
            }
        });
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    private boolean hasPermission(String perm) {
        return (Build.VERSION.SDK_INT >= 23 && PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));

    }
    public void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    // Activity

//    /**
//     * Check if we have SMS permission
//     */
//    public boolean isSmsPermissionGranted() {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    /**
//     * Request runtime SMS permission
//     */
//    private void requestReadAndSendSmsPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
//            // You may display a non-blocking explanation here, read more in the documentation:
//            // https://developer.android.com/training/permissions/requesting.html
//        }
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
//    }

}
