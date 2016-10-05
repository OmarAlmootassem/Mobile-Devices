package com.almootassem.android.lab5;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView licenseClick = (TextView) findViewById(R.id.license_click);
        licenseClick.setPaintFlags(licenseClick.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        licenseClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowLicense.class));
            }
        });

    }
}
