package gofereatsdriver.service;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage service
 * @category GPS_Service
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.Enums;
import gofereatsdriver.utils.RequestCallback;

/*************************************************************
 GPS_Service
 Get and update driver current location in server
 *************************************************************** */
public class GPS_Service extends Service implements ServiceListener {

    // Location time interval
    private static final int LOCATION_INTERVAL = 1000 * 1 * 1; // 1000 * 60 * 1 for 1 minute 1000 * 10 * 1 for 10 seconds
    private static final int LOCATION_INTERVAL_CHECK = 1000 * 1 * 1;
    private static final int LOCATION_UPDATE_INTERVAL = 1000 * 10 * 1; // 1000 * 60 * 1 for 1 minute 1000 * 10 * 1 for 10 seconds
    private static final float LOCATION_DISTANCE = 10; // 30 meters
    private static final float maxDistance = (float) 0.4;
    private static final String TAG = "GPS Service";
    public static List<LatLng> latLngList;
    public Location mLastLocation = null;
    public Location mCheckLocation = null;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    SessionManager sessionManager;
    public HashMap<String, String> locationHashMap;
    public boolean first = true;
    public boolean isbeginfirst = true;
    public boolean isotherfirst = true;
    public float distanceInMeters = 0;
    public float distanceInKM = 0;
    public float totalDistanceInKM = 0;
    public int count = 0;
    public int counts = 0;
    public double oldlatitude = 0;
    public double oldlongitude = 0;
    public boolean gps_enabled = false;
    public boolean network_enabled = false;
    public Timer timer = new Timer();
    public MyTimerTask timerTask;
    public boolean updateLocationandReport = false;
    public DecimalFormat twoDForm;
    public PowerManager mgr;
    public PowerManager.WakeLock wakeLock;
    protected boolean isInternetAvailable;
    private LocationListener listener;
    private LocationManager locationManager;
    private DatabaseReference mFirebaseDatabase;
    private ValueEventListener mSearchedLocationReferenceListener;
    private long locationUpdatedAt = Long.MIN_VALUE;
    private int FASTEST_INTERVAL = LOCATION_INTERVAL_CHECK; // use whatever suits you
    private Location location = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d("GPS Service started", "GPS Service started");
        AppController.getAppComponent().inject(this);
        mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");

