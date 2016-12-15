package com.almootassem.android.practiceexam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataDownloadListener {

    EditText bikeIDText, nameText, addressText, latText, longText, numText;
    Spinner spinner;
    Button save, map;

    BikeDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bikeIDText = (EditText) findViewById(R.id.bikeID_edittext);
        nameText = (EditText) findViewById(R.id.name_edittext);
        addressText = (EditText) findViewById(R.id.address_edittext);
        latText = (EditText) findViewById(R.id.latitude_edittext);
        longText = (EditText) findViewById(R.id.longitude_edittext);
        numText = (EditText) findViewById(R.id.num_bikes_edittext);

        spinner = (Spinner) findViewById(R.id.spinner);

        save = (Button) findViewById(R.id.saveBTN);
        map = (Button) findViewById(R.id.mapBTN);

        db = new BikeDBHelper(this);

        new DownloadDataTask(MainActivity.this, this).execute("http://feeds.bikesharetoronto.com/stations/stations.xml");
    }

    @Override
    public void dataDownloaded(final List<Bike> bikes) {
        final List<String> spinnerData = new ArrayList<>();
        for (Bike bike : bikes){
            spinnerData.add(bike.toString());
        }
        Log.v("Main", bikes.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, spinnerData);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bikeIDText.setText(String.valueOf(bikes.get(i).getBikeShareId()));
                nameText.setText(bikes.get(i).getName());
                addressText.setText(bikes.get(i).getAddress());
                latText.setText(String.valueOf(bikes.get(i).getLatitude()));
                longText.setText(String.valueOf(bikes.get(i).getLongitude()));
                numText.setText(String.valueOf(bikes.get(i).getNumBikes()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bikes.get(spinner.getSelectedItemPosition()).setName(nameText.getText().toString());
                db.updateItem(bikes.get(spinner.getSelectedItemPosition()));
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowMapActivity.class);
                intent.putExtra("lat", bikes.get(spinner.getSelectedItemPosition()).getLatitude());
                intent.putExtra("long", bikes.get(spinner.getSelectedItemPosition()).getLongitude());
                startActivity(intent);
            }
        });
    }
}
