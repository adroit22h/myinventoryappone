package data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public final class InventoryContract {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {
    }

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */

    public static abstract class InventoryEntry implements BaseColumns {

        // The "Content authority" is a name for the entire content provider, similar to the
        // relationship between a domain name and its website.  A convenient string to use for the
        // content authority is the package name for the app, which is guaranteed to be unique on the
        // device.
        public static final String CONTENT_AUTHORITY = "com.example.android.myinventoryappone";

        // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
        // the content provider.
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        // Possible paths (appended to base content URI for possible URI's)
        // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
        // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
        // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
        // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
        public static final String PATH_INVENTORY = "inventory";
        public static final String PATH_TABLE = "products";

        // To make it easy to query for the exact date, we normalize all dates that go into
        // the database to the start of the the Julian day at UTC.
        public static long normalizeDate(long startDate) {
            // normalize the start date to the beginning of the (UTC) day
            Time time = new Time();
            time.set(startDate);
            int julianDay = Time.getJulianDay(startDate, time.gmtoff);
            return time.setJulianDay(julianDay);
        }

        /* Inner class that defines the table contents of the product table */
        public static final class LocationEntry implements BaseColumns {

            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_TABLE).build();

            public static final String CONTENT_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLE;
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLE;


            /**
             * Name of database table for pets
             */
            public final static String TABLE_NAME = "products";

            /**
             * Unique ID number for the products (only for use in the database table).
             * <p>
             * Type: INTEGER
             */
            public final static String _ID = BaseColumns._ID;

            /**
             * Name of the product.
             * <p>
             * Type: TEXT
             */
            public final static String COLUMN_PRODUCT_NAME = "name";

            public final static String COLUMN_QUANTITY = "quantity";

            /**
             * product price
             * <p>
             * Type: TEXT
             */
            public final static String COLUMN_PRODUCT_PRICE = "price";

            /**
             * Name of Supplier.
             * <p>
             * The only possible values are {@link #SUPPLIER_WONG}, {@link #SUPPLIER_JONES},
             * or {@link #SUPPLIER_DMIT}.
             * <p>
             * Type: INTEGER
             */
            public final static String COLUMN_SUPPLIER_NAME = "supplierName";

            /**
             * Weight of the pet.
             * <p>
             * Type: INTEGER
             */
            public final static String COLUMN_SUPPLIER_PHONE = "supplierPhone";

            /**
             * Possible values for suppliers
             */
            public static final String SUPPLIER_WONG = "Wong";
            public static final String SUPPLIER_JONES = "Jones";
            public static final String SUPPLIER_DMIT = "Dmit";
        }

    }
}