package com.example.mechanic2.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.downloader.request.DownloadRequest;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.SplashActivity;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.GoodAutoCompleteAdapter;
import com.example.mechanic2.adapters.GooodStoreAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.interfaces.VoiceOnClickListener;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.CountriesAndWarranties;
import com.example.mechanic2.models.Country;
import com.example.mechanic2.models.Good;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.models.Warranty;
import com.example.mechanic2.views.MyTextView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;

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

public class StoreFragment extends Fragment implements VoiceOnClickListener, View.OnClickListener {

    private static final int REQUEST_CODE_FOR_SEARCH_GOODS = 101;
    public static final String ALL = "همه";
    public static final String CANCEL_SEARCH = "بی خیال";
    public static final String SEARCH = "جست و جو کن";
    private RecyclerView recyclerStore;
    private int lastId = 0;
    private List<Good> goods = new ArrayList<>();
    private List<Good> tmpGoods = new ArrayList<>();
    private List<Goood> gooods = new ArrayList<>();
    private List<Goood> tmpGooods = new ArrayList<>();
    private GooodStoreAdapter adapter;
    private boolean isLoading;
    private RelativeLayout submitFilterParent;


    private CoordinatorLayout parent;
    private AppBarLayout appbar;
    private AutoCompleteTextView carQuestion;
    private AutoCompleteTextView goodQuestion;
    private AppCompatTextView stoke;
    private AppCompatSpinner warrantySpinner;
    private AppCompatSpinner countrySpinner;
    private int selectedCarId;
    private int selectedGoodId;
    String countryIdInString = "0";
    String warrantyIdInString = "0";
    private int selectedWarrantyId;
    ArrayAdapter<String> spinnerAdapter;
    MySpinnerAdapter countrySpinnerAdapter;
    MySpinnerAdapter warrantySpinnerAdapter;
    private TextView submitFilter;
    private SpinKitView loading;
    private ImageView resetCar;
    private ImageView resetGood;
    private int stokeState;
    View view;

    RelativeLayout btnShowAllGoods;
    RelativeLayout contactUs;
    SweetAlertDialog sweetAlertDialogGoodNotExist;

    private SwipeRefreshLayout swipeRefreshLayout;

    String detail;


    int i = 0;
    int j = 0;
    private Car car;
    private Good good;

    public static StoreFragment newInstance(String detail) {
        Bundle args = new Bundle();
        args.putString("detail", detail);
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View inflate;

    boolean fromConfig = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_store, container, false);
        if (getArguments() != null) {

            detail = getArguments().getString("detail");
        }

