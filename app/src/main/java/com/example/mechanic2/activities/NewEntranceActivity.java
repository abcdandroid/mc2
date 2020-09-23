package com.example.mechanic2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.nightonke.wowoviewpager.Animation.ViewAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoPositionAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoRotationAnimation;
import com.nightonke.wowoviewpager.Enum.Ease;
import com.nightonke.wowoviewpager.WoWoViewPager;
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewEntranceActivity extends AppCompatActivity {
    private USER_TYPE user_type;

    private ImageView gear;
    SweetAlertDialog sweetAlertDialogSendType;


    WoWoViewPager wowo;
    WoWoViewPager wowoGear;
    private ImageView imgMechanic;
    public int imgHeight;
    public int imgHeightNormal;
    private ImageView imgNormal;

    private LinearLayout txtNormal1;
    private LinearLayout txtNormal2;
    private LinearLayout txtNormal3;
    int wowoHeight;
    private LinearLayout txtMechanic1;
    private LinearLayout txtMechanic2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entrance);


        TextView submit = findViewById(R.id.submit);
        wowo = findViewById(R.id.wowo);
        wowoGear = findViewById(R.id.wowoGear);
        gear = findViewById(R.id.gear2);
        imgMechanic = findViewById(R.id.img_mechanic);
        imgNormal = findViewById(R.id.img_normal);
        txtNormal1 = findViewById(R.id.txt_normal_1);
        txtNormal2 = findViewById(R.id.txt_normal_2);
        txtNormal3 = findViewById(R.id.txt_normal_3);
        txtMechanic1 = findViewById(R.id.txt_mechanic_1);
        txtMechanic2 = findViewById(R.id.txt_mechanic_2);
        wowo.setAdapter(WoWoViewPagerAdapter.builder()
                .fragmentManager(getSupportFragmentManager())
                .count(2)
                .build());
        wowoGear.setAdapter(WoWoViewPagerAdapter.builder()
                .fragmentManager(getSupportFragmentManager())
                .count(2)
                .build());

        ViewAnimation viewAnimationRotateGear = new ViewAnimation(gear);
        viewAnimationRotateGear.add(WoWoRotationAnimation.builder().page(0)
                .fromZ(0).toZ(145).keepY(0).keepX(0).ease(Ease.InOutBack).build());

        user_type = USER_TYPE.MECHANIC;
        imgNormal.post(new Runnable() {
            @Override
            public void run() {
                ViewAnimation viewAnimationTransitImage1 = new ViewAnimation(imgMechanic);
                viewAnimationTransitImage1.add(WoWoPositionAnimation.builder().page(0).fromX(0).toX(3000).fromY(0).toY(-2000).ease(Ease.InOutElastic).build());
                ViewAnimation viewAnimationTransitImage2 = new ViewAnimation(imgNormal);
                viewAnimationTransitImage2.add(WoWoPositionAnimation.builder().page(0).fromX(-3000).toX(0).fromY(-2000).toY(0).ease(Ease.InOutElastic).build());
                ViewAnimation viewAnimationTransitTextNormal1 = new ViewAnimation(txtNormal1);
                viewAnimationTransitTextNormal1.add(WoWoPositionAnimation.builder().page(0).keepX((imgMechanic.getWidth() - txtNormal1.getWidth()) / 2).fromY(-imgMechanic.getHeight()).toY(imgMechanic.getHeight() - txtNormal1.getHeight() - txtNormal2.getHeight() / 2 + (wowo.getHeight() - imgMechanic.getHeight()) / 2).ease(Ease.InOutElastic).build());
                ViewAnimation viewAnimationTransitTextNormal2 = new ViewAnimation(txtNormal2);
                viewAnimationTransitTextNormal2.add(WoWoPositionAnimation.builder().page(0).keepX((imgMechanic.getWidth() - txtNormal2.getWidth()) / 2).fromY(-imgMechanic.getHeight()).toY(imgMechanic.getHeight() - txtNormal2.getHeight() / 2 + (wowo.getHeight() - imgMechanic.getHeight()) / 2).ease(Ease.InOutBack).build());
                ViewAnimation viewAnimationTransitTextNormal3 = new ViewAnimation(txtNormal3);
                viewAnimationTransitTextNormal3.add(WoWoPositionAnimation.builder().page(0).keepX((imgMechanic.getWidth() - txtNormal3.getWidth()) / 2).fromY(-imgMechanic.getHeight()).toY(imgMechanic.getHeight() - txtNormal2.getHeight() / 2 + txtNormal3.getHeight() + (wowo.getHeight() - imgMechanic.getHeight()) / 2).ease(Ease.InOutElastic).build());
                wowo.addAnimation(viewAnimationTransitImage1);
                wowo.addAnimation(viewAnimationTransitImage2);
                wowo.addAnimation(viewAnimationTransitImage2);


                ViewAnimation viewAnimationTransitTextMechanic1 = new ViewAnimation(txtMechanic1);
                viewAnimationTransitTextMechanic1.add(WoWoPositionAnimation.builder().page(0).keepX((imgMechanic.getWidth() - txtMechanic1.getWidth()) / 2).fromY(imgMechanic.getHeight() - txtMechanic1.getHeight() / 2 - txtMechanic2.getHeight() / 2 + (wowo.getHeight() - imgMechanic.getHeight()) / 2).toY(-imgMechanic.getHeight()).ease(Ease.InOutBack).build());
                ViewAnimation viewAnimationTransitTextMechanic2 = new ViewAnimation(txtMechanic2);
                viewAnimationTransitTextMechanic2.add(WoWoPositionAnimation.builder().page(0).keepX((imgMechanic.getWidth() - txtMechanic2.getWidth()) / 2).fromY(imgMechanic.getHeight() + txtMechanic2.getHeight() / 2 + (wowo.getHeight() - imgMechanic.getHeight()) / 2).toY(-imgMechanic.getHeight()).ease(Ease.InOutElastic).build());

                wowo.addAnimation(viewAnimationTransitTextNormal1).setDirection(WoWoViewPager.Horizontal);
                wowo.addAnimation(viewAnimationTransitTextNormal2);
                wowo.addAnimation(viewAnimationTransitTextNormal3);

                wowo.addAnimation(viewAnimationTransitTextMechanic1);
                wowo.addAnimation(viewAnimationTransitTextMechanic2);

                wowo.ready();
            }
        });


        wowoGear.addAnimation(viewAnimationRotateGear);
        wowoGear.setScrollDuration(5000);
        RadioGroup radioGroup1 = findViewById(R.id.radioGroupUserType);
        wowo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                wowo.setScrollDuration(4000);
                wowoGear.setScrollDuration(5000);
            }

            @Override
            public void onPageSelected(int position) {
                wowo.setScrollDuration(4000);
                wowoGear.setScrollDuration(5000);
                switch (position) {
                    case 0:
                        radioGroup1.check(R.id.mechanic_user);
                        wowoGear.setCurrentItem(0);
                        user_type = USER_TYPE.MECHANIC;
                        submit.setText("تکمیل اطلاعات");
                        break;
                    case 1:
                        user_type = USER_TYPE.NORMAL;
                        radioGroup1.check(R.id.normal_user);
                        wowoGear.setCurrentItem(1);
                        submit.setText("ورود به برنامه");
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                wowo.setScrollDuration(4000);
                wowoGear.setScrollDuration(5000);
            }
        });


        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mechanic_user:
                        user_type = NewEntranceActivity.USER_TYPE.MECHANIC;
                        wowo.setCurrentItem(0);
                        wowoGear.setCurrentItem(0);
                        submit.setText("تکمیل اطلاعات");
                        break;
                    case R.id.normal_user:
                        user_type = NewEntranceActivity.USER_TYPE.NORMAL;
                        wowo.setCurrentItem(1);
                        wowoGear.setCurrentItem(1);
                        submit.setText("ورود به برنامه");
                        break;
                    default:
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (user_type.equals(USER_TYPE.MECHANIC)) {
                    startActivity(new Intent(NewEntranceActivity.this, NewMechanicActivity2.class));
                } else if (user_type.equals(USER_TYPE.NORMAL)) {

                    sweetAlertDialogSendType = new SweetAlertDialog(NewEntranceActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialogSendType.setTitleText("لطفا شکیبا باشید.").setCancelable(false);
                    sweetAlertDialogSendType.show();

                    Map<String, String> map = new HashMap<>();
                    map.put("route", "sms");
                    map.put("action", "registration");
                    map.put("mobile", SharedPrefUtils.getStringData("phoneNumber").equals("-1") ? "11111111111" : SharedPrefUtils.getStringData("phoneNumber"));

                    map.put("type", String.valueOf(user_type.equals(USER_TYPE.NORMAL) ?0:1));

                    Application.getApi().getDataInString(map).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            sweetAlertDialogSendType.dismissWithAnimation();
                            String jsonString = response.body();
                            if (response.body() != null) {
                                JSONObject jsonObject;
                                String entranceId;
                                try {
                                    if (jsonString != null) {
                                        jsonObject = new JSONObject(jsonString);
                                        entranceId = jsonObject.getString("registerId");
                                        SharedPrefUtils.saveData("entranceId", entranceId);
                                        SharedPrefUtils.saveData("type", 0);
                                        startActivity(new Intent(NewEntranceActivity.this, MainActivity.class));
                                    }
                                } catch (JSONException e) {
                                    sweetAlertDialogSendType.dismissWithAnimation();
                                    SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewEntranceActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("خطا");
                                    sweetAlertDialogSendCode.show();
                                }

                            } else if (response.body() == null) {
                                sweetAlertDialogSendType.dismissWithAnimation();
                                SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewEntranceActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("خطا در برقراری ارتباط");
                                sweetAlertDialogSendCode.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            sweetAlertDialogSendType.dismissWithAnimation();
                            SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewEntranceActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("خطا در برقراری ارتباط");
                            sweetAlertDialogSendCode.show();
                        }
                    });

                }









            }
        });
    }

    public enum USER_TYPE {
        MECHANIC, NORMAL
    }


}
