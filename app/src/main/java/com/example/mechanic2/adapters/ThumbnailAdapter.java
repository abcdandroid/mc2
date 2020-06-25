package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.FullThumbActivity;
import com.example.mechanic2.app.Application;

import de.hdodenhof.circleimageview.CircleImageView;


public class ThumbnailAdapter extends RecyclerView.Adapter {
    private String[] linkList;
    private Activity activity;

    ThumbnailAdapter(String[] linkList, Activity activity) {
        this.linkList = linkList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(Application.getContext()).inflate(R.layout.item_thumbnail, parent, false);
        return new ThumbnailViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ThumbnailViewHolder) holder).bindView(linkList[position]);
    }

    @Override
    public int getItemCount() {
        return linkList.length;
    }

    class ThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView ivThumbnail;

        ThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }

        void bindView(String url) {
            Glide.with(Application.getContext()).load(url).into(ivThumbnail);
            ivThumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Application.getContext(), FullThumbActivity.class);
            intent.putExtra("currentItem", getAdapterPosition());
            intent.putExtra("linkList", linkList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Application.getContext().startActivity(intent);
        }
    }
}
