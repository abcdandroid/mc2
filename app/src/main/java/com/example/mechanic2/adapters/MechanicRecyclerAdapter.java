package com.example.mechanic2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.activities.ShowMechanicDetailActivity;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Job;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.views.MyTextView;
import com.facebook.drawee.view.SimpleDraweeView;

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
        private SimpleDraweeView profileMechanic;
        private MyTextView tvStoreMechanic;
        private ImageView imJobMechanic;
        private MyTextView tvJobMechanic;
        private ImageView imRegionMechanic;
        private ImageView signed_mechanic;
        private MyTextView tvRegionMechanic;
        RatingBar rate_mechanic;

        public MechanicViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            profileMechanic = itemView.findViewById(R.id.profile_mechanic);
            tvStoreMechanic = itemView.findViewById(R.id.tv_store_mechanic);
            imJobMechanic = itemView.findViewById(R.id.im_job_mechanic);
            tvJobMechanic = itemView.findViewById(R.id.tv_job_mechanic);
            imRegionMechanic = itemView.findViewById(R.id.im_region_mechanic);
            tvRegionMechanic = itemView.findViewById(R.id.tv_region_mechanic);
            signed_mechanic = itemView.findViewById(R.id.signed_mechanic);
            rate_mechanic = itemView.findViewById(R.id.rate_mechanic);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowMechanicDetailActivity.class);
                    intent.putExtra("mechanic", mechanicList.get(getAdapterPosition()));
                    context.startActivity(intent);

                }
            });
        }

        private void binder(Mechanic mechanic) {


            if (mechanic.getScore() == 0)
                rate_mechanic.setVisibility(View.GONE);
            else {
                rate_mechanic.setVisibility(View.VISIBLE);
                rate_mechanic.setNumStars(mechanic.getScore());
            }


            profileMechanic.setImageRequest(null);
            if (mechanic.getMechanic_image().length() > 0) {
                app.fresco( context,context.getString(R.string.drmo) + mechanic.getMechanic_image(), profileMechanic);
            } else
                profileMechanic.setImageResource(R.drawable.mechanic_avatar);
            tvStoreMechanic.setText(mechanic.getStore_name());
            bindJobs(mechanic.getJob());
            tvRegionMechanic.setText(mechanic.getRegion().getName());
        }

        private void bindJobs(List<Job> jobs) {
            StringBuilder jobsText = new StringBuilder();

            if (jobs.size() == 1) {
                tvJobMechanic.setText(jobs.get(0).getName());
                return;
            }
            for (int i = 0; i < jobs.size(); i++) {
                String connector;
                if (i == jobs.size() - 1) connector = "";
                else connector = " _ ";
                jobsText.append(jobs.get(i).getName()).append(connector);
            }
            tvJobMechanic.setText(jobsText.toString());
        }
    }


}
