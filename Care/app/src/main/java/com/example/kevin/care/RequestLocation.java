package com.example.kevin.care;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

public class RequestLocation extends AppCompatActivity {
   public String emaild,type,message,e;
    TextView kl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_location);
        kl = (TextView) findViewById(R.id.k);
        if(savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
               // emaild = null;
            } else {
                emaild= extras.getString("Emailid");
                Log.d("htrthgdc",emaild);
            }
        }
        else {
            emaild= (String) savedInstanceState.getSerializable("Emailid");
            Log.d("ggddg",emaild);
        }
        message ="request";

           e = new ChildList().email2;

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
                type = "login";
                new Background().execute(type,emaild,message);


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


            String login_url ="http://virtualwall.co.in/requestlocation.php";


            if (type.equals("login")) {
                String emailid1 = params[1];
                String message1 = params[2];



                try {

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("emailid", "UTF-8") + "=" + URLEncoder.encode(emailid1, "UTF-8")+"&"
                            +URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message1, "UTF-8");
                    Log.d("fjhj",emailid1);
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


            kl.setText(result);


        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}

