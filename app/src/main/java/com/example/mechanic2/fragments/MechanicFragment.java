package com.example.mechanic2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.MechanicRecyclerAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Mechanic;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MechanicFragment extends Fragment implements View.OnClickListener {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_mechanic, container, false);
        return init(inflate);
    }
    private View init(View inflate) {

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









        Map<String,String> map=new HashMap<>();
        map.put("route","getMechanics");
        map.put("offset","0");
        map.put("jobId","0");
        map.put("regionId","0");
        map.put("x","0");
        map.put("y","0");
        map.put("sortBy","0");
        Application.getApi().getMechanicList(map).enqueue(new Callback<List<Mechanic>>() {
            @Override
            public void onResponse(Call<List<Mechanic>> call, Response<List<Mechanic>> response) {
                app.l("MMEC"+response.body().get(0).getMovies().size());
            }

            @Override
            public void onFailure(Call<List<Mechanic>> call, Throwable t) {
                app.l("MMECHANIC"+t.getLocalizedMessage());
            }
        });
        return inflate;
    }

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

    }
}
