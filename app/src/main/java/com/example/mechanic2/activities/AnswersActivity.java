package com.example.mechanic2.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
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
import com.example.mechanic2.adapters.AnswerRecyclerAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.QuestionImagesFragment;
import com.example.mechanic2.interfaces.AnswerVoiceOnClickListener;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.example.mechanic2.models.AnswerWithMsg;
import com.example.mechanic2.models.Answers;
import com.example.mechanic2.models.Question;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hmomeni.progresscircula.ProgressCircula;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ParcelCreator")
public class AnswersActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AnswerVoiceOnClickListener, OnViewPagerClickListener {
    private ViewPager questionImages;
    private TextView carName;
    private TextView titleName;
    private TextView questionText;
    private FloatingActionButton addAnswerFab;


    private LottieAnimationView ltRecord;
    private AppCompatButton btnManageRecord;
    private ImageView ivPlayPause;
    private ImageView when_no_image_for_question;
    private SeekBar sbProgress;
    private LinearLayout layerPb;
    private MediaPlayer player = null;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;


    private LottieAnimationView ltPlayPause;

    private MediaRecorder recorder = null;

    private boolean rotate = false;

    String fileName;
    Question question;
    private SeekBarUpdater seekBarUpdater;
    private RecyclerView recyclerAnswers;
    private ExtensiblePageIndicator extensiblePageIndicator;
    private FloatingActionButton newAnswer;

    int q_id;
    String[] imageUrl;


    private EditText etAnswer;
    private TextView pressRedButton;
    private LottieAnimationView startRecord;
    private LottieAnimationView recordingWaves;
    private LinearLayout voiceManager;
    private Chronometer chronometerExample;
    private Toolbar toolbar;

    private NestedScrollView nestedContent;
    private SweetAlertDialog sweetAlertDialogErrorConnection;
    private SweetAlertDialog sweetAlertDialogAnswerNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        toolbar = findViewById(R.id.toolbar);

        nestedContent = findViewById(R.id.nested_content);


        appbar = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        newAnswer = findViewById(R.id.new_answer);
        newAnswer.setOnClickListener(this);
        extensiblePageIndicator = findViewById(R.id.flexibleIndicator);
        questionImages = findViewById(R.id.questionImages);
        recyclerAnswers = findViewById(R.id.recyclerAnswers);
        when_no_image_for_question = findViewById(R.id.when_no_image_for_question);
        recyclerAnswers.setLayoutManager(new LinearLayoutManager(this));
        carName = findViewById(R.id.carName);
        titleName = findViewById(R.id.titleName);
        questionText = findViewById(R.id.questionText);
        question = (Question) getIntent().getSerializableExtra("question");
        questionText.setText(question.getQ_text());
        carName.setText(question.getCarName());
        titleName.setText(question.getQ_title());

