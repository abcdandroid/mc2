package com.example.mechanic2.fragments;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mechanic2.R;
import com.example.mechanic2.activities.AddQuestionActivity;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.MySpinnerAdapter;
import com.example.mechanic2.adapters.QuestionRecyclerAdapter;
import com.example.mechanic2.adapters.TitleQuestionAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AlertAction;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
import com.example.mechanic2.interfaces.IOnBackPressed;
import com.example.mechanic2.models.Car;
import com.example.mechanic2.models.Question;
import com.example.mechanic2.models.QusetionWithMsg;
import com.example.mechanic2.models.Title;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment implements View.OnClickListener, IOnBackPressed {
    private boolean x;
    private FloatingActionButton fab_add_layout;
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
    private ValueAnimator valueAnimator;


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
    SwipeRefreshLayout swipeRefreshLayout;
    MySpinnerAdapter filterSpinnerAdapter;


    private int selectedCarId;
    private int selectedTitleId;
    String filterIdInString = "1";


    private boolean isMyQuestionActive = false;

    SweetAlertDialog sweetAlertDialogErrorConnection;
    int i;
    private String detail;
    private Title title;
    private Car car;
    private SweetAlertDialog loadingData;

    public static QuestionFragment newInstance(String detail) {

        Bundle args = new Bundle();
        args.putString("detail", detail);
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    boolean fromConfig = false;
    AdapterView<?> parentAdapterView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_question, container, false);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        if (getArguments() != null) {

            detail = getArguments().getString("detail");
        }

        return init(inflate);
    }

    private View init(View inflate) {


        fab_add_layout = inflate.findViewById(R.id.fab_add_layout);
        recyclerQuestion = inflate.findViewById(R.id.recyclerQuestion);
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
        swipeRefreshLayout = inflate.findViewById(R.id.swipe_refresh_layout);

        resetCar.setOnClickListener(this);
        resetTitle.setOnClickListener(this);
        submitFilterParent.setOnClickListener(this);
        myQuestion.setOnClickListener(this);

        List<String> filterNames = new ArrayList<>();
        filterNames.add("جدیدترین سوالات");
        filterNames.add("پربازدیدترین سوالات");
        List<Integer> filterIds = new ArrayList<>();
        filterIds.add(1);
        filterIds.add(2);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("mpd2"));

        filterSpinnerAdapter = new MySpinnerAdapter(getContext(), R.layout.item_spinner, filterNames, filterIds, false);
        spinnerFilter.setAdapter(filterSpinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QuestionFragment.this.parentAdapterView = parent;
                i++;

                if (view != null) {
                    modifyIds();

                    View view1 = parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent()));
                    TextView myTextView = view1.findViewById(R.id.id_spinner);
                    filterIdInString = myTextView == null ? "1" : myTextView.getText().toString();
                    int loaderLimiter = fromConfig ? 2 : 1;

                    if (i > loaderLimiter) {


                        loading.setVisibility(View.VISIBLE);
                        submitFilter.setVisibility(View.INVISIBLE);
                        requestDataWithValidation();
                    }
                    if (fromConfig) {
                        fromConfig = false;
                        requestDataWithValidation();
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        CarAutoCompleteAdapter carAdapter = new CarAutoCompleteAdapter(getActivity(), R.layout.item_show_auto_complete, true);
        carQuestion.setAdapter(carAdapter);
        TitleQuestionAutoCompleteAdapter goodAdapter = new TitleQuestionAutoCompleteAdapter(Application.getContext(), R.layout.item_show_auto_complete, true);
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
                } else {
                    resetCar.setVisibility(View.VISIBLE);
                    if ((!carQuestion.getText().toString().equals("همه ماشین ها") || (car != null && !carQuestion.getText().toString().trim().equals(car.getName().trim())) && selectedCarId == 0)) {


                        selectedCarId = -1;
                    }
                }
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
                } else {
                    resetTitle.setVisibility(View.VISIBLE);
                    if ((!titleQuestion.getText().toString().equals("همه موضوعات") || (title != null && !titleQuestion.getText().toString().trim().equals(title.getName().trim()))) && selectedTitleId == 0) {

                        selectedTitleId = -1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerQuestion.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(Application.getContext(), android.R.anim.slide_in_left)));
        loading.setVisibility(View.VISIBLE);
        submitFilter.setVisibility(View.INVISIBLE);
        fab_add_layout.setOnClickListener(this);


        if (detail == null) {
            fromConfig = false;
        } else {
            fromConfig = true;
            configureQuestionWithDetail(detail);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                requestDataWithValidation();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        requestDataWithValidation();

        return inflate;

    }

    private void requestDataWithValidation() {
        app.validateConnection(getActivity(), sweetAlertDialogErrorConnection, new ConnectionErrorManager() {
            @Override
            public void doAction() {


                resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myQuestion:


                modifyIds();
                isMyQuestionActive = !isMyQuestionActive;
                filterSpinnerAdapter.disableAdapter(isMyQuestionActive);
                if (isMyQuestionActive) {
                    selectedTitleId = 0;
                    selectedCarId = 0;
                    carQuestion.setText("");
                    titleQuestion.setText("");

                    myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                    spinnerFilter.setEnabled(false);
                    spinnerFilter.setClickable(false);
                } else {
                    myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_white));
                    spinnerFilter.setEnabled(true);
                    spinnerFilter.setClickable(true);
                }

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                requestDataWithValidation();


                break;
            case R.id.btn_show_all_goods:
                sweetAlertDialogQuestionNotExist.dismissWithAnimation();
                myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_white));
                titleQuestion.setText("");
                carQuestion.setText("");
                spinnerFilter.setEnabled(true);
                spinnerFilter.setClickable(true);
                filterSpinnerAdapter.disableAdapter(false);
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
            case R.id.btn_contact_us:
                sweetAlertDialogQuestionNotExist.dismissWithAnimation();
                getActivity().startActivity(new Intent(getActivity(), AddQuestionActivity.class));
                break;
            case R.id.reset_title:
                resetTitle();
                requestDataWithValidation();
                break;
            case R.id.reset_car:
                resetCar();
                requestDataWithValidation();
                break;
            case R.id.submit_filter_parent:
                app.hideKeyboard(carQuestion);
                app.hideKeyboard(titleQuestion);
                modifyIds();


                requestDataWithValidation();
                break;
        }
        switch (v.getId()) {

            case R.id.fab_add_layout:
                startActivityForResult(new Intent(getActivity(), AddQuestionActivity.class), 1);
                break;
        }

    }

    private void resetTitle() {
        app.hideKeyboard(carQuestion);
        app.hideKeyboard(titleQuestion);
        modifyIds();
        if (!isMyQuestionActive) {
            spinnerFilter.setEnabled(true);
            spinnerFilter.setClickable(true);
        }
        filterSpinnerAdapter.disableAdapter(isMyQuestionActive);

        titleQuestion.setHint("موضوع سوالت چیه؟");
        titleQuestion.setText("");


        selectedTitleId = 0;
        filterSpinnerAdapter.disableAdapter(isMyQuestionActive);
    }

    private void resetCar() {
        app.hideKeyboard(carQuestion);
        app.hideKeyboard(titleQuestion);
        carQuestion.setText("");
        carQuestion.setHint("درباره چه ماشینی سوال داری؟");
        selectedCarId = 0;
        modifyIds();
        if (!isMyQuestionActive) {
            spinnerFilter.setEnabled(true);
            spinnerFilter.setClickable(true);
        }
        filterSpinnerAdapter.disableAdapter(isMyQuestionActive);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isLoading = false;
    }


    SweetAlertDialog sweetAlertDialogQuestionNotExist;

    View view;

    private RelativeLayout btnAddQuestion;
    private RelativeLayout btnShowAllQuestions;

    private void getQuestions(int carId, int titleId, int sortBy, int showMyQuestion) {
        if (loadingData != null || (loadingData != null && loadingData.isShowing()))
            loadingData.dismiss();

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
        map.put("entrance_id", SharedPrefUtils.getStringData("entranceId"));


        view = LayoutInflater.from(getContext()).inflate(R.layout.view_good_not_found, null);
        btnAddQuestion = view.findViewById(R.id.btn_contact_us);
        btnShowAllQuestions = view.findViewById(R.id.btn_show_all_goods);

        btnAddQuestion.setOnClickListener(QuestionFragment.this);
        btnShowAllQuestions.setOnClickListener(QuestionFragment.this);
        loadingData = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setContentText("در حال دریافت اطلاعات").setTitleText("لطفا شکیبا باشید.");
        loadingData.setCancelable(false);
        loadingData.show();

        app.enableDisableView(QuestionFragment.this.getView(), false);

        Application.getApi().getQuestionWithMsg(map).enqueue(new Callback<QusetionWithMsg>() {
            @Override
            public void onResponse(Call<QusetionWithMsg> call, Response<QusetionWithMsg> response) {
                loadingData.dismissWithAnimation();
                loading.setVisibility(View.INVISIBLE);
                submitFilter.setVisibility(View.VISIBLE);


                app.enableDisableView(QuestionFragment.this.getView(), true);

                if (response.body() != null && response.body().getQuestion().size() > 0) {


                    int id = response.body().getQuestion().get(0).getQ_id();
                    if (id == -2 || id == -3 || id == -4) {
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_ok_layout, null);
                        TextView txt = view.findViewById(R.id.txt);

                        if (id == -2) {
                            txt.setText("لطفا روی یکی از خودروهای پیشنهادی کلیک کنید.");
                        } else if (id == -3) {
                            txt.setText("لطفا روی یکی از موضوعات پیشنهادی کلیک کنید.");
                        } else if (id == -4) {
                            txt.setText("لطفا روی یکی از خودروها , موضوعات پیشنهادی کلیک کنید.");
                        }

                        RelativeLayout btnConfirm = view.findViewById(R.id.btn_confirm);
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setCustomView(view);

                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sweetAlertDialog.dismissWithAnimation();
                                carQuestion.setText("");
                                titleQuestion.setText("");
                                selectedCarId = 0;
                                selectedTitleId = 0;
                            }
                        });

                        sweetAlertDialog.hideConfirmButton();
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();

                        return;
                    }


                    questions = response.body().getQuestion();
                    if (questions != null && questions.size() != 0) {
                        tmpQuestions.addAll(questions);
                        QuestionFragment.this.lastId = questions.get(questions.size() - 1).getQ_id();
                        QuestionFragment.this.lastSeenCount = questions.get(questions.size() - 1).getSeen_count();
                    } else {

                        if (questions != null) {

                            isLoading = false;


                        }
                    }
                    Intent intent = new Intent("dataCount");
                    intent.putExtra("ref", "qsf");
                    LocalBroadcastManager.getInstance(QuestionFragment.this.getContext()).sendBroadcast(intent);
                    adapter = new QuestionRecyclerAdapter(tmpQuestions, getActivity());
                    recyclerQuestion.setAdapter(adapter);

                } else {
                    int i = Integer.parseInt(response.body().getMsg());
                    TextView textView = view.findViewById(R.id.txt);
                    TextView cancel_action = view.findViewById(R.id.cancel_action);
                    TextView txt_ok = view.findViewById(R.id.txt_ok);
                    LottieAnimationView lottieAnimationView = view.findViewById(R.id.warranty_lt);
                    lottieAnimationView.setAnimation(R.raw.exclamation_mark);
                    if (i != 0) {

                        textView.setText("شما هنوز سوالی نپرسیده اید.");
                    } else {

                        textView.setText("سوال مورد نظر شما هنوز پرسیده نشده است.");
                    }

                    cancel_action.setText("نمایش تمام سوالات");
                    txt_ok.setText("ایجاد پرسش جدید");

                    sweetAlertDialogQuestionNotExist = null;
                    sweetAlertDialogQuestionNotExist = new SweetAlertDialog(getContext()).hideConfirmButton()
                            .setCustomView(view);
                    sweetAlertDialogQuestionNotExist.setCancelable(false);
                    sweetAlertDialogQuestionNotExist.show();
                }
            }

            @Override
            public void onFailure(Call<QusetionWithMsg> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showAlertDialog("خطا در برقراری ارتباط", "تلاش مجدد", true, new AlertAction() {
                            @Override
                            public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                getQuestions(carId, titleId, sortBy, showMyQuestion);

                            }
                        });
                    }
                }, 1000);
            }
        });

    }

    private void showAlertDialog(String titleMsg, String okMsg, boolean isCancellable, AlertAction alertAction) {
        if (getContext() == null) {
            Toast.makeText(Application.getContext(), "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show();
            return;
        }
        SweetAlertDialog sweetAlertDialogInvalidLength = new SweetAlertDialog(getContext());
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

        Application.getApi().getQuestionWithMsg(map).enqueue(new Callback<QusetionWithMsg>() {
            @Override
            public void onResponse(Call<QusetionWithMsg> call, Response<QusetionWithMsg> response) {

                if (response.body() != null && response.body().getQuestion().size() == 0) {

                    return;
                }
                List<Question> newQuestions = response.body().getQuestion();
                if (newQuestions != null) {


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

                    resumeGetQuestions(lastId, offset, carId, titleId, sortBy, showMyQuestion);
                }
            }
        });
    }

    private void modifyIds() {
        String carQuestionText = carQuestion.getText().toString();
        if (carQuestionText.length() == 0 && selectedCarId == 0) {
            selectedCarId = 0;
        } else if ((carQuestionText.length() > 0 && (!carQuestionText.equals(getString(R.string.all_cars)) && (car != null && !carQuestionText.trim().equals(car.getName())))) && selectedCarId == 0) {
            selectedCarId = -1;
        }
        String titleQuestionText = titleQuestion.getText().toString();
        if (titleQuestionText.length() == 0 && selectedTitleId == 0) {
            selectedTitleId = 0;
        } else if ((titleQuestionText.length() > 0 && (!titleQuestionText.equals(getString(R.string.all_titles)) && (title != null && !titleQuestionText.trim().equals(title.getName())))) && selectedTitleId == 0) {
            selectedTitleId = -1;
        }
    }

    private int getMyQuestionValue() {
        if (isMyQuestionActive) return 1;
        else return 0;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String detail = intent.getStringExtra("detail");
            try {
                JSONObject jsonObject = new JSONObject(detail);
                Gson gson = new Gson();
                Car car = gson.fromJson(jsonObject.getString("car"), Car.class);
                Title title = gson.fromJson(jsonObject.getString("title"), Title.class);
                filterIdInString = jsonObject.getString("sortBy");
                int showMyQuestion = jsonObject.getInt("showMyQuestion");
                carQuestion.setText(car.getName());
                titleQuestion.setText(title.getName());
                spinnerFilter.setSelection(Integer.parseInt(filterIdInString) - 1);

                isMyQuestionActive = showMyQuestion == 1;
                filterSpinnerAdapter.disableAdapter(isMyQuestionActive);
                if (isMyQuestionActive) {
                    myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                    spinnerFilter.setEnabled(false);
                    spinnerFilter.setClickable(false);

                    selectedTitleId = 0;
                    selectedCarId = 0;
                    carQuestion.setText("");
                    titleQuestion.setText("");

                } else {
                    myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_white));
                    spinnerFilter.setEnabled(true);
                    spinnerFilter.setClickable(true);
                }

                loading.setVisibility(View.VISIBLE);
                submitFilter.setVisibility(View.INVISIBLE);
                app.validateConnection(getActivity(), sweetAlertDialogErrorConnection, new ConnectionErrorManager() {
                    @Override
                    public void doAction() {
                        resumeQuestionListener(car.getId(), title.getId(), Integer.parseInt(filterIdInString), getMyQuestionValue());
                        getQuestions(car.getId(), title.getId(), Integer.parseInt(filterIdInString), getMyQuestionValue());
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void configureQuestionWithDetail(String detail) {
        try {
            JSONObject jsonDetail = new JSONObject(detail);
            int sortBy = Integer.parseInt(jsonDetail.getString("sortBy"));
            Gson gson = new Gson();
            car = gson.fromJson(jsonDetail.getString("car"), Car.class);
            int isMyQuestionActiveInteger = jsonDetail.getInt("showMyQuestion");


            if (isMyQuestionActiveInteger == 1) {
                selectedTitleId = 0;
                selectedCarId = 0;
                carQuestion.setText("");
                titleQuestion.setText("");


                isMyQuestionActive = true;
                myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_active_stoke));
                spinnerFilter.setEnabled(false);
                spinnerFilter.setClickable(false);
            } else if (isMyQuestionActiveInteger == 0) {

                isMyQuestionActive = false;
                myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_white));
                spinnerFilter.setEnabled(true);
                spinnerFilter.setClickable(true);
            }
            filterSpinnerAdapter.disableAdapter(isMyQuestionActive);


            JSONObject jsonTitle = new JSONObject(jsonDetail.getString("title"));
            title = new Title(jsonTitle.getString("name"), jsonTitle.getInt("id"));
            carQuestion.setText(car.getName());
            selectedCarId = car.getId();
            titleQuestion.setText(title.getName());
            selectedTitleId = title.getId();


            spinnerFilter.setSelection(sortBy);
            if (sortBy == 0) {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressed() {
        modifyIds();
        if (selectedCarId != 0 || selectedTitleId != 0 || spinnerFilter.getSelectedItemPosition() != 0 || getMyQuestionValue() != 0) {
            app.hideKeyboard(carQuestion);
            app.hideKeyboard(titleQuestion);
            carQuestion.setText("");
            carQuestion.setHint("درباره چه ماشینی سوال داری؟");
            selectedCarId = 0;

            isMyQuestionActive = false;

            spinnerFilter.setEnabled(true);
            spinnerFilter.setClickable(true);
            filterSpinnerAdapter.disableAdapter(isMyQuestionActive);

            titleQuestion.setHint("موضوع سوالت چیه؟");
            titleQuestion.setText("");


            selectedTitleId = 0;
            filterSpinnerAdapter.disableAdapter(isMyQuestionActive);

            myQuestion.setBackground(getResources().getDrawable(R.drawable.btn_white));

            spinnerFilter.setSelection(0);

            app.validateConnection(getActivity(), sweetAlertDialogErrorConnection, new ConnectionErrorManager() {
                @Override
                public void doAction() {
                    resumeQuestionListener(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                    getQuestions(selectedCarId, selectedTitleId, Integer.parseInt(filterIdInString), getMyQuestionValue());
                }
            });

            return true;
        }

        return false;
    }
}
