package com.almootassem.android.midterm;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
TODO:

1. Set the type parameters for this class.  This AsyncTask will not use progress.
   Its input will be a URL string, and its result will be a list of HousingProject
   objects.  The important fields in the CSV file are as follows:
   a. latitude (column 0, header says 'X')
   b. longitude (column 1, header says 'Y')
   c. address (column 5, header says 'PROJ_ADDRESS')
   d. municipality (column 6, header says 'MUNICIPALITY')
   e. numUnits (column 9, header says 'NUM_UNITS')
2. Implement the doInBackground() method to download and process the CSV data
   into a list of HousingProject objects.
3. Implement the onPostExecute() method to handle any exceptions and pass the
   list of HousingProjects back to the listener.
*/
public class DownloadHousingTask extends AsyncTask<String, Void, String> {
    private Exception exception = null;
    private HousingDownloadListener listener = null;

    private static final String TAG = "DownloadHousingTask";

    public DownloadHousingTask(HousingDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        //Set up URL connection
        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            //Get URL passed
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();

            //If success, continue and read the URL content
            if(code==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = bufferedReader.readLine()) != null)
                        result += "\n" + line;
                }
                in.close();
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //Log.d(TAG, result);
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String[] resultArray = result.split("\n");
        ArrayList<HousingProject> housingList = new ArrayList<>();
        //Create a HousingProject arrayList and build the housingProjects
        for (int i = 1; i < resultArray.length; i++){
            if (!resultArray[i].split(",")[0].equals("X")){
                HousingProject proj = new HousingProject(Float.parseFloat(resultArray[i].split(",")[0]), Float.parseFloat(resultArray[i].split(",")[1]), resultArray[i].split(",")[5], resultArray[i].split(",")[6], Integer.parseInt(resultArray[i].split(",")[9]));
                housingList.add(proj);
            }
        }
        Log.d(TAG, housingList.get(0).getAddress());
        listener.housingDataDownloaded(housingList);
    }

	/**
	 * loadCSVLines()
	 *	
	 * @arg inStream The input stream from which to read the CSV data
	 *
	 * @return A list of strings, each of which will be one line of CSV data
	 *
	 * This function is included to help you process the CSV file.  This function
	 * downloads all of the data from the provided InputStream, and returns a list of
	 * lines.  Since we are downloading a CSV file, these lines will consist of 
	 * comma-separated data (like the example given in Listing 1).
	**/
    private List<String> loadCSVLines(InputStream inStream) throws IOException {
        List<String> lines = new ArrayList<>();

        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

        String line = null;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

	// TODO:  Implement the doInBackGround() method
	//        This method will download the CSV data from the URL
	//        parameter (params[0]), extract the relevant data from
	//        the file, creating a list of HousingProject objects.
	//        The HousingProject list will be the result.

    // TODO:  Implement the onPostExecute() method
    //        This method will handle exceptions, and pass the result data
    //        back to the listener
}
