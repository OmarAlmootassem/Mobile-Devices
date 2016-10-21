package com.almootassem.android.mobiledevices_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private final static String TAG = "MainMenu";

    Button startQuizBTN;    //survey start button
    ArrayList<Integer> answers; //survey answers are stored here
    int num = 0;    //counter to keep track of the questions done

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //change activity title
        setTitle(getString(R.string.activity_main_menu));

        answers = new ArrayList<>();
        startQuizBTN = (Button) findViewById(R.id.btn_startQuiz);
        startQuizBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to AskQuestion activity and pass the current question number
                Intent intent = new Intent(MainMenu.this, AskQuestion.class);
                intent.putExtra("QUESTION_NUM", num);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //Checks if the questions are all answered. If they are, it goes to the Summary activity and
        // passes the answers ArrayList. If not all questions are answered, it changes the text of
        // the button from start survey to continue survey
        if (num >= getResources().getStringArray(R.array.questions).length){
            Intent intent = new Intent(MainMenu.this, Summary.class);
            intent.putIntegerArrayListExtra("ANSWERS", answers);
            startActivity(intent);
            finish();
        } else if (num > 0) {
            startQuizBTN.setText(getString(R.string.main_button_continue));
        }
    }

    /**
     * Used to retrieve the response from the AskQuestion activity
     * @param requestCode - the code that is chosen when AskQuestion activity is started
     * @param resultCode - the code that is returned by AskQuestion activity
     * @param data - data from the return intent (unused)
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            //adds response to the answer arrayList
            answers.add(resultCode);
            num++;
        }
    }
}
