package gofereatsdriver.views.main.home;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main
 * @category RequestAcceptActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.backgroundtask.ImageCompressAsyncTask;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.Constants;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.accept.AcceptDetailsModel;
import gofereatsdriver.datamodels.accept.AcceptOrderItemModel;
import gofereatsdriver.datamodels.accept.AcceptResultModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.drawpolyline.DownloadTask;
import gofereatsdriver.drawpolyline.PolylineOptionsInterface;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ImageListener;
import gofereatsdriver.interfaces.LatLngInterpolator;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.pushnotification.Config;
import gofereatsdriver.pushnotification.NotificationUtils;
import gofereatsdriver.service.GPS_Service;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.MyLocation;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;
import gofereatsdriver.views.main.commondialog.ShowDialog;
import gofereatsdriver.views.main.pickuporderdetails.DeliveryEnrouteActivity;
import gofereatsdriver.views.main.pickuporderdetails.PickupOrderActivity;
import gofereatsdriver.views.main.rating.RatingStep1Activity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static gofereatsdriver.utils.Enums.REQ_COMPLETE_TRIP;
import static gofereatsdriver.utils.Enums.REQ_GET_TRIP;
import static gofereatsdriver.utils.Enums.REQ_START_TRIP;

/*************************************************************
 Request Accept Activity
 Its used to get Request Accept Activity for eater and restaurant with details
 ****************************************************************/

