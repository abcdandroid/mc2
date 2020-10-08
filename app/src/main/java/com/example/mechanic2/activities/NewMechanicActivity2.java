package com.example.mechanic2.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.downloader.request.DownloadRequest;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.MechanicMoviesRecyclerAdapter;
import com.example.mechanic2.adapters.RegionAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AddJobClickListener;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.models.AdminMedia;
import com.example.mechanic2.models.Job;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.models.Movies;
import com.example.mechanic2.models.Region;
import com.example.mechanic2.views.JobRow;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMechanicActivity2 extends Activity implements View.OnClickListener, OnClickListener {


    private Uri resultUri;

    ArrayList<ImageView> images = new ArrayList<>();
    List<MultipartBody.Part> files = new ArrayList<>();
    int current_image;
    Uri uri;
    Boolean[] fill_images = new Boolean[4];
    String[] address_images = new String[4];
    MultipartBody.Part body0;
    MultipartBody.Part body1;
    MultipartBody.Part body2;
    MultipartBody.Part body3;
    private LinearLayout jobContainer;


    private RelativeLayout parent;
    private LinearLayout picHint;
    private LinearLayout lytForm;
    private LinearLayout jobHint;
    private JobRow jobRow1;
    private LinearLayout mapHint;
    private TextView whatsUpIntent;
    private TextView submitNewMechanic;
    private ArrayList<Integer> listIds = new ArrayList<>();
    private ArrayList<String> listNames = new ArrayList<>();
    private AutoCompleteTextView regionNameAuto;
    private AppCompatEditText regionDesc;
    private View mapThumbnailFake;
    private MapView mMapView;
    private int selectedRegionId;
    private String selectedRegionName;
    private TextView regionId;
    private TextView lttLngValue;

    private AppCompatEditText mechanicName;
    private AppCompatEditText mechanicStoreName;
    private AppCompatEditText mechanicPhone;
    private AppCompatEditText mechanicAbout;

    MyLocationNewOverlay mLocationOverlay;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
    private IMapController mapController;
    private CardView movieLayer;
    private RecyclerView mechanicMovies;
    private Mechanic mechanic;
    private SweetAlertDialog sweetAlertDialog;


    private SpinKitView loadingImage3;
    private SpinKitView loadingImage2;
    private SpinKitView loadingImage1;
    private SpinKitView loadingImageProfile;

    private ImageView retryImage3;
    private ImageView retryImage2;
    private ImageView retryImage1;
    private ImageView retryimageProfile;
    private SweetAlertDialog sweetAlertDialogFailureLoading;
    private SweetAlertDialog sweetAlertDialogGoodNotExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mechanic_2);

        movieLayer = findViewById(R.id.movie_layer);
        parent = findViewById(R.id.parent);
        picHint = findViewById(R.id.pic_hint);
        lytForm = findViewById(R.id.lyt_form);
        jobHint = findViewById(R.id.job_hint);
        jobContainer = findViewById(R.id.job_container);
        jobRow1 = findViewById(R.id.job_row_1);
        regionNameAuto = findViewById(R.id.region_name_auto);
        regionId = findViewById(R.id.region_id);
        mapHint = findViewById(R.id.map_hint);
        whatsUpIntent = findViewById(R.id.whats_up_intent);
        submitNewMechanic = findViewById(R.id.submit_new_mechanic);
        regionDesc = findViewById(R.id.region_desc);
        mMapView = findViewById(R.id.map_thumbnail);
        mapThumbnailFake = findViewById(R.id.map_thumbnail_fake);
        lttLngValue = findViewById(R.id.ltt_lng_value);
        mapThumbnailFake.setOnClickListener(this);

        mechanicName = findViewById(R.id.mechanic_name);
        mechanicStoreName = findViewById(R.id.mechanic_store_name);
        mechanicPhone = findViewById(R.id.mechanic_phone);
        mechanicAbout = findViewById(R.id.mechanic_about);


        loadingImage3 = findViewById(R.id.loading_image3);
        loadingImage2 = findViewById(R.id.loading_image2);
        loadingImage1 = findViewById(R.id.loading_image1);
        loadingImageProfile = findViewById(R.id.loading_imageProfile);
        loadingImage3.setVisibility(View.GONE);
        loadingImage2.setVisibility(View.GONE);
        loadingImage1.setVisibility(View.GONE);
        loadingImageProfile.setVisibility(View.GONE);

        retryImage3 = findViewById(R.id.retryImage3);
        retryImage2 = findViewById(R.id.retryImage2);
        retryImage1 = findViewById(R.id.retryImage1);
        retryimageProfile = findViewById(R.id.retryImageProfile);

        retryImage3.setVisibility(View.GONE);
        retryImage2.setVisibility(View.GONE);
        retryImage1.setVisibility(View.GONE);
        retryimageProfile.setVisibility(View.GONE);


        TextInputLayout mechanic_about_holder = findViewById(R.id.mechanic_about_holder);
        mechanic_about_holder.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.ttf)));

        ImageView storeImage1 = findViewById(R.id.image1);
        storeImage1.setClipToOutline(true);
        images.add(storeImage1);
        ImageView storeImage2 = findViewById(R.id.image2);
        storeImage2.setClipToOutline(true);
        images.add(storeImage2);
        ImageView storeImage3 = findViewById(R.id.image3);
        images.add(storeImage3);
        storeImage3.setClipToOutline(true);
        CircleImageView circularProfileImage = findViewById(R.id.imageProfile);
        images.add(circularProfileImage);
        jobContainer = findViewById(R.id.job_container);

        mMapView.setMultiTouchControls(false);

        mapController = mMapView.getController();
        mapController.setZoom(14f);

        Arrays.fill(fill_images, false);

        Arrays.fill(address_images, "");

        if ((Mechanic) getIntent().getSerializableExtra("mechanicInfo") == null) {

            if (SharedPrefUtils.getStringData("ltt").equals("-1") && SharedPrefUtils.getStringData("lng").equals("-1")) {
                SharedPrefUtils.saveData("ltt", String.valueOf(36.3201));
                SharedPrefUtils.saveData("lng", String.valueOf(59.5896));

            } else {
                mechanic = (Mechanic) getIntent().getSerializableExtra("mechanicInfo");
                if (mechanic != null) {
                    SharedPrefUtils.saveData("ltt", mechanic.getX_location());
                    SharedPrefUtils.saveData("lng", mechanic.getY_location());
                }

            }
        }

        if (getIntent().getExtras() == null) {

            movieLayer.setVisibility(View.GONE);
        } else {
            int mechanicId;
            if (getIntent().getSerializableExtra("mechanicInfo") != null) {
                mechanic = (Mechanic) getIntent().getSerializableExtra("mechanicInfo");
                mechanicId = mechanic.getId();

                mechanicName.setText(mechanic.getName());
                mechanicStoreName.setText(mechanic.getStore_name());
                mechanicPhone.setText(mechanic.getPhone_number());

                if (mechanic.getJob().size() > 0) jobContainer.removeView(jobRow1);
                for (int i = 0; i < mechanic.getJob().size(); i++) {

                    JobRow jobRow = new JobRow(this);
                    jobRow.setText(mechanic.getJob().get(i).getName());
                    jobRow.setIdJobRow(String.valueOf(mechanic.getJob().get(i).getId()));
                    jobRow.setRemoveFocus(true);
                    jobContainer.addView(jobRow);
                    jobIconManager();
                }

                regionNameAuto.setText(mechanic.getRegion().getName());
                regionId.setText(String.valueOf(mechanic.getRegion().getId()));

                regionDesc.setText(mechanic.getAddress());


                SharedPrefUtils.saveData("ltt", mechanic.getX_location());
                SharedPrefUtils.saveData("lng", mechanic.getY_location());


                mechanicAbout.setText(mechanic.getAbout());

                if (mechanic.getMechanic_image().length() != 0 && SharedPrefUtils.getStringData("imageNo" + 3).equals("-1")) {
                    /*String mechanic_image = mechanic.getMechanic_image();
                    String mechanicImageName = mechanic_image.substring(mechanic_image.lastIndexOf("/") + 1, mechanic_image.length() - 1);

                    Target target = new Target() {
                        @Override
                        public void onPrepareLoad(Drawable arg0) {
                        }

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                            try {
                                circularProfileImage.setImageBitmap(null);
                                circularProfileImage.setImageBitmap(bitmap);
                                File file = createImageFile(mechanicImageName);
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                ostream.close();
                                SharedPrefUtils.saveData("imageNo" + 3, file.getPath());
                                fill_images[3] = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }
                    };

                    Picasso.get()
                            .load(getString(R.string.drweb) + mechanic.getMechanic_image())
                            .into(target);*/
                    loadImage(mechanic.getMechanic_image(), circularProfileImage, loadingImageProfile, retryimageProfile);
                }
                if (mechanic.getStore_image_1().length() != 0 && SharedPrefUtils.getStringData("imageNo" + 0).equals("-1")) {
/*                    String store_image_1 = mechanic.getStore_image_1();
                    String Store1ImageName = store_image_1.substring(store_image_1.lastIndexOf("/") + 1, store_image_1.length() - 1);

                    Target target = new Target() {
                        @Override
                        public void onPrepareLoad(Drawable arg0) {
                            return;
                        }

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                            try {
                                storeImage1.setImageBitmap(null);
                                storeImage1.setImageBitmap(bitmap);
                                File file = createImageFile(Store1ImageName);
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                ostream.close();
                                SharedPrefUtils.saveData("imageNo" + 0, file.getPath());
                                fill_images[0] = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }
                    };

                    Picasso.get()
                            .load(getString(R.string.drweb) + mechanic.getStore_image_1())
                            .into(target);*/
                    loadImage(mechanic.getStore_image_1(), storeImage1, loadingImage1, retryImage1);
                }
                if (mechanic.getStore_image_2().length() != 0 && SharedPrefUtils.getStringData("imageNo" + 1).equals("-1")) {
                  /*  String store_image_2 = mechanic.getStore_image_2();
                    String Store2ImageName = store_image_2.substring(store_image_2.lastIndexOf("/") + 1, store_image_2.length() - 1);

                    Target target = new Target() {
                        @Override
                        public void onPrepareLoad(Drawable arg0) {
                            return;
                        }

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                            try {
                                storeImage2.setImageBitmap(null);
                                storeImage2.setImageBitmap(bitmap);
                                File file = createImageFile(Store2ImageName);
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                ostream.close();
                                SharedPrefUtils.saveData("imageNo" + 1, file.getPath());
                                fill_images[1] = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }
                    };

                    Picasso.get()
                            .load(getString(R.string.drweb) + mechanic.getStore_image_2())
                            .into(target);*/

                    loadImage(mechanic.getStore_image_2(), storeImage2, loadingImage2, retryImage2);
                }
                if (mechanic.getStore_image_3().length() != 0 && SharedPrefUtils.getStringData("imageNo" + 2).equals("-1")) {
                    /*String store_image_3 = mechanic.getStore_image_3();
                    String Store3ImageName = store_image_3.substring(store_image_3.lastIndexOf("/") + 1, store_image_3.length() - 1);

                    Target target = new Target() {
                        @Override
                        public void onPrepareLoad(Drawable arg0) {
                            return;
                        }

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                            try {
                                storeImage3.setImageBitmap(null);
                                storeImage3.setImageBitmap(bitmap);
                                File file = createImageFile(Store3ImageName);
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                ostream.close();

                                SharedPrefUtils.saveData("imageNo" + 2, file.getPath());
                                fill_images[2] = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }
                    };

                    Picasso.get()
                            .load(getString(R.string.drweb) + mechanic.getStore_image_3())
                            .into(target);*/
                    loadImage(mechanic.getStore_image_3(), storeImage3, loadingImage3, retryImage3);
                }


            } else {
                mechanic = null;
                mechanicId = Integer.parseInt(SharedPrefUtils.getStringData("mechanicId"));

                if (!SharedPrefUtils.getStringData("mechanicName").equals("-1")) {
                    mechanicName.setText(SharedPrefUtils.getStringData("mechanicName"));
                }

                if (!SharedPrefUtils.getStringData("mechanicStoreName").equals("-1")) {
                    mechanicStoreName.setText(SharedPrefUtils.getStringData("mechanicStoreName"));
                }

                if (!SharedPrefUtils.getStringData("mechanicPhone").equals("-1")) {
                    mechanicPhone.setText(SharedPrefUtils.getStringData("mechanicPhone"));
                }


                if (!SharedPrefUtils.getStringData("jobNames").equals("-1") && !SharedPrefUtils.getStringData("jobIds").equals("-1")) {
                    List<String> jobNames = Arrays.asList(SharedPrefUtils.getStringData("jobNames").split(","));
                    List<String> jobIds = Arrays.asList(SharedPrefUtils.getStringData("jobIds").split(","));
                    if (jobNames.size() > 0) jobContainer.removeView(jobRow1);
                    for (int i = 0; i < jobNames.size(); i++) {
                        JobRow jobRow = new JobRow(this);
                        jobRow.setText(jobNames.get(i));
                        jobRow.setIdJobRow(jobIds.get(i));
                        jobRow.setRemoveFocus(true);
                        jobContainer.addView(jobRow);
                        jobIconManager();
                    }
                }
                if (!SharedPrefUtils.getStringData("regionName").equals("-1") && !SharedPrefUtils.getStringData("regionId").equals("-1")) {
                    regionNameAuto.setText(SharedPrefUtils.getStringData("regionName"));
                    regionId.setText(SharedPrefUtils.getStringData("regionId"));
                }

                if (!SharedPrefUtils.getStringData("regionDesc").equals("-1")) {
                    regionDesc.setText(SharedPrefUtils.getStringData("regionDesc"));
                }

                if (!SharedPrefUtils.getStringData("mechanicAbout").equals("-1")) {
                    mechanicAbout.setText(SharedPrefUtils.getStringData("mechanicAbout"));
                }
            }

            movieLayer.setVisibility(View.VISIBLE);
            mechanicMovies = findViewById(R.id.mechanic_movies);
            mechanicMovies.setLayoutManager(new LinearLayoutManager(this));
            mechanicMovies.setLayoutAnimation((new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left))));

            Map<String, String> map = new HashMap<>();
            map.put("route", "getMechanicMovies");
            map.put("id", String.valueOf(mechanicId));

            Application.getApi().getMechanicMovies(map).enqueue(new Callback<List<Movies>>() {
                @Override
                public void onResponse(Call<List<Movies>> call, Response<List<Movies>> response) {
                    if (response.body() != null) {

                    }
                    MechanicMoviesRecyclerAdapter mechanicMoviesRecyclerAdapter = new MechanicMoviesRecyclerAdapter(NewMechanicActivity2.this, response.body(), NewMechanicActivity2.this);
                    mechanicMovies.setAdapter(mechanicMoviesRecyclerAdapter);
                    mechanicMovies.setLayoutAnimation((new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left))));
                }

                @Override
                public void onFailure(Call<List<Movies>> call, Throwable t) {


                }
            });
        }


        for (int i = 0; i < 4; i++) {
            addToFileList(i);
        }
        GeoPoint startPoint = new GeoPoint(Double.parseDouble(SharedPrefUtils.getStringData("ltt")), Double.parseDouble(SharedPrefUtils.getStringData("lng")));
        mapController.setCenter(startPoint);
        if (Double.parseDouble(SharedPrefUtils.getStringData("ltt")) != 0 && Double.parseDouble(SharedPrefUtils.getStringData("lng")) != 0) {
            ArrayList<OverlayItem> overlayArray = new ArrayList<>();
            OverlayItem mapItem = new OverlayItem("", "", startPoint);
            final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
            mapItem.setMarker(marker);
            overlayArray.add(mapItem);
            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
            mMapView.getOverlays().add(anotherItemizedIconOverlay);
            mMapView.getOverlays().remove(mLocationOverlay);
            mMapView.invalidate();
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(jobRowReceiver, new IntentFilter("fromJobRow"));


        JobRow.addJobClickListener = new AddJobClickListener() {
            @Override
            public void onClick(View view) {
                int jobLimiter = 5;
                int jobId;
                String idJobRow = ((JobRow) view.getParent().getParent()).getIdJobRow();
                if (idJobRow.length() > 0)
                    jobId = Integer.parseInt(idJobRow);
                else jobId = -2;
                String jobName = new String(((JobRow) view.getParent().getParent()).getText());

                if (view.getTag() != null && view.getTag().equals("remove")) {
                    jobContainer.removeView(((LinearLayout) view.getParent().getParent()));
                    jobIconManager();
                    if (jobId != -2) {
                        listIds.remove(Integer.valueOf(jobId));
                        listNames.remove(jobName);
                    }
                    return;
                }
                if (view.getTag() != null && view.getTag().equals("removeByLimit")) {
                    jobContainer.removeView(((LinearLayout) view.getParent().getParent()));
                    jobIconManager();
                    listIds.remove(Integer.valueOf(jobId));
                    listNames.remove(jobName);
                    return;
                }


                JobRow jobRow = new JobRow(getApplicationContext());
                jobContainer.addView(jobRow);
                jobIconManager();


                if (jobContainer.getChildCount() >= jobLimiter) {
                    LinearLayout linearLayout = ((LinearLayout) jobContainer.getChildAt(jobLimiter - 1));
                    ImageView jobIcon = linearLayout.findViewById(R.id.job_add_remove_icon);
                    jobIcon.setTag("removeByLimit");
                    jobIcon.setImageDrawable(getDrawable(R.drawable.remove_job));
                }
            }
        };


        images.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                current_image = 0;


                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });


        images.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 1;

                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });
        images.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 2;

                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });

        images.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 3;

                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });

        submitNewMechanic.setOnClickListener(this);
        whatsUpIntent.setOnClickListener(this);
        RegionAutoCompleteAdapter regionAutoCompleteAdapter = new RegionAutoCompleteAdapter(this, R.layout.item, false);
        regionNameAuto.setAdapter(regionAutoCompleteAdapter);
        regionNameAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRegionId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
                selectedRegionName = ((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.title)).getText().toString();
                regionId.setText(String.valueOf(selectedRegionId));
            }
        });
        regionNameAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                regionId.setText(String.valueOf(""));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void addToFileList(int i) {
        String key = "imageNo" + i;
        if (!SharedPrefUtils.getStringData(key).equals("-1") && SharedPrefUtils.getStringData("imageNo").length() > 0) {
            fill_images[i] = true;
            Uri parsedUri = Uri.parse(SharedPrefUtils.getStringData("imageNo" + i));
            images.get(i).setImageURI(null);
            images.get(i).setImageURI(parsedUri);

            if (i == 0) {
                body0 = app.prepareImagePart("fileNo" + i, parsedUri);
                files.add(body0);

            }
            if (i == 1) {

                body1 = app.prepareImagePart("fileNo" + i, parsedUri);
                files.add(body1);

            }
            if (i == 2) {

                body2 = app.prepareImagePart("fileNo" + i, parsedUri);
                files.add(body2);

            }
            if (i == 3) {
                body3 = app.prepareImagePart("fileNo" + i, parsedUri);
                files.add(body3);

            }

        }
    }

    private void jobIconManager() {
        for (int i = 0; i < jobContainer.getChildCount(); i++) {
            LinearLayout linearLayout = ((LinearLayout) jobContainer.getChildAt(i));
            ImageView jobIcon = (ImageView) linearLayout.findViewById(R.id.job_add_remove_icon);

            if (i == jobContainer.getChildCount() - 1) {
                jobIcon.setTag("add");
                jobIcon.setImageDrawable(getDrawable(R.drawable.add_job));
            } else {
                jobIcon.setTag("remove");
                jobIcon.setImageDrawable(getDrawable(R.drawable.remove_job));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.getOverlays().clear();
        GeoPoint startPoint = new GeoPoint(Double.parseDouble(SharedPrefUtils.getStringData("ltt")), Double.parseDouble(SharedPrefUtils.getStringData("lng")));
        mapController.setCenter(startPoint);
        if (Double.parseDouble(SharedPrefUtils.getStringData("ltt")) != 0 && Double.parseDouble(SharedPrefUtils.getStringData("lng")) != 0) {
            ArrayList<OverlayItem> overlayArray = new ArrayList<>();
            OverlayItem mapItem = new OverlayItem("", "", startPoint);
            final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
            mapItem.setMarker(marker);
            overlayArray.add(mapItem);
            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
            mMapView.getOverlays().add(anotherItemizedIconOverlay);
            mMapView.getOverlays().remove(mLocationOverlay);
            mMapView.invalidate();
        }

    }

    public void show_dialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("انتخاب تصویر")
                .setCancelText("از گالری")
                .setConfirmText("از دوربین")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(Intent.createChooser(gallery_intent, "لطفا یک عکس را انتخاب کنید"), 2);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(sDialog -> {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File photoFile = createImageFile();
                    uri = Uri.fromFile(photoFile);

                    if (photoFile != null) {


                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, 1);
                    }

                    sDialog.dismissWithAnimation();
                })
                .show();


    }


    public void show_delete_dialog() {


        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("حذف تصویر")
                .setContentText("آیا مطمئن به حذف عکس هستید ؟")
                .setCancelText("بله")
                .setConfirmText("خیر")
                .showCancelButton(true)
                .setCancelClickListener(sDialog -> {

                    images.get(current_image).setImageResource(current_image == 3 ? R.drawable.mechanic_face2 : R.drawable.mechanic_store_preview);
                    fill_images[current_image] = false;
                    SharedPrefUtils.removeDataByArgument("imageNo" + current_image);


                    switch (current_image) {
                        case 0:
                            files.remove(body0);

                            if (mechanic != null) {
                                mechanic.setStore_image_1("");
                            }
                            break;
                        case 1:
                            files.remove(body1);

                            if (mechanic != null) {
                                mechanic.setStore_image_2("");
                            }
                            break;
                        case 2:
                            files.remove(body2);

                            if (mechanic != null) {
                                mechanic.setStore_image_3("");
                            }
                            break;
                        case 3:
                            files.remove(body3);

                            if (mechanic != null) {
                                mechanic.setMechanic_image("");
                            }
                            break;
                    }
                    sDialog.dismissWithAnimation();

                })
                .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();


    }

    private File createImageFile() {

        long timeStamp = System.currentTimeMillis();
        String imageFileName = "NAME_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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

    private File createImageFile(String imageFileName) {

        long timeStamp = System.currentTimeMillis();
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK) {

            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).setOutputCompressQuality(100).start(this);

        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            uri = data.getData();

            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).setOutputCompressQuality(100).start(this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            resultUri = result.getUri();

            SharedPrefUtils.saveData("imageNo" + current_image, resultUri.getPath());

            images.get(current_image).setImageURI(resultUri);

            fill_images[current_image] = true;
            if (current_image == 0) {
                body0 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body0);

            }
            if (current_image == 1) {

                body1 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body1);

            }
            if (current_image == 2) {

                body2 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body2);

            }
            if (current_image == 3) {

                body3 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body3);

            }


        }

    }

    SweetAlertDialog sweetAlertDialogErrorConnection;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_new_mechanic:

                app.validateConnection(this, sweetAlertDialogErrorConnection, new ConnectionErrorManager() {
                    @Override
                    public void doAction() {
                        submitNewMechanic();
                    }
                });


                break;
            case R.id.whats_up_intent:

                if (app.appInstalledOrNot("com.whatsapp")) {
                    PackageManager pm = getPackageManager();
                    String url = getString(R.string.wha_send_mov) + SplashActivity.etcetera.get(4).getMessage();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.hideConfirmButton();
                    View viewErrorConnection = LayoutInflater.from(this).inflate(R.layout.view_error_connection, null);
                    TextView retry = viewErrorConnection.findViewById(R.id.retry);
                    TextView msg = viewErrorConnection.findViewById(R.id.msg);
                    LottieAnimationView lt = viewErrorConnection.findViewById(R.id.lt);

                    msg.setText("لطفا قبل از ارسال فیلم برنامه واتساپ را نصب کنید.");
                    retry.setText("خب");
                    lt.setVisibility(View.GONE);

                    SweetAlertDialog finalSweetAlertDialog = sweetAlertDialog;
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.setCustomView(viewErrorConnection);
                    sweetAlertDialog.show();

                }
                break;
            case R.id.map_thumbnail_fake:
                startActivity(new Intent(NewMechanicActivity2.this, MechanicLocationActivity.class));
                break;
        }
    }

    private void submitNewMechanic() {
        listIds = new ArrayList<>();
        listNames = new ArrayList<>();
        if (mechanic != null) {
            mechanic.getJob().clear();
        }
        for (int i = 0; i < jobContainer.getChildCount(); i++) {
            JobRow jobRow = (JobRow) jobContainer.getChildAt(i);
            String idJobRow = jobRow.getIdJobRow();
            if (idJobRow.length() > 0) {
                listIds.add(Integer.parseInt(idJobRow));
                listNames.add(jobRow.getText());
                if (mechanic != null) {
                    mechanic.getJob().add(new Job(jobRow.getText(), Integer.parseInt(idJobRow)));
                }
            }

        }


        String mechanicName = this.mechanicName.getText().toString();
        if (mechanicName.length() >= 3)
            if (mechanic == null)
                SharedPrefUtils.saveData("mechanicName", mechanicName);
            else mechanic.setName(mechanicName);
        else {

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitle("لطفا نام  خود را به صورت کامل وارد نمایید.");
            sweetAlertDialog.setConfirmText("خب");
            sweetAlertDialog.show();
            return;
        }


        String mechanicStoreName = this.mechanicStoreName.getText().toString();
        if (mechanicStoreName.length() > 3)
            if (mechanic == null)
                SharedPrefUtils.saveData("mechanicStoreName", mechanicStoreName);
            else mechanic.setStore_name(mechanicStoreName);
        else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitle("لطفا نام مغازه خود را به صورت کامل وارد نمایید.");
            sweetAlertDialog.setConfirmText("خب");
            sweetAlertDialog.show();
            return;
        }


        String mechanicPhone = this.mechanicPhone.getText().toString();
        if (mechanicPhone.matches("^09\\d{9}$"))
            if (mechanic == null)
                SharedPrefUtils.saveData("mechanicPhone", mechanicPhone);
            else mechanic.setPhone_number(mechanicPhone);
        else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitle("لطفا شماره همراه خود را به صورت کامل وارد نمایید.");
            sweetAlertDialog.setConfirmText("خب");
            sweetAlertDialog.show();
            return;
        }

        String phoneNumber = SharedPrefUtils.getStringData("phoneNumber");

        String jobNames = jobSeparator(listNames);
        String jobIdsText = jobSeparator(listIds);

        if (mechanic == null)
            if (jobIdsText.length() != 0 && jobNames.length() != 0) {
                SharedPrefUtils.saveData("jobNames", jobNames);
                SharedPrefUtils.saveData("jobIds", jobIdsText);
            } else {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitle("لطفا برای خود حداقل یکی از تخصص های پیشنهادی را انتخاب کنید.");
                sweetAlertDialog.setConfirmText("خب");
                sweetAlertDialog.show();
                return;
            }


        String regionNameText = regionNameAuto.getText().toString();
        String regionIdText = regionId.getText().toString();

        if (regionNameText.length() != 0 && regionIdText.length() != 0) {
            if (mechanic == null) {
                SharedPrefUtils.saveData("regionName", regionNameText);
                SharedPrefUtils.saveData("regionId", regionIdText);
            } else
                mechanic.setRegion(new Region(regionNameText, Integer.parseInt(regionIdText)));
        } else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitle("لطفا یکی از منطقه های پیشنهادی را به عنوان منطقه فعالیت خود انتخاب کنید.");
            sweetAlertDialog.setConfirmText("خب");
            sweetAlertDialog.show();
            return;
        }


        String regionDesc = Objects.requireNonNull(this.regionDesc.getText()).toString();
        if (regionDesc.length() >= 10)
            if (mechanic == null)
                SharedPrefUtils.saveData("regionDesc", regionDesc);
            else mechanic.setAddress(regionDesc);
        else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitle("لطفا آدرس خود را دقیق وارد کنید.");
            sweetAlertDialog.setConfirmText("خب");
            sweetAlertDialog.show();
            return;
        }


        if (mechanic != null) {
            mechanic.setX_location(SharedPrefUtils.getStringData("ltt"));
            mechanic.setY_location(SharedPrefUtils.getStringData("lng"));
        }


        String mechanicAbout = Objects.requireNonNull(this.mechanicAbout.getText()).toString();
        if (mechanic == null)
            SharedPrefUtils.saveData("mechanicAbout", mechanicAbout);
        else mechanic.setAbout(mechanicAbout);


        if (mechanic != null)
            SharedPrefUtils.saveData("mechanicInfo", new Gson().toJson(mechanic));

        String lttFinal = SharedPrefUtils.getStringData("ltt");
        String lngFinal = SharedPrefUtils.getStringData("lng");


        sweetAlertDialog = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("لطفا شکیبا باشید");
        sweetAlertDialog.setContentText("در حال بارگذاری اطلاعات");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();


        Call<String> stringCall;
        Map<String, String> map = new HashMap<>();
        map.put("route", "addNewMechanic");
        map.put("job_ids", jobIdsText);
        map.put("region_id", regionIdText);
        map.put("address", regionDesc);
        map.put("name", mechanicName);
        map.put("store_name", mechanicStoreName);
        map.put("phone_number_entrance", phoneNumber);
        map.put("phone_number_mechanic", mechanicPhone);
        map.put("about", mechanicAbout);
        map.put("x_location", lttFinal);
        map.put("y_location", lngFinal);


        if (getIntent().getSerializableExtra("mechanicInfo") != null) {
            map.put("m_id", String.valueOf(mechanic.getId()));
        } else if (getIntent().getIntExtra("id", 0) != 0) {
            map.put("m_id", SharedPrefUtils.getStringData("mechanicId"));
        } else map.put("m_id", String.valueOf(0));


        if (files.size() == 0) {
            {
                stringCall = Application.getApi().sendQuestion(map);

            }
        } else {
            stringCall = Application.getApi().uploadMultipleFilesDynamic(map, files);

        }


        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();

                sweetAlertDialog.dismissWithAnimation();

                if (body != null) {

                    JSONObject jsonObject;
                    String entranceId;
                    String jsonString = body;
                    try {
                        jsonObject = new JSONObject(jsonString);
                        String state = jsonObject.getString("state");
                        if (state.equals("saved")) {
                            entranceId = jsonObject.getString("entrance_id");
                            String mId = jsonObject.getString("m_id");
                            SharedPrefUtils.saveData("entranceId", entranceId);
                            SharedPrefUtils.saveData("type", 1);
                            SharedPrefUtils.saveData("mechanicId", mId);
                        }

                        startActivity(new Intent(NewMechanicActivity2.this, MainActivity.class));
                    } catch (JSONException e) {
                        SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.ERROR_TYPE).setTitleText("خطا در برقراری ارتباط");
                        sweetAlertDialogSendCode.show();
                        sweetAlertDialog.dismiss();
                    }

                } else {
                    SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.ERROR_TYPE).setTitleText("خطا در برقراری ارتباط");
                    sweetAlertDialogSendCode.show();
                    sweetAlertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                View view = LayoutInflater.from(NewMechanicActivity2.this).inflate(R.layout.view_good_not_found, null, false);
                TextView textView = view.findViewById(R.id.txt);
                textView.setText("خطا در ارسال اطلاعات.\n سرعت اینترنت شما برای به روز رسانی اطلاعات کافی نیست.");
                RelativeLayout btnShowAllGoods = view.findViewById(R.id.btn_show_all_goods);

                RelativeLayout contactUs = view.findViewById(R.id.btn_contact_us);
                LottieAnimationView warranty_lt = view.findViewById(R.id.warranty_lt);
                warranty_lt.setVisibility(View.GONE);
                btnShowAllGoods.setVisibility(View.GONE);

                ((TextView) view.findViewById(R.id.txt_ok)).setText("خروج");

                contactUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewMechanicActivity2.this.finish();
                    }
                });
                sweetAlertDialogGoodNotExist = new SweetAlertDialog(NewMechanicActivity2.this).hideConfirmButton()
                        .setCustomView(view);

                sweetAlertDialogGoodNotExist.setCancelable(false);
                sweetAlertDialogGoodNotExist.show();
            }
        });
    }



    public String jobSeparator(List<?> list) {
        StringBuilder jobIds = new StringBuilder();
        if (list.size() == 1) {
            return String.valueOf(list.get(0));
        }
        for (int i = 0; i < list.size(); i++) {
            String connector;

            if (i == list.size() - 1) connector = "";
            else connector = ",";
            jobIds.append(list.get(i)).append(connector);
        }
        return jobIds.toString();

    }

    public BroadcastReceiver jobRowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jobName = intent.getStringExtra("jobName");
            String jobId = intent.getStringExtra("jobId");
            listIds.add(Integer.parseInt(jobId));
            listNames.add(jobName);


        }
    };

    @Override
    public void onDownloadStateClick(AdminMedia adminMedia, View viewHolder) {

    }


    @Override
    public void onDownloadStateClick(Movies movies, View itemView) {
        ProgressCircula progressCircula;
        LottieAnimationView lottieAnimationView;
        TextView percentDone;
        progressCircula = itemView.findViewById(R.id.progressCircula);
        lottieAnimationView = itemView.findViewById(R.id.lottieAnimationView);
        percentDone = itemView.findViewById(R.id.percentDone);
        String adminUrl = movies.getMovie_url();
        String url = movies.getMovie_url();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + url.substring(url.lastIndexOf("/")));

        if (file.exists() && (file.length() - movies.getMovie_size() == -8 || file.length() - movies.getMovie_size() == 0)) {

            Intent intent = new Intent(this, ExoVideoActivity.class);
            intent.putExtra("path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + url.
                    substring(url.lastIndexOf("/")));

            intent.putExtra("id", movies.getId());
            startActivity(intent);
            return;
        }

        if (!lottieAnimationView.isAnimating()) {
            lottieAnimationView.resumeAnimation();
            progressCircula.startRotation();
        } else {
            lottieAnimationView.pauseAnimation();
            progressCircula.stopRotation();
        }


        int downloadId = SharedPrefUtils.getIntData("downloadId**" + movies.getMovie_desc());
        if (Status.RUNNING == PRDownloader.getStatus(downloadId)) {
            PRDownloader.pause(downloadId);
            progressCircula.stopRotation();
            return;
        }
        if (Status.PAUSED == PRDownloader.getStatus(downloadId)) {
            PRDownloader.resume(downloadId);
            progressCircula.startRotation();
            return;
        }


        DownloadRequest downloadRequest = PRDownloader.download(url, path, adminUrl.substring(adminUrl.lastIndexOf("/"))).build();
        downloadRequest.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {

            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int value = (int) (100 * progress.currentBytes / progress.totalBytes);
                progressCircula.setProgress(value);
                percentDone.setText(String.valueOf(value) + "%");
            }
        }).setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {

            }
        });
        downloadId = downloadRequest.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("downloadId**" + movies.getMovie_desc()).apply();
                progressCircula.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                lottieAnimationView.setAnimation(R.raw.play_anim);
                lottieAnimationView.setRepeatCount(0);
                lottieAnimationView.playAnimation();

                Picasso.get().load(movies.getMovie_preview())
                        .into(((ImageView) itemView.findViewById(R.id.preview)));
                ((TextView) itemView.findViewById(R.id.totalSize)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));


            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("downloadId**" + movies.getMovie_desc(), downloadId);

    }

    @Override
    public void onRemoveClick(Movies movies, View viewHolder) {

    }

    public <E extends ImageView> void loadImage(String imageUrl, E imageView, SpinKitView spinKitView, ImageView retryImage) {
        if (imageUrl.trim().length() == 0) return;

        imageView.setVisibility(View.GONE);
        spinKitView.setVisibility(View.VISIBLE);
        retryImage.setVisibility(View.GONE);

        String Store1ImageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length() - 1);
        Glide.with(this)
                .asBitmap().timeout(10000)
                .load(getString(R.string.drweb) + imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    private Bitmap resource;
                    private Transition<? super Bitmap> transition;

                    @Override
                    public void onStart() {
                        super.onStart();
                        imageView.setVisibility(View.GONE);
                        spinKitView.setVisibility(View.VISIBLE);
                        retryImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        this.resource = resource;
                        this.transition = transition;
                        imageView.setImageBitmap(resource);
                        File file = createImageFile(Store1ImageName);
                        FileOutputStream ostream;
                        try {
                            ostream = new FileOutputStream(file);
                            resource.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.close();

                            int i;
                            if (imageUrl.equals(mechanic.getMechanic_image())) {
                                i = 3;
                            } else if (imageUrl.equals(mechanic.getStore_image_1())) {
                                i = 0;
                            } else if (imageUrl.equals(mechanic.getStore_image_2())) {
                                i = 1;
                            } else if (imageUrl.equals(mechanic.getStore_image_3())) {
                                i = 2;
                            } else {
                                i = -1;

                            }


                            SharedPrefUtils.saveData("imageNo" + i, file.getPath());
                            fill_images[i] = true;
                            addToFileList(i);

                            spinKitView.setVisibility(View.GONE);
                            retryImage.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }


                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        if (sweetAlertDialogFailureLoading != null && sweetAlertDialogFailureLoading.isShowing())
                            return;
                        spinKitView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        retryImage.setVisibility(View.VISIBLE);
                        fill_images[current_image] = false;

                        sweetAlertDialogFailureLoading = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialogFailureLoading.setTitle("خطا در دریافت اطلاعات");
                        sweetAlertDialogFailureLoading.setContentText("متاسفانه به علت پایین بودن سرعت اینترنت قادر به ویرایش اطلاعات نیستید.");
                        sweetAlertDialogFailureLoading.setConfirmText("خروج");
                        sweetAlertDialogFailureLoading.setCancelable(false);
                        sweetAlertDialogFailureLoading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialogFailureLoading) {
                                NewMechanicActivity2.this.finish();
                            }
                        });
                        sweetAlertDialogFailureLoading.show();
                    }


                });

    }
}
