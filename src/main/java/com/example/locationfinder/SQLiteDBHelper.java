package com.example.locationfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LocationFinder.db";  // Updated to reflect the app purpose

    // Table schema for addresses
    public static final String TABLE_NAME = "Addresses";
    public static final String COLUMN_ID = "address_id";
    public static final String COLUMN_TITLE = "address_title";
    public static final String COLUMN_ADDRESS = "address_description";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";

    // SQL query to create the address table
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_ADDRESS + " TEXT, " +
            COLUMN_LONGITUDE + " REAL, " +
            COLUMN_LATITUDE + " REAL);";

    public SQLiteDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE); // Create the addresses table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If upgrading the database, drop the old table and create the new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to close the database connection
    public void close() {
        SQLiteDatabase db = getWritableDatabase();
        db.close();
    }

    // Insert new address into the database
    public boolean insertAddress(Address address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, address.getTitle());
        values.put(COLUMN_ADDRESS, address.getAddress());
        values.put(COLUMN_LONGITUDE, address.getLongitude());
        values.put(COLUMN_LATITUDE, address.getLatitude());

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // Retrieve all addresses
    public Cursor retrieveAllAddresses() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Retrieve addresses by title
    public Cursor retrieveAddressesByTitle(String title) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " LIKE ?", new String[]{"%" + title + "%"});
    }

    // Delete an address by title
    public void deleteAddressByTitle(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TITLE + " = ?", new String[]{title});
    }

    // Delete an address by ID
    public void deleteAddressById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Update address information
    public boolean updateAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, address.getTitle());
        values.put(COLUMN_ADDRESS, address.getAddress());
        values.put(COLUMN_LONGITUDE, address.getLongitude());
        values.put(COLUMN_LATITUDE, address.getLatitude());

        int result = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(address.getId())});
        db.close();
        return result > 0;
    }
}
