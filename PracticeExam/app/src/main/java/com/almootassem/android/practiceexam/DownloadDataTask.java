package com.almootassem.android.practiceexam;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

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
 * Created by 100520286 on 12/10/2016.
 */

public class DownloadDataTask extends AsyncTask<String, Void, List<Bike>> {
    private DataDownloadListener listener = null;
    private Exception exception = null;
    private BikeDBHelper db = null;
    Context context;

    public DownloadDataTask (Context context, DataDownloadListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<Bike> doInBackground(String... params){
        URLConnection urlConnection = null;
        List<Bike> bikesList = new ArrayList<>();

        try{
            URL url = new URL(params[0]);
            urlConnection = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(urlConnection.getInputStream());

            db = new BikeDBHelper(context);

            NodeList nodes = document.getElementsByTagName("station");
            db.dropTable();
            for (int i = 0; i < 10; i++){
                Element element = (Element) nodes.item(i);

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(element.getElementsByTagName("lat").item(0).getTextContent()), Double.parseDouble(element.getElementsByTagName("long").item(0).getTextContent()), 1);
                Bike bike = new Bike(Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent()),
                        Double.parseDouble(element.getElementsByTagName("lat").item(0).getTextContent()),
                        Double.parseDouble(element.getElementsByTagName("long").item(0).getTextContent()),
                        element.getElementsByTagName("name").item(0).getTextContent(),
                        Integer.parseInt(element.getElementsByTagName("nbBikes").item(0).getTextContent()),
                        addresses.get(0).getAddressLine(0));
                bikesList.add(bike);
                db.addItem(bike);
            }
            return bikesList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Bike> list){
        listener.dataDownloaded(db.getAllItems());
    }
}
