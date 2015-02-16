package com.example.hugo.syms;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.hugo.myapplication.backend.messaging.Messaging;
import com.example.hugo.syms.clientData.Kid;
import com.example.hugo.syms.clientData.Notification;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Hugo on 06/01/2015.
 */
public class SendNotifAsyncTask extends AsyncTask<Void, Void,String> {
    private Messaging mesService = null;
    private boolean base64;
    private String icon;
    private String title;
    private String text;
    private String to;
    private Context context;

public SendNotifAsyncTask(Context context, boolean base64,Kid to, String icon, String title, String text){
    this.base64 = base64;
    this.icon = icon;
    this.title = title;
    this.text = text;
    this.to =to.getNumber();
    this.context = context;
}
public SendNotifAsyncTask(Context context, Kid to, Notification notif){
        this.base64 = false;
        this.icon = notif.getIcon();
        this.title = notif.getTitle();
        this.text = notif.getText();
        this.to =to.getNumber();
        this.context =context;
    }


    @Override
    protected String doInBackground(Void... params) {
        String ret = "Notification sent";
        if (mesService == null) {
            Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setRootUrl("https://dulcet-order-808.appspot.com/_ah/api/");
                    // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                    // otherwise they can be skipped
                    /*.setRootUrl("http://192.168.1.67:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });*/
            // end of optional local run code
            mesService = builder.build();
        }
        try {
            mesService.notifToBackend(base64,to, icon, title, text,Utils.getMPhoneNumber()).execute();
            Log.d("Sending notification", "to " + to);
        } catch (IOException ex) {
            ex.printStackTrace();
            ret = "Notification not sent";

        }
        return ret;

    }

    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT ).show();
    }

}
