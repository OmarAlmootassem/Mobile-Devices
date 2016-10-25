package com.almootassem.android.lab6;

/**
 * Created by 100520286 on 10/25/2016.
 */

public class Contact {
    private int id;
    private String fname;
    private String lname;
    private String number;

    public Contact (int id, String fname, String lname, String number){
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.number = number;
    }

    public int getId(){
        return id;
    }

    public String getFname(){
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getNumber() {
        return number;
    }
}
