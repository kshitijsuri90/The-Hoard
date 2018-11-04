package com.example.android.hoard;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.hoard.data.BookContract;
import com.example.android.hoard.data.BookDbHelper;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private BookDbHelper mDbHelper = new BookDbHelper(this);
    private long row_id = 0;
    private static final int BOOK_LOADER = 0;
    BookCursorAdapter bookCursorAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, BookEditorActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new BookDbHelper(this);
        ListView list =  findViewById(R.id.list);
        View empty_view = findViewById(R.id.empty_view);
        list.setEmptyView(empty_view);
        bookCursorAdapter = new BookCursorAdapter(this,null);
        list.setAdapter(bookCursorAdapter);
        getLoaderManager().initLoader(BOOK_LOADER,null,this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(InventoryActivity.this,BookEditorActivity.class);
                Uri book_uri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI,id);
                intent.setData(book_uri);
                startActivity(intent);
            }
        });

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
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.PRODUCT_NAME, "Harry Potter");
        values.put(BookContract.BookEntry.AUTHOR ,"J K Rowling");
        values.put(BookContract.BookEntry.GENRE, BookContract.BookEntry.GENRE_FICTION);
        values.put(BookContract.BookEntry.SUPPLIER_NAME, "Penguin Publications");
        values.put(BookContract.BookEntry.SUPPLIER_CONTACT, "9865269636");
        values.put(BookContract.BookEntry.PRICE, "25");
        values.put(BookContract.BookEntry.QUANTITY, "40");
        values.put(BookContract.BookEntry.RELEASED_YEAR, "1997");
        Uri new_uri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI,values);
    }

    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(BookContract.BookEntry.CONTENT_URI, null, null);
        Log.v("InventoryActivity", rowsDeleted + " rows deleted from pet database");
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String [] projections = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.PRODUCT_NAME,
                BookContract.BookEntry.AUTHOR,
                BookContract.BookEntry.PRICE,

        };
        return new CursorLoader(this,
                BookContract.BookEntry.CONTENT_URI,
                projections,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        bookCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        bookCursorAdapter.swapCursor(null);
    }

}