        return init(inflate);
    }


    private View init(View inflate) {


        goodQuestion = inflate.findViewById(R.id.good_question);
        stoke = inflate.findViewById(R.id.stoke);
        warrantySpinner = inflate.findViewById(R.id.warranty_spinner);
        countrySpinner = inflate.findViewById(R.id.country_spinner);
        submitFilterParent = inflate.findViewById(R.id.submit_filter_parent);
        submitFilter = inflate.findViewById(R.id.submit_filter);
        loading = inflate.findViewById(R.id.loading);
        resetCar = inflate.findViewById(R.id.reset_car);
        resetGood = inflate.findViewById(R.id.reset_good);

        swipeRefreshLayout = inflate.findViewById(R.id.swipe_refresh_layout);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, new IntentFilter("mpd3"));
        resetCar.setOnClickListener(this);
        resetGood.setOnClickListener(this);

        submitFilterParent.setOnClickListener(this);

        SweetAlertDialog loadingData = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setContentText("در حال دریافت اطلاعات").setTitleText("لطفا شکیبا باشید.");
        loadingData.setCancelable(false);
        loadingData.show();
        loadingData.setCancelable(false);
        Map<String, String> map = new HashMap<>();
        map.put("route", "getCountriesAndWarranties");
        warrantySpinner.setVisibility(View.GONE);
        countrySpinner.setVisibility(View.GONE);
        stoke.setVisibility(View.GONE);
        app.validateConnection(getActivity(), null, new ConnectionErrorManager() {
            @Override
            public void doAction() {

                Application.getApi().getCountriesAndWarranties(map).enqueue(new Callback<CountriesAndWarranties>() {
                    @Override
                    public void onResponse(Call<CountriesAndWarranties> call, Response<CountriesAndWarranties> response) {
                        if (loadingData != null)
                            loadingData.dismissWithAnimation();
                        List<Country> countries = response.body().getCountries();
                        List<Warranty> warranties = response.body().getWarranties();
                        List<String> countryNames = new ArrayList<>();
                        List<Integer> countryIds = new ArrayList<>();
                        List<String> warrantyNames = new ArrayList<>();
                        List<Integer> warrantyIds = new ArrayList<>();
                        for (Country countries1 : countries) {
                            countryNames.add(countries1.getName());
                            countryIds.add(countries1.getId());
                        }
                        for (Warranty warranty1 : warranties) {
                            warrantyNames.add(warranty1.getName());
                            warrantyIds.add(warranty1.getId());
                        }
                        if (getContext() != null) {
                            countrySpinnerAdapter = new MySpinnerAdapter(getContext(), R.layout.item_spinner, countryNames, countryIds, false);
                            warrantySpinnerAdapter = new MySpinnerAdapter(getContext(), R.layout.item_spinner, warrantyNames, warrantyIds, false);
                            warrantySpinner.setAdapter(warrantySpinnerAdapter);
                            countrySpinner.setAdapter(countrySpinnerAdapter);
                        }
                        stoke.setVisibility(View.VISIBLE);
                        countrySpinner.setVisibility(View.VISIBLE);
                        warrantySpinner.setVisibility(View.VISIBLE);
                        if (detail == null) {
                            fromConfig = false;
                        } else {
                            fromConfig = true;
                            configureStoreWithDetail(detail);
                        }


                       /* resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue(stokeState));
                        getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue(stokeState));*/
                    }

                    @Override
                    public void onFailure(Call<CountriesAndWarranties> call, Throwable t) {
                        loadingData.dismissWithAnimation();

                    }
                });
            }
        });
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                j++;
                if (view != null) {
                    modifyIds();

                    View view1 = parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent()));
                    TextView myTextView = view1.findViewById(R.id.id_spinner);
                    countryIdInString = myTextView == null ? "0" : myTextView.getText().toString();
                    int loaderLimiter = 0;
                    if (j > loaderLimiter) {

                        loading.setVisibility(View.VISIBLE);
                        submitFilter.setVisibility(View.INVISIBLE);
                        app.validateConnection(getActivity(), null, new ConnectionErrorManager() {
                            @Override
                            public void doAction() {
                                getGoodsPackage();
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        warrantySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i++;

                if (view != null) {
                    modifyIds();

                    View view1 = parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent()));
                    TextView myTextView = view1.findViewById(R.id.id_spinner);
                    warrantyIdInString = myTextView == null ? "0" : myTextView.getText().toString();
                    int loaderLimiter = fromConfig ? 1 : 1;
                    if (i > loaderLimiter) {
                        loading.setVisibility(View.VISIBLE);
                        submitFilter.setVisibility(View.INVISIBLE);
                        app.validateConnection(getActivity(), null, new ConnectionErrorManager() {
                            @Override
                            public void doAction() {

                                getGoodsPackage();
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerStore = inflate.findViewById(R.id.recyclerStore);
        parent = inflate.findViewById(R.id.parent);
        appbar = inflate.findViewById(R.id.appbar);
        carQuestion = inflate.findViewById(R.id.car_question);

        stoke.setOnClickListener(this);
        CarAutoCompleteAdapter carAdapter = new CarAutoCompleteAdapter(getActivity(), R.layout.item_show_auto_complete, true);
        carQuestion.setAdapter(carAdapter);
        GoodAutoCompleteAdapter goodAdapter = new GoodAutoCompleteAdapter(Application.getContext(), R.layout.item_show_auto_complete);
        goodQuestion.setAdapter(goodAdapter);


        carQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCarId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
                app.hideKeyboard(carQuestion);
                carQuestion.clearFocus();
            }
        });
        goodQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGoodId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
                goodQuestion.clearFocus();

                if (stokeState == 1) {
                    is_stoke_active = true;
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                    countrySpinner.setEnabled(false);
                    countrySpinner.setClickable(false);
                    warrantySpinner.setEnabled(false);
                    warrantySpinner.setClickable(false);


                } else if (stokeState == 0) {
                    is_stoke_active = false;
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_white));
                    countrySpinner.setEnabled(true);
                    countrySpinner.setClickable(true);
                    warrantySpinner.setEnabled(true);
                    warrantySpinner.setClickable(true);

                }

                app.hideKeyboard(goodQuestion);
                if (selectedGoodId == -2) {
                    selectedGoodId = 0;
                    stokeState = 2;
                    stoke.setEnabled(false);
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_white));
                    stoke.setTextColor(getActivity().getResources().getColor(R.color.grey_40));
                    is_stoke_active = false;
                    warrantySpinner.setEnabled(true);
                    countrySpinner.setEnabled(true);
                    countrySpinner.setClickable(true);
                    warrantySpinner.setClickable(true);
                    if (getContext() != null) {
                        countrySpinnerAdapter.disableAdapter(is_stoke_active);
                        warrantySpinnerAdapter.disableAdapter(is_stoke_active);
                    }
                } else {
                    stoke.setEnabled(true);

                }
            }
        });

        recyclerStore.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return super.canScrollVertically();
            }
        });

        carQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    selectedCarId = 0;
                    resetCar.setVisibility(View.INVISIBLE);
                } else {
                    resetCar.setVisibility(View.VISIBLE);
                    if ((!carQuestion.getText().toString().equals("همه ماشین ها") || (car != null && !carQuestion.getText().toString().equals(car.getName()))) && selectedCarId == 0) {

                        selectedCarId = -1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        goodQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("کالای لوکس")) app.enableDisableView(stoke, false);
                else app.enableDisableView(stoke, true);
                if (s.toString().length() == 0) {
                    selectedGoodId = 0;
                    resetGood.setVisibility(View.INVISIBLE);
                } else {
                    resetGood.setVisibility(View.VISIBLE);
                    if ((!goodQuestion.getText().toString().equals("همه قطعه ها") || (good != null && !goodQuestion.getText().toString().equals(good.getName()))) && selectedGoodId == 0) {
                        selectedGoodId = -1;

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                app.validateConnection(getActivity(), null, new ConnectionErrorManager() {
                    @Override
                    public void doAction() {
                        getGoodsPackage();
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerStore.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        loading.setVisibility(View.VISIBLE);
        submitFilter.setVisibility(View.INVISIBLE);
        return inflate;
    }

    private void modifyIds() {
        if (carQuestion.getText().toString().length() == 0 && selectedCarId == 0) {
            selectedCarId = 0;
        } else if (carQuestion.getText().toString().length() > 0 && (!carQuestion.getText().toString().equals(getString(R.string.all_cars)) && (car != null && !carQuestion.getText().toString().equals(car.getName()))) && selectedCarId == 0) {
            selectedCarId = -1;

        }
        if (goodQuestion.getText().toString().length() == 0 && selectedGoodId == 0) {
            selectedGoodId = 0;
        } else if (
                goodQuestion.getText().toString().length() > 0 &&
                        (!goodQuestion.getText().toString().equals(getString(R.string.all_goods)) && !goodQuestion.getText().toString().equals(getString(R.string.luxury_good)) && (good != null && !goodQuestion.getText().toString().equals(good.getName())))
                        && selectedGoodId == 0
        ) {


            selectedGoodId = -1;


        }
    }

    private MyTextView txt;
    private RelativeLayout btnConfirm;
    private static final String TAG = "StoreFragment";

    private void getGooods(int carId, int goodId, int warrantyId, int countryId, int isStock) {
        lastId = 0;
        gooods = new ArrayList<>();
        tmpGooods = new ArrayList<>();
        adapter = new GooodStoreAdapter(gooods, getActivity());









        Map<String, String> map = new HashMap<>();
        map.put("route", "getStore3");
        map.put("lastId", String.valueOf(lastId));
        map.put("carId", String.valueOf(carId));
        map.put("goodId", String.valueOf(goodId));
        map.put("warrantyId", String.valueOf(warrantyId));
        map.put("countryId", String.valueOf(countryId));
        map.put("isStock", String.valueOf(isStock));

        if (StoreFragment.this.getView() != null)
            app.enableDisableView(StoreFragment.this.getView(), false);
        Application.getApi().getGooodList(map).enqueue(new Callback<List<Goood>>() {
            @Override
            public void onResponse(Call<List<Goood>> call, Response<List<Goood>> response) {
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);

                if (StoreFragment.this.getView() != null)
                    app.enableDisableView(StoreFragment.this.getView(), true);

                app.enableDisableView(countrySpinner, !is_stoke_active);
                app.enableDisableView(warrantySpinner, !is_stoke_active);

                if (response.body() != null && response.body().size() > 0) {

                    int id = response.body().get(0).getId();
                    if (id == -2 || id == -3 || id == -4) {
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_ok_layout, null);
                        txt = view.findViewById(R.id.txt);

                        if (id == -2) {
                            txt.setText("لطفا روی یکی از خودروهای پیشنهادی کلیک کنید.");
                        } else if (id == -3) {
                            txt.setText(" لطفا روی یکی از قطعات پیشنهادی کلیک کنید.");
                        } else if (id == -4) {
                            txt.setText("لطفا روی یکی از قطعات و خودروهای پیشنهادی کلیک کنید.");
                        }

                        btnConfirm = view.findViewById(R.id.btn_confirm);
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setCustomView(view);

                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sweetAlertDialog.dismissWithAnimation();
                                carQuestion.setText("");
                                goodQuestion.setText("");
                                selectedCarId = 0;
                                selectedGoodId = 0;
                            }
                        });

                        sweetAlertDialog.hideConfirmButton();
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();

                        return;
                    }


                    gooods = response.body();
                    if (gooods != null && gooods.size() != 0) {
                        tmpGooods.addAll(gooods);
                        StoreFragment.this.lastId = gooods.get(gooods.size() - 1).getId();
                    } else {
                        if (gooods != null) {
                            isLoading = false;
                        }
                    }
                    Intent intent = new Intent("dataCount");
                    intent.putExtra("ref", "stf");
                    LocalBroadcastManager.getInstance(StoreFragment.this.getContext()).sendBroadcast(intent);
                    adapter = new GooodStoreAdapter(tmpGooods, getActivity());
                    recyclerStore.setAdapter(adapter);

                } else {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, ((ViewGroup) inflate), false);
                    TextView textView = view.findViewById(R.id.txt);
                    btnShowAllGoods = view.findViewById(R.id.btn_show_all_goods);
                    contactUs = view.findViewById(R.id.btn_contact_us);
                    btnShowAllGoods.setOnClickListener(StoreFragment.this);
                    contactUs.setOnClickListener(StoreFragment.this);
                    sweetAlertDialogGoodNotExist = new SweetAlertDialog(getContext()).hideConfirmButton()
                            .setCustomView(view);
                    sweetAlertDialogGoodNotExist.setCancelable(false);
                    sweetAlertDialogGoodNotExist.show();

                }
            }

            @Override
            public void onFailure(Call<List<Goood>> call, Throwable t) {
            }
        });

    }

    private void resumeGetGooods(int lastIdN, int carId, int goodId, int warrantyId, int countryId, int isStock) {

        Map<String, String> map = new HashMap<>();
        map.put("route", "getStore3");
        map.put("lastId", String.valueOf(lastIdN));
        map.put("carId", String.valueOf(carId));
        map.put("goodId", String.valueOf(goodId));
        map.put("warrantyId", String.valueOf(warrantyId));
        map.put("countryId", String.valueOf(countryId));
        map.put("isStock", String.valueOf(isStock));

        Application.getApi().getGooodList(map).enqueue(new Callback<List<Goood>>() {
            @Override
            public void onResponse(Call<List<Goood>> call, Response<List<Goood>> response) {

                if (response.body() != null && response.body().size() == 0) {

                    return;
                }
                List<Goood> newGooods = response.body();
                if (newGooods != null) {


                    lastId = newGooods.get(newGooods.size() - 1).getId();
                }
                if (newGooods != null) {
                    tmpGooods.addAll(newGooods);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;

            }

            @Override
            public void onFailure(Call<List<Goood>> call, Throwable t) {


            }
        });


    }

    private void resumeGooodListener(int carId, int goodId, int warrantyId, int countryId, int isStock) {
        isLoading = false;
        recyclerStore.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpGooods.size() - 1 && !isLoading) {
                    isLoading = true;
                    resumeGetGooods(lastId, carId, goodId, warrantyId, countryId, isStock);
                }
            }
        });
    }


    @Override
    public void onClick(View v, Good good) {

        ProgressCircula progressCirculaSound;
        ImageView startDownload;
        ImageView ivPlayPause;
        TextView percentDone;
        LottieAnimationView ltPlayPause;

        progressCirculaSound = v.findViewById(R.id.progressCirculaSound);
        startDownload = v.findViewById(R.id.startDownload);
        percentDone = v.findViewById(R.id.percentDone);
        ivPlayPause = v.findViewById(R.id.ivPlayPause);
        ltPlayPause = v.findViewById(R.id.ltPlayPause);
        ltPlayPause.setAnimation(R.raw.play_to_pause);
        ltPlayPause.pauseAnimation();
        startDownload.setAlpha(0f);
        String url = good.getVoice();
        String path = getActivity().getExternalFilesDir("voice/mp3").getAbsolutePath();

        int downloadId = SharedPrefUtils.getIntData("soundDownloadId**" + good.getId());
        if (Status.RUNNING == PRDownloader.getStatus(downloadId)) {
            PRDownloader.pause(downloadId);
            progressCirculaSound.stopRotation();
            return;
        }
        if (Status.PAUSED == PRDownloader.getStatus(downloadId)) {
            PRDownloader.resume(downloadId);
            progressCirculaSound.startRotation();
            return;
        }

        DownloadRequest downloadRequest = PRDownloader.download(url, path, url.substring(url.lastIndexOf("/"))).build();
        downloadRequest.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int value = (int) (100 * progress.currentBytes / progress.totalBytes);
                progressCirculaSound.setProgress(value);
                percentDone.setText(String.valueOf(value) + "%");

            }
        }).setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {
                progressCirculaSound.setVisibility(View.VISIBLE);
            }
        });
        downloadId = downloadRequest.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("soundDownloadId**" + good.getId()).apply();
                progressCirculaSound.setVisibility(View.GONE);
                startDownload.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                ivPlayPause.setVisibility(View.VISIBLE);
                ltPlayPause.setVisibility(View.VISIBLE);


            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("soundDownloadId**" + good.getId(), downloadId);


    }


    private boolean is_stoke_active = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stoke:
                if (goodQuestion.getText().toString().equals("کالای لوکس")) return;
                modifyIds();
                is_stoke_active = !is_stoke_active;
                countrySpinnerAdapter.disableAdapter(is_stoke_active);
                warrantySpinnerAdapter.disableAdapter(is_stoke_active);
                if (is_stoke_active) {

                    stokeState = 1;
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                    countrySpinner.setEnabled(false);
                    countrySpinner.setClickable(false);
                    warrantySpinner.setEnabled(false);
                    warrantySpinner.setClickable(false);
                } else {
                    stokeState = 0;
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_white));
                    countrySpinner.setEnabled(true);
                    countrySpinner.setClickable(true);
                    warrantySpinner.setEnabled(true);
                    warrantySpinner.setClickable(true);
                }

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                getGoodsPackage();


                break;
            case R.id.submit_filter_parent:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                modifyIds();
                getGoodsPackage();
                break;
            case R.id.btn_contact_us:
                if (sweetAlertDialogGoodNotExist != null)
                    sweetAlertDialogGoodNotExist.dismissWithAnimation();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + SplashActivity.etcetera.get(3).getMessage()));
                startActivity(intent);
                break;
            case R.id.reset_car:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);
                carQuestion.setText("");
                carQuestion.setHint("ماشینت چیه؟");
                selectedCarId = 0;
                modifyIds();
                if (!is_stoke_active) {
                    warrantySpinner.setEnabled(true);
                    countrySpinner.setEnabled(true);
                    countrySpinner.setClickable(true);
                    warrantySpinner.setClickable(true);
                }
                countrySpinnerAdapter.disableAdapter(is_stoke_active);
                warrantySpinnerAdapter.disableAdapter(is_stoke_active);

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);

                getGoodsPackage();
                break;
            case R.id.reset_good:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);
                modifyIds();
                goodQuestion.setHint(getString(R.string.ask_good));
                goodQuestion.setText("");
                if (stokeState == 2) {
                    stokeState = 0;
                    stoke.setEnabled(true);
                    stoke.setTextColor(Color.WHITE);
                    stoke.setBackground(getActivity().getDrawable(R.drawable.btn_white));

                } else


                    selectedGoodId = 0;
                if (!is_stoke_active) {
                    warrantySpinner.setEnabled(true);
                    countrySpinner.setEnabled(true);
                    countrySpinner.setClickable(true);
                    warrantySpinner.setClickable(true);
                    countrySpinnerAdapter.disableAdapter(is_stoke_active);
                    warrantySpinnerAdapter.disableAdapter(is_stoke_active);
                }

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                getGoodsPackage();
                break;
            case R.id.btn_show_all_goods:
                sweetAlertDialogGoodNotExist.dismissWithAnimation();
                stokeState = 0;
                stoke.setBackground(getResources().getDrawable(R.drawable.btn_white));
                goodQuestion.setText("");
                carQuestion.setText("");
                is_stoke_active = false;
                stoke.setEnabled(true);
                selectedGoodId = 0;
                selectedCarId = 0;
                warrantySpinner.setEnabled(true);
                countrySpinner.setClickable(true);
                warrantySpinner.setClickable(true);
                countrySpinner.setEnabled(true);
                if (getContext() != null) {
                    warrantySpinnerAdapter.disableAdapter(is_stoke_active);
                    countrySpinnerAdapter.disableAdapter(is_stoke_active);
                }
                warrantyIdInString = "0";
                countryIdInString = "0";
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);


                if (warrantySpinner.getSelectedItemPosition() != 0 && countrySpinner.getSelectedItemPosition() == 0) {
                    warrantySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() != 0) {
                    warrantySpinner.setSelection(0);
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() == 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    getGoodsPackage();
                }
                break;


        }
    }

    private void getGoodsPackage() {
        app.validateConnection(getActivity(), null, new ConnectionErrorManager() {
            @Override
            public void doAction() {

                resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue(stokeState));
                getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue(stokeState));
            }
        });
    }


    private int getStockValue(int stockState) {
        return stockState;
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String detail = intent.getStringExtra("detail");
            configureStoreWithDetail(detail);


        }
    };

    private void configureStoreWithDetail(String detail) {
        try {
            JSONObject jsonDetail = new JSONObject(detail);
            Gson gson = new Gson();
            car = gson.fromJson(jsonDetail.getString("car"), Car.class);
            int isStockActive = jsonDetail.getInt("isStockActive");


            if (isStockActive == 1) {
                stokeState = 1;
                is_stoke_active = true;
                stoke.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                countrySpinner.setEnabled(false);
                countrySpinner.setClickable(false);
                warrantySpinner.setEnabled(false);
                warrantySpinner.setClickable(false);


            } else if (isStockActive == 0) {
                stokeState = 0;
                is_stoke_active = false;
                stoke.setBackground(getResources().getDrawable(R.drawable.btn_white));
                countrySpinner.setEnabled(true);
                countrySpinner.setClickable(true);
                warrantySpinner.setEnabled(true);
                warrantySpinner.setClickable(true);
            } else if (isStockActive == 2) {
                stokeState = 2;
                is_stoke_active = false;
                stoke.setBackground(getResources().getDrawable(R.drawable.btn_white));
                countrySpinner.setEnabled(true);
                countrySpinner.setClickable(true);
                warrantySpinner.setEnabled(true);
                warrantySpinner.setClickable(true);
            }
            if (getContext() != null) {
                countrySpinnerAdapter.disableAdapter(is_stoke_active);
                warrantySpinnerAdapter.disableAdapter(is_stoke_active);
            }
            JSONObject jsonGood = new JSONObject(jsonDetail.getString("goood"));
            good = new Good(jsonGood.getString("name"), jsonGood.getInt("id"));
            Warranty warranty = gson.fromJson(jsonDetail.getString("warranty"), Warranty.class);
            Country country = gson.fromJson(jsonDetail.getString("country"), Country.class);

            carQuestion.setText(car.getName());
            if (isStockActive != 2) {
                goodQuestion.setText(good.getName());
                selectedGoodId = good.getId();
            } else {
                goodQuestion.setText("کالای لوکس");
                selectedGoodId = 0;
                stoke.setEnabled(false);
                stoke.setTextColor(getActivity().getResources().getColor(R.color.grey_40));
            }
            selectedCarId = car.getId();
            warrantyIdInString = String.valueOf(warranty.getId());
            countryIdInString = String.valueOf(country.getId());
            if (getContext() != null) {
                warrantySpinner.setSelection(warrantySpinnerAdapter.getPosition(warranty.getName()));
                countrySpinner.setSelection(countrySpinnerAdapter.getPosition(country.getName()));
            }

            if (warrantyIdInString.equals("0") && countryIdInString.equals("0")) {

                getGoodsPackage();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
