package com.almootassem.android.lab7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DeleteGrade extends AppCompatActivity {

    Spinner spinner;
    Button delete;
    List<Grade> grades;
    ArrayList<String> gradesFormatted;

    GradesDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_grade);

        spinner = (Spinner) findViewById(R.id.grades_spinner);
        delete = (Button) findViewById(R.id.delete_grade);

        db = new GradesDBHelper(this);

        grades = db.getAllGrades();
        gradesFormatted = new ArrayList<>();
        for (int i = 0; i < grades.size(); i++){
            String row = "";
            row+=grades.get(i).getStudentId() + "\n";
            row+=grades.get(i).getCourseComponent() + "\n";
            row+=grades.get(i).getMark() + "\n";
            gradesFormatted.add(row);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gradesFormatted);
        spinner.setAdapter(adapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("DELETE", spinner.getSelectedItem().toString());
                String[] grade = spinner.getSelectedItem().toString().split("\n");
                db.deleteGrade(new Grade(Integer.valueOf(grade[0]), grade[1], Float.valueOf(grade[2])));
                finish();
            }
        });


    }
}
