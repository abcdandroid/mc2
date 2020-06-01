package com.example.mechanic2.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.example.mechanic2.adapters.StoreRecyclerAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.AdsViewPagerFragment;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.example.mechanic2.interfaces.ThumbnailViewPagerState;
import com.example.mechanic2.models.Ads;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Good;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.views.MyTextView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShowGoodDetailActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;
    int ci = 0;
    private Activity activity = this;
    private Context context = activity;


    private MyTextView goodName;
    private ImageView carIcon;
    private MyTextView suitableCars;
    private ImageView factoryIcon;
    private MyTextView companyName;
    private MyTextView countryName;
    private ImageView warrantyIcon;
    private MyTextView warrantyName;
    private ImageView stateIcon;
    private MyTextView stateText;
    private LinearLayout parent;
    private MyTextView desc;


    private MediaPlayer mediaPlayer;
    /**/
    private SeekBarUpdater seekBarUpdater;
    private ImageView startDownload;
    private ImageView ivPlayPause;
    private ProgressCircula progressCirculaSound;
    private LottieAnimationView ltPlayPause;
    private TextView percentDone;/**/
    private SeekBar sbProgress;


    private ImageView imSen1;
    private TextView sen1;
    private ImageView imSen2;
    private TextView sen2;
    private ImageView imSen3;
    private TextView sen3;



    private TextView priceBtn;
    private TextView call;
    Goood goood;
    private ExtensiblePageIndicator extensiblePageIndicator;
    String audioAddress;
    private TextView price;

    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                sbProgress.setProgress(mediaPlayer.getCurrentPosition());
                mSeekbarUpdateHandler.postDelayed(this, 50);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_good_detail);
        initViews();
        sbProgress.setEnabled(false);
        //seekBarUpdater = new SeekBarUpdater();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        goood = (Goood) getIntent().getSerializableExtra("good");
        if (goood == null) {
            return;
        }
        binder(goood);
        initViewPager();

        app.l(goood.getSentence_1(),goood.getSentence_2(),goood.getSentence_3());

        audioAddress = context.getExternalFilesDir("voice/mp3").getAbsolutePath() + goood.getVoice().substring(goood.getVoice().lastIndexOf("/"));

        String url = goood.getVoice();
        File file = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")));
        if (file.exists() && file.length() == goood.getFileSize()) {
            mediaPlayer = MediaPlayer.create(Application.getContext(), Uri.parse(audioAddress));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    seekBarUpdater = new SeekBarUpdater();
                    mediaPlayer.seekTo(0);/*
                    mediaPlayer.release();*/
                    if (mediaPlayer.isPlaying()) {
                        sbProgress.postDelayed(seekBarUpdater, 100);
                        ivPlayPause.setImageResource(R.drawable.pause_icon);
                        ltPlayPause.setAnimation(R.raw.play_to_pause);
                    } else {
                        sbProgress.removeCallbacks(seekBarUpdater);
                        ivPlayPause.setImageResource(R.drawable.play_icon);
                        ltPlayPause.setAnimation(R.raw.pause_to_play);
                    }
                }
            });
            app.l("exit");
            progressCirculaSound.setVisibility(View.GONE);
            percentDone.setVisibility(View.GONE);
            startDownload.setVisibility(View.GONE);
            ivPlayPause.setVisibility(View.VISIBLE);
            ltPlayPause.setVisibility(View.VISIBLE);
            ltPlayPause.setAnimation(R.raw.pause_to_play);
            ltPlayPause.playAnimation();
            sbProgress.setEnabled(false);

        }

        File tmpFile = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")) + ".temp");
        if (tmpFile.exists()) {
            sbProgress.setEnabled(false);
            progressCirculaSound.setVisibility(View.VISIBLE);
            percentDone.setVisibility(View.VISIBLE);
            startDownload.setAlpha(0f);
            ivPlayPause.setVisibility(View.GONE);
            ltPlayPause.setVisibility(View.GONE);
            int progress = (int) (tmpFile.length() * 100 / goood.getFileSize());
            app.l(progress + "____");
            progressCirculaSound.stopRotation();
            progressCirculaSound.setProgress(progress);
            progressCirculaSound.setSpeed(0f);
            String text = progress + "%";
            percentDone.setText(text);
        }

        /*if (mediaPlayer != null) {
            ltPlayPause.setAnimation(mediaPlayer.isPlaying() ? R.raw.play_to_pause : R.raw.pause_to_play);
            ltPlayPause.pauseAnimation();
        }*/

        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playManager(goood);
            }
        });/**/


        ivPlayPause.setOnClickListener(this);
        ltPlayPause.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this); /* */
        initComponent();
    }

    private void initViewPager() {
        String thumbAddressesInString = goood.getThumbnails();
        String[] splitThumb = thumbAddressesInString.split(",");
        String[] splitAll = new String[splitThumb.length + 1];
        splitAll[0] = goood.getPreview();
        System.arraycopy(splitThumb, 0, splitAll, 1, splitThumb.length);

        renderData(splitAll);
        viewpager.setOffscreenPageLimit(3);

        extensiblePageIndicator.initViewPager(viewpager);
    }

    private void initViews() {
        extensiblePageIndicator = findViewById(R.id.flexibleIndicator);
        viewpager = findViewById(R.id.viewpager);
        goodName = findViewById(R.id.good_name);
        carIcon = findViewById(R.id.car_icon);
        suitableCars = findViewById(R.id.suitable_cars);
        factoryIcon = findViewById(R.id.factory_icon);
        companyName = findViewById(R.id.company_name);
        countryName = findViewById(R.id.country_name);
        warrantyIcon = findViewById(R.id.warranty_icon);
        warrantyName = findViewById(R.id.warranty_name);
        stateIcon = findViewById(R.id.state_icon);
        stateText = findViewById(R.id.state_text);
        parent = findViewById(R.id.parent);
        desc = findViewById(R.id.desc);

        priceBtn = findViewById(R.id.price);
        call = findViewById(R.id.call);


        startDownload = findViewById(R.id.startDownload);
        progressCirculaSound = findViewById(R.id.progressCirculaSound);
        percentDone = findViewById(R.id.percentDone);
        ivPlayPause = findViewById(R.id.ivPlayPause);
        sbProgress = findViewById(R.id.sbProgress);
        ltPlayPause = findViewById(R.id.ltPlayPause);


        imSen1 = findViewById(R.id.im_sen1);
        sen1 = findViewById(R.id.sen1);
        imSen2 = findViewById(R.id.im_sen2);
        sen2 = findViewById(R.id.sen2);
        imSen3 = findViewById(R.id.im_sen3);
        sen3 = findViewById(R.id.sen3);

    }

    private void playManager(Goood good) {
        ltPlayPause.pauseAnimation();
        startDownload.setAlpha(0f);
        String url = good.getVoice();
        String path = getExternalFilesDir("voice/mp3").getAbsolutePath();

        int downloadId = SharedPrefUtils.getIntData("soundDownloadId**" + good.getId());
        if (Status.RUNNING == PRDownloader.getStatus(downloadId)) {
            PRDownloader.pause(downloadId);
            app.l("AAAAA");
            progressCirculaSound.setSpeed(0.000001f);
            return;
        }
        if (Status.PAUSED == PRDownloader.getStatus(downloadId)) {
            PRDownloader.resume(downloadId);
            progressCirculaSound.startRotation();
            progressCirculaSound.setSpeed(4f);
            return;
        }

        DownloadRequest downloadRequest = PRDownloader.download(url, path, url.substring(url.lastIndexOf("/"))).build();
        downloadRequest.setOnPauseListener(() -> {
        }).setOnProgressListener(progress -> {
            int value = (int) (100 * progress.currentBytes / progress.totalBytes);
            progressCirculaSound.setProgress(value);
            String text = value + "%";
            percentDone.setText(text);
        }).setOnStartOrResumeListener(() -> {
            progressCirculaSound.setVisibility(View.VISIBLE);
            progressCirculaSound.setSpeed(4f);
        });
        downloadId = downloadRequest.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("soundDownloadId**" + good.getId()).apply();
                progressCirculaSound.setSpeed(0f);
                progressCirculaSound.setVisibility(View.GONE);
                startDownload.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                ivPlayPause.setVisibility(View.VISIBLE);
                ltPlayPause.setVisibility(View.VISIBLE);
                sbProgress.setEnabled(true);

                app.l("completed");
            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("soundDownloadId**" + good.getId(), downloadId);
    }

    /**/
    private class SeekBarUpdater implements Runnable {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                sbProgress.setProgress(mediaPlayer.getCurrentPosition());
                sbProgress.postDelayed(this, 100);
            }
        }
    }

    private void renderData(String[] body) {
        for (String url : body) {
            adapter.addFragment(ShowThumbnailFragment.newInstance(url));
        }

        viewpager.setAdapter(adapter);
        //pageSwitcher(body.length);
    }

    private void initComponent() {
        final LinearLayout image = findViewById(R.id.ll);
        final CollapsingToolbarLayout collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
//                app.l(verticalOffset+"SSS"+min_height);
//                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);

            }
        });
    }

    void binder(Goood goood) {
        imSen1.setVisibility(goood.getSentence_1().length()>0?View.VISIBLE:View.GONE);
        imSen2.setVisibility(goood.getSentence_2().length()>0?View.VISIBLE:View.GONE);
        imSen3.setVisibility(goood.getSentence_3().length()>0?View.VISIBLE:View.GONE);

        sen1.setVisibility(goood.getSentence_1().length()>0?View.VISIBLE:View.GONE);
        sen2.setVisibility(goood.getSentence_2().length()>0?View.VISIBLE:View.GONE);
        sen3.setVisibility(goood.getSentence_3().length()>0?View.VISIBLE:View.GONE);

        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = goood.getPrice();
        String formattedNumber = formatter.format(myNumber);

        String separatedPrice = formattedNumber + " تومان";
        priceBtn.setText(separatedPrice);

        sen1.setText(goood.getSentence_1());
        sen2.setText(goood.getSentence_2());
        sen3.setText(goood.getSentence_3());


        desc.setText(goood.getGood_desc());
        goodName.setText(goood.getGood_id());
        Gson gson = new Gson();
        Car[] cars = gson.fromJson(goood.getSuitable_car(), Car[].class);
        bindCars(cars);
        companyName.setText(goood.getCompany());
        countryName.setText(goood.getMade_by());
        warrantyName.setText(goood.getWarranty());
        if (goood.getIs_stock() == 0 && goood.getStatus() == 0) {
            stateText.setText("این کالا در حال حاضر موجود نمی باشد");
            stateText.setVisibility(View.VISIBLE);
            stateIcon.setVisibility(View.VISIBLE);
        } else if (goood.getIs_stock() == 2 && goood.getStatus() == 1) {

            stateText.setVisibility(View.VISIBLE);
            stateText.setText(activity.getResources().getString(R.string.luxury_good));
            stateText.setTextColor(activity.getResources().getColor(R.color.yellow_900));

            stateIcon.setVisibility(View.VISIBLE);
            stateIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_diamond));
            stateIcon.setColorFilter(activity.getResources().getColor(R.color.yellow_900));


        } else if (goood.getIs_stock() == 1 && goood.getStatus() == 1) {
            stateText.setVisibility(View.VISIBLE);
            stateText.setText(activity.getResources().getString(R.string.stoke_good));
            stateText.setTextColor(activity.getResources().getColor(R.color.red_full));

            stateIcon.setVisibility(View.VISIBLE);
            stateIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_nis));
            stateIcon.setColorFilter(activity.getResources().getColor(R.color.red_full));

        } else if (goood.getIs_stock() == 0 && goood.getStatus() == 1) {
            stateText.setVisibility(View.INVISIBLE);
            stateIcon.setVisibility(View.INVISIBLE);
        }

    }

    private void bindCars(Car[] cars) {
        StringBuilder carsText = new StringBuilder();

        if (cars.length == 1) {
            suitableCars.setText(cars[0].getName());
            return;
        }
        for (int i = 0; i < cars.length; i++) {
            String connector;

            if (i == cars.length - 1) connector = "";
            else connector = "* ";
            carsText.append(cars[i].getName()).append(connector);
        }

        suitableCars.setText(carsText.toString());
    }

    /**/
    private void updatePlayingView() {
        sbProgress.setMax(mediaPlayer.getDuration());
        sbProgress.setProgress(mediaPlayer.getCurrentPosition());
        sbProgress.setEnabled(true);
        if (mediaPlayer.isPlaying()) {
            sbProgress.postDelayed(mUpdateSeekbar, 100);
            ivPlayPause.setImageResource(R.drawable.pause_icon);
            ltPlayPause.setAnimation(R.raw.play_to_pause);
        } else {
            sbProgress.removeCallbacks(mUpdateSeekbar);
            ivPlayPause.setImageResource(R.drawable.play_icon);
            ltPlayPause.setAnimation(R.raw.pause_to_play);
        }
        ltPlayPause.playAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPlayPause:
            case R.id.ltPlayPause: {

                if (mediaPlayer == null) {
                    app.l("AA");
                    startMediaPlayer(audioAddress);
                } else {
                    app.l("BB");

                    if (mediaPlayer.isPlaying()) {
                        app.l("CC");
                        mediaPlayer.pause();
                    } else {
                        app.l("DD");
                        mediaPlayer.start();
                    }
                }

                /**/
                updatePlayingView();
            }
            break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaPlayer.seekTo(progress);
        }
    }

    private void startMediaPlayer(String audioAddress) {
        mediaPlayer = MediaPlayer.create(Application.getContext(), Uri.parse(audioAddress));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }

    private void releaseMediaPlayer() {
        mediaPlayer = null;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            releaseMediaPlayer();
        }
        super.onBackPressed();
    }/* */
}

