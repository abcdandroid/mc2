package com.example.mechanic2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.models.Movies;
import com.hmomeni.progresscircula.ProgressCircula;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class MechanicMoviesRecyclerAdapter extends RecyclerView.Adapter<MechanicMoviesRecyclerAdapter.AdminViewHolder> {

    private Context context;
    private List<Movies> movies;
    private OnClickListener onClickListener;

    public MechanicMoviesRecyclerAdapter(Context context, List<Movies> movies, OnClickListener onClickListener) {
        this.context = context;
        this.movies = movies;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mechanic_movie, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.onBind(movies.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).getId();
    }

    class AdminViewHolder extends RecyclerView.ViewHolder {

        ImageView preview;
        TextView seenCount;
        TextView totalSize;
        TextView percentDone;
        TextView desc;
        ProgressCircula progressCircula;
        LottieAnimationView lottieAnimationView;

        AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            seenCount = itemView.findViewById(R.id.seenCount);
            totalSize = itemView.findViewById(R.id.totalSize);
            percentDone = itemView.findViewById(R.id.percentDone);
            desc = itemView.findViewById(R.id.desc);
            progressCircula = itemView.findViewById(R.id.progressCircula);
            lottieAnimationView = itemView.findViewById(R.id.lottieAnimationView);
        }

        void onBind(Movies movies, OnClickListener onClickListener) {

            String url = movies.getMovie_url();

            File file = new File(context.getExternalFilesDir("video/mp4").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

            if (file.exists() && file.length() == movies.getMovie_size()) {
                app.l(movies.getMovie_desc() + "file exist");
                progressCircula.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                totalSize.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                lottieAnimationView.setAnimation(R.raw.play_anim);
                lottieAnimationView.pauseAnimation();
                lottieAnimationView.setProgress(0.99f);
            }

            File tmpFile = new File(context.getExternalFilesDir("video/mp4").getAbsolutePath() + url.substring(url.lastIndexOf("/")) + ".temp");
            if (tmpFile.exists()) {

                progressCircula.setVisibility(View.VISIBLE);
                percentDone.setVisibility(View.VISIBLE);
                int progress = (int) (tmpFile.length() * 100 / movies.getMovie_size());
                app.l(String.valueOf(tmpFile.length()) + "**" + movies.getMovie_size() + "**" + progress);
                progressCircula.setProgress(progress);
                percentDone.setText(String.valueOf(progress) + "%");
            }


            View itemView = this.itemView;
            totalSize.setText(String.valueOf(movies.getMovie_size()));
            desc.setText(movies.getMovie_desc());
            //Glide.with(Application.getContext()).load("http://drkamal3.com/Mechanic/"+movies.getMovie_preview()).into(preview);
            if (file.exists() && file.length() == movies.getMovie_size()) {
                Picasso.get().load("http://drkamal3.com/Mechanic/" + movies.getMovie_preview())
                        .into(preview);
            } else {
                Picasso.get().load("http://drkamal3.com/Mechanic/" + movies.getMovie_preview())
                        .transform(new BlurTransformation(context, 25, 1)).into(preview);
            }
            lottieAnimationView.setOnClickListener(v -> onClickListener.onDownloadStateClick(movies, itemView));
        }

    }
}
