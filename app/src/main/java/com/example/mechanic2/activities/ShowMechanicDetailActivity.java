package com.example.mechanic2.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.MechanicWebViewAdapter;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.fragments.QuestionImagesFragment;
import com.example.mechanic2.interfaces.OnViewPagerClickListener;
import com.example.mechanic2.models.Job;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.views.MyTextView;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mechanic2.app.Application.getContext;

@SuppressLint("ParcelCreator")
public class ShowMechanicDetailActivity extends AppCompatActivity implements OnViewPagerClickListener {
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingMl;
    private ViewPager mechanicImages;
    private ExtensiblePageIndicator flexibleIndicator;
    private MotionLayout ml;
    private RelativeLayout b;
    private TextView storeName;
    private SimpleDraweeView ll;
    private NestedScrollView nestedContent;
    private MyTextView myInfo;
    private ImageView personIcon;
    private MyTextView mechanicName;
    private ImageView storeIcon;
    private MyTextView storeNameMain;
    private ImageView jobIcon;
    private MyTextView jobName;
    private ImageView regionIcon;
    private MyTextView regionName;
    private ImageView phoneIcon;

    private ImageView scoreIcon;

    private MyTextView myAbout;
    private MyTextView aboutDesc;
    private MyTextView myMovies;
    private RecyclerView recyclerMechanicMovies;
    private MapView mechanicMapPreview;
    Mechanic mechanic;
    private IMapController mapController;
    private CardView mapParent;
    private CardView film_list_container;
    private IMapController mapControllerFull;
    private GeoPoint startPoint;
    private List<String> imageList;

    private LinearLayout btnCallMechanic;
    private LinearLayout warning_report;
    public static final int CALL_REQUEST_CODE = 110;

