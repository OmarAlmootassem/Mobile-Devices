package com.almootassem.android.lab7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 100520286 on 11/6/2016.
 */

public class GradesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ClassManager";

    private static final String TABLE_GRADES = "Grades";

    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "studentId";
    private static final String KEY_COMP = "courseComponent";
    private static final String KEY_MARK = "mark";

    public GradesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method is called during creation of the database
     * @param db - the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_GRADES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COMP + " TEXT NOT NULL,"
                + KEY_MARK + " DECIMAL NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /**
     * Method is called during an upgrade of the database
     * @param db - the database
     * @param oldVersion - old version of the database
     * @param newVersion - new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        onCreate(db);
    }

    /**
     * Adds a grade to the database
     * @param grade - the product to be added
     */
    public void addGrade(Grade grade){
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();

        //set the values of the query
        ContentValues values = new ContentValues();
        values.put(KEY_COMP, grade.getCourseComponent());
        values.put(KEY_MARK, grade.getMark());

        //perform the query and close the database
        db.insert(TABLE_GRADES, null, values);
        db.close();
    }

    /**
     * Gets a single product
     * @param id - student ID
     * @return the mark info
     */
    public Grade getGrade(int id){
        //get instance of database
        SQLiteDatabase db = this.getReadableDatabase();
        //set the values of the query using a cursor
        Cursor cursor = db.query(TABLE_GRADES, new String[]{KEY_ID, KEY_COMP, KEY_MARK}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        //Check that the cursor return something
        if (cursor != null){
            cursor.moveToFirst();
        }
        //create the new product
        Grade grade = new Grade(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Float.valueOf(cursor.getString(2)));
        return grade;
    }

    /**
     * Gets all grades from the DB
     * @return
     */
    public List<Grade> getAllGrades(){
        //List that contains all products
        List<Grade> gradestList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_GRADES;

        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();
        //Use a cursor with a rawQuery
        Cursor cursor = db.rawQuery(query, null);
        //Loop through the result and create a new product and add to the list
        if (cursor.moveToFirst()){
            do{
                Grade grade = new Grade(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Float.valueOf(cursor.getString(2)));
                gradestList.add(grade);
            } while (cursor.moveToNext());
        }
        //Close the cursor
        cursor.close();
        return gradestList;
    }

    /**
     * Gets how many grades there are in the database
     * @return
     */
    public int getGradeCount() {
        //Build Query
        String countQuery = "SELECT  * FROM " + TABLE_GRADES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        //return count
        return cursor.getCount();
    }

    /**
     * Updates the product information
     * @param grade - The product to be updated
     * @return - whether it succeeded or not
     */
    public int updateGrade(Grade grade){
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();

        //set the values of the query
        ContentValues values = new ContentValues();
        values.put(KEY_COMP, grade.getCourseComponent());
        values.put(KEY_MARK, grade.getMark());

        //Perform the query and return its status
        return db.update(TABLE_GRADES, values, KEY_ID + "=?", new String[]{String.valueOf(grade.getStudentId())});
    }

    /**
     * Deletes the selected grade
     * @param grade - the grade to delete
     */
    public void deleteGrade(Grade grade) {
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();
        //Delete the entry and close the db
        db.delete(TABLE_GRADES, KEY_ID + " = ?",
                new String[] { String.valueOf(grade.getStudentId()) });
        db.close();
    }
}