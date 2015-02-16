package com.example.hugo.syms;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.hugo.syms.clientData.Kid;
import com.example.hugo.syms.clientData.KidDAO;
import com.example.hugo.syms.clientData.Notification;
import com.example.hugo.syms.clientData.NotificationDAO;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.List;

/**
 * Created by Hugo on 28/12/2014.
 */
public class Utils extends Application  {
    private static final int NOTIFICATION_ID = 12;
    private static SharedPreferences prefs;
    private static Context context;
    private  static KidDAO kidDAO;
    private static NotificationDAO notificationDAO;
    private static List<Kid> kids;
    private static List<Notification> notifications;


    public static void setKids(List<Kid> kids) {
        Utils.kids = kids;
    }
    public static void addKid(Kid kid){

    }
    public static void addNotification(Notification notification){

    }
    public static void removeKid(Kid kid){

    }
    public static void removeNotification(Notification notification){

    }

    public static List<Kid> getKids() {
        return kids;
    }

    public static List<Notification> getNotifications() {
        return notifications;
    }

    public static KidDAO getKidDAO() {
        return kidDAO;
    }

    public static NotificationDAO getNotificationDAO() {
        return notificationDAO;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (context == null){
            context = getApplicationContext();
        }
        kidDAO = KidDAO.getInstance(this);
        notificationDAO = NotificationDAO.getInstance(this);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

    }





    public static void setRegId(String regId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("regId", "0000");
        editor.apply();
    }

    public static SharedPreferences getPrefs() {
        return prefs;
    }

    public static String getRegId(){
        return prefs.getString("regId","0000");
    }



    public static void showNotif(Context context, Notification notification, String from){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(context.getResources().getIdentifier("com.example.hugo.syms:drawable/"+notification.getIcon()+"_l",null,null))
                        .setContentTitle(from+" : "+notification.getTitle())
                        .setContentText(notification.getText());
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setAutoCancel(true);
        mBuilder.setColor(context.getResources().getColor(R.color.blue01));
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    public static int getWidthScreen(Context context){
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    public static Bitmap getCircleBitmap(Bitmap bitmap, Context context) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = context.getResources().getColor(R.color.blue01);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


    public static String getServerUrl() {
        return prefs.getString("server_url_pref", Constant.SERVER_URL);
    }
    public static String getSenderId() {
        return prefs.getString("sender_id_pref", Constant.SENDER_ID);
    }

    public static void addMPhoneNumber(String number) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mPhone", number);
        editor.commit();
    }
    public static boolean isMPhoneIn(){
        return prefs.contains("mPhone");
    }
    public static String getMPhoneNumber() {
        return prefs.getString("mPhone", "0000");
    }

    public static void sendInvitationTo(String to) {
        Toast.makeText(context,to+" doesn't have the app", Toast.LENGTH_SHORT).show();
    }

    public static Kid getKidByPhone(String from) {
        Kid ret = null;
        for(Kid current : kids){
            if(current.getNumber().equals(from)){
                ret = current;
                break;
            }
        }
        return ret;
    }

    public static void setNotifications(List<Notification> notifications) {
        Utils.notifications = notifications;
    }
}
