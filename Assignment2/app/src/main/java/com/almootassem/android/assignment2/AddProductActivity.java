package com.almootassem.android.assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "AddProductActivity";

    ProductDBHelper db;

    EditText name, description, cadPrice;
    Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        name = (EditText) findViewById(R.id.new_name_edittext);
        description = (EditText) findViewById(R.id.new_desc_edittext);
        cadPrice = (EditText) findViewById(R.id.new_cad_price_edittext);

        save = (Button) findViewById(R.id.save_product);
        cancel = (Button) findViewById(R.id.cancel_product);

        db = new ProductDBHelper(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addProduct(new Product(name.getText().toString(), description.getText().toString(), Double.valueOf(cadPrice.getText().toString())));
                Toast.makeText(getApplicationContext(), R.string.product_added, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
