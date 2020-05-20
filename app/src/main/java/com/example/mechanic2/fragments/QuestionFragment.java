package com.example.mechanic2.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.AddQuestionActivity;
import com.example.mechanic2.adapters.QuestionRecyclerAdapter;
import com.example.mechanic2.adapters.StoreRecyclerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AddQuestionFab;
import com.example.mechanic2.models.Good;
import com.example.mechanic2.models.Question;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment implements View.OnClickListener {
    private boolean x;
    private TextView fab_main;
    private LinearLayout fab_main_layout;
    private LinearLayout fab_add_layout;
    private LinearLayout fab_my_layout;
    private TextView fab_add;
    private TextView fab_my;
    private FragmentManager fragmentManager;
    private RecyclerView recyclerQuestion;
    private List<Question> questions;
    private List<Question> tmpQuestions;
    private QuestionRecyclerAdapter adapter;
    private int lastId = 0;
    private boolean isLoading;
    private boolean isFABOpen;
    private Fragment initialFragment;
    private LottieAnimationView close_fab;
    private LottieAnimationView plus_fab;
    private LottieAnimationView person_fab;
    private RelativeLayout fab_parent_layout;
    private ValueAnimator valueAnimator;
    private View transparent_view;


    //http://drkamal3.com/Mechanic/index.php?route=getGoodsByCar&carName=%D9%BE%D8%B1%D8%A7%DB%8C%D8%AF

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        app.l("D");
        View inflate = inflater.inflate(R.layout.fragment_question, container, false);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        return init(inflate);
    }

    private View init(View inflate) {

        fab_main = inflate.findViewById(R.id.fab_main);
        transparent_view = inflate.findViewById(R.id.transparent_view);
        fab_add = inflate.findViewById(R.id.fab_add);
        fab_my = inflate.findViewById(R.id.fab_my);
        fab_my_layout = inflate.findViewById(R.id.fab_my_layout);
        fab_add_layout = inflate.findViewById(R.id.fab_add_layout);
        fab_main_layout = inflate.findViewById(R.id.fab_main_layout);
        recyclerQuestion = inflate.findViewById(R.id.recyclerQuestion);
        close_fab = inflate.findViewById(R.id.close_fab);
        plus_fab = inflate.findViewById(R.id.plus_fab);
        person_fab = inflate.findViewById(R.id.person_fab);
        fab_parent_layout = inflate.findViewById(R.id.fab_parent_layout);
        recyclerQuestion.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerQuestion.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));



        resumeDataListener("questionsFetchAll", "null", "null", "null");
        requestQuestion(0, "questionsFetchAll", "null", "null", "null");

        fab_main_layout.setOnClickListener(this);
        fab_my_layout.setOnClickListener(this);
        fab_add_layout.setOnClickListener(this);
        return inflate;

    }

    private void showFABMenu() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(animation -> transparent_view.setAlpha(((Float) animation.getAnimatedValue())));
        valueAnimator.setDuration(300);
        valueAnimator.start();
        fab_add_layout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab_my_layout.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab_main.setText("بی خیال شدم");
        close_fab.setMinProgress(0);
        close_fab.setMaxProgress(0.5f);
        plus_fab.setMinProgress(0);
        plus_fab.setMaxProgress(0.5f);
        person_fab.setMinProgress(0);
        person_fab.setMaxProgress(0.5f);
    }

    private void closeFABMenu() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                transparent_view.setAlpha(((Float) animation.getAnimatedValue()));
            }
        });
        valueAnimator.setDuration(300);

        valueAnimator.start();

        fab_add_layout.animate().translationY(0);
        fab_my_layout.animate().translationY(0);
        fab_main.setText("یه چیزی بگو");
        close_fab.setMinProgress(0.5f);
        close_fab.setMaxProgress(1);
        plus_fab.setMinProgress(0.5f);
        plus_fab.setMaxProgress(1);
        person_fab.setMinProgress(0.5f);
        person_fab.setMaxProgress(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_layout:
            case R.id.fab_my_layout:
            case R.id.fab_main_layout:
                x = !x;
                if (x) {
                    //ObjectAnimator.ofFloat(fab_main, View.ROTATION, 0f, -45).setDuration(100).start();
                    showFABMenu();
                } else {
                    closeFABMenu();
                    //ObjectAnimator.ofFloat(fab_main, View.ROTATION, -45f, 0).setDuration(100).start();
                }
                close_fab.setSpeed(1.75f);
                close_fab.playAnimation();
                plus_fab.setSpeed(1.75f);
                plus_fab.playAnimation();
                person_fab.setSpeed(2.5f);
                person_fab.playAnimation();
                break;
        }
        switch (v.getId()){
            case R.id.fab_my_layout:
                break;
            case R.id.fab_add_layout:
                startActivityForResult(new Intent(getActivity(), AddQuestionActivity.class),1);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isLoading = false;
        resumeDataListener("questionsFetchAll", "null", "null", "null");
        requestQuestion(0, "questionsFetchAll", "null", "null", "null");

    }

    private void resumeGetData(int lastIdN, String root, String carName, String entranceId, String search) {

        Map<String, String> data = new HashMap<>();
        data.put("route", root);
        data.put("lastId", String.valueOf(lastIdN));
        data.put("carName", carName);
        data.put("entranceId", entranceId);
        data.put("search", search);

        Application.getApi().getQuestionList(data).enqueue(new Callback<List<Question>>() {

            @Override
            public void onResponse(@NotNull Call<List<Question>> call, @NotNull Response<List<Question>> response) {
                app.l(new Gson().toJson(response.body()));
                if (response.body() != null && response.body().size() == 0) {
                    app.l("f");
                    return;
                }
                List<Question> newQuestions = response.body();

                app.l(new Gson().toJson(newQuestions) + "**");
                if (newQuestions != null && newQuestions.size()!=0) {
                    QuestionFragment.this.lastId = newQuestions.get(newQuestions.size() - 1).getQ_id();
                }
                if (newQuestions != null) {
                    tmpQuestions.addAll(newQuestions);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                app.l(t.getLocalizedMessage());
            }
        });
    }

    private void requestQuestion(int lastId, String root, String carName, String entranceId, String search) {
        lastId = 0;
        questions = new ArrayList<>();
        tmpQuestions = new ArrayList<>();
        adapter = new QuestionRecyclerAdapter(tmpQuestions,getActivity());
        Map<String, String> data = new HashMap<>();
        data.put("route", root);
        data.put("lastId", String.valueOf(lastId));
        data.put("carName", carName);
        data.put("entranceId", entranceId);
        data.put("search", search);
        Application.getApi().getQuestionList(data).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, retrofit2.Response<List<Question>> response) {
                questions = response.body();
                if (response.body() != null && questions.size()!=0) {
                    app.l(response.body().toString());
                }
                if (questions != null && questions.size()!=0) {
                    app.l(new Gson().toJson(questions) + "**7");
                    app.l(new Gson().toJson(response.body()));
                    tmpQuestions.addAll(questions);
                    app.l(questions.toString());
                }
                if (questions != null && questions.size() != 0) {
                    QuestionFragment.this.lastId = questions.get(questions.size() - 1).getQ_id();
                } else {
                    if (questions != null) {
                        questions.size();
           /*               StoreFragment.this.search.setText(SEARCH);
                            toolbar.setVisibility(View.GONE);
                            ((View) StoreFragment.this.goodName.getParent()).setVisibility(View.INVISIBLE);
                            ((View) StoreFragment.this.carName.getParent()).setVisibility(View.INVISIBLE);*/

                        isLoading = false;
                        resumeDataListener("getStore2", "null", "null", "null");
                        requestQuestion(0, "questionsFetchAll", "null", "null", "null");
                    }
                }
                adapter = new QuestionRecyclerAdapter(tmpQuestions,getActivity());
                recyclerQuestion.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                app.l(t.getLocalizedMessage());
            }
        });
    }

    private void resumeDataListener(String root, String carName, String entranceId, String search) {
        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpQuestions.size() - 1 && !isLoading) {
                    isLoading = true;
                    resumeGetData(lastId, root, carName, entranceId, search);
                }
            }
        });
    }


}
