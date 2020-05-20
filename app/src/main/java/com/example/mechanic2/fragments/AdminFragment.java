package com.example.mechanic2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
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
import com.example.mechanic2.activities.VideoActivity;
import com.example.mechanic2.adapters.AdminRecyclerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.models.AdminMedia;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminFragment extends Fragment implements OnClickListener {

    private RecyclerView recyclerAdmin;


    private int lastId = 0;
    private AdminRecyclerAdapter adapter;
    private RequestQueue requestQueue;
    private List<AdminMedia> adminMedias;


    private List<AdminMedia> models = new ArrayList<>();
    private List<AdminMedia> tmpModels = new ArrayList<>();
    private Map<String, String> data = new HashMap<>();
    private boolean isLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminMedias = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_admin, container, false);
        return init(inflate);
    }

    private View init(View inflate) {
        recyclerAdmin = inflate.findViewById(R.id.recyclerAdmin);
        //requestQueue = Application.getRequest();
        recyclerAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdmin.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        recyclerAdmin.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpModels.size() - 1 && !isLoading) {
 /*                   StringRequest stringRequest = new StringRequest(Request.Method.POST, app.adminBaseUrl + lastId, response -> {
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

                    requestQueue.add(stringRequest);*/
                    //------------------------------
                    isLoading = true;
                    data.put("route", "getAdminMedias");
                    data.put("lastId", String.valueOf(lastId));
                    Application.getApi().getAdminMediaInList(data).enqueue(new Callback<List<AdminMedia>>() {

                        @Override
                        public void onResponse(@NotNull Call<List<AdminMedia>> call, @NotNull Response<List<AdminMedia>> response) {
                            app.l(new Gson().toJson(response.body()));
                            if (response.body() != null && response.body().size() == 0) {
                                app.l("f");
                                return;
                            }
                            List<AdminMedia> newModels = response.body();
                            if (newModels != null) {
                                lastId = newModels.get(newModels.size() - 1).getId();
                            }
                            if (newModels != null) {
                                tmpModels.addAll(newModels);
                            }
                            adapter.notifyDataSetChanged();
                            isLoading = false;

                        }

                        @Override
                        public void onFailure(Call<List<AdminMedia>> call, Throwable t) {
                            app.l(t.getLocalizedMessage());
                        }
                    });


                }
            }
        });
        requestMedia(lastId);
        return inflate;
    }

    public void requestMedia(int lastId) {
      /*  StringRequest stringRequest = new StringRequest(Request.Method.GET, app.adminBaseUrl + lastId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                app.l(response);
                Gson gson = new Gson();
                AdminMedia[] adminMediasModels = gson.fromJson(response, AdminMedia[].class);
                models = Arrays.asList(adminMediasModels);
                tmpModels.addAll(models);
                AdminFragment.this.lastId = models.get(models.size() - 1).getId();
                adapter = new AdminRecyclerAdapter(getContext(), tmpModels, AdminFragment.this);
                recyclerAdmin.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                app.l(error.getLocalizedMessage() + "AA");
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);*/
        //------------------------------


        //drkamal3.com/Mechanic/index.php?route=getAdminMedias&lastId=0
        Map<String, String> data = new HashMap<>();
        data.put("route", "getAdminMedias");
        data.put("lastId", String.valueOf(lastId));

        Application.getApi().getAdminMediaInList(data).enqueue(new Callback<List<AdminMedia>>() {
            @Override
            public void onResponse(Call<List<AdminMedia>> call, retrofit2.Response<List<AdminMedia>> response) {
                models = response.body();
                app.l(new Gson().toJson(response.body()));
                if (models != null) {
                    tmpModels.addAll(models);
                }
                if (models != null) {
                    AdminFragment.this.lastId = models.get(models.size() - 1).getId();
                }
                adapter = new AdminRecyclerAdapter(getContext(), tmpModels, AdminFragment.this);
                recyclerAdmin.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<AdminMedia>> call, Throwable t) {
                app.l(t.getLocalizedMessage());
            }
        });
        //------------------------------


    }


    @Override
    public void onDownloadStateClick(AdminMedia adminMedia, View itemView) {


        ProgressCircula progressCircula;
        LottieAnimationView lottieAnimationView;
        TextView percentDone;
        progressCircula = itemView.findViewById(R.id.progressCircula);
        lottieAnimationView = itemView.findViewById(R.id.lottieAnimationView);
        percentDone = itemView.findViewById(R.id.percentDone);
        String adminUrl = adminMedia.getUrl();
        String url = adminMedia.getUrl();
        String path = getActivity().getExternalFilesDir("video/mp4").getAbsolutePath();
        //String path = getActivity().getExternalFilesDir("video/mp4").getAbsolutePath() + adminUrl.substring(adminUrl.lastIndexOf("/"));
        File file = new File(getActivity().getExternalFilesDir("video/mp4").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

        if (file.exists() && file.length() == adminMedia.getFileSize()) {
            app.l("playing");
            Intent intent = new Intent(getActivity(), VideoActivity.class);
            intent.putExtra("path", getActivity().getExternalFilesDir("video/mp4").getAbsolutePath() + url.
                    substring(url.lastIndexOf("/")));

            intent.putExtra("id", adminMedia.getId());
            getActivity().startActivity(intent);
            return;
        }

        if (!lottieAnimationView.isAnimating()) {
            lottieAnimationView.resumeAnimation();
            progressCircula.startRotation();
        } else {
            lottieAnimationView.pauseAnimation();
            progressCircula.stopRotation();
        }


        int downloadId = SharedPrefUtils.getIntData("downloadId**" + adminMedia.getMedia_desc());
        if (Status.RUNNING == PRDownloader.getStatus(downloadId)) {
            PRDownloader.pause(downloadId);
            progressCircula.stopRotation();
            return;
        }
        if (Status.PAUSED == PRDownloader.getStatus(downloadId)) {
            PRDownloader.resume(downloadId);
            progressCircula.startRotation();
            return;
        }


        DownloadRequest downloadRequest = PRDownloader.download(url, path, adminUrl.substring(adminUrl.lastIndexOf("/"))).build();
        downloadRequest.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
                app.l("pause***" + adminMedia.getMedia_desc());
            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int value = (int) (100 * progress.currentBytes / progress.totalBytes);
                progressCircula.setProgress(value);
                percentDone.setText(String.valueOf(value)+"%");
                app.l(String.valueOf(progress.currentBytes));
            }
        }).setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {
                app.l("start or resume***" + adminMedia.getMedia_desc());
            }
        });
        downloadId = downloadRequest.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("downloadId**" + adminMedia.getMedia_desc()).apply();
                progressCircula.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                lottieAnimationView.setAnimation(R.raw.play_anim);
                lottieAnimationView.setRepeatCount(0);
                lottieAnimationView.playAnimation();
                ((TextView) itemView.findViewById(R.id.totalSize)).setTextColor(getActivity().getResources().getColor(android.R.color.holo_green_dark));

                app.l("completed");

            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData("downloadId**" + adminMedia.getMedia_desc(), downloadId);


    }

    @Override
    public void onDownloadCompleteClick(AdminMedia adminMedia, View itemView) {


    }
}
