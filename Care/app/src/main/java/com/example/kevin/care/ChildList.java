package com.example.kevin.care;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChildList extends AppCompatActivity {
 String emaild;
    public String email2;
    TextView name,email,user,phone,parent,device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
               emaild = null;
            } else {
                emaild= extras.getString("Emailid");
                Log.d("df",emaild);
            }
        }
        else {
            emaild= (String) savedInstanceState.getSerializable("Emailid");
            Log.d("",emaild);
        }
        setContentView(R.layout.activity_child_list);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.emailid);
        phone = (TextView) findViewById(R.id.phoneno);
        user = (TextView) findViewById(R.id.user1);
        parent = (TextView) findViewById(R.id.parent);
        device = (TextView) findViewById(R.id.device);
        new Check_Internet().execute();





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
                String type = "childlist";

                new ChildList.Background().execute(type,emaild);



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


            String login_url = "http://virtualwall.co.in/childlist.php";


            if (type.equals("childlist")) {
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

            Log.d("hello",result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("Child");
                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    name.setText(c.getString("name"));
                    email.setText(c.getString("emailid"));
                    phone.setText(c.getString("phoneno"));
                    parent.setText(c.getString("parent_uniqueid"));
                    email2 = c.getString("emailid");
                    System.out.println("emaild" + email2);
                     user.setText(c.getString("user"));
                    device.setText(c.getString("device"));



                }







            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }



    }

    public void requestlocation(View view)
    {
        Intent i = new Intent(ChildList.this,RequestLocation.class);
        i.putExtra("Emailid",email2);
        startActivity(i);
    }
}