public class RequestAcceptActivity extends AppCompatActivity implements ImageListener, OnMapReadyCallback, ServiceListener {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 1000;
    public static boolean isTripCancelled = false;
    public final ArrayList movepoints = new ArrayList();
    public GoogleMap mGoogleMap;
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
    public AcceptDetailsModel acceptDetailsModel;
    public ArrayList<AcceptOrderItemModel> itemModelArrayList;
    public int count = 1;
    public LatLng newLatLng = null;
    public ArrayList markerPoints = new ArrayList();
    public Marker carmarker;
    public float startbear = 0, endbear = 0;
    public Marker marker;
    public boolean samelocation = false;
    public float speed = 13f;
    public boolean isFirst = true;
    public String imagePath = "";
    public int status;
    public Polyline polyline;
    public ValueAnimator valueAnimator;
    public DecimalFormat twoDForm;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.mapview)
    MapView mMapView;
    public @InjectView(R.id.tvAddress)
    CustomTextView tvAddress;
    public @InjectView(R.id.tvName)
    CustomTextView tvName;
    public @InjectView(R.id.tvpickordrop)
    CustomTextView tvpickordrop;
    public @InjectView(R.id.tvpickup)
    CustomTextView tvpickup;
    public @InjectView(R.id.tvTripstatus)
    CustomTextView tvTripstatus;
    /*public @InjectView(R.id.tvFrom)
    CustomTextView tvFrom;
    public @InjectView(R.id.tvOdId)
    CustomTextView tvOdId;*/
    public @InjectView(R.id.ivPen)
    ImageView ivPen;
    public @InjectView(R.id.ivNext)
    ImageView ivNext;
    public @InjectView(R.id.btnTripstatus)
    Button btnTripstatus;
    public @InjectView(R.id.rltUserdetails)
    RelativeLayout rltUserdetails;
    /*public @InjectView(R.id.lltpickupinst)
    RelativeLayout lltpickupinst;*/
    public @InjectView(R.id.rltstatus)
    RelativeLayout rltstatus;
    /*public @InjectView(R.id.lltOrderList)
    LinearLayout lltOrderList;*/
    public @InjectView(R.id.lltnavigation)
    LinearLayout lltnavigation;
    public @InjectView(R.id.rltOrderDetail)
    RelativeLayout rltOrderDetail;
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
    private AlertDialog dialog;
    private BroadcastReceiver mBroadcastReceiver;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d("", String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                //drawMarker(location);
                changeMap(location);
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

    /**
     * getBitMap Image for Url
     *
     * @param src
     * @return
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * begin trip
     */
    @OnClick(R.id.btnTripstatus)
    public void starttrip() {
        int status = sessionManager.getTripStatus();
        if (status == 1) {
            startTrip();
        } else if (status == 3 && !TextUtils.isEmpty(acceptDetailsModel.getCollectCash())) {
            Intent i = new Intent(this, RatingStep1Activity.class);
            i.putExtra("orderId", acceptDetailsModel.getOrderId());
            startActivity(i);
        } else if (status == 4 && isPermission(Constants.PERMISSIONS_RESTORAGE)) {
            commonMethods.showProgressDialog(this, customDialog);
            drawStaticMap();
            //status=5;
            //sessionManager.setTripStatus(status);
        }
    }

    /**
     * order and Eater details
     */
    @OnClick(R.id.rltUserdetails)
    public void enroute() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("menu_item", acceptDetailsModel);
        Intent i = new Intent(this, DeliveryEnrouteActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    /**
     * Trip Status
     */
    @OnClick(R.id.rltstatus)
    public void changestatus() {
        int status = sessionManager.getTripStatus();
        if (status == 0 || status == 5) {
            Bundle bundle = new Bundle();
            itemModelArrayList = acceptDetailsModel.getOrderItems();
            bundle.putSerializable("menu_item", itemModelArrayList);
            Intent i = new Intent(this, PickupOrderActivity.class);
            i.putExtra("orderId", acceptDetailsModel.getOrderId());
            i.putExtra("eatername", acceptDetailsModel.getEaterName());
            i.putExtra("restaurant", acceptDetailsModel.getRestaurantName());
            i.putExtras(bundle);
            startActivity(i);
        } else if (status == 3) {
            Intent i = new Intent(this, RatingStep1Activity.class);
            i.putExtra("orderId", acceptDetailsModel.getOrderId());
            startActivity(i);
        }
        if (acceptDetailsModel != null) {
            updateView();
        }
    }

    /**
     * navigation button
     */
    @OnClick(R.id.lltnavigation)
    public void navigate() {
        Double lat = Double.valueOf(acceptDetailsModel.getDropLatitude());
        Double lon = Double.valueOf(acceptDetailsModel.getDropLongitude());
        if (status == 0 || status == 1) {
            lat = Double.valueOf(acceptDetailsModel.getPickupLatitude());
            lon = Double.valueOf(acceptDetailsModel.getPickupLongitude());
        }
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @OnClick(R.id.rltOrderDetail)
    public void orderDetails() {
        showOrderDetails(acceptDetailsModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_request_accept);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);


        mMapView.onCreate(savedInstanceState);
        //checkAllPermission(Constants.PERMISSIONS_RESTORAGE);

        twoDForm = new DecimalFormat("#.##########");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);


        //mGoogleMap = mMapView.getMap();
        mMapView.getMapAsync(this); //this is important
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initMap();
        getCurrentLocation();
        receivepushnotification();
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            String languageToLoad = "ar";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
        }
        //getTripDetails();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        //stopGPSService();
    }

    @Override
    public void onResume() {
        super.onResume();
        startGPSService();
        mMapView.onResume();
        getCurrentLocation();
        getTripDetails();


        // register FCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        //checkAllPermission(Constants.PERMISSIONS_STORAGE);
        /*if (acceptDetailsModel != null) {
            updateView();
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mLocationManager.removeUpdates(mLocationListener);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * inintalize Map
     */
    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, this, -1).show();
            finish();
        } else {
            if (mGoogleMap != null) {
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
            if (isNetworkEnabled) {
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
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d("", String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
            sessionManager.setCurrentLat(String.valueOf(location.getLatitude()));
            sessionManager.setCurrentLng(String.valueOf(location.getLongitude()));
            changeMap(location);
            // drawMarker(location);
        }
    }

    /**
     * Get driver current location in map
     **/
    private void changeMap(Location location) {


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
          /*  float targetBearing;
            if (newLatLng == null) {
                targetBearing = bearingBetweenLatLngs(mGoogleMap.getCameraPosition().target, latLong);
            } else {
                targetBearing = bearingBetweenLatLngs(newLatLng, latLong);
            }*/
            newLatLng = latLong;

        } /*else {
            Toast.makeText(this,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }


    /****************************************************************** */
    /**                  Animate Marker for Live Tracking               */
    /****************************************************************** */


    private boolean isPermission(String[] permission) {

        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(RequestAcceptActivity.this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(RequestAcceptActivity.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(RequestAcceptActivity.this, permission, 150);
            }
            return false;
        } else {
            return true;
        }

    }

    /**
     * Check service is running or not
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
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

        double distance = Double.valueOf(twoDForm.format(startbearlocation.distanceTo(endbearlocation)));
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            twoDForm = new DecimalFormat("#.####");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
            dfs.setDecimalSeparator('.');
            twoDForm.setDecimalFormatSymbols(dfs);
            String arabicDouble=twoDForm.format(distance);
            arabicToDecimal(arabicDouble);
            System.out.println("Decimal Value "+arabicToDecimal(arabicDouble));
        }

        if (distance > 0) animateMarker(latlng1, marker, speed, endbear);

        twoDForm = new DecimalFormat("#.####");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        String oldlat = twoDForm.format(latlng.latitude);
        double ola = Double.valueOf(oldlat);
        String oldlong = twoDForm.format(latlng.longitude);
        double olo = Double.valueOf(oldlong);
        String newlat = twoDForm.format(latlng1.latitude);
        double nla = Double.valueOf(newlat);
        String newlong = twoDForm.format(latlng1.longitude);
        double nlo = Double.valueOf(newlong);

        if (ola != nla && olo != nlo && markerPoints.size() > 1) {
            drawRoute(latlng);
        }
    }
    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
    /**
     * Update route while trip (Car moving)
     */
    public void drawRoute(LatLng driverlatlng) {

        LatLng pickupLatlng = (LatLng) markerPoints.get(0);
        LatLng dropLatlng = (LatLng) markerPoints.get(1);


        // Add new marker to the Google Map Android API V2

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.
        builder.include(driverlatlng);
        if (status >= 3) {
            builder.include(dropLatlng);
        } else {
            builder.include(pickupLatlng);
        }

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels / 2;
        int height = getResources().getDisplayMetrics().heightPixels / 2;
        int padding = (int) (width * 0.08); // offset from edges of the map 10% of screen

        if (isFirst) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mGoogleMap.moveCamera(cu);
            isFirst = false;
        }

        String url;
        if (status >= 3) {
            // Getting URL to the Google Directions API
            url = getDirectionsUrl(driverlatlng, dropLatlng);
        } else {
            url = getDirectionsUrl(driverlatlng, pickupLatlng);
        }

        DownloadTask downloadTask = new DownloadTask(new PolylineOptionsInterface() {
            @Override
            public void getPolylineOptions(PolylineOptions output, ArrayList points) {

                if (mGoogleMap != null && output != null) {
                    if (polyline != null) polyline.remove();
                    polyline = mGoogleMap.addPolyline(output);
                } else {
                    Toast.makeText(getApplicationContext(), "Map or route not ready", Toast.LENGTH_LONG).show();
                }
            }
        });
        Log.v("Downloadd", "task");
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    /**
     * Update route while trip (Car moving)
     */
    public void drawMarker() {

        mGoogleMap.clear();

        LatLng pickuplatlng = (LatLng) markerPoints.get(0);
        LatLng droplatlng = (LatLng) markerPoints.get(1);
        LatLng driverlatlng = new LatLng(Double.valueOf(sessionManager.getCurrentLat()), Double.valueOf(sessionManager.getCurrentLng()));


        // Creating MarkerOptions
        MarkerOptions pickupOptions = new MarkerOptions();

        // Setting the position of the marker

        if (status >= 3) {
            pickupOptions.position(droplatlng);
            pickupOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ub__ic_pin_dropoff));
        } else {
            pickupOptions.position(pickuplatlng);
            pickupOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ub__ic_pin_pickup));
        }
        // Add new marker to the Google Map Android API V2
        mGoogleMap.addMarker(pickupOptions);

        // Creating MarkerOptions
        MarkerOptions dropOptions = new MarkerOptions();


        // Setting the position of the marker
        dropOptions.position(driverlatlng);
        dropOptions.anchor(0.5f, 0.5f);
        dropOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gf_moto_bike));
        // Add new marker to the Google Map Android API V2
        carmarker = mGoogleMap.addMarker(dropOptions);

        drawRoute(driverlatlng);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(driverlatlng).zoom(16.5f).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * get direction for given origin, dest
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + getResources().getString(R.string.google_maps_key);

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
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


            if (("updated").equals(type)) {
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
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(RequestAcceptActivity.this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(RequestAcceptActivity.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(RequestAcceptActivity.this, permission, 150);
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
            commonMethods.showProgressDialog(this, customDialog);
            drawStaticMap();
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
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * Open permission dialog
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(this);
        if (!isGpsEnabled) {
            //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            MyLocation.defaultHandler().getLocation(this, locationResult);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMap();
        getCurrentLocation();
    }

    /**
     * FCM push nofication received funcation called
     */
    public void receivepushnotification() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // FCM successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    //String message = intent.getStringExtra("message");

                    String JSON_DATA = sessionManager.getPushNotification();

                    if (JSON_DATA != null && count == 1) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(JSON_DATA);
                            String title = jsonObject.getJSONObject("custom").getString("title");
                            int tripId = Integer.parseInt(jsonObject.getJSONObject("custom").getString("order_id"));
                            if (jsonObject.getJSONObject("custom").has("type")) {
                                if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_request")) {
                                    count++;
                                    Intent requstreceivepage = new Intent(getApplicationContext(), RequestReceiveActivity.class);
                                    startActivity(requstreceivepage);
                                }
                            } else if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_cancelled")) {
                                sessionManager.setTripId(0);
                                sessionManager.setTripStatus(0);
                                Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                                dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                dialogs.putExtra("message", title);
                                dialogs.putExtra("type", 2);
                                dialogs.putExtra("tripid", tripId);
                                startActivity(dialogs);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    /**
     * Call API to get accepted or pending trip details
     */
    public void getTripDetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.getOrderDetails(sessionManager.getTripId(), sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_TRIP, this));
    }

    /**
     * Call API to get accepted or pending trip details
     */
    public void startTrip() {

        try {
            GPS_Service gps_service = new GPS_Service();
            LatLng newLatLng = new LatLng(Double.valueOf(sessionManager.getCurrentLat()), Double.valueOf(sessionManager.getCurrentLng()));
            GPS_Service.latLngList.add(newLatLng);
        } catch (Exception e) {
            e.printStackTrace();
        }

        commonMethods.showProgressDialog(this, customDialog);
        apiService.startTrip(sessionManager.getToken(), sessionManager.getTripId()).enqueue(new RequestCallback(REQ_START_TRIP, this));
    }

    public void completeDelivery() {
        //commonMethods.showProgressDialog(this, customDialog);
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        File file = null;


        try {

            file = new File(imagePath);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            multipartBody.addFormDataPart("map_image", "IMG_" + timeStamp + ".jpg", RequestBody.create(MediaType.parse("image/png"), file));
            multipartBody.addFormDataPart("token", sessionManager.getToken());
            multipartBody.addFormDataPart("order_id", String.valueOf(sessionManager.getTripId()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody formBody = multipartBody.build();
        //commonMethods.showProgressDialog(this,customDialog);
        apiService.completeDelivery(formBody, sessionManager.getToken()).enqueue(new RequestCallback(REQ_COMPLETE_TRIP, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_GET_TRIP:
                if (jsonResp.isSuccess()) {
                    onSuccessAccept(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_START_TRIP:
                if (jsonResp.isSuccess()) {
                    status = 3;
                    sessionManager.setTripStatus(status);
                    updateView();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_COMPLETE_TRIP:
                if (jsonResp.isSuccess()) {
                    stopGPSService();
                    FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
                    DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("live_tracking");
                    mFirebaseDatabase.child(String.valueOf(sessionManager.getTripId())).removeValue();
                    sessionManager.setDriverStatus(1);
                    sessionManager.setTripStatus(-1);
                    sessionManager.setTripId(0);
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;


        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    public void onSuccessAccept(JsonResponse jsonResponse) {
        AcceptResultModel acceptResultModel = gson.fromJson(jsonResponse.getStrResponse(), AcceptResultModel.class);
        if (acceptResultModel != null) {
            acceptDetailsModel = acceptResultModel.getAcceptDetailsModel();
            if (acceptDetailsModel != null) {
                /*lltOrderList.removeAllViews();
                itemModelArrayList = acceptDetailsModel.getOrderItems();
                for (int i = 0; i < itemModelArrayList.size(); i++) {

                    View view = LayoutInflater.from(this).inflate(R.layout.order_item, null);
                    CustomTextView tvQuantity = (CustomTextView) view.findViewById(R.id.tvQuantity);
                    CustomTextView tvOrderItem = (CustomTextView) view.findViewById(R.id.tvOrderItem);

                    tvQuantity.setText(itemModelArrayList.get(i).getQuantity().toString());
                    tvOrderItem.setText(itemModelArrayList.get(i).getName());
                    lltOrderList.setTag(i);
                    lltOrderList.addView(view);
                }*/

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
                status = acceptDetailsModel.getStatus();
                sessionManager.setTripStatus(status);
                updateView();
            }
        } else {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            finish();
        }
    }

    public void updateView() {
        LatLng pickupLatlng = new LatLng(Double.valueOf(acceptDetailsModel.getPickupLatitude()), Double.valueOf(acceptDetailsModel.getPickupLongitude()));
        LatLng dropLatlng = new LatLng(Double.valueOf(acceptDetailsModel.getDropLatitude()), Double.valueOf(acceptDetailsModel.getDropLongitude()));
        markerPoints.add(0, pickupLatlng);
        markerPoints.add(1, dropLatlng);
        drawMarker();


        int status = sessionManager.getTripStatus();
        if (status != 3 && status != 4 && status != 5 && status != 6) {
            tvAddress.setText(acceptDetailsModel.getPickupLocation());
            tvName.setText(acceptDetailsModel.getRestaurantName());
            tvpickup.setText(acceptDetailsModel.getRestaurantName());
        } else {
            tvAddress.setText(acceptDetailsModel.getDropLocation());
            tvName.setText(acceptDetailsModel.getEaterName());
            tvpickup.setText(acceptDetailsModel.getEaterName());
        }
        if (status == 0) {
            ivPen.setColorFilter(getResources().getColor(R.color.app_green));
            tvTripstatus.setTextColor(getResources().getColor(R.color.app_green));
            ivNext.setColorFilter(R.color.app_green);
            ivNext.setVisibility(View.VISIBLE);
            btnTripstatus.setBackgroundColor(getResources().getColor(R.color.graybutton));
            btnTripstatus.setEnabled(false);
        } else if (status == 1) {
            rltstatus.setEnabled(false);
            ivPen.setColorFilter(getResources().getColor(R.color.graybutton));
            ivPen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.apptheme), android.graphics.PorterDuff.Mode.SRC_IN);
            ivPen.setImageDrawable(getResources().getDrawable(R.drawable.selected_check_mark));
            tvTripstatus.setTextColor(getResources().getColor(R.color.graybutton));
            tvTripstatus.setText(R.string.orders_confirmed);
            ivNext.setColorFilter(R.color.graybutton);
            ivNext.setVisibility(View.GONE);
            btnTripstatus.setEnabled(true);
            //btnTripstatus.setBackgroundColor(getResources().getColor(R.color.ub__uber_green_80));
            btnTripstatus.setBackground(getResources().getDrawable(R.drawable.background_green));
        } else if (status == 3) {
            rltstatus.setEnabled(true);
            ivPen.setColorFilter(getResources().getColor(R.color.app_green));
            ivPen.setImageDrawable(getResources().getDrawable(R.drawable.pencil));
            tvTripstatus.setTextColor(getResources().getColor(R.color.app_green));
            tvTripstatus.setText(R.string.confirmdropoff);
            ivNext.setColorFilter(R.color.app_green);
            if (!TextUtils.isEmpty(acceptDetailsModel.getCollectCash()) && Float.valueOf(acceptDetailsModel.getCollectCash()) > 0) {
                btnTripstatus.setBackground(getResources().getDrawable(R.drawable.background_green));
                btnTripstatus.setEnabled(true);
                btnTripstatus.setTextSize(15f);
                if("0".equalsIgnoreCase(getString(R.string.layout_direction)))
                    btnTripstatus.setText(getResources().getString(R.string.collect_cash) + " (" + sessionManager.getCurrencySymbol() + acceptDetailsModel.getCollectCash() + ")");
                else
                    btnTripstatus.setText(getResources().getString(R.string.collect_cash) + " ("+ acceptDetailsModel.getCollectCash()  + sessionManager.getCurrencySymbol() + ")");
                rltstatus.setVisibility(View.GONE);
            } else {
                btnTripstatus.setBackgroundColor(getResources().getColor(R.color.graybutton));
                btnTripstatus.setEnabled(false);
                btnTripstatus.setText(R.string.completetrip);
                rltstatus.setVisibility(View.VISIBLE);
            }
            rltOrderDetail.setVisibility(View.VISIBLE);
            ivNext.setVisibility(View.VISIBLE);
            tvpickordrop.setText(getString(R.string.dropoff));
            tvpickordrop.setTextColor(getResources().getColor(R.color.ub__red));
        } else if (status == 4) {
            ivPen.setColorFilter(getResources().getColor(R.color.graybutton));
            ivPen.setImageDrawable(getResources().getDrawable(R.drawable.pencil));
            tvTripstatus.setTextColor(getResources().getColor(R.color.graybutton));
            tvTripstatus.setText(R.string.dropoff_confirmed);
            ivNext.setVisibility(View.GONE);
            ivNext.setColorFilter(R.color.graybutton);
            //btnTripstatus.setBackgroundColor(getResources().getColor(R.color.ub__red));
            btnTripstatus.setBackground(getResources().getDrawable(R.drawable.background_red));
            btnTripstatus.setEnabled(true);
            btnTripstatus.setText(R.string.completetrip);
            rltOrderDetail.setVisibility(View.VISIBLE);
            //lltpickupinst.setVisibility(View.VISIBLE);
            //tvOdId.setText(acceptDetailsModel.getOrderId().toString());
            // int l=acceptDetailsModel.getOrder_items().length;
            // tvOrderItem.setText(l+"   "+acceptDetailsModel.getOrder_items()[0]);
            //tvFrom.setText(acceptDetailsModel.getRestaurantName());
            tvpickup.setText(acceptDetailsModel.getEaterName());
            tvpickordrop.setText(getString(R.string.dropoff));
            tvpickordrop.setTextColor(getResources().getColor(R.color.ub__red));

            /*if (!TextUtils.isEmpty(acceptDetailsModel.getCollectCash())&&Float.valueOf(acceptDetailsModel.getCollectCash())>0){
                btnTripstatus.setBackground(getResources().getDrawable(R.drawable.background_green));
                btnTripstatus.setEnabled(true);
                btnTripstatus.setText(getResources().getString(R.string.collect_cash)+"("+sessionManager.getCurrencySymbol()+acceptDetailsModel.getCollectCash()+")");
            }else {
                btnTripstatus.setBackground(getResources().getDrawable(R.drawable.background_red));
                btnTripstatus.setEnabled(true);
                btnTripstatus.setText(R.string.completetrip);
            }*/
        }

    }

    /**
     * Start service based on driver status
     **/
    public void startGPSService() {
        if (!isMyServiceRunning(GPS_Service.class)) {
            Intent GPSservice = new Intent(getApplicationContext(), GPS_Service.class);
            startService(GPSservice);
        }
    }

    /**
     * Stop service based on driver status
     **/
    public void stopGPSService() {
        if (isMyServiceRunning(GPS_Service.class)) {
            Intent GPSservice = new Intent(getApplicationContext(), GPS_Service.class);
            stopService(GPSservice);
        }
    }

    public void drawStaticMap() {

        GPS_Service gps_service = new GPS_Service();
        try {
            LatLng newLatLng = new LatLng(Double.valueOf(sessionManager.getCurrentLat()), Double.valueOf(sessionManager.getCurrentLng()));
            GPS_Service.latLngList.add(newLatLng);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<LatLng> newlatLngList = GPS_Service.latLngList;

        int j = 1;
        String trip_path = "";
        if (newlatLngList.size() > 100) {
            j = newlatLngList.size() / 100;
        }

        for (int i = 0; i < newlatLngList.size(); i = i + j) {
            trip_path = trip_path + "|" + newlatLngList.get(i).latitude + "," + newlatLngList.get(i).longitude;
        }

        LatLng pickuplatlng = new LatLng(newlatLngList.get(0).latitude, newlatLngList.get(0).longitude);
        LatLng droplatlng = new LatLng(newlatLngList.get(newlatLngList.size() - 1).latitude, newlatLngList.get(newlatLngList.size() - 1).longitude);


        String pathString = "&path=color:0x000000ff|weight:4" + trip_path;
        String pickupstr = pickuplatlng.latitude + "," + pickuplatlng.longitude;
        String dropstr = droplatlng.latitude + "," + droplatlng.longitude;
        String positionOnMap = "&markers=size:mid|icon:" + getResources().getString(R.string.imageurl) + "map_green.png|" + pickupstr;
        String positionOnMap1 = "&markers=size:mid|icon:" + getResources().getString(R.string.imageurl) + "map.png|" + dropstr;


        String staticMapURL = "https://maps.googleapis.com/maps/api/staticmap?size=640x250&" + pickuplatlng.latitude + "," + pickuplatlng.longitude + pathString + "" + positionOnMap + "" + positionOnMap1 + //"&zoom=16.5" +
                "&key=" + getResources().getString(R.string.google_maps_key);

        Bitmap bm = getBitmapFromURL(staticMapURL);
        imagePath(bm);

    }

    private void imagePath(Bitmap thumbnail) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");


        Uri selectedImage = getImageUri(getApplicationContext(), thumbnail);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};


        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        cursor.close();


        imagePath = picturePath;


        new ImageCompressAsyncTask(10, RequestAcceptActivity.this, imagePath, this, 0).execute();

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Getting image uri from bitmap
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        //requestbody = requestBody;
        imagePath = filePath;
        completeDelivery();
    }

    private void showOrderDetails(AcceptDetailsModel acceptDetailsModel) {

        if (acceptDetailsModel != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            View dialogView = LayoutInflater.from(this).inflate(R.layout.order_details, null, false);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            RelativeLayout receipt_footer = dialogView.findViewById(R.id.rltClose);
            LinearLayout lltOrderList = dialogView.findViewById(R.id.lltOrderList);
            CustomTextView tvOdId = dialogView.findViewById(R.id.tvOdId);
            CustomTextView tvFrom = dialogView.findViewById(R.id.tvFrom);
            CustomTextView tvCollectCash = dialogView.findViewById(R.id.tvCollectCash);
            View view1 = dialogView.findViewById(R.id.line3);
            if("0".equalsIgnoreCase(getString(R.string.layout_direction)))
                tvOdId.setText("# "+String.valueOf(acceptDetailsModel.getOrderId()));
            else
                tvOdId.setText(String.valueOf(acceptDetailsModel.getOrderId())+" #");
            tvFrom.setText(acceptDetailsModel.getRestaurantName());
            if (!TextUtils.isEmpty(acceptDetailsModel.getCollectCash()) && Float.valueOf(acceptDetailsModel.getCollectCash()) > 0) {
                view1.setVisibility(View.VISIBLE);
                tvCollectCash.setVisibility(View.VISIBLE);
                tvCollectCash.setText(getResources().getString(R.string.collect_cash) + " \t" + acceptDetailsModel.getCollectCash());
            } else {
                view1.setVisibility(View.GONE);
                tvCollectCash.setVisibility(View.GONE);
            }
            receipt_footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            lltOrderList.removeAllViews();
            itemModelArrayList = acceptDetailsModel.getOrderItems();
            for (int i = 0; i < itemModelArrayList.size(); i++) {

                View view = LayoutInflater.from(this).inflate(R.layout.order_item_alert_detail, null);
                CustomTextView tvQuantity = view.findViewById(R.id.tvQuantity);
                CustomTextView tvOrderItem = view.findViewById(R.id.tvOrderItem);

                tvQuantity.setText(String.valueOf(itemModelArrayList.get(i).getQuantity())/*+"X"*/);
                tvOrderItem.setText(itemModelArrayList.get(i).getName());
                lltOrderList.setTag(i);
                lltOrderList.addView(view);
            }
        } else {
            Toast.makeText(this, "Some Error Occurred try again", Toast.LENGTH_SHORT).show();
        }
    }
}
