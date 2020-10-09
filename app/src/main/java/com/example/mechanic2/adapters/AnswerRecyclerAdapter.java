package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.ShowMechanicDetailActivity;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AnswerVoiceOnClickListener;
import com.example.mechanic2.models.Answers;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.views.MyTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hmomeni.progresscircula.ProgressCircula;

import java.io.File;
import java.util.List;

public class AnswerRecyclerAdapter extends RecyclerView.Adapter<AnswerRecyclerAdapter.AnswerViewHolder> {

    private Context context;
    private Activity activity;
    private List<Answers> answerList;
    private AnswerVoiceOnClickListener voiceOnClickListener;
    private MediaPlayer mediaPlayer;
    private int currentPlayingPosition;
    private SeekBarUpdater seekBarUpdater;
    private AnswerViewHolder playingHolder;
    private Answers answer;


    public AnswerRecyclerAdapter(Activity activity, Context context, List<Answers> answerList, AnswerVoiceOnClickListener voiceOnClickListener) {
        this.context = context;
        this.activity = activity;
        this.answerList = answerList;
        this.voiceOnClickListener = voiceOnClickListener;
        this.currentPlayingPosition = -1;
        seekBarUpdater = new SeekBarUpdater();
    }


    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {

        if (position == currentPlayingPosition) {

            playingHolder = holder;
            updatePlayingView();
        } else {
            updateNonPlayingView(holder);
        }
        holder.bindView(answerList.get(position));
    }

    private void updateNonPlayingView(AnswerViewHolder holder) {
        holder.sbProgress.removeCallbacks(seekBarUpdater);
        holder.sbProgress.setEnabled(false);
        holder.sbProgress.setProgress(0);
        holder.ltPlayPause.setProgress(0);
    }


    private void updatePlayingView() {
        playingHolder.sbProgress.setMax(mediaPlayer.getDuration());
        playingHolder.sbProgress.setProgress(mediaPlayer.getCurrentPosition());
        playingHolder.sbProgress.setEnabled(true);
        if (mediaPlayer.isPlaying()) {
            playingHolder.sbProgress.postDelayed(seekBarUpdater, 100);
            playingHolder.ltPlayPause.setSpeed(3f);
        } else {
            playingHolder.sbProgress.removeCallbacks(seekBarUpdater);
            playingHolder.ltPlayPause.setSpeed(-3f);
        }
        playingHolder.ltPlayPause.playAnimation();
    }


    @Override
    public int getItemCount() {
        return answerList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private class SeekBarUpdater implements Runnable {
        @Override
        public void run() {
            if (null != playingHolder) {
                playingHolder.sbProgress.setProgress(mediaPlayer.getCurrentPosition());
                playingHolder.sbProgress.postDelayed(this, 100);
            }
        }
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

        private ImageView startDownload;
        private ProgressCircula progressCirculaSound;
        private LottieAnimationView ltPlayPause;
        private TextView percentDone;
        private LinearLayout parent;
        private SeekBar sbProgress;
        private SimpleDraweeView ivProfile;
        private MyTextView name;
        private MyTextView readMore;
        private MyTextView answer;
        private LinearLayout voiceParent;
        private MyTextView storeName;


        AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.store_name);
            startDownload = itemView.findViewById(R.id.startDownload);
            voiceParent = itemView.findViewById(R.id.voice_parent);
            parent = itemView.findViewById(R.id.parent);
            answer = itemView.findViewById(R.id.answer);
            progressCirculaSound = itemView.findViewById(R.id.progressCirculaSound);
            percentDone = itemView.findViewById(R.id.percentDone);
            sbProgress = itemView.findViewById(R.id.sbProgress);
            ltPlayPause = itemView.findViewById(R.id.ltPlayPause);
            ltPlayPause.setProgress(0f);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            name = itemView.findViewById(R.id.name);
            readMore = itemView.findViewById(R.id.read_more);


        }

