package com.example.kevin.care;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Geofence1 extends AppCompatActivity {
  EditText radius;
    Button geo;

    public String radius2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        radius = (EditText) findViewById(R.id.ed1);
        geo = (Button) findViewById(R.id.geoo);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);

    }

    public void geoo(View view)
    {   radius2 = radius.getText().toString().trim();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putString("radius",radius2);
        editor.commit();
        Intent i = new Intent(Geofence1.this, MapsActivity2.class);

        startActivity(i);

    }











}
