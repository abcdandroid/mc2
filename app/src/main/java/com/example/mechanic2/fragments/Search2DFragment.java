package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mechanic2.R;

public class Search2DFragment extends Fragment implements View.OnClickListener {
    public static Search2DFragment newInstance() {

        Bundle args = new Bundle();

        Search2DFragment fragment = new Search2DFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.view_hybrid_search, container, false);
        init(inflate);
        return inflate;
    }

    private void init(View inflate) {


    }

    @Override
    public void onClick(View v) {

    }
}
