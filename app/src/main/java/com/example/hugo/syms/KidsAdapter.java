package com.example.hugo.syms;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hugo on 09/12/2014.
 */
public class KidsAdapter extends ArrayAdapter<Kid> {
    private final Context context;
    private final ArrayList<Kid> kids;

    public KidsAdapter(Context context,ArrayList<Kid> kids) {
        super(context,R.layout.list_item_kids, kids);
        this.context = context;
        this.kids = kids;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_kids, parent, false);
        ImageView picture = (ImageView) rowView.findViewById(R.id.kid_picture);
        TextView name = (TextView) rowView.findViewById(R.id.kid_name);
        TextView phone  = (TextView) rowView.findViewById(R.id.kid_phone);

        phone.setText(kids.get(position).getNumber());
        name.setText(kids.get(position).getName());
        picture.setImageBitmap(kids.get(position).getPicture());
        // change the icon for Windows and iPhone
        return rowView;
    }

}
