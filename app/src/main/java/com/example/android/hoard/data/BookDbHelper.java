package com.example.android.hoard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Constructs a new instance of {@link BookDbHelper}.
     *
     * @param context of the app
     */

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.PRODUCT_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.AUTHOR + " TEXT, "
                + BookContract.BookEntry.GENRE + " TEXT, "
                + BookContract.BookEntry.SUPPLIER_NAME + " TEXT,"
                + BookContract.BookEntry.SUPPLIER_CONTACT + " TEXT NOT NULL DEFAULT '9824112365', "
                + BookContract.BookEntry.QUANTITY + " INTEGER, "
                + BookContract.BookEntry.PRICE + " INTEGER NOT NULL,"
                + BookContract.BookEntry.RELEASED_YEAR + " INTEGER NOT NULL );";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
