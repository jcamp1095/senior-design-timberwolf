package com.tmj.timberwolf;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;

public class LoadingActivity extends AppCompatActivity {

    private SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        smsBroadcastReceiver = new SmsBroadcastReceiver("6178588563", "");
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        smsBroadcastReceiver.setListener(new SmsBroadcastReceiver.Listener() {
            @Override
            public void onTextReceived(String text) {
                Log.i("test", text);
            }
        });

        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        String destination = intent.getStringExtra("destination");
        if (source.equals("Your location")) {
            SmartLocation.with(getApplicationContext()).
                    location(new LocationManagerProvider()).oneFix().
                    config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    Log.i("Tag","LocationUpdate : "+location.getLatitude()+","+location.getLongitude());
                }
            });
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
