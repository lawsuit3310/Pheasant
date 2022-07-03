package com.example.pheasant;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DatabaseManager {
    private DatabaseReference dbRef = null;

    public final static int MOVE_STOP= 0;
    public final static int MOVE_FORWARD = 1;
    public final static int MOVE_BACKWARD = 2;
    public final static int MOVE_LEFT = 3;
    public final static int MOVE_RIGHT = 4;
    public final static int SPD_INCREASE = 5;
    public final static int SPD_DECREASE = 6;
    public final static int SPIN_SPD_INCREASE = 7;
    public final static int SPIN_SPD_DECREASE = 8;
    public final static int SAVE_PROC = 9;
    public final static int MOD_CHANGE_TO_1 = 10;
    public final static int MOD_CHANGE_TO_2 = 11;
    public final static int MOD_CHANGE_TO_3 = 12;

    DatabaseManager() {
        dbRef = FirebaseDatabase.getInstance().getReference();
    }
    DatabaseManager(String sender) {
        this();
        dbRef.child(sender).child("control").updateChildren(ctrlClear());
        dbRef.child(sender).child("speed").setValue(0);

    }

    public boolean toSend(String sender, int type, Object o){
        dbRef = FirebaseDatabase.getInstance().getReference();
        try{

            dbRef.child(sender).child("control").child("stop").setValue(1);
            dbRef.child(sender).child("control").child("forward").setValue(0);
            dbRef.child(sender).child("control").child("backward").setValue(0);
            dbRef.child(sender).child("control").child("left").setValue(0);
            dbRef.child(sender).child("control").child("right").setValue(0);
            switch (type)
            {
                case MOVE_STOP:
                    //db에 들어있던 값 초기화
                    dbRef.child(sender).child("control").updateChildren(ctrlClear());
                    //db에 값 업데이트
                    dbRef.child(sender).child("control").child("stop").setValue(1);
                    break;
                case MOVE_FORWARD:
                    dbRef.child(sender).child("control").updateChildren(ctrlClear());
                    dbRef.child(sender).child("control").child("forward").setValue(1);
                    break;
                case MOVE_BACKWARD:
                    dbRef.child(sender).child("control").updateChildren(ctrlClear());
                    dbRef.child(sender).child("control").child("backward").setValue(1);
                    break;
                case MOVE_LEFT:
                    dbRef.child(sender).child("control").updateChildren(ctrlClear());
                    dbRef.child(sender).child("control").child("left").setValue(1);
                    break;
                case MOVE_RIGHT:
                    dbRef.child(sender).child("control").updateChildren(ctrlClear());
                    dbRef.child(sender).child("control").child("right").setValue(1);
                    break;
                case SAVE_PROC:
                    dbRef.child(sender).updateChildren(ctrlClear());
                    dbRef.child(sender).child("save").setValue(1);
                    break;

                case SPD_INCREASE:
                    dbRef.child(sender).child("speed").setValue(o.toString());
                case SPD_DECREASE:
                    dbRef.child(sender).child("speed").setValue(o.toString());
                    break;
                case SPIN_SPD_INCREASE:
                    dbRef.child(sender).child("spin_speed").setValue(o.toString());
                case SPIN_SPD_DECREASE:
                    dbRef.child(sender).child("spin_speed").setValue(o.toString());
                    break;

                case MOD_CHANGE_TO_1:
                case MOD_CHANGE_TO_2:
                case MOD_CHANGE_TO_3:
                    Log.d("JB",o.toString());
                    dbRef.child(sender).child("mod").setValue(o.toString());
                    break;


            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
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

    private HashMap<String, Object> ctrlClear(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("forward",0);
        result.put("backward",0);
        result.put("left",0);
        result.put("right",0);
        result.put("stop",0);
        result.put("save",0);
        return result;
    }
}
