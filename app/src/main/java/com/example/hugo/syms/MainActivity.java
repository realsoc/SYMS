package com.example.hugo.syms;


import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hugo.syms.clientData.Kid;
import com.example.hugo.syms.clientData.KidDAO;
import com.example.hugo.syms.clientData.Notification;
import com.example.hugo.syms.clientData.NotificationDAO;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity{


    private static final int PICK_CONTACT_REQUEST = 3;
    private NotificationAdapter notificationAdapter;
    private ListView listView;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private int width;
    private View header;
    private Vibrator v;

public MainActivity(){
    super();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        int position =0;
        if(savedInstanceState != null){
            position = savedInstanceState.getInt("position");
        }
        setContentView(R.layout.activity_main);
        if(!Utils.isMPhoneIn()){
            AddMPhoneNumber dialog = new AddMPhoneNumber();
            dialog.show(getSupportFragmentManager(), "Enter your phone number");
        }
        width =Utils.getWidthScreen(this);
        if(!Utils.getMPhoneNumber().equals("0000")){
            new GcmRegistrationAsyncTask(this).execute();
        }
        initPager(position);
    }

   public void initPager(int position){
       Utils.setKids(Utils.getKidDAO().getAllKids());
       Utils.setNotifications(Utils.getNotificationDAO().getAllNotifications());

       header = getLayoutInflater().inflate(R.layout.header_fragment,null, false);


       listView = (ListView) findViewById(R.id.listview_notifications);

       mPager = (ViewPager) header.findViewById(R.id.pager);

       notificationAdapter = new NotificationAdapter(this,R.layout.list_item_notifications,Utils.getNotifications());

       mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());


       mPager.setPageTransformer(true, new ZoomOutPageTransformer());
       mPager.setAdapter(mPagerAdapter);
       if(Utils.getKids() !=null){
           mPager.setCurrentItem(position);
       }
       setLayoutParams(header, width, 1143);

       listView.setDividerHeight(0);
       listView.addHeaderView(header,null,false);
       listView.setAdapter(notificationAdapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
               if(Utils.getKids().size()>0){
                   if(position == 1){
                       showDialogNewNotif();
                       //initPager(mPager.getCurrentItem());
                   }
                   else{
                       v.vibrate(200);
                       new SendNotifAsyncTask(getBaseContext(),getKid(),Utils.getNotifications().get(position-1)).execute();
                   }
               }

           }
       });
   }

    private void showDialogNewNotif() {
        NewNotifDialog dialog = new NewNotifDialog();
        dialog.show(getSupportFragmentManager(), "Enter your new Notif");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }if (id== R.id.action_add) {
            pickContact();
            return true;
        }if(id==R.id.action_delete){
            if(Utils.getKids().size()>0){
                Kid currentKid = Utils.getKids().get(mPager.getCurrentItem());
                removeKid(currentKid);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case PICK_CONTACT_REQUEST:
                if ( resultCode == RESULT_OK ) {
                    Kid newKid = retrieveKidFromIntent(data);
                    addKid(newKid);
                }
                break;
        }
    }

    public void pickContact(){
        Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    private Kid retrieveKidFromIntent(Intent data) {
        Uri pickedPhoneNumber = data.getData();
        Cursor cursor = getContentResolver()
                .query(pickedPhoneNumber, null, null, null, null);
        cursor.moveToNext();

        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
        cursor.close();
        return new Kid(name, phone);
    }

    private void setLayoutParams(View header, int width, int i) {
        AbsListView.LayoutParams headerViewParams = new AbsListView.LayoutParams(width, i);
        header.setLayoutParams(headerViewParams);
    }

    public Kid getKid(){
        int position = mPager.getCurrentItem();
        return Utils.getKids().get(position);
    }
    public void addKid(Kid newKid){
        Utils.getKidDAO().addKid(newKid);
        Utils.setKids(Utils.getKidDAO().getAllKids());
        refreshPager(false);
    }
    public void updateKid(Kid kid){
        Utils.getKidDAO().updateKid(kid);
        refreshPager(false);
    }
    public void removeKid(Kid kid){
        Utils.getKidDAO().deleteKid(kid);
        Utils.getKids().remove(kid);
        refreshPager(true);
    }

    public void refreshPager(boolean delete){
        int position = mPager.getCurrentItem();
        if(delete){
            position = 0;
        }
        mPager.setAdapter(null);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(position);

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
            kidView.setCurrentKid(Utils.getKids().get(position));
            return kidView;
        }

        @Override
        public int getCount() {
            return Utils.getKidDAO().getKidsCount();
        }
        @Override
        public String getTag(int position) {
            return Utils.getKids().get(position).getNumber();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mPager.getCurrentItem());
    }
}
