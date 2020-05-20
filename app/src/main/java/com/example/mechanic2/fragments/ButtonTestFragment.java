package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;

public class ButtonTestFragment extends Fragment implements View.OnClickListener {
    Button nb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_button_test, container, false);
        init(inflate);
        return inflate;
    }

    private void init(View inflate) {
        nb=inflate.findViewById(R.id.nb);
        nb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
                 FragmentTransaction ft = getFragmentManager().beginTransaction()
                .replace(R.id.ccc, Search1DFragment.newInstance())
                .addToBackStack("transaction")
                .addSharedElement(nb, nb.getTransitionName());
        ft.commit();
    }
}
