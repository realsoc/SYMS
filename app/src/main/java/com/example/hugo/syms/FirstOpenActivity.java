package com.example.hugo.syms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
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
public class FirstOpenActivity extends ActionBarActivity{


    private final KidsListFragment kidsListFragment;
    private final EditMomFragment editMomFragment;



    public FirstOpenActivity() {
        kidsListFragment = KidsListFragment.getInstance();
        editMomFragment = EditMomFragment.getInstance();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_open);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_first_open, new ChooseSideFragment());
        ft.commit();
    }



    public void onKidClick(View v){
        showMomEditFragment();
    }
    public void onMomClick(View v){
        showKidsListFragment();
    }

    public void showMomEditFragment(){
        if (editMomFragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_first_open, editMomFragment);
            transaction.commit();
        }
    }
    public void showKidsListFragment(){
        if(kidsListFragment!= null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_first_open, kidsListFragment);
            transaction.commit();
        }
    }



}
