package com.example.mechanic2.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Slide;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.FilterListeners;
import com.example.mechanic2.interfaces.KeyboardManger;
import com.google.android.material.textfield.TextInputLayout;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddQuestionFragment extends Fragment implements View.OnClickListener {
    private AutoCompleteTextView carType;
    private EditText questionText;
    private Button submitQuestion;
    private Button cancelSend;
    TextInputLayout fieldCarType;
    TextInputLayout fieldQuestion;

    private FragmentManager fragmentManager;
    public static KeyboardManger keyboardManger;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_add_question, container, false);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        return init(inflate);

    }

    public static AddQuestionFragment newInstance() {
        return new AddQuestionFragment();
    }


    private View init(View inflate) {
        carType = inflate.findViewById(R.id.carType);
        questionText = inflate.findViewById(R.id.questionText);
        submitQuestion = inflate.findViewById(R.id.submitQuestion);
        cancelSend = inflate.findViewById(R.id.cancelSend);
        fieldCarType = inflate.findViewById(R.id.fieldCarType);
        fieldQuestion = inflate.findViewById(R.id.fieldQuestion);

        keyboardManger= () -> new EditText[]{questionText, carType};

        CarAutoCompleteAdapter adapter = new CarAutoCompleteAdapter(Objects.requireNonNull(getContext()), android.R.layout.simple_expandable_list_item_1);
        carType.setAdapter(adapter);

        AdapterView.OnItemClickListener l = (parent, view, position, id) -> {
            fieldQuestion.setVisibility(View.VISIBLE);
            questionText.requestFocus();
            app.showKeyboard(questionText);
        };

        carType.setOnItemClickListener(l);

        carType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldQuestion.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        questionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int visibility = s.toString().length() > 1 ? View.VISIBLE : View.INVISIBLE;
                submitQuestion.setVisibility(visibility);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancelSend.setOnClickListener(this);
        submitQuestion.setOnClickListener(this);

        return inflate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelSend:
                //removeContentToFrameLayout();
                app.hideKeyboard(questionText);
                app.hideKeyboard(carType);
                break;
            case R.id.submitQuestion:
                sendQuestion();
                break;
        }
    }

    private void sendQuestion() {
        app.t("a");

    }


}
