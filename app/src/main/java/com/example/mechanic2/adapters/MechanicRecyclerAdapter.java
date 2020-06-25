package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.activities.ShowMechanicDetailActivity;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.views.MyTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MechanicRecyclerAdapter extends RecyclerView.Adapter<MechanicRecyclerAdapter.MechanicViewHolder> {
    private Context context;
    private List<Mechanic> mechanicList;

    public MechanicRecyclerAdapter(List<Mechanic> mechanicList, Context context) {
        this.context = context;
        this.mechanicList = mechanicList;
    }

    @NonNull
    @Override
    public MechanicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MechanicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mechanic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MechanicViewHolder holder, int position) {
        holder.binder(mechanicList.get(position));
    }

    @Override
    public int getItemCount() {
        return mechanicList.size();
    }

    public class MechanicViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parent;
        private CircularImageView profileMechanic;
        private ImageView imStoreName;
        private MyTextView tvStoreMechanic;
        private ImageView imJobMechanic;
        private MyTextView tvJobMechanic;
        private ImageView imRegionMechanic;
        private MyTextView tvRegionMechanic;


        public MechanicViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            profileMechanic = itemView.findViewById(R.id.profile_mechanic);
            imStoreName = itemView.findViewById(R.id.im_store_name);
            tvStoreMechanic = itemView.findViewById(R.id.tv_store_mechanic);
            imJobMechanic = itemView.findViewById(R.id.im_job_mechanic);
            tvJobMechanic = itemView.findViewById(R.id.tv_job_mechanic);
            imRegionMechanic = itemView.findViewById(R.id.im_region_mechanic);
            tvRegionMechanic = itemView.findViewById(R.id.tv_region_mechanic);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowMechanicDetailActivity.class);
                    intent.putExtra("mechanic", mechanicList.get(getAdapterPosition()));
                    context.startActivity(intent);
                   // app.l("EEmehc"+mechanicList.get(getAdapterPosition()).getMechanic_image());
                }
            });
        }

        private void binder(Mechanic mechanic) {
            if (mechanic.getMechanic_image().length() > 0)
                Picasso.get().load("http://drkamal3.com/Mechanic/" +mechanic.getMechanic_image()).into(profileMechanic);
            tvStoreMechanic.setText(mechanic.getStore_name());
            bindJobs(mechanic.getJob_ids());
            tvRegionMechanic.setText(mechanic.getRegion_id());
        }

        private void bindJobs(List<String> jobs) {
            StringBuilder jobsText = new StringBuilder();

            if (jobs.size() == 1) {
                tvJobMechanic.setText(jobs.get(0));
                return;
            }
            for (int i = 0; i < jobs.size(); i++) {
                String connector;
                if (i == jobs.size() - 1) connector = "";
                else connector = "* ";
                jobsText.append(jobs.get(i)).append(connector);
            }
            tvJobMechanic.setText(jobsText.toString());
        }
    }


}
