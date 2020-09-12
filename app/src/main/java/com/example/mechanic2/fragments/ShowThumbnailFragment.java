package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.squareup.picasso.Picasso;


public class ShowThumbnailFragment extends Fragment {
    private String url;
    private OnViewPagerClickListener onViewPagerClickListener;


    public ShowThumbnailFragment() {

    }

    public static ShowThumbnailFragment newInstance(String url, OnViewPagerClickListener onViewPagerClickListener) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putParcelable("onClick", onViewPagerClickListener);
        ShowThumbnailFragment fragment = new ShowThumbnailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            url = null;
            if (getArguments() != null && getArguments().getString("url") != null) {
                url = getArguments().getString("url");

            }
            if (getArguments() != null && getArguments().getParcelable("onClick") != null) {
                onViewPagerClickListener = getArguments().getParcelable("onClick");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_show_thumbnail, container, false);
        init(inflate);
        return inflate;
    }


    private void init(View inflate) {
        ImageView imThumbnail = inflate.findViewById(R.id.imThumbnail);
        if (onViewPagerClickListener != null) {
            imThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewPagerClickListener.onViewPagerClick(v);
                }
            });
        }
        Picasso.get().load(url).into(imThumbnail);
    }
}
