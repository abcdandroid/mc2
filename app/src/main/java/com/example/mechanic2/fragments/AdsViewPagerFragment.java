package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.models.Ads;

public class AdsViewPagerFragment extends Fragment implements View.OnClickListener {
    private ImageView adsImage;
    private TextView adsDesc;

    public static AdsViewPagerFragment newInstance(Ads ads) {
        Bundle args = new Bundle();
        args.putSerializable("ads",ads);
        AdsViewPagerFragment fragment = new AdsViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ads_viewpager, container, false);
        Ads ads= (Ads) getArguments().getSerializable("ads");
        assert ads != null;
        init(inflate,ads);
        return inflate;
    }

    private void init(View inflate,Ads ads) {
        adsImage = inflate.findViewById(R.id.adsImage);
        adsDesc = inflate.findViewById(R.id.adsDesc);
        Glide.with(getActivity()).load(ads.getAdsImageUrl()).into(adsImage);
        adsDesc.setText(ads.getAdsDesc());
        adsImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
