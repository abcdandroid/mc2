package com.example.mechanic2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.mechanic2.adapters.MechanicRecyclerAdapter;
import com.example.mechanic2.adapters.StoreRecyclerAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.QuestionImagesFragment;
import com.example.mechanic2.interfaces.AnswerVoiceOnClickListener;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.example.mechanic2.models.AnswerWithMsg;
import com.example.mechanic2.models.Answers;
import com.example.mechanic2.models.Good;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.models.MechanicWithMsg;
import com.example.mechanic2.models.Question;
import com.example.mechanic2.utils.ViewAnimation;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hmomeni.progresscircula.ProgressCircula;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.tyorikan.voicerecordingvisualizer.RecordingSampler;

import android.widget.Button;

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
    private TextView questionText;
    private FloatingActionButton addAnswerFab;
    private CardView cardMic;
    private CardView cardCall;
    private LottieAnimationView ltRecord;
    private AppCompatButton btnManageRecord;
    private ImageView ivPlayPause;
    private SeekBar sbProgress;
    private LinearLayout layerPb;
    private MediaPlayer player = null;
    private AppBarLayout appbar;

    private LottieAnimationView ltPlayPause;

    private MediaRecorder recorder = null;
    private View lyt_mic;
    private View back_drop;
    private View lyt_call;
    private boolean rotate = false;
    FloatingActionButton fab_add;
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
    private RelativeLayout voiceManager;
    private Chronometer chronometerExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);


        appbar = findViewById(R.id.appbar);
        newAnswer = findViewById(R.id.new_answer);
        newAnswer.setOnClickListener(this);
        extensiblePageIndicator = findViewById(R.id.flexibleIndicator);
        questionImages = findViewById(R.id.questionImages);
        recyclerAnswers = findViewById(R.id.recyclerAnswers);
        recyclerAnswers.setLayoutManager(new LinearLayoutManager(this));
        carName = findViewById(R.id.carName);
        questionText = findViewById(R.id.questionText);
        question = (Question) getIntent().getSerializableExtra("question");
        questionText.setText(question.getQ_text());
        carName.setText(question.getCarName());
        q_id = question.getQ_id();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        seekBarUpdater = new SeekBarUpdater();
        cardMic = findViewById(R.id.card_mic);
        cardCall = findViewById(R.id.card_call);
        app.l(question.getQ_image_url1().trim() + "**");
        app.l(question.getQ_image_url2().trim() + "**");
        app.l(question.getQ_image_url3().trim() + "**");
        if (question.getQ_image_url1().trim().length() == 0 & question.getQ_image_url2().trim().length() == 0 & question.getQ_image_url3().trim().length() == 0) {
            appbar.setVisibility(View.GONE);
        }
        if (question.getQ_image_url1().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url1().trim(), this));
            app.l("A");
        }

        if (question.getQ_image_url2().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url2().trim(), this));
            app.l("B");
        }

        if (question.getQ_image_url3().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url3().trim(), this));
            app.l("C");
        }

        imageUrl = new String[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {
            imageUrl[i] = ((QuestionImagesFragment) adapter.getItem(i)).getImageUrl();
        }


        final FloatingActionButton fab_mic = findViewById(R.id.fab_mic);
        final FloatingActionButton fab_call = findViewById(R.id.fab_call);
        fab_add = findViewById(R.id.fab_add);
        back_drop = findViewById(R.id.back_drop);
        lyt_mic = findViewById(R.id.lyt_mic);
        lyt_call = findViewById(R.id.lyt_call);
        ViewAnimation.initShowOut(lyt_mic);
        ViewAnimation.initShowOut(lyt_call);
        back_drop.setVisibility(View.GONE);

        fab_add.setOnClickListener(this);

        back_drop.setOnClickListener(this);

        fab_mic.setOnClickListener(this);
        cardMic.setOnClickListener(this);
        cardCall.setOnClickListener(this);

        fab_call.setOnClickListener(this);/**/


        questionImages.setAdapter(adapter);


        if (imageUrl.length > 1) {
            extensiblePageIndicator.initViewPager(questionImages);
        } else extensiblePageIndicator.setVisibility(View.INVISIBLE);

        recyclerAnswers.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        resumeAnswerListener(offset);
        getAnswers();/**/
    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_mic);
            ViewAnimation.showIn(lyt_call);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_mic);
            ViewAnimation.showOut(lyt_call);
            back_drop.setVisibility(View.GONE);
        }
    }


    private File createVoiceFile() {

        long timeStamp = System.currentTimeMillis();
        String imageFileName = "NAME_" + timeStamp;
        File storageDir = getExternalFilesDir("audio");
        File voice = null;
        try {
            voice = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".3gp",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return voice;
    }

    private void showTextDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_review);
        dialog.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText et_post = (EditText) dialog.findViewById(R.id.et_post);
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showAudioDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_voice);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        fileName = createVoiceFile().getAbsolutePath();
        ltRecord = dialog.findViewById(R.id.lt_record);
        btnManageRecord = dialog.findViewById(R.id.btn_manage_record);
        ivPlayPause = dialog.findViewById(R.id.ivPlayPause);
        sbProgress = dialog.findViewById(R.id.sbProgress);

        layerPb = dialog.findViewById(R.id.layer_pb);
        File file = new File(fileName);
        layerPb.setVisibility(file.exists() && file.length() > 0 ? View.VISIBLE : View.INVISIBLE);

        dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete file
                File file = new File(fileName);
                if (file.exists()) {
                    app.l(file.delete() ? "ok" : "false");
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send file and delete file
                Map<String, String> data = new HashMap<>();
                data.put("route", "audioUpload");
                data.put("a_entrance_id", "1");
                data.put("q_id", String.valueOf(question.getQ_id()));

                Application.getApi().uploadAudioFile(data, app.prepareAudioPart("recordedAnswer", Uri.parse(fileName))).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        app.l(response.body());
                        File file = new File(fileName);
                        if (file.exists()) {
                            app.l(file.delete() ? "ok" : "false");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        app.l(t.getLocalizedMessage() + "****" + new File(fileName).length());
                    }
                });


                dialog.dismiss();
            }
        });


        btnManageRecord.setOnClickListener(this);
        ivPlayPause.setOnClickListener(this);

        sbProgress.setOnSeekBarChangeListener(this);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    boolean x;
    boolean y;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                toggleFabMode(v);
                break;
            case R.id.back_drop:
                toggleFabMode(fab_add);
                break;
            case R.id.fab_mic:
            case R.id.card_mic:
                showAudioDialog();
                break;
            case R.id.btn_manage_record:
                x = !x;
                recordVoice(x);
                break;
            case R.id.ivPlayPause:
            case R.id.ltPlayPause:
                playVoice(y);
                break;
            case R.id.fab_call:
            case R.id.card_call:
                showTextDialog();
                break;
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
                Map<String, String> data = new HashMap<>();
                data.put("route", "audioUpload");
                data.put("a_entrance_id", "1");
                data.put("q_id", String.valueOf(question.getQ_id()));
                data.put("a_text", etAnswer.getText().toString());

                Call<String> recordedAnswer;
                File file1 = new File(fileName);
                if (file1.length() > 0)
                    recordedAnswer = Application.getApi().uploadAudioFile(data, app.prepareAudioPart("recordedAnswer", Uri.parse(fileName)));
                else recordedAnswer = Application.getApi().uploadAudioFile(data);
                recordedAnswer.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        app.l(response.body());
                        File file = new File(fileName);
                        if (file.exists()) {
                            app.l(file.delete() ? "ok" : "false");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        app.l(t.getLocalizedMessage() + "ffffffff" + new File(fileName).length());
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

    private void recordVoice(boolean x) {
        if (x) {
            File file = new File(fileName);
            if (file.exists()) {
                if (file.delete()) {
                    layerPb.setVisibility(View.INVISIBLE);
                }
            }
            ltRecord.playAnimation();
            btnManageRecord.setText("تموم شد");
            startRecording(fileName);
        } else {
            ltRecord.cancelAnimation();
            ltRecord.setProgress(0f);
            btnManageRecord.setText("یکی دیگه ضبط کن");
            stopRecording();

            File file = new File(fileName);
            layerPb.setVisibility(file.exists() && file.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }


    }

    private void updatePlayingView() {
        sbProgress.setMax(player.getDuration());
        sbProgress.setProgress(player.getCurrentPosition());
        sbProgress.setEnabled(true);

        if (player.isPlaying()) {

            sbProgress.postDelayed(seekBarUpdater, 100);
//            ivPlayPause.setImageResource(R.drawable.pause_icon);
            ltPlayPause.setAnimation(R.raw.play_to_pause);
        } else {

            sbProgress.removeCallbacks(seekBarUpdater);
            //  ivPlayPause.setImageResource(R.drawable.play_icon);
            ltPlayPause.setAnimation(R.raw.pause_to_play);
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
            app.l(e.getMessage());
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startMediaPlayer(String audioAddress) {
        player = MediaPlayer.create(Application.getContext(), Uri.parse(audioAddress));
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();

            }
        });
        player.start();
    }

    private void releaseMediaPlayer() {
        ltPlayPause.setAnimation(R.raw.pause_to_play);
        ltPlayPause.playAnimation();
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
        app.l(imageUrl.length + imageUrl[0] + "QAZZZZ");
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
        ImageView ivPlayPause;
        TextView percentDone;
        LottieAnimationView ltPlayPause;

        progressCirculaSound = v.findViewById(R.id.progressCirculaSound);
        startDownload = v.findViewById(R.id.startDownload);
        percentDone = v.findViewById(R.id.percentDone);
        ivPlayPause = v.findViewById(R.id.ivPlayPause);
        ltPlayPause = v.findViewById(R.id.ltPlayPause);
        ltPlayPause.setAnimation(R.raw.play_to_pause);
        ltPlayPause.pauseAnimation();
        startDownload.setAlpha(0f);
        String url = "http://drkamal3.com/Mechanic/" + answers.getA_voice_url();
        String path = getExternalFilesDir("voice/mp3").getAbsolutePath();

        int downloadId = SharedPrefUtils.getIntData("soundDownloadId**" + answers.getA_id());
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
                percentDone.setText(String.valueOf(value) + "%");
                app.l(String.valueOf(progress.currentBytes));
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
                SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("soundDownloadId**" + answers.getA_id()).apply();
                progressCirculaSound.setVisibility(View.GONE);
                startDownload.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                ivPlayPause.setVisibility(View.VISIBLE);
                ltPlayPause.setVisibility(View.VISIBLE);


                app.l("completed");
            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("soundDownloadId**" + answers.getA_id(), downloadId);


    }


    int offset;
    List<Answers> answers;
    List<Answers> tmpAnswers;
    AnswerRecyclerAdapter adapter;
    boolean isLoading;

    private void getAnswers() {
        offset = 0;
        answers = new ArrayList<>();
        tmpAnswers = new ArrayList<>();
        adapter = new AnswerRecyclerAdapter(this, this, answers, this);

        Map<String, String> map = new HashMap<>();
        map.put("route", "getAnswers");
        map.put("offset", String.valueOf(offset));
        map.put("q_id", String.valueOf(q_id));


        Application.getApi().getAnswersWithMsg(map).enqueue(new Callback<AnswerWithMsg>() {
            @Override
            public void onResponse(Call<AnswerWithMsg> call, Response<AnswerWithMsg> response) {
                if (response.body() != null && response.body().getAnswers().size() > 0) {
                    if (response.body().getMsg().equals("zeroSize")) {
                        new SweetAlertDialog(AnswersActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از خودروهای پیشنهادی کلیک کنید").show();
                        return;
                    }
                    app.l("answers" + response.body().getAnswers().size());

                    answers = response.body().getAnswers();
                    if (answers != null && answers.size() != 0) {
                        tmpAnswers.addAll(answers);
                    } else {
                        if (answers != null) {
                            app.t("not found11");
                            isLoading = false;

                        }
                    }
                    adapter = new AnswerRecyclerAdapter(AnswersActivity.this, AnswersActivity.this, tmpAnswers, AnswersActivity.this);
                    recyclerAnswers.setAdapter(adapter);

                } else {/*
                    sweetAlertDialogQuestionNotExist = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).hideConfirmButton()
                            .setCustomView(view);
                    sweetAlertDialogQuestionNotExist.show();*/
                    app.t("not ffound");
                }
            }

            @Override
            public void onFailure(Call<AnswerWithMsg> call, Throwable t) {
                app.l(t.getLocalizedMessage() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });

    }

    private void resumeGetAnswers(int offset) {

        Map<String, String> map = new HashMap<>();
        map.put("route", "getAnswers");
        map.put("offset", String.valueOf(offset));
        map.put("q_id", String.valueOf(q_id));
        app.l("QQWWrr");

        Application.getApi().getAnswersWithMsg(map).enqueue(new Callback<AnswerWithMsg>() {
            @Override
            public void onResponse(Call<AnswerWithMsg> call, Response<AnswerWithMsg> response) {
                app.l("MMECRES" + response.body().getMsg());
                if (response.body() != null && response.body().getAnswers().size() == 0) {
                    app.l("MMECRES" + response.body().getMsg());
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
                app.l("MMMEC" + t.getLocalizedMessage());
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
    }/**/

}
