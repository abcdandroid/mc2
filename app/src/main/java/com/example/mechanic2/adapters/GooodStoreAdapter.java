package com.example.mechanic2.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mechanic2.R;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.views.MyTextView;
import com.google.gson.Gson;

import java.util.List;

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
        return new GooodViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_good, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GooodViewHolder holder, int position) {
        holder.binder(gooodList.get(position));
    }

    @Override
    public int getItemCount() {
        return gooodList.size();
    }

    class GooodViewHolder extends RecyclerView.ViewHolder {
        private ImageView preview;
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
        }

        void binder(Goood goood) {
            Glide.with(activity).load(goood.getPreview()).into(preview);
            goodName.setText(goood.getGood_id());
            Gson gson = new Gson();
            Car[] cars = gson.fromJson(goood.getSuitable_car(), Car[].class);
            bindCars(cars);
            companyName.setText(goood.getCompany());
            countryName.setText(goood.getMade_by());
            warrantyName.setText(goood.getWarranty());
            if (goood.getIs_stock() == 0 && goood.getStatus() == 0) {
                stateText.setText("این کالا در حال حاضر موجود نمی باشد");
                stateText.setVisibility(View.VISIBLE);
                stateIcon.setVisibility(View.VISIBLE);
                readMore.setVisibility(View.INVISIBLE);
            } else if (goood.getIs_stock() == 1 && goood.getStatus() == 1) {
                stateText.setText("استوک");
                stateText.setVisibility(View.VISIBLE);
                readMore.setVisibility(View.VISIBLE);
                stateIcon.setVisibility(View.VISIBLE);
            } else if (goood.getIs_stock() == 0 && goood.getStatus() == 1) {
                stateText.setVisibility(View.INVISIBLE);
                stateIcon.setVisibility(View.INVISIBLE);
                readMore.setVisibility(View.VISIBLE);
            }


        }

        private void bindCars(Car[] cars) {
            StringBuilder carsText = new StringBuilder();

            if (cars.length == 1) {
                suitableCars.setText(cars[0].getName());
                return;
            }
            for (int i = 0; i < cars.length; i++) {
                String connector;

                if (i == cars.length - 1) connector = "";
                else connector = "* ";
                carsText.append(cars[i].getName()).append(connector);
            }

            suitableCars.setText(carsText.toString());
        }
    }


}
