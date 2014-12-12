package com.example.hugo.syms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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


    private static final int KID_EDIT_REQUEST = 5;
    private static Bus bus;
    private KidsListFragment kidsListFragment;
    private static final int MOM_CONTACT_REQUEST = 1;
    private static final int KID_CONTACT_REQUEST = 3;


    public FirstOpenActivity() {
        bus = BusProvider.getInstance();
        bus.register(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_open);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_first_open, new ChooseSideFragment());
        ft.commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this,String.valueOf(requestCode),Toast.LENGTH_SHORT).show();
        switch(requestCode){
            case MOM_CONTACT_REQUEST:
               // Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
                if(resultCode == RESULT_OK){

                }
                break;
            case KID_CONTACT_REQUEST:
                Toast.makeText(this,"KID_CONTACT_REQUEST", Toast.LENGTH_SHORT).show();
                if(resultCode == RESULT_OK){
                    Toast.makeText(this,"TOTO1",Toast.LENGTH_SHORT).show();
                    Kid newKid = retrieveKidFromIntent(data);
                    kidsListFragment.kidSelected(newKid);
                }
                break;
            case KID_EDIT_REQUEST:
                break;
        }
    }
    public Kid retrieveKidFromIntent(Intent data){
        Uri contactUri = data.getData();
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor cursor = getContentResolver()
                .query(contactUri, projection, null, null, null);
        cursor.moveToFirst();
        int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
        String number = cursor.getString(column);
        column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        String name = cursor.getString(column);
        return new Kid(name, number);
    }
    public void onKidClick(View v){
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, MOM_CONTACT_REQUEST);
    }
    public void onMomClick(View v){
        kidsListFragment = KidsListFragment.newInstance();
        showKidsListFragment();
    }

    public void showKidsListFragment(){
        if(kidsListFragment!= null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_first_open, kidsListFragment);
            transaction.commit();
        }
    }

    public KidsListFragment getKidsListFragment() {
        return kidsListFragment;
    }

    public void setKidsListFragment(KidsListFragment kidsListFragment) {
        this.kidsListFragment = kidsListFragment;
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Date");
    }

}
