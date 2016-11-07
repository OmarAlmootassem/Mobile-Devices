package com.almootassem.android.lab7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddGrade extends AppCompatActivity {

    EditText id, component, mark;
    Button add;

    GradesDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        id = (EditText) findViewById(R.id.id_edit_text);
        component = (EditText) findViewById(R.id.comp_edit_text);
        mark = (EditText) findViewById(R.id.mark_edit_text);
        add = (Button) findViewById(R.id.add_button);

        db = new GradesDBHelper(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addGrade(new Grade(Integer.valueOf(id.getText().toString()), component.getText().toString(), Float.valueOf(mark.getText().toString())));
                finish();
            }
        });
    }
}
