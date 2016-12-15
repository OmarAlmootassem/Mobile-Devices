package com.almootassem.android.practiceexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 100520286 on 12/10/2016.
 */

public class BikeDBHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BikeManager";

    private static final String TABLE_NAME = "Bikes";

    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "_id";
    private static final String KEY_BIKE_ID = "bikeShareId";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LONG = "longitude";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUM_BIKES = "numBikes";
    private static final String KEY_ADDRESS = "address";

    public BikeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method is called during creation of the database
     * @param db - the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_BIKE_ID + " INT NOT NULL,"
                + KEY_LAT + " DOUBLE NOT NULL," + KEY_LONG + " DOUBLE NOT NULL," + KEY_NAME + " TEXT NOT NULL,"
                + KEY_NUM_BIKES + " INT NOT NULL," + KEY_ADDRESS + " text NULL" + ")";
        db.execSQL(CREATE_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Adds an item to the database
     * @param item - the item to be added
     */
    public void addItem(Bike item){
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();

        //set the values of the query
        ContentValues values = new ContentValues();
        values.put(KEY_BIKE_ID, item.getBikeShareId());
        values.put(KEY_LAT, item.getLatitude());
        values.put(KEY_LONG, item.getLongitude());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_NUM_BIKES, item.getNumBikes());
        values.put(KEY_ADDRESS, item.getAddress());

        //perform the query and close the database
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Gets a single item
     * @param id - the ID
     * @return the item info
     */
    public Bike getItem(int id){
        //get instance of database
        SQLiteDatabase db = this.getReadableDatabase();
        //set the values of the query using a cursor
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_BIKE_ID, KEY_LAT, KEY_LONG, KEY_NAME, KEY_NUM_BIKES, KEY_ADDRESS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        //Check that the cursor return something
        if (cursor != null){
            cursor.moveToFirst();
        }
        //create the new product
        Bike item = null;
        if (cursor != null) {
            item = new Bike(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Double.parseDouble(cursor.getString(2)),
                    Double.parseDouble(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
        }
        return item;
    }

    /**
     * Gets all items from the DB
     * @return
     */
    public List<Bike> getAllItems(){
        //List that contains all products
        List<Bike> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;

        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();
        //Use a cursor with a rawQuery
        Cursor cursor = db.rawQuery(query, null);
        //Loop through the result and create a new product and add to the list
        if (cursor.moveToFirst()){
            do{
                Bike item = new Bike(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Double.parseDouble(cursor.getString(2)),
                        Double.parseDouble(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
                list.add(item);
            } while (cursor.moveToNext());
        }
        //Close the cursor
        cursor.close();
        return list;
    }

    /**
     * Gets how many grades there are in the database
     * @return
     */
    public int getCount() {
        //Build Query
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        //return count
        return cursor.getCount();
    }

    /**
     * Updates the item information
     * @param item - The item to be updated
     * @return - whether it succeeded or not
     */
    public int updateItem(Bike item){
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();

        //set the values of the query
        ContentValues values = new ContentValues();
        values.put(KEY_BIKE_ID, item.getBikeShareId());
        values.put(KEY_LAT, item.getLatitude());
        values.put(KEY_LONG, item.getLongitude());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_NUM_BIKES, item.getNumBikes());
        values.put(KEY_ADDRESS, item.getAddress());

        //Perform the query and return its status
        return db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    /**
     * Deletes the selected item
     * @param item - the item to delete
     */
    public void deleteItem(Bike item) {
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();
        //Delete the entry and close the db
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
    }

    /**
     * Drops entire table
     */
    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}