        q_id = question.getQ_id();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        seekBarUpdater = new SeekBarUpdater();
        if (question.getQ_image_url1().trim().length() == 0 & question.getQ_image_url2().trim().length() == 0 & question.getQ_image_url3().trim().length() == 0) {
            questionImages.setVisibility(View.GONE);
            when_no_image_for_question.setVisibility(View.VISIBLE);
        } else {
            when_no_image_for_question.setVisibility(View.GONE);
            questionImages.setVisibility(View.VISIBLE);
        }
        if (question.getQ_image_url1().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url1().trim(), this));

        }

        if (question.getQ_image_url2().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url2().trim(), this));

        }

        if (question.getQ_image_url3().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url3().trim(), this));

        }

        imageUrl = new String[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {
            imageUrl[i] = ((QuestionImagesFragment) adapter.getItem(i)).getImageUrl();
        }


        questionImages.setAdapter(adapter);


        if (imageUrl.length > 1) {
            extensiblePageIndicator.initViewPager(questionImages);
        } else extensiblePageIndicator.setVisibility(View.INVISIBLE);

        recyclerAnswers.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));

        app.validateConnection(this, sweetAlertDialogErrorConnection, new ConnectionErrorManager() {
            @Override
            public void doAction() {
                resumeAnswerListener(offset);
                getAnswers();
            }
        });


    }


    private File createVoiceFile() {

        long timeStamp = System.currentTimeMillis();
        String imageFileName = "NAME_" + timeStamp;
        File storageDir = getExternalFilesDir("audio");
        File voice = null;
        try {
            voice = File.createTempFile(
                    imageFileName,
                    ".3gp",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return voice;
    }

    boolean x;
    boolean y;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_all_goods:
                finish();
                break;

            case R.id.ivPlayPause:
            case R.id.ltPlayPause:
                playVoice(y);
                break;
            case R.id.btn_contact_us:
            case R.id.new_answer:
                showNewAnswerDialog();
                break;
            case R.id.start_record:
                pressRedButton.setText("در حال ضبط");
                buttonStates(false);
                startRecord.setVisibility(View.INVISIBLE);
                recordingWaves.setVisibility(View.VISIBLE);
                voiceManager.setVisibility(View.VISIBLE);
                long systemCurrTime = SystemClock.elapsedRealtime();
                chronometerExample.setBase(systemCurrTime);
                chronometerExample.start();

                File file = new File(fileName);
                if (file.exists()) {
                    file.delete();
                }
                startRecording(fileName);
                break;
            case R.id.stop:
                pressRedButton.setText("پاسخ صوتیتو گوش کن");
                buttonStates(true);
                stopRecording();
                startRecord.setVisibility(View.INVISIBLE);
                recordingWaves.setVisibility(View.INVISIBLE);
                voiceManager.setVisibility(View.INVISIBLE);
                File file2 = new File(fileName);

                layerPb.setVisibility(file2.exists() && file2.length() > 0 ? View.VISIBLE : View.INVISIBLE);
                break;
            case R.id.cancel_answer:
                File file3 = new File(fileName);
                file3.exists();
                sweetAlertDialog.dismissWithAnimation();
                break;
            case R.id.send_answer:


                if (sweetAlertDialogAnswerNotFound != null)
                    sweetAlertDialogAnswerNotFound.dismiss();
                Map<String, String> data = new HashMap<>();
                data.put("route", "audioUpload");
                data.put("a_entrance_id", SharedPrefUtils.getStringData("entranceId"));
                data.put("q_id", String.valueOf(question.getQ_id()));
                String answerText = etAnswer.getText().toString();
                data.put("a_text", answerText);

                Call<String> recordedAnswer;
                File file1 = new File(fileName);
                if (answerText.length() == 0 && file1.length() == 0) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AnswersActivity.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitle("لطفا به سوال پاسخ دهید.");
                    sweetAlertDialog.hideConfirmButton();
                    sweetAlertDialog.show();
                    new Handler().postDelayed(() -> sweetAlertDialog.dismissWithAnimation(), 1500);
                    return;
                }

                if (file1.length() > 0) {

                    recordedAnswer = Application.getApi().uploadAudioFile(data, app.prepareAudioPart("recordedAnswer", Uri.parse(fileName)));
                } else recordedAnswer = Application.getApi().uploadAudioFile(data);
                recordedAnswer.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        File file = new File(fileName);
                        if (file.exists()) {

                        }


                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AnswersActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setContentText("پاسخ شما با موفقیت ثبت شد و به زودی منتشر خواهد شد").hideConfirmButton().show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }, 2000);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                sweetAlertDialog.dismissWithAnimation();
                break;
        }
    }

    private void buttonStates(boolean isEnabled) {
        sendAnswer.setBackground(getDrawable(isEnabled ? R.drawable.btn_submit_store : R.drawable.btn_disable));
        sendAnswer.setEnabled(isEnabled);
        cancelAnswer.setBackground(getDrawable(isEnabled ? R.drawable.btn_cancel_answer : R.drawable.btn_disable));
        cancelAnswer.setEnabled(isEnabled);
    }

    private ImageView stop;
    private TextView cancelAnswer;
    private TextView sendAnswer;
    SweetAlertDialog sweetAlertDialog;
    TextInputLayout et_answer_layout;

    private void showNewAnswerDialog() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        View view = LayoutInflater.from(this).inflate(R.layout.view_new_answer, null, false);


        fileName = createVoiceFile().getAbsolutePath();
        etAnswer = view.findViewById(R.id.et_answer);
        pressRedButton = view.findViewById(R.id.press_red_button);
        startRecord = view.findViewById(R.id.start_record);
        recordingWaves = view.findViewById(R.id.recording_waves);
        voiceManager = view.findViewById(R.id.voice_manager);
        chronometerExample = view.findViewById(R.id.chronometerExample);
        layerPb = view.findViewById(R.id.layer_pb);
        et_answer_layout = view.findViewById(R.id.et_answer_layout);
        et_answer_layout.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/b.ttf"));

        ltPlayPause = view.findViewById(R.id.ltPlayPause);
        stop = view.findViewById(R.id.stop);

        cancelAnswer = view.findViewById(R.id.cancel_answer);
        sendAnswer = view.findViewById(R.id.send_answer);

        chronometerExample.stop();
        startRecord.setOnClickListener(this);
        stop.setOnClickListener(this);
        cancelAnswer.setOnClickListener(this);
        sendAnswer.setOnClickListener(this);

        ltPlayPause = view.findViewById(R.id.ltPlayPause);
        sbProgress = view.findViewById(R.id.sbProgress);

        sbProgress.setOnSeekBarChangeListener(this);
        ltPlayPause.setOnClickListener(this);

        sweetAlertDialog.setCustomView(view);
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    private void playVoice(boolean y) {



        if (player == null) {

            startMediaPlayer(fileName);
        } else {

            if (player.isPlaying()) {

                player.pause();
            } else {

                player.start();
            }
        }
        updatePlayingView();
    }


    private void updatePlayingView() {

        sbProgress.setMax(player.getDuration());
        sbProgress.setProgress(player.getCurrentPosition());
        sbProgress.setEnabled(true);

        if (player.isPlaying()) {

            sbProgress.postDelayed(seekBarUpdater, 100);
            ltPlayPause.setSpeed(3);
        } else {

            sbProgress.removeCallbacks(seekBarUpdater);
            ltPlayPause.setSpeed(-3);
        }
        ltPlayPause.playAnimation();
    }

    private void startRecording(String fileName) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {

        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startMediaPlayer(String audioAddress) {
        File file = new File(audioAddress);

        player = MediaPlayer.create(this, Uri.fromFile(file));
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();
            }
        });
        player.start();
    }

    private void releaseMediaPlayer() {
        ltPlayPause.setProgress(0);
        sbProgress.removeCallbacks(seekBarUpdater);
        sbProgress.setEnabled(false);
        sbProgress.setProgress(0);
        player.release();
        player = null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            player.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
        Intent intent = new Intent(AnswersActivity.this, FullThumbActivity.class);

        intent.putExtra("from", "answerActivity");
        intent.putExtra("linkList", imageUrl);
        intent.putExtra("currentItem", questionImages.getCurrentItem());
        startActivity(intent);
    }


    private class SeekBarUpdater implements Runnable {
        @Override
        public void run() {

            sbProgress.setProgress(player.getCurrentPosition());
            sbProgress.postDelayed(this, 100);
        }
    }


    @Override
    public void onClick(View v, Answers answers) {
        ProgressCircula progressCirculaSound;
        ImageView startDownload;
        TextView percentDone;
        LottieAnimationView ltPlayPause;

        progressCirculaSound = v.findViewById(R.id.progressCirculaSound);
        startDownload = v.findViewById(R.id.startDownload);
        percentDone = v.findViewById(R.id.percentDone);
        ltPlayPause = v.findViewById(R.id.ltPlayPause);
        ltPlayPause.setProgress(0);
        ltPlayPause.pauseAnimation();
        startDownload.setAlpha(0f);
        String url = getString(R.string.drweb) + answers.getAnswer().getA_voice_url();
        String path = getExternalFilesDir("voice/mp3").getAbsolutePath();

        int downloadId = SharedPrefUtils.getIntData("soundDownloadId**" + answers.getAnswer().getA_id());
        if (Status.RUNNING == PRDownloader.getStatus(downloadId)) {
            PRDownloader.pause(downloadId);
            progressCirculaSound.stopRotation();
            return;
        }
        if (Status.PAUSED == PRDownloader.getStatus(downloadId)) {
            PRDownloader.resume(downloadId);
            progressCirculaSound.startRotation();
            return;
        }

        DownloadRequest downloadRequest = PRDownloader.download(url, path, url.substring(url.lastIndexOf("/"))).build();
        downloadRequest.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int value = (int) (100 * progress.currentBytes / progress.totalBytes);
                progressCirculaSound.setProgress(value);
                percentDone.setText(String.valueOf(value).concat("%"));

            }
        }).setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {
                progressCirculaSound.setVisibility(View.VISIBLE);
            }
        });
        downloadId = downloadRequest.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("soundDownloadId**" + answers.getAnswer().getA_id()).apply();
                progressCirculaSound.setVisibility(View.GONE);
                startDownload.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                ltPlayPause.setVisibility(View.VISIBLE);
                ltPlayPause.setProgress(0);

            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("soundDownloadId**" + answers.getAnswer().getA_id(), downloadId);


    }


    int offset;
    List<Answers> answers;
    List<Answers> tmpAnswers;
    AnswerRecyclerAdapter adapter;
    boolean isLoading;


    private RelativeLayout addAnswer;
    private RelativeLayout returnToQuestions;

    private void getAnswers() {
        offset = 0;
        answers = new ArrayList<>();
        tmpAnswers = new ArrayList<>();
        adapter = new AnswerRecyclerAdapter(this, this, answers, this);

        Map<String, String> map = new HashMap<>();
        map.put("route", "getAnswers");
        map.put("offset", String.valueOf(offset));
        map.put("q_id", String.valueOf(q_id));
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AnswersActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("لطفا شکیبا باشید");
        sweetAlertDialog.setContentText("در حال دریافت پاسخ");
        sweetAlertDialog.show();
        Application.getApi().getAnswersWithMsg(map).enqueue(new Callback<AnswerWithMsg>() {
            @Override
            public void onResponse(Call<AnswerWithMsg> call, Response<AnswerWithMsg> response) {
                sweetAlertDialog.dismissWithAnimation();
                if (response.body() != null && response.body().getAnswers().size() > 0) {




                    answers = response.body().getAnswers();
                    if (answers != null && answers.size() != 0) {
                        tmpAnswers.addAll(answers);
                    } else {
                        if (answers != null) {
                            isLoading = false;

                        }
                    }
                    adapter = new AnswerRecyclerAdapter(AnswersActivity.this, AnswersActivity.this, tmpAnswers, AnswersActivity.this);
                    recyclerAnswers.setAdapter(adapter);

                } else {


                    sweetAlertDialogAnswerNotFound = new SweetAlertDialog(AnswersActivity.this);
                    View inflateViewAnwerNotFound = LayoutInflater.from(AnswersActivity.this).inflate(R.layout.view_good_not_found, null, false);
                    LottieAnimationView lottieAnimationView = inflateViewAnwerNotFound.findViewById(R.id.warranty_lt);
                    lottieAnimationView.setAnimation(R.raw.lt_not_found);
                    TextView textView = inflateViewAnwerNotFound.findViewById(R.id.txt);
                    TextView txt_ok = inflateViewAnwerNotFound.findViewById(R.id.txt_ok);
                    TextView cancel_action = inflateViewAnwerNotFound.findViewById(R.id.cancel_action);
                    addAnswer = inflateViewAnwerNotFound.findViewById(R.id.btn_contact_us);
                    returnToQuestions = inflateViewAnwerNotFound.findViewById(R.id.btn_show_all_goods);

                    addAnswer.setOnClickListener(AnswersActivity.this);
                    returnToQuestions.setOnClickListener(AnswersActivity.this);

                    textView.setText("پاسخی برای این سوال پیدا نشد.\n شما اولین پاسخ رو بدید.");
                    txt_ok.setText("پاسخ به سوال");
                    cancel_action.setText("برگشت به تالار سوالات");


                    sweetAlertDialogAnswerNotFound.setCustomView(inflateViewAnwerNotFound);
                    sweetAlertDialogAnswerNotFound.hideConfirmButton();
                    sweetAlertDialogAnswerNotFound.show();
                }
            }

            @Override
            public void onFailure(Call<AnswerWithMsg> call, Throwable t) {
                SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(AnswersActivity.this);
                View inflate = LayoutInflater.from(AnswersActivity.this).inflate(R.layout.view_good_not_found, null, false);
                TextView textView = inflate.findViewById(R.id.txt);
                TextView txt_ok = inflate.findViewById(R.id.txt_ok);
                RelativeLayout btn_contact_us = inflate.findViewById(R.id.btn_contact_us);
                RelativeLayout btn_show_all_goods = inflate.findViewById(R.id.btn_show_all_goods);
                textView.setText("خطا در دریافت اطلاعات");
                txt_ok.setText("تلاش مجدد");
                btn_show_all_goods.setVisibility(View.GONE);
                btn_contact_us.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAnswers();
                    }
                });

                sweetAlertDialog1.setCustomView(inflate);
                sweetAlertDialog1.show();
            }
        });

    }

    private void resumeGetAnswers(int offset) {

        Map<String, String> map = new HashMap<>();
        map.put("route", "getAnswers");
        map.put("offset", String.valueOf(offset));
        map.put("q_id", String.valueOf(q_id));


        Application.getApi().getAnswersWithMsg(map).enqueue(new Callback<AnswerWithMsg>() {
            @Override
            public void onResponse(Call<AnswerWithMsg> call, Response<AnswerWithMsg> response) {




                if (response.body() != null && response.body().getAnswers().size() == 0) {

                    return;
                }
                List<Answers> newAnswers = response.body().getAnswers();

                if (newAnswers != null) {
                    tmpAnswers.addAll(newAnswers);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<AnswerWithMsg> call, Throwable t) {

            }
        });


    }

    private void resumeAnswerListener(int sortBy) {
        isLoading = false;
        recyclerAnswers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpAnswers.size() - 1 && !isLoading) {
                    isLoading = true;
                    offset++;
                    resumeGetAnswers(offset);

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (player != null)
            releaseMediaPlayer();
        adapter.killMedia();
        super.onBackPressed();
    }
}
