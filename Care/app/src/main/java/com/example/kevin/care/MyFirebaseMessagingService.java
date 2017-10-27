package com.example.kevin.care;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Kevin on 21-01-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public String body, data1;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"FROM:"+remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Message Data:"+remoteMessage.getData());
            data1 = remoteMessage.getData().toString();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("data", data1);
            editor.commit();}
        else
        {
            Log.d("ff","No");
        }
        if(remoteMessage.getNotification() !=null)
        {
            Log.d(TAG, "Message Body:" + remoteMessage.getNotification().getBody());
            body = remoteMessage.getNotification().getBody().toString();
        }

        if(data1.contains("{message=georeques"))
       sendNotification("Request");
        if (data1.equals("{message=Entered the location}"))
            sendNotification("Entered the safe zone");
        if (data1.equals("{message=Exited the location}"))
            sendNotification("Exited the safe zone");
        if (data1.equals("{message=Dwelling}"))
            sendNotification("Dwelling in safe zone");
        if(data1.equals("{message=request}"))
        sendNotification1("Request for location");
         if(data1.contains("{message=location"))
             sendNotification1("Child's Location");

    }

    private void sendNotification(String data)
    {
        Intent intent = new Intent(this,MapsActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Care")
                .setContentText(data).setAutoCancel(true)
                .setSound(notificationSound).setContentIntent(pendingIntent);
        Log.d(TAG,"Done");
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifiBuilder.build());

    }
    private void sendNotification1(String data)
    {
        Intent intent = new Intent(this,MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Care")
                .setContentText(data).setAutoCancel(true)
                .setSound(notificationSound).setContentIntent(pendingIntent);
        Log.d(TAG,"Done");
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifiBuilder.build());

    }
}
