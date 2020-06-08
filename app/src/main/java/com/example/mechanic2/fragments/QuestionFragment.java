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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.AddQuestionActivity;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.GoodAutoCompleteAdapter;
import com.example.mechanic2.adapters.GooodStoreAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.adapters.QuestionRecyclerAdapter;
import com.example.mechanic2.adapters.StoreRecyclerAdapter;
import com.example.mechanic2.adapters.TitleQuestionAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AddQuestionFab;
import com.example.mechanic2.models.Goood;
import com.example.mechanic2.models.QusetionWithMsg;
import com.example.mechanic2.models.Title;
import com.example.mechanic2.models.Question;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private int lastSeenCount = 0;

    private boolean isLoading;
    private boolean isFABOpen;
    private Fragment initialFragment;
    private LottieAnimationView close_fab;
    private LottieAnimationView plus_fab;
    private LottieAnimationView person_fab;
    private RelativeLayout fab_parent_layout;
    private ValueAnimator valueAnimator;
    private View transparent_view;

    View view;

    private CoordinatorLayout parent;
    private AppBarLayout appbar;
    private RelativeLayout submitFilterParent;
    private TextView submitFilter;
    private SpinKitView loading;
    private RelativeLayout carQuestionParent;
    private AutoCompleteTextView carQuestion;
    private ImageView resetCar;
    private RelativeLayout titleQuestionParent;
    private AutoCompleteTextView titleQuestion;
    private ImageView resetTitle;
    private AppCompatSpinner spinnerFilter;
    private AppCompatTextView myQuestion;
    TitleQuestionAutoCompleteAdapter titleQuestionAutoCompleteAdapter;

    MySpinnerAdapter filterSpinnerAdapter;


    private int selectedCarId;
    private int selectedTitleId;
    String filterIdInString = "1";

    private RelativeLayout btnAddQuestion;
    private RelativeLayout btnShowAllQuestions;

    private boolean isMyQuestionActive = false;

    int i;

    //http://drkamal3.com/Mechanic/index.php?route=getTitlesByCar&carName=%D9%BE%D8%B1%D8%A7%DB%8C%D8%AF

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


        parent = inflate.findViewById(R.id.parent);
        appbar = inflate.findViewById(R.id.appbar);
        submitFilterParent = inflate.findViewById(R.id.submit_filter_parent);
        submitFilter = inflate.findViewById(R.id.submit_filter);
        loading = inflate.findViewById(R.id.loading);
        carQuestionParent = inflate.findViewById(R.id.car_question_parent);
        carQuestion = inflate.findViewById(R.id.car_question);
        resetCar = inflate.findViewById(R.id.reset_car);
        titleQuestionParent = inflate.findViewById(R.id.title_question_parent);
        titleQuestion = inflate.findViewById(R.id.title_question);
        resetTitle = inflate.findViewById(R.id.reset_title);
        spinnerFilter = inflate.findViewById(R.id.spinner_filter);
        myQuestion = inflate.findViewById(R.id.myQuestion);

        resetCar.setOnClickListener(this);
        resetTitle.setOnClickListener(this);
        submitFilterParent.setOnClickListener(this);
        myQuestion.setOnClickListener(this);
/*
        resumeDataListener("questionsFetchAll", "null", "null", "null");
        requestQuestion(0, "questionsFetchAll", "null", "null", "null");*/
        List<String> filterNames = new ArrayList<>();
        filterNames.add("جدیدترین");
        filterNames.add("پربازدیدترین");
        List<Integer> filterIds = new ArrayList<>();
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
                    if (i > 2) {
                        loading.setVisibility(View.VISIBLE);
                        submitFilter.setVisibility(View.INVISIBLE);
                        app.l("selectedCarId" + selectedCarId + "selectedTitleId" + selectedTitleId + "filterIdInString" + Integer.parseInt(filterIdInString) + "getMyQuestionValue" + getMyQuestionValue());
                        resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                        getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });/**/


        CarAutoCompleteAdapter carAdapter = new CarAutoCompleteAdapter(getActivity(), R.layout.item_show_auto_complete);
        carQuestion.setAdapter(carAdapter);
        TitleQuestionAutoCompleteAdapter goodAdapter = new TitleQuestionAutoCompleteAdapter(Application.getContext(), R.layout.item_show_auto_complete);
        titleQuestion.setAdapter(goodAdapter);


        carQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCarId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });
        titleQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTitleId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });

        recyclerQuestion.setLayoutManager(new LinearLayoutManager(getContext()) {
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
        titleQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    selectedTitleId = 0;
                    resetTitle.setVisibility(View.INVISIBLE);
                } else resetTitle.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerQuestion.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        loading.setVisibility(View.VISIBLE);
        submitFilter.setVisibility(View.INVISIBLE);
        resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
        getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());


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
            case R.id.myQuestion:
                app.l("selectedCarId:" + selectedCarId);
                app.l("selectedGoodId:" + selectedTitleId);
                app.l("warrantyIdInStringInteger:" + Integer.parseInt(filterIdInString));
                app.l("is_stoke_active:" + getMyQuestionValue());
