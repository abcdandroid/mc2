package com.example.mechanic2.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MechanicLocationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GPS_REQUEST_CODE = 120;
    private MapView mMapView;
    private FloatingActionButton findMeFab;
    MyLocationNewOverlay mLocationOverlay;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
    IMapController mapController;
    Overlay overlay1;
    private TextView submitLocation;
    private MyOverLay myOverLay = new MyOverLay();
    private SweetAlertDialog sweetAlertDialogRequestGps;
    private TextView requestForGps;
    private RelativeLayout allowAccessGps;
    private SweetAlertDialog gpsSweetAlertDialog;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private SweetAlertDialog sweetAlertDialogGpsWarning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_location);
        Context context = Application.getContext();
        findMeFab = findViewById(R.id.findMeFab);
        submitLocation = findViewById(R.id.submitLocation);
        mMapView = findViewById(R.id.map);
        mMapView.setMultiTouchControls(true);
        IMapController mMapViewController = mMapView.getController();
        mMapViewController.setZoom(9.5);
        findMeFab.setOnClickListener(this);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mMapView)/* {

            @Override
            public void onLocationChanged(Location location, IMyLocationProvider source) {
                Log.d(TAG, "onLocationChanged: q");
                super.onLocationChanged(location, source);
                if (gpsSweetAlertDialog != null && gpsSweetAlertDialog.isShowing())
                    gpsSweetAlertDialog.dismissWithAnimation();

                GeoPoint startPoint = mLocationOverlay.getMyLocation();
                mapController.setCenter(startPoint);

            }
        }*/;
        this.mLocationOverlay.enableMyLocation();
        mapController = mMapView.getController();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_location_new);
        mLocationOverlay.setDirectionArrow(icon, icon);
        mLocationOverlay.setPersonIcon(icon);
        mMapView.getOverlays().add(this.mLocationOverlay);


        GeoPoint startPoint = new GeoPoint(Double.parseDouble(SharedPrefUtils.getStringData("ltt")), Double.parseDouble(SharedPrefUtils.getStringData("lng")));

        mapController.setCenter(startPoint);
        if (!SharedPrefUtils.getStringData("ltt").equals("-1") && !SharedPrefUtils.getStringData("lng").equals("-1")) {
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

        mapController.setZoom(18f);


        overlay1 = new Overlay() {
            public GeoPoint geoPoint = new GeoPoint(0, 0);

            @Override
            public boolean onTouchEvent(MotionEvent event, MapView mapView) {
                return super.onTouchEvent(event, mapView);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
                if (mMapView.getOverlays().contains(mLocationOverlay)) {

                    mMapView.getOverlays().remove(mLocationOverlay);
                    mMapView.getOverlays().add(overlay1);
                }
                final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
                String longitude = Double.toString(((double) loc.getLongitudeE6()) / 1000000);
                String latitude = Double.toString(((double) loc.getLatitudeE6()) / 1000000);

                ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
                OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double) loc.getLatitudeE6()) / 1000000), (((double) loc.getLongitudeE6()) / 1000000)));
                mapItem.setMarker(marker);
                overlayArray.add(mapItem);
                if (anotherItemizedIconOverlay == null) {
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                    mapView.invalidate();
                } else {
                    mapView.getOverlays().remove(anotherItemizedIconOverlay);
                    mapView.invalidate();
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                }
                return true;
            }

            @Override
            public boolean onLongPress(MotionEvent e, MapView mapView) {
                return super.onLongPress(e, mapView);
            }
        };

        mMapView.getOverlays().add(myOverLay);
        submitLocation.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();


        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();


        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.findMeFab) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {


                int checkGpsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (checkGpsPermission == PackageManager.PERMISSION_GRANTED) {

                    findLocationWithCheckPermission();
                } else {
                    View gpsView = LayoutInflater.from(this).inflate(R.layout.view_get_gps_permission, null);
                    requestForGps = gpsView.findViewById(R.id.request_for_gps);
                    allowAccessGps = gpsView.findViewById(R.id.allow_access_gps);
                    sweetAlertDialogRequestGps = new SweetAlertDialog(this).hideConfirmButton().setCustomView(gpsView);
                    sweetAlertDialogRequestGps.show();
                    allowAccessGps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sweetAlertDialogRequestGps.dismiss();
                            ActivityCompat.requestPermissions(MechanicLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_REQUEST_CODE);
                        }
                    });
                }

            } else {
                findLocationWithCheckPermission();
            }

        } else if (v.getId() == R.id.submitLocation) {

            double longitude = 0;
            double latitude = 0;
            if (mMapView.getOverlays().get(0) instanceof MyLocationNewOverlay) {

                if (mLocationOverlay.getMyLocation() == null) {
                    longitude = 0;
                    latitude = 0;
                } else {
                    longitude = mLocationOverlay.getMyLocation().getLongitude();
                    latitude = mLocationOverlay.getMyLocation().getLatitude();
                }

            } else if (mMapView.getOverlays().get(0) instanceof MyOverLay) {

                longitude = myOverLay.mGeoPoint.getLongitude();
                latitude = myOverLay.mGeoPoint.getLatitude();

            } else {
                longitude = 0;
                latitude = 0;

            }


            SharedPrefUtils.saveData("ltt", String.valueOf(latitude));
            SharedPrefUtils.saveData("lng", String.valueOf(longitude));
            finish();
        }
    }

    private void findLocationWithCheckPermission() {
        try {
            if (gpsSweetAlertDialog == null) {
                gpsSweetAlertDialog = new SweetAlertDialog(MechanicLocationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                gpsSweetAlertDialog.setTitle("لطفا شکیبا باشید.");
                gpsSweetAlertDialog.setContentText("در حال پیدا کردن موقعیت فعلی شما");
                gpsSweetAlertDialog.show();
            }

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "onLocationChanged: ");

                    if (!mMapView.getOverlays().contains(mLocationOverlay)) {
                        Log.d(TAG, "onLocationChanged: 1");
                        if (mMapView.getOverlays().contains(myOverLay)) {
                            mMapView.getOverlays().clear();
                            mMapView.getOverlays().add(mLocationOverlay);
                            mMapView.getOverlays().add(myOverLay);
                        }
                    }


                    GeoPoint startPoint = mLocationOverlay.getMyLocation();
                    if (startPoint == null) {
                        Log.d(TAG, "onLocationChanged: 2");
                        startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());


                        final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);

                        GeoPoint loc = startPoint;

                        ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
                        OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double) loc.getLatitudeE6()) / 1000000), (((double) loc.getLongitudeE6()) / 1000000)));
                        mapItem.setMarker(marker);
                        overlayArray.add(mapItem);
                        if (anotherItemizedIconOverlay == null) {
                            Log.d(TAG, "onLocationChanged: 21");
                            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                            mMapView.getOverlays().add(anotherItemizedIconOverlay);
                            mMapView.invalidate();
                        } else {
                            Log.d(TAG, "onLocationChanged: 22");
                            mMapView.getOverlays().remove(anotherItemizedIconOverlay);
                            mMapView.invalidate();
                            anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                            mMapView.getOverlays().add(anotherItemizedIconOverlay);
                        }
                    }
                    mapController.setCenter(startPoint);
                    if (gpsSweetAlertDialog != null) {
                        Log.d(TAG, "onLocationChanged: 3");
                        gpsSweetAlertDialog.dismissWithAnimation();
                    }


                    /*
                    double x=startPoint.getAltitude();
                    double y=startPoint.getLongitude();
                    double z=startPoint.getLatitude();
                    double a=startPoint.getLatitudeE6();
                    double b=startPoint.getLongitudeE6();*/


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    /*
                    if (gpsSweetAlertDialog != null && gpsSweetAlertDialog.isShowing()) {

                        return;
                    }*/

                    Log.d(TAG, "onStatusChanged: ");
                    if (gpsSweetAlertDialog == null) {
                        Log.d(TAG, "onStatusChanged: 1");
                        gpsSweetAlertDialog = new SweetAlertDialog(MechanicLocationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        gpsSweetAlertDialog.setTitle("لطفا شکیبا باشید.");
                        gpsSweetAlertDialog.setContentText("در حال پیدا کردن موقعیت فعلی شما");
                        gpsSweetAlertDialog.show();
                    }


                    if (!mMapView.getOverlays().contains(mLocationOverlay)) {
                        Log.d(TAG, "onStatusChanged: 2  ");
                        if (mMapView.getOverlays().contains(myOverLay)) {
                            mMapView.getOverlays().clear();
                            mMapView.getOverlays().add(mLocationOverlay);
                            mMapView.getOverlays().add(myOverLay);
                        }
                    }


                    GeoPoint startPoint = mLocationOverlay.getMyLocation();
                    mapController.setCenter(startPoint);


                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.d(TAG, "onProviderEnabled: ");
                    if (sweetAlertDialogGpsWarning != null) {
                        Log.d(TAG, "onProviderEnabled: 1");
                        sweetAlertDialogGpsWarning.dismissWithAnimation();
                    }
                    if (gpsSweetAlertDialog != null) {
                        Log.d(TAG, "onProviderEnabled: 2");
                        gpsSweetAlertDialog.dismissWithAnimation();

                    }
                    Log.d(TAG, "onProviderEnabled: 3");

                    gpsSweetAlertDialog = new SweetAlertDialog(MechanicLocationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    gpsSweetAlertDialog.setTitle("لطفا شکیبا باشید.");
                    gpsSweetAlertDialog.setContentText("در حال پیدا کردن موقعیت فعلی شما");
                    gpsSweetAlertDialog.show();


                    if (!mMapView.getOverlays().contains(mLocationOverlay)) {
                        Log.d(TAG, "onProviderEnabled: 4");
                        if (mMapView.getOverlays().contains(myOverLay)) {
                            Log.d(TAG, "onProviderEnabled: 5");
                            mMapView.getOverlays().clear();
                            mMapView.getOverlays().add(mLocationOverlay);
                            mMapView.getOverlays().add(myOverLay);
                        }
                    }

                    app.l("CCC");
                    GeoPoint startPoint = mLocationOverlay.getMyLocation();
                    mapController.setCenter(startPoint);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.d(TAG, "onProviderDisabled: ");
                    sweetAlertDialogGpsWarning = new SweetAlertDialog(MechanicLocationActivity.this);
                    sweetAlertDialogGpsWarning.hideConfirmButton();
                    sweetAlertDialogGpsWarning.setCancelable(false);
                    View view = LayoutInflater.from(MechanicLocationActivity.this).inflate(R.layout.view_enable_gps, null, false);
                    RelativeLayout relativeLayout = view.findViewById(R.id.gps_intent);
                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sweetAlertDialogGpsWarning.dismiss();
                            Log.d(TAG, "onClick: ");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "run: ");
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            }, 10);
                        }
                    });
                    sweetAlertDialogGpsWarning.setCustomView(view);
                    sweetAlertDialogGpsWarning.show();


                }
            };
            Log.d(TAG, "findLocationWithCheckPermission: ");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15, 1000, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        //------
