package com.example.hugo.syms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hugo.syms.data.Kid;
import com.shamanland.fab.FloatingActionButton;

/**
 * Created by Hugo on 21/12/2014.
 */
public class KidViewFragment extends Fragment {
    private Kid currentKid;
    private static final int SELECT_PHOTO = 7;
    private boolean isModified = false;
    private ImageView kidPicture;
    private EditText kidNameEdit;
    private TextView kidName;
    private OnOffHScrollView pictureHScroll;
    private OnOffVScrollView pictureVScroll;
    private FloatingActionButton kidModify;
    private GestureDetector gestureDetector;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_kid, container, false);
        kidModify = (FloatingActionButton) rootView.findViewById(R.id.kid_modify);
        TextView kidNumber = (TextView) rootView.findViewById(R.id.kid_phone);

        kidNameEdit = (EditText) rootView.findViewById(R.id.kid_name_edit);
        kidName = (TextView) rootView.findViewById(R.id.kid_name);
        kidPicture = (ImageView) rootView.findViewById(R.id.kid_picture);
        pictureHScroll = (OnOffHScrollView) rootView.findViewById(R.id.kid_picture_scrollH);
        pictureVScroll = (OnOffVScrollView) rootView.findViewById(R.id.kid_picture_scrollV);
        gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());

        pictureVScroll.setEnableScrolling(true);
        pictureHScroll.setEnableScrolling(true);
        if ((savedInstanceState != null) && (savedInstanceState.getParcelable("kid") != null)) {
            currentKid = (Kid) savedInstanceState.getParcelable("kid");
        }
        if(currentKid !=null){
            kidName.setText(currentKid.getName());
            kidNumber.setText(currentKid.getNumber());
            if(currentKid.getPicture() !=null){
                Bitmap bMap = BitmapFactory.decodeFile(currentKid.getPicture());
                kidPicture.setImageBitmap(bMap);
            }

        }


        kidModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isModified){
                    editKidOk();
                }else{
                    editKid();
                }
            }
        });
        kidPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isModified){
                    changeImage();
                }
            }
        });
        return rootView;
    }

    public void changeImage() {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(pickIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri photoUri = imageReturnedIntent.getData();
                    if (photoUri != null) {
                        try {
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getActivity().getContentResolver().query(photoUri, filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String filePath = cursor.getString(columnIndex);
                            cursor.close();
                            if(filePath != null ){
                                currentKid.setPicture(filePath);
                                Bitmap bMap = BitmapFactory.decodeFile(filePath);
                                kidPicture.setImageBitmap(bMap);
                                ((MainActivity)getActivity()).updateKid(currentKid);
                            }
                        } catch (Exception e) {
                        }
                    }
                    break;
                }
        }
    }

    public void editKid(){
        pictureHScroll.setEnableScrolling(true);
        pictureVScroll.setEnableScrolling(true);
        kidNameEdit.setHint(kidName.getText());
        kidNameEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        kidName.setVisibility(View.INVISIBLE);
        kidNameEdit.setVisibility(View.VISIBLE);
        kidModify.setImageResource(R.drawable.ic_action_accept);
        isModified = true;
    }

    public void editKidOk(){
        pictureHScroll.setEnableScrolling(false);
        pictureVScroll.setEnableScrolling(false);
        if(!kidNameEdit.getText().toString().equals("")){
            currentKid.setName(kidNameEdit.getText().toString());
            kidNameEdit.setText("");
        }
        kidNameEdit.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(kidNameEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        kidName.setVisibility(View.VISIBLE);
        kidModify.setImageResource(R.drawable.ic_action_edit);
        ((MainActivity)getActivity()).updateKid(currentKid);
        isModified = false;
    }

    public Kid getCurrentKid() {
        return currentKid;
    }

    public void setCurrentKid(Kid currentKid) {
        this.currentKid = currentKid;
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("kid", currentKid);
    }
}