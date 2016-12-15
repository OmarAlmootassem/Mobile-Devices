package ca.uoit.csci4100.assessments.finalexamstarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * TODO: Implement the updatePreferredStore() and getPreferredStore() methods below
 */

public class PreferredStoreLocationDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_FILENAME = "preferredStoreLocation.db";

    private static final String CREATE_STATEMENT = "" +
            "create table PreferredStore(" +
            "  _id integer primary key autoincrement," +
            "  storeId integer not null," +
            "  name text not null," +
            "  streetAddress text null," +
            "  city text null," +
            "  postalCode text null)";

    private static final String DROP_STATEMENT = "" +
            "drop table PreferredStore";

    public PreferredStoreLocationDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STATEMENT);
        db.execSQL(CREATE_STATEMENT);
    }

    /**
     * updatePreferredStore
     *
     * This method will take a Store object, and save all of its fields to the database.
     *
     * @param preferredStore The newly selected preferred store, to be saved
     */
    public void updatePreferredStore(Store preferredStore) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("Delete From PreferredStore");

        //set the values of the query
        ContentValues values = new ContentValues();
        values.put("storeId", preferredStore.getId());
        values.put("name", preferredStore.getName());
        values.put("streetAddress", preferredStore.getStreetAddress());
        values.put("city", preferredStore.getCity());
        values.put("postalCode", preferredStore.getPostalCode());
        Log.v("DB", preferredStore.getId() + "");

        //Perform the query and return its status
        db.insert("PreferredStore", null, values);
        db.close();
    }

    /**
     * getPreferredStore
     *
     * This method will read the last saved preferred store's ID from the database.  If no store has
     * been saved before, it will return -1.
     *
     * @return The ID of the last preferred store, or -1 if there were none saved
     */
    public int getPreferredStore() {


        String query = "SELECT * FROM PreferredStore";

        //get instance of database
        SQLiteDatabase db = this.getWritableDatabase();
        //Use a cursor with a rawQuery
        Cursor cursor = db.rawQuery(query, null);
        //Loop through the result and create a new product and add to the list
        if (cursor.moveToFirst()){
            Log.v("DB", cursor.getString(0) + "," + cursor.getString(1));
            int id = Integer.parseInt(cursor.getString(1));
            cursor.close();
            db.close();
            return id;
        }

        return -1;
    }
}
