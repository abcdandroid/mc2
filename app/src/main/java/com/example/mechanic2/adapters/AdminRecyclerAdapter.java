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
import com.example.mechanic2.models.AdminMedia;
import com.hmomeni.progresscircula.ProgressCircula;

import java.io.File;
import java.util.List;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.AdminViewHolder> {

    private Context context;
    private List<AdminMedia> adminMedias;
    private OnClickListener onClickListener;

    public AdminRecyclerAdapter(Context context, List<AdminMedia> adminMedias, OnClickListener onClickListener) {
        this.context = context;
        this.adminMedias = adminMedias;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.onBind(adminMedias.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return adminMedias.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return adminMedias.get(position).getId();
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

        void onBind(AdminMedia adminMedia, OnClickListener onClickListener) {

            String url = adminMedia.getUrl();

            File file = new File(context.getExternalFilesDir("video/mp4").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

            if (file.exists() && file.length() == adminMedia.getFileSize()) {
                app.l(adminMedia.getMedia_desc() + "file exist");
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
                int progress =(int) (tmpFile.length()*100/ adminMedia.getFileSize()) ;
                app.l(String.valueOf(tmpFile.length())+"**"+adminMedia.getFileSize()+"**"+progress);
                progressCircula.setProgress(progress);
                percentDone.setText(String.valueOf(progress)+"%");
            }


            View itemView = this.itemView;
            totalSize.setText(String.valueOf(adminMedia.getFileSize()));
            desc.setText(adminMedia.getMedia_desc());
            Glide.with(Application.getContext()).load(adminMedia.getPreview()).into(preview);
            lottieAnimationView.setOnClickListener(v -> onClickListener.onDownloadStateClick(adminMedia, itemView));
        }

    }
}
