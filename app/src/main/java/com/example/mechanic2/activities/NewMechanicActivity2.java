package com.example.mechanic2.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.CarAutoCompleteAdapter;
import com.example.mechanic2.adapters.JobAutoCompleteAdapter;
import com.example.mechanic2.adapters.RegionAutoCompleteAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.AddJobClickListener;
import com.example.mechanic2.models.Warranty;
import com.example.mechanic2.views.JobRow;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMechanicActivity2 extends Activity implements View.OnClickListener /*implements View.OnClickListener*/ {


    private Uri resultUri;


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
    private LinearLayout jobContainer;


    private RelativeLayout parent;
    private LinearLayout picHint;
    private LinearLayout lytForm;
    private LinearLayout jobHint;
    private JobRow jobRow1;
    private LinearLayout mapHint;
    private TextView whatsUpIntent;
    private TextView submitNewMechanic;
    private ArrayList<Integer> listIds = new ArrayList<>();
    private ArrayList<String> listNames = new ArrayList<>();
    private AutoCompleteTextView regionNameAuto;
    private AppCompatEditText regionDesc;
    private View mapThumbnailFake;
    private MapView mMapView;
    private int selectedRegionId;
    private String selectedRegionName;
    private TextView regionId;
    private TextView lttLngValue;

    private AppCompatEditText mechanicName;
    private AppCompatEditText mechanicStoreName;
    private AppCompatEditText mechanicPhone;
    private AppCompatEditText mechanicAbout;

    MyLocationNewOverlay mLocationOverlay;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mechanic_2);


        parent = findViewById(R.id.parent);
        picHint = findViewById(R.id.pic_hint);
        lytForm = findViewById(R.id.lyt_form);
        jobHint = findViewById(R.id.job_hint);
        jobContainer = findViewById(R.id.job_container);
        jobRow1 = findViewById(R.id.job_row_1);
        regionNameAuto = findViewById(R.id.region_name_auto);
        regionId = findViewById(R.id.region_id);
        mapHint = findViewById(R.id.map_hint);
        whatsUpIntent = findViewById(R.id.whats_up_intent);
        submitNewMechanic = findViewById(R.id.submit_new_mechanic);
        regionDesc = findViewById(R.id.region_desc);
        mMapView = findViewById(R.id.map_thumbnail);/**/
        mapThumbnailFake = findViewById(R.id.map_thumbnail_fake);
        lttLngValue = findViewById(R.id.ltt_lng_value);
        mapThumbnailFake.setOnClickListener(this);

        mechanicName = findViewById(R.id.mechanic_name);
        mechanicStoreName = findViewById(R.id.mechanic_store_name);
        mechanicPhone = findViewById(R.id.mechanic_phone);
        mechanicAbout = findViewById(R.id.mechanic_about);


        images.add(findViewById(R.id.image1));
        images.add(findViewById(R.id.image2));
        images.add(findViewById(R.id.image3));
        images.add(findViewById(R.id.imageProfile));
        jobContainer = findViewById(R.id.job_container);


        mMapView.setMultiTouchControls(false);

        mapController = mMapView.getController();
        mapController.setZoom(14f);
        if (SharedPrefUtils.getDoubleData("ltt") == 0 && SharedPrefUtils.getDoubleData("lng") == 0) {
            SharedPrefUtils.saveData("ltt", 36.3201);
            SharedPrefUtils.saveData("lng", 59.5896);
        }
        GeoPoint startPoint = new GeoPoint(SharedPrefUtils.getDoubleData("ltt"), SharedPrefUtils.getDoubleData("ltt"));
        mapController.setCenter(startPoint);
        if (SharedPrefUtils.getDoubleData("ltt") != 0 && SharedPrefUtils.getDoubleData("ltt") != 0) {
            ArrayList<OverlayItem> overlayArray = new ArrayList<>();
            OverlayItem mapItem = new OverlayItem("", "", startPoint);
            final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
            mapItem.setMarker(marker);
            overlayArray.add(mapItem);
            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
            mMapView.getOverlays().add(anotherItemizedIconOverlay);
            mMapView.getOverlays().remove(mLocationOverlay);
            mMapView.invalidate();
        }
        /* */


        LocalBroadcastManager.getInstance(this).registerReceiver(jobRowReceiver, new IntentFilter("fromJobRow"));
