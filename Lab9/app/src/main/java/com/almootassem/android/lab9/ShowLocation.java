package com.almootassem.android.lab9;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ShowLocation extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private static final String TAG = "ShowLocation";

    LocationManager manager;
    MapFragment mMapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10, this);
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "Location Changed");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address1, address2, city, province, country, postalCode, phone, url;
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address1 = addresses.get(0).getAddressLine(0);
            address2 = "";
            if (addresses.get(0).getMaxAddressLineIndex() >= 2)
                address2 = addresses.get(0).getAddressLine(1);
            city = addresses.get(0).getLocality();
            province = addresses.get(0).getAdminArea();
            postalCode = addresses.get(0).getPostalCode();
            country = addresses.get(0).getCountryName();
            phone = addresses.get(0).getPhone();
            url = addresses.get(0).getUrl();
            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()))
                    .title(address1 + " " + city + " " + province + " " + country + " " + postalCode));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()), 15));
        } catch (IOException e){}
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.v(TAG, "Location Disabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.v(TAG, "Location Enabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v(TAG, "Location Status Changed");
    }
}
