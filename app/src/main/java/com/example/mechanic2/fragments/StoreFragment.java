package com.example.mechanic2.fragments;

import android.os.Bundle;
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
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.GoodAutoCompleteAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.adapters.StoreRecyclerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.VoiceOnClickListener;
import com.example.mechanic2.models.Country;
import com.example.mechanic2.models.CountriesAndWarranties;
import com.example.mechanic2.models.Good;
import com.example.mechanic2.models.Warranty;
import com.example.mechanic2.views.MyTextView;
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
    private StoreRecyclerAdapter adapter;
    private boolean isLoading;


    private CoordinatorLayout parent;
    private AppBarLayout appbar;
    private TextView submitFilter;
    private AutoCompleteTextView carQuestion;
    private AutoCompleteTextView goodQuestion;
    private AppCompatTextView stoke;
    private AppCompatSpinner warrantySpinner;
    private AppCompatSpinner countrySpinner;
    private int selectedCarId;
    String countryIdInString;
    String warrantyIdInString;
    private int selectedWarrantyId;
    ArrayAdapter<String> spinnerAdapter;
    MySpinnerAdapter countrySpinnerAdapter;
    MySpinnerAdapter warrantySpinnerAdapter;

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
        goodQuestion = inflate.findViewById(R.id.good_question);
        stoke = inflate.findViewById(R.id.stoke);

        warrantySpinner = inflate.findViewById(R.id.warranty_spinner);
        countrySpinner = inflate.findViewById(R.id.country_spinner);

        SweetAlertDialog loadingData = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("شکیبا باشید");
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
                app.l(t.getLocalizedMessage() + "WW");
            }
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    View view1 = parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent()));
                    TextView myTextView = view1.findViewById(R.id.id_spinner);
                    countryIdInString = myTextView == null ? "" : myTextView.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        warrantySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    View view1 = parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent()));
                    TextView myTextView = view1.findViewById(R.id.id_spinner);
                    warrantyIdInString = myTextView == null ? "" : myTextView.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerStore = inflate.findViewById(R.id.recyclerStore);
        parent = inflate.findViewById(R.id.parent);
        appbar = inflate.findViewById(R.id.appbar);
        submitFilter = inflate.findViewById(R.id.submit_filter);
        carQuestion = inflate.findViewById(R.id.car_question);
        carQuestion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    resetAppbar();
                }
            }
        });


        initAppbar();

        CarAutoCompleteAdapter carAdapter = new CarAutoCompleteAdapter(getActivity(), R.layout.item_show_auto_complete);
        carQuestion.setAdapter(carAdapter);
        GoodAutoCompleteAdapter goodAdapter = new GoodAutoCompleteAdapter(Application.getContext(), R.layout.item);
        goodQuestion.setAdapter(goodAdapter);

        carQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCarId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });

        recyclerStore.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return super.canScrollVertically();
            }
        });


        recyclerStore.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
