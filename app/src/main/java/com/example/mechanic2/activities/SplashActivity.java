package com.example.mechanic2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mechanic2.R;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends Activity {

    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app.validateConnection(this, sweetAlertDialog, () -> {
            String entranceId = SharedPrefUtils.getStringData("entranceId");
            if (!entranceId.equals("-1")) {
                Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent1);
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        });
    }


}
