package com.example.kevin.care;

/**
 * Created by Kevin on 07-04-2017.
 */


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class HelloService extends Service {
    Context mContext;
    public String d;
    public int q =1;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        mContext = getApplicationContext();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(mBroadcastReceiver,iFilter);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String x = sharedpreferences.getString("parent_phone","");
        d = "+91"+x;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

            float percentage = level/ (float) scale;

            int a = (int) (percentage *100);
            String message = "Your Child Device battery is " + String.valueOf(a);
            if(a <= 75 && q == 1)
            {
                Toast.makeText(HelloService.this,"Low",Toast.LENGTH_SHORT).show();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(d, null, message, null, null);
                Log.d("SMS", "Done");
                q = 0;

            }










        }
    };



}