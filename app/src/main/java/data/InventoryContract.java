package data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.myinventoryappone";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PRODUCTS = "products";


    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    //public static final class InventoryEntry implements BaseColumns {

    public static abstract class InventoryEntry implements BaseColumns {
        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;




        /** Name of database table for products */
        public final static String TABLE_NAME = "products";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME ="name";

        /**
         * Price  of the product.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Quantity  of the product.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Gender of the pet.SupplierName
         *
         * The only possible values are {@link #SUPPLIER_DMIT}, {@link #SUPPLIER_JONES},
         * or {@link #SUPPLIER_WONG}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_NAME = "suppliername";

        /**
         * Supplier phone.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PHONE = "supplierphone";

        /**
         * Possible values for the supplier Names.
         */
        public static final int SUPPLIER_DMIT = 0;
        public static final int SUPPLIER_JONES = 1;
        public static final int SUPPLIER_WONG = 2;

        /**
         * Returns whether or not the given gender is {@link #SUPPLIER_DMIT}, {@link #SUPPLIER_JONES},
         * or {@link #SUPPLIER_WONG}.
         */
        public static boolean isValidGender(int suppliername) {
            if (suppliername == SUPPLIER_DMIT || suppliername == SUPPLIER_JONES || suppliername == SUPPLIER_WONG) {
                return true;
            }
            return false;
        }
    }

}
