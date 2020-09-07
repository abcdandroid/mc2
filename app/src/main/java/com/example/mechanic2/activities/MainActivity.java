package com.example.mechanic2.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.AdminFragment;
import com.example.mechanic2.fragments.MainPageFragment;
import com.example.mechanic2.fragments.MechanicFragment;
import com.example.mechanic2.fragments.QuestionFragment;
import com.example.mechanic2.fragments.StoreFragment;
import com.example.mechanic2.interfaces.UpdateNavBar;
import com.example.mechanic2.models.Etcetera;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    ViewPager viewpager;
    BottomNavigationView bottomNavigationView;
    boolean searchPressed;

    boolean isFabPressed;
    private SweetAlertDialog sweetAlertDialog;
    private int type;
    private String mechanicInfo;

    public static UpdateNavBar updateNavBar;

    //پرداخت مبلغ فونت ایران سنس به کار رفته
    public static List<Etcetera> etcetera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.main_page);
        if (checkPermission()) {
            Map<String, String> map = new HashMap<>();
            map.put("route", "getEtcData");
            Application.getApi().getEtcetera(map).enqueue(new Callback<List<Etcetera>>() {
                @Override
                public void onResponse(Call<List<Etcetera>> call, Response<List<Etcetera>> response) {
                    etcetera=response.body();
                    myAction(); /**/
                }

                @Override
                public void onFailure(Call<List<Etcetera>> call, Throwable t) {

                }
            });
        }
    }


    private void myAction() {


        this.type = SharedPrefUtils.getIntData("type");
        this.mechanicInfo = SharedPrefUtils.getStringData("mechanicInfo");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("mp"));
        bottomNavigationView.setSelectedItemId(R.id.main_page);
        loadMainPageFragment(type, mechanicInfo);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.mechanics:
                    app.loadFragment(this, new MechanicFragment());
                    break;
                case R.id.home:
                    app.loadFragment(this, new AdminFragment());
                    break;
                case R.id.questions:
                    app.loadFragment(this, new QuestionFragment());
                    break;
                case R.id.store:
                    app.loadFragment(this, new StoreFragment());
                    break;
                case R.id.main_page:
                    loadMainPageFragment(type, mechanicInfo);
                    break;
            }
            return false;
        });
        //bottomNavigationView.setSelectedItemId(R.id.main_page);
        updateNavBar = new UpdateNavBar() {
            @Override
            public void setSelectedItem(int field) {
                switch (field) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.mechanics);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.questions);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.main_page);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.store);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                }
            }
        };
        app.l("starteddd6");
    }

    private void loadMainPageFragment(int type, String mechanicInfo) {
        MainPageFragment mainPageFragment1;
        if (type == 0 || (type == 1 && mechanicInfo.equals("-1"))) {
            mainPageFragment1 = new MainPageFragment(type);
        } else if (type == 1 && !mechanicInfo.equals("-1")) {
            mainPageFragment1 = new MainPageFragment(type, mechanicInfo);
        } else mainPageFragment1 = new MainPageFragment(type);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mainPageFragment1);
        fragmentTransaction.commitAllowingStateLoss();

    }

/*    private void loadMainPageFragment(int type, String mechanicInfo) {
        MainPageFragment mainPageFragment;
        if (type == 0 || (type == 1 && mechanicInfo.equals("-1"))) {
            mainPageFragment = MainPageFragment.newInstance(type);
        } else if (type == 1 && !mechanicInfo.equals("-1")) {
            mainPageFragment = MainPageFragment.newInstance(type, mechanicInfo);
        } else mainPageFragment = MainPageFragment.newInstance(type);

        app.loadFragment(this,mainPageFragment);
    }*/

    private Boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

            if (result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
            }
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        app.l("ppoottmm" + requestCode);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                myAction();
            } else {
                app.t("not granted");
            }
        } else if (requestCode == MechanicFragment.REQUEST_CODE) {
            app.l("fgps" + grantResults[grantResults.length - 1]);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("forGps")  /**/;
                LocalBroadcastManager.getInstance(Application.getContext()).sendBroadcast(intent);
            }
        } else if (requestCode == ShowMechanicDetailActivity.CALL_REQUEST_CODE) {
            app.l("ppoottmm1");
            Intent intent = new Intent("forCall")  /**/;
            LocalBroadcastManager.getInstance(Application.getContext()).sendBroadcast(intent);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        int id = bottomNavigationView.getSelectedItemId();
        if (id == R.id.main_page) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.main_page);
            loadMainPageFragment(type, mechanicInfo);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            bottomNavigationView.setVisibility(View.GONE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            bottomNavigationView.setVisibility(View.VISIBLE);

        }
    }

    private int selected = 5;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            selected = intent.getIntExtra("selected", 0);
            String detail = intent.getStringExtra("detail");
            Menu menu = bottomNavigationView.getMenu();
            menu.getItem(selected).setChecked(true);

//            switch (selected) {
//                case 0:
//                    bottomNavigationView.setSelectedItemId(R.id.mechanics);
//                    break;
//                case 1:
//                    bottomNavigationView.setSelectedItemId(R.id.questions);
//                    break;
//                case 2:
//                    bottomNavigationView.setSelectedItemId(R.id.main_page);
//                    break;
//                case 3:
//                    bottomNavigationView.setSelectedItemId(R.id.store);
//                    break;
//                case 4:
//                    bottomNavigationView.setSelectedItemId(R.id.home);
//                    break;
//            }
        }
    };


}
