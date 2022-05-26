package com.example.pheasant;

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
import java.util.UUID;

public class Main extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    /* 맴버 변수 */
    Spinner setStartPoint;
    Spinner setArrivePoint;

    ArrayAdapter<CharSequence> adapter;

    Button sendBtn;

    String resultS = null; //시작점 전달해줄 변수
    String resultA = null; //도착점 전달해줄 변수

    final static String CACHE_DEVICE_ID = "CacheDeviceID";

    DatabaseManager dbManager;

    /* 메인 동작 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DatabaseManager();

        setStartPoint = findViewById(R.id.Start);
        setArrivePoint = findViewById(R.id.Arrive);
        sendBtn = findViewById(R.id.sender);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.Nodes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setStartPoint.setAdapter(adapter);
        setArrivePoint.setAdapter(adapter);

        setStartPoint.setOnItemSelectedListener(this);
        setArrivePoint.setOnItemSelectedListener(this);
        sendBtn.setOnClickListener(this);


    }

    /* 메소드 */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //n-1번째로 선택된 값 출력
        try {
            if (adapter.getItemId(i) != 0) {
                if (adapterView.getId() == (R.id.Start) && setStartPoint.getId() != 0) {
                    resultS = adapter.getItem(i).toString();
                }

                if (adapterView.getId() == (R.id.Arrive) && setArrivePoint.getId() != 0) {
                    resultA = adapter.getItem(i).toString();
                }
            }
        } catch (Exception e) {
            Log.d("JB", e.getMessage());
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (resultS != null && resultA != null) {
            if (resultS.equals(resultA))
                Toast.makeText(Main.this, "출발점과 도착점이 같을 수 없습니다!", Toast.LENGTH_SHORT).show();
            else
                dbManager.toSend(GetDeviceUUID(), resultS, resultA);

            JsonParsing(getJsonString());
        }
    }

    /* 앱을 사용하는 기기의 UUID를 가져오는 메소드 (정상작동) */
    public String GetDeviceUUID() {
        UUID deviceUUID = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Main.this);
        String cachedDeviceID = sharedPreferences.getString(CACHE_DEVICE_ID, "");

        if (cachedDeviceID != "") {
            deviceUUID = UUID.fromString(cachedDeviceID);
        } else {
            final String androidUniqueID = Settings.Secure.getString(Main.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                if (androidUniqueID != null) {
                    deviceUUID = UUID.nameUUIDFromBytes(androidUniqueID.getBytes("utf8"));
                } else {
                    deviceUUID = UUID.randomUUID();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        sharedPreferences.edit().putString(cachedDeviceID, deviceUUID.toString()).apply();
        return deviceUUID.toString();
    }

    public String getJsonString() {
        String json = "";

        try {
            InputStream is = getAssets().open("Nodes.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    void JsonParsing(String json){
            try {
                JSONObject jsonObject = new JSONObject(json); //json 포맷의 string 변수를 넘겨받아 jsonObject 생성

                JSONArray AreaArray = jsonObject.getJSONArray("Nodes");
                for (int i = 0; i < AreaArray.length(); i++)
                {
                    JSONObject Node = AreaArray.getJSONObject(i); //출력하려면 JSONObject 형으로 바꿔줘야함
                    Log.d("JB", Node.toString());
                }
            } catch (JSONException e) {
                Log.d("JB", e.getMessage());
                e.printStackTrace();
            }
    }
}
