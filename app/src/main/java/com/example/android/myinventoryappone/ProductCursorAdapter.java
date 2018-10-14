package com.example.android.myinventoryappone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import data.InventoryContract.InventoryEntry;

public class ProductCursorAdapter extends CursorAdapter {
    /**
     * Creates a @link Inventory cursor Adapter
     *
     * @param context the context
     * @param c       The cursor from which to get the data.
     */


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.edit_quantity);

        // Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex);


        if (TextUtils.isEmpty(productPrice)) {
            productPrice = context.getString(R.string.price_tba);
        }
        // Update the TextViews with the attributes for the current product
        nameTextView.setText(String.valueOf(productName));
        summaryTextView.setText(String.valueOf(productPrice));
        quantityTextView.setText(Integer.toString(quantity));

//button to order
        final Button orderButton = view.findViewById(R.id.button_order);
        int currentId = cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID));

        final Uri contentUri = Uri.withAppendedPath(InventoryEntry.CONTENT_URI, Integer.toString(currentId));

        //updates the inventory quantity
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.valueOf(quantityTextView.getText().toString());
                if (quantity > 0) {
                    quantity = quantity - 1;
                } else {
                    Toast.makeText(context, "Click plus sign to add an item to inventory.", Toast.LENGTH_SHORT).show();
                }
                ContentValues values = new ContentValues();
                values.put(InventoryEntry.COLUMN_QUANTITY, quantity);
                context.getContentResolver().update(contentUri, values, null, null);
            }

        });

    }
}