package com.example.hugo.syms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


public class MainActivity extends ActionBarActivity{

    private static Bus bus;
    private static final int FIRST_LAUNCH_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean neverLaunched = sharedPreferences.getBoolean("neverLaunched", true);
        bus = new Bus(ThreadEnforcer.ANY);
        bus.register(this);
        if (savedInstanceState == null) {
            if (neverLaunched) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                Intent intent = new Intent(this, FirstOpenActivity.class);
                startActivityForResult(intent,FIRST_LAUNCH_REQUEST);
            } else {
                Log.d("TOTO", "JE CROIS QUIE FUCK");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case FIRST_LAUNCH_REQUEST:
                if(resultCode == RESULT_OK){

                }
                break;

        }
    }
}
