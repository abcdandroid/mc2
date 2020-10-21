package com.example.mechanic2.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.Status;
import com.downloader.request.DownloadRequest;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.views.MyTextView;
import com.example.mechanic2.views.MyViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowGoodDetailActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private MyViewPager viewpager;
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

    private SeekBarUpdater seekBarUpdater;
    private ImageView startDownload;
    private ProgressCircula progressCirculaSound;
    private LottieAnimationView ltPlayPause;
    private TextView percentDone;
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
    String[] splitAll;
    LottieAnimationView lottieAnimationView;
    private int currentCondition;
    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                try {
                    currentCondition = mediaPlayer.getCurrentPosition();
                    if (sbProgress != null) {
                        sbProgress.setProgress(currentCondition);
                        mSeekbarUpdateHandler.postDelayed(this, 50);
                    }
                } catch (Exception ex) {
                    mediaPlayer.seekTo(0);
                    currentCondition = mediaPlayer.getCurrentPosition();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_good_detail);
        initViews();
        sbProgress.setEnabled(false);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        goood = (Goood) getIntent().getSerializableExtra("good");
        if (goood == null) {
            return;
        }
        binder(goood);
        initViewPager();


        audioAddress = context.getExternalFilesDir("voice/mp3").getAbsolutePath() + goood.getVoice().substring(goood.getVoice().lastIndexOf("/"));

        String url = goood.getVoice();
        File file = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")));
        if (file.exists() && file.length() == goood.getFileSize()) {
            mediaPlayer = MediaPlayer.create(Application.getContext(), Uri.parse(audioAddress));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    seekBarUpdater = new SeekBarUpdater();
                    mediaPlayer.seekTo(0);
                    ltPlayPause.setProgress(0);
                    lottieAnimationView.pauseAnimation();
                    lottieAnimationView.setProgress(0);
                    ltPlayPause.setAnimation(R.raw.pplt);
                    if (mediaPlayer.isPlaying()) {
                        sbProgress.postDelayed(seekBarUpdater, 100);
                        ltPlayPause.setSpeed(-3f);
                    } else {
                        sbProgress.removeCallbacks(seekBarUpdater);
                        ltPlayPause.setSpeed(3);
                    }
                }
            });

            progressCirculaSound.setVisibility(View.GONE);
            percentDone.setVisibility(View.GONE);
            startDownload.setVisibility(View.GONE);
            ltPlayPause.setVisibility(View.VISIBLE);
            ltPlayPause.setAnimation(R.raw.pplt);
            ltPlayPause.setProgress(0f);
            sbProgress.setEnabled(false);
        }

        File tmpFile = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")) + ".temp");
        if (tmpFile.exists()) {
            sbProgress.setEnabled(false);
            progressCirculaSound.setVisibility(View.VISIBLE);
            percentDone.setVisibility(View.VISIBLE);
            startDownload.setAlpha(0f);

            ltPlayPause.setVisibility(View.GONE);
            int progress = (int) (tmpFile.length() * 100 / goood.getFileSize());

            progressCirculaSound.stopRotation();
            progressCirculaSound.setProgress(progress);
            progressCirculaSound.setSpeed(0f);
            String text = progress + "%";
            percentDone.setText(text);
        }


        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playManager(goood);
            }
        });
        call.setOnClickListener(this);

        ltPlayPause.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);
        initComponent();
    }


    private void initViewPager() {
        String thumbAddressesInString = goood.getThumbnails().trim();
        if (thumbAddressesInString.length() > 0) {
            String[] splitThumb = thumbAddressesInString.split(",");
            if (splitThumb[0].length() > 0) {
                splitAll = new String[splitThumb.length + 1];
                System.arraycopy(splitThumb, 0, splitAll, 1, splitThumb.length);
            } else
                splitAll = new String[1];
        } else splitAll = new String[1];

        splitAll[0] = goood.getPreview();
        renderData(splitAll);
        viewpager.setOffscreenPageLimit(3);
        if (splitAll.length > 1) {
            extensiblePageIndicator.initViewPager(viewpager);
        } else extensiblePageIndicator.setVisibility(View.INVISIBLE);
    }

    private void initViews() {
        extensiblePageIndicator = findViewById(R.id.flexibleIndicator);
        viewpager = findViewById(R.id.viewpager);
        lottieAnimationView = findViewById(R.id.voice_img);
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
        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String path = getExternalFilesDir("voice/mp3").getAbsolutePath();

        int downloadId = SharedPrefUtils.getIntData("soundDownloadId**" + good.getId());
        if (Status.RUNNING == PRDownloader.getStatus(downloadId)) {
            PRDownloader.pause(downloadId);

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

                ltPlayPause.setVisibility(View.VISIBLE);
                sbProgress.setEnabled(true);


            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("soundDownloadId**" + good.getId(), downloadId);
    }


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
            ShowThumbnailFragment fragment = ShowThumbnailFragment.newInstance(url, new OnViewPagerClickListener() {
                @Override
                public void onViewPagerClick(View view) {
                    Intent intent = new Intent(ShowGoodDetailActivity.this, FullThumbActivity.class);
                    intent.putExtra("from", "showGoodDetailActivity");
                    intent.putExtra("linkList", splitAll);
                    intent.putExtra("currentItem", viewpager.getCurrentItem());
                    ShowGoodDetailActivity.this.startActivity(intent);
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {

                }
            });
            adapter.addFragment(fragment);
        }

        viewpager.setAdapter(adapter);

    }

    private void initComponent() {
        final LinearLayout image = findViewById(R.id.ll);
        final CollapsingToolbarLayout collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;


                image.setScaleY(scale >= 0 ? scale : 0);

            }
        });
    }

    void binder(Goood goood) {
        imSen1.setVisibility(goood.getSentence_1().length() > 0 ? View.VISIBLE : View.GONE);
        imSen2.setVisibility(goood.getSentence_2().length() > 0 ? View.VISIBLE : View.GONE);
        imSen3.setVisibility(goood.getSentence_3().length() > 0 ? View.VISIBLE : View.GONE);

        sen1.setVisibility(goood.getSentence_1().length() > 0 ? View.VISIBLE : View.GONE);
        sen2.setVisibility(goood.getSentence_2().length() > 0 ? View.VISIBLE : View.GONE);
        sen3.setVisibility(goood.getSentence_3().length() > 0 ? View.VISIBLE : View.GONE);
        try {
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber = Integer.parseInt(goood.getPrice());
            String formattedNumber = formatter.format(myNumber);

            String separatedPrice = formattedNumber + " تومان";
            priceBtn.setText(separatedPrice);

        } catch (NumberFormatException n) {
            priceBtn.setText(goood.getPrice());
        }

        sen1.setText(goood.getSentence_1().trim());
        sen2.setText(goood.getSentence_2().trim());
        sen3.setText(goood.getSentence_3().trim());

        desc.setText(goood.getGood_desc().trim());
        goodName.setText(goood.getGood_id().trim());
        Gson gson = new Gson();
        Car[] cars = gson.fromJson(goood.getSuitable_car(), Car[].class);
        bindCars(cars);
        companyName.setText(goood.getCompany().trim());
        countryName.setText(goood.getMade_by().trim());
        warrantyName.setText(goood.getWarranty().trim());

        if (goood.getStatus() == 0) {
            stateText.setText("این کالا در حال حاضر موجود نمی باشد");
            stateText.setVisibility(View.VISIBLE);
            stateIcon.setVisibility(View.INVISIBLE);
        } else if (goood.getIs_stock() == 2 && goood.getStatus() == 1) {

            stateText.setVisibility(View.VISIBLE);
            stateText.setText(activity.getResources().getString(R.string.luxury_good));
            stateText.setTextColor(activity.getResources().getColor(R.color.yellow_900));

            stateIcon.setVisibility(View.VISIBLE);
            stateIcon.setImageDrawable(activity.getDrawable(R.drawable.diamond_ic));
            stateIcon.setColorFilter(activity.getResources().getColor(R.color.yellow_900));


        } else if (goood.getIs_stock() == 1 && goood.getStatus() == 1) {
            stateText.setVisibility(View.VISIBLE);
            stateText.setText(activity.getResources().getString(R.string.stoke_good));
            stateText.setTextColor(activity.getResources().getColor(R.color.red_full));

            stateIcon.setVisibility(View.INVISIBLE);
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
            else connector = " * ";
            carsText.append(cars[i].getName().trim()).append(connector);
        }

        suitableCars.setText(carsText.toString().trim());
    }


    private void updatePlayingView() {
        sbProgress.setMax(mediaPlayer.getDuration());
        sbProgress.setProgress(mediaPlayer.getCurrentPosition());
        sbProgress.setEnabled(true);
        ltPlayPause.setAnimation(R.raw.pplt);
        if (mediaPlayer.isPlaying()) {
            ltPlayPause.setSpeed(3);
            lottieAnimationView.playAnimation();
            sbProgress.postDelayed(mUpdateSeekbar, 100);

        } else {
            lottieAnimationView.pauseAnimation();
            ltPlayPause.setSpeed(-3);
            sbProgress.removeCallbacks(mUpdateSeekbar);

        }
        ltPlayPause.playAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ltPlayPause: {

                if (mediaPlayer == null) {

                    startMediaPlayer(audioAddress);
                } else {


                    if (mediaPlayer.isPlaying()) {

                        mediaPlayer.pause();
                    } else {

                        mediaPlayer.start();
                    }
                }


                updatePlayingView();
            }
            break;
            case R.id.call:

                Map<String, String> map = new HashMap<>();
                map.put("route", "addToSold");
                map.put("userId", SharedPrefUtils.getStringData("entranceId"));
                map.put("goodId", String.valueOf(goood.getId()));
                Application.getApi().addToSold(map).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + goood.getPhone()));
                startActivity(intent);
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
                // mediaPlayer.release();

                seekBarUpdater = new SeekBarUpdater();
                mediaPlayer.seekTo(0);
                ltPlayPause.setProgress(0);
                lottieAnimationView.pauseAnimation();
                lottieAnimationView.setProgress(0);
                ltPlayPause.setAnimation(R.raw.pplt);
                if (mediaPlayer.isPlaying()) {
                    sbProgress.postDelayed(seekBarUpdater, 100);
                    ltPlayPause.setSpeed(-3f);
                } else {
                    sbProgress.removeCallbacks(seekBarUpdater);
                    ltPlayPause.setSpeed(3);
                }
            }
        });
        mediaPlayer.start();
    }

    private void releaseMediaPlayer() {
        mediaPlayer.stop();
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
            releaseMediaPlayer();
        }
        super.onBackPressed();
    }
}

