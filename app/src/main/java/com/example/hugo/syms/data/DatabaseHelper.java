package com.example.hugo.syms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Hugo on 23/12/2014.
 */
public class DatabaseHelper extends SQLiteAssetHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }


/*
    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Kids.DELETE_TABLE);
        db.execSQL(DatabaseContract.Notifications.DELETE_TABLE);
    }
*/

}