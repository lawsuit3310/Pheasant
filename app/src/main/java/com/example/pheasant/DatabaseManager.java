package com.example.pheasant;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    private DatabaseReference dbRef = null;

    DatabaseManager() {
        dbRef = FirebaseDatabase.getInstance().getReference();
    }
    
    public void toSend(String sender, String argS, String argA) //출발점과 도착점을 매개변수로 받음
    {
        dbRef = FirebaseDatabase.getInstance().getReference();
        Log.d("JB", "Start : "+ argS + "\tArrive: " + argA);

        dbRef.child(sender).child("Start").setValue(argS);
        dbRef.child(sender).child("Arrive").setValue(argA);
    }

    public DatabaseReference getReference()
    {
        dbRef = FirebaseDatabase.getInstance().getReference();
        return dbRef;
    }
}