/*        recyclerStore.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpGoods.size() - 1 && !isLoading) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, app.adminBaseUrl + lastId, response -> {
                        app.l("**" + response + "**");
                        Gson gson = new Gson();
                        AdminMedia[] adminMediasModels = gson.fromJson(response, AdminMedia[].class);
                        if (adminMediasModels.length == 0) {
                            app.l("f");
                            return;
                        }
                        List<AdminMedia> newModels = Arrays.asList(adminMediasModels);
                        lastId = newModels.get(newModels.size() - 1).getId();

                        tmpModels.addAll(newModels);
                        adapter.notifyDataSetChanged();
                    }, error -> {

                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            60000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    requestQueue.add(stringRequest);
                    isLoading = true;
                   resumeGetData(lastId, "getStore2", "null", "null", "null");
                }

            }
        });  */
        resumeDataListener("getStore2", "null", "null", "null");
        requestGoods(lastId, "getStore2", "null", "null", "null");
        return inflate;
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
        stoke.setOnClickListener(this);
    }

    private void resumeDataListener(String root, String carName, String goodName, String search) {
        recyclerStore.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpGoods.size() - 1 && !isLoading) {
                    isLoading = true;
                    resumeGetData(lastId, root, carName, goodName, search);
                }

            }
        });
    }

    private void resumeGetData(int lastIdN, String root, String carName, String goodName, String search) {

        Map<String, String> data = new HashMap<>();
        data.put("route", root);
        data.put("lastId", String.valueOf(lastIdN));
        data.put("carName", carName);
        data.put("goodName", goodName);
        data.put("search", search);

        Application.getApi().getGoodList(data).enqueue(new Callback<List<Good>>() {

            @Override
            public void onResponse(@NotNull Call<List<Good>> call, @NotNull Response<List<Good>> response) {
                app.l(new Gson().toJson(response.body()));
                if (response.body() != null && response.body().size() == 0) {
                    app.l("f");
                    return;
                }
                List<Good> newGoods = response.body();
                if (newGoods != null) {

                    app.l(newGoods.get(newGoods.size() - 1).getId() + "******");
                    lastId = newGoods.get(newGoods.size() - 1).getId();
                }
                if (newGoods != null) {
                    tmpGoods.addAll(newGoods);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;

            }

            @Override
            public void onFailure(Call<List<Good>> call, Throwable t) {
                app.l(t.getLocalizedMessage());
            }
        });
    }

    private void requestGoods(int lastId, String root, String carName, String goodName, String search) {
        lastId = 0;
        goods = new ArrayList<>();
        tmpGoods = new ArrayList<>();
        adapter = new StoreRecyclerAdapter(getActivity(), getContext(), tmpGoods, StoreFragment.this);
        Map<String, String> data = new HashMap<>();
        data.put("route", root);
        data.put("lastId", String.valueOf(lastId));
        data.put("carName", carName);
        data.put("goodName", goodName);
        data.put("search", search);
        Application.getApi().getGoodList(data).enqueue(new Callback<List<Good>>() {
            @Override
            public void onResponse(Call<List<Good>> call, retrofit2.Response<List<Good>> response) {
                goods = response.body();
                if (response.body() != null) {
                    app.l(response.body().toString());
                }
                app.l(new Gson().toJson(response.body()));
                if (goods != null) {
                    tmpGoods.addAll(goods);
                    app.l(goods.toString());
                }
                if (goods != null && goods.size() != 0) {
                    StoreFragment.this.lastId = goods.get(goods.size() - 1).getId();
                } else {
                    if (goods != null) {
                        goods.size();
                        app.t("not found");

                        isLoading = false;
                        resumeDataListener("getStore2", "null", "null", "null");
                        requestGoods(0, "getStore2", "null", "null", "null");
                    }
                }
                adapter = new StoreRecyclerAdapter(getActivity(), getContext(), tmpGoods, StoreFragment.this);
                recyclerStore.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Good>> call, Throwable t) {
                app.l(t.getLocalizedMessage());
            }
        });
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

    public void collectData() {
        String carQuestionString = carQuestion.getText().toString();
        String goodQuestionString = goodQuestion.getText().toString();
        int warrantyId = 0;
    }

    boolean is_stoke_active;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stoke:
                is_stoke_active = !is_stoke_active;
                countrySpinnerAdapter.disableAdapter(is_stoke_active);
                warrantySpinnerAdapter.disableAdapter(is_stoke_active);
                if (is_stoke_active) {
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                    countrySpinner.setEnabled(false);
                    countrySpinner.setClickable(false);
                    warrantySpinner.setEnabled(false);
                    warrantySpinner.setClickable(false);


                } else {
                    stoke.setBackground(getResources().getDrawable(R.drawable.btn_inactive_stoke));
                    countrySpinner.setEnabled(true);
                    countrySpinner.setClickable(true);
                    warrantySpinner.setEnabled(true);
                    warrantySpinner.setClickable(true);
                }
                break;
        }
    }
}
