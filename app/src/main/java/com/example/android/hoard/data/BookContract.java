package com.example.android.hoard.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BookContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.hoard";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";

    public static abstract class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "books";
        public static final String PRODUCT_NAME = "Name";
        public static final String _ID = BaseColumns._ID;
        public static final String AUTHOR = "author";
        public static final String GENRE = "genre";
        public static final String SUPPLIER_NAME = "supplier_name";
        public static final String SUPPLIER_CONTACT = "contact_info";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String RELEASED_YEAR = "released_year";
        public static final int GENRE_FICTION = 0;
        public static final int GENRE_HORROR = 1;
        public static final int GENRE_DRAMA = 2;
        public static final int GENRE_TRAVEL = 3;
        public static final int GENRE_OTHER= 4;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

    }
}