    private View view;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView no_mechanic_image;
    private View background;
    RatingBar star;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mechanic_detail);

        view = findViewById(R.id.view);

        star = findViewById(R.id.star);
        appbar = findViewById(R.id.appbar);
        collapsingMl = findViewById(R.id.collapsing_ml);
        mechanicImages = findViewById(R.id.mechanic_images);
        flexibleIndicator = findViewById(R.id.flexibleIndicator);
        no_mechanic_image = findViewById(R.id.no_mechanic_image);
        warning_report = findViewById(R.id.warning_report);
        warning_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(ShowMechanicDetailActivity.this).inflate(R.layout.view_good_not_found, null, false);
                TextView textView = view.findViewById(R.id.txt);
                textView.setText(R.string.warning_alert);

                RelativeLayout btnShowAllGoods = view.findViewById(R.id.btn_show_all_goods);
                MyTextView txt_ok = view.findViewById(R.id.txt_ok);


                RelativeLayout contactUs = view.findViewById(R.id.btn_contact_us);
                MyTextView cancel_action = view.findViewById(R.id.cancel_action);

                txt_ok.setText("بی خیال");
                cancel_action.setText("ارسال گزارش");


                SweetAlertDialog sweetAlertDialogGoodNotExist = new SweetAlertDialog(ShowMechanicDetailActivity.this).hideConfirmButton()
                        .setCustomView(view);
                sweetAlertDialogGoodNotExist.setCancelable(false);
                sweetAlertDialogGoodNotExist.show();

                contactUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sweetAlertDialogGoodNotExist.dismissWithAnimation();
                    }
                });
                btnShowAllGoods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sweetAlertDialogGoodNotExist.dismissWithAnimation();


                        Map<String, String> map = new HashMap<>();

                        map.put("route", "errorReport");
                        map.put("id", String.valueOf(mechanic.getId()));
                        map.put("type", String.valueOf(1));
                        map.put("phone", String.valueOf(SharedPrefUtils.getStringData("phoneNumber")));

                        View view = LayoutInflater.from(ShowMechanicDetailActivity.this).inflate(R.layout.view_good_not_found, null, false);
                        TextView textView = view.findViewById(R.id.txt);
                        textView.setText("در حال ارسال گزارش خطا");

                        RelativeLayout btnShowAllGoods = view.findViewById(R.id.btn_show_all_goods);
                        btnShowAllGoods.setVisibility(View.GONE);

                        view.findViewById(R.id.warranty_lt).setVisibility(View.GONE);

                        RelativeLayout contactUs = view.findViewById(R.id.btn_contact_us);
                        contactUs.setVisibility(View.GONE);


                        SweetAlertDialog sweetAlertDialogGoodNotExist = new SweetAlertDialog(ShowMechanicDetailActivity.this, SweetAlertDialog.PROGRESS_TYPE).hideConfirmButton()
                                .setCustomView(view);
                        sweetAlertDialogGoodNotExist.setCancelable(true);
                        sweetAlertDialogGoodNotExist.show();

                        Application.getApi().problemReport(map).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                sweetAlertDialogGoodNotExist.dismissWithAnimation();
                                SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(ShowMechanicDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog1.hideConfirmButton();
                                sweetAlertDialog1.setTitle("گزارش شما با موفقیت ثبت گردید.");
                                sweetAlertDialog1.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sweetAlertDialog1.dismissWithAnimation();
                                    }
                                }, 1500);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                sweetAlertDialogGoodNotExist.dismissWithAnimation();
                                SweetAlertDialog sweetAlertDialog1 = new SweetAlertDialog(ShowMechanicDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog1.hideConfirmButton();
                                sweetAlertDialog1.setTitle("گزارش شما با موفقیت ثبت گردید.");
                                sweetAlertDialog1.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sweetAlertDialog1.dismissWithAnimation();
                                    }
                                }, 1500);
                            }
                        });

                    }
                });
            }
        });
        ml = findViewById(R.id.ml);
        b = findViewById(R.id.b);
        storeName = findViewById(R.id.store_name);
        ll = findViewById(R.id.ll);
        nestedContent = findViewById(R.id.nested_content);

        personIcon = findViewById(R.id.person_icon);
        mechanicName = findViewById(R.id.mechanic_name);
        storeIcon = findViewById(R.id.store_icon);
        storeNameMain = findViewById(R.id.store_name_main);
        jobIcon = findViewById(R.id.job_icon);
        jobName = findViewById(R.id.job_name);
        regionIcon = findViewById(R.id.region_icon);
        regionName = findViewById(R.id.region_name);
        film_list_container = findViewById(R.id.film_list_container);
        myAbout = findViewById(R.id.my_about);
        aboutDesc = findViewById(R.id.about_desc);
        myMovies = findViewById(R.id.my_movies);
        background = findViewById(R.id.background);
        btnCallMechanic = findViewById(R.id.btn_call_mechanic);
        recyclerMechanicMovies = findViewById(R.id.recyclerMechanicMovies);
        mechanicMapPreview = findViewById(R.id.mechanic_map_preview);
        mechanic = ((Mechanic) getIntent().getSerializableExtra("mechanic"));
        mapController = mechanicMapPreview.getController();
        startPoint = new GeoPoint(Double.parseDouble(mechanic.getX_location()), Double.parseDouble(mechanic.getY_location()));
        mapController.setZoom(15f);
        mechanicMapPreview.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mechanicMapPreview.setMultiTouchControls(false);
        mapParent = findViewById(R.id.map_parent);
        view.setOnClickListener(v -> {
            String q = "geo:0,0?q=".concat(mechanic.getX_location()).concat(",").concat(mechanic.getY_location());
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(q));
            startActivity(intent);
        });
        recyclerMechanicMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerMechanicMovies.setLayoutAnimation((new LayoutAnimationController(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left))));
        if (mechanic.getMovies().size() > 0) {
            MechanicWebViewAdapter adapter = new MechanicWebViewAdapter(this,mechanic.getMovies());
            recyclerMechanicMovies.setAdapter(adapter);
        } else {
            film_list_container.setVisibility(View.GONE);
        }

        if (mechanic.getScore() == 0) star.setVisibility(View.GONE);
        else {
            star.setVisibility(View.VISIBLE);
            star.setNumStars(mechanic.getScore());
        }
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        imageList = new ArrayList<>();
        if (mechanic.getStore_image_1().length() > 0) imageList.add(mechanic.getStore_image_1());
        if (mechanic.getStore_image_2().length() > 0) imageList.add(mechanic.getStore_image_2());
        if (mechanic.getStore_image_3().length() > 0) imageList.add(mechanic.getStore_image_3());

        personIcon.setVisibility(mechanic.getIs_signed() == 1 ? View.VISIBLE : View.GONE);

        aboutDesc.setText(mechanic.getAbout());

        if (imageList.size() > 0) {
            for (String url : imageList) {
                viewPagerAdapter.addFragment(QuestionImagesFragment.newInstance(url, this));
            }
            mechanicImages.setAdapter(viewPagerAdapter);
            mechanicImages.setVisibility(View.VISIBLE);
            background.setVisibility(View.GONE);
            no_mechanic_image.setVisibility(View.GONE);
        } else {
            mechanicImages.setVisibility(View.INVISIBLE);
            background.setVisibility(View.VISIBLE);
            no_mechanic_image.setVisibility(View.VISIBLE);
        }


        GeoPoint startPoint = new GeoPoint(Double.parseDouble(mechanic.getX_location()), Double.parseDouble(mechanic.getY_location()));


        ArrayList<OverlayItem> overlayArray = new ArrayList<>();
        OverlayItem mapItem = new OverlayItem("", "", startPoint);
        final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_location_on_40);
        mapItem.setMarker(marker);
        overlayArray.add(mapItem);
        ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
        mechanicMapPreview.getOverlays().add(anotherItemizedIconOverlay);

        mapController.setCenter(startPoint);

        storeName.setText(mechanic.getStore_name());
        ll.setImageRequest(null);
        if (mechanic.getMechanic_image().length() > 0) {
            final ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
            progressBarDrawable.setColor(getResources().getColor(R.color.green_A700));
            progressBarDrawable.setBackgroundColor(getResources().getColor(R.color.blue_grey_50));
            progressBarDrawable.setRadius(getResources().getDimensionPixelSize(R.dimen.spacing_xsmall));


            String imageUri = getString(R.string.drweb) + mechanic.getMechanic_image();
            ll.setImageURI(imageUri.replaceAll(" ", "%20"));
            ll.getHierarchy().setProgressBarImage(progressBarDrawable);

            //Picasso.get().load(imageUri).into(ll);
        } else ll.setImageResource(R.drawable.mechanic_avatar);
        mechanicName.setText(mechanic.getName());
        storeNameMain.setText(mechanic.getStore_name());
        bindJobs(mechanic.getJob());
        regionName.setText(mechanic.getRegion().getName().concat((" | " + mechanic.getAddress())));


        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> ml.setProgress(-verticalOffset / ((float) appbar.getTotalScrollRange())));

        View.OnClickListener onClickListener = v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mechanic.getPhone_number().trim()));
            startActivity(intent);

            Map<String, String> map = new HashMap<>();
            map.put("route", "addToCalledMechanic");
            map.put("userId", SharedPrefUtils.getStringData("entranceId"));
            map.put("mechanicId", String.valueOf(mechanic.getId()));
            Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


        };
        btnCallMechanic.setOnClickListener(onClickListener);
    }


    private void bindJobs(List<Job> jobs) {
        StringBuilder carsText = new StringBuilder();
        if (jobs.size() == 1) {
            jobName.setText(jobs.get(0).getName());
            return;
        }
        for (int i = 0; i < jobs.size(); i++) {
            String connector;
            if (i == jobs.size() - 1) connector = "";
            else connector = " _ ";
            carsText.append(jobs.get(i).getName()).append(connector);
        }
        jobName.setText(carsText.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public void onViewPagerClick(View view) {
        Intent intent = new Intent(this, FullThumbActivity.class);


        String[] imageUrl = new String[viewPagerAdapter.getCount()];
        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            imageUrl[i] = ((QuestionImagesFragment) viewPagerAdapter.getItem(i)).getImageUrl();
        }

        intent.putExtra("linkList", imageUrl);
        intent.putExtra("currentItem", mechanicImages.getCurrentItem());
        intent.putExtra("from", "showMechanicDetailActivity");


        startActivity(intent);


    }

}