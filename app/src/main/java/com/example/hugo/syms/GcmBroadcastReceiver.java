package com.example.hugo.syms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.example.hugo.syms.clientData.Kid;
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
                String type = intent.getStringExtra(Constant.MESSAGE_TYPE);
                if(type.equals(Constant.CONTACT_NOT_FOUND)){
                    sendInvitationFromIntent(context, intent);
                    setResultCode(Activity.RESULT_OK);
                }else if(type.equals(Constant.MESSAGE)){
                    showNotifFromMessage(context,intent);
                    setResultCode(Activity.RESULT_OK);
                }


            }


        } finally {
            mWakeLock.release();
        }
    }

    private void sendInvitationFromIntent(Context context, Intent intent){
        String to = intent.getStringExtra(Constant.TO);
        Utils.sendInvitationTo(to);
    }
    private void showNotifFromMessage(Context context, Intent intent){
        String title = intent.getStringExtra(Constant.TITLE);
        String text = intent.getStringExtra(Constant.TEXT);
        String icon = intent.getStringExtra(Constant.ICON);
        String from = intent.getStringExtra(Constant.FROM);
        Notification newNotif = new Notification(icon, title, text);
        Kid fromKid = Utils.getKidByPhone(from);
        if(fromKid != null){
            from = fromKid.getName();
        }
        Utils.showNotif(context, newNotif, from);
    }
}