package com.example.mechanic2.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.AnswersActivity;
import com.example.mechanic2.activities.ShowGoodDetailActivity;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.models.Question;
import com.example.mechanic2.views.MyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainPageItemFragment extends Fragment implements View.OnClickListener {


    // TODO: Rename and change types of parameters
    private String titleArg;
    private String descArg;
    private String imageUrlArg;
    private String jsonParamsArg;
    private int field;

    private ImageView imageView;
    private VideoView videoView;
    private MyTextView title;
    private MyTextView desc;
    private RelativeLayout parent;

    public MainPageItemFragment() {
        // Required empty public constructor
    }


    public static MainPageItemFragment newInstance(int field, String title, String desc, String imageUrl, String JsonParams) {
        MainPageItemFragment fragment = new MainPageItemFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("desc", desc);
        args.putString("imageUrl", imageUrl);
        args.putString("JsonParams", JsonParams);
        args.putInt("field", field);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titleArg = getArguments().getString("title");
            descArg = getArguments().getString("desc");
            imageUrlArg = getArguments().getString("imageUrl");
            jsonParamsArg = getArguments().getString("JsonParams");
            field = getArguments().getInt("field");


        }


    }

    String detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_page_item, container, false);

        imageView = inflate.findViewById(R.id.image_view);
        videoView = inflate.findViewById(R.id.video_view);
        title = inflate.findViewById(R.id.title);
        desc = inflate.findViewById(R.id.desc);

        parent = inflate.findViewById(R.id.parent);

        if (field != 8) {
            Picasso.get().load(imageUrlArg).into(imageView);
            videoView.setVisibility(View.GONE);
        } else {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(imageUrlArg));
        }

        try {
            JSONObject jsonParams = new JSONObject(jsonParamsArg);
            detail = jsonParams.getString("detail");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        parent.setOnClickListener(this);

        title.setText(titleArg);

        desc.setText(descArg);


        return inflate;
    }

    @Override
    public void onClick(View v) {
        Intent intentMainPage = new Intent("mpd");
        intentMainPage.putExtra("field", field);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentMainPage);
        switch (field) {
            case 3:
                Intent intent = new Intent("mpd3");
                intent.putExtra("detail", detail);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                break;
            case 7:
                Intent intentGood = new Intent(getActivity(), ShowGoodDetailActivity.class);
                Goood goood = new Gson().fromJson(detail, Goood.class);
                intentGood.putExtra("good", goood);
                startActivity(intentGood);
                break;
            case 2:
                Intent intentQuestionList=new Intent("mpd2");
                intentQuestionList.putExtra("detail", detail);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentQuestionList);
                break;
            case 6:
                Intent intentQuestion = new Intent(getActivity(), AnswersActivity.class);
                Question question = new Gson().fromJson(detail, Question.class);
                intentQuestion.putExtra("question", question);
                startActivity(intentQuestion);
                break;


        }
    }
/*
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

// Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            videoView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height= (int) app.dpToPixels(240);
            videoView.setLayoutParams(params);
        }

    }*/
}