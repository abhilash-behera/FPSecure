package com.abhilash.developer.fpsecure;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abhilash.developer.fpsecure.activity.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

/**
 * Created by Abhilash on 17-09-2017
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String type="";

        if (remoteMessage.getData().size() > 0) {
            Log.d("fpsecure", "Message data payload: " + remoteMessage.getData());
            try{
                JSONObject data=(JSONObject)remoteMessage.getData();
                if(data.getString("type").equalsIgnoreCase("Sent Document")){
                    type=Utils.TYPE_SENT_DOCUMENT;
                }else{
                    type=Utils.TYPE_RECEIVED_DOCUMENT;
                }
            }catch (Exception e){
                Log.d("fpsecure","Cannot parse message");
            }
        }

        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody(),type);
            Log.d("fpsecure", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody,String type) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if(type.equalsIgnoreCase(Utils.TYPE_SENT_DOCUMENT)){
            intent.putExtra(Utils.DOCUMENT_TYPE,Utils.TYPE_SENT_DOCUMENT);
        }else{
            intent.putExtra(Utils.DOCUMENT_TYPE,Utils.TYPE_RECEIVED_DOCUMENT);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.channel_name);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(Color.RED,1000,400)
                .setPriority(Notification.PRIORITY_MAX)
                .setVibrate(new long[]{100,200,300})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
