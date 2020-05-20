package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.StoreRecyclerAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.QuestionImagesFragment;
import com.example.mechanic2.models.Question;
import com.example.mechanic2.utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswersActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
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

    private MediaRecorder recorder = null;
    private View lyt_mic;
    private View back_drop;
    private View lyt_call;
    private boolean rotate = false;
    FloatingActionButton fab_add;
    String fileName;
    Question question;
    private SeekBarUpdater seekBarUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        questionImages = findViewById(R.id.questionImages);
        carName = findViewById(R.id.carName);
        questionText = findViewById(R.id.questionText);
        question = (Question) getIntent().getSerializableExtra("question");
        questionText.setText(question.getQ_text());
        carName.setText(question.getCarName());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        seekBarUpdater = new SeekBarUpdater();
        cardMic = findViewById(R.id.card_mic);
        cardCall = findViewById(R.id.card_call);
        app.l(question.getQ_image_url1().trim() + "**");
        app.l(question.getQ_image_url2().trim() + "**");
        app.l(question.getQ_image_url3().trim() + "**");
        if (question.getQ_image_url1().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url1().trim()));
            app.l("A");
        }

        if (question.getQ_image_url2().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url2().trim()));
            app.l("B");
        }

        if (question.getQ_image_url3().trim().length() > 0) {
            adapter.addFragment(QuestionImagesFragment.newInstance(question.getQ_image_url3().trim()));
            app.l("C");
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
                data.put("a_entrance_id","1");
                data.put("q_id",String.valueOf(question.getQ_id()));

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
                        app.l(t.getLocalizedMessage()+"****"+new File(fileName).length());
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
                playVoice(y);
                break;
            case R.id.fab_call:
            case R.id.card_call:
                showTextDialog();
        }
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
            ivPlayPause.setImageResource(R.drawable.pause_icon);
            /*ltPlayPause.setAnimation(R.raw.play_to_pause);*/
        } else {

            sbProgress.removeCallbacks(seekBarUpdater);
            ivPlayPause.setImageResource(R.drawable.play_icon);
            /*ltPlayPause.setAnimation(R.raw.pause_to_play);*/
        }
        /*ltPlayPause.playAnimation();*/
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
/*

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            app.l(e.getMessage());
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }*/

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

        sbProgress.removeCallbacks(seekBarUpdater);
        sbProgress.setEnabled(false);
        sbProgress.setProgress(0);
        ivPlayPause.setImageDrawable(getDrawable(R.drawable.play_icon));
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


    private class SeekBarUpdater implements Runnable {
        @Override
        public void run() {

            sbProgress.setProgress(player.getCurrentPosition());
            sbProgress.postDelayed(this, 100);
        }
    }

}
