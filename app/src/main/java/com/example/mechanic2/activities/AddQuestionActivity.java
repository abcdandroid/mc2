package com.example.mechanic2.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
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

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ImageView> images = new ArrayList<>();
    List<MultipartBody.Part> files = new ArrayList<>();
    int current_image;

    Uri uri;
    Boolean[] fill_images = new Boolean[3];
    String[] address_images = new String[3];
    Map<Integer, String> map = new HashMap<>();
    private Button submitQuestion;
    private Button cancelSend;
    MultipartBody.Part body0;
    MultipartBody.Part body1;
    MultipartBody.Part body2;
    private TextInputLayout fieldCarType;
    private AutoCompleteTextView carType;
    private TextInputLayout fieldQuestion;
    private EditText questionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question);
        images.add(findViewById(R.id.image1));
        images.add(findViewById(R.id.imageProfile));
        images.add(findViewById(R.id.image3));


        submitQuestion = findViewById(R.id.submitQuestion);
        cancelSend = findViewById(R.id.cancelSend);

        fieldCarType = findViewById(R.id.fieldCarType);
        carType = findViewById(R.id.carType);
        fieldQuestion = findViewById(R.id.fieldQuestion);
        questionText = findViewById(R.id.questionText);

        carType.setAdapter(new CarAutoCompleteAdapter(this, R.layout.item_show_auto_complete));

        images.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                current_image = 0;


                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });


        images.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 1;

                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });


        images.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 2;

                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });
        submitQuestion.setOnClickListener(this);
        cancelSend.setOnClickListener(this);

        Arrays.fill(fill_images, false);

        Arrays.fill(address_images, "");


    }

    public void show_dialog() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("انتخاب عکس از گالری");
        list.add("گرفتن عکس با دوربین");


        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestionActivity.this);

        builder.setAdapter(new ArrayAdapter<String>(AddQuestionActivity.this, R.layout.row, R.id.mytext, list)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        if (i == 0) {//gallery


                            Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(Intent.createChooser(gallery_intent, "لطفا یک عکس را انتخاب کنید"), 2);


                        } else {//camera
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

// Create the File where the photo should go
                            File photoFile = createImageFile();
                            uri = Uri.fromFile(photoFile);

                            if (photoFile != null) {
                                app.l("AAA2");

                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent, 1);
                                /* app.l("AAA3");
                                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                } else {
                                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        String packageName = resolveInfo.activityInfo.packageName;
                                        grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                }

                                startActivityForResult(takePictureIntent, 1);*/


                            }


                        }


                    }

                });


        builder.show();


    }


    public void show_delete_dialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestionActivity.this);

        builder.setMessage("آیا مطمئن به حذف عکس هستید ؟");

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                images.get(current_image).setImageResource(R.drawable.camera);
                fill_images[current_image] = false;

                switch (current_image) {
                    case 0:
                        files.remove(body0);
                        break;
                    case 1:
                        files.remove(body1);
                        break;
                    case 2:
                        files.remove(body2);
                        break;
                }

                app.l(files.size());

            }
        });

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();


    }

    private File createImageFile() {

        long timeStamp = System.currentTimeMillis();
        String imageFileName = "NAME_" + timeStamp;
        File storageDir = getExternalFilesDir("image");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".png",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        app.l("AAA1");
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        app.l("AAA4" + resultCode);

        if (requestCode == 1 && resultCode == RESULT_OK) {//camera8-
            app.l("AAA");

            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);
            app.l("AAA5");

        } else if (requestCode == 2 && resultCode == RESULT_OK) {//gallery

            uri = data.getData();

            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resultUri = result.getUri();
            images.get(current_image).setImageURI(resultUri);

            fill_images[current_image] = true;
            if (current_image == 0) {
                body0 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body0);
            }
            if (current_image == 1) {
                body1 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body1);
            }
            if (current_image == 2) {
                body2 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body2);
            }


            app.l(files.size());

        }

    }

    @Override
    public void onClick(View v) {
        Call<String> stringCall;
        switch (v.getId()) {
            case R.id.submitQuestion:
                Map<String, String> map = new HashMap<>();
                map.put("route", "upload");
                map.put("entrance_id", "1");
                map.put("q_text", questionText.getText().toString().trim());
                map.put("car_name", carType.getText().toString().trim());
                if (files.size() == 0) {
                    stringCall = Application.getApi().sendQuestion(map);
                } else
                    stringCall = Application.getApi().uploadMultipleFilesDynamic(map, files);

                stringCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        app.l("WWWWWWWWWWWWWWWWWWW");
                        if (response.body() != null)
                            app.l(response.body()+"RRRRRRRRRRRRRRRRR");
                        else app.l("nothing");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        app.l(t.getLocalizedMessage());
                    }
                });

                setResult(1);
                finish();
                break;
            case R.id.cancelSend:
                break;
        }
    }
}
