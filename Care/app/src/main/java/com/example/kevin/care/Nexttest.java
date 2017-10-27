package com.example.kevin.care;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nexttest extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    LinearLayout layout, layout2;

    protected Context context;
    public String result, name1, device, emailid, password, user, p_uniqueid = "sprr", c_uniqueid, phone_no, t1 = "Child", t2 = "Parent", token, p_name, p_email, p_phone, p_child, p_user;
    TextView textView;
    public String email, refreshtoken, type;
    public int a = 1, t = 0;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nexttest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = (LinearLayout) findViewById(R.id.ll1);
        layout2 = (LinearLayout) findViewById(R.id.ll2);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {

            finish();

            startActivity(new Intent(this, MainActivity.class));
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();

        textView = (TextView) findViewById(R.id.textView2);
        MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();

        if (user != null) {
            email = user.getEmail();
        }

        System.out.println("Email" + email);


        refreshtoken = FirebaseInstanceId.getInstance().getToken();

        new Check_Internet().execute();
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
                type = "retrieve";
                new Nexttest.Background().execute(type, email);


            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        }
    }


    private class Background extends AsyncTask<String, Void, String> {

        Context context;



        @Override
        protected String doInBackground(String... params) {
            String type = params[0];


            String login_url = "http://virtualwall.co.in/retrieve.php";


            if (type.equals("retrieve")) {
                String emaild = params[1];


                try {

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("emaild", "UTF-8") + "=" + URLEncoder.encode(emaild, "UTF-8");

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

            } else if (type.equals("parent")) {
                String emaild = params[1];


                try {

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("emaild", "UTF-8") + "=" + URLEncoder.encode(emaild, "UTF-8");

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

            Log.d("Result", result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                Log.d("Result", "i am here");
                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("kevin");
                // looping through All Contacts
                if (t == 0) {
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        name1 = c.getString("name");

                        emailid = c.getString("emailid");
                        password = c.getString("password");
                        user = c.getString("user");
                        phone_no = c.getString("phoneno");
                        if (user.equals(t1)) {
                            p_uniqueid = c.getString("parent_uniqueid");
                            layout.setVisibility(View.GONE);
                            if (a == 1) {
                                String s = "parent";
                                new Background().execute(s, p_uniqueid);
                                a = 0;
                                t = 1;
                            }


                        } else if (user.equals(t2)) {
                            c_uniqueid = c.getString("child_uniqueid");
                            Log.d("fgb", c_uniqueid);

                            Pattern p = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
                            Matcher m = p.matcher(c_uniqueid);
                            boolean b = m.matches();
                            if (b) {
                                device = "No";
                            } else {
                                device = "Yes";
                            }
                            Log.d("TAG", "device" + device);
                            layout2.setVisibility(View.GONE);

                        }
                        token = c.getString("token");


                    }
                }

                if (t == 1) {
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        p_name = c.getString("name");
                        p_email = c.getString("emailid");
                        p_phone = c.getString("phoneno");
                        p_child = c.getString("child_uniqueid");

                        p_user = c.getString("user");
                        t = 0;
                        Log.d("Parent", p_phone);


                    }


                }
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("pu", p_uniqueid);
                editor.commit();
                editor.putString("cu", c_uniqueid);
                editor.commit();
                editor.putString("user", user);
                editor.commit();
                editor.putString("device", device);
                editor.commit();
                editor.putString("parent_phone",p_phone);
                editor.commit();
                startService(new Intent(getBaseContext(), HelloService.class));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

    public void see(View view) {
        Intent i = new Intent(Nexttest.this, ChildList.class);
        i.putExtra("Emailid", c_uniqueid);
        startActivity(i);
    }

    public void logout(View view) {
        firebaseAuth.signOut();

        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("login", false);
        editor.commit();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    public void showmap(View view) {
        Intent i = new Intent(Nexttest.this, MapsActivity.class);
        startActivity(i);
    }

    public void rad(View view) {
        Intent i = new Intent(Nexttest.this, Geofence1.class);
        startActivity(i);
    }

    public void emergency(View v) {
        Intent i1 = new Intent(Nexttest.this, MapsActivity3.class);
        startActivity(i1);
    }

    public void panic(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+91" + p_phone));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        startActivity(intent);
    }

    boolean exit = false;
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);// finish activity

        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


}
