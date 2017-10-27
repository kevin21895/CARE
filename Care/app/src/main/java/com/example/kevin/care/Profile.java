package com.example.kevin.care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

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

public class Profile extends AppCompatActivity {
    EditText name,phoneo,emailid,password,puniqueid1;
    TextView a,b;
    LinearLayout l1,l2;
    RadioButton parent,child;
    LinearLayout line;
    Button register;
    Switch aSwitch;
    public String type,token;
    public String name1,phoneno1,password1,email,user1,puniqueid,device;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        firebaseAuth = FirebaseAuth.getInstance();

        name = (EditText) findViewById(R.id.name);
        phoneo = (EditText) findViewById(R.id.phoneno);
        parent = (RadioButton) findViewById(R.id.parent);
        child = (RadioButton) findViewById(R.id.child);
        register = (Button) findViewById(R.id.save);
        emailid = (EditText) findViewById(R.id.emaildid);
        password = (EditText) findViewById(R.id.password);
        puniqueid1 = (EditText) findViewById(R.id.puniqueid);
        line = (LinearLayout) findViewById(R.id.line1);
        aSwitch = (Switch) findViewById(R.id.device);
        a = (TextView) findViewById(R.id.tt1);
        b = (TextView) findViewById(R.id.tt2);
        l1 = (LinearLayout) findViewById(R.id.la1);
        l2 = (LinearLayout) findViewById(R.id.line1);

        progressDialog = new ProgressDialog(this);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("Token"+token);



        child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(child.isChecked())
                {
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                }


            }
        });

        parent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(parent.isChecked())
                {
                    l2.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                }


            }
        });


    }

    private void registerUser(){

        name1 = name.getText().toString().trim();
        phoneno1 = phoneo.getText().toString().trim();
        user1 ="sorry";

        email = emailid.getText().toString().trim();
        password1  = password.getText().toString().trim();


        if(child.isChecked())
        {
            user1 = "Child";
        }
        if (parent.isChecked())
        {
            user1 = "Parent";
        }

       if(aSwitch.isChecked())
       {
           device = "Yes";

       }
       else

       {
           device = "No";
       }

        Log.d("TAG",device+"device");
        puniqueid = puniqueid1.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

       else if(TextUtils.isEmpty(password1)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
      else if(password1.length()<=6)
        {
            Toast.makeText(this,"Password length should be more than six characters",Toast.LENGTH_LONG).show();
        }



        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("f", "createUserWithEmail:onComplete:" + task.isSuccessful());



                        if (!task.isSuccessful()) {


                        }

                        progressDialog.dismiss();
                        // ...
                        new Check_Internet().execute();
                    }
                });


    }


    public void x(View v)
    {
        registerUser();
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
                type = "register";
                new Background().execute(type,name1,email,password1,phoneno1,user1,puniqueid,token,device);

            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }





    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);// finish activity1
        } else {
            Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }





    private class Background extends AsyncTask<String,Void,String> {
        public String result;
        Context context;

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];


            String login_url = "http://virtualwall.co.in/register.php";


            if (type.equals("register")) {
                String name = params[1];
                String emaild = params[2];
                String password2 = params[3];
                String phoneno = params[4];
                String user = params[5];
                String puniqueid = params[6];
                String token1 = params[7];
                String device1 = params[8];



                try {

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+"&"
                            +URLEncoder.encode("emailid","UTF-8")+"="+URLEncoder.encode(emaild,"UTF-8")  +"&"+
                            URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password2,"UTF-8")
                            +"&"+URLEncoder.encode("phoneno","UTF-8")+"="+URLEncoder.encode(phoneno,"UTF-8")
                            +"&"+URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(user,"UTF-8")
                            +"&"+URLEncoder.encode("cuniqueid","UTF-8")+"="+URLEncoder.encode(puniqueid,"UTF-8")
                            +"&"+URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(token1,"UTF-8")
                            +"&"+URLEncoder.encode("device","UTF-8")+"="+URLEncoder.encode(device1,"UTF-8");

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

            if (result.contains("ConnectRegistered")) {
                Toast.makeText(Profile.this,"Successfully Registered...",Toast.LENGTH_SHORT).show();
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("login",true);
                editor.commit();
                Intent i = new Intent(Profile.this,Nexttest.class);
                startActivity(i);
                finish();

            } else if (result.contains("ConnectNotRegistered")) {
                Toast.makeText(Profile.this,"Not Done",Toast.LENGTH_SHORT).show();
            }
            else if (result.contains("ConnectChildproblem"))
            {
                Toast.makeText(Profile.this,"ChildProb",Toast.LENGTH_SHORT).show();
            }


        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

}
