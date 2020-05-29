package com.example.mechanic2.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.AdsViewPagerFragment;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.example.mechanic2.interfaces.ThumbnailViewPagerState;
import com.example.mechanic2.models.Ads;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.views.MyTextView;
import com.google.gson.Gson;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShowGoodDetailActivity extends AppCompatActivity {
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;
    int ci = 0;
    private ExtensiblePageIndicator flexibleIndicator;
    private MyTextView goodName;
    private ImageView carIcon;
    private MyTextView suitableCars;
    private ImageView factoryIcon;
    private MyTextView companyName;
    private MyTextView countryName;
    private ImageView warrantyIcon;
    private MyTextView warrantyName;
    private ImageView stateIcon;
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_good_detail);
        viewpager = findViewById(R.id.viewpager);
        flexibleIndicator = findViewById(R.id.flexibleIndicator);
        goodName = findViewById(R.id.good_name);
        carIcon = findViewById(R.id.car_icon);
        suitableCars = findViewById(R.id.suitable_cars);
        factoryIcon = findViewById(R.id.factory_icon);
        companyName = findViewById(R.id.company_name);
        countryName = findViewById(R.id.country_name);
        warrantyIcon = findViewById(R.id.warranty_icon);
        warrantyName = findViewById(R.id.warranty_name);
        stateIcon = findViewById(R.id.state_icon);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ExtensiblePageIndicator extensiblePageIndicator = findViewById(R.id.flexibleIndicator);
        Goood goood = (Goood) getIntent().getSerializableExtra("good");

        if (goood == null) {
            return;
        }
        binder(goood);


        String thumbAddressesInString = goood.getThumbnails();
        String[] splitThumb = thumbAddressesInString.split(",");
        String[] splitAll = new String[splitThumb.length + 1];
        splitAll[0] = goood.getPreview();
        System.arraycopy(splitThumb, 0, splitAll, 1, splitThumb.length);

        renderData(splitAll);
        viewpager.setOffscreenPageLimit(3);

        extensiblePageIndicator.initViewPager(viewpager);

    }


    private void renderData(String[] body) {
        for (String url : body) {
            adapter.addFragment(ShowThumbnailFragment.newInstance(url));
        }

        viewpager.setAdapter(adapter);
        //pageSwitcher(body.length);
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
                    viewpager.setCurrentItem(ci);
                });
            }
        }, 1000, 5000);
    }



    void binder(Goood goood) {
        goodName.setText(goood.getGood_id());
        Gson gson = new Gson();
        Car[] cars = gson.fromJson(goood.getSuitable_car(), Car[].class);
        bindCars(cars);
        companyName.setText(goood.getCompany());
        countryName.setText(goood.getMade_by());
        warrantyName.setText(goood.getWarranty());
        if (goood.getIs_stock() == 0 && goood.getStatus() == 0) {
            stateIcon.setVisibility(View.VISIBLE);
        } else if (goood.getIs_stock() == 2 && goood.getStatus() == 1) {


            stateIcon.setVisibility(View.VISIBLE);
            stateIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_diamond));
            stateIcon.setColorFilter(activity.getResources().getColor(R.color.yellow_900));


        } else if (goood.getIs_stock() == 1 && goood.getStatus() == 1) {

            stateIcon.setVisibility(View.VISIBLE);
            stateIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_nis));
            stateIcon.setColorFilter(activity.getResources().getColor(R.color.red_full));

        } else if (goood.getIs_stock() == 0 && goood.getStatus() == 1) {
            stateIcon.setVisibility(View.INVISIBLE);
        }

    }

    private void bindCars(Car[] cars) {
        StringBuilder carsText = new StringBuilder();

        if (cars.length == 1) {
            suitableCars.setText(cars[0].getName());
            return;
        }
        for (int i = 0; i < cars.length; i++) {
            String connector;

            if (i == cars.length - 1) connector = "";
            else connector = "* ";
            carsText.append(cars[i].getName()).append(connector);
        }

        suitableCars.setText(carsText.toString());
    }
}

