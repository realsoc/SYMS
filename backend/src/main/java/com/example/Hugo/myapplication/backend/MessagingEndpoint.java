/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.example.Hugo.myapplication.backend;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.example.Hugo.myapplication.backend.OfyService.ofy;
import  com.example.Hugo.myapplication.backend.Constant;

/**
 * An endpoint to send messages to devices registered with the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(
        name = "messaging",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Hugo.example.com",
                ownerName = "backend.myapplication.Hugo.example.com",
                packagePath = "")
)
public class MessagingEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */
    @ApiMethod(name = "sendMessage", path = "/tarace/")
    public void sendMessage(@Named("message") String message) throws IOException {
        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder().addData("message", message).build();
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(10).list();
        for (RegistrationRecord record : records) {
            Result result = sender.send(msg, record.getRegId(), 5);
            if (result.getMessageId() != null) {
                log.info("Message sent to " + record.getRegId());
                String canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    log.info("Registration Id changed for " + record.getRegId() + " updating to " + canonicalRegId);
                    record.setRegId(canonicalRegId);
                    ofy().save().entity(record).now();
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    log.warning("Registration Id " + record.getRegId() + " no longer registered with GCM, removing from datastore");
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    ofy().delete().entity(record).now();
                } else {
                    log.warning("Error when sending message : " + error);
                }
            }
        }
    }
    @ApiMethod(name = "notifToBackend")
//    @ApiMethod(name = "sendToBackend")
    public void notifToBackend(@Named("isBase64") boolean isBase64, @Named("to") String to,@Named("icon") String icon,@Named("title") String title, @Named("text") String text, @Named("from") String from) throws IOException {
        /*if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it  isempty");
            return;
        }*/
        Sender sender = new Sender(API_KEY);
        Key<RegistrationRecord> registrationRecordKey = Key.create(RegistrationRecord.class,to);
        RegistrationRecord registrationRecord = ofy().load().key(registrationRecordKey).now();

        if(registrationRecord != null){
            Message msg = new Message.Builder().addData(Constant.MESSAGE_TYPE, Constant.MESSAGE).
                    addData(Constant.BASE64,String.valueOf(isBase64)).
                    addData(Constant.TITLE,title).
                    addData(Constant.TEXT, text).
                    addData(Constant.ICON,icon).
                    addData(Constant.FROM, from).build();
            log.info("Sending notification to " + registrationRecord);
            Result result = sender.send(msg, registrationRecord.getRegId(), 5);
            //contactNotExist(from, to, sender);
        }else {
            Message msg = new Message.Builder().
                    addData(Constant.MESSAGE_TYPE, Constant.CONTACT_NOT_FOUND).
                    addData(Constant.TO,to).build();
            registrationRecordKey = Key.create(RegistrationRecord.class,from);
            registrationRecord = ofy().load().key(registrationRecordKey).now();
            log.info("Sending error to " + registrationRecord);
            Result result = sender.send(msg, registrationRecord.getRegId(), 5);
            msg =  new Message.Builder().addData(Constant.MESSAGE_TYPE, Constant.MESSAGE).build();
        }
    }

    private void contactNotExist(String to, String contactNumber, Sender sender) throws IOException {

    }
}
