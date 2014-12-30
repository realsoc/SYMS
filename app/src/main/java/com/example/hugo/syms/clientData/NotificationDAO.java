package com.example.hugo.syms.clientData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 26/12/2014.
 */
public class NotificationDAO {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static NotificationDAO instance;

    private NotificationDAO(Context context){
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public static NotificationDAO getInstance(Context context) {
        if(instance == null){
            instance = new NotificationDAO(context);
        }
        return instance;
    }

    public void addNotification(Notification notification) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Notifications.COLUMN_ICON, notification.getIcon());
        values.put(DatabaseContract.Notifications.COLUMN_TITLE, notification.getTitle());
        values.put(DatabaseContract.Notifications.COLUMN_TEXT, notification.getText());
        values.put(DatabaseContract.Notifications.COLUMN_TYPE, notification.getType());
        notification.set_id(mDb.insert(DatabaseContract.Notifications.TABLE_NAME, null, values));
    }

    public Notification getNotification(long id) {
        Cursor cursor = mDb.query(DatabaseContract.Notifications.TABLE_NAME, new String[] { DatabaseContract.Notifications._ID,
                        DatabaseContract.Notifications.COLUMN_ICON, DatabaseContract.Notifications.COLUMN_TITLE, DatabaseContract.Notifications.COLUMN_TEXT }, DatabaseContract.Notifications._ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Notification notification = new Notification( Long.parseLong(cursor.getString(0)),cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));
        cursor.close();
        return notification;
    }
    public void close(){
        mDb.close();
    }

    public Cursor getAllNotificationsCursor(){

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseContract.Notifications.TABLE_NAME;

        return mDb.rawQuery(selectQuery, null);
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notificationsList = new ArrayList<Notification>();
        Cursor cursor = getAllNotificationsCursor();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                notificationsList.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notificationsList;
    }

    // Getting contacts Count
    public int getNotificationsCount() {
        String countQuery = "SELECT  * FROM " + DatabaseContract.Notifications.TABLE_NAME;
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int ret = cursor.getCount();
        cursor.close();

        return ret;
    }
    // Updating single contact
    public int updateNotification(Notification notification) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Notifications._ID, notification.get_id());
        values.put(DatabaseContract.Notifications.COLUMN_ICON, notification.getIcon());
        values.put(DatabaseContract.Notifications.COLUMN_TITLE, notification.getTitle());
        values.put(DatabaseContract.Notifications.COLUMN_TEXT, notification.getText());
        values.put(DatabaseContract.Notifications.COLUMN_TYPE, notification.getType());

        // updating row
        int affectedCols = mDb.update(DatabaseContract.Notifications.TABLE_NAME, values, DatabaseContract.Notifications._ID + " = ?",
                new String[] { String.valueOf(notification.get_id()) });
        return affectedCols;
    }

    // Deleting single contact
    public void deleteNotification(Notification notification) {
        mDb.delete(DatabaseContract.Notifications.TABLE_NAME, DatabaseContract.Notifications._ID + " = ?",
                new String[] { String.valueOf(notification.get_id()) });
    }
}
