package gofereatsdriver.views.main;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main
 * @category MainActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ActivityListener;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.pushnotification.Config;
import gofereatsdriver.pushnotification.NotificationUtils;
import gofereatsdriver.service.GPS_Service;
import gofereatsdriver.utils.BottomNavigationViewHelper;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.MyLocation;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.fragments.AccountFragment;
import gofereatsdriver.views.main.fragments.EarningFragment;
import gofereatsdriver.views.main.fragments.HomeFragment;
import gofereatsdriver.views.main.home.RequestReceiveActivity;

import static gofereatsdriver.utils.Enums.REQ_CHECK_STATUS;
import static gofereatsdriver.utils.Enums.REQ_UPDATE_DEVICE_ID;
import static gofereatsdriver.utils.Enums.REQ_UPDATE_LOCATION;

/**************************************************************
 MainActivity page
 Its main page to connected to all the screen pages
 *************************************************************** */
public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, ActivityListener, ServiceListener {
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CustomDialog customDialog1;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    RunTimePermission runTimePermission;
    public int count = 1;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.tvDriverStatus)
    CustomTextView tvDriverStatus;
    public @InjectView(R.id.tvCheckDriverStatus)
    CustomTextView tvCheckDriverStatus;
    public @InjectView(R.id.swDriverStatus)
    SwitchCompat swDriverStatus;
    public @InjectView(R.id.frameMain)
    FrameLayout frameMain;
    public @InjectView(R.id.navMain)
    BottomNavigationView navMain;
    public @InjectView(R.id.ivHomeList)
    ImageView ivHomeList;
    public String type = "";
    /**
     * Get user current location
     */
    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            if (location == null) {
                Log.v("locationnull", "main");
                return;
            }

            sessionManager.setCurrentLat(String.valueOf(location.getLatitude()));
            sessionManager.setCurrentLng(String.valueOf(location.getLongitude()));
            if (sessionManager.getDriverStatus() == -1) {
                sessionManager.setDriverStatus(0);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        swDriverStatus.setChecked(false);
                        tvDriverStatus.setText(getResources().getString(R.string.offline));
                    }
                });

            } else if (sessionManager.getDriverStatus() == 3) {
                swDriverStatus.setVisibility(View.GONE);
                tvDriverStatus.setVisibility(View.GONE);
                tvCheckDriverStatus.setVisibility(View.VISIBLE);
            }
            updateLocation();
        }
    };
    private AlertDialog dialog;
    private BroadcastReceiver mBroadcastReceiver;
    private int backPressed = 0;
    private boolean isPermissionGranted = false;

    /*@OnClick(R.id.tvDriverStatus) public void driverStatus(){
        //sessionManager.setTripId(30);
        Intent requestaccept=new Intent(getApplicationContext(), RequestAcceptActivity.class);
        startActivity(requestaccept);
    }*/

    /**
     * Check User Status
     */
    @OnClick(R.id.tvCheckDriverStatus)
    public void checkstatus() {
        checkStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        type = getIntent().getStringExtra("type");

        if (type == null || type.equals("")) {
            type = "";
        }
        initBottomNavigationView();
        updateDeviceId();
        setSwitch();
        swDriverStatus.setSwitchPadding(40);
        swDriverStatus.setOnCheckedChangeListener(this);
        receivepushnotification();
        //updateLocationTimer();


    }

    /**
     * Start or stop service based on driver status
     **/
    public void setGPSService() {
        if (sessionManager.getDriverStatus() == 1) {
            startGPSService();
        } else {
            stopGPSService();
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

    /**
     * Change switch options
     **/
    public void setSwitch() {
        if (sessionManager.getDriverStatus() == 0) {
            swDriverStatus.setChecked(false);
            tvDriverStatus.setText(getResources().getString(R.string.offline));
        } else if (sessionManager.getDriverStatus() == 1 || sessionManager.getDriverStatus() == 2) {
            swDriverStatus.setChecked(true);
            tvDriverStatus.setText(getResources().getString(R.string.online));
        } else if (sessionManager.getDriverStatus() == 3) {
            swDriverStatus.setVisibility(View.GONE);
            tvDriverStatus.setVisibility(View.GONE);
            tvCheckDriverStatus.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Bottom navigation view to show and set fragment for Home page, Earning page, Rating page, Account page
     */
    public void initBottomNavigationView() {
        if ("account".equals(type)) {
            navMain.setSelectedItemId(R.id.tab_profile);
        }
        BottomNavigationViewHelper.removeShiftMode(navMain);
        // navMain.removeViewAt(2);
        navMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.tab_home:
                        selectedFragment = HomeFragment.newInstance();
                        break;
                    case R.id.tab_earning:
                        selectedFragment = EarningFragment.newInstance();
                        break;
                          /*  case R.id.tab_rating:
                                selectedFragment = RatingFragment.newInstance();
                                break;*/
                    case R.id.tab_profile:
                        selectedFragment = AccountFragment.newInstance();
                        break;
                    default:
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameMain, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        if ("account".equals(type)) {
            //Redirects displaying the Account fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameMain, AccountFragment.newInstance());
            transaction.commit();
        } else {
            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameMain, HomeFragment.newInstance());
            transaction.commit();
        }
    }

    /**
     * Check driver status is online or offline
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.swDriverStatus:
                Log.i("switch_compat", isChecked + "");
                if (swDriverStatus.isChecked()) {
                    tvDriverStatus.setText(getResources().getString(R.string.online));
                    sessionManager.setDriverStatus(1);
                } else {
                    tvDriverStatus.setText(getResources().getString(R.string.offline));
                    sessionManager.setDriverStatus(0);
                }

                updateLocation();
                break;
            default:
                break;
        }
    }

    /**
     * Interface to get resource and instance from fragments
     **/
    @Override
    public Resources getRes() {
        return MainActivity.this.getResources();
    }

    @Override
    public MainActivity getInstance() {
        return MainActivity.this;
    }

    /**
     * While Back button pressed
     **/
    @Override
    public void onBackPressed() {
        if (backPressed >= 1) {
            // startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            super.onBackPressed();
        } else {
            // clean up
            backPressed = backPressed + 1;
            Toast.makeText(this, getResources().getString(R.string.press_back), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update location after get location in home fragment
     */
    public void updateLocationTimer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        }, 5000);
    }

    /**
     * Call API to update location and change status
     */
    public void updateLocation() {
        /**
         *  status 0 for Offline 1 for Online 2 for Trip
         */
        apiService.updateLocation(getLocation()).enqueue(new RequestCallback(REQ_UPDATE_LOCATION, this));
    }

    /**
     * Call API to update restaurant device id
     */
    public void updateDeviceId() {
        /**
         *  Type 0 for Eater 1 for restaurant 2 for Driver
         *  Device type 2 for android and 1 for iOS
         */
        if (TextUtils.isEmpty(sessionManager.getDeviceId())) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            sessionManager.setDeviceId(refreshedToken);
        }
        apiService.updateDeviceId(2, sessionManager.getToken(), "2", sessionManager.getDeviceId()).enqueue(new RequestCallback(REQ_UPDATE_DEVICE_ID, this));
    }

    /**
     * Call API to check driver status
     */
    public void checkStatus() {
        commonMethods.showProgressDialog(this, customDialog1);
        apiService.checkStatus(sessionManager.getToken(),sessionManager.getLanguageCode()).enqueue(new RequestCallback(REQ_CHECK_STATUS, this));
    }


    /**
     * Hash map for update driver status
     **/


    public HashMap<String, String> getLocation() {
        HashMap<String, String> locationHashMap = new HashMap<>();
        locationHashMap.put("latitude", sessionManager.getCurrentLat());
        locationHashMap.put("longitude", sessionManager.getCurrentLng());
        locationHashMap.put("status", String.valueOf(sessionManager.getDriverStatus()));
        locationHashMap.put("token", sessionManager.getToken());
        return locationHashMap;
    }

    /**
     * Get response from API
     **/
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_DEVICE_ID:
                if (!jsonResp.isSuccess() && !TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                else{


                    String currencySymbol=(String)commonMethods.getJsonValue(jsonResp.getStrResponse(), "default_currency_symbol", String.class);
                    System.out.println("Currency Symbol"+currencySymbol);
                    sessionManager.setCurrencySymbol(currencySymbol);
                }
                break;
            case REQ_UPDATE_LOCATION:
                if (jsonResp.isSuccess()) {
                    setGPSService();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if ("The latitude field is required.".equals(jsonResp.getStatusMsg())) {
                        if (sessionManager.getDriverStatus() != -1) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    //showEnablePermissionDailog(1, getString(R.string.please_enable_location));
                                    //checkGpsEnable();
                                    MyLocation.defaultHandler().getLocation(MainActivity.this, locationResult);
                                    updateLocation();
                                }
                            });
                        }
                        /*sessionManager.setDriverStatus(0);
                        setSwitch();
                        //checkGpsEnable();
                        */
                    } else {
                        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    }
                    if (jsonResp.getStatusMsg().equals("Pending signup process / Inactive driver.")) {
                        sessionManager.setDriverStatus(3);
                        setSwitch();
                    }
                }
                break;
            case REQ_CHECK_STATUS:
                if (jsonResp.isSuccess()) {
                    commonMethods.showMessage(this, dialog, getString(R.string.driver_activated));
                    // if(jsonResp.getStatusMsg().equals("active"))
                    driverStatusUpdate();
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

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 150);
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
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(this);
        if (!isGpsEnabled) {
            //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            isPermissionGranted = true;
            MyLocation.defaultHandler().getLocation(this, locationResult);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  if(customDialog.isVisible())
            customDialog.dismiss();*/
        if (!isPermissionGranted)
            //checkAllPermission(Constants.PERMISSIONS_LOCATION);
            count = 1;
        // register FCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        if (sessionManager.getDriverStatus() == 3) {
            swDriverStatus.setVisibility(View.GONE);
            tvDriverStatus.setVisibility(View.GONE);
            tvCheckDriverStatus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
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
                            if (jsonObject.getJSONObject("custom").has("type")) {
                                if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_request")) {
                                    count++;
                                    System.out.println("Order request in main activity ");
                                    Intent requstreceivepage = new Intent(getApplicationContext(), RequestReceiveActivity.class);
                                    //startActivity(requstreceivepage);
                                }
                            } else if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("cancel_trip")) {
                                commonMethods.showMessage(getApplicationContext(), dialog, getRes().getString(R.string.trip_cancel_driver));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };
    }

    public void driverStatusUpdate() {
        sessionManager.setDriverStatus(0);
        setSwitch();
        swDriverStatus.setVisibility(View.VISIBLE);
        tvDriverStatus.setVisibility(View.VISIBLE);
        tvCheckDriverStatus.setVisibility(View.GONE);
    }
    /*private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }*/


}
