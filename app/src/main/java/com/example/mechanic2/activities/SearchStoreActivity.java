package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.GoodAutoCompleteAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.AdsViewPagerFragment;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.example.mechanic2.models.Ads;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchStoreActivity extends AppCompatActivity implements View.OnClickListener {

    //http://drkamal3.com/Mechanic/index.php?route=getAds

    private ViewPager viewPagerAds;
    private ViewPagerAdapter adapter;
    private AutoCompleteTextView carName;
    private AutoCompleteTextView goodName;
    private TextView submitSearch;


    int ci = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);
        initViews();
        getData();
    }

    private void initViews() {

        viewPagerAds = findViewById(R.id.viewPagerAds);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        carName = findViewById(R.id.carName);
        goodName = findViewById(R.id.goodName);
        submitSearch = findViewById(R.id.submitSearch);
        carName.setAdapter(new CarAutoCompleteAdapter(Application.getContext(), R.layout.item_show_auto_complete));
        carName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                app.l(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString() + "QQWW");
            }
        });
        goodName.setAdapter(new GoodAutoCompleteAdapter(Application.getContext(), R.layout.item_show_auto_complete));
        submitSearch.setOnClickListener(this);
    }

    private void getData() {
        Application.getApi().getAdsList("getAds").enqueue(new Callback<List<Ads>>() {
            @Override
            public void onResponse(Call<List<Ads>> call, Response<List<Ads>> response) {
                assert response.body() != null;
                renderData(response.body());
            }

            @Override
            public void onFailure(Call<List<Ads>> call, Throwable t) {

            }
        });
    }

    private void renderData(List<Ads> body) {
        for (Ads ads : body) {
            adapter.addFragment(AdsViewPagerFragment.newInstance(ads));
        }

        viewPagerAds.setAdapter(adapter);
        pageSwitcher(body.size());
    }


    public void pageSwitcher(int size) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ci++;
                if (ci == size)
                    ci = 0;
                runOnUiThread(() -> {
                    app.l(ci);
                    viewPagerAds.setCurrentItem(ci);
                });
            }
        }, 1000, 5000);
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.fieldCarName).setVisibility(View.GONE);
        findViewById(R.id.fieldGoodName).setVisibility(View.GONE);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {


        Intent intent = new Intent("aa");
        intent.putExtra("carName", carName.getText().toString());
        intent.putExtra("goodName", goodName.getText().toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }


}
