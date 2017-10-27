package com.example.kevin.care;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = "GeofenceTransitions";
 public String message,puniqueid;
    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {



        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Goefencing Error " + geofencingEvent.getErrorCode());
            return;
        }


        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            message = "Entered the location";
            Log.i(TAG, "ENtered");
            goog();

        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            message = "Exited the location";
            Log.i(TAG, "Exited");
            goog();
        } else  if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            message = "Dwelling";
            Log.i(TAG, "Dwelling");
            goog();
        }

        else {
            message = "Error";
            goog();
        }


    }

    private  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    boolean check_internet;
    public  boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
            }
        } else {
        }
        return false;
    }

    public void goog()
    {
        new Check_Internet().execute();
        Log.i("TAG","Kuch toh ho raha h");
    }


    private class Check_Internet extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            check_internet = hasActiveInternetConnection(getApplicationContext());
            return check_internet;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result) {
                SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                        Context.MODE_PRIVATE);
                puniqueid = sharedpreferences.getString("pu","");
                Log.i(TAG,puniqueid);
              String type = "sendn";
             new Background().execute(type,puniqueid,message);

            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class Background extends AsyncTask<String,Void,String> {
        public String result;
        Context context;
        AlertDialog alertDialog;

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];


            String login_url ="http://virtualwall.co.in/sendmessage.php";


            if (type.equals("sendn")) {
                String emailid1 = params[1];
                String message11 = params[2];



                try {

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("emailid", "UTF-8") + "=" + URLEncoder.encode(emailid1, "UTF-8")+"&"+
                            URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message11, "UTF-8");
                    Log.d("fjhj", emailid1);
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;

                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onPreExecute() {}

        protected void onPostExecute(String result) {

            Log.i(TAG,result);
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }





}

