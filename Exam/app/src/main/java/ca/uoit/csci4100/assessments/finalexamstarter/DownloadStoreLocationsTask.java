package ca.uoit.csci4100.assessments.finalexamstarter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * TODO: Complete the rest of this class.
 */

public class DownloadStoreLocationsTask extends AsyncTask<String, Void, List<Store>> {
    private Context context = null;
    private StoreLocationsReadyListener listener = null;

    private double userLatitude;
    private double userLongitude;

    private int preferredStoreId;

    private Exception exception = null;

    public DownloadStoreLocationsTask(Context context, StoreLocationsReadyListener listener, double userLatitude, double userLongitude, int preferredStoreId) {
        this.context = context;
        this.listener = listener;

        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;

        this.preferredStoreId = preferredStoreId;
    }

    /**
     * calculateDistance
     *
     * This method determines the distance between two sets of latitude/longitude
     * coordinates.
     *
     * @param lat1 The first latitude
     * @param lng1 The first longitude
     * @param lat2 The second latitude
     * @param lng2 The second longitude
     *
     * @return The distance between lat1,lng1 and lat2,lng2, in kilometres
     **/
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double val =  Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(lng1 - lng2));
        return Math.toDegrees(Math.acos(val)) * 60 * 1.1515 * 1.609344;
    }

    /**
     * geocode
     *
     * This method performs a forward geocode from a street address of a store
     * location, to a set of latitude/longitude coordinates
     *
     * @param address The street address for which we want to coordinates
     *
     * @return The geolocation (Address, which has latitude and longitude)
     **/
    private Address geocode(String address) {
        // TODO: Implement this method
        //Done in doInBackground

        return null;
    }

    /**
     * doInBackground
     *
     * This method loads XML data from the URL passed as a string argument,
     * extracting the data into a list of stores.  The store data is extracted.
     * For the distance of the Store instance, you will use the method provided
     * above, called calculateDistance() and the mobile device's current
     * position.  To get the latitude/longitude for the Store instance, use
     * the geocode() function.
     *
     * @param params An array that contains the (string) URL use for downloading
     *
     * @return The list of stores
     **/
    protected List<Store> doInBackground(String... params) {
        URLConnection urlConnection = null;
        List<Store> list = new ArrayList<>();

        try{
            Log.v("Async", "Started");
            URL url = new URL(params[0]);
            urlConnection = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(urlConnection.getInputStream());

            NodeList nodes = document.getElementsByTagName("store");
            Log.v("Async", "Length: " + nodes.getLength());

            for (int i = 0; i < nodes.getLength(); i++){
                Element element = (Element) nodes.item(i);
                NodeList node = element.getElementsByTagName("address");
                //Log.v("Async", element.getAttribute("name"));
                //Log.v("Async", ((Element) node.item(0)).getAttribute("street"));

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> address = geocoder.getFromLocationName(((Element) node.item(0)).getAttribute("street") + " " + ((Element) node.item(0)).getAttribute("city") + " " + ((Element) node.item(0)).getAttribute("postalCode"), 1);
                double lat, longitude;
                Store store = new Store(element.getAttribute("name"), ((Element) node.item(0)).getAttribute("street"), ((Element) node.item(0)).getAttribute("city"), ((Element) node.item(0)).getAttribute("postalCode"));
                store.setId(Integer.parseInt(element.getAttribute("id")));
                try{
                    lat = address.get(0).getLatitude();
                    longitude = address.get(0).getLongitude();
                    store.setDistance(calculateDistance(userLatitude, userLongitude, lat, longitude));
                    //Log.v("Async", "Lat: " + lat + " Long: " + longitude + " Distance: " + store.getDistance());
                } catch (Exception e ){
                    store.setDistance(0);
                }
                list.add(store);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * onPostExecute
     *
     * This method handles any exceptions that occurred during doInBackground,
     * and (if no exceptions occurred), passes the data to the activity class
     * using the StoreLocationsReadyListener interface.
     *
     * @param results The results from the doInBackground method call
     **/
     @Override
     protected void onPostExecute(List<Store> results) {
         if (this.exception != null) {
             exception.printStackTrace();
         } else {
             listener.storeLocationsReady(results);
         }
     }
}
