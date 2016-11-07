package com.almootassem.android.lab7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShowGrades extends AppCompatActivity {

    ListView list;
    Button add, delete;
    List<Grade> grades;
    ArrayList<String> gradesFormatted;
    GradesDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_grades);

        list = (ListView) findViewById(R.id.grades_listview);
        add = (Button) findViewById(R.id.add_grade_button);
        delete = (Button) findViewById(R.id.delete_grade_button);

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
        list.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowGrades.this, AddGrade.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowGrades.this, DeleteGrade.class));
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
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
        list.setAdapter(adapter);
    }
}
