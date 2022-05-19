package com.example.pheasant;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager database  = new DatabaseManager();

        database.mDatabase.child("value").setValue("0");


    }
}
