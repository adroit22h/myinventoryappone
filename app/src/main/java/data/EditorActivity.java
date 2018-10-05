package data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.myinventoryappone.R;

//import com.example.android.myinventoryappone;


/**
 * Allows user to create a new product or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the product name
     */
    private EditText mNameEditText;

    private EditText mQuantityEditText;

    /**
     * EditText field to enter product price
     */
    private EditText mProductPriceEditText;

    /**
     * EditText field to enter supplier phone
     */
    private EditText mSupplierPhoneEditText;

    /**
     * EditText field to enter the supplier gender
     */
    private Spinner mSupplierName;

    private String mSupplier;

    /**
     * .
     */
    //private String mySupplierName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mProductPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mSupplierName = (Spinner) findViewById(R.id.spinner_supplierName);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the name of the supplier.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_name_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierName.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (!TextUtils.isEmpty(selection)) {
                        if (selection.equals(getString(R.string.supplier_Jones))) {
                            mSupplier = "Jones"; // Jones
                        } else if (selection.equals(getString(R.string.supplier_Wong))) {
                            mSupplier = "Wong"; // Wong
                        } else {
                            mSupplier = "Dmit";// Dmit
                        }
                    }
                }

                // Because AdapterView is an abstract class, onNothingSelected must be defined

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = "Dmit"; // Dmit
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu options from the res/menu/menu_editor.xml file.
                // This adds menu items to the app bar.
                getMenuInflater().inflate(R.menu.menu_editor, menu);
                return true;
            }

            private void insertProduct() {
                String nameString = mNameEditText.getText().toString().trim();
                String quantityString = mQuantityEditText.getText().toString().trim();
                String productPrice = mProductPriceEditText.getText().toString().trim();
                String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();
                //Spinner SupplierName = mSupplier.getTag().toString().trim();

                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a ContentValues object where column names are the keys,
                // and Toto's pet attributes are the values.
                ContentValues values = new ContentValues();

                values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "Banana Ice Cream");
                values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, "$9.99");
                values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, "5");
                values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, "Wong");
                values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, "9995552525");

                long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);

                log.v(".CatalogActivity", "New Row ID", +newRowId);


            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // User clicked on a menu option in the app bar overflow menu
                switch (item.getItemId()) {
                    // Respond to a click on the "Save" menu option
                    case R.id.action_save:
                        // Do nothing for now
                        insertProduct();
                        finish();
                        return true;
                    // Respond to a click on the "Delete" menu option
                    case R.id.action_delete:
                        // Do nothing for now
                        return true;
                    // Respond to a click on the "Up" arrow button in the app bar
                    case android.R.id.home:
                        // Navigate back to parent activity (CatalogActivity)
                        NavUtils.navigateUpFromSameTask(this);
                        return true;
                }
                return super.onOptionsItemSelected(item);
            }
        });
    }
}


