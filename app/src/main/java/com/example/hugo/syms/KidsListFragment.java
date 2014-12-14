package com.example.hugo.syms;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link KidsListFragment#getInstance} method to
 * get the instance of this fragment.
 */
public class KidsListFragment extends Fragment {

    private static KidsListFragment instance = null;
    private ArrayList<Kid> kids;
    private KidsAdapter adapter;
    private Bus bus;
    private static final int KID_EDIT_REQUEST = 5;
    private static final int KID_CONTACT_REQUEST = 3;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ChooseKidsFragment.
     */
    public static KidsListFragment getInstance() {
        if(instance == null){
            instance = new KidsListFragment();
        }
        return instance;
    }

    public KidsListFragment() {
        kids = new ArrayList<Kid>();
        bus = BusProvider.getInstance();
        bus.register(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_kids_list, container,false);
        ListView listView = (ListView) rootView.findViewById(R.id.kid_list);
        adapter = new KidsAdapter(getActivity(),kids);
        listView.setAdapter(adapter);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("ListKid", "Createoptionmenu");
        inflater.inflate(R.menu.choosekidsfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add:
                Toast.makeText(getActivity(),"ADD", Toast.LENGTH_SHORT).show();
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(pickContactIntent, KID_CONTACT_REQUEST);
        }
        return true;
    }
    /*@Subscribe
    public void answerAvailable(UserFromDialogMessage message) {
        Kid kiddo = message.getmKid();
        boolean canceled = message.isCanceled();
        int index = kiddo.getIndexInArrayList(kids);
        if(index == -1){
            if(!canceled){
                kids.add(kiddo);
            }
        } else{
            if(!canceled){
                kids.remove(index);
                kids.add(kiddo);
            }
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(),"OnactivityresultKidlistFragment",Toast.LENGTH_SHORT);
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case KID_CONTACT_REQUEST:
                if(resultCode == getActivity().RESULT_OK){
                    Kid newKid = retrieveKidFromIntent(data);
                    showEditKidDialog(newKid);
                }
                break;
            case KID_EDIT_REQUEST:

                break;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bus.unregister(this);
    }

    public int getKidIndex(Kid kid){
        int i = 0;
        boolean found = false;
        for (Kid currentKid :kids){
            if (kid.getNumber().equals(currentKid.getNumber())){
                found = true;
                break;
            }
            i++;
        }
        if(!found){
            i = -1;
        }
        return i;
    }

    public void kidUpdate(Kid kid){
        int index = getKidIndex(kid);
        if(index != -1){
            kids.remove(index);
        }
        kids.add(kid);
    }

    public void showEditKidDialog(Kid kid) {

        EditKidDialogFragment fragment = EditKidDialogFragment.newInstance(kid, this);
        fragment.setCurrentKid(kid);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_first_open, fragment, "EditKidFragment");
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public Kid retrieveKidFromIntent(Intent data){
        Kid retKid = null;
        Uri dataUri = data.getData();
        String id = retrieveIdFromUri(dataUri);
        InputStream picture = retrieveContactPhoto(dataUri, id);
        String name = retrieveContactName(dataUri);
        String number = retrieveContactNumber(dataUri, id);
        if(picture == null){
            retKid = new Kid(name,number);
            Toast.makeText(getActivity(),"PICTURE NOT FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
                    retKid = new Kid(name,number,picture);
        }
        return retKid;

    }
    public String retrieveNumberFromIntent(Uri dataUri){
        String number = null;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER};
        Cursor cursor = getActivity().getContentResolver()
                .query(dataUri, projection, null, null, null);
        if(cursor.moveToFirst()){
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
            number = cursor.getString(column);
        }
       return number;
    }

    public String retrieveNameFromIntent(Uri dataUri){
        String name = null;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver()
                .query(dataUri, projection, null, null, null);
        if(cursor.moveToFirst()){
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            name = cursor.getString(column);
        }
        return name;
    }
    public String retrieveIdFromUri(Uri dataUri){
        String id = null;
        Cursor cursor = getActivity().getContentResolver().query(dataUri,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        return id;
    }
    private InputStream retrieveContactPhoto(Uri uri, String id) {

        InputStream photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

            if (inputStream != null) {
                photo = inputStream;
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    private String retrieveContactNumber(Uri uri, String id) {

        String contactNumber = null;
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{id},
        null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        return contactNumber;
    }

    private String retrieveContactName(Uri uri) {
        String contactName = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return contactName;
    }


}
