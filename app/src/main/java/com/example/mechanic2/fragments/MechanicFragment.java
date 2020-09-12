package com.example.mechanic2.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.adapters.JobAutoCompleteAdapter;
import com.example.mechanic2.adapters.MechanicRecyclerAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.adapters.RegionAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.models.Job;
import com.example.mechanic2.models.Mechanic;
import com.example.mechanic2.models.MechanicWithMsg;
import com.example.mechanic2.models.Region;
import com.example.mechanic2.views.MyTextView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private TextView requestForGps;
    private RelativeLayout allowAccessGps;
    private MyTextView denyAccessGps;
    private GpsReceiver gpsReceiver;

    private String detail;
    private boolean fromConfig;
    private SweetAlertDialog gpsSweetAlertDialogChanged;
    private SweetAlertDialog sweetAlertDialogGpsWarning;
    private SweetAlertDialog errorConnectionSweetAlertDialog;
    private SweetAlertDialog gpsSweetAlertDialogFromConfig;
    private SweetAlertDialog sweetAlertDialogQuestionNotExist;
    private boolean dataGutted = false;
    private Region region;
    private Job job;

    public static MechanicFragment newInstance(String detail) {
        Bundle args = new Bundle();
        args.putString("detail", detail);
        MechanicFragment fragment = new MechanicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_mechanic, container, false);
        if (getArguments() != null) {
            detail = getArguments().getString("detail");
        }

        return init(inflate);
    }


    SweetAlertDialog sweetAlertDialogRequestGps;

    SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = inflate.findViewById(R.id.swipe_refresh_layout);

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

                    if (i > 1 || detail != null) {
                        spinnerCheckForGpsPermission(true);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                } else {
                    resetJob.setVisibility(View.VISIBLE);
                    if ((!s.toString().equals("همه تخصص ها") || (job != null && !s.toString().equals(job.getName()))) && selectedJobId == 0) {
                        selectedJobId = -1;
                    }
                }
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
                } else {
                    resetRegion.setVisibility(View.VISIBLE);
                    if ((!s.toString().equals("همه مناطق") || (region != null && !s.toString().equals(region.getName()))) && selectedRegionId == 0) {
                        selectedRegionId = -1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        recyclerMechanic.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        loading.setVisibility(View.VISIBLE);
        submitFilter.setVisibility(View.INVISIBLE);



        String tempDetail = detail;

        if (detail == null) {

            fromConfig = false;
        } else {

            fromConfig = true;


            configureMechanicWithDetail(detail);
        }


        app.validateConnection(getActivity(), errorConnectionSweetAlertDialog, new ConnectionErrorManager() {
            @Override
            public void doAction() {

                try {
                    JSONObject jsonDetail = null;
                    if (tempDetail != null) {
                        jsonDetail = new JSONObject(tempDetail);

                    }
                    int sortBy = 0;
                    if (jsonDetail != null) {

                        sortBy = Integer.parseInt(jsonDetail.getString("sortBy"));

                    }

                    if (sortBy != 2) {

                        getDataWithConnectionValidation();
                    }

                } catch (JSONException e) {


                    e.printStackTrace();
                }

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                app.validateConnection(getActivity(), errorConnectionSweetAlertDialog, new ConnectionErrorManager() {
                    @Override
                    public void doAction() {
                        try {

                            JSONObject jsonDetail = null;
                            if (detail != null)
                                jsonDetail = new JSONObject(detail);
                            int sortBy = 0;
                            if (jsonDetail != null) {
                                sortBy = Integer.parseInt(jsonDetail.getString("sortBy"));
                            }

                            if (sortBy == 2) {
                                return;
                            }

                            getDataWithConnectionValidation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


        return inflate;
    }

    private void spinnerCheckForGpsPermission(boolean requestData) {
        if (Integer.parseInt(filterIdInString) == 2) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {


                int checkGpsPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (checkGpsPermission == PackageManager.PERMISSION_GRANTED) {
                    if (detail != null) {
                        detail = null;
                    }


                    getLocation();
                } else {
                    View gpsView = LayoutInflater.from(getContext()).inflate(R.layout.view_get_gps_permission, null);
                    requestForGps = gpsView.findViewById(R.id.request_for_gps);
                    allowAccessGps = gpsView.findViewById(R.id.allow_access_gps);
                    sweetAlertDialogRequestGps = new SweetAlertDialog(getContext()).hideConfirmButton().setCustomView(gpsView);
                    sweetAlertDialogRequestGps.show();
                    allowAccessGps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sweetAlertDialogRequestGps.dismiss();
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


            if (locationListener != null)
                locationManager.removeUpdates(locationListener);
            if (requestData) {


                dataGutted = true;


                app.validateConnection(getActivity(), errorConnectionSweetAlertDialog, new ConnectionErrorManager() {
                    @Override
                    public void doAction() {

                        resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                        getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                    }
                });

            }
        }
    }

    int offset;
    List<Mechanic> mechanics;
    List<Mechanic> tmpMechanics;
    MechanicRecyclerAdapter adapter;
    boolean isLoading;
    double x, y;

    LocationManager locationManager;

    LocationListener locationListener;
    SweetAlertDialog gpsSweetAlertDialog;
    AlertDialog.Builder ab;
    AlertDialog alertDialog;

    void getLocation() {
        try {


            gpsSweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            gpsSweetAlertDialog.setTitle("لطفا شکیبا باشید.");
            gpsSweetAlertDialog.setContentText("در حال پیدا کردن موقعیت فعلی شما");
            gpsSweetAlertDialog.show();
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    x = location.getLatitude();
                    y = location.getLongitude();


                    app.validateConnection(getActivity(), errorConnectionSweetAlertDialog, new ConnectionErrorManager() {
                        @Override
                        public void doAction() {


                            resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                            getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                        }
                    });
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {



                }

                @Override
                public void onProviderEnabled(String provider) {
                    gpsSweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                    gpsSweetAlertDialog.setTitle("لطفا شکیبا باشید.");
                    gpsSweetAlertDialog.setContentText("در حال پیدا کردن موقعیت فعلی شما");
                    gpsSweetAlertDialog.show();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    gpsSweetAlertDialog.dismissWithAnimation();
                    if (getContext() != null) {
                        sweetAlertDialogGpsWarning = new SweetAlertDialog(MechanicFragment.this.getContext());
                        sweetAlertDialogGpsWarning.hideConfirmButton();
                        sweetAlertDialogGpsWarning.setCancelable(false);
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_enable_gps, null, false);
                        RelativeLayout relativeLayout = view.findViewById(R.id.gps_intent);
                        relativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sweetAlertDialogGpsWarning.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }
                                }, 10);
                            }
                        });
                        sweetAlertDialogGpsWarning.setCustomView(view);
                        sweetAlertDialogGpsWarning.show();
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15, 1000, locationListener);
        } catch (SecurityException e) {

            e.printStackTrace();
        }
    }


    View view;

    private RelativeLayout btnAddQuestion;
    private RelativeLayout btnShowAllQuestions;


    private void getMechanics(int jobId, int regionId, int sortBy) {
        offset = 0;
        mechanics = new ArrayList<>();
        tmpMechanics = new ArrayList<>();
        adapter = new MechanicRecyclerAdapter(mechanics, getContext());
        dataGutted = true;
        detail = null;
        Map<String, String> map = new HashMap<>();
        map.put("route", "getMechanics");
        map.put("offset", String.valueOf(offset));
        map.put("jobId", String.valueOf(jobId));
        map.put("regionId", String.valueOf(regionId));
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        map.put("sortBy", String.valueOf(sortBy));











        view = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null);
        btnAddQuestion = view.findViewById(R.id.btn_contact_us);
        btnAddQuestion.setOnClickListener(MechanicFragment.this);
        RelativeLayout showAllGoods = view.findViewById(R.id.btn_show_all_goods);
        showAllGoods.setVisibility(View.GONE);
        SweetAlertDialog loadingData = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setContentText("در حال دریافت اطلاعات").setTitleText("لطفا شکیبا باشید.");

        loadingData.show();

        Application.getApi().getMechanicWutMsg(map).enqueue(new Callback<MechanicWithMsg>() {
            @Override
            public void onResponse(Call<MechanicWithMsg> call, Response<MechanicWithMsg> response) {



                if (loadingData != null)
                    loadingData.dismissWithAnimation();
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                if (response.body() != null && response.body().getMechanic().size() > 0) {


                    int id = response.body().getMechanic().get(0).getId();
                    if (id == -2 || id == -3 || id == -4) {
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_ok_layout, null);
                        TextView txt = view.findViewById(R.id.txt);

                        if (id == -2) {
                            txt.setText("لطفا روی یکی از تخصص های پیشنهادی کلیک کنید.");
                        } else if (id == -3) {
                            txt.setText(" لطفا روی یکی از مناطق پیشنهادی کلیک کنید.");
                        } else if (id == -4) {
                            txt.setText("لطفا روی یکی از تخصص ها و مناطق پیشنهادی کلیک کنید.");
                        }

                        RelativeLayout btnConfirm = view.findViewById(R.id.btn_confirm);
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setCustomView(view);

                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sweetAlertDialog.dismissWithAnimation();
                                regionQuestion.setText("");
                                jobQuestion.setText("");
                                selectedJobId = 0;
                                selectedRegionId = 0;
                            }
                        });

                        sweetAlertDialog.hideConfirmButton();
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();

                        return;
                    }




                    mechanics = response.body().getMechanic();
                    if (mechanics != null && mechanics.size() != 0) {
                        tmpMechanics.addAll(mechanics);
                    } else {
                        if (mechanics != null) {
                            isLoading = false;
                        }
                    }
                    Intent intent = new Intent("dataCount");
                    intent.putExtra("ref", "mcf");
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);


                    adapter = new MechanicRecyclerAdapter(tmpMechanics, getContext());
                    recyclerMechanic.setAdapter(adapter);
                    if (x != 0 || y != 0) {
                        if (gpsSweetAlertDialog != null) {
                            gpsSweetAlertDialog.dismissWithAnimation();
                        }
                        if (gpsSweetAlertDialogChanged != null)
                            gpsSweetAlertDialogChanged.dismissWithAnimation();

                        if (gpsSweetAlertDialogFromConfig != null)
                            gpsSweetAlertDialogFromConfig.dismissWithAnimation();

                    }

                } else {

                    TextView textView = view.findViewById(R.id.txt);
                    TextView txt_ok = view.findViewById(R.id.txt_ok);
                    LottieAnimationView lottieAnimationView = view.findViewById(R.id.warranty_lt);
                    lottieAnimationView.setAnimation(R.raw.exclamation_mark);

                    textView.setText("مکانیک مورد نظر شما هنوز ثبت نشده است.");

                    txt_ok.setText("نمایش تمام مکانیک ها");

                    sweetAlertDialogQuestionNotExist = null;
                    sweetAlertDialogQuestionNotExist = new SweetAlertDialog(getContext()).hideConfirmButton()
                            .setCustomView(view);
                    sweetAlertDialogQuestionNotExist.setCancelable(false);
                    sweetAlertDialogQuestionNotExist.show();

                }
            }

            @Override
            public void onFailure(Call<MechanicWithMsg> call, Throwable t) {

            }
        });

    }

    private void resumeGetMechanics(int offset, int jobId, int regionId, int sortBy) {

        Map<String, String> map = new HashMap<>();
        map.put("route", "getMechanics");
        map.put("offset", String.valueOf(offset));
        map.put("jobId", String.valueOf(jobId));
        map.put("regionId", String.valueOf(regionId));
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        map.put("sortBy", String.valueOf(sortBy));

        Application.getApi().getMechanicWutMsg(map).enqueue(new Callback<MechanicWithMsg>() {
            @Override
            public void onResponse(Call<MechanicWithMsg> call, Response<MechanicWithMsg> response) {
                if (response.body() != null && response.body().getMechanic().size() == 0) {

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
    }


    private void modifyIds() {
        if (jobQuestion.getText().toString().length() == 0 && selectedJobId == 0) {
            selectedJobId = 0;
        } else if (jobQuestion.getText().toString().length() > 0 && (!jobQuestion.getText().toString().equals(getString(R.string.all_jobs)) && (region != null && !jobQuestion.getText().toString().equals(region.getName()))) && selectedJobId == 0) {
            selectedJobId = -1;
        }
        if (regionQuestion.getText().toString().length() == 0 && selectedRegionId == 0) {
            selectedRegionId = 0;
        } else if (regionQuestion.getText().toString().length() > 0 && (!regionQuestion.getText().toString().equals(getString(R.string.all_regions)) && (region != null && !regionQuestion.toString().equals(region.getName()))) && selectedRegionId == 0) {
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
                getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
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
                getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                break;
            case R.id.submit_filter_parent:
                app.hideKeyboard(jobQuestion);
                app.hideKeyboard(regionQuestion);
                modifyIds();
                resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                break;
            case R.id.btn_contact_us:
                sweetAlertDialogQuestionNotExist.dismissWithAnimation();
                regionQuestion.setText("");
                jobQuestion.setText("");
                spinnerFilter.setEnabled(true);
                spinnerFilter.setClickable(true);
                selectedRegionId = 0;
                selectedJobId = 0;
                filterIdInString = "0";
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                if (spinnerFilter.getSelectedItemPosition() != 0) {
                    spinnerFilter.setSelection(0);
                } else if (spinnerFilter.getSelectedItemPosition() == 0) {
                    resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                    getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                }
                break;
        }

    }

    private void getDataWithConnectionValidation() {
        if (!dataGutted && detail == null)
            app.validateConnection(getActivity(), errorConnectionSweetAlertDialog, new ConnectionErrorManager() {
                @Override
                public void doAction() {

                    resumeMechanicListener(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                    getMechanics(selectedJobId, selectedRegionId, Integer.parseInt(filterIdInString));
                }
            });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            selectedJobId = 1;
            selectedRegionId = 0;
            filterIdInString = "0";


            getDataWithConnectionValidation();
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


    private void configureMechanicWithDetail(String detail) {
        try {


            JSONObject jsonDetail = new JSONObject(detail);
            int sortBy = Integer.parseInt(jsonDetail.getString("sortBy"));
            Gson gson = new Gson();
            job = gson.fromJson(jsonDetail.getString("job"), Job.class);

            JSONObject jsonTitle = new JSONObject(jsonDetail.getString("region"));
            region = new Region(jsonTitle.getString("name"), jsonTitle.getInt("id"));
            jobQuestion.setText(job.getName());
            selectedJobId = job.getId();
            regionQuestion.setText(region.getName());
            selectedRegionId = region.getId();
            spinnerFilter.setSelection(sortBy);
            filterIdInString = String.valueOf(sortBy);


            spinnerCheckForGpsPermission(false);
            if (sortBy == 0) {
                getDataWithConnectionValidation();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
