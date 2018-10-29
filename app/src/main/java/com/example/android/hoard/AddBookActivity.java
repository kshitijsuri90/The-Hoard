package com.example.android.hoard;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.hoard.data.BookContract;
import com.example.android.hoard.data.BookDbHelper;

import java.util.Objects;

public class AddBookActivity extends AppCompatActivity {
    private BookDbHelper mDbHelper = new BookDbHelper(this);
    long row_id =0;

    private EditText NameEditText;
    private EditText AuthorEditText;

    private EditText YearEditText;

    private EditText QuantityEditText;

    private EditText SupplierNameEditText;

    private EditText SupplierContactEditText;

    private EditText PriceEditText;

    private Spinner GenreSpinner;

    private int Genre = BookContract.BookEntry.GENRE_OTHER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        // Find all relevant views that we will need to read user input from
        NameEditText =  findViewById(R.id.book_name_edittext);
        AuthorEditText =  findViewById(R.id.author_edittext);
        YearEditText =  findViewById(R.id.released_year);
        PriceEditText = findViewById(R.id.price);
        QuantityEditText =  findViewById(R.id.quantity);
        SupplierNameEditText =  findViewById(R.id.supplier_name);
        SupplierContactEditText =  findViewById(R.id.supplier_contact);
        GenreSpinner =findViewById(R.id.spinner_genre);
        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the genre of the book.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genreSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_genre_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genreSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        GenreSpinner.setAdapter(genreSpinnerAdapter);

        // Set the integer mSelected to the constant values
        GenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.genre_drama))) {
                        Genre = BookContract.BookEntry.GENRE_DRAMA;
                    } else if (selection.equals(getString(R.string.genre_fiction))) {
                        Genre = BookContract.BookEntry.GENRE_FICTION;
                    } else if (selection.equals(getString(R.string.genre_horror))) {
                        Genre = BookContract.BookEntry.GENRE_HORROR;
                    } else if (selection.equals(getString(R.string.genre_travel))) {
                        Genre = BookContract.BookEntry.GENRE_TRAVEL;
                    }
                    else {
                        Genre = BookContract.BookEntry.GENRE_OTHER;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Genre = BookContract.BookEntry.GENRE_OTHER;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);

        return true;
    }
    private void insertPet(){
        String name = NameEditText.getText().toString().trim();
        String author = AuthorEditText.getText().toString().trim();
        String released_year = YearEditText.getText().toString().trim();
        String quantity = QuantityEditText.getText().toString().trim();
        String price = PriceEditText.getText().toString().trim();
        String supplier_name = SupplierNameEditText.getText().toString().trim();
        String supplier_contact = SupplierContactEditText.getText().toString().trim();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.PRODUCT_NAME, name);
        values.put(BookContract.BookEntry.AUTHOR ,author);
        values.put(BookContract.BookEntry.GENRE, Genre);
        values.put(BookContract.BookEntry.SUPPLIER_NAME, supplier_name);
        values.put(BookContract.BookEntry.SUPPLIER_CONTACT, supplier_contact);
        values.put(BookContract.BookEntry.PRICE, price);
        values.put(BookContract.BookEntry.QUANTITY, quantity);
        values.put(BookContract.BookEntry.RELEASED_YEAR, released_year);
        row_id = db.insert(BookContract.BookEntry.TABLE_NAME,null,values);
        if(row_id==-1){
            Toast.makeText(getApplicationContext(),"Eroor adding pet to database",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Pet Saved with id " + row_id,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                insertPet();
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
