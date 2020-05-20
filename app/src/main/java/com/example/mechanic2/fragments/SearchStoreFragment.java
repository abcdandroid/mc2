package com.example.mechanic2.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.example.mechanic2.R;

public class SearchStoreFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Transition transition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.custom_transition);
            setSharedElementEnterTransition(transition);
        }
    }

    public static SearchStoreFragment newInstance() {

        Bundle args = new Bundle();

        SearchStoreFragment fragment = new SearchStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_store, container, false);
    }
}
