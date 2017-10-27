package com.example.kevin.care;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.android.gms.wearable.DataMap.TAG;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
    PendingIntent mGeofencePendingIntent;
    public GoogleMap mMap;
    boolean gps_enabled = false, network_enabled = false;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, mLastLocation1;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    List<Geofence> mGeofenceList;
    public static final String TAG = "Activity";
    double currentLatitude, currentLongitude,laa,lona,clat,clon;
    private String lat, lon, data2, tdata, tdata1, data4, data6,data8,appd2,aapd4,aapd6, type,puniqueid,user,data3,e1,device,message1,message2,phoneno,phoneno1;
    int s = 0, a = 1, g = 1,e =1, d = 0;
    public float radius2,rad,radius3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps22);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {

        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e)

        {

        }

        if (!gps_enabled || !network_enabled) {
            Toast.makeText(getApplicationContext(), "Please enable location", Toast.LENGTH_SHORT).show();

        }

        Intent appLinkIntent = getIntent();

        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        handleIntent(getIntent());
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        user = sharedpreferences.getString("user", "");
        Log.d("user", user);
        if (savedInstanceState == null) {

            mGeofenceList = new ArrayList<Geofence>();


            MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();


            tdata = "{message=georequest";
            tdata1 = "^[0-9.+]+$";


            puniqueid = sharedpreferences.getString("pu", "");



        }


    }



    public void onMapReady(GoogleMap googleMap){
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                    // createGeofences(currentLatitude,currentLongitude);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                Log.d(TAG, "DDDDDDDDD");
            }
            SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                    Context.MODE_PRIVATE);
            data2 = sharedpreferences.getString("data", "");
            sharedpreferences.edit().remove("data").commit();
        Log.d("Message",data2);

      if(user.equals("Parent")) {
          e1 = sharedpreferences.getString("cu", "");
          Log.d("Child",e1);
          device = sharedpreferences.getString("device", "");

          radius2 = Float.parseFloat(sharedpreferences.getString("radius", ""));
          message1 = "georequest";
          Log.d("Device", device);
          phoneno = sharedpreferences.getString("cu", "");
          Log.d("Phoneno", phoneno);
      }

            if (user.equals("Child") && data2.contains(tdata) && e == 1) {

                Log.d("fkjgf", "yess");
                String[] sep = data2.split("\\&");
                String data3 = sep[1];
                Log.d("data3",data3);
                String[] sep1 = data3.split("!");
                data4 = sep1[0];
                Log.d("data4", sep1[1]);
                String data5 = sep1[1];
                String[] sep2 = data5.split("\\)");
                data6 = sep2[0];
                String data7 = sep2[1];
                String[] sep3 = data7.split("\\}");
                data8 = sep3[0];

                Log.d("data6", data6);
                Log.d("data8", data8);
                clat = Double.parseDouble(data4);
                clon = Double.parseDouble(data6);
                rad = Float.parseFloat(data8);



                createGeofences(clat, clon,rad);

                Log.d("fkjgf", "go");

                Log.d("djkf", puniqueid);



                s = 1;
                e = 0;
                d = 1;

            } else {
                Log.d("fkjgf", "no");
            }





        }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {


        if (user.equals("Child")) {
            Log.i(TAG, "connected");
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                Log.d(TAG, "FFFF");

            }


            if (d == 1) {

                try {
                    LocationServices.GeofencingApi.addGeofences(
                            mGoogleApiClient,
                            getGeofencingRequest(),
                            getGeofencePendingIntent()
                    ).setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Log.i(TAG, "Saving Geofence");

                            } else {
                                Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                        " : " + status.getStatusCode());
                            }
                        }
                    });

                } catch (SecurityException securityException) {

                    Log.e(TAG, "Error");
                }
                d = 0;
            }
        }


        if (user.equals("Parent")) {
            Log.i(TAG, "connected");
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                Log.d(TAG, "FFFF");

            }
        }
    }




        @Override
    public void onConnectionSuspended(int i) {
     Log.d(TAG,"Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"Failed");

    }

    public void createGeofences(double latitude, double longitude,float radii) {
        String id = UUID.randomUUID().toString();
        mGeofenceList.add(new Geofence.Builder()

                .setRequestId(id)

                .setCircularRegion(
                        latitude,
                        longitude,
                        radii
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT |Geofence.GEOFENCE_TRANSITION_DWELL).setLoiteringDelay(1000)
                .build());
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onLocationChanged(Location location) {


        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        mMap.getUiSettings(). setZoomGesturesEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.i(TAG, currentLatitude + "onLocationChanged" + currentLongitude);
        if(aapd4 != null || appd2 != null) {
            LatLng latLng3 = new LatLng(Double.parseDouble(appd2), Double.parseDouble(aapd4));
            mMap.addMarker(new MarkerOptions().position(latLng3).title("Child Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }
        if(user.equals("Parent") && a ==1 && device.equals("No"))
        {   type = "sendcenter";
            new Check_Internet().execute();
             new Background().execute(type,e1,message1,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),String.valueOf(radius2));
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
            markerForGeofence(latLng1);
            drawGeofence(radius2);
            a=0;

        }
        if (user.equals("Parent") && device.equals("Yes") && a == 1  ) {

            phoneno1 = "+91" + phoneno;
            Log.d("Phoneno", phoneno1);
            radius3 = radius2/1000;
            message2 =  radius3 + " , " + String.valueOf(location.getLatitude()) +  " , " + String.valueOf(location.getLongitude()) ;
            sendSMSMessage();
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
            markerForGeofence(latLng1);
            drawGeofence(radius2);
            a=0;



        }






        if (s == 1) {

             Log.d(TAG,"Sendl");
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
                new Check_Internet().execute();
            type = "sentlocation";
            new MapsActivity2.Background().execute(type, puniqueid, lat, lon);


        }
 
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
         data2 = sharedpreferences.getString("data", "");
       if(data2 != null)
        data3 = data2;
        Log.d(TAG,data3);
        sharedpreferences.edit().remove("data").commit();


                Pattern p = Pattern.compile(".*[^0-9].*");
        Matcher m = p.matcher(data3);


                    if (data3.matches(".*\\d+.*")) {

                        Log.d("fkjgf", "yess");
                        String[] sep = data2.split("=");
                        String data3 = sep[1];
                        String[] sep1 = data3.split("!");
                        data4 = sep1[0];
                        String data5 = sep1[1];
                        String[] sep2 = data5.split("\\}");
                        data6 = sep2[0];
                        Log.d("data4", data4);
                        Log.d("data6", data6);
                        currentLatitude = Double.parseDouble(data4);
                        currentLongitude = Double.parseDouble(data6);

                        LatLng latLng1 = new LatLng(currentLatitude, currentLongitude);


                      MarkerOptions  options = new MarkerOptions().position(latLng1).title("Child Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                        PositionUpdate positionUpdaterTask = new PositionUpdate(mMap, options, latLng1);
                        positionUpdaterTask.execute();

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                    }








    }




    private Circle geoFenceLimits;

    private void drawGeofence(float radius) {
        Log.d(TAG, "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(radius);
        geoFenceLimits = mMap.addCircle(circleOptions);
    }

    private Marker geoFenceMarker;


    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");
        String title = "Center";

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if (mMap != null) {

            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = mMap.addMarker(markerOptions);
        }


    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d("Message","Hi");
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String recipeId = appLinkData.getLastPathSegment();
            Uri appData = Uri.parse("content://com.google.maps/").buildUpon()
                    .appendPath(recipeId).build();
            Log.d("Message", String.valueOf(appData));
            String sep = String.valueOf(appData);
            String [] seep = String.valueOf(sep).split("D");
            String appd1 = seep[1];
            Log.d("CCC",appd1);
            String[] sep1 = appd1.split("%");
             appd2 = sep1[0];
            Log.d("BBB",appd2);
            String aapd3 = sep1[1];
            Log.d("AAA",seep[1]);

            String[] sep2 = aapd3.split("D");

            aapd4 = seep[2];
            Log.d("Message", appd2 + "& " + aapd4);

        }
    }


    protected void sendSMSMessage() {
        Log.d("SMS","Entered");
        SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneno, null, message2, null, null);
            Log.d("SMS", "Done");


    }









    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    boolean check_internet;

    public boolean hasActiveInternetConnection(Context context) {
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

    @Override
    public void onResult(@NonNull Status status) {
        Log.d(TAG,"stopped");
    }


    private class Check_Internet extends AsyncTask<Void, Void, Boolean> {

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

            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Background extends AsyncTask<String, Void, String> {
        public String result;
        Context context;
        AlertDialog alertDialog;

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String login_url = "http://virtualwall.co.in/sendlocation.php";
            String send_url = "http://virtualwall.co.in/sendlocation1.php";

            if (type.equals("sentlocation")) {
                    String puniqueid1 = params[1];
                    String lat1 = params[2];
                    String lon1 = params[3];
                    try {
                        URL url = new URL(login_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String post_data = URLEncoder.encode("puniqueid", "UTF-8") + "=" + URLEncoder.encode(puniqueid1, "UTF-8") + "&" +
                                URLEncoder.encode("lat1", "UTF-8") + "=" + URLEncoder.encode(lat1, "UTF-8") + "&" +
                                URLEncoder.encode("lon1", "UTF-8") + "=" + URLEncoder.encode(lon1, "UTF-8");
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

            else if(type.equals("sendcenter"))
            {
                String puniqueid1 = params[1];
                String message = params[2];
                String lat1 = params[3];
                String lon1 = params[4];
                String radius = params[5];
                try {
                    URL url = new URL(send_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("puniqueid", "UTF-8") + "=" + URLEncoder.encode(puniqueid1, "UTF-8")
                            + "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8")
                            + "&" + URLEncoder.encode("lat1", "UTF-8") + "=" + URLEncoder.encode(lat1, "UTF-8")
                            + "&" + URLEncoder.encode("lon1", "UTF-8") + "=" + URLEncoder.encode(lon1, "UTF-8")
                            + "&" + URLEncoder.encode("radius", "UTF-8") + "=" + URLEncoder.encode(radius, "UTF-8");
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
        protected void onPreExecute() {
        }

        protected void onPostExecute(String result) {
            Log.d("Final Result", result);
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }



    }




}

class PositionUpdate extends AsyncTask<Void, Void, Void> {

    private GoogleMap myGoogleMap;
    private MarkerOptions options;
    private LatLng positionToAdd;
    public Marker mar;


    public PositionUpdate(GoogleMap googleMap, MarkerOptions options, LatLng position) {
        this.myGoogleMap = googleMap;
        this.options = options;
        this.positionToAdd = position;
    }


    @Override
    protected Void doInBackground(Void... voids) {




        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(TAG,"Markerr");
        if (myGoogleMap != null) {
            mar = myGoogleMap.addMarker(options);
            Log.i(TAG, "MARKER ADDED");
        } else {
            Log.e(TAG, "ERROR ADDING THE MARKER");
        }





    }
}

class SmsListener extends BroadcastReceiver {


public  String msgBody;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from = null;
            if (bundle != null){

                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                         msgBody = msgs[i].getMessageBody();

                    }
                    Log.d("Message",msg_from + "!" + msgBody);
                }catch(Exception e){
                                 Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
