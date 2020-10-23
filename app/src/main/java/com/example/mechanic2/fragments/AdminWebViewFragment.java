package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.WebViewAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AlertAction;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.models.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminWebViewFragment extends Fragment {

    public final String OFFSET = "offset";
    public final String GET_ADMIN_MEDIAS_FROM_APARAT_API = "getAdminMediasFromAparatApiWebView";
    private RecyclerView recyclerAdmin;


    private int lastId = 0;
    private WebViewAdapter adapter;

    private List<Job> adminMedias;

    private List<Job> models = new ArrayList<>();
    private List<Job> tmpModels = new ArrayList<>();
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



    private void getAdminMedias() {

        if (sweetAlertDialog != null) sweetAlertDialog.dismiss();
        if (sweetAlertDialogInvalidLength != null) sweetAlertDialogInvalidLength.dismiss();


        sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("لطفا شکیبا باشید.");
        sweetAlertDialog.setContentText("در حال دریافت فیلم های آموزشی");
        sweetAlertDialog.show();

        offset = 0;
        adminMedias = new ArrayList<>();
        tmpModels = new ArrayList<>();
        adapter = new WebViewAdapter(getActivity(),adminMedias);
        Map<String, String> map = new HashMap<>();
        map.put(getString(R.string.rt), GET_ADMIN_MEDIAS_FROM_APARAT_API);
        map.put(OFFSET, String.valueOf(offset));


        Application.getApi().getWebMediaInList(map).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {

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
                    adapter = new WebViewAdapter(getActivity(),tmpModels);
                    recyclerAdmin.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
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

        Application.getApi().getWebMediaInList(map).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.body() != null && response.body().size() == 0) {
                    return;
                }


                List<Job> newMedias = response.body();
                if (newMedias != null) {
                    tmpModels.addAll(newMedias);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {

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
