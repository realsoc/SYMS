package com.example.hugo.syms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link } subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstOpenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstOpenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstOpenFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final int PICK_CONTACT_REQUEST = 1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FirstOpenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstOpenFragment newInstance() {
        FirstOpenFragment fragment = new FirstOpenFragment();
        return fragment;
    }

    public FirstOpenFragment() {
        // Required empty public constructor
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
                ChooseKidsFragment newFragment = new ChooseKidsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getActivity(),"KID",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),R.string.choose_mom,Toast.LENGTH_LONG).show();
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
