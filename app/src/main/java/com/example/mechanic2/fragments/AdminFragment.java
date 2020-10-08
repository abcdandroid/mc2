package com.example.mechanic2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.example.mechanic2.activities.ExoVideoActivity;
import com.example.mechanic2.adapters.NewAdminAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AlertAction;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.interfaces.OnClickListener;
import com.example.mechanic2.models.AdminMedia;
import com.example.mechanic2.models.Movies;
import com.hmomeni.progresscircula.ProgressCircula;
import com.squareup.picasso.Picasso;

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

    public final String OFFSET = "offset";
    public final String GET_ADMIN_MEDIAS_FROM_APARAT_API = "getAdminMediasFromAparatApi";
    private RecyclerView recyclerAdmin;


    private int lastId = 0;
    private NewAdminAdapter adapter;
    private List<AdminMedia> adminMedias;


    private List<AdminMedia> models = new ArrayList<>();
    private List<AdminMedia> tmpModels = new ArrayList<>();
    private Map<String, String> data = new HashMap<>();
    private boolean isLoading;
    private SweetAlertDialog sweetAlertDialog;
    private SweetAlertDialog sweetAlertDialogInvalidLength;

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
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + url.substring(url.lastIndexOf("/")));


        if (file.exists() && (file.length() - adminMedia.getMovie_size() == -8 || file.length() - adminMedia.getMovie_size() == 0)) {

            Picasso.get().load(adminMedia.getMovie_preview())
                    .into(preview);
            Intent intent = new Intent(getActivity(), ExoVideoActivity.class);
            intent.putExtra(getString(R.string.path), getActivity().getExternalFilesDir(getString(R.string.videomp4)).getAbsolutePath() + url.
                    substring(url.lastIndexOf("/")));
            intent.putExtra(getString(R.string.id), adminMedia.getId());
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


        int downloadId = SharedPrefUtils.getIntData(getString(R.string.ab) + adminMedia.getMedia_desc());
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

            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int value = (int) (100 * progress.currentBytes / progress.totalBytes);
                progressCircula.setProgress(value);
                percentDone.setText(String.valueOf(value) + "%");

            }
        }).setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {

            }
        });
        downloadId = downloadRequest.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {

                Picasso.get().load(adminMedia.getMovie_preview())
                        .into(preview);
                SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove(getString(R.string.ab) + adminMedia.getMedia_desc()).apply();
                progressCircula.setVisibility(View.GONE);
                percentDone.setVisibility(View.GONE);
                lottieAnimationView.setAnimation(R.raw.play_main2);
                lottieAnimationView.setRepeatCount(0);
                lottieAnimationView.playAnimation();
                ((TextView) itemView.findViewById(R.id.totalSize)).setTextColor(getActivity().getResources().getColor(android.R.color.holo_green_dark));

            }

            @Override
            public void onError(Error error) {

            }
        });
        SharedPrefUtils.saveData(getString(R.string.ab) + adminMedia.getMedia_desc(), downloadId);


    }

    @Override
    public void onDownloadStateClick(Movies movies, View viewHolder) {

    }

    @Override
    public void onRemoveClick(Movies movies, View itemView) {
    }


    private void getAdminMedias() {

        if(sweetAlertDialog !=null) sweetAlertDialog.dismiss();;
        if(sweetAlertDialogInvalidLength!=null) sweetAlertDialogInvalidLength.dismiss();


        sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("لطفا شکیبا باشید.");
        sweetAlertDialog.setContentText("در حال دریافت فیلم های آموزشی");
        sweetAlertDialog.show();

        offset = 0;
        adminMedias = new ArrayList<>();
        tmpModels = new ArrayList<>();
        adapter = new NewAdminAdapter(getContext(), adminMedias, this);
        Map<String, String> map = new HashMap<>();
        map.put(getString(R.string.rt), GET_ADMIN_MEDIAS_FROM_APARAT_API);
        map.put(OFFSET, String.valueOf(offset));

        Application.getApi().getAdminMediaInList(map).enqueue(new Callback<List<AdminMedia>>() {
            @Override
            public void onResponse(Call<List<AdminMedia>> call, Response<List<AdminMedia>> response) {

                sweetAlertDialog.dismissWithAnimation();
                if (response.body() != null && response.body().size() > 0) {
                    adminMedias = response.body();
                    if (adminMedias != null && adminMedias.size() != 0) {
                        tmpModels.addAll(adminMedias);
                    } else {
                        if (adminMedias != null) {

                            isLoading = false;
                        }
                    }
                    Intent intent = new Intent(getString(R.string.dc));
                    intent.putExtra("ref", "qsf");
                    LocalBroadcastManager.getInstance(AdminFragment.this.getContext()).sendBroadcast(intent);
                    adapter = new NewAdminAdapter(getContext(), tmpModels, AdminFragment.this);
                    recyclerAdmin.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<AdminMedia>> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getContext() == null) {
                            app.t("خطا در برقراری ارتباط");
                            return;
                        }
                        showAlertDialog("خطا در برقراری ارتباط", "تلاش دوباره", false, new AlertAction() {
                            @Override
                            public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                                getAdminMedias();
                            }
                        });
                    }
                }, 1000);
            }
        });

    }

    private void resumeGetMedias(int offset) {


        Map<String, String> map = new HashMap<>();
        map.put(getString(R.string.rt), GET_ADMIN_MEDIAS_FROM_APARAT_API);
        map.put(OFFSET, String.valueOf(offset));
        Application.getApi().getAdminMediaInList(map).enqueue(new Callback<List<AdminMedia>>() {
            @Override
            public void onResponse(Call<List<AdminMedia>> call, Response<List<AdminMedia>> response) {
                if (response.body() != null && response.body().size() == 0) {
                    return;
                }

                List<AdminMedia> newMedias = response.body();
                if (newMedias != null) {
                    tmpModels.addAll(newMedias);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<List<AdminMedia>> call, Throwable t) {

            }
        });


    }

    int offset;

    private void resumeMediaListener() {

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


    private void showAlertDialog(String titleMsg, String okMsg, boolean isCancellable, AlertAction alertAction) {
        if (getContext() == null) {
            Toast.makeText(Application.getContext(), "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show();
            return;
        }
        sweetAlertDialogInvalidLength = new SweetAlertDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null);
        ((TextView) view.findViewById(R.id.txt)).setText(titleMsg);
        view.findViewById(R.id.btn_show_all_goods).setVisibility(View.GONE);
        TextView txtOk = view.findViewById(R.id.txt_ok);
        txtOk.setText(okMsg);
        view.findViewById(R.id.btn_contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAction.doOnClick(sweetAlertDialogInvalidLength);
            }
        });
        sweetAlertDialogInvalidLength.setCancelable(isCancellable);
        sweetAlertDialogInvalidLength.setCustomView(view);
        sweetAlertDialogInvalidLength.hideConfirmButton();
        sweetAlertDialogInvalidLength.show();
    }
}
