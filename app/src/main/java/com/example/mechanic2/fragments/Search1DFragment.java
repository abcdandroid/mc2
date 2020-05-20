package com.example.mechanic2.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionInflater;

import com.example.mechanic2.R;

import java.util.Objects;

public class Search1DFragment extends Fragment implements View.OnClickListener {
    TextView submitQuestion;

    public static Search1DFragment newInstance() {

        Bundle args = new Bundle();

        Search1DFragment fragment = new Search1DFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startPostponedEnterTransition();
        postponeEnterTransition();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.custom_transition));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.view_single_search, container, false);
        init(inflate);
        return inflate;
    }

    private void init(View inflate) {
        submitQuestion=inflate.findViewById(R.id.submitQuestion);
        submitQuestion.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {}
}
