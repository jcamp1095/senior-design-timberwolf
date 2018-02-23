package com.tmj.timberwolf;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {

    private GPSTracker gpsTracker;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);
        Button ShowLocationButton = findViewById(R.id.button);
        if (Build.VERSION.SDK_INT >= 23 && !canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }

        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                gpsTracker = new GPSTracker(LandingActivity.this);

                if (gpsTracker.canGetLocation())
                {
                    double longitude = gpsTracker.getLongitude();
                    double latitude = gpsTracker.getLatitude();

                    Toast.makeText(getApplicationContext(), "Location : \nLAT " + Double.toString(latitude) + "\nLNG " + Double.toString(longitude), Toast.LENGTH_LONG).show();
                }
                else
                {
                    gpsTracker.showSettingsAlert();
                }
            }
        });


//        String phoneNumber;
//        String message;
//
//        phoneNumber = "+16178588563";
//        message = "test";
//        sendSMS(phoneNumber, message);
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            //max 160 characters.  to send longer message:
            //ArrayList<String> parts = smsManager.divideMessage(message);
            //smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    private boolean hasPermission(String perm) {
        return (Build.VERSION.SDK_INT >= 23 && PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));

    }
}
