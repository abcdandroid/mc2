package com.example.mechanic2.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.mechanic2.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class NewSampleActivity extends Activity /*implements View.OnClickListener*/ {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sample_activity);


        Button viewById = findViewById(R.id.custom_btn_colors_test);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(NewSampleActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Custom view")
                        .setCancelButton("red", null)
                        .setCancelButtonBackgroundColor(Color.RED)
                        .setNeutralButton("cyan", null)
                        .setNeutralButtonBackgroundColor(Color.CYAN)
                        .setConfirmButton("blue", null)
                        .setConfirmButtonBackgroundColor(Color.BLUE)
                        .show();
            }
        });

    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
*//*            case R.id.basic_test:
                SweetAlertDialog sd = new SweetAlertDialog(this);
                sd.setCancelable(true);
                sd.setCanceledOnTouchOutside(true);
                sd.setContentText("Here's a message");
                sd.show();
                break;
            case R.id.basic_test_without_buttons:
                SweetAlertDialog sd2 = new SweetAlertDialog(this);
                sd2.setCancelable(true);
                sd2.setCanceledOnTouchOutside(true);
                sd2.setContentText("Here's a message");
                sd2.hideConfirmButton();
                sd2.show();
                break;
            case R.id.under_text_test:
                new SweetAlertDialog(this)
                        .setTitleText("Title")
                        .setContentText("It's pretty, isn't it?")
                        .show();
                break;
            case R.id.styled_text_and_stroke:
                new SweetAlertDialog(this)
                        .setTitleText("<font color='red'>Red</font> title")
                        .setContentText("Big <font color='green'>green </font><b><i> bold</i></b>")
                        .setContentTextSize(21)
                        .setStrokeWidth(2)
                        .show();
                break;
            case R.id.error_text_test:
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();
                break;
            case R.id.success_text_test:
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("You clicked the button!")
                        .show();
                break;
            case R.id.warning_confirm_test:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setCancelButton("Yes, delete it!", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                // reuse previous dialog instance
                                sweetAlertDialog.setTitleText("Deleted!")
                                        .setContentText("Your imaginary file has been deleted!")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
                break;
            case R.id.warning_cancel_test:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setCancelText("No, cancel pls!")
                        .setConfirmText("Yes, delete it!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance, keep widget user state, reset them if you need
                                sDialog.setTitleText("Cancelled!")
                                        .setContentText("Your imaginary file is safe :)")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                // or you can new a SweetAlertDialog to show
                               *//**//* sDialog.dismiss();
                                new SweetAlertDialog(SampleActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Cancelled!")
                                        .setContentText("Your imaginary file is safe :)")
                                        .setConfirmText("OK")
                                        .show();*//**//*
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Deleted!")
                                        .setContentText("Your imaginary file has been deleted!")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
                break;
            case R.id.custom_img_test:
                new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Sweet!")
                        .setContentText("Here's a custom image.")
                        .setCustomImage(R.drawable.custom_img)
                        .show();
                break;
            case R.id.progress_dialog:
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(800 * 7, 800) {
                    public void onTick(long millisUntilFinished) {
                        // you can change the progress bar color by ProgressHelper every 800 millis
                        i++;
                        switch (i) {
                            case 0:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                break;
                            case 2:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                break;
                            case 6:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                        }
                    }

                    public void onFinish() {
                        i = -1;
                        pDialog.setTitleText("Success!")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                }.start();
                break;

            case R.id.neutral_btn_test:
                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Title")
                        .setContentText("Three buttons dialog")
                        .setConfirmText("Confirm")
                        .setCancelText("Cancel")
                        .setNeutralText("Neutral")
                        .show();
                break;

            case R.id.disabled_btn_test:
                final SweetAlertDialog disabledBtnDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Title")
                        .setContentText("Disabled button dialog")
                        .setConfirmText("OK")
                        .setCancelText("Cancel")
                        .setNeutralText("Neutral");

                disabledBtnDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        disabledBtnDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(false);
                    }
                });
                disabledBtnDialog.show();
                break;

            case R.id.dark_style:
                if (((CheckBox) v).isChecked()) {
                    SweetAlertDialog.DARK_STYLE = true;
                } else {
                    SweetAlertDialog.DARK_STYLE = false;
                }
                break;

            case R.id.custom_view_test:
                final EditText editText = new EditText(this);
                final CheckBox checkBox = new CheckBox(this);
                editText.setText("Some edit text");
                checkBox.setChecked(true);
                checkBox.setText("Some checkbox");

                if (SweetAlertDialog.DARK_STYLE) {
                    editText.setTextColor(Color.WHITE);
                    checkBox.setTextColor(Color.WHITE);
                }

                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editText);
                linearLayout.addView(checkBox);

                SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Custom view")
                        .hideConfirmButton();

                dialog.setCustomView(linearLayout);
                dialog.show();
                break;*//*
            case R.id.custom_btn_colors_test:
                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Custom view")
                        .setCancelButton("red", null)
                        .setCancelButtonBackgroundColor(Color.RED)
                        .setNeutralButton("cyan", null)
                        .setNeutralButtonBackgroundColor(Color.CYAN)
                        .setConfirmButton("blue", null)
                        .setConfirmButtonBackgroundColor(Color.BLUE)
                        .show();
                break;

        }
    }*/
}
