package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.models.Movies;
import com.hmomeni.progresscircula.ProgressCircula;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
        TextView totalSize;
        TextView percentDone;
        TextView desc;
        ProgressCircula progressCircula;
        LottieAnimationView lottieAnimationView;
        private ImageView removeIcon;


        AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            preview.setClipToOutline(true);
            totalSize = itemView.findViewById(R.id.totalSize);
            percentDone = itemView.findViewById(R.id.percentDone);
            desc = itemView.findViewById(R.id.desc);
            progressCircula = itemView.findViewById(R.id.progressCircula);
            lottieAnimationView = itemView.findViewById(R.id.lottieAnimationView);
            removeIcon =  itemView.findViewById(R.id.remove_icon);
            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(Application.getContext(),SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("آیا مایل به حذف این فیلم از حافظه گوشی خود هستید؟");
                    sweetAlertDialog.setConfirmText("خیر");
                    sweetAlertDialog.setCancelText("بله");
                    sweetAlertDialog.show();
                }
            });
        }

        void onBind(Movies movies, OnClickListener onClickListener) {

            String url = movies.getMovie_url();

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + url.substring(url.lastIndexOf("/")));


            if (file.exists() && (file.length() - movies.getMovie_size() == -8 || file.length() - movies.getMovie_size() == 0 )) {

                progressCircula.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                totalSize.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                lottieAnimationView.setAnimation(R.raw.play_main2);
                lottieAnimationView.pauseAnimation();
                lottieAnimationView.setProgress(0.99f);
            }

            File tmpFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + url.substring(url.lastIndexOf("/")) + ".temp");
            if (tmpFile.exists()) {

                progressCircula.setVisibility(View.VISIBLE);
                percentDone.setVisibility(View.VISIBLE);
                int progress = (int) (tmpFile.length() * 100 / movies.getMovie_size());

                progressCircula.setProgress(progress);
                percentDone.setText(String.valueOf(progress) + "%");
            }


            View itemView = this.itemView;

            Long movie_size = Long.valueOf(movies.getMovie_size());
            String movie_size_human_readable = "";
            DecimalFormat decimalFormat = new DecimalFormat("##.00");

            if (movie_size < 1024)
                movie_size_human_readable = movie_size + "B";
            else if (movie_size < 1024 * 1024) {
                movie_size_human_readable = decimalFormat.format(movie_size / 1024.00) + " کیلوبایت";
            } else if (movie_size < 1024 * 1024 * 1024)
                movie_size_human_readable = decimalFormat.format(movie_size / 1048576.00) + " مگابایت";
            else {
                movie_size_human_readable = decimalFormat.format(movie_size / 1073741824.00) + " گیگابایت";
            }


            totalSize.setText(movie_size_human_readable);
            desc.setText(movies.getMovie_desc());

            if (file.exists() && (file.length() - movies.getMovie_size() == -8 || file.length() - movies.getMovie_size() == 0 )) {
                Picasso.get().load( movies.getMovie_preview())
                        .into(preview);
            } else {
                Picasso.get().load(  movies.getMovie_preview())
                        .transform(new BlurTransformation(context, 25, 1)).into(preview);
            }
            lottieAnimationView.setOnClickListener(v -> onClickListener.onDownloadStateClick(movies, itemView));
        }

    }
}
