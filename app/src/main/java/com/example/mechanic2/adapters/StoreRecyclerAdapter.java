package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.fragments.StoreFragment;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.interfaces.SearchButtonAction;
import com.example.mechanic2.models.AudioItem;
import com.example.mechanic2.models.Car;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;
import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.VoiceOnClickListener;
import com.example.mechanic2.models.Good;

import java.io.File;
import java.util.List;

public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.StoreViewHolder> {

    private Context context;
    private Activity activity;
    private List<Good> goodList;
    private VoiceOnClickListener voiceOnClickListener;
    private MediaPlayer mediaPlayer;
    private int currentPlayingPosition;
    private int currentExtendedPosition;
    private SeekBarUpdater seekBarUpdater;
    private StoreViewHolder playingHolder;
    private StoreViewHolder expandedHolder;
    private Good good;

    public StoreRecyclerAdapter(Activity activity, Context context, List<Good> goodList, VoiceOnClickListener voiceOnClickListener) {
        this.context = context;
        this.activity = activity;
        this.goodList = goodList;
        this.voiceOnClickListener = voiceOnClickListener;
        this.currentPlayingPosition = -1;
        this.currentExtendedPosition = -1;
        seekBarUpdater = new SeekBarUpdater();
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        StoreViewHolder storeViewHolder = new StoreViewHolder(inflate);
        final LinearLayout parent2 = storeViewHolder.parent;
        final LottieAnimationView expandLottie = storeViewHolder.expand;
        parent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (storeViewHolder.getAdapterPosition() == currentExtendedPosition) {
                    good.setVisible(!good.isVisible());
                    notifyItemChanged(expandedHolder.getAdapterPosition());
                } else {

                    currentExtendedPosition = storeViewHolder.getAdapterPosition();
                    if (good != null) {
                        if (expandedHolder != null) {
                            good.setVisible(false);
                            notifyItemChanged(expandedHolder.getAdapterPosition());
                        }
                    }

                    good = goodList.get(currentExtendedPosition);
                    good.setVisible(true);
                    expandedHolder = storeViewHolder;
                    notifyItemChanged(storeViewHolder.getAdapterPosition());
                }

                /*
                if (storeViewHolder.getAdapterPosition() == currentExtendedPosition) {
                    good.setVisible(!good.isVisible());
                    //  notifyDataSetChanged();
                } else {
                    currentExtendedPosition = storeViewHolder.getAdapterPosition();
                    if (good != null) {
                        good.setVisible(false);
                        // notifyDataSetChanged();
                    }
                    good = goodList.get(currentExtendedPosition);
                    good.setVisible(true);
//                    notifyDataSetChanged();
                }
                    notifyDataSetChanged();
                    */
            }
        });
        return storeViewHolder;
    }

    private void updateNonPlayingView(StoreViewHolder holder) {
        holder.sbProgress.removeCallbacks(seekBarUpdater);
        holder.sbProgress.setEnabled(false);
        holder.sbProgress.setProgress(0);
        holder.ivPlayPause.setImageResource(R.drawable.play_icon);
        holder.ltPlayPause.setAnimation(R.raw.pause_to_play);
        holder.ltPlayPause.playAnimation();
        holder.ltSoundIsPlaying.setVisibility(View.GONE);
    }

    private void updateNonExtendedView(StoreViewHolder holder) {
        holder.expand.setProgress(0.3f);
        holder.secondary_data.setVisibility(View.GONE);
    }


    @Override
    public void onViewRecycled(@NonNull StoreViewHolder holder) {
        super.onViewRecycled(holder);
    }



    private void updatePlayingView() {
        playingHolder.sbProgress.setMax(mediaPlayer.getDuration());
        playingHolder.sbProgress.setProgress(mediaPlayer.getCurrentPosition());
        playingHolder.sbProgress.setEnabled(true);
        playingHolder.ltSoundIsPlaying.setVisibility(View.VISIBLE);
        if (mediaPlayer.isPlaying()) {
            playingHolder.ltSoundIsPlaying.playAnimation();
            playingHolder.sbProgress.postDelayed(seekBarUpdater, 100);
            playingHolder.ivPlayPause.setImageResource(R.drawable.pause_icon);
            playingHolder.ltPlayPause.setAnimation(R.raw.play_to_pause);
        } else {
            playingHolder.ltSoundIsPlaying.pauseAnimation();
            playingHolder.sbProgress.removeCallbacks(seekBarUpdater);
            playingHolder.ivPlayPause.setImageResource(R.drawable.play_icon);
            playingHolder.ltPlayPause.setAnimation(R.raw.pause_to_play);
        }
        playingHolder.ltPlayPause.playAnimation();
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        if (position == currentExtendedPosition)
            expandedHolder = holder;

        if (position == currentPlayingPosition) {

            playingHolder = holder;
            updatePlayingView();
        } else {
            updateNonPlayingView(holder);
        }
        holder.bindView(goodList.get(position));
    }

    @Override
    public int getItemCount() {
        return goodList.size();
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

    class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        private ImageView preview;
        private ImageView startDownload;
        private ImageView ivPlayPause;
        private ProgressCircula progressCirculaSound;
        private LottieAnimationView expand;
        private LottieAnimationView ltPlayPause;
        private LottieAnimationView ltSoundIsPlaying;
        private TextView name;
        private TextView desc;
        private TextView price;
        private TextView last_update;
        private TextView suitable_car;
        private TextView percentDone;
        private LinearLayout secondary_data;
        private LinearLayout parent;
        private LinearLayout add;
        private SeekBar sbProgress;
        private RecyclerView recyclerThumbnail;

        StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            expand = itemView.findViewById(R.id.expand);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            last_update = itemView.findViewById(R.id.last_update);
            suitable_car = itemView.findViewById(R.id.suitable_car);
            secondary_data = itemView.findViewById(R.id.secondary_data);
            add = itemView.findViewById(R.id.add);
            startDownload = itemView.findViewById(R.id.startDownload);
            parent = itemView.findViewById(R.id.parent);
            progressCirculaSound = itemView.findViewById(R.id.progressCirculaSound);
            percentDone = itemView.findViewById(R.id.percentDone);
            ivPlayPause = itemView.findViewById(R.id.ivPlayPause);
            sbProgress = itemView.findViewById(R.id.sbProgress);
            ltPlayPause = itemView.findViewById(R.id.ltPlayPause);
            ltSoundIsPlaying = itemView.findViewById(R.id.ltSoundIsPlaying);
            recyclerThumbnail = itemView.findViewById(R.id.recyclerThumbnail);
            recyclerThumbnail.setLayoutManager(new LinearLayoutManager(Application.getContext(), RecyclerView.HORIZONTAL, false));

        }

        private void bindView(Good good) {

            prepareRecyclerThumbnail();

            String url = good.getVoice();
            File file = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

            if (file.exists() && file.length() == good.getFileSize()) {
                progressCirculaSound.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                startDownload.setVisibility(View.GONE);
                ivPlayPause.setVisibility(View.VISIBLE);
                ltPlayPause.setVisibility(View.VISIBLE);

            }

            File tmpFile = new File(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + url.substring(url.lastIndexOf("/")) + ".temp");
            if (tmpFile.exists()) {

                progressCirculaSound.setVisibility(View.VISIBLE);
                percentDone.setVisibility(View.VISIBLE);
                startDownload.setAlpha(0f);
                ivPlayPause.setVisibility(View.GONE);
                ltPlayPause.setVisibility(View.GONE);
                int progress = (int) (tmpFile.length() * 100 / good.getFileSize());
                progressCirculaSound.setProgress(progress);
                percentDone.setText(String.valueOf(progress) + "%");

            }


            if (mediaPlayer != null && playingHolder != null) {
                playingHolder.ltPlayPause.setAnimation(mediaPlayer.isPlaying() ? R.raw.play_to_pause : R.raw.pause_to_play);
                playingHolder.ltPlayPause.pauseAnimation();
            }

            if (mediaPlayer != null && mediaPlayer.isPlaying() && !good.isVisible()) {
                playingHolder.ltSoundIsPlaying.setVisibility(View.VISIBLE);
            }

            Glide.with(context).load(good.getPreview()).into(preview);
            name.setText(good.getName());
            desc.setText(good.getGood_desc());
            price.setText(String.valueOf(good.getPrice()));
            last_update.setText(good.getPrice_time());
            //app.l(good.getSuitable_car()+"cc");
            Gson gson = new Gson();
            Car[] cars = gson.fromJson(good.getSuitable_car(), Car[].class);
            app.l(cars.length + "cars" + good.getName() + good.getId());
            bindCars(cars);
            boolean expanded = good.isVisible();
            expand.setProgress(expanded ? 1f : 0.3f);


            secondary_data.setVisibility(expanded ? View.VISIBLE : View.GONE);
            startDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voiceOnClickListener.onClick(itemView, good);
                }
            });
            ivPlayPause.setOnClickListener(this);
            ltPlayPause.setOnClickListener(this);
            sbProgress.setOnSeekBarChangeListener(this);
            // parent.setOnClickListener(this);
