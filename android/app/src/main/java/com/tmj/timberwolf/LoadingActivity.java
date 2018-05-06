package com.tmj.timberwolf;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;

public class LoadingActivity extends AppCompatActivity {
    final String number = "6178588563";
    private SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Random rand = new Random();
        final int randNum = rand.nextInt(1000);

        smsBroadcastReceiver = new SmsBroadcastReceiver(number, Integer.toString(randNum));
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        smsBroadcastReceiver.setListener(new SmsBroadcastReceiver.Listener() {
            @Override
            public void onTextReceived(String text) {
                Log.i("text message: ", text);
                text = text.substring(38);
                Log.i("new text message: ", text);
                String delims = "\\|";
                String[] tokens = text.split(delims);
                Log.i("tocken east", tokens[1]);
                Intent intent = new Intent(LoadingActivity.this, SelectionActivity.class);
                Bundle b = new Bundle();
                b.putDouble("latN", Double.parseDouble(tokens[2]));
                b.putDouble("latS", Double.parseDouble(tokens[3]));
                b.putDouble("latE", Double.parseDouble(tokens[1]));
                b.putDouble("latW", Double.parseDouble(tokens[4]));
                intent.putExtras(b);

                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        final String destination = intent.getStringExtra("destination");
        if (source.equals("Your location")) {
            SmartLocation.with(getApplicationContext()).
                    location(new LocationManagerProvider()).oneFix().
                    config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    Log.i("Tag","LocationUpdate : "+location.getLatitude()+","+location.getLongitude());
                    sendSMS("6178588563", randNum + "|" + location.getLatitude()+","+location.getLongitude()+"|"+destination+"|1");
                    sendSMS("6178588563", randNum + "|" + location.getLatitude()+","+location.getLongitude()+"|"+destination+"|2");
                }
            });
        } else {
            sendSMS("6178588563",randNum + "|" + source + "|" + destination + "|1");
            sendSMS("6178588563",randNum + "|" + source + "|" + destination + "|2");
        }

    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        unregisterReceiver(smsBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    
}
