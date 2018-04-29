package com.tmj.timberwolf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(200);

        ImageView imageView = findViewById(R.id.imageView);
        Bitmap mBitMap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.image_1_6).copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(mBitMap);
        Canvas mCanvas = new Canvas(mBitMap);
        mCanvas.drawPoint(5 ,5,mPaint);
        imageView.invalidate();
    }
}
