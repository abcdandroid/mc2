package com.example.mechanic2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.models.AdminMedia;
import com.example.mechanic2.views.MyTextView;

import java.util.List;

public class AdminMediaRecyclerAdapter extends RecyclerView.Adapter<AdminMediaRecyclerAdapter.AdminMediaViewHolder> {

    private Context context;
    private List<String> adminMediaList;

    public AdminMediaRecyclerAdapter(Context context, List<String> adminMediaList) {
        this.context = context;
        this.adminMediaList = adminMediaList;
    }

    @NonNull
    @Override
    public AdminMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminMediaViewHolder(LayoutInflater.from(context).inflate(R.layout.row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMediaViewHolder holder, int position) {
        holder.mytext.setText(adminMediaList.get(position));
    }

    @Override
    public int getItemCount() {
        return adminMediaList.size();
    }

    class AdminMediaViewHolder extends RecyclerView.ViewHolder {
        private MyTextView mytext;
        public AdminMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            mytext = itemView.findViewById(R.id.mytext);
        }
    }
}
