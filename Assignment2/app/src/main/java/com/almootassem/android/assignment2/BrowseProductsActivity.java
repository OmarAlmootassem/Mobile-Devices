package com.almootassem.android.assignment2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * This is the main activity of the app. It shows all the products that are
 * in the local database. It allows deleting products and has a button that
 * goes to AddProductActivity to add a product.
 */
public class BrowseProductsActivity extends AppCompatActivity implements  OnTaskCompleted {

    private static final String TAG = "BrowseProductActivity";

    //Instance of database helper class
    ProductDBHelper db;

    //Layout Elements
    EditText name, description, cadPrice, bitcoinPrice;
    Button previous, delete, next;
    //Information about products
    int currentProduct = 0;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_products);

        //Initializing the layout elements
        name = (EditText) findViewById(R.id.name_edittext);
        description = (EditText) findViewById(R.id.desc_edittext);
        cadPrice = (EditText) findViewById(R.id.cad_price_edittext);
        bitcoinPrice = (EditText) findViewById(R.id.bitcoin_price_edittext);

        previous = (Button) findViewById(R.id.previous_product);
        delete = (Button) findViewById(R.id.delete_product);
        next = (Button) findViewById(R.id.next_product);

        //Initializing the DB Helper
        db = new ProductDBHelper(this);

        //Saves all products in an arrayList
        products = db.getAllProducts();

        currentProduct = 0;
        //Checks how many products there are and gets the Bitcoin price and then updates
        //the fields
        if (products.size() > 0)
            showProduct(products.get(currentProduct));
        else
            Toast.makeText(this, R.string.no_products, Toast.LENGTH_LONG).show();

        //Enables/Disables buttons accordingly
        buttonStatus();

        //OnClickListener for previous button; it goes to previous product
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct--;
                showProduct(products.get(currentProduct));
                buttonStatus();
            }
        });

        //OnClickListener for next button; it goes to next product
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct++;
                showProduct(products.get(currentProduct));
                buttonStatus();
            }
        });

        //OnClickListener for delete button; it deletes the product then updates
        //the products list
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteProduct(products.get(currentProduct));
                Toast.makeText(getApplicationContext(), products.get(currentProduct).getName() + getString(R.string.deleted), Toast.LENGTH_LONG).show();
                products = db.getAllProducts();
                currentProduct = 0;
                if (products.size() > 0)
                    showProduct(products.get(currentProduct));
            }
        });
    }

    @Override
    public void taskCompleted(String result){
        //Set the EditText values
        bitcoinPrice.setText(result);
    }

    /**
     * showProduct - takes care of displaying the information in the edittexts
     * @param product
     */
    public void showProduct(Product product){
        new ConvertToBitCoin(this).execute(product.getPrice());
        name.setText(product.getName());
        description.setText(product.getDescription());
        String cad = getString(R.string.dollar) + String.valueOf(product.getPrice());
        cadPrice.setText(cad);
        buttonStatus();
    }

    @Override
    public void onResume(){
        super.onResume();
        products = db.getAllProducts();

        currentProduct = 0;
        if (products.size() > 0)
            showProduct(products.get(currentProduct));

        buttonStatus();
    }

    /**
     * Updates the buttons depending on how many products are there in the database
     * and which product is currently visible
     */
    private void buttonStatus(){
        Log.v(TAG, "Current: " + currentProduct + ", Size: " + (products.size() - 1));
        if (currentProduct == 0){
            previous.setEnabled(false);
            next.setEnabled(true);
        }
        if (currentProduct == products.size() - 1){
            next.setEnabled(false);
            previous.setEnabled(true);
        }
        if (currentProduct > 0 && currentProduct < products.size() - 1){
            previous.setEnabled(true);
            next.setEnabled(true);
        }
        if (products.size() == 0){
            next.setEnabled(false);
            previous.setEnabled(false);
            delete.setEnabled(false);
        }
        if (products.size() == 1){
            next.setEnabled(false);
            previous.setEnabled(false);
            delete.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_product:
                startActivity(new Intent(BrowseProductsActivity.this, AddProductActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Asynctask to retrieve the Bitcoin price from a CAD price
     */
    class ConvertToBitCoin extends AsyncTask<Float, Void, String> {
        private OnTaskCompleted listener = null;

        ConvertToBitCoin(OnTaskCompleted listener){
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Float... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                //Initilize connection to url and pass the CAD value
                URL url = new URL("https://blockchain.info/tobtc?currency=CAD&value=" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();

                //If success
                if(code==200){
                    //Go through the result and save it in result variable
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line;

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    //close stream
                    in.close();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                //Disconnect connection
                urlConnection.disconnect();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            listener.taskCompleted(result);
        }
    }
}
