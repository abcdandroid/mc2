package com.example.mechanic2.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.AnswersActivity;
import com.example.mechanic2.activities.MainActivity;
import com.example.mechanic2.activities.ShowGoodDetailActivity;
import com.example.mechanic2.activities.ShowMechanicDetailActivity;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.UpdateNavBar;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.models.Mechanic;
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
    private MyTextView title;
    private MyTextView desc;
    private LinearLayout parent;

    public MainPageItemFragment() {
        // Required empty public constructor
    }

    public MainPageItemFragment(int field, String title, String desc, String imageUrl, String JsonParams) {
        this.titleArg = title;
        this.imageUrlArg = imageUrl;
        this.jsonParamsArg = JsonParams;
        this.field = field;
    }


/*    public static MainPageItemFragment newInstance(int field, String title, String desc, String imageUrl, String JsonParams) {
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
            imageUrlArg = getArguments().getString("imageUrl");
            jsonParamsArg = getArguments().getString("JsonParams");
            field = getArguments().getInt("field");
            app.l(imageUrlArg+"jleng");
        }


    }*/

    String detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app.l(desc + "jleng" + "onCreateView");
        View inflate = inflater.inflate(R.layout.fragment_main_page_item, container, false);
        imageView = inflate.findViewById(R.id.image_view);
        title = inflate.findViewById(R.id.title);
        desc = inflate.findViewById(R.id.desc);
        parent = inflate.findViewById(R.id.parent);

        Picasso.get().load(imageUrlArg).into(imageView);

        if (jsonParamsArg != null)
            try {
                JSONObject jsonParams = new JSONObject(jsonParamsArg);
                detail = jsonParams.getString("detail");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        parent.setOnClickListener(this);
        if (title != null && titleArg != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title.setText(Html.fromHtml(titleArg, Html.FROM_HTML_MODE_COMPACT));
            } else {
                title.setText(Html.fromHtml(titleArg));
            }
        }
        //title.setText(titleArg);
        return inflate;
    }

    @Override
    public void onClick(View v) {

        if (getActivity() != null) {
            if (field == 3 || field == 2 || field == 1 || field == 4) {
                Intent intent = new Intent("mp");
                int selected;
                if (field == 3 || field == 4) selected = field;
                else selected = field - 1;
                intent.putExtra("selected", selected);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }

            switch (field) {
                case 9:

                    break;

                case 3:
                    app.loadFragment((AppCompatActivity) getActivity(), StoreFragment.newInstance(detail));
                    //MainActivity.updateNavBar.setSelectedItem(3);

                    break;
                case 7:
                    Intent intentGood = new Intent(getActivity(), ShowGoodDetailActivity.class);
                    Goood goood = new Gson().fromJson(detail, Goood.class);
                    intentGood.putExtra("good", goood);
                    startActivity(intentGood);
                    break;
                case 2:
                    app.loadFragment((AppCompatActivity) getActivity(), QuestionFragment.newInstance(detail));
                    //MainActivity.updateNavBar.setSelectedItem(1);
                    break;
                case 6:
                    Intent intentQuestion = new Intent(getActivity(), AnswersActivity.class);
                    Question question = new Gson().fromJson(detail, Question.class);
                    intentQuestion.putExtra("question", question);
                    startActivity(intentQuestion);
                    break;
                case 1:
                    app.loadFragment((AppCompatActivity) getActivity(), MechanicFragment.newInstance(detail));
                    //MainActivity.updateNavBar.setSelectedItem(0);
                    break;
                case 5:
                    Intent intentMechanic = new Intent(getActivity(), ShowMechanicDetailActivity.class);
                    Mechanic mechanic = new Gson().fromJson(detail, Mechanic.class);
                    intentMechanic.putExtra("mechanic", mechanic);
                    startActivity(intentMechanic);
                    break;
                case 4:
                    app.loadFragment((AppCompatActivity) getActivity(), new AdminFragment());
                    //MainActivity.updateNavBar.setSelectedItem(4);
                    break;
            }

        }
    }


    /**/
}