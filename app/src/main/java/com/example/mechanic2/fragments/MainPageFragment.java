package com.example.mechanic2.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.LoginActivity;
import com.example.mechanic2.activities.NewMechanicActivity2;
import com.example.mechanic2.adapters.MyFragmentStatePagerAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.models.Warranty;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainPageFragment extends Fragment implements View.OnClickListener {

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


    public MainPageFragment() {
        // Required empty public constructor
    }

    public MainPageFragment(int type) {
        this.type = type;
    }

    public MainPageFragment(int type, String mechanicInfo) {
        this.mechanicInfo = mechanicInfo == null ? "" : mechanicInfo;
        this.type = type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        return init(view);
    }

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
    MyFragmentStatePagerAdapter adapterPlace1;
    MyFragmentStatePagerAdapter adapterPlace2;
    MyFragmentStatePagerAdapter adapterPlace3;
    MyFragmentStatePagerAdapter adapterPlace4;
    MyFragmentStatePagerAdapter adapterPlace6;
    MyFragmentStatePagerAdapter adapterPlace7;
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

    private View init(View view) {

        adapterPlace1 = new MyFragmentStatePagerAdapter(getActivity().getSupportFragmentManager(), 0);
        adapterPlace2 = new MyFragmentStatePagerAdapter(getActivity().getSupportFragmentManager(), 0);
        adapterPlace3 = new MyFragmentStatePagerAdapter(getActivity().getSupportFragmentManager(), 0);
        adapterPlace4 = new MyFragmentStatePagerAdapter(getActivity().getSupportFragmentManager(), 0);
        adapterPlace6 = new MyFragmentStatePagerAdapter(getActivity().getSupportFragmentManager(), 0);
        adapterPlace7 = new MyFragmentStatePagerAdapter(getActivity().getSupportFragmentManager(), 0);


        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        navView = view.findViewById(R.id.nav_view);
        appbar = view.findViewById(R.id.appbar);
        hamburgerButton = view.findViewById(R.id.hamburger_button);


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
            hamburgerButton.setTag(1);
            this.mechanicInfo = mechanicInfo == null ? "" : mechanicInfo;
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
                    if (mechanic.getMechanic_image().length() > 0)
                        Glide.with(getActivity()).load("http://drkamal3.com/Mechanic/" + mechanic.getMechanic_image()).into(circleImageView);
                    else {
                        if (getActivity() != null)
                            circleImageView.setImageDrawable(getActivity().getDrawable(R.drawable.mechanic_avatar));
                    }
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
            navView.setVisibility(View.GONE);
            mDrawerLayout.removeView(navView);
            hamburgerButton.setTag(0);
        }


        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(1))
                    mDrawerLayout.openDrawer(GravityCompat.END);
                else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext());
                    View inflate = LayoutInflater.from(getContext()).inflate(R.layout.normal_user_info, null);
                    sweetAlertDialog.setCustomView(inflate);
                    sweetAlertDialog.hideConfirmButton();
                    sweetAlertDialog.show();
                }
            }
        });


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
        return view;
    }


    private void getData() {
        SweetAlertDialog loadingData = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setContentText("در حال دریافت اطلاعات").setTitleText("لطفا شکیبا باشید.");
        loadingData.show();
        Map<String, String> map = new HashMap<>();
        map.put("route", "getMainPageData");
        Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (loadingData != null)
                    loadingData.dismissWithAnimation();
                try {
                    if (response.body() != null) {
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
                                    adapterPlace1.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace1.notifyDataSetChanged();
                                    app.l("jlengbbb");
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
                        LocalBroadcastManager.getInstance(MainPageFragment.this.getContext()).sendBroadcast(intent);
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

                Intent intent = new Intent(getActivity(), NewMechanicActivity2.class);
                if (mechanicInfo.length() == 0) {
                    intent.putExtra("id", mechanicId);
                } else {
                    intent.putExtra("mechanicInfo", mechanic);
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
                SharedPrefUtils.clear();
                Objects.requireNonNull(getActivity()).finish();
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }

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