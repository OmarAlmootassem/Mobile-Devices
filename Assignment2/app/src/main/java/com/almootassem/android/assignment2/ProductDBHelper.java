package com.almootassem.android.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 100520286 on 10/30/2016.
 */

public class ProductDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ProductsManager";

    private static final String TABLE_PRODUCTS = "Products";

    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "productId";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRICE = "price";

    public ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method is called during creation of the database
     * @param db - the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT, " + KEY_PRICE + " DECIMAL" + ")";
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    /**
     * Adds a product to the database
     * @param product - the product to be added
     */
    public void addProduct(Product product){
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();

        //set the values of the query
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_DESCRIPTION, product.getDescription());
        values.put(KEY_PRICE, product.getPrice());

        //perform the query and close the database
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    /**
     * Gets a single product
     * @param id - product ID
     * @return the product
     */
    public Product getProduct(int id){
        //get instance of database
        SQLiteDatabase db = this.getReadableDatabase();
        //set the values of the query using a cursor
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KEY_ID, KEY_NAME, KEY_DESCRIPTION, KEY_PRICE}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        //Check that the cursor return something
        if (cursor != null){
            cursor.moveToFirst();
        }
        //create the new product
        Product product = new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Double.valueOf(cursor.getString(3)));
        return product;
    }

    /**
     * Gets all products from the DB
     * @return
     */
    public List<Product> getAllProducts(){
        //List that contains all products
        List<Product> productList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PRODUCTS;

        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();
        //Use a cursor with a rawQuery
        Cursor cursor = db.rawQuery(query, null);
        //Loop through the result and create a new product and add to the list
        if (cursor.moveToFirst()){
            do{
                Product product = new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Double.valueOf(cursor.getString(3)));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        //Close the cursor
        cursor.close();
        return productList;
    }

    /**
     * Gets how many products there are in the database
     * @return
     */
    public int getProductCount() {
        //Build Query
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        //return count
        return cursor.getCount();
    }

    /**
     * Updates the product information
     * @param product - The product to be updated
     * @return - whether it succeeded or not
     */
    public int updateProduct(Product product){
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();

        //set the values of the query
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_DESCRIPTION, product.getDescription());
        values.put(KEY_PRICE, product.getPrice());

        //Perform the query and return its status
        return db.update(TABLE_PRODUCTS, values, KEY_ID + "=?", new String[]{String.valueOf(product.getProductId())});
    }

    /**
     * Deletes the selected product
     * @param product - the product to delete
     */
    public void deleteProduct(Product product) {
        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();
        //Delete the entry and close the db
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getProductId()) });
        db.close();
    }
}