package com.example.mechanic2.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.BuildConfig;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.LoginActivity;
import com.example.mechanic2.activities.NewMechanicActivity2;
import com.example.mechanic2.activities.PrivacyActivity;
import com.example.mechanic2.activities.SplashActivity;
import com.example.mechanic2.adapters.MyFragmentStatePagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AlertAction;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.models.ViewpagerData;
import com.example.mechanic2.views.MyTextView;
import com.github.ybq.android.spinkit.SpinKitView;
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
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainPageFragment extends Fragment implements View.OnClickListener {

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

    private SpinKitView loadingImageProfile;
    private ImageView retryImageProfile;
    private SweetAlertDialog sweetAlertDialogInvalidLength;
    private static boolean alarmShowed = false;

    public MainPageFragment() {

    }

    public MainPageFragment(int type) {
        this.type = type;
    }

    public MainPageFragment(int type, String mechanicInfo) {
        this.mechanicInfo = mechanicInfo == null ? "" : mechanicInfo;
        this.type = type;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SplashActivity.etcetera != null && Integer.parseInt(SplashActivity.etcetera.get(0).getMessage().substring(1)) != BuildConfig.VERSION_CODE/**/) {
            if (alarmShowed) return;
            LottieAnimationView warrantyLt;
            MyTextView txt;
            RelativeLayout btnContactUs;
            MyTextView txtOk;
            RelativeLayout btnShowAllGoods;
            MyTextView cancelAction;

            SweetAlertDialog exitDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null);
            exitDialog.setCustomView(view1);
            exitDialog.hideConfirmButton();

            warrantyLt = view1.findViewById(R.id.warranty_lt);
            warrantyLt.setVisibility(View.GONE);

            txt = view1.findViewById(R.id.txt);
            btnContactUs = view1.findViewById(R.id.btn_contact_us);
            txtOk = view1.findViewById(R.id.txt_ok);
            btnShowAllGoods = view1.findViewById(R.id.btn_show_all_goods);
            cancelAction = view1.findViewById(R.id.cancel_action);


            txt.setText("نسخه جدیدی از برنامه اومده");

            txtOk.setText("آپدیت کن");
            cancelAction.setText("بعدا");

            cancelAction.setVisibility(View.VISIBLE);
            btnShowAllGoods.setVisibility(View.VISIBLE);

            btnContactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = SplashActivity.etcetera.get(1).getMessage();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    getActivity().startActivity(i);
                    alarmShowed = true;
                }
            });

            btnShowAllGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitDialog.dismissWithAnimation();
                    alarmShowed = true;
                }
            });


            exitDialog.show();
            /* */
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        return init(view);
    }

    private RelativeLayout parent;
    private CardView containerP1;
    private LinearLayout containerP34;
    private CardView containerP2;
    private AutoScrollViewPager place1;
    private AutoScrollViewPager place2;
    private AutoScrollViewPager place3;
    private AutoScrollViewPager place4;
    private AutoScrollViewPager place6;
    private AutoScrollViewPager place7;
    private LinearLayout containerP67;
    private VideoView place5VideoView;


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
    private LinearLayout text;
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
        loadingImageProfile = view.findViewById(R.id.loading_imageProfile);
        retryImageProfile = view.findViewById(R.id.retryImageProfile);
        loadingImageProfile.setVisibility(View.GONE);
        retryImageProfile.setVisibility(View.GONE);


        circleImageView = view.findViewById(R.id.nav_mechanic_image);
        navMechanicName = view.findViewById(R.id.nav_mechanic_name);
        navStoreName = view.findViewById(R.id.nav_store_name);

        whatsUpIntent = view.findViewById(R.id.whats_up_intent);
        editInfo = view.findViewById(R.id.edit_info);
        contactUs = view.findViewById(R.id.contact_us);
        aboutUs = view.findViewById(R.id.about_us);
        biKhial = view.findViewById(R.id.bi_khial);
        signOut = view.findViewById(R.id.sign_out);
        text = view.findViewById(R.id.text);

        whatsUpIntent.setOnClickListener(this);
        editInfo.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        biKhial.setOnClickListener(this);
        signOut.setOnClickListener(this);
        text.setOnClickListener(this);


        if (type == 1) {
            hamburgerButton.setTag(1);
            this.mechanicInfo = mechanicInfo == null ? "" : mechanicInfo;
            appbar.setVisibility(View.VISIBLE);
            navView.setVisibility(View.VISIBLE);
            if (mechanicInfo.length() == 0) {
                mechanicId = Integer.parseInt(SharedPrefUtils.getStringData("m_id"));

                String stringData = SharedPrefUtils.getStringData("imageNo" + 3);
                if (!stringData.trim().equals("-1")) {
                    Uri parsedUri = Uri.parse(stringData);
                    circleImageView.setImageURI(parsedUri);
                } else {
                    circleImageView.setImageDrawable(getActivity().getDrawable(R.drawable.mechanic_avatar));
                }
                navStoreName.setText(SharedPrefUtils.getStringData("mechanicStoreName"));
                navMechanicName.setText(SharedPrefUtils.getStringData("mechanicName"));

            } else {

                mechanic = new Gson().fromJson(mechanicInfo, Mechanic.class);
                mechanicId = mechanic.getId();
                if (SharedPrefUtils.getStringData("imageNo3").equals("-1"))
                    if (mechanic.getMechanic_image().length() > 0) {

                        loadImage(getString(R.string.drweb) + mechanic.getMechanic_image(), circleImageView, loadingImageProfile, retryImageProfile);
                    } else {

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
                    sweetAlertDialog.hideConfirmButton();
                    LinearLayout editInfoNormalUser = inflate.findViewById(R.id.edit_info);
                    LinearLayout contactUsNormalUser = inflate.findViewById(R.id.contact_us);
                    LinearLayout aboutUsNormalUser = inflate.findViewById(R.id.about_us);
                    LinearLayout signOutNormalUser = inflate.findViewById(R.id.sign_out);
                    LinearLayout text = inflate.findViewById(R.id.text);

                    editInfoNormalUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentCall = new Intent(Intent.ACTION_DIAL);
                            intentCall.setData(Uri.parse("tel:" + SplashActivity.etcetera.get(2).getMessage()));
                            startActivity(intentCall);
                        }
                    });
                    contactUsNormalUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Uri uri = Uri.parse(getString(R.string.insapp) + SplashActivity.etcetera.get(5).getMessage());
                            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                            likeIng.setPackage(getString(R.string.inspa));

                            try {
                                startActivity(likeIng);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(getString(R.string.insweb) + SplashActivity.etcetera.get(5).getMessage())));
                            }

                        }
                    });
                    aboutUsNormalUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                                sweetAlertDialog.dismissWithAnimation();
                            showAboutUs();
                        }
                    });
                    signOutNormalUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showExitDialog(sweetAlertDialog);
                        }
                    });
                    text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getContext(), PrivacyActivity.class));
                        }
                    });


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
        place5VideoView.setClipToOutline(true);
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
        });/**/

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);

        place1.setAdapter(adapterPlace1);
        place2.setAdapter(adapterPlace2);
        place3.setAdapter(adapterPlace3);
        place4.setAdapter(adapterPlace4);
        place6.setAdapter(adapterPlace6);
        place7.setAdapter(adapterPlace7);

        place1.startAutoScroll(7100);
        place2.startAutoScroll(200);
        place3.startAutoScroll(300);
        place4.startAutoScroll(400);
        place6.startAutoScroll(600);
        place7.startAutoScroll(700);


        place1.setInterval(3000);
        place2.setInterval(3000);
        place3.setInterval(3000);
        place4.setInterval(3000);
        place6.setInterval(3000);
        place7.setInterval(3000);

        app.validateConnection(getActivity(), null, new ConnectionErrorManager() {
            @Override
            public void doAction() {
                getData();
            }
        });

        return view;
    }


    private void showExitDialog(SweetAlertDialog sweetAlertDialog) {
        LottieAnimationView warrantyLt;
        MyTextView txt;
        RelativeLayout btnContactUs;
        MyTextView txtOk;
        RelativeLayout btnShowAllGoods;
        MyTextView cancelAction;

        SweetAlertDialog exitDialog = new SweetAlertDialog(getContext());
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null);
        exitDialog.setCustomView(view1);
        exitDialog.hideConfirmButton();


        warrantyLt = view1.findViewById(R.id.warranty_lt);
        txt = view1.findViewById(R.id.txt);
        btnContactUs = view1.findViewById(R.id.btn_contact_us);
        txtOk = view1.findViewById(R.id.txt_ok);
        btnShowAllGoods = view1.findViewById(R.id.btn_show_all_goods);
        cancelAction = view1.findViewById(R.id.cancel_action);

        warrantyLt.setVisibility(View.GONE);

        txt.setText("آیا مطمئن به خروج از حساب کاربری خود هستید؟");

        txtOk.setText("نه، دستم خورد.");
        cancelAction.setText("آره، میخوام برم.");

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sweetAlertDialog != null)
                    sweetAlertDialog.dismiss();
                exitDialog.dismissWithAnimation();
            }
        });

        btnShowAllGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefUtils.clear();
                Objects.requireNonNull(getActivity()).finish();
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });
        exitDialog.show();
    }

    SweetAlertDialog loadingData;
    SweetAlertDialog sweetAlertDialog;

    private void getData() {

        if (loadingData != null) {
            loadingData.dismissWithAnimation();
            loadingData = null;
        }
        if (sweetAlertDialogInvalidLength != null) sweetAlertDialogInvalidLength.dismiss();

        loadingData = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setContentText("در حال دریافت اطلاعات").setTitleText("لطفا شکیبا باشید.");
        loadingData.setCancelable(false);
        loadingData.show();
        Map<String, String> map = new HashMap<>();
        map.put("route", "getMainPageData");
        Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
            private ViewpagerData[] viewpagerData;

            private void viewPagerInfo(ViewpagerData viewpagerData, AutoScrollViewPager viewPager) {
                if (viewpagerData.getChangeState() == 1) {
                    viewPager.startAutoScroll();
                    viewPager.setInterval(viewpagerData.getIntervalTime());
                    viewPager.setDirection(AutoScrollViewPager.Direction.LEFT);

                    viewPager.setScrollDurationFactor(viewpagerData.getDelayTime());
                } else viewPager.stopAutoScroll();
            }

            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                if (loadingData != null) {

                    loadingData.dismiss();
                } else {

                }

                if (sweetAlertDialog != null) {
                    //      sweetAlertDialog.dismiss();

                } else {

                }


                try {
                    if (response.body() != null) {
                        JSONObject jsonObjectMain = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObjectMain.getJSONArray("mainPageData");
                        String jsonArrayViewPager = jsonObjectMain.getString("viewpagerData");
                        viewpagerData = new Gson().fromJson(jsonArrayViewPager.toString(), ViewpagerData[].class);


                        int len = jsonArray.length();


                        for (int i = 0; i < len; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getInt("field") == 8) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    place5VideoView.setVisibility(View.VISIBLE);
                                    place5VideoView.setVideoURI(Uri.parse(jsonObject.getString("view_url")));
                                } else {
                                    place5VideoView.setVisibility(View.GONE);
                                }
                            }


                            if (jsonObject.getInt("place") == 1) {

                                if (jsonObject.getInt("visibility") == 1) {

                                    containerP1.setVisibility(View.VISIBLE);
                                    adapterPlace1.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace1.notifyDataSetChanged();

                                } else {
                                    containerP1.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 2) {
                                if (jsonObject.getInt("visibility") == 1) {

                                    containerP2.setVisibility(View.VISIBLE);
                                    adapterPlace2.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace2.notifyDataSetChanged();
                                } else {
                                    containerP2.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 3) {
                                if (jsonObject.getInt("visibility") == 1) {

                                    place3.setVisibility(View.VISIBLE);
                                    adapterPlace3.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace3.notifyDataSetChanged();
                                } else {
                                    place3.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 4) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    place4.setVisibility(View.VISIBLE);
                                    adapterPlace4.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace4.notifyDataSetChanged();
                                } else {
                                    place4.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 6) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    place6.setVisibility(View.VISIBLE);
                                    adapterPlace6.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace6.notifyDataSetChanged();
                                } else {
                                    place6.setVisibility(View.GONE);
                                }
                            } else if (jsonObject.getInt("place") == 7) {
                                if (jsonObject.getInt("visibility") == 1) {
                                    place7.setVisibility(View.VISIBLE);
                                    adapterPlace7.addFragment(new MainPageItemFragment(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                            jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                    adapterPlace7.notifyDataSetChanged();
                                } else {
                                    place7.setVisibility(View.GONE);
                                }
                            }
                        }


                        viewPagerInfo(viewpagerData[0], place1);

                        viewPagerInfo(viewpagerData[1], place2);
                        viewPagerInfo(viewpagerData[2], place3);
                        viewPagerInfo(viewpagerData[3], place4);
                        viewPagerInfo(viewpagerData[4], place6);
                        viewPagerInfo(viewpagerData[5], place7);

                        /**/
                        /*Intent intent = new Intent("dataCount");
                        intent.putExtra("ref", "mpf");
                        LocalBroadcastManager.getInstance(MainPageFragment.this.getContext()).sendBroadcast(intent);*/

                    } else {

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getContext() != null) {

                            showAlertDialog("خطا در برقراری ارتباط", "تلاش دوباره", false, new AlertAction() {
                                @Override
                                public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getData();
                                }
                            });


                        }
                    }
                }, 1000);


            }
        });
    }


    private void showAlertDialog(String titleMsg, String okMsg, boolean isCancellable, AlertAction alertAction) {
        if (getContext() == null) {
            Toast.makeText(Application.getContext(), "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show();
            return;
        }
        sweetAlertDialogInvalidLength = new SweetAlertDialog(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null);

        ((TextView) view.findViewById(R.id.txt)).setText(titleMsg);
        ((TextView) view.findViewById(R.id.cancel_action)).setText(titleMsg);

        TextView txtOk = view.findViewById(R.id.txt_ok);
        txtOk.setText(okMsg);

        TextView cancel_action = view.findViewById(R.id.cancel_action);
        cancel_action.setText("خروج از برنامه");


        view.findViewById(R.id.btn_contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAction.doOnClick(sweetAlertDialogInvalidLength);
            }
        });

        view.findViewById(R.id.btn_show_all_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).moveTaskToBack(true);
                getActivity().finish();
            }
        });

        sweetAlertDialogInvalidLength.setCancelable(isCancellable);
        sweetAlertDialogInvalidLength.setCustomView(view);
        sweetAlertDialogInvalidLength.hideConfirmButton();
        sweetAlertDialogInvalidLength.show();
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
                if (app.appInstalledOrNot("com.whatsapp")) {
                    PackageManager pm = getActivity().getPackageManager();
                    String url = getString(R.string.wha) + SplashActivity.etcetera.get(4).getMessage();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.hideConfirmButton();
                    View viewErrorConnection = LayoutInflater.from(getContext()).inflate(R.layout.view_error_connection, null);
                    TextView retry = viewErrorConnection.findViewById(R.id.retry);
                    TextView msg = viewErrorConnection.findViewById(R.id.msg);
                    LottieAnimationView lt = viewErrorConnection.findViewById(R.id.lt);

                    msg.setText("لطفا قبل از ارسال فیلم برنامه واتساپ را نصب کنید.");
                    retry.setText("خب");
                    lt.setVisibility(View.GONE);

                    SweetAlertDialog finalSweetAlertDialog = sweetAlertDialog;
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.setCustomView(viewErrorConnection);
                    sweetAlertDialog.show();

                }
                break;

            case R.id.contact_us:
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + SplashActivity.etcetera.get(2).getMessage()));
                startActivity(intentCall);

                break;
            case R.id.about_us:

                showAboutUs();


                break;
            case R.id.bi_khial:

                Uri uri = Uri.parse(getString(R.string.insabc) + SplashActivity.etcetera.get(5).getMessage());//http://instagram.com/_u/abc
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage(getString(R.string.insap));

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.inweb) + SplashActivity.etcetera.get(5).getMessage())));//http://instagram.com/abc
                }

                break;
            case R.id.sign_out:


                showExitDialog(null);
                break;
            case R.id.text:
                startActivity(new Intent(getContext(), PrivacyActivity.class));
                break;
        }
    }

    private void showAboutUs() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null, false);
        TextView textView = view.findViewById(R.id.txt);
        textView.setText("آنلاین مکانیک برای ارتباط سریع و آسان میان خدمات دهندگان صنف مشاغل خودرویی و شهروندان، طراحی گردیده است. با استفاده از این برنامه، شهروندان می توانند برای سرویس خودروی خود انتخاب هوشمندانه تری داشته باشند. همچنین سرویس دهندگان خودرویی می توانند فارغ از محلی که در آن فعالیت می کنند با دادن مشاوره و نشان دادن خدمات حرفه ای،  اقدام به جذب مشتری کنند.");
        textView.setTextSize(16f);
        RelativeLayout btnShowAllGoods = view.findViewById(R.id.btn_show_all_goods);
        RelativeLayout contactUs = view.findViewById(R.id.btn_contact_us);
        (view.findViewById(R.id.warranty_lt)).setVisibility(View.GONE);

        ((TextView) view.findViewById(R.id.txt_ok)).setText("موافقم");
        btnShowAllGoods.setVisibility(View.GONE);
        SweetAlertDialog sweetAlertDialogGoodNotExist = new SweetAlertDialog(getContext()).hideConfirmButton()
                .setCustomView(view);


        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialogGoodNotExist.dismissWithAnimation();
            }
        });
        sweetAlertDialogGoodNotExist.setCancelable(false);
        sweetAlertDialogGoodNotExist.show();
    }


    public <E extends ImageView> void loadImage(String imageUrl, E imageView, SpinKitView spinKitView, ImageView retryImage) {
        if (imageUrl.trim().length() == 0) {
            return;
        }

        imageView.setVisibility(View.GONE);
        spinKitView.setVisibility(View.VISIBLE);
        retryImage.setVisibility(View.GONE);

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onStart() {
                        super.onStart();

                        imageView.setVisibility(View.GONE);
                        spinKitView.setVisibility(View.VISIBLE);
                        retryImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        imageView.setImageBitmap(resource);

                        spinKitView.setVisibility(View.GONE);
                        retryImage.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }


                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        spinKitView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        retryImage.setVisibility(View.VISIBLE);

                    }

                });

    }

    @Override
    public void onStop() {
        super.onStop();
        place5VideoView.pause();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setVisibility(View.GONE);
            }
            appbar.setVisibility(View.GONE);
            place5VideoView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) place5VideoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
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
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = place5VideoViewHeight;
            params.rightMargin = place5VideoViewMR;
            params.leftMargin = place5VideoViewML;
            params.bottomMargin = place5VideoViewMB;
            params.topMargin = place5VideoViewMT;
            place5VideoView.setLayoutParams(params);
        }
    }/**/
}