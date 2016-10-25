package com.almootassem.android.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ShowContacts extends AppCompatActivity {

    Button addContact, deleteContact;
    ListView contactsList;
    ArrayList<String> contacts, toShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);

        addContact = (Button) findViewById(R.id.add_contact_button);
        deleteContact = (Button) findViewById(R.id.delete_contact_button);
        contactsList = (ListView) findViewById(R.id.contacts_listview);
        contacts = new ArrayList<>();
        toShow = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt"));
            contacts.clear();
            toShow.clear();
            while(sc.hasNext()){
                String line = sc.nextLine();
//                Log.d("FILE", sc.nextLine());
                contacts.add(line);
                toShow.add(line.replaceFirst("<<>>", " ").replace("<<>>", "\n"));
            }
        } catch (Exception e){}
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toShow);
        contactsList.setAdapter(adapter);


        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ShowContacts.this, AddContact.class), 1);
            }
        });

        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ShowContacts.this, DeleteContact.class), 2);
            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e("STOP", "on");
        try{
            new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt").delete();
            FileOutputStream fos = new FileOutputStream(new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt"), true);
            for (String contact: contacts){
                fos.write(contact.getBytes());
                Log.d("STOP", contact);
                fos.write("\n".getBytes());
            }
            fos.close();
        }catch (Exception e){}
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.e("START", "on");
        try{
            Scanner sc = new Scanner(new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt"));
            contacts.clear();
            toShow.clear();
            while(sc.hasNext()){
                String line = sc.nextLine();
                //Log.d("FILE", sc.nextLine());
                contacts.add(line);
                toShow.add(line.replaceFirst("<<>>", " ").replace("<<>>", "\n"));
            }
        }catch (Exception e){}
        Log.d("Array", contacts.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toShow);
        contactsList.setAdapter(adapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            //saves contact in file
            Log.d("New Contact", data.getStringExtra("CONTACT"));
            contacts.add(data.getStringExtra("CONTACT"));
            toShow.add(data.getStringExtra("CONTACT").replaceFirst("<<>>", " ").replace("<<>>", "\n"));
//            Log.d("File", new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt").getAbsolutePath());
        } else if (requestCode == 2){
            Log.d("DELETE", "CONTACT: " + data.getIntExtra("CONTACT", 99));
            contacts.remove(data.getIntExtra("CONTACT", 99));
        }
        try{
            new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt").delete();
            FileOutputStream fos = new FileOutputStream(new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/contacts.txt"), true);
            for (String contact: contacts){
                fos.write(contact.getBytes());
                Log.d("After", contact);
                fos.write("\n".getBytes());
            }
            fos.close();
        }catch (Exception e){}
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toShow);
        contactsList.setAdapter(adapter);
    }
}
