package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.downloader.request.DownloadRequest;
import com.downloader.utils.Utils;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.MechanicMoviesRecyclerAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.QuestionImagesFragment;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.example.mechanic2.models.AdminMedia;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Job;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.models.Movies;
import com.example.mechanic2.views.MyTextView;
import com.example.mechanic2.views.MyViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hmomeni.progresscircula.ProgressCircula;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

@SuppressLint("ParcelCreator")
public class ShowMechanicDetailActivity extends AppCompatActivity implements OnViewPagerClickListener, OnClickListener {
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingMl;
    private ViewPager mechanicImages;
    private ExtensiblePageIndicator flexibleIndicator;
    private MotionLayout ml;
    private RelativeLayout b;
    private TextView storeName;
    private CircleImageView ll;
    private NestedScrollView nestedContent;
    private MyTextView myInfo;
    private ImageView personIcon;
    private MyTextView mechanicName;
    private ImageView storeIcon;
    private MyTextView storeNameMain;
    private ImageView jobIcon;
    private MyTextView jobName;
    private ImageView regionIcon;
    private MyTextView regionName;
    private ImageView phoneIcon;
    private MyTextView phoneName;
    private ImageView scoreIcon;
    private MyTextView scoreName;
    private MyTextView myAbout;
    private MyTextView aboutDesc;
    private MyTextView myMovies;
    private RecyclerView recyclerMechanicMovies;
    private MapView mechanicMapPreview;
    Mechanic mechanic;
    private IMapController mapController;
    private CardView mapParent;
    private IMapController mapControllerFull;
    private GeoPoint startPoint;
    private List<String> imageList;


