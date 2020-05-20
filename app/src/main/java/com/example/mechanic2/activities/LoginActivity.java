package com.example.mechanic2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.Smsbroadcastreciver;
import com.example.mechanic2.app.app;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText phoneInput;
    private PinView pinView;
    private TextView sendPhone;
    private TextView sendCode;
    private ImageView gear;
    Smsbroadcastreciver smsbroadcastreciver;
    static final int Codeintentforresult = 1;

    /*
     * moarefi mechanic
     * video haye amoozeshi
     * forushgah mahsulat
     * talar porsesh pasokh
     * sos     map
     * main page
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Start();
        setContentView(R.layout.activity_login);
        phoneInput = findViewById(R.id.phoneInput);
        pinView = findViewById(R.id.pinView);
        sendPhone = findViewById(R.id.sendPhone);
        sendCode = findViewById(R.id.sendCode);
        gear = findViewById(R.id.gear2);
        Animation rotation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        gear.startAnimation(rotation);


        sendPhone.setOnClickListener(this);
        sendCode.setOnClickListener(this);


    }


    SweetAlertDialog sweetAlertDialogSendPhone;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendPhone:

                if (phoneInput.getText() != null && phoneInput.getText().toString().length() != 11) {
                    SweetAlertDialog sweetAlertDialogInvalidLength = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialogInvalidLength.setTitleText("phone number is not valid").show();
                } else if (phoneInput.getText() != null && !phoneInput.getText().toString().matches("(09)\\d{9}")) {
                    SweetAlertDialog sweetAlertDialogInvalidPhone = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialogInvalidPhone.setTitleText("phone number is not valid").show();
                } else {


                    //http://drkamal3.com/Mechanic/index.php?route=sms&action=prepareCode&mobile=147
                    sweetAlertDialogSendPhone = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("لطفا شکیبا باشید");
                    sweetAlertDialogSendPhone.setCancelable(false);
                    sweetAlertDialogSendPhone.show();

                    Map<String, String> data = new HashMap<>();
                    data.put("route", "sms");
                    data.put("action", "prepareCode");
                    data.put("mobile", phoneInput.getText().toString().trim());

                    Application.getApi().sendPhone(data).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            sweetAlertDialogSendPhone.dismissWithAnimation();
                            phoneInput.setActivated(false);
                            phoneInput.setInputType(InputType.TYPE_NULL);
                            pinView.setVisibility(View.VISIBLE);
                            sendPhone.setVisibility(View.GONE);
                            sendCode.setVisibility(View.VISIBLE);

                            //app.l(response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            app.l(t.getLocalizedMessage());
                        }
                    });
                }
                break;
            case R.id.sendCode:
                if (pinView.getText() != null && pinView.getText().toString().length() != 4) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText("pin code is not valid").show();
                } else {
                    //https://drkamal3.com/Mechanic/index.php?route=sms&action=verifyCode&mobile=091232177&code=1622
                    sendPin();
                }
                break;
        }
    }


    SweetAlertDialog sweetAlertDialogSendCode;

    private void sendPin() {

        sweetAlertDialogSendCode = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("لطفا شکیبا باشید");
        sweetAlertDialogSendCode.setCancelable(false);
        sweetAlertDialogSendCode.show();
        Map<String, String> dataPin = new HashMap<>();
        dataPin.put("route", "sms");
        dataPin.put("action", "verifyCode");
        String phoneNumber = phoneInput.getText().toString().trim();
        dataPin.put("mobile", phoneNumber);
        dataPin.put("code", pinView.getText().toString());

        Application.getApi().sendPhone(dataPin).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                sweetAlertDialogSendCode.dismissWithAnimation();
                String responseBody = response.body();

                app.l("EE"+responseBody);
                if (responseBody != null) {
                    if (responseBody.equals("registrationStep1")) {
                        SharedPrefUtils.saveData("phoneNumber", phoneNumber);
                        startActivity(new Intent(LoginActivity.this, NewEntranceActivity.class));
                    } else if (responseBody.equals("errorCode")) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("error code").show();
                    } else if (responseBody.equals("time out")) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("time out").show();

                        phoneInput.setActivated(true);
                        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
                        pinView.setVisibility(View.GONE);
                        sendPhone.setVisibility(View.VISIBLE);
                        sendCode.setVisibility(View.GONE);

                    } else if (responseBody.contains("entranceId")) {
                        app.l("EE");
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            String type = jsonObject.getString("type");
                            String entranceId = jsonObject.getString("entranceId");
                            SharedPrefUtils.saveData("entranceId", entranceId);
                            SharedPrefUtils.saveData("type", type);
                            app.l("QQQ");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            app.l("WWW");
                        } catch (JSONException e) {
                            app.l(e.getLocalizedMessage());
                            SweetAlertDialog sweetAlertDialogSendCode2 = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error json parsing");
                            sweetAlertDialogSendCode2.show();
                        }
                    }
                } else if (response.body() == null) {
                    SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                    sweetAlertDialogSendCode.show();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                sweetAlertDialogSendCode.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiverSms();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsbroadcastreciver);
    }

    void registerBroadcastReceiverSms() {
        app.l("aa");
        smsbroadcastreciver = new Smsbroadcastreciver();
        smsbroadcastreciver.smsbroadcastlistner = new Smsbroadcastreciver.smsbroadcastlistner() {
            @Override
            public void onsucess(Intent intent) {
                app.l("register");
                startActivityForResult(intent, Codeintentforresult);
            }

            @Override
            public void error() {
                app.l("error");
            }
        };
        app.l("uu");
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        app.l("vv");
        registerReceiver(smsbroadcastreciver, intentFilter);
        app.l("ww");
    }

    void Start() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent("50004000142663").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                app.l("client A");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                app.l("client B");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        app.l("result A");

        if (requestCode == Codeintentforresult) {
            app.l("result B");

            if (resultCode == RESULT_OK && data != null) {
                app.l("result C");

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                app.l(message);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        app.l("opt");
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            pinView.setText(matcher.group(0));
            sendPin();
            ;
        }
    }
}
