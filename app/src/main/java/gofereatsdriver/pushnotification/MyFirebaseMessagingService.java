package gofereatsdriver.pushnotification;
/**
 * @package com.gofereatsdriver
 * @subpackage pushnotification
 * @category FirebaseMessagingService
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.views.main.commondialog.ShowDialog;
import gofereatsdriver.views.main.home.RequestReceiveActivity;


/**************************************************************
 Firebase Messaging Service to base push notification message to activity
 ****************************************************************/
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public String message = "";
    public String status = "";
    public String title = "";
    public int orderId = 0;
    public AlertDialog dialog;

    @Override
    public void onCreate() {
        super.onCreate();
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Message Notification remoteMessage: " + remoteMessage.toString());

        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage == null) return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());


            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
                if (remoteMessage.getNotification() != null) {
                    Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            //{"custom":{"type":"order_cancelled","title":"Your Order Id #310 has been cancelled by restaurant","order_id":310}}
            sessionManager.setPushNotification(json.toString());

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Log.e(TAG, "IF: " + json.toString());
                // app is in foreground, broadcast the push message

                if (json.getJSONObject("custom").has("type")) {

                    status = json.getJSONObject("custom").getString("type");
                }

                if (json.getJSONObject("custom").has("title")) {

                    title = json.getJSONObject("custom").getString("title");
                }

                if (json.getJSONObject("custom").has("order_id")) {

                    orderId = json.getJSONObject("custom").getInt("order_id");
                }
                /**
                 * @TripStatus 'pending' => 0, Driver accept the request
                 * 'confirmed' => 1, Driver confirmed the  list and rating for restaurant
                 * 'declined' => 2,  Driver declined the order list
                 * 'started' => 3,  Driver start the trip (pickup the order)
                 * 'delivered'  => 4, Driver drop off or delivered the order and rating for eater
                 * 'completed' => 5, Driver complete the trip
                 * 'cancelled' => 6, Driver or restaurant cancel the trip
                 *
                 * @DriverStatus 0 for offline
                 * 1 for online
                 * 2 for trip
                 */

                if ("order_cancelled".equalsIgnoreCase(status)) {

                    sessionManager.setDriverStatus(1);
                    sessionManager.setTripStatus(-1);
                    sessionManager.setTripId(0);
                    Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                    dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogs.putExtra("message", title);
                    dialogs.putExtra("type", 2);
                    dialogs.putExtra("tripid", orderId);
                    startActivity(dialogs);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());

                } else if ("order_request".equalsIgnoreCase(status)) {

                   /* Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", "message");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);*/
                    Intent intent = new Intent(getApplicationContext(), RequestReceiveActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if ("order_delayed".equalsIgnoreCase(status)) {
                    Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                    dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogs.putExtra("message", title);
                    dialogs.putExtra("type", 3);
                    startActivity(dialogs);
                } else {
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                }
            } else {
                Log.e(TAG, "ELSE: " + json.toString());

                if (json.getJSONObject("custom").has("type")) {

                    status = json.getJSONObject("custom").getString("type");
                }

                if (json.getJSONObject("custom").has("title")) {
                    message="";
                    title = json.getJSONObject("custom").getString("title");
                }
                if (json.getJSONObject("custom").has("order_id")) {
                    orderId = json.getJSONObject("custom").getInt("order_id");
                }

                if ("".equals(message)) {
                    message = title;
                }


                if ("order_cancelled".equalsIgnoreCase(status)) {
                    sessionManager.setDriverStatus(1);
                    sessionManager.setTripStatus(-1);
                    sessionManager.setTripId(0);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());

                } else if ("order_request".equalsIgnoreCase(status)) {
                    Intent intent = new Intent(getApplicationContext(), RequestReceiveActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else if ("order_delayed".equalsIgnoreCase(status)) {

                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());
                }
                else {
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    if (!sessionManager.getToken().equals("")) {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    }
                }

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


}