/*
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(goodQuestion);*/
                modifyIds();
                isMyQuestionActive = !isMyQuestionActive;
                app.l("ci:" + selectedCarId + "ti" + selectedTitleId + "fi" + filterIdInString + "mi" + (getMyQuestionValue()));
                filterSpinnerAdapter.disableAdapter(isMyQuestionActive);
                if (isMyQuestionActive) {
                    myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                    spinnerFilter.setEnabled(false);
                    spinnerFilter.setClickable(false);


                } else {
                    myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_inactive_stoke));
                    spinnerFilter.setEnabled(true);
                    spinnerFilter.setClickable(true);
                }

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());


                break;
            case R.id.btn_show_all_questions:
                sweetAlertDialogQuestionNotExist.dismissWithAnimation();
                myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_inactive_stoke));
                titleQuestion.setText("");
                carQuestion.setText("");
                spinnerFilter.setEnabled(true);
                spinnerFilter.setClickable(true);
                isMyQuestionActive = false;
                myQuestion.setEnabled(true);
                selectedTitleId = 0;
                selectedCarId = 0;
                filterIdInString = "1";
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                if (spinnerFilter.getSelectedItemPosition() != 0) {
                    spinnerFilter.setSelection(0);
                } else if (spinnerFilter.getSelectedItemPosition() == 0) {
                    resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                    getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                }
                break;
            case R.id.btn_add_question:
                break;
            case R.id.reset_title:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(titleQuestion);
                modifyIds();
                titleQuestion.setHint(getString(R.string.ask_good));
                titleQuestion.setText("");
                myQuestion.setEnabled(true);
                myQuestion.setBackground(getActivity().getDrawable(R.drawable.btn_inactive_stoke));
                selectedTitleId = 0;
                app.l("selectedCarId:" + selectedCarId);
                app.l("selectedTitleId:" + selectedTitleId);
                app.l("filterIdInString:" + Integer.parseInt(filterIdInString));
                app.l("getMyQuestionValue:" + (getMyQuestionValue()));
                app.l("filterIdInString:" + Integer.parseInt(filterIdInString));
                filterSpinnerAdapter.disableAdapter(isMyQuestionActive);
                resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                break;
            case R.id.reset_car:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(titleQuestion);
                carQuestion.setText("");
                selectedCarId = 0;
                modifyIds();
                filterSpinnerAdapter.disableAdapter(isMyQuestionActive);
                resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                break;
            case R.id.submit_filter_parent:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(titleQuestion);
                modifyIds();
                app.l(carQuestion.getText().toString());
                app.l(carQuestion.getHint().toString());
                resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                break;
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
        switch (v.getId()) {
            case R.id.fab_my_layout:
                break;
            case R.id.fab_add_layout:
                startActivityForResult(new Intent(getActivity(), AddQuestionActivity.class), 1);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isLoading = false;/*
        resumeDataListener("questionsFetchAll", "null", "null", "null");*//*
        requestQuestion(0, "questionsFetchAll", "null", "null", "null");*/
    }


    /*-------------------*/

    SweetAlertDialog sweetAlertDialogQuestionNotExist;

    private void getQuestions(int carId, int titleId, int sortBy, int showMyQuestion) {
        lastId = 0;
        offset = 0;
        questions = new ArrayList<>();
        tmpQuestions = new ArrayList<>();
        adapter = new QuestionRecyclerAdapter(questions, getActivity());


        Map<String, String> map = new HashMap<>();
        map.put("route", "getQuestions");
        map.put("lastId", String.valueOf(lastId));
        map.put("offset", String.valueOf(offset));
        map.put("carId", String.valueOf(carId));
        map.put("titleId", String.valueOf(titleId));
        map.put("sortBy", String.valueOf(sortBy));
        map.put("showMyQuestion", String.valueOf(showMyQuestion));
        map.put("entrance_id", "1");


        view = LayoutInflater.from(getContext()).inflate(R.layout.view_question_not_found, null);
        TextView textView = view.findViewById(R.id.txt);
        btnAddQuestion = view.findViewById(R.id.btn_add_question);
        btnShowAllQuestions = view.findViewById(R.id.btn_show_all_questions);
        String text = textView.getText().toString() + app.getEmojiByUnicode(0x1F614);
        textView.setText(text);
        btnAddQuestion.setOnClickListener(QuestionFragment.this);
        btnShowAllQuestions.setOnClickListener(QuestionFragment.this);

        app.l(lastId + "****" + lastSeenCount + "****" + carId + "****" + titleId + "****" + sortBy + "****" + showMyQuestion);
        Application.getApi().getQuestionWithMsg(map).enqueue(new Callback<QusetionWithMsg>() {
            @Override
            public void onResponse(Call<QusetionWithMsg> call, Response<QusetionWithMsg> response) {
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);
                if (response.body() != null && response.body().getQuestion().size() > 0) {
                    app.l(response.body().getMsg() + "!!!!!getQuestions");

                    if (response.body().getQuestion().get(0).getQ_id() == -2) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از خودروهای پیشنهادی کلیک کنید").show();
                        return;
                    }
                    if (response.body().getQuestion().get(0).getQ_id() == -3) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از موضوعات پیشنهادی کلیک کنید").show();
                        return;
                    }
                    if (response.body().getQuestion().get(0).getQ_id() == -4) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("لطفا روی یکی از خودروهای پیشنهادی و یکی از موضوعات پیشنهادی کلیک کنید").show();
                        return;
                    }


                    questions = response.body().getQuestion();
                    if (questions != null && questions.size() != 0) {
                        tmpQuestions.addAll(questions);
                        QuestionFragment.this.lastId = questions.get(questions.size() - 1).getQ_id();
                        QuestionFragment.this.lastSeenCount = questions.get(questions.size() - 1).getSeen_count();
                    } else {
                        if (questions != null) {
                            app.t("not found11");
                            isLoading = false;
                        /*resumeGooodListener(0, 0, 0, 0, is_stoke_active ? 1 : 0);
                        getGooods(0, 0, 0, 0, is_stoke_active ? 1 : 0);*/

                        }
                    }
                    adapter = new QuestionRecyclerAdapter(tmpQuestions, getActivity());
                    recyclerQuestion.setAdapter(adapter);

                } else {
                    app.l(response.body().getMsg()+"@@@@@@@@@@@");
                    sweetAlertDialogQuestionNotExist = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).hideConfirmButton()
                            .setCustomView(view);
                    sweetAlertDialogQuestionNotExist.show();
                }
            }

            @Override
            public void onFailure(Call<QusetionWithMsg> call, Throwable t) {
                app.l(t.getLocalizedMessage() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });

    }

    private void resumeGetQuestions(int lastIdN, int offset, int carId, int titleId, int sortBy, int showMyQuestion) {

        Map<String, String> map = new HashMap<>();
        map.put("route", "getQuestions");
        map.put("lastId", String.valueOf(lastIdN));
        map.put("offset", String.valueOf(offset));
        map.put("carId", String.valueOf(carId));
        map.put("titleId", String.valueOf(titleId));
        map.put("sortBy", String.valueOf(sortBy));
        map.put("showMyQuestion", String.valueOf(showMyQuestion));
        map.put("entrance_id", SharedPrefUtils.getStringData("entranceId"));
        app.l("resumeGetQuestions" + lastIdN + "***" + offset + "***" + carId + "***" + titleId + "***" + sortBy + "***" + showMyQuestion);
        Application.getApi().getQuestionWithMsg(map).enqueue(new Callback<QusetionWithMsg>() {
            @Override
            public void onResponse(Call<QusetionWithMsg> call, Response<QusetionWithMsg> response) {
                app.l(response.body().getMsg()+"!!!!!resumeGetQuestions");
                if (response.body() != null && response.body().getQuestion().size() == 0) {
                    app.l("f");
                    return;
                }
                List<Question> newQuestions = response.body().getQuestion();
                if (newQuestions != null) {

                    app.l(newQuestions.get(newQuestions.size() - 1).getQ_id() + "******");


                    QuestionFragment.this.lastId = newQuestions.get(newQuestions.size() - 1).getQ_id();
                    QuestionFragment.this.lastSeenCount = newQuestions.get(newQuestions.size() - 1).getSeen_count();
                }
                if (newQuestions != null) {
                    tmpQuestions.addAll(newQuestions);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;

            }

            @Override
            public void onFailure(Call<QusetionWithMsg> call, Throwable t) {

            }
        });


    }

    int offset;
    private void resumeQuestionListener(int carId, int titleId, int sortBy, int showMyQuestion) {
        isLoading = false;
        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tmpQuestions.size() - 1 && !isLoading) {
                    isLoading = true;
                    offset++;
                    app.l("ahmaddddd"+offset+lastId + "&&&&" + lastSeenCount + "&&&&" + carId + "&&&&" + titleId + "&&&&" + sortBy + "&&&&" + showMyQuestion);
                    resumeGetQuestions(lastId, offset, carId, titleId, sortBy, showMyQuestion);
                }
            }
        });
    }/**/

    private void modifyIds() {
        if (carQuestion.getText().toString().length() == 0 && selectedCarId == 0) {
            selectedCarId = 0;
        } else if (carQuestion.getText().toString().length() > 0 && !carQuestion.getText().toString().equals(getString(R.string.all_cars)) && selectedCarId == 0) {
            selectedCarId = -1;
        }
        if (titleQuestion.getText().toString().length() == 0 && selectedTitleId == 0) {
            selectedTitleId = 0;
        } else if (titleQuestion.getText().toString().length() > 0 && !titleQuestion.getText().toString().equals(getString(R.string.all_goods)) && selectedTitleId == 0) {
            selectedTitleId = -1;
        }
    }

    private int getMyQuestionValue() {
        if (isMyQuestionActive) return 1;
        else return 0;
    }
}
