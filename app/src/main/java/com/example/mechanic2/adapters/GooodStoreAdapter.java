package com.example.mechanic2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.activities.ShowGoodDetailActivity;
import com.example.mechanic2.activities.SplashActivity;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.views.MyTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
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

    class GooodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView preview;
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
        private SweetAlertDialog sweetAlertDialogGoodNotExist;


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
                    if (gooodList != null && gooodList.size() > 0 && getAdapterPosition() != -1) {
                        int status = gooodList.get(getAdapterPosition()).getStatus();

                        parent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (status == 1) {
                                    Intent intent = new Intent(activity, ShowGoodDetailActivity.class);
                                    intent.putExtra("good", gooodList.get(getAdapterPosition()));
                                    activity.startActivity(intent);
                                } else {

                                    View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.view_good_not_found, null, false);
                                    TextView textView = view.findViewById(R.id.txt);
                                    RelativeLayout btnShowAllGoods = view.findViewById(R.id.btn_show_all_goods);
                                    RelativeLayout contactUs = view.findViewById(R.id.btn_contact_us);
                                    btnShowAllGoods.setOnClickListener(GooodViewHolder.this);
                                    contactUs.setOnClickListener(GooodViewHolder.this);
                                    sweetAlertDialogGoodNotExist = new SweetAlertDialog(itemView.getContext()).hideConfirmButton()
                                            .setCustomView(view);
                                    sweetAlertDialogGoodNotExist.setCancelable(false);
                                    sweetAlertDialogGoodNotExist.show();

                                }


                            }
                        });
                    }
                }
            }, 100);
        }

        void binder(Goood goood) {

           // Picasso.get().load(goood.getPreview()).into(preview);
            app.fresco(itemView.getContext(),goood.getPreview(),preview);
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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_contact_us:
                    if (sweetAlertDialogGoodNotExist != null)
                        sweetAlertDialogGoodNotExist.dismissWithAnimation();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + SplashActivity.etcetera.get(3).getMessage()));
                    itemView.getContext().startActivity(intent);
                    break;
                case R.id.btn_show_all_goods:
                    sweetAlertDialogGoodNotExist.dismissWithAnimation();
                    break;
            }
        }
    }


}
