package com.example.mechanic2.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.AudioItem;

import java.util.List;

public class AudioItemAdapter extends RecyclerView.Adapter<AudioItemAdapter.AudioItemsViewHolder> {

    private MediaPlayer mediaPlayer;

    private List<AudioItem> audioItems;
    private int currentPlayingPosition;
    private SeekBarUpdater seekBarUpdater;
    private AudioItemsViewHolder playingHolder;

    public AudioItemAdapter(List<AudioItem> audioItems) {
        this.audioItems = audioItems;
        this.currentPlayingPosition = -1;
        seekBarUpdater = new SeekBarUpdater();
    }

    @Override
    public AudioItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AudioItemsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(AudioItemsViewHolder holder, int position) {

        if (position == currentPlayingPosition) {
            app.l("again");
            playingHolder = holder;
            updatePlayingView();
        } else {
            updateNonPlayingView(holder);
        }
    }

/*    @Override
    public void onViewRecycled(AudioItemsViewHolder holder) {
        super.onViewRecycled(holder);
        app.l("holder.getAdapterPosition()"+holder.getAdapterPosition()+"**playingHolder.getAdapterPosition():"+playingHolder.getAdapterPosition()+"**currentPlayingPosition:"+currentPlayingPosition);
        if (currentPlayingPosition == holder.getAdapterPosition()) {
            app.l("Enterd");
            updateNonPlayingView(playingHolder);
            playingHolder = null;
        }
    }*/

    private void updateNonPlayingView(AudioItemsViewHolder holder) {
        holder.sbProgress.removeCallbacks(seekBarUpdater);
        holder.sbProgress.setEnabled(false);
        holder.sbProgress.setProgress(0);
        holder.ivPlayPause.setImageResource(R.drawable.play_icon);
    }

    private void updatePlayingView() {
        playingHolder.sbProgress.setMax(mediaPlayer.getDuration());
        playingHolder.sbProgress.setProgress(mediaPlayer.getCurrentPosition());
        playingHolder.sbProgress.setEnabled(true);
        if (mediaPlayer.isPlaying()) {
            playingHolder.sbProgress.postDelayed(seekBarUpdater, 100);
            playingHolder.ivPlayPause.setImageResource(R.drawable.pause_icon);
        } else {
            playingHolder.sbProgress.removeCallbacks(seekBarUpdater);
            playingHolder.ivPlayPause.setImageResource(R.drawable.play_icon);
        }
    }

    void stopPlayer() {
        if (null != mediaPlayer) {
            releaseMediaPlayer();
        }
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

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    class AudioItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        SeekBar sbProgress;
        ImageView ivPlayPause;

        AudioItemsViewHolder(View itemView) {
            super(itemView);
            ivPlayPause = itemView.findViewById(R.id.ivPlayPause);
            ivPlayPause.setOnClickListener(this);
            sbProgress = itemView.findViewById(R.id.sbProgress);
            sbProgress.setOnSeekBarChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == currentPlayingPosition) {
                app.l("DD");
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            } else {
                currentPlayingPosition = getAdapterPosition();
                app.l("AA");
                if (mediaPlayer != null) {
                    app.l("BB");

                    if (null != playingHolder) {
                        app.l("CC");

                        updateNonPlayingView(playingHolder);
                    }
                    mediaPlayer.release();
                }
                playingHolder = this;

                AudioItem audioItem = audioItems.get(currentPlayingPosition);
                if (audioItem.getAudioResId() == null)
                    startMediaPlayer(audioItem.getAudioAddress());
                else if (audioItem.getAudioAddress() == null)
                    startMediaPlayer(audioItem.getAudioResId());
            }
            updatePlayingView();
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

    private void startMediaPlayer(int audioResId) {
        mediaPlayer = MediaPlayer.create(Application.getContext(), audioResId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();
            }
        });
        mediaPlayer.start();
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
}