    private View view;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mechanic_detail);


        view = findViewById(R.id.view);

        appbar = findViewById(R.id.appbar);
        collapsingMl = findViewById(R.id.collapsing_ml);
        mechanicImages = findViewById(R.id.mechanic_images);
        flexibleIndicator = findViewById(R.id.flexibleIndicator);
        ml = findViewById(R.id.ml);
        b = findViewById(R.id.b);
        storeName = findViewById(R.id.store_name);
        ll = findViewById(R.id.ll);
        nestedContent = findViewById(R.id.nested_content);
        myInfo = findViewById(R.id.my_info);
        personIcon = findViewById(R.id.person_icon);
        mechanicName = findViewById(R.id.mechanic_name);
        storeIcon = findViewById(R.id.store_icon);
        storeNameMain = findViewById(R.id.store_name_main);
        jobIcon = findViewById(R.id.job_icon);
        jobName = findViewById(R.id.job_name);
        regionIcon = findViewById(R.id.region_icon);
        regionName = findViewById(R.id.region_name);
        phoneIcon = findViewById(R.id.phone_icon);
        phoneName = findViewById(R.id.phone_name);
        scoreIcon = findViewById(R.id.score_icon);
        scoreName = findViewById(R.id.score_name);
        myAbout = findViewById(R.id.my_about);
        aboutDesc = findViewById(R.id.about_desc);
        myMovies = findViewById(R.id.my_movies);
        recyclerMechanicMovies = findViewById(R.id.recyclerMechanicMovies);
        mechanicMapPreview = findViewById(R.id.mechanic_map_preview);
        mechanic = ((Mechanic) getIntent().getSerializableExtra("mechanic"));
        mapController = mechanicMapPreview.getController();
        startPoint = new GeoPoint(Double.parseDouble(mechanic.getX_location()), Double.parseDouble(mechanic.getY_location()));
        mapController.setZoom(15f);
        mechanicMapPreview.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mechanicMapPreview.setMultiTouchControls(false);
        mapParent = findViewById(R.id.map_parent);
        view.setOnClickListener(v -> {
            String q = "geo:0,0?q=".concat(mechanic.getX_location()).concat(",").concat(mechanic.getY_location());
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(q));
            startActivity(intent);
        });
        recyclerMechanicMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerMechanicMovies.setLayoutAnimation((new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left))));
        MechanicMoviesRecyclerAdapter adapter = new MechanicMoviesRecyclerAdapter(this, mechanic.getMovies(), this);
        recyclerMechanicMovies.setAdapter(adapter);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        imageList = new ArrayList<>();
        if (mechanic.getStore_image_1().length() > 0) imageList.add(mechanic.getStore_image_1());
        if (mechanic.getStore_image_2().length() > 0) imageList.add(mechanic.getStore_image_2());
        if (mechanic.getStore_image_3().length() > 0) imageList.add(mechanic.getStore_image_3());

        aboutDesc.setText(mechanic.getAbout());

        for (String url : imageList) {
            viewPagerAdapter.addFragment(QuestionImagesFragment.newInstance(url, this));
        }
        mechanicImages.setAdapter(viewPagerAdapter);


        GeoPoint startPoint = new GeoPoint(Double.parseDouble(mechanic.getX_location()), Double.parseDouble(mechanic.getY_location()));


        ArrayList<OverlayItem> overlayArray = new ArrayList<>();
        OverlayItem mapItem = new OverlayItem("", "", startPoint);
        final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
        mapItem.setMarker(marker);
        overlayArray.add(mapItem);
        ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
        mechanicMapPreview.getOverlays().add(anotherItemizedIconOverlay);

        mapController.setCenter(startPoint);

        storeName.setText(mechanic.getStore_name());
        Glide.with(this).load("http://drkamal3.com/Mechanic/" + mechanic.getMechanic_image()).into(ll);
        mechanicName.setText(mechanic.getName());
        storeNameMain.setText(mechanic.getStore_name());
        bindJobs(mechanic.getJob_ids());
        regionName.setText(mechanic.getAddress().concat(mechanic.getRegion_id()));
        phoneName.setText(mechanic.getPhone_number());
        scoreName.setText(String.valueOf(mechanic.getScore()));
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> ml.setProgress(-verticalOffset / ((float) appbar.getTotalScrollRange())));/**/
    }


    private void bindJobs(List<String> jobs) {
        StringBuilder carsText = new StringBuilder();
        if (jobs.size() == 1) {
            jobName.setText(jobs.get(0));
            return;
        }
        for (int i = 0; i < jobs.size(); i++) {
            String connector;
            if (i == jobs.size() - 1) connector = "";
            else connector = "* ";
            carsText.append(jobs.get(i)).append(connector);
        }
        jobName.setText(carsText.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public void onViewPagerClick(View view) {
        Intent intent = new Intent(this, FullThumbActivity.class);


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
    public void onDownloadStateClick(AdminMedia movies, View viewHolder) {

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
        String path = getExternalFilesDir("video/mp4").getAbsolutePath();
        //String getExternalFilesDir("video/mp4").getAbsolutePath() + adminUrl.substring(adminUrl.lastIndexOf("/"));
        File file = new File(getExternalFilesDir("video/mp4").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

        if (file.exists() && file.length() == movies.getMovie_size()) {
            app.l("playing");
            Intent intent = new Intent(this, ExoVideoActivity.class);
            intent.putExtra("path", getExternalFilesDir("video/mp4").getAbsolutePath() + url.
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
                app.l("pause***" + movies.getMovie_desc());
            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int value = (int) (100 * progress.currentBytes / progress.totalBytes);
                progressCircula.setProgress(value);
                percentDone.setText(String.valueOf(value) + "%");
                app.l(String.valueOf(progress.currentBytes));
            }
        }).setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {
                app.l("start or resume***" + movies.getMovie_desc());
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

                Picasso.get().load("http://drkamal3.com/Mechanic/" + movies.getMovie_preview())
                        .into(((ImageView) itemView.findViewById(R.id.preview)));
                ((TextView) itemView.findViewById(R.id.totalSize)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                app.l("completed");

            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("downloadId**" + movies.getMovie_desc(), downloadId);

    }

    @Override
    public void onDownloadCompleteClick(AdminMedia movies, View viewHolder) {

    }
}