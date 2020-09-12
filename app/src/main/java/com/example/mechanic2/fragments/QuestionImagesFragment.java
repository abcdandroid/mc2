package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.squareup.picasso.Picasso;


public class QuestionImagesFragment extends Fragment {
    private ImageView image;
    private OnViewPagerClickListener onViewPagerClickListener;

    public QuestionImagesFragment() {

    }

    public static QuestionImagesFragment newInstance(String imageUri, OnViewPagerClickListener onViewPagerClickListener) {

        Bundle args = new Bundle();
        args.putString("imageUri", imageUri);
        args.putParcelable("onClick", onViewPagerClickListener);
        QuestionImagesFragment fragment = new QuestionImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public String getImageUrl() {

        if (getArguments() != null) {
            return getArguments().getString("imageUri") == null ? "" : getArguments().getString("imageUri");
        } else return "";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_question_images, container, false);

        image = inflate.findViewById(R.id.image);

        if (getArguments() != null && getArguments().getParcelable("onClick") != null) {
            onViewPagerClickListener = getArguments().getParcelable("onClick");
        }
        if (getArguments() != null) {

            Picasso.get().load(getString(R.string.drweb) + getArguments().getString("imageUri")).into(image);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onViewPagerClickListener != null)
                    onViewPagerClickListener.onViewPagerClick(v);

            }
        });
        return inflate;
    }
}