        private void bindView(Answers answer) {

            voiceParent.setVisibility(answer.getAnswer().getA_voice_url().length() == 0 ? View.GONE : View.VISIBLE);

            if (answer.getType() == 0) {
                name.setText("کاربر آنلاین مکانیک");
                storeName.setVisibility(View.GONE);
                readMore.setVisibility(View.GONE);
            } else {
                storeName.setVisibility(View.VISIBLE);
                storeName.setText(answer.getMechanic().getStore_name());
                name.setText(answer.getMechanic().getName());
                readMore.setVisibility(View.VISIBLE);
                parent.setOnClickListener(this);
            }


            if ((answer.getMechanic().getMechanic_image() == null || answer.getMechanic().getMechanic_image().length() == 0) && answer.getType() == 1) {
                ivProfile.setImageResource((R.drawable.mechanic_avatar));
            } else if (answer.getMechanic().getMechanic_image() == null && answer.getType() == 0) {
                ivProfile.setImageResource((R.drawable.profile_normal_user));
            } else
                app.fresco(context, context.getString(R.string.drweb) + answer.getMechanic().getMechanic_image(), ivProfile);

            this.answer.setText(answer.getAnswer().getA_text());

            if (answer.getAnswer().getA_voice_url().equals(""))
                voiceParent.setVisibility(View.GONE);

            if (answer.getAnswer().getA_text().equals("")) this.answer.setVisibility(View.GONE);

            String url = answer.getAnswer().getA_voice_url();


            if (url.length() > 0) {
                File file = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

                if (file.exists() && file.length() == answer.getAnswer().getFileSize()) {
                    progressCirculaSound.setVisibility(View.GONE);
                    percentDone.setVisibility(View.GONE);
                    startDownload.setVisibility(View.GONE);
                    ltPlayPause.setVisibility(View.VISIBLE);
                    ltPlayPause.setProgress(0);
                }

                File tmpFile = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")) + ".temp");
                if (tmpFile.exists()) {
                    progressCirculaSound.setVisibility(View.VISIBLE);
                    percentDone.setVisibility(View.VISIBLE);
                    startDownload.setAlpha(0f);
                    ltPlayPause.setVisibility(View.GONE);
                    int progress = (int) (tmpFile.length() * 100 / answer.getAnswer().getFileSize());
                    progressCirculaSound.setProgress(progress);
                    percentDone.setText(String.valueOf(progress) + "%");
                }


                if (mediaPlayer != null && playingHolder != null) {

                    playingHolder.ltPlayPause.pauseAnimation();
                }


                startDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voiceOnClickListener.onClick(itemView, answer);
                    }
                });

                ltPlayPause.setOnClickListener(this);
                sbProgress.setOnSeekBarChangeListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.ltPlayPause: {
                    if (getAdapterPosition() == currentPlayingPosition) {

                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        } else {
                            mediaPlayer.start();
                        }
                    } else {
                        currentPlayingPosition = getAdapterPosition();

                        if (mediaPlayer != null) {


                            if (null != playingHolder) {

                                updateNonPlayingView(playingHolder);
                            }
                            mediaPlayer.release();
                        }
                        playingHolder = this;

                        startMediaPlayer(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + answerList.get(getAdapterPosition()).getAnswer().getA_voice_url().substring(answerList.get(getAdapterPosition()).getAnswer().getA_voice_url().lastIndexOf("/")));
                    }
                    updatePlayingView();
                }
                break;
                case R.id.parent:


                    Answers answers = answerList.get(getAdapterPosition());

                    Mechanic mechanic = answers.getMechanic();
                    Intent intent = new Intent(activity, ShowMechanicDetailActivity.class);
                    intent.putExtra("mechanic", mechanic);
                    activity.startActivity(intent);


                    break;
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private void startMediaPlayer(String audioAddress) {
        mediaPlayer = MediaPlayer.create(Application.getContext(), Uri.parse(audioAddress));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();
            }
        });
        mediaPlayer.start();
    }

    private void releaseMediaPlayer() {
        if (null != playingHolder) {
            updateNonPlayingView(playingHolder);
        }

        mediaPlayer.release();
        mediaPlayer = null;
        currentPlayingPosition = -1;
    }

    public void killMedia() {
        if (mediaPlayer != null)
            releaseMediaPlayer();
    }


}
