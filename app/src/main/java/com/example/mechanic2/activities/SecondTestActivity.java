package com.example.mechanic2.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.mechanic2.R;
import com.example.mechanic2.interfaces.OnTagCrossClickListener;

public class SecondTestActivity extends AppCompatActivity {

    CoordinatorLayout parentSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_test);
        parentSecond = findViewById(R.id.parent_second);

    }

}