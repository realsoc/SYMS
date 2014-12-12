package com.example.hugo.syms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Hugo on 09/12/2014.
 */
public class EditKidDialogFragment extends Fragment {

    private static final int SELECT_PHOTO = 100;
    private static Bus bus;
    int mNum;
    private Kid currentKid;
    private String title;
    private Bitmap picture;
    private String name;
    private String number;
    private Date birthday;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static EditKidDialogFragment newInstance(int mNum, Kid kid) {
        EditKidDialogFragment f = new EditKidDialogFragment();
        return f;
    }


    public EditKidDialogFragment(){
        bus = BusProvider.getInstance();
        bus.register(this);//getString(R.string.title_edit_kids);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_edit_kid, container, false);
         EditText editName = (EditText) v.findViewById(R.id.kid_name);
        TextView phone = (TextView) v.findViewById(R.id.kid_phone);
        ImageView image = (ImageView) v.findViewById(R.id.kid_picture);
        image.setImageBitmap(picture);
        Toast.makeText(getActivity(),"ONCREATEVIEW",Toast.LENGTH_SHORT).show();
        Spinner gender = (Spinner) v.findViewById(R.id.kid_gender);
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


    public static void setBus(Bus bus) {
        EditKidDialogFragment.bus = bus;
    }

}
