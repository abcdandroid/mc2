package com.example.mechanic2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chaos.view.PinView;
import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.Smsbroadcastreciver;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AlertAction;
import com.example.mechanic2.interfaces.ConnectionErrorManager;
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

public class LoginActivity extends Activity implements View.OnClickListener {
    private TextInputEditText phoneInput;
    private PinView pinView;
    private TextView sendPhone;
    private TextView sendCode;
    private ImageView gear;
    Smsbroadcastreciver smsbroadcastreciver;
    static final int Codeintentforresult = 1;


    private TextView guideMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Start();
        setContentView(R.layout.activity_login);
        phoneInput = findViewById(R.id.phoneInput);
        pinView = findViewById(R.id.pinView);
        sendPhone = findViewById(R.id.sendPhone);
        guideMsg = findViewById(R.id.guide_msg);
        sendCode = findViewById(R.id.sendCode);
        gear = findViewById(R.id.gear2);
        Animation rotation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        gear.startAnimation(rotation);
        sendPhone.setOnClickListener(this);
        sendCode.setOnClickListener(this);

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, PrivacyActivity.class));
            }
        });

    }


    SweetAlertDialog sweetAlertDialogSendPhone;
    SweetAlertDialog connectionSweetAlertDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendPhone:
                sendPhone();
                break;
            case R.id.sendCode:
                if (pinView.getText() != null && pinView.getText().toString().length() != 4) {

                    showAlertDialog("کد فعال سازی معتبر نمی باشد.", new AlertAction() {
                        @Override
                        public void doOnClick(SweetAlertDialog sweetAlertDialog) {

                            pinView.setText("");
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                } else {

                    app.validateConnection(this, connectionSweetAlertDialog, new ConnectionErrorManager() {
                        @Override
                        public void doAction() {
                            sendPin();
                        }
                    });
                }
                break;
        }
    }

    private void sendPhone() {
        if (phoneInput.getText() != null && (phoneInput.getText().toString().length() != 11 || !phoneInput.getText().toString().matches("(09)\\d{9}"))) {
            showAlertDialog("لطفا شماره همراه خود را \nبه درستی وارد کنید.", new AlertAction() {
                @Override
                public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    phoneInput.setText("");
                }
            });

        } else {
            app.validateConnection(this, connectionSweetAlertDialog, new ConnectionErrorManager() {
                @Override
                public void doAction() {
                    sendPhoneMakeConnection();
                }
            });
        }
    }

    private void sendPhoneMakeConnection() {
        phoneInput.setEnabled(false);
        guideMsg.setText("در حال ارسال کد فعال سازی\n لطفا شکیبا باشید.");


        phoneInput.clearFocus();

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

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sweetAlertDialogSendCode.dismissWithAnimation();
                showAlertDialog("خطا در ارسال اطلاعات", new AlertAction() {
                    @Override
                    public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }
        });
    }

    private void showAlertDialog(String msg, AlertAction alertAction) {
        SweetAlertDialog sweetAlertDialogInvalidLength = new SweetAlertDialog(LoginActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.view_good_not_found, null);
        ((TextView) view.findViewById(R.id.txt)).setText(msg);
        view.findViewById(R.id.btn_show_all_goods).setVisibility(View.GONE);
        TextView txtOk = view.findViewById(R.id.txt_ok);
        txtOk.setText("خب");
        view.findViewById(R.id.btn_contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAction.doOnClick(sweetAlertDialogInvalidLength);
            }
        });
        sweetAlertDialogInvalidLength.setCustomView(view);
        sweetAlertDialogInvalidLength.hideConfirmButton();
        sweetAlertDialogInvalidLength.show();
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

                if (responseBody != null) {

                    if (responseBody.equals("registrationStep1")) {
                        SharedPrefUtils.saveData("phoneNumber", phoneNumber);

                        startActivity(new Intent(LoginActivity.this, NewEntranceActivity.class));
                    } else if (responseBody.equals("errorCode")) {

                        showAlertDialog("کد وارد شده صحیح نمی باشد.", new AlertAction() {
                            @Override
                            public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                    } else if (responseBody.equals("time out")) {

                        showAlertDialog("برای ورود به برنامه مجددا شماره خود را وارد کنید.", new AlertAction() {
                            @Override
                            public void doOnClick(SweetAlertDialog sweetAlertDialog) {

                                phoneInput.setActivated(true);
                                phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
                                pinView.setVisibility(View.GONE);
                                sendPhone.setVisibility(View.VISIBLE);
                                sendCode.setVisibility(View.GONE);

                            }
                        });
                    } else if (responseBody.contains("entranceId")) {

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);

                            String type = jsonObject.getString("type");
                            String entranceId = jsonObject.getString("entranceId");
                            String mobile = jsonObject.getString("mobile");

                            if (Integer.parseInt(type) == 1) {

                                String mechanicInfo = jsonObject.getString("mechanicInfo");
                                SharedPrefUtils.saveData("mechanicInfo", mechanicInfo);
                            }

                            SharedPrefUtils.saveData("entranceId", entranceId);
                            SharedPrefUtils.saveData("type", Integer.parseInt(type));
                            SharedPrefUtils.saveData("phoneNumber", mobile);

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        } catch (JSONException e) {

                            showAlertDialog("خطا در ارسال اطلاعات", new AlertAction() {
                                @Override
                                public void doOnClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                        }
                    }
                } else if (response.body() == null) {
                    showAlertDialog("خطا در ارسال اطلاعات", new AlertAction() {
                        @Override
                        public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sweetAlertDialogSendCode.dismissWithAnimation();

                showAlertDialog("خطا در ارسال اطلاعات", new AlertAction() {
                    @Override
                    public void doOnClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
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

        smsbroadcastreciver = new Smsbroadcastreciver();
        smsbroadcastreciver.smsbroadcastlistner = new Smsbroadcastreciver.smsbroadcastlistner() {
            @Override
            public void onsucess(Intent intent) {

                startActivityForResult(intent, Codeintentforresult);
            }

            @Override
            public void error() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);

        registerReceiver(smsbroadcastreciver, intentFilter);

    }

    void Start() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent("50004000142663").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Codeintentforresult) {


            if (resultCode == RESULT_OK && data != null) {


                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {

        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            pinView.setText(matcher.group(0));
            app.validateConnection(this, connectionSweetAlertDialog, new ConnectionErrorManager() {
                @Override
                public void doAction() {
                    sendPin();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        if (pinView.getVisibility() == View.VISIBLE) {
            phoneInput.setActivated(true);
            phoneInput.requestFocus();
            phoneInput.setEnabled(true);
            phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
            pinView.setVisibility(View.GONE);
            sendPhone.setVisibility(View.VISIBLE);
            sendCode.setVisibility(View.GONE);
            guideMsg.setText("لطفا شماره تلفن همراه \n خود را وارد نمایید.");
        } else
            super.onBackPressed();

    }

}


