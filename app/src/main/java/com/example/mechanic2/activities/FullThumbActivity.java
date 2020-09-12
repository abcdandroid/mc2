package com.example.mechanic2.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

public class FullThumbActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViewPager vpThumbnail;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_tumbnail);
        vpThumbnail = findViewById(R.id.vp_thumbnail);

        String[] linkList = getIntent().getStringArrayExtra("linkList");
        int currentItem = getIntent().getIntExtra("currentItem", 0);
        String from = getIntent().getStringExtra("from");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (linkList != null) {
            if (from.equals("answerActivity") || from.equals("showMechanicDetailActivity"))
                for (String s : linkList) {
                    adapter.addFragment(ShowThumbnailFragment.newInstance(getString(R.string.drmm) + s, null));
                }
            else if (from.equals("showGoodDetailActivity"))
                for (String s : linkList) {
                    adapter.addFragment(ShowThumbnailFragment.newInstance(s, null));
                }
        }
        vpThumbnail.setAdapter(adapter);
        vpThumbnail.setCurrentItem(currentItem);

        if (linkList.length > 1) {
            ExtensiblePageIndicator extensiblePageIndicator = findViewById(R.id.flexibleIndicator);
            extensiblePageIndicator.initViewPager(vpThumbnail);
        }
    }
}
