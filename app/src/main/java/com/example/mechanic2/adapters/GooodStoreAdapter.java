package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.ShowGoodDetailActivity;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.example.mechanic2.interfaces.OnGoodClickListener;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.views.MyTextView;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GooodStoreAdapter extends RecyclerView.Adapter<GooodStoreAdapter.GooodViewHolder> {
    private List<Goood> gooodList;
    private Activity activity;

    public GooodStoreAdapter(List<Goood> gooodList, Activity activity) {
        this.gooodList = gooodList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GooodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_good, parent, false);
  /*      final GooodViewHolder holder = new GooodViewHolder(v);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShowGoodDetailActivity.class);

                intent.putExtra("good", gooodList.get(holder.getAdapterPosition()));

                Pair<View, String> pair = Pair.create(v.findViewById(R.id.preview), "img");


                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);

                activity.startActivity(intent, optionsCompat.toBundle());
            }
        });*/
        return new GooodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GooodViewHolder holder, int position) {
        holder.binder(gooodList.get(position));
    }

    @Override
    public int getItemCount() {
        return gooodList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    class GooodViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView preview;
        private MyTextView goodName;
        private ImageView carIcon;
        private MyTextView suitableCars;
        private ImageView factoryIcon;
        private MyTextView companyName;
        private MyTextView countryName;
        private ImageView warrantyIcon;
        private MyTextView warrantyName;
        private MyTextView readMore;
        private ImageView stateIcon;
        private MyTextView stateText;
        private LinearLayout parent;


        GooodViewHolder(@NonNull View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            goodName = itemView.findViewById(R.id.good_name);
            carIcon = itemView.findViewById(R.id.car_icon);
            suitableCars = itemView.findViewById(R.id.suitable_cars);
            factoryIcon = itemView.findViewById(R.id.factory_icon);
            companyName = itemView.findViewById(R.id.company_name);
            countryName = itemView.findViewById(R.id.country_name);
            warrantyIcon = itemView.findViewById(R.id.warranty_icon);
            warrantyName = itemView.findViewById(R.id.warranty_name);
            readMore = itemView.findViewById(R.id.read_more);
            stateIcon = itemView.findViewById(R.id.state_icon);
            stateText = itemView.findViewById(R.id.state_text);
            parent = itemView.findViewById(R.id.parent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int status = gooodList.get(getAdapterPosition()).getStatus();
                    if (status == 1)
                        parent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                app.l(status);
                                Intent intent = new Intent(activity, ShowGoodDetailActivity.class);

                                intent.putExtra("good", gooodList.get(getAdapterPosition()));

                                activity.startActivity(intent/*, optionsCompat.toBundle()*/);

                            }
                        });
                }
            }, 100);
        }

        void binder(Goood goood) {
            Glide.with(activity).load(goood.getPreview()).into(preview);
            goodName.setText(goood.getGood_id().trim());
            Gson gson = new Gson();
            Car[] cars = gson.fromJson(goood.getSuitable_car(), Car[].class);
            bindCars(cars);
            companyName.setText(goood.getCompany().trim());
            countryName.setText(goood.getMade_by().trim());
            warrantyName.setText(goood.getWarranty().trim());
            if (goood.getStatus() == 0) {
                stateText.setText("این کالا در حال حاضر موجود نمی باشد");
                stateText.setVisibility(View.VISIBLE);
                stateIcon.setVisibility(View.INVISIBLE);
                readMore.setVisibility(View.INVISIBLE);
            } else if (goood.getIs_stock() == 2 && goood.getStatus() == 1) {

                stateText.setVisibility(View.VISIBLE);
                stateText.setText(activity.getResources().getString(R.string.luxury_good));
                stateText.setTextColor(activity.getResources().getColor(R.color.yellow_900));

                stateIcon.setVisibility(View.VISIBLE);
                stateIcon.setImageDrawable(activity.getDrawable(R.drawable.diamond_ic));
                stateIcon.setColorFilter(activity.getResources().getColor(R.color.yellow_900));

                readMore.setVisibility(View.VISIBLE);

            } else if (goood.getIs_stock() == 1 && goood.getStatus() == 1) {
                stateText.setVisibility(View.VISIBLE);
                stateText.setText(activity.getResources().getString(R.string.stoke_good));
                stateText.setTextColor(activity.getResources().getColor(R.color.red_full));

                stateIcon.setVisibility(View.INVISIBLE);
                stateIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_nis));
                stateIcon.setColorFilter(activity.getResources().getColor(R.color.red_full));

                readMore.setVisibility(View.VISIBLE);
            } else if (goood.getIs_stock() == 0 && goood.getStatus() == 1) {
                stateText.setVisibility(View.INVISIBLE);
                stateIcon.setVisibility(View.INVISIBLE);
                readMore.setVisibility(View.VISIBLE);
            }

        }

        private void bindCars(Car[] cars) {
            StringBuilder carsText = new StringBuilder();

            if (cars.length == 1) {
                suitableCars.setText(cars[0].getName().trim());
                return;
            }
            for (int i = 0; i < cars.length; i++) {
                String connector;

                if (i == cars.length - 1) connector = "";
                else connector = " * ";
                carsText.append(cars[i].getName().trim()).append(connector);
            }

            suitableCars.setText(carsText.toString().trim());
        }
    }


}
