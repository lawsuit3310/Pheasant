package com.example.pheasant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    public static DatabaseReference mDatabase;

    DatabaseManager()
    {
        getReference(mDatabase);
    }

    void getReference(DatabaseReference dr)
    {
        dr = FirebaseDatabase.getInstance().getReference();
    }

}
