package com.example.mechanic2.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mechanic2.R;
import com.example.mechanic2.activities.FullThumbActivity;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.example.mechanic2.models.Mechanic;

import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class ShowMechanicImagesFragment extends Fragment implements OnViewPagerClickListener {


    private ViewPagerAdapter viewPagerAdapter;
    private Mechanic mechanic;
    private ArrayList<String> imageList;
    private ViewPager mechanicImages;

    public ShowMechanicImagesFragment() {
        // Required empty public constructor
    }

    public static ShowMechanicImagesFragment newInstance(Mechanic mechanic) {
        ShowMechanicImagesFragment fragment = new ShowMechanicImagesFragment();
        Bundle args = new Bundle();
        args.putSerializable("mechanic",mechanic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mechanic = (Mechanic) getArguments().getSerializable("mechanic");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_show_mechanic_images, container, false);

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        imageList = new ArrayList<>();
        if (mechanic.getStore_image_1().length() > 0) imageList.add(mechanic.getStore_image_1());
        if (mechanic.getStore_image_2().length() > 0) imageList.add(mechanic.getStore_image_2());
        if (mechanic.getStore_image_3().length() > 0) imageList.add(mechanic.getStore_image_3());
        for (String url : imageList) {
            viewPagerAdapter.addFragment(QuestionImagesFragment.newInstance(url, this));
        }

        mechanicImages = inflate.findViewById(R.id.mechanic_images);
        mechanicImages.setAdapter(viewPagerAdapter);
        return inflate;
    }

    @Override
    public void onViewPagerClick(View view) {
        Intent intent = new Intent(getActivity(), FullThumbActivity.class);


        String[] imageUrl = new String[viewPagerAdapter.getCount()];
        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            imageUrl[i] = ((QuestionImagesFragment) viewPagerAdapter.getItem(i)).getImageUrl();
        }

        intent.putExtra("linkList", imageUrl);
        intent.putExtra("currentItem", mechanicImages.getCurrentItem());
        intent.putExtra("from", "showMechanicDetailActivity");

        app.l("Eeeeeeeafb" + imageList.toArray().length);

        startActivity(intent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}