/*
                SharedPrefUtils.saveData("jobIds", jobSeparator(listIds));
                SharedPrefUtils.saveData("jobNames", jobSeparator(listNames));*/

        if (!SharedPrefUtils.getStringData("jobNames").equals("-1") && !SharedPrefUtils.getStringData("jobIds").equals("-1")) {
            List<String> jobNames = Arrays.asList(SharedPrefUtils.getStringData("jobNames").split(","));
            List<String> jobIds = Arrays.asList(SharedPrefUtils.getStringData("jobIds").split(","));
            if (jobNames.size() > 0) jobContainer.removeView(jobRow1);
            for (int i = 0; i < jobNames.size(); i++) {
                JobRow jobRow = new JobRow(this);
                jobRow.setText(jobNames.get(i));
                jobRow.setIdJobRow(jobIds.get(i));
                jobRow.setRemoveFocus(true);
                jobContainer.addView(jobRow);
                jobIconManager();
            }
        }


        if (!SharedPrefUtils.getStringData("regionName").equals("-1") && !SharedPrefUtils.getStringData("regionId").equals("-1")) {
            regionNameAuto.setText(SharedPrefUtils.getStringData("regionName"));
            regionId.setText(SharedPrefUtils.getStringData("regionId"));
        }


        if (!SharedPrefUtils.getStringData("mechanicName").equals("-1")) {
            mechanicName.setText(SharedPrefUtils.getStringData("mechanicName"));
        }

        if (!SharedPrefUtils.getStringData("mechanicStoreName").equals("-1")) {
            mechanicStoreName.setText(SharedPrefUtils.getStringData("mechanicStoreName"));
        }

        if (!SharedPrefUtils.getStringData("mechanicPhone").equals("-1")) {
            mechanicPhone.setText(SharedPrefUtils.getStringData("mechanicPhone"));
        }


        if (!SharedPrefUtils.getStringData("mechanicAbout").equals("-1")) {
            mechanicAbout.setText(SharedPrefUtils.getStringData("mechanicAbout"));
        }

        if (!SharedPrefUtils.getStringData("regionDesc").equals("-1")) {
            regionDesc.setText(SharedPrefUtils.getStringData("regionDesc"));
        }


        String regionDesc = this.regionDesc.getText().toString();
        SharedPrefUtils.saveData("regionDesc", regionDesc);


        JobRow.addJobClickListener = new AddJobClickListener() {
            @Override
            public void onClick(View view) {
                int jobLimiter = 5;
                int jobId;
                String idJobRow = ((JobRow) view.getParent().getParent()).getIdJobRow();
                if (idJobRow.length() > 0)
                    jobId = Integer.parseInt(idJobRow);
                else jobId = -2;
                String jobName = new String(((JobRow) view.getParent().getParent()).getText());

                if (view.getTag() != null && view.getTag().equals("remove")) {
                    jobContainer.removeView(((LinearLayout) view.getParent().getParent()));
                    jobIconManager();
                    if (jobId != -2) {
                        listIds.remove(Integer.valueOf(jobId));
                        listNames.remove(jobName);
                    }
                    return;
                }
                if (view.getTag() != null && view.getTag().equals("removeByLimit")) {
                    jobContainer.removeView(((LinearLayout) view.getParent().getParent()));
                    jobIconManager();
                    listIds.remove(Integer.valueOf(jobId));
                    listNames.remove(jobName);
                    return;
                }


                JobRow jobRow = new JobRow(getApplicationContext());
                jobContainer.addView(jobRow);
                jobIconManager();


                if (jobContainer.getChildCount() >= jobLimiter) {
                    LinearLayout linearLayout = ((LinearLayout) jobContainer.getChildAt(jobLimiter - 1));
                    ImageView jobIcon = linearLayout.findViewById(R.id.job_add_remove_icon);
                    jobIcon.setTag("removeByLimit");
                    jobIcon.setImageDrawable(getDrawable(R.drawable.remove_job));
                }

            }


        };


        Arrays.fill(fill_images, false);

        Arrays.fill(address_images, "");


        for (int i = 0; i < 4; i++) {
            String key = "imageNo" + i;
            if (!SharedPrefUtils.getStringData(key).equals("-1") && SharedPrefUtils.getStringData("imageNo").length() > 0) {
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
        whatsUpIntent.setOnClickListener(this);
        RegionAutoCompleteAdapter regionAutoCompleteAdapter = new RegionAutoCompleteAdapter(this, R.layout.item,false);
        regionNameAuto.setAdapter(regionAutoCompleteAdapter);
        regionNameAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRegionId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
                selectedRegionName = ((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.title)).getText().toString();
                regionId.setText(String.valueOf(selectedRegionId));
            }
        });
        regionNameAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                regionId.setText(String.valueOf(""));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void jobIconManager() {
        for (int i = 0; i < jobContainer.getChildCount(); i++) {
            LinearLayout linearLayout = ((LinearLayout) jobContainer.getChildAt(i));
            ImageView jobIcon = (ImageView) linearLayout.findViewById(R.id.job_add_remove_icon);

            if (i == jobContainer.getChildCount() - 1) {
                jobIcon.setTag("add");
                jobIcon.setImageDrawable(getDrawable(R.drawable.add_job));
            } else {
                jobIcon.setTag("remove");
                jobIcon.setImageDrawable(getDrawable(R.drawable.remove_job));
            }
        }
    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_new_mechanic:
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
                                    startActivity(new Intent(NewMechanicActivity2.this, MainActivity.class));
                                } catch (JSONException e) {
                                    SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                                    sweetAlertDialogSendCode.show();
                                }

                            } else {
                                SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                                sweetAlertDialogSendCode.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection");
                            sweetAlertDialogSendCode.show();
                        }
                    });
                }

                break;




        }

    }*/


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.getOverlays().clear();
        GeoPoint startPoint = new GeoPoint(SharedPrefUtils.getDoubleData("ltt"), SharedPrefUtils.getDoubleData("lng"));
        mapController.setCenter(startPoint);
        if (SharedPrefUtils.getDoubleData("ltt") != 0 && SharedPrefUtils.getDoubleData("lng") != 0) {
            ArrayList<OverlayItem> overlayArray = new ArrayList<>();
            OverlayItem mapItem = new OverlayItem("", "", startPoint);
            final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
            mapItem.setMarker(marker);
            overlayArray.add(mapItem);
            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
            mMapView.getOverlays().add(anotherItemizedIconOverlay);
            mMapView.getOverlays().remove(mLocationOverlay);
            mMapView.invalidate();
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

                    images.get(current_image).setImageResource(current_image == 3 ? R.drawable.mechanic_face2 : R.drawable.mechanic_store_preview);
                    fill_images[current_image] = false;
                    SharedPrefUtils.removeDataByArgument("imageNo" + current_image);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_new_mechanic:
                app.l("sdf");
                listIds = new ArrayList<>();
                listNames = new ArrayList<>();
                for (int i = 0; i < jobContainer.getChildCount(); i++) {
                    JobRow jobRow = (JobRow) jobContainer.getChildAt(i);
                    String idJobRow = jobRow.getIdJobRow();
                    if (idJobRow.length() > 0) {
                        listIds.add(Integer.parseInt(idJobRow));
                        listNames.add(jobRow.getText());
                    }
                }


                String mechanicName = this.mechanicName.getText().toString();
                if (mechanicName.length() >= 3)
                    SharedPrefUtils.saveData("mechanicName", mechanicName);
                else {

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("لطفا نام  خود را به صورت کامل وارد نمایید.");
                    sweetAlertDialog.setConfirmText("خب");
                    sweetAlertDialog.show();
                    return;
                }


                String mechanicStoreName = this.mechanicStoreName.getText().toString();
                if (mechanicStoreName.length() > 3)
                    SharedPrefUtils.saveData("mechanicStoreName", mechanicStoreName);
                else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("لطفا نام مغازه خود را به صورت کامل وارد نمایید.");
                    sweetAlertDialog.setConfirmText("خب");
                    sweetAlertDialog.show();
                    return;
                }

                String mechanicPhone = this.mechanicPhone.getText().toString();
                if (mechanicPhone.matches("^09\\d{9}$"))
                    SharedPrefUtils.saveData("mechanicPhone", mechanicPhone);


                String jobNames = jobSeparator(listNames);
                String jobIdsText = jobSeparator(listIds);

                if (jobIdsText.length() != 0 && jobNames.length() != 0) {
                    SharedPrefUtils.saveData("jobNames", jobNames);
                    SharedPrefUtils.saveData("jobIds", jobIdsText);
                } else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("لطفا برای خود حداقل یکی از تخصص های پیشنهادی را انتخاب کنید.");
                    sweetAlertDialog.setConfirmText("خب");
                    sweetAlertDialog.show();
                    return;
                }


                String regionNameText = regionNameAuto.getText().toString();
                String regionIdText = regionId.getText().toString();

                if (regionNameText.length() != 0 && regionIdText.length() != 0) {
                    SharedPrefUtils.saveData("regionName", regionNameText);
                    SharedPrefUtils.saveData("regionId", regionIdText);
                } else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("لطفا یکی از منطقه های پیشنهادی را برای منطقه فعالیت خود انتخاب کنید.");
                    sweetAlertDialog.setConfirmText("خب");
                    sweetAlertDialog.show();
                    return;
                }


                String regionDesc = Objects.requireNonNull(this.regionDesc.getText()).toString();
                if (regionDesc.length() >= 10)
                    SharedPrefUtils.saveData("regionDesc", regionDesc);
                else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("لطفا آدرس خود را دقیق وارد کنید.");
                    sweetAlertDialog.setConfirmText("خب");
                    sweetAlertDialog.show();
                    return;
                }


                String mechanicAbout = Objects.requireNonNull(this.mechanicAbout.getText()).toString();
                SharedPrefUtils.saveData("mechanicAbout", mechanicAbout);
                 /*  if (mechanicAbout.length() >= 10)
             else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("لطفا بتوضیحات کاملتری ارائه دهید..");
                    sweetAlertDialog.setConfirmText("خب");
                    sweetAlertDialog.show();
                    return;
                }*/


                String lttFinal = String.valueOf(SharedPrefUtils.getDoubleData("ltt"));
                String lngFinal = String.valueOf(SharedPrefUtils.getDoubleData("lng"));




                /**/
                Call<String> stringCall;
                Map<String, String> map = new HashMap<>();
                map.put("route", "addNewMechanic");
                map.put("job_ids", jobIdsText);
                map.put("region_id", regionIdText);
                map.put("address", regionDesc);
                map.put("name", mechanicName);
                map.put("store_name", mechanicStoreName);
                map.put("phone_number", mechanicPhone);
                map.put("about", mechanicAbout);
                map.put("x_location", lttFinal);
                map.put("y_location", lngFinal);


                if (files.size() == 0) {
                    stringCall = Application.getApi().sendQuestion(map);
                } else
                    stringCall = Application.getApi().uploadMultipleFilesDynamic(map, files);

                stringCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        app.l(response.body() + "adftts");
                        if (response.body() != null) {

                            JSONObject jsonObject;
                            String entranceId;
                            String jsonString = response.body();
                            try {
                                jsonObject = new JSONObject(jsonString);
                                entranceId = jsonObject.getString("entrance_id");
                                //String state = jsonObject.getString("state");
                                //app.l(state+"asdfafdjktyb");

                                SharedPrefUtils.saveData("entranceId", entranceId);
                                SharedPrefUtils.saveData("type", 1);

                                startActivity(new Intent(NewMechanicActivity2.this, MainActivity.class));
                            } catch (JSONException e) {
                                SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection1" + e.getLocalizedMessage());
                                sweetAlertDialogSendCode.show();
                            }

                        } else {
                            SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection2");
                            sweetAlertDialogSendCode.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        SweetAlertDialog sweetAlertDialogSendCode = new SweetAlertDialog(NewMechanicActivity2.this, SweetAlertDialog.WARNING_TYPE).setTitleText("error connection3");
                        sweetAlertDialogSendCode.show();
                    }
                });


                break;
            case R.id.whats_up_intent:

                if (app.appInstalledOrNot("com.whatsapp")) {
                    PackageManager pm = getPackageManager();
                    String url = "https://api.whatsapp.com/send?phone=+989365487593";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("لطفا قبل از ارسال فیلم برنامه واتساپ را نصب کنید.");
                    sweetAlertDialog.setConfirmText("خب");
                    sweetAlertDialog.show();
                }
                break;
            case R.id.map_thumbnail_fake:
                startActivity(new Intent(NewMechanicActivity2.this, MechanicLocationActivity.class));
                break;
        }
    }

    public String jobSeparator(List<?> list) {
        StringBuilder jobIds = new StringBuilder();
        if (list.size() == 1) {
            return String.valueOf(list.get(0));
        }
        for (int i = 0; i < list.size(); i++) {
            String connector;

            if (i == list.size() - 1) connector = "";
            else connector = ",";
            jobIds.append(list.get(i)).append(connector);
        }
        return jobIds.toString();

    }

    public BroadcastReceiver jobRowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jobName = intent.getStringExtra("jobName");
            String jobId = intent.getStringExtra("jobId");/**/
            listIds.add(Integer.parseInt(jobId));
            listNames.add(jobName);

            app.l(jobName + "sgfddds" + jobId);
        }
    };
}
