package com.example.hugo.syms;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
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
                    kidSelected(newKid);
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

    public void kidSelected(Kid kid){
        Bundle bundle = new Bundle();
        Toast.makeText(getActivity(),"kidSelected",Toast.LENGTH_SHORT).show();
        bundle.putString("name",kid.getName());
        bundle.putString("number", kid.getNumber());
        Toast.makeText(getActivity(),"name "+kid.getName()+" number "+kid.getNumber(),Toast.LENGTH_SHORT).show();
        showEditKidDialog(kid);
    }
    public void showEditKidDialog(Kid kid) {

        EditKidDialogFragment fragment = EditKidDialogFragment.newInstance(kid, this);
        fragment.setName(kid.getName());
        fragment.setNumber(kid.getNumber());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_first_open, fragment, "EditKidFragment");
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public Kid retrieveKidFromIntent(Intent data){
        Uri contactUri = data.getData();
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver()
                .query(contactUri, projection, null, null, null);
        cursor.moveToFirst();
        int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
        String number = cursor.getString(column);
        column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        String name = cursor.getString(column);
        return new Kid(name, number);
    }


}
