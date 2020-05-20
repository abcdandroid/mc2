package com.example.mechanic2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mechanic2.R;
import com.example.mechanic2.activities.SearchStoreActivity;

import java.util.Objects;

public class TestFragment extends Fragment implements View.OnClickListener {
    TextView submitQuestion;
    Fragment nextFragment;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_test, container, false);
        init(inflate);
        return inflate;
    }

    private void init(View inflate) {
        submitQuestion = inflate.findViewById(R.id.submitQuestion);
        nextFragment=Search1DFragment.newInstance();
        submitQuestion.setOnClickListener(this);
       // loadFragment(new ButtonTestFragment(),R.id.c);
    }

    private void loadFragment(Fragment fragment,int containerId) {
        FragmentManager fm = Objects.requireNonNull(getActivity().getSupportFragmentManager());
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), SearchStoreActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(),
                        Pair.create(submitQuestion, submitQuestion.getTransitionName()));
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

//        getFragmentManager().beginTransaction()
//                .add(R.id.container, nextFragment)
//                .hide(nextFragment)
//                .commit();
//        getFragmentManager().beginTransaction()
//                .addSharedElement(submitQuestion, submitQuestion.getTransitionName())
//                .remove(this)
//                .show(nextFragment)
//                .commit();

//        FragmentTransaction ft = getFragmentManager().beginTransaction()
//                .replace(R.id.container, nextFragment)
//                .addToBackStack("transaction")
//                .addSharedElement(submitQuestion, submitQuestion.getTransitionName());
//        ft.commit();


}



}



