package com.example.hugo.syms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Hugo on 13/12/2014.
 */
public class EditMomFragment extends Fragment {

    private static EditMomFragment instance = null;
    private static final int MOM_CONTACT_REQUEST = 1;

    public static EditMomFragment getInstance() {
        if(instance == null){
            instance = new EditMomFragment();
        }
        return instance;
    }
    public EditMomFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_edit_kid,container);
        selectMom();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case MOM_CONTACT_REQUEST:
                // Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
                if(resultCode == getActivity().RESULT_OK){
                    Toast.makeText(getActivity(), "Momselected", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
    public void selectMom(){
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, MOM_CONTACT_REQUEST);
    }
}
