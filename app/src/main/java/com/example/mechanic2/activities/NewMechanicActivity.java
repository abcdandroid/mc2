package com.example.mechanic2.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.TagMessage;
import com.example.mechanic2.models.Job;
import com.example.mechanic2.views.TagLayoutView;
import com.example.mechanic2.views.TagView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMechanicActivity extends Activity implements View.OnClickListener {
    private FloatingActionButton locationFab;
    private FloatingActionButton infoFab;
    private FloatingActionButton contactMeFab;
    private FloatingActionButton jobFab;


    private CircleImageView img;
    private EditText about;
    private TextView cancelAction;
    private SweetAlertDialog sweetAlertDialogAbout;
    private SweetAlertDialog sweetAlertDialogContactMe;
    private SweetAlertDialog sweetAlertDialogJobPicker;
    private TextView phone;
    private EditText address;
    private Uri resultUri;
    private NumberPicker jobPicker;
    private TextView jobTitle;
    private EditText name;
    private EditText storeName;


    private TextView nameMain;
    private TextView aboutMain;
    private TextView storeMain;


    private TextView phoneMain;
    private TextView addressMain;


    ArrayList<ImageView> images = new ArrayList<>();
    List<MultipartBody.Part> files = new ArrayList<>();
    int current_image;
    Uri uri;
    Boolean[] fill_images = new Boolean[4];
    String[] address_images = new String[4];
    MultipartBody.Part body0;
    MultipartBody.Part body1;
    MultipartBody.Part body2;
    MultipartBody.Part body3;
    List<Job> jobs = new ArrayList<>();

    private TextView submitNewMechanic;
    SweetAlertDialog sweetAlertDialogProgress;
    HashMap<String, Job> jobMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mechanic);
        Map<String, String> data = new HashMap<>();
        data.put("route", "getAllJobs");


        sweetAlertDialogProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialogProgress.setCancelable(false);
        sweetAlertDialogProgress.show();

        Application.getApi().getJobs(data).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.body() != null) {
                    sweetAlertDialogProgress.dismissWithAnimation();
                    jobs = response.body();
                    for (Job job : jobs) {
                        jobMap.put(job.getName(), job);
                    }
                } else sweetAlertDialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }


            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {

            }
        });

        locationFab = findViewById(R.id.locationFab);
        infoFab = findViewById(R.id.info);
        contactMeFab = findViewById(R.id.contactMe);
        jobTitle = findViewById(R.id.region_name);
        images.add(findViewById(R.id.image1));
        images.add(findViewById(R.id.image2));
        images.add(findViewById(R.id.image3));
        images.add(findViewById(R.id.imageProfile));
        jobFab = findViewById(R.id.jobFab);
        nameMain = findViewById(R.id.name_main);
        aboutMain = findViewById(R.id.about_main);
        storeMain = findViewById(R.id.store_main);
        about = findViewById(R.id.about);
        locationFab.setOnClickListener(this);
        infoFab.setOnClickListener(this);
        contactMeFab.setOnClickListener(this);
        jobFab.setOnClickListener(this);
        phoneMain = findViewById(R.id.phone_main);
        addressMain = findViewById(R.id.address_main);
        submitNewMechanic = findViewById(R.id.submit_new_mechanic);


        Arrays.fill(fill_images, false);

        Arrays.fill(address_images, "");

        if (SharedPrefUtils.getStringData("jobTitles") != null && SharedPrefUtils.getStringData("jobTitles").length() > 0)
            jobTitle.setText(SharedPrefUtils.getStringData("jobTitles"));
        if (SharedPrefUtils.getStringData("name") != null && SharedPrefUtils.getStringData("name").length() > 0)
            nameMain.setText(SharedPrefUtils.getStringData("name"));
        if (SharedPrefUtils.getStringData("about") != null && SharedPrefUtils.getStringData("about").length() > 0)
            aboutMain.setText(SharedPrefUtils.getStringData("about"));
        if (SharedPrefUtils.getStringData("storeName") != null && SharedPrefUtils.getStringData("storeName").length() > 0)
            storeMain.setText(SharedPrefUtils.getStringData("storeName"));
        if (SharedPrefUtils.getStringData("phone") != null && SharedPrefUtils.getStringData("phone").length() > 0)
            phoneMain.setText(SharedPrefUtils.getStringData("phone"));
        if (SharedPrefUtils.getStringData("address") != null && SharedPrefUtils.getStringData("address").length() > 0)
            addressMain.setText(SharedPrefUtils.getStringData("address"));


        for (int i = 0; i < 4; i++) {
            if (SharedPrefUtils.getStringData("imageNo" + i) != null && SharedPrefUtils.getStringData("imageNo" + i).length() > 0) {
                fill_images[i] = true;
                Uri parsedUri = Uri.parse(SharedPrefUtils.getStringData("imageNo" + i));
                images.get(i).setImageURI(parsedUri);

                if (i == 0) {
                    body0 = app.prepareImagePart("fileNo" + i, parsedUri);
                    files.add(body0);
                }
                if (i == 1) {
                    //img.setImageURI(parsedUri);
                    body1 = app.prepareImagePart("fileNo" + i, parsedUri);
                    files.add(body1);
                }
                if (i == 2) {
                    //img.setImageURI(parsedUri);
                    body2 = app.prepareImagePart("fileNo" + i, parsedUri);
                    files.add(body2);
                }
                if (i == 3) {
                    //img.setImageURI(parsedUri);
                    body3 = app.prepareImagePart("fileNo" + i, parsedUri);
                    files.add(body3);
                }

            }
        }

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

        images.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image = 3;

                if (!fill_images[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }


            }
        });

        submitNewMechanic.setOnClickListener(this);


    }

    public String jobSeparator(List<Job> list) {
        StringBuilder jobIds = new StringBuilder();
        if (list.size() == 1) {
            return String.valueOf(list.get(0).getId());
        }
        for (int i = 0; i < list.size(); i++) {
            String connector;

            if (i == list.size() - 1) connector = "";
            else connector = ",";
            jobIds.append(list.get(i).getId()).append(connector);
        }
        return jobIds.toString();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_new_mechanic:
                // app.l(files.size());

                String jobIds = "";
                String selectedJobTitles = SharedPrefUtils.getStringData("jobTitles");
                if (!selectedJobTitles.equals("-1") && selectedJobTitles.length() != 0) {
                    List<String> myList = new ArrayList<>(Arrays.asList(selectedJobTitles.split(" - ")));
                    List<Job> selectedJobsList = new ArrayList<>();
                    for (String jobTitle : myList) {
                        Job job = jobMap.get(jobTitle);
                        selectedJobsList.add(job);
                    }
                    jobIds = jobSeparator(selectedJobsList);
                }
                String addressFinal = SharedPrefUtils.getStringData("address");
                String nameFinal = SharedPrefUtils.getStringData("name");
                String storeNameFinal = SharedPrefUtils.getStringData("storeName");
                String phoneFinal = SharedPrefUtils.getStringData("phone");
                String aboutFinal = SharedPrefUtils.getStringData("about");
                String lttFinal = String.valueOf(SharedPrefUtils.getDoubleData("ltt"));
                String lngFinal = String.valueOf(SharedPrefUtils.getDoubleData("lng"));

                if (addressFinal == null || addressFinal.length() == 0 || nameFinal == null || nameFinal.length() == 0 || storeNameFinal == null || storeNameFinal.length() == 0 || phoneFinal == null || phoneFinal.length() == 0 || aboutFinal == null || aboutFinal.length() == 0 || lttFinal.length() == 0 || lngFinal.length() == 0) {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("fill all fields").show();
                } else {
                    Call<String> stringCall;
                    Map<String, String> map = new HashMap<>();
                    map.put("route", "addNewMechanic");
                    map.put("job_ids", jobIds);
                    map.put("region_id", "1");
                    map.put("address", addressFinal);
                    map.put("name", nameFinal);
                    map.put("store_name", storeNameFinal);
                    map.put("phone_number", phoneFinal);
                    map.put("about", aboutFinal);
                    map.put("x_location", lttFinal);
                    map.put("y_location", lngFinal);
                    if (files.size() == 0) {
                        stringCall = Application.getApi().sendQuestion(map);
                    } else
                        stringCall = Application.getApi().uploadMultipleFilesDynamic(map, files);

                    stringCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body() != null) {

                                JSONObject jsonObject;
                                String entranceId;
                                String jsonString = response.body();
                                try {
                                    jsonObject = new JSONObject(jsonString);
                                    entranceId = jsonObject.getString("entrance_id");
                                    SharedPrefUtils.saveData("entranceId", entranceId);
                                    startActivity(new Intent(NewMechanicActivity.this, MainActivity.class));
                                } catch (JSONException e) {
                                    SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                                    sweetAlertDialogSendCode.show();
                                }

                            } else {
                                SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                                sweetAlertDialogSendCode.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                            sweetAlertDialogSendCode.show();
                        }
                    });
                }

                break;
            case R.id.locationFab:
                startActivity(new Intent(NewMechanicActivity.this, MechanicLocationActivity.class));
                break;
            case R.id.info:
                @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.view_about_mechanic_sweet, null);

                img = view.findViewById(R.id.img);
                if (resultUri != null) img.setImageURI(resultUri);
                if (SharedPrefUtils.getStringData("imageNo" + 3) != null && SharedPrefUtils.getStringData("imageNo" + 3).length() > 0) {
                    img.setImageURI(Uri.parse(SharedPrefUtils.getStringData("imageNo" + 3)));
                }
                cancelAction = view.findViewById(R.id.cancel_action);
                this.name = view.findViewById(R.id.name);
                this.storeName = view.findViewById(R.id.store_name);
                this.about = view.findViewById(R.id.about);


                if (SharedPrefUtils.getStringData("name") != null && SharedPrefUtils.getStringData("name").length() > 0)
                    this.name.setText(SharedPrefUtils.getStringData("name"));
                if (SharedPrefUtils.getStringData("about") != null && SharedPrefUtils.getStringData("about").length() > 0)
                    this.about.setText(SharedPrefUtils.getStringData("about"));
                if (SharedPrefUtils.getStringData("storeName") != null && SharedPrefUtils.getStringData("storeName").length() > 0)
                    this.storeName.setText(SharedPrefUtils.getStringData("storeName"));

                sweetAlertDialogAbout = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("درباره خودت بنویس")
                        .setCustomView(view)
                        .setConfirmText("تایید اطلاعات")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String storeName = NewMechanicActivity.this.storeName.getText().toString();
                                String name = NewMechanicActivity.this.name.getText().toString();
                                String aboutString = NewMechanicActivity.this.about.getText().toString();
                                if (name.length() == 0 || storeName.length() == 0 || aboutString.length() == 0) {
                                    new SweetAlertDialog(NewMechanicActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("fill all fields").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    }).show();
                                } else {

                                    nameMain.setText(name);
                                    SharedPrefUtils.saveData("name", name);
                                    aboutMain.setText(aboutString);
                                    SharedPrefUtils.saveData("about", aboutString);
                                    storeMain.setText(storeName);
                                    SharedPrefUtils.saveData("storeName", storeName);
                                    sweetAlertDialog.dismissWithAnimation();
                                }


                            }
                        });
                sweetAlertDialogAbout.show();
                break;
            case R.id.contactMe:
                @SuppressLint("InflateParams") View view2 = LayoutInflater.from(this).inflate(R.layout.view_contact_mechanic_sweet, null);

                this.phone = view2.findViewById(R.id.phone);
                this.address = view2.findViewById(R.id.address);

                if (SharedPrefUtils.getStringData("phone") != null && SharedPrefUtils.getStringData("phone").length() > 0)
                    this.phone.setText(SharedPrefUtils.getStringData("phone"));
                if (SharedPrefUtils.getStringData("address") != null && SharedPrefUtils.getStringData("address").length() > 0)
                    this.address.setText(SharedPrefUtils.getStringData("address"));

                sweetAlertDialogContactMe = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("راه های ارتباطی")
                        .setCustomView(view2).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String phoneString = NewMechanicActivity.this.phone.getText().toString();
                                String addressString = NewMechanicActivity.this.address.getText().toString();

                                if (phoneString.length() == 0 || addressString.length() == 0) {
                                    new SweetAlertDialog(NewMechanicActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("fill all fields").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    }).show();
                                } else {
                                    phoneMain.setText(phoneString);
                                    SharedPrefUtils.saveData("phone", phoneString);
                                    addressMain.setText(addressString);
                                    SharedPrefUtils.saveData("address", addressString);
                                    sweetAlertDialog.dismissWithAnimation();
                                }

                            }
                        });
                sweetAlertDialogContactMe.show();
                break;
            case R.id.jobFab:
                @SuppressLint("InflateParams") View viewJobPicker = LayoutInflater.from(this).inflate(R.layout.view_job_picker_sweet, null);
                jobPicker = viewJobPicker.findViewById(R.id.job_picker);


                String[] itemsArray = new String[jobs.size()];
                List<String> jobNames = new ArrayList<>();
                for (Job j : jobs) {
                    jobNames.add(j.getName());
                }
                itemsArray = jobNames.toArray(itemsArray);

                jobPicker.setMinValue(0);
                jobPicker.setMaxValue(itemsArray.length - 1);
                jobPicker.setDisplayedValues(itemsArray);

                TagLayoutView tagParent = viewJobPicker.findViewById(R.id.tag_parent);
                String titles = SharedPrefUtils.getStringData("jobTitles");
                if (titles != null && titles.length() != 0) {
                    List<String> myList = new ArrayList<String>(Arrays.asList(titles.split(" - ")));
                    tagParent.addTags(myList);
                }

                tagParent.setTagMessage(new TagMessage() {
                    @Override
                    public void showError() {
                        new SweetAlertDialog(NewMechanicActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ّیش از سه عدد نمی توان امتخاب کرد").setConfirmText("باشه").show();
                    }
                });

                List<Integer> selectedJobsId = new ArrayList<>();

                sweetAlertDialogJobPicker = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("حرفه خودتو انتخاب کن")
                        .setCustomView(viewJobPicker)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                int pos = jobPicker.getValue();
                                Job selectedJob = jobs.get(pos);
                                TagView tagView = new TagView(NewMechanicActivity.this);
                                tagView.setTagTitle(selectedJob.getName());
                                tagParent.addTag(tagView);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                app.l(tagParent.getAllTags().size());
                                jobTitle.setText(arraySeparator(tagParent.getAllTags()));
                                SharedPrefUtils.saveData("jobTitles", arraySeparator(tagParent.getAllTags()));
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmText("افزودن حرفه").setCancelText("تایید")
                        .setConfirmButtonBackgroundColor(Color.GRAY);
                sweetAlertDialogJobPicker.show();
                break;

        }

    }


    public void show_dialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("انتخاب تصویر")/*
                .setContentText("Won't be able to recover this file!")*/
                .setCancelText("از گالری")
                .setConfirmText("از دوربین")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
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
                        app.l("AAA2");

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, 1);
                    }

                    sDialog.dismissWithAnimation();
                })
                .show();


    }


    public void show_delete_dialog() {


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
                            break;
                        case 1:
                            files.remove(body1);
                            break;
                        case 2:
                            files.remove(body2);
                            break;
                        case 3:
                            files.remove(body3);
                            break;
                    }
                    sDialog.dismissWithAnimation();
                    app.l(files.size());
                })
                .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();


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

            resultUri = result.getUri();

            SharedPrefUtils.saveData("imageNo" + current_image, resultUri.getPath());

            images.get(current_image).setImageURI(resultUri);

            fill_images[current_image] = true;
            if (current_image == 0) {
                body0 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body0);
            }
            if (current_image == 1) {
                //img.setImageURI(resultUri);
                body1 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body1);
            }
            if (current_image == 2) {
                //img.setImageURI(resultUri);
                body2 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body2);
            }
            if (current_image == 3) {
                //img.setImageURI(resultUri);
                body3 = app.prepareImagePart("fileNo" + current_image, resultUri);
                files.add(body3);
            }


            app.l(files.size());

        }

    }

    public String arraySeparator(List<String> list) {
        StringBuilder jobTitles = new StringBuilder();
        if (list.size() == 1) {
            return list.get(0);
        }
        for (int i = 0; i < list.size(); i++) {
            String connector;

            if (i == list.size() - 1) connector = "";
            else connector = " - ";
            jobTitles.append(list.get(i)).append(connector);
        }
        return jobTitles.toString();

    }


}
