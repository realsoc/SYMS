package com.example.hugo.syms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hugo.syms.clientData.Notification;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {



    Context context;
    int layoutResourceId;
    List<Notification> data = null;

    public NotificationAdapter(Context context, int layoutResourceId, List<Notification> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((MainActivity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();



            // set circle bitmap
            holder.iconView = (ImageView) row.findViewById(R.id.notification_icon);
            holder.titleView = (TextView)row.findViewById(R.id.notification_title);
            holder.textView = (TextView)row.findViewById(R.id.notification_text);
            row.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        int tata =row.getResources().getIdentifier("com.example.hugo.syms:drawable/"+data.get(position).getIcon(), null,null);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                tata);
        Notification notification = data.get(position);
        holder.titleView.setText(notification.getTitle());
        holder.textView.setText(notification.getText());
        holder.iconView.setImageBitmap(Utils.getCircleBitmap(bm, context));
        //holder.iconView.setImageResource(row.getResources().getIdentifier("com.example.hugo.syms:drawable/"+notification.getIcon(),null,null));

        return row;
    }

    public static class ViewHolder
    {
        public ImageView iconView;
        public TextView titleView;
        public TextView textView;
    }
}