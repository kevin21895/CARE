package com.example.kevin.care;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Kevin on 21-01-2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInsIdService";

    @Override
    public void onTokenRefresh() {
        String refreshtoken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"New Token" +refreshtoken);
    }
}
