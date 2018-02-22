package com.tmj.timberwolf;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {

    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        gpsTracker = new GPSTracker(LandingActivity.this);
        Button ShowLocationButton = findViewById(R.id.button);

        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                gpsTracker = new GPSTracker(LandingActivity.this);

                if (gpsTracker.canGetLocation())
                {
                    Location loc = gpsTracker.getLocation();
                    double longitude = loc.getLongitude();
                    double latitude = loc.getLatitude();

                    Toast.makeText(getApplicationContext(), "Location : \nLAT " + latitude + "\nLNG " + longitude, Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Konum bilgisi alinamiyorsa mesaj kutusunu goster
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
}
