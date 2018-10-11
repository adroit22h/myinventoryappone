package com.example.android.myinventoryappone;

//import android.app.LoaderManager;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import data.InventoryContract;
import data.InventoryContract.InventoryEntry;
import data.InventoryDbHelper;


/**
 * Allows user to create a new product or edit an existing one.
 */
public abstract class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //Identifies the inventory Loader
    private static final int EXISTING_INVENTORY_LOADER = 0;
    //phone intent
    String mNumber;
    EditText numberText;
    AlertDialog alertDialog = builder.create();

    /**
     * EditText field to enter the product name
     */
    private EditText mNameEditText;
    /**
     * EditText field to enter the product price
     */
    private EditText mPriceEditText;
    /**
     * EditText field to enter the product quantity
     */
    private EditText mQuantityEditText;
    /**
     * EditText field to enter the supplier phone
     */
    private EditText mSupplierPhoneEditText;
    /**
     * EditText field to enter the pet's gender
     */
    // private Spinner mGenderSpinner;
    private Spinner mSupplierNameSpinner;
    /**
     * Gender of the pet. The possible valid values are in the PetContract.java file:
     * {@link InventoryEntry#SUPPLIER_WONG}, {@link InventoryEntry#SUPPLIER_DMIT}, or
     * {@link InventoryEntry#SUPPLIER_JONES}.
     */
    private int mSupplierName = InventoryEntry.SUPPLIER_DMIT;
    //URi for existing inventory loader
    private Uri mCurrentInventoryUri;
    //Sets the value to false if no changes have been made to product values
    private boolean mInventoryHasChanged = false;
    //On Touch Listener
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();

        if (mCurrentInventoryUri == null) {
            setTitle(getString(R.string.empty_view_title_text));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.empty_view_title_text));
        }

        //init loader to read inventory
        getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mSupplierNameSpinner = (Spinner) findViewById(R.id.spinner_supplierName);

        setupSpinner();

        //ontouch listener for each field
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);
        mSupplierNameSpinner.setOnTouchListener(mTouchListener);


    }

    /**
     * Setup the dropdown spinner that allows the user to select a supplier.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter supplierNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_name_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        supplierNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierNameSpinner.setAdapter(supplierNameSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_Jones))) {
                        mSupplierName = InventoryEntry.SUPPLIER_JONES;
                    } else if (selection.equals(getString(R.string.supplier_Wong))) {
                        mSupplierName = InventoryEntry.SUPPLIER_WONG;
                    } else {
                        //mSupplierName = InventoryEntry.SUPPLIER_DMIT;
                        mSupplierName = InventoryContract.InventoryEntry.SUPPLIER_DMIT;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplierName = InventoryEntry.SUPPLIER_DMIT;
            }
        });
    }

    /**
     * Get user input from editor and save new pet into database.
     */
    private void insertProduct() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space

        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String phoneString = mSupplierPhoneEditText.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        int quantity = Integer.parseInt(quantityString);
        int phone = Integer.parseInt(phoneString);

        if (TextUtils.isEmpty(nameString)
                || TextUtils.isEmpty(priceString)
                || TextUtils.isEmpty(quantityString)
                || TextUtils.isEmpty(phoneString)) {
            Toast.makeText(this, getString(R.string.editor_activity_title_new_product),
                    Toast.LENGTH_LONG).show();
        }

        // Create database helper
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, priceString);
        values.put(InventoryEntry.COLUMN_QUANTITY, quantityString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, phoneString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, mSupplierName);


        if (mCurrentInventoryUri == null) {
            // Insert a new row for pet in the database, returning the ID of that new row.
            //long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the row ID is -1, then there was an error with insertion.
                //Toast.makeText(this, "Error with saving this product", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, getString(R.string.editor_inser_product_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                // Toast.makeText(this, "This product has been saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, getString(R.string.editor_inser_product_success), Toast.LENGTH_SHORT).show();
            }
        }
        //URI: mCurrentInventoryUri
        else {
            getContentResolver().update(mCurrentInventoryUri, values, null, null);
        }
        if (mCurrentInventoryUri != null && TextUtils.isEmpty(nameString)
                || TextUtils.isEmpty(priceString)
                || TextUtils.isEmpty(quantityString)
                || TextUtils.isEmpty(phoneString)) {
            Toast.makeText(this, getString(R.string.item_missing),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_inser_product_success),
                    Toast.LENGTH_LONG).show();
        }
        finish();
    }

        private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.loose, discardButtonClickListener)
        builder.setNegativeButton(R.string.keep_working, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    });

