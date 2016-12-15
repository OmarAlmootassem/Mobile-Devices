package ca.uoit.csci4100.assessments.finalexamstarter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * TODO: Complete the rest of this class.
 */

public class MainActivity extends AppCompatActivity implements LocationListener, StoreLocationsReadyListener {

    private static final String TAG = "MainActivity";

    LocationManager manager;
    private boolean loadedStoreLocations = false;

    private List<Store> storeLocations = null;

    private PreferredStoreLocationDBHelper dbHelper = null;

    private int preferredStore = -1;

    TextView topText;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topText = (TextView) findViewById(R.id.lblMessage);
        list = (ListView) findViewById(R.id.lstContacts);
        dbHelper = new PreferredStoreLocationDBHelper(MainActivity.this);

        // load the preferred store (if any) from the database, using PreferredStoreLocationDBHelper
        loadPreferredStore();
        // start listening for geolocation data
        setupGeolocation();
    }

    /**
     * setupGeolocation
     *
     * This method will enable geolocation, and register this activity as a
     * listener of location updates.
     **/
    private void setupGeolocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10, this);
                }
                break;
        }
    }

    /**
     * loadPreferredStore
     *
     * This method will instantiate the PreferredStoreLocationDBHelper, and use
     * it to load the preferred store saved by the user (if any) on a previous
     * execution of this application (-1 if none was saved previously).
     **/
    private void loadPreferredStore() {
        preferredStore = dbHelper.getPreferredStore();
        Log.v(TAG, "Preferred Store: " + preferredStore);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "changed");
       // TODO: Implement this method
        new DownloadStoreLocationsTask(MainActivity.this, this, location.getLatitude(), location.getLongitude(), preferredStore).execute("http://csundergrad.science.uoit.ca/csci1040u/csci4100u_data/store_locations.xml");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
       // this can be left empty
    }

    @Override
    public void onProviderEnabled(String provider) {
      // this can be left empty
    }

    @Override
    public void onProviderDisabled(String provider) {
      // this can be left empty
    }

    @Override
    public void storeLocationsReady(final List<Store> storeLocations) {
        Log.v(TAG, "Callback: " + storeLocations.size());
        topText.setText(R.string.select_preferred_store);
        StoreArrayAdapter adapter = new StoreArrayAdapter(MainActivity.this, storeLocations);
        list.setAdapter(adapter);

        if (preferredStore != -1){
            for (int i = 0; i < storeLocations.size(); i++){
                if (storeLocations.get(i).getId() == preferredStore){
                    Log.v(TAG, "Store ID: " + storeLocations.get(i).getId() + " " + i);
                    list.setItemChecked(i, true);
                    Toast.makeText(MainActivity.this, "Preferred Store is " + storeLocations.get(i).getName() + " in position " + (i+1), Toast.LENGTH_LONG).show();
                }
            }
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.updatePreferredStore(storeLocations.get(position));
                topText.setText(R.string.preferred_store_saved);
            }
        });
    }
}
