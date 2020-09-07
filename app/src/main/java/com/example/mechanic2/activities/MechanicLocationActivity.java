package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MechanicLocationActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mMapView;
    private FloatingActionButton findMeFab;
    MyLocationNewOverlay mLocationOverlay;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
    IMapController mapController;
    Overlay overlay1;
    private TextView submitLocation;
    private MyOverLay myOverLay = new MyOverLay();


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
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mMapView);
        this.mLocationOverlay.enableMyLocation();
        mapController = mMapView.getController();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_location_new);
        mLocationOverlay.setDirectionArrow(icon, icon);
        mLocationOverlay.setPersonIcon(icon);
        mMapView.getOverlays().add(this.mLocationOverlay);
        //app.l(SharedPrefUtils.getDoubleData("ltt") + "@@" + SharedPrefUtils.getDoubleData("lng"));

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
                    app.l("QQ");
                    mMapView.getOverlays().remove(mLocationOverlay);
                    mMapView.getOverlays().add(overlay1);
                }
                final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
                String longitude = Double.toString(((double) loc.getLongitudeE6()) / 1000000);
                String latitude = Double.toString(((double) loc.getLatitudeE6()) / 1000000);
                System.out.println("- Latitude ********= " + latitude + ", Longitude = " + longitude);

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
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        //needed for compass, my location overlays, v6.0.0 and up
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.findMeFab) {
            if (!mMapView.getOverlays().contains(mLocationOverlay)) {
                if (mMapView.getOverlays().contains(myOverLay)) {
                    mMapView.getOverlays().clear();
                    mMapView.getOverlays().add(mLocationOverlay);
                    mMapView.getOverlays().add(myOverLay);

                }
            }
            GeoPoint startPoint = mLocationOverlay.getMyLocation();
            mapController.setCenter(startPoint);
        } else if (v.getId() == R.id.submitLocation) {
            // app.l(myOverLay.mGeoPoint.getLatitude() + "*****" + myOverLay.mGeoPoint.getLongitude());
            double longitude = 0;
            double latitude = 0;
            if (mMapView.getOverlays().get(0) instanceof MyLocationNewOverlay) {
                app.l("@@");
                if (mLocationOverlay.getMyLocation() == null) {
                    longitude = 0;
                    latitude = 0;
                } else {
                    longitude = mLocationOverlay.getMyLocation().getLongitude();
                    latitude = mLocationOverlay.getMyLocation().getLatitude();
                }
//                app.l(mLocationOverlay.getMyLocation().getLatitude() + "*****my location*****" + mLocationOverlay.getMyLocation().getLongitude());
            } else if (mMapView.getOverlays().get(0) instanceof MyOverLay) {

                longitude = myOverLay.mGeoPoint.getLongitude();
                latitude = myOverLay.mGeoPoint.getLatitude();
                app.l(myOverLay.mGeoPoint.getLatitude() + "****new new location******" + myOverLay.mGeoPoint.getLongitude());
            } else {
                longitude = 0;
                latitude = 0;
                app.l("unknown");
            }
            app.l(latitude + "(((" + longitude);

            SharedPrefUtils.saveData("ltt", String.valueOf(latitude));
            SharedPrefUtils.saveData("lng", String.valueOf(longitude));
            finish();
        }
    }

    public class MyOverLay extends Overlay {
        public int a = 2;

        private GeoPoint mGeoPoint = new GeoPoint(33, 0); // for reuse

        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) {
            /*    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }*/
            return super.onTouchEvent(event, mapView);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
            if (mMapView.getOverlays().contains(mLocationOverlay)) {
                app.l("QQ");
                mMapView.getOverlays().remove(mLocationOverlay);
                mMapView.getOverlays().add(myOverLay);
            }
            final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.ic_location_new);
            Projection proj = mapView.getProjection();
            GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
            String longitude = Double.toString(((double) loc.getLongitudeE6()) / 1000000);
            String latitude = Double.toString(((double) loc.getLatitudeE6()) / 1000000);
            mGeoPoint = new GeoPoint(loc);
            System.out.println("- Latitude ********= " + latitude + ", Longitude = " + longitude);
            Log.d("TAG", "onSingleTapConfirmed: ********" + ("- Latitude = " + latitude + ", Longitude = " + longitude));
            ;

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
    }
}
