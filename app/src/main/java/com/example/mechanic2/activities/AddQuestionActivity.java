package com.example.mechanic2.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri; 
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.TitleQuestionAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQuestionActivity extends Activity implements View.OnClickListener {

    public final String successQ =  "سوال شما با موفقیت ثبت شد و به زودی منتشر خواهد شد";
    public final String Q_TEXT = "q_text";
    public final String ENTRANCE_ID = "entranceId";
    public final String ENTRANCE_ID1 = "entrance_id";
    public final String UPLOAD = "upload";
    public final String ROUTE = "route";
    public final String sumbitting = "در حال ثبت سوال شما";
    public final String khob = "خب";
    public final String chooseT = "لطفا موضوع سوال خود را انتخاب کنید";
    public final String chooseC = "لطفا خودروی خود را انتخاب کنید";
    public final String longQ = "طول پرسش شما بیش از حد مجاز است.";
    public final String addQ = "لطفا پرسش خود را مطرح کنید";
    public final String FILE_NO = "fileNo";
    public final String PNG = ".png";
    ArrayList<ImageView> images = new ArrayList<>();
    List<MultipartBody.Part> files = new ArrayList<>();
    int current_image;

    Uri uri;
    Boolean[] fill_images = new Boolean[3];
    String[] address_images = new String[3];
    private TextView submitQuestion;
    private TextView cancelSend;
    MultipartBody.Part body0;
    MultipartBody.Part body1;
    MultipartBody.Part body2;
    private AutoCompleteTextView carType;
    private AutoCompleteTextView questionType;
    private TextInputLayout fieldQuestion;
    private EditText questionText;
    int selectedCarId = -1;
    int selectedTitleId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question);
        images.add(findViewById(R.id.image1));
        images.add(findViewById(R.id.imageProfile));
        images.add(findViewById(R.id.image3));
        submitQuestion = findViewById(R.id.submitQuestion);
        cancelSend = findViewById(R.id.cancelSend);

        carType = findViewById(R.id.carType);
        questionType = findViewById(R.id.questionType);
        fieldQuestion = findViewById(R.id.fieldQuestion);
        questionText = findViewById(R.id.questionText);
        fieldQuestion.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.ttf)));
        carType.setAdapter(new CarAutoCompleteAdapter(this, R.layout.item_show_auto_complete, false));
        questionType.setAdapter(new TitleQuestionAutoCompleteAdapter(this, R.layout.item_show_auto_complete, false));
        carType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCarId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });
        carType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedCarId = -1;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        questionType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTitleId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
            }
        });

        questionType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedTitleId = -1;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        images.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 0;
                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    showDeleteDialog();
                }
            }
        });


        images.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 1;

                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    showDeleteDialog();
                }


            }
        });


        images.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 2;

                if (!fill_images[current_image]) {
                    showDialog();
                } else {
                    showDeleteDialog();
                }


            }
        });
        submitQuestion.setOnClickListener(this);
        cancelSend.setOnClickListener(this);

        Arrays.fill(fill_images, false);

        Arrays.fill(address_images, "");


    }

    public void showDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("انتخاب تصویر")
                .setCancelText("از گالری")
                .setConfirmText("از دوربین")
                .showCancelButton(true)
                .setCancelButtonBackgroundColor(Color.parseColor("#FF0000")).setConfirmButtonBackgroundColor(Color.parseColor("#2EB543"))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(gallery_intent, "لطفا یک عکس را انتخاب کنید"), 2);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(sDialog -> {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File photoFile = createImageFile();
                    uri = Uri.fromFile(photoFile);

                    if (photoFile != null) {


                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, 1);
                    }

                    sDialog.dismissWithAnimation();
                })
                .show();







    }


    public void showDeleteDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("حذف تصویر")
                .setContentText("آیا مطمئن به حذف عکس هستید ؟")
                .setCancelText("بله")
                .setConfirmText("خیر")
                .showCancelButton(true)
                .setCancelClickListener(sDialog -> {

                    images.get(current_image).setImageResource(R.drawable.camera2);
                    fill_images[current_image] = false;


                    switch (current_image) {
                        case 0:
                            files.remove(body0);
                        case 1:
                            files.remove(body1);
                            break;
                        case 2:
                            files.remove(body2);
                            break;
                    }
                    sDialog.dismissWithAnimation();

                })
                .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();


    }

    private File createImageFile() {

        long timeStamp = System.currentTimeMillis();
        String imageFileName = getString(R.string.NAME_) + timeStamp;
        File storageDir = getExternalFilesDir("image");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    PNG,
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }


        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK) {


            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);


        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            uri = data.getData();

            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resultUri = result.getUri();
            images.get(current_image).setImageURI(resultUri);

            fill_images[current_image] = true;
            if (current_image == 0) {
                body0 = app.prepareImagePart(FILE_NO + current_image, resultUri);
                files.add(body0);
            }
            if (current_image == 1) {
                body1 = app.prepareImagePart(FILE_NO + current_image, resultUri);
                files.add(body1);
            }
            if (current_image == 2) {
                body2 = app.prepareImagePart(FILE_NO + current_image, resultUri);
                files.add(body2);
            }




        }

    }

    @Override
    public void onClick(View v) {
        Call<String> stringCall;
        switch (v.getId()) {
            case R.id.submitQuestion:
                if (questionText.getText().toString().length() < 10) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddQuestionActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setContentText(addQ);
                    sweetAlertDialog.setConfirmText(khob);
                    sweetAlertDialog.setConfirmButtonBackgroundColor(Color.GREEN);
                    sweetAlertDialog.show();
                    break;
                }
                if (questionText.getText().toString().length() > 200) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddQuestionActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setContentText(longQ);
                    sweetAlertDialog.setConfirmText(khob);
                    sweetAlertDialog.setConfirmButtonBackgroundColor(Color.GREEN);
                    sweetAlertDialog.show();
                    break;
                }
                if (selectedCarId == -1) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddQuestionActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setContentText(chooseC);
                    sweetAlertDialog.setConfirmText(khob);
                    sweetAlertDialog.setConfirmButtonBackgroundColor(Color.GREEN);
                    sweetAlertDialog.show();
                    break;
                }
                if (selectedTitleId == -1) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddQuestionActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setContentText(chooseT);
                    sweetAlertDialog.setConfirmText(khob);
                    sweetAlertDialog.setConfirmButtonBackgroundColor(Color.GREEN);
                    sweetAlertDialog.show();
                    break;
                }

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddQuestionActivity.this, SweetAlertDialog.PROGRESS_TYPE).setContentText(sumbitting);
                sweetAlertDialog.setCancelable(true);
                sweetAlertDialog.show();


                Map<String, String> map = new HashMap<>();
                map.put(ROUTE, UPLOAD);
                map.put(ENTRANCE_ID1, SharedPrefUtils.getStringData(ENTRANCE_ID));
                map.put(Q_TEXT, questionText.getText().toString().trim());
                map.put(getString(R.string.car_id), String.valueOf(selectedCarId));
                map.put(getString(R.string.title_id), String.valueOf(selectedTitleId));
                if (files.size() == 0) {
                    {stringCall = Application.getApi().sendQuestion(map);

                    }
                } else{
                    stringCall = Application.getApi().uploadMultipleFilesDynamic(map, files);

                }

                stringCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (response.body() != null) {
                            sweetAlertDialog.dismiss();
                            SweetAlertDialog sweetAlertDialogSubmit = new SweetAlertDialog(AddQuestionActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialogSubmit.setContentText(successQ).hideConfirmButton().show();
                            sweetAlertDialogSubmit.setCancelable(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sweetAlertDialogSubmit.dismissWithAnimation();
                                    finish();
                                }
                            }, 2000);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

                break;
            case R.id.cancelSend:
                finish();
                break;
        }
    }
}
