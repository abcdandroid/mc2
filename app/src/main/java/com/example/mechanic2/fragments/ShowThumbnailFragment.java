package com.example.mechanic2.fragments;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mechanic2.R;
import com.example.mechanic2.app.app;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowThumbnailFragment extends Fragment {
    String url;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = null;
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_show_thumbnail, container, false);
        init(inflate);
        return inflate;
    }

    int i;

    private void init(View inflate) {
        ImageView imThumbnail = inflate.findViewById(R.id.imThumbnail);
        Glide.with(Objects.requireNonNull(getActivity())).load(url).into(imThumbnail);
    }
}
