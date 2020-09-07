package com.example.mechanic2.fragments;import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.example.mechanic2.activities.AnswersActivity;
import com.example.mechanic2.activities.ExoVideoActivity;
import com.example.mechanic2.activities.VideoActivity;
import com.example.mechanic2.adapters.NewAdminAdapter;
import com.example.mechanic2.adapters.NewAdminAdapter;
import com.example.mechanic2.adapters.QuestionRecyclerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.models.AdminMedia;
import com.example.mechanic2.models.Movies;
import com.example.mechanic2.models.Question;
import com.google.gson.Gson;
import com.hmomeni.progresscircula.ProgressCircula;
import com.j256.ormlite.stmt.query.In;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminFragment extends Fragment implements OnClickListener {

    private RecyclerView recyclerAdmin;


    private int lastId = 0;
    private NewAdminAdapter adapter;
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
        recyclerAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdmin.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        app.validateConnection(getActivity(), null, new ConnectionErrorManager() {
            @Override
            public void doAction() {
                resumeMediaListener();
                getAdminMedias();
            }
        });
        return inflate;
    }



    @Override
    public void onDownloadStateClick(AdminMedia adminMedia, View itemView) {


        ProgressCircula progressCircula;
        LottieAnimationView lottieAnimationView;
        TextView percentDone;
        ImageView preview;
        progressCircula = itemView.findViewById(R.id.progressCircula);
        lottieAnimationView = itemView.findViewById(R.id.lottieAnimationView);
        percentDone = itemView.findViewById(R.id.percentDone);
        preview = itemView.findViewById(R.id.preview);

        String adminUrl = adminMedia.getMovie_url();
        String url = adminMedia.getMovie_url();
        String path = getActivity().getExternalFilesDir("video/mp4").getAbsolutePath();
        //String path = getActivity().getExternalFilesDir("video/mp4").getAbsolutePath() + adminUrl.substring(adminUrl.lastIndexOf("/"));
        File file = new File(getActivity().getExternalFilesDir("video/mp4").getAbsolutePath() + url.substring(url.lastIndexOf("/")));

        app.l(file.getAbsolutePath() + "adfadfbbhhf");
        app.l("file.length():"+file.length()+"adminMedia.getMovie_size():"+adminMedia.getMovie_size()+"file.length() - adminMedia.getMovie_size(): "+ (file.length() - adminMedia.getMovie_size()));
        if (file.exists() &&(file.length() - adminMedia.getMovie_size() == -8 || file.length() - adminMedia.getMovie_size() == 0 )) {

            Picasso.get().load(adminMedia.getMovie_preview())
                    .into(preview);
            Intent intent = new Intent(getActivity(), ExoVideoActivity.class);
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
                percentDone.setText(String.valueOf(value) + "%");
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

                Picasso.get().load(adminMedia.getMovie_preview())
                        .into(preview);
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
    public void onDownloadStateClick(Movies movies, View viewHolder) {

    }

    @Override
    public void onRemoveClick(Movies movies, View itemView) {
    }


    private void getAdminMedias() {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("لطفا شکیبا باشید.");
        sweetAlertDialog.setContentText("در حال دریافت فیلم های آموزشی");
        sweetAlertDialog.show();

        offset = 0;
        adminMedias = new ArrayList<>();
        tmpModels = new ArrayList<>();
        adapter = new NewAdminAdapter(getContext(), adminMedias, this);
        Map<String, String> map = new HashMap<>();
        map.put("route", "getAdminMediasFromAparatApi");
        map.put("offset", String.valueOf(offset));

        Application.getApi().getAdminMediaInList(map).enqueue(new Callback<List<AdminMedia>>() {
            @Override
            public void onResponse(Call<List<AdminMedia>> call, Response<List<AdminMedia>> response) {
                app.l(new Gson().toJson(response.body()) + "adfafbbg");
                sweetAlertDialog.dismissWithAnimation();
                if (response.body() != null && response.body().size() > 0) {
                    adminMedias = response.body();
                    if (adminMedias != null && adminMedias.size() != 0) {
                        tmpModels.addAll(adminMedias);
                    } else {
                        if (adminMedias != null) {
                            app.t("not found11");
                            isLoading = false;
                        }
                    }
                    Intent intent = new Intent("dataCount");
                    intent.putExtra("ref", "qsf");
                    LocalBroadcastManager.getInstance(AdminFragment.this.getContext()).sendBroadcast(intent);
                    adapter = new NewAdminAdapter(getContext(), tmpModels, AdminFragment.this);
                    recyclerAdmin.setAdapter(adapter);

                } else app.t("connection error1");
            }

            @Override
            public void onFailure(Call<List<AdminMedia>> call, Throwable t) {
                app.t("connection error2");
            }
        });

    }

    private void resumeGetMedias(int offset) {

        app.l("poipoipoioiBB");
        Map<String, String> map = new HashMap<>();
        map.put("route", "getAdminMediasFromAparatApi");
        map.put("offset", String.valueOf(offset));
        Application.getApi().getAdminMediaInList(map).enqueue(new Callback<List<AdminMedia>>() {
            @Override
            public void onResponse(Call<List<AdminMedia>> call, Response<List<AdminMedia>> response) {
                if (response.body() != null && response.body().size() == 0) {
                    return;
                }
                app.l("poipoipoioiBB" + new Gson().toJson(response.body()));
                List<AdminMedia> newMedias = response.body();
                if (newMedias != null) {
                    tmpModels.addAll(newMedias);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<List<AdminMedia>> call, Throwable t) {
                app.t("connection error3");
            }
        });


    }

    int offset;

    private void resumeMediaListener() {
        app.l("poipoipoioiCC");
        isLoading = false;
        recyclerAdmin.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpModels.size() - 1 && !isLoading) {
                    isLoading = true;
                    offset++;
                    resumeGetMedias(offset);
                }
            }
        });
    }
}
