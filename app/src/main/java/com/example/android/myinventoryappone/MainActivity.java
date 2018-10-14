package com.example.android.myinventoryappone;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import data.InventoryContract.InventoryEntry;

/**
 * Displays list of products that were entered and stored in the app.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;
    ProductCursorAdapter mCursorAdapter;

    /** Database helper that will provide us access to the database */
   // private InventoryDbHelper mDbHelper;

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

        //Setup Adapter to create a list item view
        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        //onClickListener added when clicked
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                //uri to collect content and set the data
                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                intent.setData(currentInventoryUri);
                startActivity(intent);
            }
        });
        //initialize the loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

           /**
     * Helper method to insert hardcoded data into the database. For debugging purposes only.
     */
    private void insertProduct() {
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

    private void deleteAllInventory() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted+"rows deleted from inventory db");
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
                deleteAllInventory();
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