package com.example.android.myinventoryappone;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import data.InventoryContract.InventoryEntry;
import data.InventoryDbHelper;

import android.support.v7.app.LoaderManager;
import android.support.v7.content.CursorLoader;
import android.support.v7.content.Loader;
import android.support.v7.widget.SimpleCursorAdapter;

/**
 * Displays list of products that were entered and stored in the app.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;

    ProductCursorAdapter mCursorAdapter;

    /** Database helper that will provide us access to the database */
    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //Find the ListView which will fill the product data
        ListView productListView = (ListView) findViewById(R.id.list);

        //Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
      //  mDbHelper = new InventoryDbHelper(this);

        //Setup Adapter to create a list item view
        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        //initialize the loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the mayat database.
     */
//    private void displayDatabaseInfo() {
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                InventoryEntry._ID,
//                InventoryEntry.COLUMN_PRODUCT_NAME,
//                InventoryEntry.COLUMN_PRODUCT_PRICE,
//                InventoryEntry.COLUMN_QUANTITY,
//                InventoryEntry.COLUMN_SUPPLIER_NAME,
//                InventoryEntry.COLUMN_SUPPLIER_PHONE};
//
//        // Perform a query on the pets table
//        Cursor cursor = db.query(
//                InventoryEntry.TABLE_NAME,   // The table to query
//                projection,            // The columns to return
//                null,                  // The columns for the WHERE clause
//                null,                  // The values for the WHERE clause
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                null);                   // The sort order

      //  TextView displayView = (TextView) findViewById(R.id.text_view_product);
//Find the ListView which will be populated with the products information
        ListView productListView = (ListView) findViewById (R.id.list);
//        try {
//            // Create a header in the Text View that looks like this:
//
//            displayView.setText("The products table contains " + cursor.getCount() + " products.\n\n");
//            displayView.append(InventoryEntry._ID + " - " +
//                    InventoryEntry.COLUMN_PRODUCT_NAME + " - " +
//                    InventoryEntry.COLUMN_PRODUCT_PRICE + " - " +
//                    InventoryEntry.COLUMN_QUANTITY + " - " +
//                    InventoryEntry.COLUMN_SUPPLIER_NAME + " - " +
//                    InventoryEntry.COLUMN_SUPPLIER_PHONE + "\n");


        //Set up an Adapter to create a list of items for each row of product info in the cursor
        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);

            // Figure out the index of each column
//            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
//            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
//            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
//            int suppliernameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
//            int supplierphoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);
//
//            // Iterate through all the returned rows in the cursor
//            while (cursor.moveToNext()) {
//                // Use that index to extract the String or Int value of the word
//                // at the current row the cursor is on.
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentPrice = cursor.getString(priceColumnIndex);
//                int currentQuantity = cursor.getInt(quantityColumnIndex);
//                int currentSuppliername = cursor.getInt(suppliernameColumnIndex);
//                int currentSupplierphone = cursor.getInt(supplierphoneColumnIndex);
//                // Display the values from each column of the current row in the cursor in the TextView
//                displayView.append(("\n" + currentID + " - " +
//                        currentName + " - " +
//                        currentPrice + " - " +
//                        currentQuantity + " - " +
//                        currentSuppliername + " -" +
//                        currentSupplierphone));
//            }
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }

        //Attach the adapter to the ListView.
        productListView.setAdapter(adapter);
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertProduct() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "Banana Iced");
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, "19.99");
        values.put(InventoryEntry.COLUMN_QUANTITY, "10");
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, InventoryEntry.SUPPLIER_WONG);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, 786-999-5555);

        // Insert a new row for the database, returning the ID of that new row.

       // long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_mainl file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProduct();
               // displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //define  projection that specifies the column for the table
        String [] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE};

        //This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,  //parent activity
        InventoryEntry.CONTENT_URI,   //provider content URI to query
                projection,           //Columns to include in the resulting Cursor
                null,        //No selection clause
                null,    //no selection arguments
                null);     //Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
      mCursorAdapter.swapCursor(null);
    }
}