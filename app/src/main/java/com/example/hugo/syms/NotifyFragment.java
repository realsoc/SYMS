package com.example.hugo.syms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hugo.syms.data.DatabaseContract;
import com.example.hugo.syms.data.DatabaseHelper;
import com.example.hugo.syms.data.Notification;
import com.example.hugo.syms.data.NotificationDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Hugo on 26/12/2014.
 */
public class NotifyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final static int NOTIFICATION_LOADER = 0;
    private NotificationAdapter notificationAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        notificationAdapter = new NotificationAdapter(getActivity(),null,0);
        View rootView = inflater.inflate(R.layout.fragment_notify,container,false);
        ListView notificationsList = (ListView)rootView.findViewById(R.id.listview_notifications);
        notificationsList.setAdapter(notificationAdapter);
        /*NotificationDAO notificationDAO = new NotificationDAO(getActivity());
        List<Notification> notifications = notificationDAO.getAllNotifications();*/
        return rootView;
    }


    public static final class NotificationLoader extends SimpleCursorLoader {

        private NotificationDAO notificationDAO;

        public NotificationLoader(Context context) {
            super(context);
            Log.d("NOtFL", "constructor");
            notificationDAO = new NotificationDAO(context);
        }

        @Override
        public Cursor loadInBackground() {
            Log.d("NOtF", "loadinback");
            List<Notification> notifications = notificationDAO.getAllNotifications();
            for(Notification not : notifications){
                Log.d(not.getTitle() , not.getText());
            }
            Log.d("Notif", "done");
            return notificationDAO.getAllNotificationsCursor();
        }

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new NotificationLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        notificationAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notificationAdapter.swapCursor(null);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTIFICATION_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
            getLoaderManager().restartLoader(NOTIFICATION_LOADER, null, this);
    }

}

