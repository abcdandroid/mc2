package com.example.mechanic2.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Etcetera;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity {

    SweetAlertDialog sweetAlertDialog;
    public static List<Etcetera> etcetera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("در حال دریافت اطلاعات");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        handleIntent(getIntent());
        // ATTENTION: This was auto-generated to handle app links.

    }

    private void handleIntent(Intent intent) {
        Uri appLinkData = intent.getData();
        app.validateConnection(this, null, () -> {
            Map<String, String> map = new HashMap<>();
            map.put("route", "getEtcData");
            Application.getApi().getEtcetera(map).enqueue(new Callback<List<Etcetera>>() {
                @Override
                public void onResponse(Call<List<Etcetera>> call, Response<List<Etcetera>> response) {
                    etcetera = response.body();
                    sweetAlertDialog.dismissWithAnimation();
                    String entranceId = SharedPrefUtils.getStringData("entranceId");
                    if (!entranceId.equals("-1")) {
                        Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                        if (appLinkData != null) {
                            intent1.putExtra("linked", true);
                        }
                        startActivity(intent1);
                    } else {
                        SharedPrefUtils.clear();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<List<Etcetera>> call, Throwable t) {

                }
            });


        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
}
