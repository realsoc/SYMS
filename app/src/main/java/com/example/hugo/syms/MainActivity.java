package com.example.hugo.syms;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.hugo.syms.clientData.Kid;
import com.example.hugo.syms.clientData.KidDAO;
import com.example.hugo.syms.clientData.Notification;
import com.example.hugo.syms.clientData.NotificationDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity{


    private static final int PICK_CONTACT_REQUEST = 3;
    private NotificationAdapter notificationAdapter;
    private KidDAO kidDAO;
    private NotificationDAO notificationDAO;
    private List<Kid> kids;
    private List<Notification> notifications;
    private ListView listView;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private int width;
    private View header;

public MainActivity(){
    super();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position =0;
        if(savedInstanceState != null){
            position = savedInstanceState.getInt("position");
        }
        setContentView(R.layout.activity_main);
        if(!Utils.isMPhoneIn()){
            AddMPhoneNumber dialog = new AddMPhoneNumber();
            dialog.show(getSupportFragmentManager(), "Enter your phone number");
        }
        kidDAO = KidDAO.getInstance(this);
        notificationDAO = NotificationDAO.getInstance(this);
        width =Utils.getWidthScreen(this);
        new GcmRegistrationAsyncTask(this).execute();
        initPager(position);
    }

   public void initPager(int position){

       kids = kidDAO.getAllKids();
       notifications = notificationDAO.getAllNotifications();

       header = getLayoutInflater().inflate(R.layout.header_fragment, null, false);

       listView = (ListView) findViewById(R.id.listview_notifications);

       mPager = (ViewPager) header.findViewById(R.id.pager);

       notificationAdapter = new NotificationAdapter(this,R.layout.list_item_notifications,notifications);

       mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

       listView.setAdapter(notificationAdapter);
       mPager.setPageTransformer(true, new ZoomOutPageTransformer());
       mPager.setAdapter(mPagerAdapter);
       if(kids !=null){
           mPager.setCurrentItem(position);
       }
       setLayoutParams(header, width, 1143);

       listView.addHeaderView(header,null,false);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
               if(position == 1){
                   showDialogNewNotif();
               }
               else{
                   Utils.showNotif(MainActivity.this, notifications.get(position-1));
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
            if(kids.size()>0){
                Kid currentKid = kids.get(mPager.getCurrentItem());
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

    public void addKid(Kid newKid){
        kidDAO.addKid(newKid);
        this.kids = kidDAO.getAllKids();
        refreshPager(false);
    }
    public void updateKid(Kid kid){
        kidDAO.updateKid(kid);
        refreshPager(false);
    }
    public void removeKid(Kid kid){
        kidDAO.deleteKid(kid);
        kids.remove(kid);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mPager.getCurrentItem());
    }
}
