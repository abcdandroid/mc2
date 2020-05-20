package com.example.mechanic2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mechanic2.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowThumbnailFragment extends Fragment {

    public ShowThumbnailFragment() {
        // Required empty public constructor
    }

    public static ShowThumbnailFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        ShowThumbnailFragment fragment = new ShowThumbnailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_show_thumbnail, container, false);
        init(inflate);
        return inflate;
    }

    private void init(View inflate) {
        CircleImageView imThumbnail = inflate.findViewById(R.id.imThumbnail);
        String url = null;
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
        Glide.with(Objects.requireNonNull(getActivity())).load(url).into(imThumbnail);
    }
}
