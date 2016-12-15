package com.almootassem.android.practiceexam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mMapFragment;
    GoogleMap map;

    BikeDBHelper db;

    List<Bike> bikes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        db = new BikeDBHelper(this);
        bikes = db.getAllItems();

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("long", 0)), 14f));
        for (Bike bike : bikes){
            map.addMarker(new MarkerOptions().position(new LatLng(bike.getLatitude(), bike.getLongitude()))
            .title(bike.toString()));
        }
    }
}
