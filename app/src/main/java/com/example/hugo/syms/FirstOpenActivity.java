package com.example.hugo.syms;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.otto.Bus;


/**
 * A simple {@link } subclass.
 * Activities that contain this fragment must implement the
 */
public class FirstOpenActivity extends
 Activity {

    public FirstOpenActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_open);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

   public void onMomClick(View v){
       getIntent().putExtra("isMom", true);
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       SharedPreferences.Editor edit = sharedPreferences.edit();
       edit.putBoolean("neverLaunched", false);
       edit.apply();
       Intent returnIntent = new Intent();
       setResult(RESULT_OK,returnIntent);
       finish();
   }

    public void onKidClick(View v){
        getIntent().putExtra("isMom", false);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();

    }



}
