package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.CardAdapter;
import com.example.mechanic2.app.Application;

public class CardFragment extends Fragment {

    private CardView mCardView;


    public static CardFragment newInstance(String title,int mediaId) {

        Bundle args = new Bundle();

        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_entrance_type, container, false);
        mCardView = view.findViewById(R.id.cardView);

        Animation animation;
        animation = AnimationUtils.loadAnimation(Application.getContext(),
                R.anim.zoom_in);

        (view.findViewById(R.id.txt)).startAnimation(animation);

        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * CardAdapter.MAX_ELEVATION_FACTOR);
        return view;
    }

    public CardView getCardView() {
        return mCardView;
    }
}
