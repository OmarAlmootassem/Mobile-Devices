package com.almootassem.android.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DeleteContact extends AppCompatActivity {

    ArrayList<String> contacts;
    Spinner spinner;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);
        contacts = new ArrayList<>();

        spinner = (Spinner) findViewById(R.id.contacts_spinner);
        delete = (Button) findViewById(R.id.delete_contact);

        try {
            Scanner sc = new Scanner(new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt"));
            contacts.clear();
            while(sc.hasNext()){
//                Log.d("FILE", sc.nextLine());
                contacts.add(sc.nextLine().replaceFirst("<<>>", " ").replace("<<>>", "\n"));
            }
        } catch (Exception e){}
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        spinner.setAdapter(adapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1, new Intent().putExtra("CONTACT", spinner.getSelectedItemPosition()));
                finish();
            }
        });

    }
}