private void showDeletionConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteInventory();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        });
        public void onClick (DialogInterface dialog, int id) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }
                alertDialog.show();


    private void deleteInventory() {
        if (mCurrentInventoryUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);
            //delete message
            Toast.makeText(this, getString(R.string.delete_inventory_failed), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.delete_inventory_success), Toast.LENGTH_LONG).show();
        }

        finish();
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
        if (mCurrentInventoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save to database
                insertProduct();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                //  showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (MainActivity)
                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener.discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                return super.onOptionsItemSelected(item);

    }
        @Override
        public void onBackPressed () {
            if (!mInventoryHasChanged) {
                super.onBackPressed();
                return;
            }
            DialogInterface.OnClickListener.discardButtonClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            };
            showUnsavedChangesDialog(discardButtonClickListener);
        }
    }

        @Override
        public Loader<Cursor> onCreateLoader ( int i, Bundle bundle){
            if (mCurrentInventoryUri == null) {
                return null;
            }

            //define  projection that specifies the column for the table
            String[] projection = {
                    InventoryEntry._ID,
                    InventoryEntry.COLUMN_PRODUCT_NAME,
                    InventoryEntry.COLUMN_PRODUCT_PRICE,
                    InventoryEntry.COLUMN_QUANTITY,
                    InventoryEntry.COLUMN_SUPPLIER_NAME,
                    InventoryEntry.COLUMN_SUPPLIER_PHONE};

            //This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(this,  //parent activity
                    mCurrentInventoryUri,
                    //jInventoryEntry.CONTENT_URI,   //provider content URI to query
                    projection,           //Columns to include in the resulting Cursor
                    null,        //No selection clause
                    null,    //no selection arguments
                    null);     //Default sort order

        }


        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor cursor){
            // mCursorAdapter.swapCursor(data);
            if (cursor == null || cursor.getcount() < 1) {
                return;
            }
            if (cursor.moveToFirst()) {
                int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
                int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
                //int suppliernameColumIndex = cursor.getColumIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
                int supplierphoneColumIndex = cursor.getColumIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

                //extracts
                String name = cursor.getString(nameColumnIndex);
                int price = cursor.getInt(priceColumnIndex);
                int quantity = cursor.getInt(quantityColumnIndex);
                //String suppliername = cursor.getString(suppliernameColumIndex);
                int supplierphone = cursor.getString(supplierphoneColumIndex);

                //updates
                mNameEditText.setText(name);
                mPriceEditText.setText(price);
                mQuantityEditText.setText(quantity);
                //mSupplierName.setText(suppliername);
                //mSupplierNameSpinner.setText(suppliername);
                mSupplierPhoneEditText.setText(supplierphone);
            }

        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            //mCursorAdapter.swapCursor(null);
            mNameEditText.setText("");
            mPriceEditText.setText("");
            mQuantityEditText.setText("");
            mSupplierPhoneEditText.setText("");
        }

        //add button
        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new, View.OnClickListener() {
            @Override
            public void onClick (View v){
                int quantity = Interger.valueOf(mQuantityEditText.getText().toString());

                if (quantity >= 0) {
                    quantity = quantity + 1;
                }
                mQuantityEditText.setText(Integer.toString(quantity));
            }
        });

        //subtract button
        Button subtractButton = findViewById(R.id.button_subtract);
        subtractButton.setOnClickListener(new, View.OnClickListener() {
            @Override
            public void onClick (View v){
                int quantity = Interger.valueOf(mQuantityEditText.getText().toString());

                if (quantity >= 1) {
                    quantity = quantity - 1;
                }
                mQuantityEditText.setText(Integer.toString(quantity));
            }
        });
        //sale button
        Button orderButton = findViewById(R.id.button_order);
        numberText = findViewById(R.id.edit_supplier_phone);

        orderButton.setOnClickListener(new, View.OnClickListener() {
            @Override
            public void onClick (View v){
                mNumber = numberText.getText().toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("phone:" + mNumber));
                startActivity(callIntent);
            }

        };


    }