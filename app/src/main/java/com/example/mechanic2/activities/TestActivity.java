package com.example.mechanic2.activities;


import android.app.Activity;
import android.icu.util.Measure;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;
import com.j256.ormlite.stmt.query.In;
import com.nightonke.wowoviewpager.Animation.ViewAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoPositionAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoRotationAnimation;
import com.nightonke.wowoviewpager.Enum.Ease;
import com.nightonke.wowoviewpager.WoWoViewPager;
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TestActivity extends Activity {




    NumberPicker np1, np2;
    TextView tv1;

    String selectedValue = "Select Value";
    String selectedBrand = "Select Brand";

    int priceApple = 3000;
    int priceLG = 1000;
    int priceSONY = 1500;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv1 = findViewById(R.id.tv1);
        np1 = findViewById(R.id.np1);
        np2 = findViewById(R.id.np2);

        final String[] mBrand = {"Select Brand", "Apple", "LG", "SONY"};
        np1.setMinValue(0);
        np1.setMaxValue(mBrand.length - 1);
        np1.setWrapSelectorWheel(true);
        np1.setDisplayedValues(mBrand);
        np1.setValue(0);

        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedBrand = mBrand[newVal];
                calculateCost(selectedBrand, selectedValue);

            }
        });

        final String[] mValue = {"Select Value", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        np2.setMinValue(0);
        np2.setMaxValue(mValue.length - 1);
        np2.setWrapSelectorWheel(true);
        np2.setDisplayedValues(mValue);
        np2.setValue(0);

        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedValue = mValue[newVal];
                calculateCost(selectedBrand, selectedValue);
            }
        });
    }

    void calculateCost(String selectedBrand, String selectedValue) {
        if (!selectedValue.equals("Select Value")) {
            switch (selectedBrand) {
                case "Apple":
                    app.l(selectedValue + priceApple);
                    tv1.setText("price= " + priceApple * Integer.parseInt(selectedValue));
                    break;
                case "LG":
                    app.l(selectedValue + priceLG);
                    tv1.setText("price= " + priceLG * Integer.parseInt(selectedValue));
                    break;
                case "SONY":
                    tv1.setText("price= " + priceSONY * Integer.parseInt(selectedValue));
                    app.l(selectedValue + priceSONY);
                    break;
                default:
                    tv1.setText("Error");
                    break;
            }
        } else tv1.setText("Error");


    }
}