/*        gpsSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        gpsSweetAlertDialog.setTitle("لطفا شکیبا باشید.");
        gpsSweetAlertDialog.setContentText("در حال پیدا کردن موقعیت فعلی شما");
        gpsSweetAlertDialog.show();

        if (!mMapView.getOverlays().contains(mLocationOverlay)) {
            if (mMapView.getOverlays().contains(myOverLay)) {
                mMapView.getOverlays().clear();
                mMapView.getOverlays().add(mLocationOverlay);
                mMapView.getOverlays().add(myOverLay);
            }
        }
        GeoPoint startPoint = mLocationOverlay.getMyLocation();
        mapController.setCenter(startPoint);*/
    }

    private static final String TAG = "MechanicLocationActivit";

    public class MyOverLay extends Overlay {
        public int a = 2;

        private GeoPoint mGeoPoint = new GeoPoint(33f, 0f);

        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) {

            return super.onTouchEvent(event, mapView);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
            Log.d(TAG, "onSingleTapConfirmed: ");
            if (mMapView.getOverlays().contains(mLocationOverlay)) {
                Log.d(TAG, "onSingleTapConfirmed: 1");
                mMapView.getOverlays().remove(mLocationOverlay);
                mMapView.getOverlays().add(myOverLay);
            }
            final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
            Projection proj = mapView.getProjection();
            GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
            mGeoPoint = new GeoPoint(loc);

            ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
            OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double) loc.getLatitudeE6()) / 1000000), (((double) loc.getLongitudeE6()) / 1000000)));
            mapItem.setMarker(marker);
            overlayArray.add(mapItem);
            if (anotherItemizedIconOverlay == null) {
                Log.d(TAG, "onSingleTapConfirmed: 2");
                anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                mapView.getOverlays().add(anotherItemizedIconOverlay);
                mapView.invalidate();
            } else {
                Log.d(TAG, "onSingleTapConfirmed: 3");
                mapView.getOverlays().remove(anotherItemizedIconOverlay);
                mapView.invalidate();
                anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                mapView.getOverlays().add(anotherItemizedIconOverlay);
            }

            return true;
        }

        @Override
        public boolean onLongPress(MotionEvent e, MapView mapView) {
            return super.onLongPress(e, mapView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GPS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
/*                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);*/
                findLocationWithCheckPermission();
            } else {
                app.t("برای پیدا کردن موقعیت مکانی شما،، این برنامه نیاز به دسترسی gps دارد.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
