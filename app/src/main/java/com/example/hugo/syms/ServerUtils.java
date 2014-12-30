package com.example.hugo.syms;

import com.example.hugo.syms.clientData.Notification;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Hugo on 29/12/2014.
 */
public class ServerUtils {

    private static final String TAG = "ServerUtilities";
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    /**
     * Register this account/device pair within the server.
     */
    public static void register(final String phone, final String regId) {
//Log.i(TAG, "registering device (regId = " + regId + ")");
        String serverUrl = Utils.getServerUrl() + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.FROM, phone);
        params.put(Constants.REG_ID, regId);
// Once GCM returns a registration id, we need to register it in the
// demo server. As the server might be down, we will retry it a couple
// times.
        try {
            post(serverUrl, params, MAX_ATTEMPTS);
        } catch (IOException e) {
        }
    }
    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final String phone) {
//Log.i(TAG, "unregistering device (email = " + email + ")");
        String serverUrl = Utils.getServerUrl() + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.FROM, phone);
        try {
            post(serverUrl, params, MAX_ATTEMPTS);
        } catch (IOException e) {
// At this point the device is unregistered from GCM, but still
// registered in the server.
// We could try to unregister again, but it is not necessary:
// if the server tries to send a message to the device, it will get
// a "NotRegistered" error message and should unregister the device.
        }
    }
    /**
     * Send a message.
     */
    public static void send(Notification notification, String to) throws IOException {
//Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = Utils.getServerUrl() + "/send";
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.ICON, notification.getIcon());
        params.put(Constants.TEXT, notification.getText());
        params.put(Constants.TITLE, notification.getTitle());
        params.put(Constants.FROM, Utils.getMPhoneNumber());
        params.put(Constants.TO, to);
        post(serverUrl, params, MAX_ATTEMPTS);
    }
    /** Issue a POST with exponential backoff */
    private static void post(String endpoint, Map<String, String> params, int maxAttempts) throws IOException {
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= maxAttempts; i++) {
//Log.d(TAG, "Attempt #" + i);
            try {
                post(endpoint, params);
                return;
            } catch (IOException e) {
//Log.e(TAG, "Failed on attempt " + i + ":" + e);
                if (i == maxAttempts) {
                    throw e;
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    return;
                }
                backoff *= 2;
            } catch (IllegalArgumentException e) {
                throw new IOException(e.getMessage(), e);
            }
        }
    }
    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params) throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
// constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
//Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
// post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
// handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
