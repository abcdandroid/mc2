package com.example.mechanic2.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.XmlRes;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.activities.TestActivity;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.JobAutoCompleteAdapter;
import com.example.mechanic2.adapters.MechanicRecyclerAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.adapters.QuestionRecyclerAdapter;
import com.example.mechanic2.adapters.RegionAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.models.MechanicWithMsg;
import com.example.mechanic2.models.Question;
import com.example.mechanic2.models.QusetionWithMsg;
import com.example.mechanic2.views.MyTextView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechanicFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 200;
    private CoordinatorLayout parent;
    private AppBarLayout appbar;
    private RelativeLayout submitFilterParent;
    private TextView submitFilter;
    private SpinKitView loading;
    private RelativeLayout jobQuestionParent;
    private AutoCompleteTextView jobQuestion;
    private ImageView resetJob;
    private RelativeLayout regionQuestionParent;
    private AutoCompleteTextView regionQuestion;
    private ImageView resetRegion;
    private AppCompatSpinner spinnerFilter;
    private RecyclerView recyclerMechanic;

    MySpinnerAdapter filterSpinnerAdapter;
    int selectedJobId;
    int selectedRegionId;
    String filterIdInString = "0";
    int i;

    private MyTextView requestForGps;
    private MyTextView allowAccessGps;
    private MyTextView denyAccessGps;
    private GpsReceiver gpsReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_mechanic, container, false);
        return init(inflate);
    }

    SweetAlertDialog sweetAlertDialogRequestGps;

    private View init(View inflate) {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("sd"));
        parent = inflate.findViewById(R.id.parent);
        appbar = inflate.findViewById(R.id.appbar);
        submitFilterParent = inflate.findViewById(R.id.submit_filter_parent);
        submitFilter = inflate.findViewById(R.id.submit_filter);
        loading = inflate.findViewById(R.id.loading);
        jobQuestionParent = inflate.findViewById(R.id.job_question_parent);
        jobQuestion = inflate.findViewById(R.id.job_question);
        resetJob = inflate.findViewById(R.id.reset_job);
        regionQuestionParent = inflate.findViewById(R.id.region_question_parent);
        regionQuestion = inflate.findViewById(R.id.region_question);
        resetRegion = inflate.findViewById(R.id.reset_region);
        spinnerFilter = inflate.findViewById(R.id.spinner_filter);
        recyclerMechanic = inflate.findViewById(R.id.recyclerMechanic);

        resetJob.setOnClickListener(this);
        resetRegion.setOnClickListener(this);
        submitFilterParent.setOnClickListener(this);

        gpsReceiver = new GpsReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(gpsReceiver, new IntentFilter("forGps"));

        List<String> filterNames = new ArrayList<>();
        filterNames.add(getResources().getStringArray(R.array.mechanic_filter)[0]);
        filterNames.add(getResources().getStringArray(R.array.mechanic_filter)[1]);
        filterNames.add(getResources().getStringArray(R.array.mechanic_filter)[2]);
        List<Integer> filterIds = new ArrayList<>();
        filterIds.add(0);
        filterIds.add(1);
        filterIds.add(2);

        filterSpinnerAdapter = new MySpinnerAdapter(getContext(), R.layout.item_spinner, filterNames, filterIds, false);
        spinnerFilter.setAdapter(filterSpinnerAdapter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i++;
                if (view != null) {
                    modifyIds();
                    View view1 = parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent()));
                    TextView myTextView = view1.findViewById(R.id.id_spinner);
                    filterIdInString = myTextView == null ? "1" : myTextView.getText().toString();
                    app.l(filterIdInString + "MMEfid2");
                    if (i > 1) {
                        app.l(filterIdInString + "MMEfid");
                        if (Integer.parseInt(filterIdInString) == 2) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                int checkGpsPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                                if (checkGpsPermission == PackageManager.PERMISSION_GRANTED) {
                                    getLocation();
                                } else {
                                    View gpsView = LayoutInflater.from(getContext()).inflate(R.layout.view_get_gps_permission, null);
                                    requestForGps = gpsView.findViewById(R.id.request_for_gps);
                                    allowAccessGps = gpsView.findViewById(R.id.allow_access_gps);
                                    denyAccessGps = gpsView.findViewById(R.id.deny_access_gps);
                                    sweetAlertDialogRequestGps = new SweetAlertDialog(getContext()).hideConfirmButton().setCustomView(gpsView);
                                    sweetAlertDialogRequestGps.show();
                                    allowAccessGps.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            loading.setVisibility(View.VISIBLE);
                                            submitFilter.setVisibility(View.INVISIBLE);
                                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                                        }
                                    });
                                }


                            } else {
                                getLocation();
                            }
                        } else {
                            app.l("changed");
                            if (locationListener != null)
                                locationManager.removeUpdates(locationListener);
                            resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                            getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });/**/

        JobAutoCompleteAdapter jobAutoCompleteAdapter = new JobAutoCompleteAdapter(getActivity(), R.layout.item_show_auto_complete);
        jobQuestion.setAdapter(jobAutoCompleteAdapter);
        RegionAutoCompleteAdapter regionAutoCompleteAdapter = new RegionAutoCompleteAdapter(getActivity(), R.layout.item_show_auto_complete);
        regionQuestion.setAdapter(regionAutoCompleteAdapter);


        jobQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedJobId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });
        regionQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRegionId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });

        recyclerMechanic.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return super.canScrollVertically();
            }
        });


        jobQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    selectedJobId = 0;
                    resetJob.setVisibility(View.INVISIBLE);
                } else resetJob.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        regionQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    selectedRegionId = 0;
                    resetRegion.setVisibility(View.INVISIBLE);
                } else resetRegion.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        recyclerMechanic.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        loading.setVisibility(View.VISIBLE);
        submitFilter.setVisibility(View.INVISIBLE);
        app.l(selectedJobId + "WW" + selectedRegionId + "WW" + filterIdInString + "WW" + "EEEEEE");
        resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
        getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));


        return inflate;
    }

    int offset;
    List<Mechanic> mechanics;
    List<Mechanic> tmpMechanics;
    MechanicRecyclerAdapter adapter;
    boolean isLoading;
    double x, y;


    LocationManager locationManager;

    LocationListener locationListener;

    void getLocation() {
        app.l("AAA");
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    x = location.getLatitude();
                    y = location.getLongitude();
                    app.l(x + "^^^^^^^^" + y);
                    resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                    getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                    app.l(provider);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    app.t("Please Enable GPS and Internet");
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15, 1000, locationListener);
        } catch (SecurityException e) {
            app.l("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
            e.printStackTrace();
        }
    }

    private void getMechanics(int jobId, int regionId, int sortBy) {
        offset = 0;
        mechanics = new ArrayList<>();
        tmpMechanics = new ArrayList<>();
        adapter = new MechanicRecyclerAdapter(mechanics, getActivity());

        Map<String, String> map = new HashMap<>();
        map.put("route", "getMechanics");
        map.put("offset", String.valueOf(offset));
        map.put("jobId", String.valueOf(jobId));
        map.put("regionId", String.valueOf(regionId));
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        map.put("sortBy", String.valueOf(sortBy));/* */


        Application.getApi().getMechanicWutMsg(map).enqueue(new Callback<MechanicWithMsg>() {
            @Override
            public void onResponse(Call<MechanicWithMsg> call, Response<MechanicWithMsg> response) {
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                if (response.body() != null && response.body().getMechanic().size() > 0) {
                    app.l("MMEC" + response.body().getMsg());
                    if (response.body().getMechanic().get(0).getId() == -2) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از خودروهای پیشنهادی کلیک کنید").show();
                        return;
                    }
                    if (response.body().getMechanic().get(0).getId() == -3) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از موضوعات پیشنهادی کلیک کنید").show();
                        return;
                    }
                    if (response.body().getMechanic().get(0).getId() == -4) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از خودروهای پیشنهادی و یکی از موضوعات پیشنهادی کلیک کنید").show();
                        return;
                    }

                    mechanics = response.body().getMechanic();
                    if (mechanics != null && mechanics.size() != 0) {
                        tmpMechanics.addAll(mechanics);
                    } else {
                        if (mechanics != null) {
                            app.t("not found11");
                            isLoading = false;

                        }
                    }
                    adapter = new MechanicRecyclerAdapter(tmpMechanics, getActivity());
                    recyclerMechanic.setAdapter(adapter);

                } else {/*
                    sweetAlertDialogQuestionNotExist = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).hideConfirmButton()
                            .setCustomView(view);
                    sweetAlertDialogQuestionNotExist.show();*/
                    app.t("not ffound");
                }
            }

            @Override
            public void onFailure(Call<MechanicWithMsg> call, Throwable t) {
                app.l(t.getLocalizedMessage() + "tttttttttt mech" + t.getMessage());
            }
        });

    }

    private void resumeGetMechanics(int offset, int jobId, int regionId, int sortBy) {

        Map<String, String> map = new HashMap<>();
        map.put("route", "getMechanics");
        map.put("offset", String.valueOf(offset));
        map.put("jobId", String.valueOf(jobId));
        map.put("regionId", String.valueOf(regionId));
        map.put("x", "0");
        map.put("y", "0");
        map.put("sortBy", String.valueOf(sortBy));

        Application.getApi().getMechanicWutMsg(map).enqueue(new Callback<MechanicWithMsg>() {
            @Override
            public void onResponse(Call<MechanicWithMsg> call, Response<MechanicWithMsg> response) {
                if (response.body() != null && response.body().getMechanic().size() == 0) {
                    app.l("MMECRES" + response.body().getMsg());
                    return;
                }
                List<Mechanic> newMechanics = response.body().getMechanic();

                if (newMechanics != null) {
                    tmpMechanics.addAll(newMechanics);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<MechanicWithMsg> call, Throwable t) {
                app.l("MMMEC" + t.getLocalizedMessage());
            }
        });


    }

    private void resumeMechanicListener(int jobId, int regionId, int sortBy) {
        isLoading = false;
        recyclerMechanic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpMechanics.size() - 1 && !isLoading) {
                    isLoading = true;
                    offset++;
                    resumeGetMechanics(offset, jobId, regionId, sortBy);
                }
            }
        });
    }/**/

    private void modifyIds() {
        if (jobQuestion.getText().toString().length() == 0 && selectedJobId == 0) {
            selectedJobId = 0;
        } else if (jobQuestion.getText().toString().length() > 0 && !jobQuestion.getText().toString().equals(getString(R.string.all_jobs)) && selectedJobId == 0) {
            selectedJobId = -1;
        }
        if (regionQuestion.getText().toString().length() == 0 && selectedRegionId == 0) {
            selectedRegionId = 0;
        } else if (regionQuestion.getText().toString().length() > 0 && !regionQuestion.getText().toString().equals(getString(R.string.all_regions)) && selectedRegionId == 0) {
            selectedRegionId = -1;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_job:
                app.hideKeyboard(jobQuestion);
                app.hideKeyboard(regionQuestion);
                jobQuestion.setText("");
                selectedJobId = 0;
                modifyIds();
                spinnerFilter.setEnabled(true);
                spinnerFilter.setClickable(true);
                resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));/**/
                break;
            case R.id.reset_region:
                app.hideKeyboard(jobQuestion);
                app.hideKeyboard(regionQuestion);
                regionQuestion.setText("");
                selectedRegionId = 0;
                modifyIds();
                spinnerFilter.setEnabled(true);
                spinnerFilter.setClickable(true);
                resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));/**/
                break;
            case R.id.submit_filter_parent:
                app.hideKeyboard(jobQuestion);
                app.hideKeyboard(regionQuestion);
                modifyIds();
                resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));/**/
                break;
        }

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            selectedJobId = 1;
            selectedRegionId = 0;
            filterIdInString = "0";

            app.l(selectedJobId + "WW" + selectedRegionId + "WW" + filterIdInString + "WW" + "EEEEEE");
            resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
            getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
        }
    };

    public class GpsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            sweetAlertDialogRequestGps.dismissWithAnimation();
            getLocation();
        }
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(gpsReceiver);
        super.onDestroy();
    }
}
