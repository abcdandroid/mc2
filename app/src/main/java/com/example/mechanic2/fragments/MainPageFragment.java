package com.example.mechanic2.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.OnVideoSizeChangedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.google.gson.JsonArray;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainPageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private VideoView videoView;
    private int cachedHeight;
    private int currentOrientation;


    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageFragment.
     */
    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            app.l("A");
            // Landscape
        } else {
            app.l("d");
            // Portrait
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        return init(view);
    }


    private CardView containerP1;
    private ViewPager place1;
    private CardView containerP2;
    private ViewPager place2;
    private LinearLayout containerP34;
    private ViewPager place3;
    private ViewPager place4;
    private CardView containerP5;
    private ViewPager place5;
    private LinearLayout containerP67;
    private ViewPager place6;
    private ViewPager place7;
    ViewPagerAdapter adapterPlace1;
    ViewPagerAdapter adapterPlace2;
    ViewPagerAdapter adapterPlace3;
    ViewPagerAdapter adapterPlace4;
    ViewPagerAdapter adapterPlace5;
    ViewPagerAdapter adapterPlace6;
    ViewPagerAdapter adapterPlace7;

    private View init(View view) {

        adapterPlace1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace3 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace4 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace5 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace6 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace7 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());


        containerP1 = view.findViewById(R.id.container_p1);
        place1 = view.findViewById(R.id.place_1);
        containerP2 = view.findViewById(R.id.container_p2);
        place2 = view.findViewById(R.id.place_2);
        containerP34 = view.findViewById(R.id.container_p34);
        place3 = view.findViewById(R.id.place_3);
        place4 = view.findViewById(R.id.place_4);
        containerP5 = view.findViewById(R.id.container_p5);
        place5 = view.findViewById(R.id.place_5);
        containerP67 = view.findViewById(R.id.container_p67);
        place6 = view.findViewById(R.id.place_6);
        place7 = view.findViewById(R.id.place_7);

        place1.setAdapter(adapterPlace1);
        place2.setAdapter(adapterPlace2);
        place3.setAdapter(adapterPlace3);
        place4.setAdapter(adapterPlace4);
        place5.setAdapter(adapterPlace5);
        place6.setAdapter(adapterPlace6);
        place7.setAdapter(adapterPlace7);
        getData();

        // setupVideoView(view);
        return view;
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("route", "getMainPageData");


        Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    app.l(jsonArray.length() + "jleng");
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getInt("place") == 1) {
                            if (jsonObject.getInt("visibility") == 1) {
                                adapterPlace1.addFragment(MainPageItemFragment.newInstance(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                        jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                adapterPlace1.notifyDataSetChanged();
                            } else {
                                containerP1.setVisibility(View.GONE);
                            }
                        } else if (jsonObject.getInt("place") == 2) {
                            if (jsonObject.getInt("visibility") == 1) {
                                adapterPlace2.addFragment(MainPageItemFragment.newInstance(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                        jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                adapterPlace2.notifyDataSetChanged();
                            } else {
                                containerP2.setVisibility(View.GONE);
                            }
                        } else if (jsonObject.getInt("place") == 3) {
                            if (jsonObject.getInt("visibility") == 1) {
                                adapterPlace3.addFragment(MainPageItemFragment.newInstance(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                        jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                adapterPlace3.notifyDataSetChanged();
                            } else {
                                place3.setVisibility(View.GONE);
                            }
                        } else if (jsonObject.getInt("place") == 4) {
                            if (jsonObject.getInt("visibility") == 1) {
                                adapterPlace4.addFragment(MainPageItemFragment.newInstance(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                        jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                adapterPlace4.notifyDataSetChanged();
                            } else {
                                place4.setVisibility(View.GONE);
                            }
                        } else if (jsonObject.getInt("place") == 5) {
                            if (jsonObject.getInt("visibility") == 1) {
                                adapterPlace5.addFragment(MainPageItemFragment.newInstance(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                        jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                adapterPlace5.notifyDataSetChanged();
                            } else {
                                containerP5.setVisibility(View.GONE);
                            }
                        } else if (jsonObject.getInt("place") == 6) {
                            if (jsonObject.getInt("visibility") == 1) {
                                adapterPlace6.addFragment(MainPageItemFragment.newInstance(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                        jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                adapterPlace6.notifyDataSetChanged();
                            } else {
                                place6.setVisibility(View.GONE);
                            }
                        } else if (jsonObject.getInt("place") == 7) {
                            if (jsonObject.getInt("visibility") == 1) {
                                adapterPlace7.addFragment(MainPageItemFragment.newInstance(jsonObject.getInt("field"), jsonObject.getString("image_title"),
                                        jsonObject.getString("image_desc"), jsonObject.getString("view_url"), jsonObject.getString("params")));
                                adapterPlace7.notifyDataSetChanged();
                            } else {
                                place7.setVisibility(View.GONE);
                            }
                        }
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

  /*  @Override
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

*/
}