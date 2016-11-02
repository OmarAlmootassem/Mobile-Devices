package com.almootassem.android.midterm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/*
TODO:

1. Add this activity as a selection listener to the dropdown
2. Make the activity class implement the right interface to be a selection listener
3. Implement the selection listener event to populate the text fields with the selected
   housing project
4. Make this activity class implement the HousingDownloadListener interface provided   
5. Make this activity class implement the housingDataDownloaded() method.  This method will 
   populate the spinner with housing data (using the toString() method of the HousingData 
   class).
*/
public class MainActivity extends AppCompatActivity {
    private static final String URL = "http://csundergrad.science.uoit.ca/csci3230u/data/Affordable_Housing.csv";

    final static String TAG = "MainActivity";

    //Declaring elements
    Spinner spinner;
    EditText address, municipality, units, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.lstHousingProjects);

        address = (EditText) findViewById(R.id.address_edittext);
        municipality = (EditText) findViewById(R.id.municipality_edittext);
        units = (EditText) findViewById(R.id.units_edittext);
        latitude = (EditText) findViewById(R.id.latitude_edittext);
        longitude = (EditText) findViewById(R.id.longitude_edittext);

		// Set up HousingDownloadListener
        HousingDownloadListener housingDownloadListener = new HousingDownloadListener() {
            @Override
            public void housingDataDownloaded(final List<HousingProject> housingProjects) {
                Log.d(TAG, "GOT IT");
                ArrayList<String> spinnerData = new ArrayList<>();
                //Build the arraylist to display the information in the spinner
                for (int i = 0; i < housingProjects.size(); i++){
                    spinnerData.add(housingProjects.get(i).getNumUnits() + " units at " + housingProjects.get(i).getAddress() + " (" + housingProjects.get(i).getLatitude() + ", " + housingProjects.get(i).getLongitude() + ")");
//                    Log.v(TAG, spinnerData.get(i));
                }
//                Log.d(TAG, "" + spinnerData);

                //Create an array adapter and assign it to the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, spinnerData);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    /**
                     * Once an item is selected, the textfields will populate
                     * @param adapterView
                     * @param view
                     * @param i
                     * @param l
                     */
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        address.setText(housingProjects.get(i).getAddress());
                        municipality.setText(housingProjects.get(i).getMunicipality());
                        units.setText(String.valueOf(housingProjects.get(i).getNumUnits()));
                        latitude.setText(String.valueOf(housingProjects.get(i).getLatitude()));
                        longitude.setText(String.valueOf(housingProjects.get(i).getLongitude()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        };
        

        // TODO:  Create an AsyncTask instance, and start it
        new DownloadHousingTask(housingDownloadListener).execute(URL);
        
    }

    // TODO:  Implement the handler method for the HousingDownloadListener interface
    //         - Populate the spinner with the downloaded data
    //        Hint:  Use an ArrayAdapter for this purpose


	// TODO:  Implement the item selection method to put the data from the selected
	//        housing project into the text fields of our UI
	
}
