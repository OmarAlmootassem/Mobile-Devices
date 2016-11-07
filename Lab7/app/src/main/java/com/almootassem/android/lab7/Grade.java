package com.almootassem.android.lab7;

/**
 * Created by 100520286 on 11/6/2016.
 */

public class Grade {

    private int studentId;
    private String courseComponent;
    private Float mark;

    public Grade(int studentId, String courseComponent, Float mark){
        this.studentId = studentId;
        this.courseComponent = courseComponent;
        this.mark = mark;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setCourseComponent(String courseComponent) {
        this.courseComponent = courseComponent;
    }

    public void setMark(Float mark) {
        this.mark = mark;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getCourseComponent() {
        return courseComponent;
    }

    public Float getMark() {
        return mark;
    }
}
