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
import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.models.AdminMedia;
import com.hmomeni.progresscircula.ProgressCircula;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class NewAdminAdapter extends RecyclerView.Adapter<NewAdminAdapter.AdminViewHolder> {

    private Context context;
    private List<AdminMedia> movies;
    private OnClickListener onClickListener;


    public NewAdminAdapter(Context context, List<AdminMedia> movies, OnClickListener onClickListener) {
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).getId();
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }

    class AdminViewHolder extends RecyclerView.ViewHolder{
        ImageView preview;
        TextView totalSize;
        TextView percentDone;
        TextView desc;
        ProgressCircula progressCircula;
        LottieAnimationView lottieAnimationView;
        private ImageView removeIcon;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            totalSize = itemView.findViewById(R.id.totalSize);
            percentDone = itemView.findViewById(R.id.percentDone);
            desc = itemView.findViewById(R.id.desc);
            progressCircula = itemView.findViewById(R.id.progressCircula);
            lottieAnimationView = itemView.findViewById(R.id.lottieAnimationView);
            removeIcon =  itemView.findViewById(R.id.remove_icon);
            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("آیا مایل به حذف این فیلم از حافظه گوشی خود هستید؟");
                    sweetAlertDialog.setConfirmText("خیر");
                    sweetAlertDialog.setCancelText("بله");
                    sweetAlertDialog.show();
                }
            });
        }

        void onBind(AdminMedia movies, OnClickListener onClickListener) {

            app.l("poipoipoioiH");
            String url = movies.getMovie_url();

            File file = new File(context.getExternalFilesDir("video/mp4").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

            if (file.exists() && (file.length() - movies.getMovie_size() == -8 || file.length() - movies.getMovie_size() == 0 )) {
                app.l(movies.getMedia_desc() + "file exist");
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
            desc.setText(movies.getMedia_desc());
            //Glide.with(Application.getContext()).load("http://drkamal3.com/Mechanic/"+movies.getMovie_preview()).into(preview);

            preview.setImageBitmap(null);
            if (file.exists() && file.length() - movies.getMovie_size() == 0) {
                app.l(movies.getMovie_preview()+"oitjd");
                Picasso.get().load(  movies.getMovie_preview())
                        .into(preview);
            } else {
                app.l(movies.getMovie_preview()+"oitjd");
                Picasso.get().load(movies.getMovie_preview())
                        .transform(new BlurTransformation(context, 25, 1)).into(preview);
            }
            lottieAnimationView.setOnClickListener(v -> onClickListener.onDownloadStateClick(movies, itemView));
        }
    }
}
