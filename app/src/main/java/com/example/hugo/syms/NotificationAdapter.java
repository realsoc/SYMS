package com.example.hugo.syms;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hugo.syms.data.DatabaseContract;

public class NotificationAdapter extends CursorAdapter {



    public static class ViewHolder {

        public final ImageView iconView;
        public final TextView titleView;
        public final TextView textView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.notification_icon);
            titleView = (TextView) view.findViewById(R.id.notification_title);
            textView = (TextView) view.findViewById(R.id.notification_text);
        }
    }

    public NotificationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notifications, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        getItemViewType(cursor.getPosition());
        int intIcon = cursor.getColumnIndex(DatabaseContract.Notifications.COLUMN_ICON);
        int intTitle = cursor.getColumnIndex(DatabaseContract.Notifications.COLUMN_TITLE);
        int intText = cursor.getColumnIndex(DatabaseContract.Notifications.COLUMN_TEXT);
        viewHolder.iconView.setImageResource(cursor.getInt(intIcon));
        viewHolder.titleView.setText(cursor.getString(intTitle));
        viewHolder.textView.setText(cursor.getString(intText));
    }

}