package com.example.mechanic2.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mechanic2.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    private ImageView simpleDrawerView;
    public String imageUrl = "http://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png";
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        simpleDrawerView = findViewById(R.id.simple_drawer_view);

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    private Bitmap resource;
                    private Transition<? super Bitmap> transition;

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                         this.resource = resource;
                        this.transition = transition;
                        simpleDrawerView.setImageBitmap(resource);

                        File file = createImageFile("testtte");
                        FileOutputStream ostream = null;
                        try {
                            ostream = new FileOutputStream(file);
                            resource.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }


                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                    }


                });


    }


    private File createImageFile(String imageFileName) {

        long timeStamp = System.currentTimeMillis();
        File storageDir = getExternalFilesDir("image");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".png",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }
}