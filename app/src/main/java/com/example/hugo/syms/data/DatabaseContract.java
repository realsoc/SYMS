package com.example.hugo.syms.data;

import android.provider.BaseColumns;

/**
 * Created by Hugo on 23/12/2014.
 */
public final class DatabaseContract {
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "syms.db";
    public static final String DATABASE_PATH      = "/data/data/com.example.hugo.syms/assets/databases/";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String NOT_NULL_TEXT_TYPE = " TEXT NOT NULL";
    private static final String INT_TYPE           = " INT";
    private static final String NOT_NULL_INT_TYPE  = " INT NOT NULL";
    private static final String COMMA_SEP          = ",";

    public DatabaseContract() {}


public static abstract class Notifications implements BaseColumns{
    public static final String TABLE_NAME ="notifications";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_TYPE = "type";

    public static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ICON + NOT_NULL_INT_TYPE + COMMA_SEP +
            COLUMN_TITLE + NOT_NULL_TEXT_TYPE + COMMA_SEP +
            COLUMN_TEXT + NOT_NULL_TEXT_TYPE + COMMA_SEP +
            COLUMN_TYPE + TEXT_TYPE +" );";
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}

    public static abstract class Kids implements BaseColumns {
        public static final String TABLE_NAME       = "kids";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_PICTURE = "picture";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + NOT_NULL_TEXT_TYPE + COMMA_SEP +
                COLUMN_NUMBER + NOT_NULL_TEXT_TYPE + COMMA_SEP +
                COLUMN_PICTURE + TEXT_TYPE +  " );";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
