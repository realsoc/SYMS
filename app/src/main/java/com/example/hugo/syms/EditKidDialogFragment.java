package com.example.hugo.syms;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

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
    private Bitmap picture;
    private String name;
    private String number;
    private Date birthday;
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
        ImageView image = (ImageView) v.findViewById(R.id.kid_picture);
        image.setImageBitmap(picture);
        Spinner gender = (Spinner) v.findViewById(R.id.kid_gender);
        editName.setText(name);
        phone.setText(number);
        Button cancel = (Button) v.findViewById(R.id.cancel_kid);
        Button update = (Button) v.findViewById(R.id.update_kid);
        Button selectPicture = (Button) v.findViewById(R.id.select_picture);
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
        selectPicture.setOnClickListener(new View.OnClickListener() {
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
        Toast.makeText(getActivity(),"OnactivityresultKidEditFragment",Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode){
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    String pictureSrc = retrievePictureFromIntent(imageReturnedIntent);
                    picture = BitmapFactory.decodeFile(pictureSrc);
                }
                break;
        }
    }


    public static void setBus(Bus bus) {
        EditKidDialogFragment.bus = bus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String retrievePictureFromIntent(Intent data){
        Uri pictureUri = data.getData();
        Toast.makeText(getActivity(),pictureUri.toString(),Toast.LENGTH_SHORT).show();
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(pictureUri, projection, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String pictureSrc = cursor.getString(idx);
        Toast.makeText(getActivity(),pictureSrc, Toast.LENGTH_SHORT).show();
        return pictureSrc;
    }
    public void changeImage(){
        /*Toast.makeText(this,"changeImage",Toast.LENGTH_SHORT).show();
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);*/
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[] { takePhotoIntent }
                );

        startActivityForResult(chooserIntent, SELECT_PHOTO);
    }
    public void updateKid(){
        Toast.makeText(getActivity(),"updateKid",Toast.LENGTH_SHORT).show();
        kidsListFragment.onActivityResult(KID_EDIT_REQUEST,getActivity().RESULT_OK, new Intent().putExtra("name",name));
        getFragmentManager().beginTransaction().remove(this).commit();
    }
    public void cancelEditKid(){
        Toast.makeText(getActivity(),"cancelEditKid",Toast.LENGTH_SHORT).show();
    }
    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "Date");
    }
}
