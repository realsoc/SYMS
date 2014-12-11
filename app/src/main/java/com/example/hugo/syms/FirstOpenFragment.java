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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.otto.Bus;


/**
 * A simple {@link } subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstOpenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstOpenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstOpenFragment extends Fragment{


    private static Bus bus;
    private static final int PICK_CONTACT_REQUEST = 1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FirstOpenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstOpenFragment newInstance(Bus bus) {
        FirstOpenFragment fragment = new FirstOpenFragment();
        setBus(bus);

        return fragment;
    }

    public FirstOpenFragment() {
        bus.register(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first_open, container, false);
        Button mom = (Button) rootView.findViewById(R.id.mom);
        Button kid = (Button) rootView.findViewById(R.id.kid);
        mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KidsListFragment newFragment = KidsListFragment.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getActivity(),R.string.choose_mom,Toast.LENGTH_LONG).show();
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PICK_CONTACT_REQUEST:
                if(resultCode == getActivity().RESULT_OK){
                    Uri contactUri = data.getData();
                    // We only need the NUMBER column, because there will be only one row in the result
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                    // Perform the query on the contact to get the NUMBER column
                    // We don't need a selection or sort order (there's only one result for the given URI)
                    // CAUTION: The query() method should be called from a separate thread to avoid blocking
                    // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                    // Consider using CursorLoader to perform the query.
                    Cursor cursor = getActivity().getContentResolver()
                            .query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();

                    // Retrieve the phone number from the NUMBER column
                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(column);


                    Toast.makeText(getActivity(), number, Toast.LENGTH_SHORT).show();

                }
        }
    }



    public void showEditKidDialog(Kid kid) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = EditKidDialogFragment.newInstance(1, kid, bus);
        dialog.show(getActivity().getSupportFragmentManager(), "EditKidDialogFragment");
    }

    public static void setBus(Bus bus) {
        FirstOpenFragment.bus = bus;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
