package com.example.hugo.syms;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Hugo on 09/12/2014.
 */
public class EditKidDialogFragment extends Fragment {

    private static final int KID_EDIT_REQUEST = 5;
    private static final int SELECT_PHOTO = 7;
    private static Bus bus;
    int mNum;
    private Kid currentKid;
    private String title;
    private ImageView pictureView;
    private static KidsListFragment kidsListFragment;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static EditKidDialogFragment newInstance(Kid kid, KidsListFragment kidsList) {
        EditKidDialogFragment f = new EditKidDialogFragment();
        f.setKidsListFragment(kidsList);
        return f;
    }


    public EditKidDialogFragment(){
        bus = BusProvider.getInstance();
        bus.register(this);//getString(R.string.title_edit_kids);
    }

    public void setKidsListFragment(KidsListFragment kidsListFragment) {
        EditKidDialogFragment.kidsListFragment = kidsListFragment;
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
        Spinner gender = (Spinner) v.findViewById(R.id.kid_gender);
        InputStream picture = currentKid.getPicture();
        if(picture != null){
            //picture = BitmapFactory.decode
            pictureView.setImageBitmap(BitmapFactory.decodeStream(currentKid.getPicture()));
        }
        pictureView = (ImageView) v.findViewById(R.id.kid_picture);
        editName.setText(currentKid.getName());
        phone.setText(currentKid.getNumber());

        Button cancel = (Button) v.findViewById(R.id.cancel_kid);
        pictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Button update = (Button) v.findViewById(R.id.update_kid);
        TextView birthday = (TextView) v.findViewById(R.id.choose_birthday_kid);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEditKid();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKid();
            }
        });
        pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode){
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK) {
                    currentKid.setPicture(retrievePictureFromIntent(imageReturnedIntent));
                    //pictureView.setImageBitmap(BitmapFactory.decodeFile(currentKid.getPicture()));
                }
                break;
        }
    }
    public static void setBus(Bus bus) {
        EditKidDialogFragment.bus = bus;
    }



    public InputStream retrievePictureFromIntent(Intent data){


        InputStream imagePath = null;

        if(data != null){

            Uri pictureUri = data.getData();
            try {
                Cursor cursor = getActivity().getContentResolver().query(pictureUri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                cursor.moveToFirst();
                cursor.close();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(pictureUri);
                pictureView.setImageBitmap(BitmapFactory.decodeStream(imageStream));

            }  catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imagePath;

    }
    public void changeImage() {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(pickIntent, SELECT_PHOTO);
    }


    public void updateKid(){
        Toast.makeText(getActivity(),"updateKid",Toast.LENGTH_SHORT).show();
        kidsListFragment.onActivityResult(
                KID_EDIT_REQUEST,
                getActivity().RESULT_OK,
                new Intent().putExtra("name", currentKid.getName())
        );
        getFragmentManager().beginTransaction().remove(this).commit();
    }
    public void cancelEditKid(){
        Toast.makeText(getActivity(),"cancelEditKid",Toast.LENGTH_SHORT).show();
    }
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setKid(currentKid);
        newFragment.show(getActivity().getSupportFragmentManager(), "Date");
    }

    public void setCurrentKid(Kid currentKid) {
        this.currentKid = currentKid;
    }
}
