package com.example.mechanic2.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.app.app;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionImagesFragment extends Fragment {
    private ImageView image;

    public QuestionImagesFragment() {
        // Required empty public constructor
    }
    public static QuestionImagesFragment newInstance(String imageUri) {

        Bundle args = new Bundle();
        args.putString("imageUri",imageUri);
        QuestionImagesFragment fragment = new QuestionImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_question_images, container, false);

        image = inflate.findViewById(R.id.image);
        if (getArguments() != null) {
            app.l("http://drkamal3.com/Mechanic/"+getArguments().getString("imageUri"));
            Glide.with(Objects.requireNonNull(getActivity())).load("http://drkamal3.com/Mechanic/"+getArguments().getString("imageUri")).into(image);
        }
        return inflate;
    }
}
