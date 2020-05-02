package gofereatsdriver.views.main.fragments;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.fragments
 * @category HomeFragment
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.Constants;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.interfaces.ActivityListener;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.LatLngInterpolator;
import gofereatsdriver.service.GPS_Service;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.MyLocation;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;

/*************************************************************
 HomeFragment
 Its used get home screen main fragment details
 *************************************************************** */
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 1000;
    public final ArrayList movepoints = new ArrayList();
    @Nullable
    public GoogleMap mGoogleMap;
    public LatLng newLatLng = null;
    public Marker carmarker;
    public float startbear = 0, endbear = 0;
    public Marker marker;
    public boolean samelocation = false;
    public float speed = 13f;
    public ValueAnimator valueAnimator;
    public DecimalFormat twoDForm;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    RunTimePermission runTimePermission;
    public @InjectView(R.id.mapview)
    MapView mMapView;
    /**
     * Get user current location
     */
    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            if (location == null) return;
            sessionManager.setCurrentLat(String.valueOf(location.getLatitude()));
            sessionManager.setCurrentLng(String.valueOf(location.getLongitude()));
            if (sessionManager.getDriverStatus() == -1) sessionManager.setDriverStatus(0);
        }
    };
    private View view;
    private ActivityListener listener;
    private MainActivity mActivity;
    private Location lastLocation = null;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d("Home ", String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                //drawMarker(location);
                //changeMap(location);
                onLocationChange(location);
                //mLocationManager.removeUpdates(mLocationListener);
            } else {
                Log.d("", "Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    /**
     * Rotate marker
     **/
    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Home must implement ActivityListener");
        }
        /*String languageToLoad = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);*/
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();

        AppController.getAppComponent().inject(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.home_fragment, container, false);

        }
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        mMapView.onCreate(savedInstanceState);


        //mGoogleMap = mMapView.getMap();
        mMapView.getMapAsync(this); //this is important
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        initMap();


        twoDForm = new DecimalFormat("#.##########");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        checkAllPermission(Constants.PERMISSIONS_LOCATION);

    }

    private void init() {
        if (listener == null) return;

        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (MainActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        if (listener != null) listener = null;
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (MyLocation.defaultHandler().getLocation(mActivity, locationResult)) {
            getCurrentLocation();
        }
        if (!sessionManager.getCurrentLat().equals("")) getCurrentLocation();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * Initialize Map
     */
    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, mActivity, -1).show();
            mActivity.finish();
        } else {
            if (mGoogleMap != null) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

    /**
     * Get Current Location
     */
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(mMapView, "Unable to get GPS", Snackbar.LENGTH_SHORT).show();
        else {
            if (isGPSEnabled) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (location != null) {
            Log.d("", String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
            changeMap(location);
            // drawMarker(location);
        } else {
            Location location1 = new Location("service provider");
            Log.d("GPS Service CurrentHome", sessionManager.getCurrentLat() + " " + sessionManager.getCurrentLng());
            if (location1 != null && !sessionManager.getCurrentLat().equals(""))
                location1.setLatitude(Double.valueOf(sessionManager.getCurrentLat()));
            if (location1 != null && !sessionManager.getCurrentLng().equals(""))
                location1.setLongitude(Double.valueOf(sessionManager.getCurrentLng()));
            if (location1 != null && isGPSEnabled && isNetworkEnabled) {
                changeMap(location1);
            }

        }
    }

    /**
     * Get driver current location in map
     **/
    private void changeMap(Location location) {


        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mGoogleMap != null) {
            //mGoogleMap.clear();
            LatLng latLong;

            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            if (newLatLng == null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(16.5f).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                MarkerOptions pickupOptions = new MarkerOptions();

                // Setting the position of the marker
                pickupOptions.position(latLong);
                pickupOptions.anchor(0.5f, 0.5f);


                pickupOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gf_moto_bike));
                // Add new marker to the Google Map Android API V2
                carmarker = mGoogleMap.addMarker(pickupOptions);
            }

            moveMarker(latLong);

            newLatLng = latLong;

        } /*else {
            Toast.makeText(mActivity,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    /**
     * Convet Latlong into location
     *
     * @param latLng
     * @return
     */
   /* private Location convertLatLngToLocation(LatLng latLng) {
        Location loc = new Location("someLoc");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        return loc;
    }*/


    /**
     * get and move location while location change
     */
    public void onLocationChange(Location location) {
        try {
            if (location != null) {
                //Toast.makeText(getActivity(), "Current speed:" + location.getSpeed(),Toast.LENGTH_SHORT).show();
                speed = location.getSpeed();

                float calculatedSpeed = 0;
                if (lastLocation != null) {
                    double elapsedTime = (location.getTime() - lastLocation.getTime()) / 1_000; // Convert milliseconds to seconds
                    if (elapsedTime <= 0) elapsedTime = 1;
                    calculatedSpeed = (float) (lastLocation.distanceTo(location) / elapsedTime);
                }
                this.lastLocation = location;

                float speedcheck = location.hasSpeed() ? location.getSpeed() : calculatedSpeed;
                if (!Float.isNaN(speedcheck) && !Float.isInfinite(speedcheck)) speed = speedcheck;

                if (speed <= 0) speed = 10;

                changeMap(location);
                sessionManager.setCurrentLat(Double.toString(location.getLatitude()));
                sessionManager.setCurrentLng(Double.toString(location.getLongitude()));
                if (sessionManager.getDriverStatus() == 1) {
                    Intent GPSservice = new Intent(mActivity, GPS_Service.class);
                    if (!isMyServiceRunning(GPS_Service.class)) {
                        mActivity.startService(GPSservice);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************************************************************** */
    /**                  Animate Marker for Live Tracking               */
    /****************************************************************** */

    /**
     * Check service is running or not
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Move marker for given location(Live tracking)
     **/
    public void moveMarker(LatLng latLng) {

        if (movepoints.size() < 1) {
            movepoints.add(0, latLng);
            movepoints.add(1, latLng);

        } else {
            movepoints.set(1, movepoints.get(0));
            movepoints.set(0, latLng);
        }

        DecimalFormat twoDForm = new DecimalFormat("#.#######");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);

        String zerolat = twoDForm.format(((LatLng) movepoints.get(0)).latitude);
        String zerolng = twoDForm.format(((LatLng) movepoints.get(0)).longitude);

        String onelat = twoDForm.format(((LatLng) movepoints.get(1)).latitude);
        String onelng = twoDForm.format(((LatLng) movepoints.get(1)).longitude);

        if (!zerolat.equals(onelat) || !zerolng.equals(onelng)) {
            adddefaultMarker((LatLng) movepoints.get(1), (LatLng) movepoints.get(0));
            samelocation = false;
        }
    }

    /**
     * Move marker for given locations
     **/
    public void adddefaultMarker(LatLng latlng, LatLng latlng1) {
        Location startbearlocation = new Location(LocationManager.GPS_PROVIDER);
        Location endbearlocation = new Location(LocationManager.GPS_PROVIDER);

        startbearlocation.setLatitude(latlng.latitude);
        startbearlocation.setLongitude(latlng.longitude);

        endbearlocation.setLatitude(latlng1.latitude);
        endbearlocation.setLongitude(latlng1.longitude);

        if (endbear != 0.0) {
            startbear = endbear;
        }

        carmarker.setFlat(true);
        carmarker.setAnchor(0.5f, 0.5f);
        marker = carmarker;
        // Move map while marker gone
        ensureMarkerOnBounds(latlng, "updated");

        endbear = (float) bearing(startbearlocation, endbearlocation);
        endbear = (float) (endbear * (180.0 / 3.14));

        //double distance = startbearlocation.distanceTo(endbearlocation);
        double distance = Double.valueOf(twoDForm.format(startbearlocation.distanceTo(endbearlocation)));

        if (distance > 0) animateMarker(latlng1, marker, speed, endbear);

    }

    /**
     * Move marker
     **/
    public void animateMarker(final LatLng destination, final Marker marker, final float speed, final float endbear) {

        final LatLng[] newPosition = new LatLng[1];
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);
            long duration;
            final Location newLoc = new Location(LocationManager.GPS_PROVIDER);
            newLoc.setLatitude(startPosition.latitude);
            newLoc.setLongitude(startPosition.longitude);
            Location prevLoc = new Location(LocationManager.GPS_PROVIDER);
            prevLoc.setLatitude(endPosition.latitude);
            prevLoc.setLongitude(endPosition.longitude);


            double distance = Double.valueOf(twoDForm.format(newLoc.distanceTo(prevLoc)));

            duration = (long) ((distance / speed) * 1015);
            if (duration >= 1000) duration = 950;
            duration = 1015;
            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            if (valueAnimator != null) {
                valueAnimator.cancel();
                valueAnimator.end();
            }
            valueAnimator = ValueAnimator.ofFloat(0, 1f);
            valueAnimator.setDuration(duration);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        newPosition[0] = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition[0]); // Move Marker
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setRotation(computeRotation(v, startRotation, endbear)); // Rotate Marker
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();
        }
    }

    /**
     * Find GPS rotate Direction
     **/
    private double bearing(Location startPoint, Location endPoint) {
        double deltaLongitude = endPoint.getLongitude() - startPoint.getLongitude();
        double deltaLatitude = endPoint.getLatitude() - startPoint.getLatitude();
        double angle = (3.14 * .5f) - Math.atan(deltaLatitude / deltaLongitude);

        if (deltaLongitude > 0) return angle;
        else if (deltaLongitude < 0) return angle + 3.14;
        else if (deltaLatitude < 0) return 3.14;

        return 0.0f;
    }

    /**
     * move map to center position while marker hide
     **/
    private void ensureMarkerOnBounds(LatLng toPosition, String type) {
        if (marker != null) {
            float currentZoomLevel = mGoogleMap.getCameraPosition().zoom;
            float bearing = mGoogleMap.getCameraPosition().bearing;

            CameraPosition cameraPosition = new CameraPosition.Builder().target(toPosition).zoom(currentZoomLevel).bearing(bearing).build();


            if ("updated".equals(type)) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                if (!mGoogleMap.getProjection().getVisibleRegion().latLngBounds.contains(toPosition)) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
    }

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(mActivity, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(mActivity, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(mActivity, permission, 150);
            }
        } else {
            checkGpsEnable();
        }
    }

    /**
     * Get permission result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        } else {
            checkGpsEnable();
        }
    }

    /**
     * If any one or more permission is deny or block show the enable permission dialog
     */
    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) {
                        callPermissionSettings();
                        customDialog.dismiss();
                    } else {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                        customDialog.dismiss();
                    }
                }
            });
            customDialog.show(mActivity.getSupportFragmentManager(), "");
        }
    }

    /**
     * Open permission dialog
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(mActivity);
        if (!isGpsEnabled) {
            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            MyLocation.defaultHandler().getLocation(mActivity, locationResult);
            getCurrentLocation();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMap();
        getCurrentLocation();
    }

}
