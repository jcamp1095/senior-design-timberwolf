package com.tmj.timberwolf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(100);

        ImageView imageView = findViewById(R.id.imageView);
        Bitmap mBitMap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.image_1_6).copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(mBitMap);

        double start_lat = 42.407282948;
        double start_lng = -71.2984292;
        double latN = 43.5801473056;
        double latS = 39.4698655864;
        double lngE = -70.9867357813;
        double lngW = -74.2826342188;

        float horizontal_dist = (float)distance(latN, latN, lngW, lngE,1, 1);
        float horizontal_dist_to_line = (float)distance(latN, latN, lngW, start_lng,1, 1);
        float horizontal_pixel = horizontal_dist_to_line / horizontal_dist * mBitMap.getWidth();

        float vertical_dist = (float)distance(latN, latS, lngW, lngW,1, 1);
        float vertical_dist_to_line = (float)distance(latN, start_lat, lngW, lngW,1, 1);
        float vertical_pixel = vertical_dist_to_line / vertical_dist * mBitMap.getHeight();

        Log.i("hor_dist", Float.toString(horizontal_dist));
        Log.i("hor_line", Float.toString(horizontal_dist_to_line));
        Log.i("hor_pixel", Float.toString(horizontal_pixel));
        Log.i("ver_dist", Float.toString(vertical_dist));
        Log.i("ver_line", Float.toString(vertical_dist_to_line));
        Log.i("ver_pixel", Float.toString(vertical_pixel));


        Canvas mCanvas = new Canvas(mBitMap);
        mCanvas.drawPoint(horizontal_pixel ,vertical_pixel,mPaint);
        imageView.invalidate();
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
