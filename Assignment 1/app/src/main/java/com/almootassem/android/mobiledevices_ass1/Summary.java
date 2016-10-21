package com.almootassem.android.mobiledevices_ass1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Summary extends AppCompatActivity {

    TextView yes, no;   //textviews that contain the number of answers
    ArrayList<Integer> answers; //used to store the arrayList passed from the MainMenu activity
    int countYes = 0, countNo = 0;  //counters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //sets title
        setTitle(getString(R.string.activity_summary));

        yes = (TextView) findViewById(R.id.TXT_yesAnswers);
        no = (TextView) findViewById(R.id.TXT_noAnswers);

        // Used to retrieve the answers arrayList from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            answers = extras.getIntegerArrayList("ANSWERS");
        }

        // iterate through the arrayList and sort the anwers
        for (int answer : answers){
            if (answer == 1){
                countYes++;
            } else {
                countNo++;
            }
        }

        yes.setText(String.valueOf(countYes));
        no.setText(String.valueOf(countNo));
    }
}
