package com.example.pheasant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.*;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    Double Spd = 0.0;
    final static Double SPD_INCREMENT = 0.5;
    Double spinSpd = 0.0;
    final static Double SPIN_SPD_INCREMENT = 0.5;
    final static Double DEFAULT_SPD = 10d;
    DatabaseManager dbManager;
    StorageManager strManager;
    ImageView map;
    TextView txtSpd;
    TextView txtSpinSpd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        findViewById(R.id.Closer).setOnClickListener(this);

        findViewById(R.id.Base).setOnClickListener(this);
        findViewById(R.id.btnForward).setOnClickListener(this);
        findViewById(R.id.btnBackward).setOnClickListener(this);
        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnIncrease).setOnClickListener(this);
        findViewById(R.id.btnDecrease).setOnClickListener(this);
        findViewById(R.id.btnSpinInc).setOnClickListener(this);
        findViewById(R.id.btnSpinInDec).setOnClickListener(this);
        findViewById(R.id.btnMod1).setOnClickListener(this);
        findViewById(R.id.btnMod2).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        map = findViewById(R.id.map);

        txtSpd = findViewById(R.id.txtSpd);
        txtSpinSpd = findViewById(R.id.txtSpinSpd);

        dbManager = new DatabaseManager(getDeviceSSAID());
        strManager = new StorageManager();

        Spd = DEFAULT_SPD;
        spdClear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Closer:
                finish();
                break;
            case R.id.Base:
                Spd = 0.0;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.MOVE_STOP, Spd)) {
                    Toast.makeText(this, "Error!(0)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnForward:
                Spd = Spd == 0 ? DEFAULT_SPD : Spd;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.MOVE_FORWARD, Spd)) {
                    Toast.makeText(this, "Error!(1)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnBackward:
                Spd = Spd == 0 ? DEFAULT_SPD : Spd;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.MOVE_BACKWARD, Spd)) {
                    Toast.makeText(this, "Error!(2)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnLeft:
                Spd = Spd == 0 ? DEFAULT_SPD : Spd;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.MOVE_LEFT, Spd)) {
                    Toast.makeText(this, "Error!(3)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRight:
                Spd = Spd == 0 ? DEFAULT_SPD : Spd;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.MOVE_RIGHT, Spd)) {
                    Toast.makeText(this, "Error!(4)", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnIncrease:
                Spd += SPD_INCREMENT;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.SPD_INCREASE, Spd)) {
                    Toast.makeText(this, "Error!(5)", Toast.LENGTH_SHORT).show();
                }
                spdClear();
                break;
            case R.id.btnDecrease:
                Spd -= SPD_INCREMENT;
                Spd = Spd <= 0 ? 0 : Spd;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.SPD_DECREASE, Spd)) {
                    Toast.makeText(this, "Error!(6)", Toast.LENGTH_SHORT).show();
                }
                spdClear();
                break;
            case R.id.btnSpinInc:
                spinSpd += SPIN_SPD_INCREMENT;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.SPIN_SPD_INCREASE, spinSpd)) {
                    Toast.makeText(this, "Error!(7)", Toast.LENGTH_SHORT).show();
                }
                spdClear();
                break;
            case R.id.btnSpinInDec:
                spinSpd -= SPIN_SPD_INCREMENT;
                spinSpd = spinSpd <= 0 ? 0 : spinSpd;
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.SPIN_SPD_DECREASE, spinSpd)) {
                    Toast.makeText(this, "Error!(8)", Toast.LENGTH_SHORT).show();
                }
                spdClear();
                break;
            case R.id.btnMod1:
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.MOD_CHANGE_TO_1, 1)) {
                    Toast.makeText(this, "Error!(7)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnMod2:
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.MOD_CHANGE_TO_2, 2)) {
                    Toast.makeText(this, "Error!(7)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSave:
                Glide.with(SubActivity.this).load("gs://la-prova.appspot.com/images/2fa942d1d424d8f97f79e9cfb85ad6f8.jpg").into(map);
                if (!dbManager.toSend(getDeviceSSAID(), dbManager.SAVE_PROC, Spd)) {
                    Toast.makeText(this, "Error!(9)", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public String getDeviceSSAID() {
        return Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void spdClear(){
        txtSpd.setText(Spd+"");
        txtSpinSpd.setText(spinSpd+"");
    }
}
