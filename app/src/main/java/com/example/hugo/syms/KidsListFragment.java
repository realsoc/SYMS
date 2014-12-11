package com.example.hugo.syms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
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
 * Use the {@link KidsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KidsListFragment extends Fragment {

    private ArrayList<Kid> kids;
    private KidsAdapter adapter;
    private Bus bus;
    private static final int PICK_CONTACT_REQUEST = 1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ChooseKidsFragment.
     */
    public static KidsListFragment newInstance() {
        KidsListFragment fragment = new KidsListFragment();
        return fragment;
    }

    public KidsListFragment() {
        kids = new ArrayList<Kid>();
        bus = ((MainActivity)getActivity()).getBus();
        bus.register(this);
        adapter = new KidsAdapter(getActivity(),kids);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_kids_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_item);
        listView.setAdapter(adapter);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.choosekidsfragment, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PICK_CONTACT_REQUEST:
                if(resultCode == getActivity().RESULT_OK){
                    Uri contactUri = data.getData();
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor cursor = getActivity().getContentResolver()
                            .query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();

                    // Retrieve the phone number from the NUMBER column
                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(column);
                    column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID);
                    String name = cursor.getString(column);
                    Kid newKid = new Kid(name, number);
                    if(!kids.contains(newKid)){
                        EditKidDialogFragment newFragment = EditKidDialogFragment.newInstance(1,newKid, bus);
                        newFragment.show(getActivity().getSupportFragmentManager(), "toto");
                    }else{
                        Toast.makeText(getActivity(),"Kid already in the list !", Toast.LENGTH_SHORT).show();
                    }

                }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add:
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

        }
        return true;
    }
    @Subscribe
    public void answerAvailable(BusMessage message) {
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
    }
}
