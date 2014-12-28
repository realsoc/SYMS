package com.example.hugo.syms.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hugo.syms.data.DatabaseContract.Kids;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 23/12/2014.
 */
public class KidDAO {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public KidDAO(Context context){
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void addKid(Kid kid) {
        ContentValues values = new ContentValues();
        values.put(Kids.COLUMN_NAME, kid.getName());
        values.put(Kids.COLUMN_NUMBER, kid.getNumber());
        values.put(Kids.COLUMN_PICTURE, kid.getPicture());
        kid.set_id(mDb.insert(Kids.TABLE_NAME, null, values));
    }

    public Kid getKid(long id) {
        Cursor cursor = mDb.query(Kids.TABLE_NAME, new String[] { Kids._ID,
                        Kids.COLUMN_NAME, Kids.COLUMN_NUMBER, Kids.COLUMN_PICTURE }, Kids._ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Kid kid = new Kid( Long.parseLong(cursor.getString(0)),cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
        cursor.close();
        return kid;
    }
    public void close(){
        mDb.close();
    }

    public List<Kid> getAllKids() {
        List<Kid> kidsList = new ArrayList<Kid>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Kids.TABLE_NAME;

        Cursor cursor = mDb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Kid kid = new Kid(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));

                kidsList.add(kid);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return kidsList;
    }

    // Getting contacts Count
    public int getKidsCount() {
        String countQuery = "SELECT  * FROM " + Kids.TABLE_NAME;
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int ret = cursor.getCount();
        cursor.close();

        return ret;
    }
    // Updating single contact
    public int updateKid(Kid kid) {
        ContentValues values = new ContentValues();
        values.put(Kids._ID, kid.get_id());
        values.put(Kids.COLUMN_NAME, kid.getName());
        values.put(Kids.COLUMN_NUMBER, kid.getNumber());
        values.put(Kids.COLUMN_PICTURE, kid.getPicture());

        // updating row
        int affectedCols = mDb.update(Kids.TABLE_NAME, values, Kids._ID + " = ?",
                new String[] { String.valueOf(kid.get_id()) });
        return affectedCols;
    }

    // Deleting single contact
    public void deleteKid(Kid kid) {
        mDb.delete(Kids.TABLE_NAME, Kids._ID + " = ?",
                new String[] { String.valueOf(kid.get_id()) });
    }
}
