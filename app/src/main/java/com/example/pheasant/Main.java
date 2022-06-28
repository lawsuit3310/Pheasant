package com.example.pheasant;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Main extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    /* 맴버 변수 */
    Spinner setStartPoint;
    Spinner setArrivePoint;
    Spinner setBuildingStart;
    Spinner setFloorStart;
    Spinner setBuildingArrive;
    Spinner setFloorArrive;

    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> Buildadapter;
    ArrayAdapter<CharSequence> Flooradapter;

    Button sendBtn;

    DatabaseManager dbManager;

    String resultS = null; //시작점 전달해줄 변수
    String resultA = null; //도착점 전달해줄 변수
    String BuildingS = null;
    String FloorS = null;
    String BuildingA = null;
    String FloorA = null;

    final static String CACHE_DEVICE_ID = "CacheDeviceID";

    int[] adapters = {R.array.DefaultSpinner, R.array.teach1F, R.array.teach2F, R.array.teach3F, R.array.teach4F,
            R.array.train1F, R.array.train2F, R.array.train3F, R.array.train4F};

    /* 메인 동작 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DatabaseManager();

        setStartPoint = findViewById(R.id.Start);
        setArrivePoint = findViewById(R.id.Arrive);
        setBuildingStart = findViewById(R.id.BuildingS);
        setBuildingArrive = findViewById(R.id.BuildingA);
        setFloorStart = findViewById(R.id.FloorS);
        setFloorArrive = findViewById(R.id.FloorA);
        sendBtn = findViewById(R.id.sender);
        findViewById(R.id.Changer).setOnClickListener(this);

        adapter = ArrayAdapter.createFromResource(this,
                adapters[0], android.R.layout.simple_spinner_item);
        Buildadapter = ArrayAdapter.createFromResource(this,
                R.array.Building, android.R.layout.simple_spinner_item);
        Flooradapter = ArrayAdapter.createFromResource(this,
                R.array.Floor, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setStartPoint.setAdapter(adapter);
        setArrivePoint.setAdapter(adapter);
        setBuildingStart.setAdapter(Buildadapter);
        setFloorStart.setAdapter(Flooradapter);
        setBuildingArrive.setAdapter(Buildadapter);
        setFloorArrive.setAdapter(Flooradapter);

        Log.d("JB", getDeviceSSAID());

        setStartPoint.setOnItemSelectedListener(this);
        setArrivePoint.setOnItemSelectedListener(this);
        setBuildingStart.setOnItemSelectedListener(this);
        setFloorStart.setOnItemSelectedListener(this);
        setBuildingArrive.setOnItemSelectedListener(this);
        setFloorArrive.setOnItemSelectedListener(this);
        sendBtn.setOnClickListener(this);


    }

    /* 메소드 */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.equals(setStartPoint) || adapterView.equals(setArrivePoint)) {
            try {
                if (adapter.getItemId(i) != 0) {
                    if (adapterView.getId() == (R.id.Start) && setStartPoint.getId() != 0) {
                        resultS = adapterView.getSelectedItem() + "";
                    }

                    if (adapterView.getId() == (R.id.Arrive) && setArrivePoint.getId() != 0) {
                        resultA = adapterView.getSelectedItem() + "";
                    }
                }
                else
                {
                    resultS = null;
                    resultA = null;
                }
            } catch (Exception e) {
                Log.d("JB", e.getMessage());
            }
        } else if (adapterView.equals(setBuildingStart)) {
            //작동 여부 확인 Log.d("JB", true+"");
            if (BuildingS == null || FloorS == null) {
                BuildingS = adapterView.getSelectedItem() + "";
            } else {
                BuildingS = adapterView.getSelectedItem() + "";
                getIndex(BuildingS, FloorS,true);
            }
        } else if (adapterView.equals(setFloorStart)) {
            //작동 여부 확인 Log.d("JB", false+"");

            if (BuildingS == null || FloorS == null) {
                FloorS = adapterView.getSelectedItem() + "";
            } else {
                FloorS = adapterView.getSelectedItem() + "";
                getIndex(BuildingS, FloorS, true);
            }
        } else if (adapterView.equals(setBuildingArrive)) {
            if (BuildingA == null || Flooradapter == null) {
                BuildingA = adapterView.getSelectedItem() + "";
            } else {
                BuildingA = adapterView.getSelectedItem() + "";
               getIndex(BuildingA, FloorA, false);
            }
        } else if (adapterView.equals(setFloorArrive)) {
            //Log.d("JB", false+"");

            if (BuildingA == null || FloorA == null) {
                FloorA = adapterView.getSelectedItem() + "";
            } else {
                FloorA = adapterView.getSelectedItem() + "";
                getIndex(BuildingA, FloorA, false);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sender){
            if (resultS != null && resultA != null) {
                if (resultS.equals(resultA))
                    Toast.makeText(Main.this, "아잇~ 출발하는 곳하고 도착할 곳을 똑같이 주면 갈 수가 없잖아!", Toast.LENGTH_SHORT).show();
                else
                {
                    dbManager.toSend(getDeviceSSAID(), resultS, resultA);
                    Toast.makeText(this, "그럼 출발한다?", Toast.LENGTH_SHORT).show();
                }
                JsonParsing(getJsonString());
            }

            else
            {
                Toast.makeText(this, "아잇~ 제대로 된 장소를 알려달란 말이야", Toast.LENGTH_SHORT).show();
            }
        }


        if (view.getId() == R.id.Changer){
            Intent intent = new Intent(Main.this, SubActivity.class);
            startActivity(intent);
        }
    }

    /* 앱을 사용하는 기기의 SSAID 가져오는 메소드 (정상작동) */
    public String getDeviceSSAID() {
        return Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getJsonString() {
        String json = "";

        try {
            InputStream is = getAssets().open("Nodes.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    void JsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json); //json 포맷의 string 변수를 넘겨받아 jsonObject 생성

            JSONArray AreaArray = jsonObject.getJSONArray("Nodes");
            for (int i = 0; i < AreaArray.length(); i++) {
                JSONObject Node = AreaArray.getJSONObject(i); //출력하려면 JSONObject 형으로 바꿔줘야함
                Log.d("JB", Node.toString());
            }
        } catch (JSONException e) {
            Log.d("JB", e.getMessage());
            e.printStackTrace();
        }
    }

    /* true가 가는거 false가 오는거 */
    int getIndex(String Building, String Floor, boolean direction) {
        int B = Building.equals("교사동") ? 0 : 4;
        int F = 1;
        switch (Floor) {
            case "1층":
                F = 1;
                break;
            case "2층":
                F = 2;
                break;
            case "3층":
                F = 3;
                break;
            case "4층":
                F = 4;
                break;
        }
        ChangeSetters(B+F, direction);
        return B+F;
    }

    void ChangeSetters(int idx, boolean direction)
    {
        adapter = ArrayAdapter.createFromResource(this,
                adapters[idx], android.R.layout.simple_spinner_item);
        if(direction)
        {
            setStartPoint.setAdapter(adapter);
        }
        else
        {
            setArrivePoint.setAdapter(adapter);
        }
    }
}
