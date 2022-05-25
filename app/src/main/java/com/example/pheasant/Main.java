package com.example.pheasant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner setStartPoint;
    Spinner setArrivePoint;

    ArrayAdapter<CharSequence> adapter;

    String[] items;

    boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DatabaseManager database  = new DatabaseManager();
        //database.mDatabase.child("value").setValue("0");

        setStartPoint = findViewById(R.id.Start);
        setArrivePoint = findViewById(R.id.Arrive);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.Nodes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setStartPoint.setAdapter(adapter);
        setArrivePoint.setAdapter(adapter);

        setStartPoint.setOnItemSelectedListener(this);
        setArrivePoint.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //n-1번째로 선택된 값 출력
        try {
            if (adapter.getItemId(i) != 0) {
                if (adapterView.getId() == (R.id.Start) && setStartPoint.getId() != 0) {
                    Log.d("JB", "Start." + adapter.getItemId(i)); //나중에 값 읽을 때 "." 을 기준으로 스플릿함
                }

                if (adapterView.getId() == (R.id.Arrive) && setArrivePoint.getId() != 0) {
                    Log.d("JB","Arrive." + adapter.getItemId((i)));

                }
            }
        }
        catch (Exception e)
        {
            Log.d("JB", e.getMessage());
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
