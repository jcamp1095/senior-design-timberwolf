package com.tmj.timberwolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by joecampbell on 5/6/18.
 */

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection);
        Button showLocationButton = findViewById(R.id.buttontext);


        showLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Bundle b = getIntent().getExtras();

                Intent intent = new Intent(SelectionActivity.this, MapActivity.class);
                intent.putExtras(b);

                startActivity(intent);
            }
        });
    }
}
