package com.example.android.hoard;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.hoard.data.BookContract;
import com.example.android.hoard.data.BookDbHelper;

public class InventoryActivity extends AppCompatActivity {
    private BookDbHelper mDbHelper = new BookDbHelper(this);
    private long row_id = -1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new BookDbHelper(this);
        displayDatabaseInfo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String [] projections = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.PRODUCT_NAME,
                BookContract.BookEntry.AUTHOR,
                BookContract.BookEntry.RELEASED_YEAR,
                BookContract.BookEntry.GENRE,
                BookContract.BookEntry.SUPPLIER_CONTACT,
                BookContract.BookEntry.SUPPLIER_NAME,
                BookContract.BookEntry.PRICE,
                BookContract.BookEntry.QUANTITY,

        };
        TextView displayView = findViewById(R.id.text_view_book);
        Cursor cursor = db.query(BookContract.BookEntry.TABLE_NAME,projections,null,null,null,null,null);
        try {
            displayView.setText("The books table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(BookContract.BookEntry._ID + " - " +
                    BookContract.BookEntry.PRODUCT_NAME + " - " +
                    BookContract.BookEntry.AUTHOR + " - " +
                    BookContract.BookEntry.RELEASED_YEAR + " - " +
                    BookContract.BookEntry.PRICE + " - " +
                    BookContract.BookEntry.QUANTITY + " - " +
                    BookContract.BookEntry.GENRE + " - " +
                    BookContract.BookEntry.SUPPLIER_NAME + " - " +
                    BookContract.BookEntry.SUPPLIER_CONTACT + "\n");
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.PRODUCT_NAME);
            int authorColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.AUTHOR);
            int yearColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.RELEASED_YEAR);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.QUANTITY);
            int genreColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.GENRE);
            int suppliernameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.SUPPLIER_NAME);
            int supplierContactColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.SUPPLIER_CONTACT);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                String currentReleasedYear = cursor.getString(yearColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                String currentQuantity = cursor.getString(quantityColumnIndex);
                String currentGenre = cursor.getString(genreColumnIndex);
                String currentSupplier_name = cursor.getString(suppliernameColumnIndex);
                String currentSupplier_contact = cursor.getString(supplierContactColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentAuthor + " - " +
                        currentReleasedYear + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentGenre + " - " +
                        currentSupplier_name + " - " +
                        currentSupplier_contact));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertPet();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.PRODUCT_NAME, "Harry Potter ");
        values.put(BookContract.BookEntry.AUTHOR ,"J K Rowling");
        values.put(BookContract.BookEntry.GENRE, "Fiction");
        values.put(BookContract.BookEntry.SUPPLIER_NAME, "Penguin Publications");
        values.put(BookContract.BookEntry.SUPPLIER_CONTACT, "+1 98 456");
        values.put(BookContract.BookEntry.PRICE, "$25");
        values.put(BookContract.BookEntry.QUANTITY, "40");
        values.put(BookContract.BookEntry.RELEASED_YEAR, "1997");

        row_id = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
    }

}
