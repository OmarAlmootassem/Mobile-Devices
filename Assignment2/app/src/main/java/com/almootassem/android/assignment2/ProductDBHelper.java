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

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT, " + KEY_PRICE + " DECIMAL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_DESCRIPTION, product.getDescription());
        values.put(KEY_PRICE, product.getPrice());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public Product getProduct(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KEY_ID, KEY_NAME, KEY_DESCRIPTION, KEY_PRICE}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Product product = new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Double.valueOf(cursor.getString(3)));
        return product;
    }

    public List<Product> getAllProducts(){
        List<Product> productList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                Product product = new Product(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Double.valueOf(cursor.getString(3)));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public int getProductCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_DESCRIPTION, product.getDescription());
        values.put(KEY_PRICE, product.getPrice());

        return db.update(TABLE_PRODUCTS, values, KEY_ID + "=?", new String[]{String.valueOf(product.getProductId())});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getProductId()) });
        db.close();
    }
}