//            parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (getAdapterPosition() != currentExtendedPosition) {
//                        if (extendedHolder != null) {
//                            StoreRecyclerAdapter.this.collpaseView(extendedHolder);
//                        }
//                        extendedHolder = StoreViewHolder.this;
//                    }
//                    good.setVisible(!expanded);
//                    isExpanded = good.isVisible();
//                    StoreRecyclerAdapter.this.expandView();
//                    // notifyItemChanged(getAdapterPosition());
//                }
//            });
        }

        private void bindCars(Car[] cars) {
            StringBuilder carsText = new StringBuilder();

            if (cars.length == 1) {
                suitable_car.setText(cars[0].getName());
                return;
            }
            for (int i = 0; i < cars.length; i++) {
                String connector;

                if (i == cars.length - 1) connector = "";
                else connector = "* ";
                carsText.append(cars[i].getName()).append(connector);
            }

            suitable_car.setText(carsText.toString());
        }

        private void prepareRecyclerThumbnail() {
            String thumbAddressesInString = goodList.get(getAdapterPosition()).getThumbnails();
            String[] split = thumbAddressesInString.split(",");
            ThumbnailAdapter adapter = new ThumbnailAdapter(split, activity);
            recyclerThumbnail.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivPlayPause:
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

                        startMediaPlayer(context.getExternalFilesDir("voice/mp3").getAbsolutePath() + goodList.get(getAdapterPosition()).getVoice().substring(goodList.get(getAdapterPosition()).getVoice().lastIndexOf("/")));
                    }
                    updatePlayingView();
                }
                break;
                case R.id.parent:
//                    if (getAdapterPosition() == currentExtendedPosition) {
//                        app.l("Aqqq");
//                        good.setVisible(!good.isVisible());
//                        notifyDataSetChanged();
//                    } else {
//                        app.l("Bqqq");
//                        currentExtendedPosition = getAdapterPosition();
//                        app.l(currentExtendedPosition+"**");
//                        if (good != null) {
//
//                            good.setVisible(false);
//                            notifyDataSetChanged();
//                        }
//                        good = goodList.get(currentExtendedPosition);
//                        good.setVisible(true);
//                        notifyDataSetChanged();
//                    }
//                    break;
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



}
