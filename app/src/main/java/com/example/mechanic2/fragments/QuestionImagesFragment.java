package com.example.mechanic2.fragments;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class QuestionImagesFragment extends Fragment {
    private ImageView image;
    private OnViewPagerClickListener onViewPagerClickListener;
    private SimpleDraweeView draweeView;
    LottieAnimationView lt_loading;
    RelativeLayout btn_retry;
    public QuestionImagesFragment() {

    }

    public static QuestionImagesFragment newInstance(String imageUri, OnViewPagerClickListener onViewPagerClickListener) {

        Bundle args = new Bundle();
        args.putString("imageUri", imageUri);
        args.putParcelable("onClick", onViewPagerClickListener);
        QuestionImagesFragment fragment = new QuestionImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public String getImageUrl() {

        if (getArguments() != null) {
            return getArguments().getString("imageUri") == null ? "" : getArguments().getString("imageUri");
        } else return "";
    }

    private static final String TAG = "QuestionImagesFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_question_images, container, false);

        image = inflate.findViewById(R.id.image);
        lt_loading = inflate.findViewById(R.id.lt_loading);
        btn_retry = inflate.findViewById(R.id.btn_retry);


        if (getArguments() != null && getArguments().getParcelable("onClick") != null) {
            onViewPagerClickListener = getArguments().getParcelable("onClick");
        }
        if (getArguments() != null) {

            draweeView = inflate.findViewById(R.id.my_image_view);

            Uri imageUri = Uri.parse(getString(R.string.drweb) + getArguments().getString("imageUri"));
            draweeView.setImageURI(imageUri);
            final ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
            progressBarDrawable.setColor(getResources().getColor(R.color.purple));
            progressBarDrawable.setBackgroundColor(getResources().getColor(R.color.blue_grey_50));
            progressBarDrawable.setRadius(getResources().getDimensionPixelSize(R.dimen.spacing_medium));


            DraweeController controller = Fresco.newDraweeControllerBuilder().setTapToRetryEnabled(true).setUri(imageUri)
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onSubmit(String id, Object callerContext) {
                            super.onSubmit(id, callerContext);

                            lt_loading.setVisibility(View.VISIBLE);
                            btn_retry.setVisibility(View.GONE);
                            lt_loading.playAnimation();
                        }

                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);

                            lt_loading.setVisibility(View.GONE);
                            btn_retry.setVisibility(View.GONE);
                            draweeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (onViewPagerClickListener != null)
                                        onViewPagerClickListener.onViewPagerClick(v);

                                }
                            });
                        }



                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);

                            lt_loading.setVisibility(View.GONE);
                            btn_retry.setVisibility(View.VISIBLE);



                        }

                        @Override
                        public void onRelease(String id) {
                            super.onRelease(id);

                        }
                    })
                    .build();

            draweeView.setController(controller);
            draweeView.getHierarchy().setProgressBarImage(progressBarDrawable);

            //Picasso.get().load(getString(R.string.drweb) + getArguments().getString("imageUri")).into(image);
            //Glide.with(this).load(getString(R.string.drweb) + getArguments().getString("imageUri")).into(image);
        }


        return inflate;
    }
}
