package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.MainPageFragment;
import com.example.mechanic2.fragments.MainPageItemFragment;
import com.example.mechanic2.models.Mechanic;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageFragmentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private VideoView videoView;
    private int cachedHeight;
    private int currentOrientation;
    private int type;
    private String mechanicInfo;
    private int place5VideoViewHeight;
    private int place5VideoViewML;
    private int place5VideoViewMR;
    private int place5VideoViewMB;
    private int place5VideoViewMT;
    private CircularImageView circleImageView;
    int mechanicId;
    private Mechanic mechanic;

    private RelativeLayout parent;
    private CardView containerP1;
    private ViewPager place1;
    private CardView containerP2;
    private ViewPager place2;
    private LinearLayout containerP34;
    private ViewPager place3;
    private ViewPager place4;
    private VideoView place5VideoView;
    private LinearLayout containerP67;
    private ViewPager place6;
    private ViewPager place7;
    ViewPagerAdapter adapterPlace1;
    ViewPagerAdapter adapterPlace2;
    ViewPagerAdapter adapterPlace3;
    ViewPagerAdapter adapterPlace4;
    ViewPagerAdapter adapterPlace6;
    ViewPagerAdapter adapterPlace7;
    private CoordinatorLayout coordinatorLayout;

    private Toolbar toolbar;
    private ListView listView;
    private DrawerLayout mDrawerLayout;


    private ImageView hamburgerButton;
    String[] titles;
    int[] images;
    private TextView navMechanicName;
    private TextView navStoreName;
    private TextView whatsUpIntent;
    private LinearLayout editInfo;
    private LinearLayout contactUs;
    private LinearLayout aboutUs;
    private LinearLayout biKhial;
    private LinearLayout signOut;


    private NavigationView navView;

    private AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_fragment);
        type = getIntent() != null ? getIntent().getIntExtra("type", 0) : 0;
        mechanicInfo = getIntent().getStringExtra("mechanicInfo") == null ? "" : getIntent().getStringExtra("mechanicInfo");
        init(this);
    }


    private void init(Activity view) {

        adapterPlace1 = new ViewPagerAdapter(getSupportFragmentManager());
        adapterPlace2 = new ViewPagerAdapter(getSupportFragmentManager());
        adapterPlace3 = new ViewPagerAdapter(getSupportFragmentManager());
        adapterPlace4 = new ViewPagerAdapter(getSupportFragmentManager());
        adapterPlace6 = new ViewPagerAdapter(getSupportFragmentManager());
        adapterPlace7 = new ViewPagerAdapter(getSupportFragmentManager());


        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        navView = view.findViewById(R.id.nav_view);
        appbar = view.findViewById(R.id.appbar);
        hamburgerButton = view.findViewById(R.id.hamburger_button);


        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });


        circleImageView = view.findViewById(R.id.nav_mechanic_image);
        navMechanicName = view.findViewById(R.id.nav_mechanic_name);
        navStoreName = view.findViewById(R.id.nav_store_name);

        whatsUpIntent = view.findViewById(R.id.whats_up_intent);
        editInfo = view.findViewById(R.id.edit_info);
        contactUs = view.findViewById(R.id.contact_us);
        aboutUs = view.findViewById(R.id.about_us);
        biKhial = view.findViewById(R.id.bi_khial);
        signOut = view.findViewById(R.id.sign_out);


        whatsUpIntent.setOnClickListener(this);
        editInfo.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        biKhial.setOnClickListener(this);
        signOut.setOnClickListener(this);


        if (type == 1) {

            appbar.setVisibility(View.VISIBLE);
            navView.setVisibility(View.VISIBLE);
            if (mechanicInfo.length() == 0) {
                mechanicId = Integer.parseInt(SharedPrefUtils.getStringData("m_id"));
                Uri parsedUri = Uri.parse(SharedPrefUtils.getStringData("imageNo" + 3));
                circleImageView.setImageURI(parsedUri);
                navStoreName.setText(SharedPrefUtils.getStringData("mechanicStoreName"));
                navMechanicName.setText(SharedPrefUtils.getStringData("mechanicName"));

            } else {
                app.l(mechanicInfo + "mechanicInfofff");
                mechanic = new Gson().fromJson(mechanicInfo, Mechanic.class);
                mechanicId = mechanic.getId();
                if (SharedPrefUtils.getStringData("imageNo3").equals("-1"))
                    Glide.with(this).load("http://drkamal3.com/Mechanic/" + mechanic.getMechanic_image()).into(circleImageView);
                else {
                    Uri parsedUri = Uri.parse(SharedPrefUtils.getStringData("imageNo" + 3));
                    circleImageView.setImageURI(parsedUri);
                }
                navMechanicName.setText(mechanic.getName());
                navStoreName.setText(mechanic.getStore_name());
            }

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.t("aA");
                }
            });

        } else {
            appbar.setVisibility(View.GONE);
            navView.setVisibility(View.GONE);
        }

        containerP1 = view.findViewById(R.id.container_p1);
        place1 = view.findViewById(R.id.place_1);
        containerP2 = view.findViewById(R.id.container_p2);
        place2 = view.findViewById(R.id.place_2);
        containerP34 = view.findViewById(R.id.container_p34);
        place3 = view.findViewById(R.id.place_3);
        place4 = view.findViewById(R.id.place_4);
        place5VideoView = view.findViewById(R.id.place_5_video_view);
        place5VideoView.showControls();
        containerP67 = view.findViewById(R.id.container_p67);
        place6 = view.findViewById(R.id.place_6);
        place7 = view.findViewById(R.id.place_7);
        parent = view.findViewById(R.id.parent);

        place5VideoView.post(new Runnable() {
            @Override
            public void run() {
                place5VideoViewHeight = place5VideoView.getHeight();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) place5VideoView.getLayoutParams();
                place5VideoViewML = layoutParams.leftMargin;
                place5VideoViewMR = layoutParams.rightMargin;
                place5VideoViewMB = layoutParams.bottomMargin;
                place5VideoViewMT = layoutParams.topMargin;


            }
        });

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);

        place1.setAdapter(adapterPlace1);
        place2.setAdapter(adapterPlace2);
        place3.setAdapter(adapterPlace3);
        place4.setAdapter(adapterPlace4);
        place6.setAdapter(adapterPlace6);
        place7.setAdapter(adapterPlace7);
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("route", "getMainPageData");
        Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.body() != null) {
                        place2.setSaveFromParentEnabled(false);
                        place3.setSaveFromParentEnabled(false);
                        place4.setSaveFromParentEnabled(false);
                        place6.setSaveFromParentEnabled(false);
                        place7.setSaveFromParentEnabled(false);
                        JSONArray jsonArray = new JSONArray(response.body());
                        app.l(jsonArray.length() + "jleng");
                        int len = jsonArray.length();

                        for (int i = 0; i < len; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getInt("field") == 8) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    place5VideoView.setVideoURI(Uri.parse(jsonObject.getString("view_url")));
                                } else {
                                    place5VideoView.setVisibility(View.GONE);
                                }
                            }
                            if (jsonObject.getInt("place") == 1) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    place1.setSaveFromParentEnabled(false);
                                    adapterPlace1.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace1.notifyDataSetChanged();
                                } else {
                                    containerP1.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 2) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    adapterPlace2.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace2.notifyDataSetChanged();
                                } else {
                                    containerP2.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 3) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    adapterPlace3.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace3.notifyDataSetChanged();
                                } else {
                                    place3.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 4) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    adapterPlace4.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace4.notifyDataSetChanged();
                                } else {
                                    place4.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 6) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    adapterPlace6.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace6.notifyDataSetChanged();
                                } else {
                                    place6.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 7) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    adapterPlace7.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace7.notifyDataSetChanged();
                                } else {
                                    place7.setVisibility(View.GONE);
                                }
                            }
                        }

                        Intent intent = new Intent("dataCount");
                        intent.putExtra("ref", "mpf");
                        LocalBroadcastManager.getInstance(MainPageFragmentActivity.this).sendBroadcast(intent);
                    } else {
                        app.l(response.body() + "mmppgg2");
                    }
                } catch (JSONException e) {
                    app.l(e.getLocalizedMessage() + "mmppgg2");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                app.l(t.getLocalizedMessage() + "mmppgg2");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info:
                Intent intent = new Intent(this, NewMechanicActivity2.class);
                if (mechanicInfo.length() == 0) {
                    intent.putExtra("id", mechanicId);
                    app.l("adv");
                } else {
                    intent.putExtra("mechanicInfo", mechanic);
                    app.l("adv2");
                }
                startActivity(intent);
                break;
            case R.id.whats_up_intent:
                app.l("b");
                break;
            case R.id.contact_us:
                app.l("c");
                break;
            case R.id.about_us:
                app.l("d");
                break;
            case R.id.bi_khial:
                app.l("e");
                break;
            case R.id.sign_out:
                app.l("f");
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.item_nav_list, null);
            } else {
                view = convertView;
            }
            TextView textView = view.findViewById(R.id.list_text);
            ImageView imageView = view.findViewById(R.id.list_image);
            textView.setText(titles[position]);
            imageView.setImageResource(images[position]);
            return view;
        }
    }

   /*  private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {

            switch (position) {
                case 1:
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;
                case 4:
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;
            }
        }
    }

   @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoView.setScaleType(ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(layoutParams);
            app.l("A");
        } else {
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            videoView.setLayoutParams(layoutParams);
            app.l("B");
        }


    }

    private void setupVideoView(View view) {
        // Make sure to use the correct VideoView import
        //videoView = (VideoView) view.findViewById(R.id.video_view);
        videoView.setScaleType(ScaleType.FIT_XY);
        setVideoAreaSize();

        videoView.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play
        videoView.setVideoURI(Uri.parse("https://hw14.cdn.asset.aparat.com/aparat-video/3dca0f2ea6e4707bfc534d2cd0899db023225996-480p.mp4"));
    }

    @Override
    public void onPrepared() {
        //Starts the video playback as soon as it is ready
        // videoView.start();
    }

    private void setVideoAreaSize() {
        videoView.post(new Runnable() {
            @Override
            public void run() {
                int width = videoView.getWidth();
                            cachedHeight = (int) (width * 405f / 720f);
                //             cachedHeight = (int) (width * 3f / 4f);
                // cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = videoView.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                videoView.setLayoutParams(videoLayoutParams);
            }
        });
    }
*//*
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);

      // Checking the orientation of the screen
      if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


          //First Hide other objects (listview or recyclerview), better hide them using Gone.
          FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) place5VideoView.getLayoutParams();
          params.width=params.MATCH_PARENT;
          params.height=params.MATCH_PARENT;
          place5VideoView.setLayoutParams(params);
      } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

          //unhide your objects here.
          FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) place5VideoView.getLayoutParams();
          params.width=params.MATCH_PARENT;
          params.height=app.dpToPixels(240,0);
          place5VideoView.setLayoutParams(params);
      }
  }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setVisibility(View.GONE);
            }
            appbar.setVisibility(View.GONE);
            place5VideoView.setVisibility(View.VISIBLE);


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) place5VideoView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            params.rightMargin = 0;
            params.leftMargin = 0;
            params.bottomMargin = 0;
            params.topMargin = 0;
            place5VideoView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setVisibility(View.VISIBLE);
            }
            appbar.setVisibility(View.VISIBLE);

            place5VideoView.showControls();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) place5VideoView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = place5VideoViewHeight;
            params.rightMargin = place5VideoViewMR;
            params.leftMargin = place5VideoViewML;
            params.bottomMargin = place5VideoViewMB;
            params.topMargin = place5VideoViewMT;


            place5VideoView.setLayoutParams(params);
        }
    }
}