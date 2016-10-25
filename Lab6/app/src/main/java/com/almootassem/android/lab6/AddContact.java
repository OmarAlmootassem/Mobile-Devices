package com.almootassem.android.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddContact extends AppCompatActivity {

    EditText fname, lname, phoneNumber;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        fname = (EditText) findViewById(R.id.first_name_edit_text);
        lname = (EditText) findViewById(R.id.last_name_edit_text);
        phoneNumber = (EditText) findViewById(R.id.phone_number_edit_text);
        add = (Button) findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("CONTACT", fname.getText() + "<<>>" + lname.getText() + "<<>>" + phoneNumber.getText()));
                finish();
            }
        });
    }
}
