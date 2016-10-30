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

public class BrowseProductsActivity extends AppCompatActivity {

    private static final String TAG = "BrowseProductActivity";

    ProductDBHelper db;

    EditText name, description, cadPrice, bitcoinPrice;
    Button previous, delete, next;
    int currentProduct = 0;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_products);

        name = (EditText) findViewById(R.id.name_edittext);
        description = (EditText) findViewById(R.id.desc_edittext);
        cadPrice = (EditText) findViewById(R.id.cad_price_edittext);
        bitcoinPrice = (EditText) findViewById(R.id.bitcoin_price_edittext);

        previous = (Button) findViewById(R.id.previous_product);
        delete = (Button) findViewById(R.id.delete_product);
        next = (Button) findViewById(R.id.next_product);

        db = new ProductDBHelper(this);

        products = db.getAllProducts();

        currentProduct = 0;
        if (products.size() > 0)
            new ConvertToBitCoin().execute(products.get(currentProduct).getPrice());
        else
            Toast.makeText(this, R.string.no_products, Toast.LENGTH_LONG).show();

        buttonStatus();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct--;
                new ConvertToBitCoin().execute(products.get(currentProduct).getPrice());
                buttonStatus();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct++;
                new ConvertToBitCoin().execute(products.get(currentProduct).getPrice());
                buttonStatus();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteProduct(products.get(currentProduct));
                Toast.makeText(getApplicationContext(), products.get(currentProduct).getName() + getString(R.string.deleted), Toast.LENGTH_LONG).show();
                products = db.getAllProducts();
                currentProduct = 0;
                if (products.size() > 0)
                    new ConvertToBitCoin().execute(products.get(currentProduct).getPrice());
                buttonStatus();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        products = db.getAllProducts();

        currentProduct = 0;
        if (products.size() > 0)
            new ConvertToBitCoin().execute(products.get(currentProduct).getPrice());

        buttonStatus();
    }

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

    class ConvertToBitCoin extends AsyncTask<Double, Void, String> {

        @Override
        protected String doInBackground(Double... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL("https://blockchain.info/tobtc?currency=CAD&value=" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();

                if(code==200){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line;

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                urlConnection.disconnect();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            bitcoinPrice.setText(result);
            name.setText(products.get(currentProduct).getName());
            description.setText(products.get(currentProduct).getDescription());
            cadPrice.setText(getString(R.string.dollar) + String.valueOf(products.get(currentProduct).getPrice()));
        }
    }
}
