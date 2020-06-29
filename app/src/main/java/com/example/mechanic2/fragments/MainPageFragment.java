package com.example.mechanic2.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
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


    public static MainPageFragment newInstance() {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().getTheme().applyStyle(R.style.AppThemeWithActionBar, true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private Toolbar toolbar;
    private ListView listView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ImageView hamburgerButton;
    String[] titles;
    int[] images;


    private NavigationView navView;


    private View init(View view) {


        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        adapterPlace1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace3 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace4 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace5 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace6 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapterPlace7 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());


        hamburgerButton = (ImageView) view.findViewById(R.id.hamburger_button);
        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });
        navView = view.findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        CircularImageView circleImageView = headerView.findViewById(R.id.nav_mechanic_image);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.t("aA");
            }
        });


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

                    Intent intent = new Intent("dataCount");
                    intent.putExtra("ref", "mpf");
                    LocalBroadcastManager.getInstance(MainPageFragment.this.getContext()).sendBroadcast(intent);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

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