        latLngList = new ArrayList<LatLng>();
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'Driver Location' node
        mFirebaseDatabase = mFirebaseInstance.getReference("live_tracking");

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location LastLocation) {
                if (LastLocation != null) {
                    sessionManager.setCurrentLat(Double.toString(LastLocation.getLatitude()));
                    sessionManager.setCurrentLng(Double.toString(LastLocation.getLongitude()));
                    Log.d("GPS Service Current", sessionManager.getCurrentLat() + " " + sessionManager.getCurrentLng());

                    if (sessionManager.getTripId() > 0) {
                        float distance = 0;
                        if (location != null) {
                            distance = LastLocation.distanceTo(location);
                            if (distance < 30) {
                                updateLocationFireBase(Double.toString(LastLocation.getLatitude()), Double.toString(LastLocation.getLongitude()), sessionManager.getTripId());
                            }
                        }
                    } else {
                        if (mSearchedLocationReferenceListener != null)
                            mFirebaseDatabase.removeEventListener(mSearchedLocationReferenceListener);
                    }
                    location = LastLocation;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                System.out.println("enable " + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                System.out.println("Disabled " + s);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled

        if (network_enabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, listener);

        if (gps_enabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, listener);


    }

    /**
     * Wake lock started
     */
    private void acquireWakeLock() {
        try {
            wakeLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wake lock end
     */
    private void releaseWakeLock() {
        try {
            wakeLock.release();
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
        timer.cancel();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, LOCATION_UPDATE_INTERVAL, LOCATION_UPDATE_INTERVAL);
        return START_STICKY;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            Intent i = new Intent("location_update");
            i.putExtra("type", "Update");
            i.putExtra("Lat", Double.valueOf(sessionManager.getCurrentLat()));
            i.putExtra("Lng", Double.valueOf(sessionManager.getCurrentLng()));
            i.putExtra("km", String.valueOf(distanceInKM));
            sendBroadcast(i);
        } else {
            Intent j = new Intent("location_update");
            j.putExtra("type", "Update");
            j.putExtra("Lat", Double.valueOf(sessionManager.getCurrentLat()));
            j.putExtra("Lng", Double.valueOf(sessionManager.getCurrentLng()));
            j.putExtra("km", String.valueOf(distanceInKM));
            j.putExtra("status", "Else");
            sendBroadcast(j);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        Intent j = new Intent("location_update");
        j.putExtra("type", "Update");
        j.putExtra("Lat", Double.valueOf(sessionManager.getCurrentLat()));
        j.putExtra("Lng", Double.valueOf(sessionManager.getCurrentLng()));
        j.putExtra("km", String.valueOf(distanceInKM));
        j.putExtra("status", "error");
        sendBroadcast(j);
    }

    /**
     * Get location change and update
     */
    public void updateLocationChange() {
        twoDForm = new DecimalFormat("#.#######");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        updateLocationandReport = false;
        Log.d("GPS Location Changed", "GPS Location Changed" + mLastLocation.toString());

        isInternetAvailable = commonMethods.isOnline(getApplicationContext());

        if (isInternetAvailable) {
            /*
            * Send location to broadcast
            */
            Intent i = new Intent("location_update");
            i.putExtra("type", "change");
            i.putExtra("Lat", mLastLocation.getLatitude());
            i.putExtra("Lng", mLastLocation.getLongitude());
            i.putExtra("net", isInternetAvailable);
            sendBroadcast(i);
            updateLocationandReport = false;
            Log.d("GPS Net available", "GPS Net available " + isInternetAvailable);

            /*
            * check update first time
            */
            if (count == 0) {
                locationUpdatedAt = System.currentTimeMillis();
                updateLocationandReport = true;
            } else {

                long secondsElapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - locationUpdatedAt);

                /**
                 * @TripStatus 'pending' => 0, Driver accept the request
                 * 'confirmed' => 1, Driver confirmed the  list and rating for restaurant
                 * 'declined' => 2,  Driver declined the order list
                 * 'started' => 3,  Driver start the trip (pickup the order)
                 * 'delivered'  => 4, Driver drop off or delivered the order and rating for eater
                 * 'completed' => 5, Driver complete the trip
                 * 'cancelled' => 6, Driver or restaurant cancel the trip
                 *
                 * @DriverStatus 0 for offline
                 * 1 for online
                 * 2 for trip
                 */

                if (sessionManager.getTripStatus() >= 0 && (sessionManager.getTripStatus() != 2 && sessionManager.getTripStatus() != 6)) {
                    sessionManager.setDriverStatus(2);
                }
                Log.e(TAG, "Trip Status : " + sessionManager.getTripStatus());
                Log.e(TAG, "Driver Status : " + sessionManager.getDriverStatus());
                secondsElapsed = secondsElapsed * 1000;
                if (secondsElapsed >= FASTEST_INTERVAL) {
                    // check location accuracy here
                    locationUpdatedAt = System.currentTimeMillis();
                    updateLocationandReport = true;
                } else {
                    updateLocationandReport = true;  // for checking
                }

                /*
                * User offline release wake lock
                */
                if (sessionManager.getDriverStatus() == 0) {
                    updateLocationandReport = false;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        releaseWakeLock();
                    }

                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        acquireWakeLock();
                    }

                }
            }

            /**
             *  Update location
             */
            Log.d("GPS location report ", String.valueOf(updateLocationandReport));
            if (updateLocationandReport) {
                count++;
                counts++;

                if (sessionManager.getTripStatus() >= 3 && sessionManager.getTripStatus() != 6) {
                    if (first || oldlatitude == 0) {
                        oldlatitude = mLastLocation.getLatitude();
                        oldlongitude = mLastLocation.getLongitude();
                    }
                    if (isbeginfirst) {
                        count = 0;
                        counts = 0;
                        isbeginfirst = false;
                        isotherfirst = true;
                        totalDistanceInKM = 0;
                        distanceInKM = 0;
                        distanceInMeters = 0;
                    }
                    Log.d("GPS location check OLD", first + " " + String.valueOf(oldlatitude) + " " + String.valueOf(oldlongitude));
                    Log.d("GPS location check NEW", first + " " + String.valueOf(mLastLocation.getLatitude()) + " " + String.valueOf(mLastLocation.getLongitude()));
                    if (first || (oldlatitude != mLastLocation.getLatitude() || oldlongitude != mLastLocation.getLongitude())) {
                        first = false;

                        distanceInKM = 0;
                        distanceInMeters = 0;
                        Location oldLocation = new Location("");//provider name is unnecessary
                        oldLocation.setLatitude(oldlatitude);//your coords of course
                        oldLocation.setLongitude(oldlongitude);

                        Location newLocation = new Location("");//provider name is unnecessary
                        newLocation.setLatitude(mLastLocation.getLatitude());//your coords of course
                        newLocation.setLongitude(mLastLocation.getLongitude());

                        distanceInMeters = oldLocation.distanceTo(newLocation);

                        distanceInKM = (float) (distanceInMeters / 1000.0);

                        distanceInKM = Float.valueOf(twoDForm.format(distanceInKM).replaceAll(",", "."));

                        oldlatitude = mLastLocation.getLatitude();
                        oldlongitude = mLastLocation.getLongitude();

                    } else {
                        distanceInKM = 0;
                        distanceInMeters = 0;
                    }
                    totalDistanceInKM = totalDistanceInKM + distanceInKM;

                    LatLng newLatLng = new LatLng(Double.valueOf(sessionManager.getCurrentLat()), Double.valueOf(sessionManager.getCurrentLng()));
                    latLngList.add(newLatLng);

                    locationHashMap = new HashMap<>();
                    locationHashMap.put("latitude", sessionManager.getCurrentLat());
                    locationHashMap.put("longitude", sessionManager.getCurrentLng());
                    locationHashMap.put("total_km", String.valueOf(distanceInKM));
                    locationHashMap.put("order_id", String.valueOf(sessionManager.getTripId()));
                    locationHashMap.put("status", String.valueOf(sessionManager.getDriverStatus()));
                    locationHashMap.put("token", sessionManager.getToken());

                    if (distanceInKM < maxDistance) {
                        apiService.updateLocation(locationHashMap).enqueue(new RequestCallback(Enums.REQ_UPDATE_LOCATION, this));
                    }
                } else if (sessionManager.getTripStatus() >= 0 && (sessionManager.getTripStatus() != 2 && sessionManager.getTripStatus() != 6)) {
                    locationHashMap = new HashMap<>();
                    locationHashMap.put("latitude", sessionManager.getCurrentLat());
                    locationHashMap.put("longitude", sessionManager.getCurrentLng());
                    locationHashMap.put("total_km", "0");
                    locationHashMap.put("order_id", String.valueOf(sessionManager.getTripId()));
                    locationHashMap.put("status", String.valueOf(sessionManager.getDriverStatus()));
                    locationHashMap.put("token", sessionManager.getToken());
                    apiService.updateLocation(locationHashMap).enqueue(new RequestCallback(Enums.REQ_UPDATE_LOCATION, this));
                } else {
                    if (isotherfirst) {
                        count = 0;
                        counts = 0;
                        isbeginfirst = true;
                        isotherfirst = false;
                        totalDistanceInKM = 0;
                        distanceInKM = 0;
                        distanceInMeters = 0;
                    }

                    locationHashMap = new HashMap<>();
                    locationHashMap.put("latitude", sessionManager.getCurrentLat());
                    locationHashMap.put("longitude", sessionManager.getCurrentLng());
                    locationHashMap.put("status", String.valueOf(sessionManager.getDriverStatus()));
                    locationHashMap.put("token", sessionManager.getToken());

                    if (sessionManager.getDriverStatus() == 1 || sessionManager.getDriverStatus() == 2) {

                        apiService.updateLocation(locationHashMap).enqueue(new RequestCallback(Enums.REQ_UPDATE_LOCATION, this));
                    }

                }
                updateLocationandReport = false;

                Intent j = new Intent("location_update");
                j.putExtra("type", "Updates");
                j.putExtra("Lat", Double.valueOf(sessionManager.getCurrentLat()));
                j.putExtra("Lng", Double.valueOf(sessionManager.getCurrentLng()));
                j.putExtra("km", String.valueOf(distanceInKM));
                j.putExtra("status", sessionManager.getTripStatus());
                sendBroadcast(j);

            } else {
                Intent j = new Intent("location_update");
                j.putExtra("type", "Updates");
                j.putExtra("Lat", Double.valueOf(sessionManager.getCurrentLat()));
                j.putExtra("Lng", Double.valueOf(sessionManager.getCurrentLng()));
                j.putExtra("km", String.valueOf(distanceInKM));
                j.putExtra("status", "Report false");
                sendBroadcast(j);
            }

        } else {
            Intent i = new Intent("location_update");
            i.putExtra("type", "change");
            i.putExtra("Lat", mLastLocation.getLatitude());
            i.putExtra("Lng", mLastLocation.getLongitude());
            i.putExtra("net", isInternetAvailable);
            sendBroadcast(i);
            Log.d("GPS Net unavailable", "GPS Net unavailable" + updateLocationandReport);
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void updateLocationFireBase(String lat, String lng, Integer tripid) {
        // TODO

        DriverLocation driverLocation = new DriverLocation(lat, lng);

        Log.e(TAG, "Driver Location data update" + tripid + " " + driverLocation.lat + " " + driverLocation.lng);
        mFirebaseDatabase.child(String.valueOf(tripid)).setValue(driverLocation);

        Intent j = new Intent("location_update");
        j.putExtra("type", "DataBase");
        j.putExtra("Lat", Double.valueOf(lat));
        j.putExtra("Lng", Double.valueOf(lng));
        sendBroadcast(j);

        if (mSearchedLocationReferenceListener == null) {
            addLatLngChangeListener(); // Get Driver Lat Lng
        }

    }

    /**
     * Driver Location change listener
     */
    private void addLatLngChangeListener() {

        Log.e(TAG, "Driver Location data called");

        // User data change listener
        final Query query = mFirebaseDatabase.child(String.valueOf(sessionManager.getTripId()));

        mSearchedLocationReferenceListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (sessionManager.getTripId() > 0) {
                    DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);

                    // Check for null
                    if (driverLocation == null) {
                        Log.e(TAG, "Driver Location data is null!");
                        return;
                    }

                    Log.e(TAG, "Driver Location data is changed!" + driverLocation.lat + ", " + driverLocation.lng);
                } else {
                    query.removeEventListener(this);
                    mFirebaseDatabase.removeEventListener(this);
                    mFirebaseDatabase.onDisconnect();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    /**
     * GPS location update
     */
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Log.d("GPS Timer Running ", "ss");
            Log.d("GPS Service Current1 ", sessionManager.getCurrentLat() + " " + sessionManager.getCurrentLng());
            if (!TextUtils.isEmpty(sessionManager.getCurrentLat()) && !TextUtils.isEmpty(sessionManager.getCurrentLng())) {
                Log.d("GPS Service Current2 ", sessionManager.getCurrentLat() + " " + sessionManager.getCurrentLng());
                Double lat = Double.valueOf(sessionManager.getCurrentLat());
                Double lng = Double.valueOf(sessionManager.getCurrentLng());
                mLastLocation = new Location("curloc");
                mLastLocation.setLatitude(lat);
                mLastLocation.setLongitude(lng);


                DecimalFormat twoDForm = new DecimalFormat("#.######");
                DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
                dfs.setDecimalSeparator('.');
                twoDForm.setDecimalFormatSymbols(dfs);
                String zerolat = twoDForm.format(mLastLocation.getLatitude());
                String zerolng = twoDForm.format(mLastLocation.getLatitude());
                String onelat;
                String onelng;
                if (mCheckLocation != null) {
                    onelat = twoDForm.format(mCheckLocation.getLatitude());
                    onelng = twoDForm.format(mCheckLocation.getLatitude());
                } else {
                    onelat = twoDForm.format(0.0);
                    onelng = twoDForm.format(0.0);
                }

                if (!zerolat.equals(onelat) || !zerolng.equals(onelng) || mCheckLocation == null)
                    updateLocationChange();
                mCheckLocation = mLastLocation;
            } else {
                Intent i = new Intent("location_update");
                i.putExtra("type", "change");
                i.putExtra("Lat", 0);
                i.putExtra("Lng", 0);
                i.putExtra("net", false);
                sendBroadcast(i);
            }
        }
    }
}
