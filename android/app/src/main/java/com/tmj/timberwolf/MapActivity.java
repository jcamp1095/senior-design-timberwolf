package com.tmj.timberwolf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;

public class MapActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            final Uri uri = data.getData();

            try {
                final Bitmap mBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri).copy(Bitmap.Config.ARGB_8888, true);
                final ImageView imageView = findViewById(R.id.imageView);
                //final Bitmap mBitMap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.image_1_6).copy(Bitmap.Config.ARGB_8888, true);
                imageView.setImageBitmap(mBitMap);
                Bundle b = getIntent().getExtras();

                final Paint mPaint = new Paint();
                mPaint.setColor(Color.BLUE);
                mPaint.setStrokeWidth(50);

                //final Canvas mCanvas = new Canvas(mBitMap);
                final float mapWidth = mBitMap.getWidth();
                final float mapHeight = mBitMap.getHeight();
                final double latN = b.getDouble("latN");
                final double latS = b.getDouble("latS");
                final double lngE = b.getDouble("latE");
                final double lngW = b.getDouble("latW");


                SmartLocation.with(getApplicationContext()).
                        location(new LocationManagerProvider()).
                        config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        try {
                            float horizontalDist = (float)distance(latN, latN, lngW, lngE,1, 1);
                            float horizontalDistToLine = (float)distance(latN, latN, lngW, location.getLongitude(),1, 1);
                            float horizontalPixel = horizontalDistToLine / horizontalDist * mapWidth;

                            float verticalDist = (float)distance(latN, latS, lngW, lngW,1, 1);
                            float verticalDistToLine = (float)distance(latN, location.getLatitude(), lngW, lngW,1, 1);
                            float verticalPixel = verticalDistToLine / verticalDist * mapHeight;

                            final ImageView imageView = findViewById(R.id.imageView);
                            final Bitmap mBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri).copy(Bitmap.Config.ARGB_8888, true);
                            imageView.setImageBitmap(mBitMap);
                            final Canvas mCanvas = new Canvas(mBitMap);
                            mCanvas.drawPoint(horizontalPixel, verticalPixel, mPaint);
                            imageView.invalidate();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
