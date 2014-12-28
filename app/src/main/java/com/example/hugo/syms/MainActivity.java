package com.example.hugo.syms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hugo.syms.data.Kid;
import com.example.hugo.syms.data.KidDAO;
import com.example.hugo.syms.data.Notification;
import com.example.hugo.syms.data.NotificationDAO;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity{

    private static final int FIRST_LAUNCH_REQUEST = 1;
    private static final int PICK_CONTACT_REQUEST = 3;
    private NotificationAdapter notificationAdapter;
    private KidDAO kidDAO;
    private List<Kid> kids;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

public MainActivity(){
    super();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean neverLaunched = sharedPreferences.getBoolean("neverLaunched", true);
        kidDAO = new KidDAO(this);
            if (neverLaunched) {
                setContentView(R.layout.activity_main);
                Intent intent = new Intent(this, FirstOpenActivity.class);
                startActivityForResult(intent,FIRST_LAUNCH_REQUEST);
            } else {
                setContentView(R.layout.activity_main);
                initPager();
            }

    }
   public void initPager(){
        kids = kidDAO.getAllKids();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        NotifyFragment myf = new NotifyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.notif_zone, myf);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case FIRST_LAUNCH_REQUEST:
                if(resultCode == RESULT_OK){
                    initPager();
                }
                break;
            case PICK_CONTACT_REQUEST:
                if ( resultCode == RESULT_OK ) {
                    Uri pickedPhoneNumber = data.getData();
                    Cursor cursor = getContentResolver()
                            .query(pickedPhoneNumber, null, null, null, null);
                    cursor.moveToNext();

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    cursor.close();
                    Kid newKid = new Kid(name, phone);
                    kidDAO.addKid(newKid);
                    this.kids = kidDAO.getAllKids();
                    mPagerAdapter.notifyDataSetChanged();

                }
                break;
        }
    }


    public void removeKid(Kid kid){
        kidDAO.deleteKid(kid);
        kids.remove(kid);
        refreshPager();

    }
    public void refreshPager(){
        mPager.setAdapter(null);
        mPager.setAdapter(mPagerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }if (id== R.id.action_add) {

            Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            return true;
        }if(id==R.id.action_delete){
            if(kids.size()>0){
                Kid currentKid = kids.get(mPager.getCurrentItem());
                Toast.makeText(this, currentKid.getName(),Toast.LENGTH_SHORT).show();
                removeKid(currentKid);
            }

        }

        return super.onOptionsItemSelected(item);
    }





    public void updateKid(Kid kid){
        kidDAO.updateKid(kid);
        refreshPager();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapterFixed {
        private ArrayList<String> mSavedFragmentTags = new ArrayList<String>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            KidViewFragment kidView = new KidViewFragment();
            kidView.setCurrentKid(kids.get(position));
            return kidView;
        }

        @Override
        public int getCount() {
            return kidDAO.getKidsCount();
        }
        @Override
        public String getTag(int position) {
            return kids.get(position).getNumber();
        }




    }

}
