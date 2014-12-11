package com.example.hugo.syms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Hugo on 09/12/2014.
 */
public class EditKidDialogFragment extends DialogFragment {

    private static final int SELECT_PHOTO = 100;
    private static Bus bus;
    int mNum;
    private Kid currentKid;
    private String title = getString(R.string.title_edit_kids);
    private Bitmap picture;
    private String name;
    private String number;
    private Date birthday;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static EditKidDialogFragment newInstance(int mNum, Kid kid, Bus bus) {
        EditKidDialogFragment f = new EditKidDialogFragment();

        // Supply num input as an argument.
        setBus(bus);
        Bundle args = new Bundle();
        args.putString("name", kid.getName());
        args.putString("number", kid.getNumber());
        f.setArguments(args);
        return f;
    }

    public EditKidDialogFragment(){
        bus.register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        number = getArguments().getString("number");


        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_edit_kid, container, false);
         EditText editName = (EditText) v.findViewById(R.id.kid_name);
        TextView phone = (TextView) v.findViewById(R.id.kid_phone);
        ImageView image = (ImageView) v.findViewById(R.id.kid_picture);
        image.setImageBitmap(picture);
        Spinner gender = (Spinner) v.findViewById(R.id.kid_gender);
        DatePicker editBirthday = (DatePicker) v.findViewById(R.id.kid_birthday);
        // Watch for button clicks.
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        picture = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("coucoubuilderoncreatedialog")
                .setPositiveButton("good", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        bus.post(new BusMessage(currentKid,false));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("zap", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        bus.post(new BusMessage(currentKid,true));
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public static void setBus(Bus bus) {
        EditKidDialogFragment.bus = bus;
    }
}
