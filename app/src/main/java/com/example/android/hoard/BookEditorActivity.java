package com.example.android.hoard;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.hoard.data.BookContract;
import com.example.android.hoard.data.BookDbHelper;

import java.util.Objects;

public class BookEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri mCurrentBookUri = null;
    private boolean mBookHasChanged = false;


    private EditText NameEditText;
    private EditText AuthorEditText;

    private EditText YearEditText;

    private EditText QuantityEditText;

    private EditText SupplierNameEditText;

    private EditText SupplierContactEditText;

    private EditText PriceEditText;

    private Spinner GenreSpinner;
    private ImageButton call;
    private ImageButton increase;
    private ImageButton decrease;

    private int Genre = BookContract.BookEntry.GENRE_OTHER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();
        if (mCurrentBookUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(R.string.add_book);

            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing pet, so change app bar to say "Edit Pet"
            setTitle(R.string.edit_book);

            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }
        // Find all relevant views that we will need to read user input from
        NameEditText =  findViewById(R.id.book_name_edittext);
        AuthorEditText =  findViewById(R.id.author_edittext);
        YearEditText =  findViewById(R.id.released_year);
        PriceEditText = findViewById(R.id.price);
        QuantityEditText =  findViewById(R.id.quantity);
        SupplierNameEditText =  findViewById(R.id.supplier_name);
        SupplierContactEditText =  findViewById(R.id.supplier_contact);
        increase = findViewById(R.id.increase_quantity);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = QuantityEditText.getText().toString();
                if(quantity.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Enter valid quantity",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Integer.valueOf(quantity)<0){
                        Toast.makeText(getApplicationContext(),"Quantity is not valid",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int quantity_value = Integer.valueOf(quantity);
                        quantity_value++;
                        quantity = quantity_value+ "";
                        QuantityEditText.setText(quantity);
                    }
                }
            }
        });
        decrease = findViewById(R.id.decrease_quantity);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = QuantityEditText.getText().toString();
                if(quantity.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Enter valid quantity",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Integer.valueOf(quantity)<=0){
                        Toast.makeText(getApplicationContext(),"Quantity is not valid",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int quantity_value = Integer.valueOf(quantity);
                        quantity_value--;
                        quantity = quantity_value+"";
                        QuantityEditText.setText(quantity);
                    }
                }
            }
        });
        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = SupplierContactEditText.getText().toString().trim();
                if(contact.isEmpty()){
                    Toast.makeText(getApplicationContext(),"No contact found",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact, null));
                    startActivity(intent);
                }
            }
        });
        GenreSpinner =findViewById(R.id.spinner_genre);
        NameEditText.setOnTouchListener(mTouchListener);
        AuthorEditText.setOnTouchListener(mTouchListener);
        YearEditText.setOnTouchListener(mTouchListener);
        PriceEditText.setOnTouchListener(mTouchListener);
        QuantityEditText.setOnTouchListener(mTouchListener);
        SupplierNameEditText.setOnTouchListener(mTouchListener);
        SupplierContactEditText.setOnTouchListener(mTouchListener);
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


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    private void savePet() {
        String name = NameEditText.getText().toString().trim();
        String author = AuthorEditText.getText().toString().trim();
        String price = PriceEditText.getText().toString().trim();
        String released_year = YearEditText.getText().toString().trim();
        String quantity = QuantityEditText.getText().toString().trim();
        String sup_name = SupplierNameEditText.getText().toString().trim();
        String sup_contact = SupplierContactEditText.getText().toString().trim();
        if(mCurrentBookUri==null && name.isEmpty() && author.isEmpty() && price.isEmpty() && released_year.isEmpty() && quantity.isEmpty() &&sup_contact.isEmpty() && sup_name.isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter Data to save book",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(name.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(author.isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter Author",Toast.LENGTH_SHORT).show();
            return ;
        }
        else if(quantity.isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter Quantity",Toast.LENGTH_SHORT).show();
            return ;
        }
        else if(price.isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter Price",Toast.LENGTH_SHORT).show();
            return ;
        }
        else if (sup_contact.isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter Supplier Contact",Toast.LENGTH_SHORT).show();
            return ;
        }
        else if(sup_name.isEmpty()){
            sup_name = "Unknown";
        }
        else if(released_year.isEmpty()){
            released_year = "unknown";
        }
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.PRODUCT_NAME,  name);
        values.put(BookContract.BookEntry.AUTHOR ,author);
        values.put(BookContract.BookEntry.GENRE, Genre);
        values.put(BookContract.BookEntry.SUPPLIER_NAME, sup_name);
        values.put(BookContract.BookEntry.SUPPLIER_CONTACT, sup_contact);
        values.put(BookContract.BookEntry.PRICE, price);
        values.put(BookContract.BookEntry.QUANTITY, quantity);
        values.put(BookContract.BookEntry.RELEASED_YEAR, released_year);
        if (mCurrentBookUri == null) {
            Uri new_uri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);
            if (new_uri == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_log), Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_LONG).show();
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.error_updating),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                savePet();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(BookEditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(BookEditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.PRODUCT_NAME,
                BookContract.BookEntry.AUTHOR,
                BookContract.BookEntry.PRICE,
                BookContract.BookEntry.QUANTITY,
                BookContract.BookEntry.RELEASED_YEAR,
                BookContract.BookEntry.SUPPLIER_NAME,
                BookContract.BookEntry.SUPPLIER_CONTACT,
                BookContract.BookEntry.GENRE
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.PRODUCT_NAME);
            int authorColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.AUTHOR);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.QUANTITY);
            int supplier_nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.SUPPLIER_NAME);
            int contactColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.SUPPLIER_CONTACT);
            int released_yearColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.RELEASED_YEAR);
            int genreColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.GENRE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String author = cursor.getString(authorColumnIndex);
            String year = cursor.getString(released_yearColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String sup_name = cursor.getString(supplier_nameColumnIndex);
            String sup_contact = cursor.getString(contactColumnIndex);
            int genre = cursor.getInt(genreColumnIndex);
            // Update the views on the screen with the values from the database
            NameEditText.setText(name);
            AuthorEditText.setText(author);
            YearEditText.setText(year);
            PriceEditText.setText(price);
            QuantityEditText.setText(quantity);
            SupplierNameEditText.setText(sup_name);
            SupplierContactEditText.setText(sup_contact);
            switch (genre) {
                case BookContract.BookEntry.GENRE_FICTION:
                    GenreSpinner.setSelection(0);
                    break;
                case BookContract.BookEntry.GENRE_HORROR:
                    GenreSpinner.setSelection(1);
                    break;
                case BookContract.BookEntry.GENRE_DRAMA:
                    GenreSpinner.setSelection(2);
                    break;
                case BookContract.BookEntry.GENRE_TRAVEL:
                    GenreSpinner.setSelection(3);
                    break;
                default:
                    GenreSpinner.setSelection(4);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        NameEditText.setText("");
        AuthorEditText.setText("");
        YearEditText.setText("");
        PriceEditText.setText("");
        QuantityEditText.setText("");
        SupplierNameEditText.setText("");
        SupplierContactEditText.setText("");
        GenreSpinner.setSelection(4);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.error_delete),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.success_delete),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }


}
