package com.example.mechanic2.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
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
import com.example.mechanic2.activities.ShowGoodDetailActivity;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.GoodAutoCompleteAdapter;
import com.example.mechanic2.adapters.GooodStoreAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.adapters.StoreRecyclerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnGoodClickListener;
import com.example.mechanic2.interfaces.VoiceOnClickListener;
import com.example.mechanic2.models.Country;
import com.example.mechanic2.models.CountriesAndWarranties;
import com.example.mechanic2.models.Good;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.models.Warranty;
import com.example.mechanic2.views.MyTextView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;

import org.jetbrains.annotations.NotNull;

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
    RelativeLayout btnShowLuxuryGoods;
    RelativeLayout btnShowStokeGoods;
    SweetAlertDialog sweetAlertDialogGoodNotExist;

    private SwipeRefreshLayout swipeRefreshLayout;



    boolean firstRun;
    int i = 0;
    int j = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_store, container, false);
        return init(inflate);
    }


    private View init(View inflate) {

        //      getGooods(selectedCarId,selectedGoodId,Integer.parseInt(warrantyIdInString),0,0);
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
        resetCar.setOnClickListener(this);
        resetGood.setOnClickListener(this);

        submitFilterParent.setOnClickListener(this);
        SweetAlertDialog loadingData = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("شکیبا باشید");
        loadingData.setCancelable(false);
        loadingData.show();
        loadingData.setCancelable(false);
        Map<String, String> map = new HashMap<>();
        map.put("route", "getCountriesAndWarranties");
        Application.getApi().getCountriesAndWarranties(map).enqueue(new Callback<CountriesAndWarranties>() {
            @Override
            public void onResponse(Call<CountriesAndWarranties> call, Response<CountriesAndWarranties> response) {
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

                countrySpinnerAdapter = new MySpinnerAdapter(getContext(), R.layout.item_spinner, countryNames, countryIds, false);
                warrantySpinnerAdapter = new MySpinnerAdapter(getContext(), R.layout.item_spinner, warrantyNames, warrantyIds, false);
                warrantySpinner.setAdapter(warrantySpinnerAdapter);
                countrySpinner.setAdapter(countrySpinnerAdapter);
            }

            @Override
            public void onFailure(Call<CountriesAndWarranties> call, Throwable t) {
                loadingData.dismissWithAnimation();
                app.l(t.getLocalizedMessage() + "WW");
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
                    if (j > 2) {
                        loading.setVisibility(View.VISIBLE);
                        submitFilter.setVisibility(View.INVISIBLE);
                        resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                        getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
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
                    if (i > 2) {
                        loading.setVisibility(View.VISIBLE);
                        submitFilter.setVisibility(View.INVISIBLE);
                        app.l("iiiiii" + i);
                        resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                        getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
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
        carQuestion = inflate.findViewById(R.id.car_question);/*
        carQuestion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    resetAppbar();
                }
            }
        });*/

        stoke.setOnClickListener(this);
        CarAutoCompleteAdapter carAdapter = new CarAutoCompleteAdapter(getActivity(), R.layout.item_show_auto_complete);
        carQuestion.setAdapter(carAdapter);
        GoodAutoCompleteAdapter goodAdapter = new GoodAutoCompleteAdapter(Application.getContext(), R.layout.item_show_auto_complete);
        goodQuestion.setAdapter(goodAdapter);

        carQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCarId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });
        goodQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGoodId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
                if (selectedGoodId == -2 /*luxury good*/) {
                    selectedGoodId = 0;
                    stokeState = 2;
                    stoke.setEnabled(false);
                    stoke.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                } else {
                    stoke.setEnabled(true);
                    stoke.setBackground(getActivity().getDrawable(R.drawable.btn_inactive_stoke));
                    stokeState = 0;
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
                } else resetCar.setVisibility(View.VISIBLE);
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
                if (s.toString().length() == 0) {
                    selectedGoodId = 0;
                    resetGood.setVisibility(View.INVISIBLE);
                } else resetGood.setVisibility(View.VISIBLE);
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
                resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        recyclerStore.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        loading.setVisibility(View.VISIBLE);
        submitFilter.setVisibility(View.INVISIBLE);
        resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
        getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
        return inflate;
    }

    private void modifyIds() {
        if (carQuestion.getText().toString().length() == 0 && selectedCarId == 0) {
            selectedCarId = 0;
        } else if (carQuestion.getText().toString().length() > 0 && !carQuestion.getText().toString().equals(getString(R.string.all_cars)) && selectedCarId == 0) {
            selectedCarId = -1;
        }
        if (goodQuestion.getText().toString().length() == 0 && selectedGoodId == 0) {
            selectedGoodId = 0;
        } else if (goodQuestion.getText().toString().length() > 0 && (!goodQuestion.getText().toString().equals(getString(R.string.all_goods)) && !goodQuestion.getText().toString().equals(getString(R.string.luxury_good))) && selectedGoodId == 0) {
            selectedGoodId = -1;
        }
    }


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

        view = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null);
        TextView textView = view.findViewById(R.id.txt);
        btnShowAllGoods = view.findViewById(R.id.btn_show_all_goods);
        btnShowLuxuryGoods = view.findViewById(R.id.btn_show_luxury_goods);
        btnShowStokeGoods = view.findViewById(R.id.btn_show_stoke_goods);
        String text = textView.getText().toString() + app.getEmojiByUnicode(0x1F614);
        textView.setText(text);
        btnShowAllGoods.setOnClickListener(StoreFragment.this);
        btnShowLuxuryGoods.setOnClickListener(StoreFragment.this);
        btnShowStokeGoods.setOnClickListener(StoreFragment.this);

        Application.getApi().getGooodList(map).enqueue(new Callback<List<Goood>>() {
            @Override
            public void onResponse(Call<List<Goood>> call, Response<List<Goood>> response) {
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                app.l("getGoods******" + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().size() > 0) {
                    app.l(response.body().size() + "@@@");

                    if (response.body().get(0).getId() == -2) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از خودروهای پیشنهادی کلیک کنید").show();
                        return;
                    }
                    if (response.body().get(0).getId() == -3) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از فطعات پیشنهادی کلیک کنید").show();
                        return;
                    }
                    if (response.body().get(0).getId() == -4) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از فطعات پیشنهادی و یکی از خودروهای پیشنهادی کلیک کنید").show();
                        return;
                    }


                    gooods = response.body();
                    if (gooods != null && gooods.size() != 0) {
                        tmpGooods.addAll(gooods);
                        StoreFragment.this.lastId = gooods.get(gooods.size() - 1).getId();
                    } else {
                        if (gooods != null) {
                            app.t("not found11");
                            isLoading = false;
                        /*resumeGooodListener(0, 0, 0, 0, is_stoke_active ? 1 : 0);
                        getGooods(0, 0, 0, 0, is_stoke_active ? 1 : 0);*/

                        }
                    }
                    adapter = new GooodStoreAdapter(tmpGooods, getActivity());
                    recyclerStore.setAdapter(adapter);

                } else {
                    sweetAlertDialogGoodNotExist = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).hideConfirmButton()
                            .setCustomView(view);
                    sweetAlertDialogGoodNotExist.show();
                }
            }

            @Override
            public void onFailure(Call<List<Goood>> call, Throwable t) {
                app.l(t.getLocalizedMessage() + "@");
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
                app.l("resume******" + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().size() == 0) {
                    app.l("f");
                    return;
                }
                List<Goood> newGooods = response.body();
                if (newGooods != null) {

                    app.l(newGooods.get(newGooods.size() - 1).getId() + "******");
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

    private void resetAppbar() {

        is_stoke_active = false;
        stoke.setBackground(getResources().getDrawable(R.drawable.btn_inactive_stoke));
        countrySpinner.setEnabled(true);
        countrySpinner.setClickable(true);
        warrantySpinner.setEnabled(true);
        warrantySpinner.setClickable(true);
        warrantySpinner.setSelection(0);
        countrySpinner.setSelection(0);
        countrySpinnerAdapter.disableAdapter(is_stoke_active);
    }

    private void initAppbar() {
    }


    //VoiceOnClickListener
    @Override
    public void onClick(View v, Good good) {
        app.l(good.getPrice());
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
                app.l(String.valueOf(progress.currentBytes));
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


                app.l("completed");
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

                app.l("selectedCarId:" + selectedCarId);
                app.l("selectedGoodId:" + selectedGoodId);
                app.l("warrantyIdInStringInteger:" + Integer.parseInt(warrantyIdInString));
                app.l("is_stoke_active:" + (getStockValue()));
                app.l("IntegercountryIdInString:" + Integer.parseInt(countryIdInString));
/*
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);*/
                modifyIds();
                is_stoke_active = !is_stoke_active;
                app.l("ci:" + selectedCarId + "gi" + selectedGoodId + "wi" + warrantyIdInString + "cni" + countryIdInString + (getStockValue()));
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
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_inactive_stoke));
                    countrySpinner.setEnabled(true);
                    countrySpinner.setClickable(true);
                    warrantySpinner.setEnabled(true);
                    warrantySpinner.setClickable(true);
                }

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());


                break;
            case R.id.submit_filter_parent:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);
                modifyIds();
                app.l(carQuestion.getText().toString());
                app.l(carQuestion.getHint().toString());
                app.l("selectedCarId:" + selectedCarId);
                app.l("selectedGoodId:" + selectedGoodId);
                app.l("warrantyIdInStringInteger:" + Integer.parseInt(warrantyIdInString));
                app.l("is_stoke_active:" + getStockValue());
                app.l("IntegercountryIdInString:" + Integer.parseInt(countryIdInString));
                resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());/**/
                break;
            case R.id.reset_car:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);
                carQuestion.setText("");
                selectedCarId = 0;
                modifyIds();
                warrantySpinner.setEnabled(true);
                countrySpinner.setEnabled(true);
                countrySpinner.setClickable(true);
                warrantySpinner.setClickable(true);
                app.l("selectedCarId:" + selectedCarId);
                app.l("selectedGoodId:" + selectedGoodId);
                app.l("warrantyIdInStringInteger:" + Integer.parseInt(warrantyIdInString));
                app.l("is_stoke_active:" + (getStockValue()));
                app.l("IntegercountryIdInString:" + Integer.parseInt(countryIdInString));
                countrySpinnerAdapter.disableAdapter(is_stoke_active);
                warrantySpinnerAdapter.disableAdapter(is_stoke_active);
                resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());/**/
                break;
            case R.id.reset_good:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);
                modifyIds();
                goodQuestion.setHint(getString(R.string.ask_good));
                goodQuestion.setText("");
                stoke.setEnabled(true);
                stoke.setBackground(getActivity().getDrawable(R.drawable.btn_inactive_stoke));
                stokeState = 0;
                selectedGoodId = 0;
                warrantySpinner.setEnabled(true);
                countrySpinner.setEnabled(true);
                countrySpinner.setClickable(true);
                warrantySpinner.setClickable(true);
                app.l("selectedCarId:" + selectedCarId);
                app.l("selectedGoodId:" + selectedGoodId);
                app.l("warrantyIdInStringInteger:" + Integer.parseInt(warrantyIdInString));
                app.l("is_stoke_active:" + (getStockValue()));
                app.l("IntegercountryIdInString:" + Integer.parseInt(countryIdInString));
                countrySpinnerAdapter.disableAdapter(is_stoke_active);
                warrantySpinnerAdapter.disableAdapter(is_stoke_active);
                resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());/**/
                break;
            case R.id.btn_show_all_goods:
                sweetAlertDialogGoodNotExist.dismissWithAnimation();
                stokeState = 0;
                stoke.setBackground(getResources().getDrawable(R.drawable.btn_inactive_stoke));
                goodQuestion.setText("");
                carQuestion.setText("");
                is_stoke_active = false;
                stoke.setEnabled(true);
                selectedGoodId = 0;
                selectedCarId = 0;
                warrantySpinner.setEnabled(true);
                countrySpinner.setEnabled(true);
                countrySpinner.setClickable(true);
                warrantySpinner.setClickable(true);
                warrantyIdInString = "0";
                countryIdInString = "0";
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                app.l(warrantySpinner.getSelectedItemPosition());
                app.l(countrySpinner.getSelectedItemPosition());
                if (warrantySpinner.getSelectedItemPosition() != 0 && countrySpinner.getSelectedItemPosition() == 0) {
                    warrantySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() != 0) {
                    warrantySpinner.setSelection(0);
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() == 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                    getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());/**/
                }
                break;
            case R.id.btn_show_luxury_goods:
                stokeState = 2;
                stoke.setEnabled(false);
                stoke.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                goodQuestion.setText(getText(R.string.luxury_good));
                carQuestion.setText("");
                stokeState = 2;
                is_stoke_active = false;
                selectedGoodId = 0;
                warrantySpinner.setEnabled(true);
                countrySpinner.setEnabled(true);
                countrySpinner.setClickable(true);
                warrantySpinner.setClickable(true);
                selectedCarId = 0;
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                sweetAlertDialogGoodNotExist.dismissWithAnimation();
                app.l(warrantySpinner.getSelectedItemPosition());
                app.l(countrySpinner.getSelectedItemPosition());
                if (warrantySpinner.getSelectedItemPosition() != 0 && countrySpinner.getSelectedItemPosition() == 0) {
                    warrantySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() != 0) {
                    warrantySpinner.setSelection(0);
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() == 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                    getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());/**/
                }
                break;
            case R.id.btn_show_stoke_goods:
                stoke.setEnabled(true);
                stokeState = 1;
                is_stoke_active = true;
                countrySpinnerAdapter.disableAdapter(true);
                warrantySpinnerAdapter.disableAdapter(true);
                stoke.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                countrySpinner.setEnabled(false);
                countrySpinner.setClickable(false);
                warrantySpinner.setClickable(false);
                warrantySpinner.setEnabled(false);
                goodQuestion.setText("");
                carQuestion.setText("");
                sweetAlertDialogGoodNotExist.dismissWithAnimation();
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                selectedGoodId = 0;
                selectedCarId = 0;
                app.l("selectedCarId:" + selectedCarId);
                app.l("selectedGoodId:" + selectedGoodId);
                app.l("warrantyIdInStringInteger:" + Integer.parseInt(warrantyIdInString));
                app.l("is_stoke_active:" + (getStockValue()));
                app.l("IntegercountryIdInString:" + Integer.parseInt(countryIdInString));
                app.l(warrantySpinner.getSelectedItemPosition());
                app.l(countrySpinner.getSelectedItemPosition());
                if (warrantySpinner.getSelectedItemPosition() != 0 && countrySpinner.getSelectedItemPosition() == 0) {
                    warrantySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() != 0 && warrantySpinner.getSelectedItemPosition() != 0) {
                    warrantySpinner.setSelection(0);
                    countrySpinner.setSelection(0);
                } else if (countrySpinner.getSelectedItemPosition() == 0 && warrantySpinner.getSelectedItemPosition() == 0) {
                    resumeGooodListener(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());
                    getGooods(selectedCarId, selectedGoodId, Integer.parseInt(warrantyIdInString), Integer.parseInt(countryIdInString), getStockValue());/**/
                }


        }
    }

    private int getStockValue() {
        if (is_stoke_active) return 1;
        else return stokeState;
    }


}
