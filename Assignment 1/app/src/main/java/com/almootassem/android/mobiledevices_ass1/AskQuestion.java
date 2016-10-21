package com.almootassem.android.mobiledevices_ass1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AskQuestion extends AppCompatActivity {

    TextView questionTXT;   //textView containing the questions
    Button yesBTN, noBTN;   // yes and no button
    int num = 0;    //question number
    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        //set title
        setTitle(getString(R.string.activity_ask_question));

        //initialize
        questionTXT = (TextView) findViewById(R.id.txt_question);
        yesBTN = (Button) findViewById(R.id.btn_yes);
        noBTN = (Button) findViewById(R.id.btn_no);

        // Used to retrieve the question number from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            num = extras.getInt("QUESTION_NUM");
        }
        //gets the question using the array in strings.xml
        question = getResources().getStringArray(R.array.questions)[num];
        questionTXT.setText(question);


        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this activity was started using startActivityForResult, which means it need to
                // return some sort of result. 1 is a 'YES' answer
                setResult(1);
                finish();   //end activity
            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this activity was started using startActivityForResult, which means it need to
                // return some sort of result. 0 is a 'NO' answer
                setResult(0);
                finish();   //end activity
            }
        });
    }
}
