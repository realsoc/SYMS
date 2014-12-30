package com.example.hugo.syms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.example.hugo.syms.clientData.Notification;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Hugo on 29/12/2014.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GcmBroadcastReceiver";
    private Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;

        PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();

        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

            String messageType = gcm.getMessageType(intent);
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.d(TAG, "Send error");

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d(TAG, "Deleted messages on server");

            } else {
                String title = intent.getStringExtra("title");
                String text = intent.getStringExtra("text");
                String icon = intent.getStringExtra("icon");
                Notification newNotif = new Notification("ic_contact_picture", "GCM", intent.getStringExtra("message"));
                Utils.showNotif(context, newNotif);
                Log.d(TAG, "Notif shown");


            }
            setResultCode(Activity.RESULT_OK);

        } finally {
            mWakeLock.release();
        }
    }
}