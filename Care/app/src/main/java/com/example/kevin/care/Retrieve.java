package com.example.kevin.care;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Retrieve extends AppCompatActivity {
    private Firebase firebase;
    private TextView name,phoneno,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
         name = (TextView) findViewById(R.id.name);
        phoneno = (TextView) findViewById(R.id.phoneno);
        type = (TextView) findViewById(R.id.type);
        firebase = new Firebase("https://care-1b888.firebaseio.com/");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name1 = dataSnapshot.getValue(String.class);
                name.setText(name1);
                System.out.println("Name1:"+name1);
                Toast.makeText(Retrieve.this,name1,Toast.LENGTH_SHORT);

                String name2 = dataSnapshot.getValue(String.class);
                String name3 = dataSnapshot.getValue(String.class);
                phoneno.setText(name2);
                type.setText(name3);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.print("Error");
            }
        });
    }

}
