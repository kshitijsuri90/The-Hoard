package com.example.android.hoard.data;

import android.provider.BaseColumns;

public class BookContract {

    public static abstract class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "Books";
        public static final String PRODUCT_NAME = "Name";
        public static final String _ID = BaseColumns._ID;
        public static final String AUTHOR = "author";
        public static final String GENRE = "genre";
        public static final String SUPPLIER_NAME = "supplier_name";
        public static final String SUPPLIER_CONTACT = "contact_info";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String RELEASED_YEAR = "released_year";

    }
}
