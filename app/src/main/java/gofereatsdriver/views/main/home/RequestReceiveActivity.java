package gofereatsdriver.views.main.home;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main
 * @category RequestReceiveActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.accept.AcceptDetailsModel;
import gofereatsdriver.datamodels.accept.AcceptResultModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CircularMusicProgressBar;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.utils.WaveDrawable;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;
import io.github.krtkush.lineartimer.LinearTimer;
import io.github.krtkush.lineartimer.LinearTimerView;

import static gofereatsdriver.utils.Enums.REQ_ACCEPT_REQ;
import static gofereatsdriver.utils.Enums.REQ_CANCEL_REQ;

/*************************************************************
 RequestReceiveActivity
 Its used to get Request Receive Activity for rider with details
 ****************************************************************/

public class RequestReceiveActivity extends AppCompatActivity implements LinearTimer.TimerListener, ServiceListener {

    public AlertDialog dialog;
    public WaveDrawable waveDrawable;
    public @InjectView(R.id.cprogressbar)
    CircularMusicProgressBar cprogressbar;
    public @InjectView(R.id.linearTimer)
    LinearTimerView linearTimerView;
    public @InjectView(R.id.rrdlayout)
    RelativeLayout request_receive_dialog_layout;
    public @InjectView(R.id.map_snap)
    ImageView map_snap;
    public @InjectView(R.id.tvReqMin)
    CustomTextView tvReqMin;
    public @InjectView(R.id.tvReqAddress)
    CustomTextView tvReqAddress;
    public @InjectView(R.id.tvReqRestaurantName)
    CustomTextView tvReqRestaurantName;

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    Gson gson;
    public MediaPlayer mPlayer;
    public int count = 1;
    public int requestId = 0;
    public int order_Id = 0;
    public ProgressDialog pd;
    private LinearTimer linearTimer;
    private long duration = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_request_receive);

        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        dialog = commonMethods.getAlertDialog(this);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        getRequestData();
        /*circularProgressfunction();
        initCircularProgressBar();*/
    }

    /**
     * Get Request From Push Notification
     */

    public void getRequestData() {
        String JSON_DATA = sessionManager.getPushNotification();
        String status = "";

                    /*
                    *   Get Rider details and request details
                    */
        if (JSON_DATA != null && !TextUtils.isEmpty(JSON_DATA)) {
            try {

                JSONObject jsonObject = new JSONObject(JSON_DATA);
                if (jsonObject.getJSONObject("custom").has("type")) {

                    status = jsonObject.getJSONObject("custom").getString("type");
                }
                if (status.equalsIgnoreCase("order_request")) {
                    String min = jsonObject.getJSONObject("custom").getJSONObject("request_data").getString("min_time");
                    requestId = jsonObject.getJSONObject("custom").getJSONObject("request_data").getInt("request_id");
                    order_Id = jsonObject.getJSONObject("custom").getJSONObject("request_data").getInt("order_id");
                    String pickup_address = jsonObject.getJSONObject("custom").getJSONObject("request_data").getString("pickup_location");
                    String ReqRestaurantName = jsonObject.getJSONObject("custom").getJSONObject("request_data").getString("store");

                    if (Integer.parseInt(min) > 1) {
                        tvReqMin.setText(min + " " + getResources().getString(R.string.minutes));
                    } else {
                        tvReqMin.setText(min + " " + getResources().getString(R.string.minute));
                    }
                    tvReqRestaurantName.setText(ReqRestaurantName);
                    tvReqAddress.setText(pickup_address);
                }


                String lat = jsonObject.getJSONObject("custom").getJSONObject("request_data").getString("pickup_latitude");
                String lng = jsonObject.getJSONObject("custom").getJSONObject("request_data").getString("pickup_longitude");
                String pickupstr = lat + "," + lng;
                //String positionOnMap = "&markers=size:mid|icon:" + getResources().getString(R.string.imageurl) + "man_marker.png|" + pickupstr;
                String positionOnMap = "&markers=size:mid|icon:" + getResources().getString(R.string.imageurl) + "shopping_bag.png|" + pickupstr;

                String staticMapURL = "https://maps.googleapis.com/maps/api/staticmap?size=250x250&" + lat + "," + lng + "" + positionOnMap + "&zoom=16.5" + "&key=" + getResources().getString(R.string.google_maps_key);

                imageUtils.loadCircleImage(this, cprogressbar, staticMapURL);
                circularProgressfunction();
                initCircularProgressBar();
                sessionManager.setPushNotification("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            finish();
        }
    }

    /**
     * Call API to notAccept the request
     */
    public void notAcceptRequest() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.notAcceptRequest(order_Id, requestId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_CANCEL_REQ, this));
    }

    /**
     * Call API to accept the request
     */
    public void acceptRequest() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.acceptRequest(order_Id, requestId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_ACCEPT_REQ, this));
    }

    /**
     * Initialize progress bar
     */
    public void initCircularProgressBar() {
        cprogressbar.setValue(100);

        cprogressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    linearTimer.pauseTimer();
                    //linearTimer.resetTimer();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                waveDrawable.stopAnimation();

                try {
                    mPlayer.stop();
                }catch (Exception e) {
                    mPlayer.release();
                }


                acceptRequest();
                /*pd = new ProgressDialog(RequestReceiveActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("Accepting pickup...");
                pd.setCancelable(false);
                pd.show();*/

            }
        });
    }

    /**
     * After given time automatically to stop animation
     */
    @Override
    public void animationComplete() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        waveDrawable.stopAnimation();
        try {
            mPlayer.stop();
        }catch (Exception e) {
            mPlayer.release();
        }

        //notAcceptRequest();
        finish();
    }

    /**
     * Animation time
     */
    @Override
    public void timerTick(long tickUpdateInMillis) {
        Log.i("Time left", String.valueOf(tickUpdateInMillis));
        mPlayer = MediaPlayer.create(this, R.raw.gofer);
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    @Override
    public void onTimerReset() {
        System.out.print("Time Reset");
    }

    /**
     * Request page circular animation function
     */
    public void circularProgressfunction() {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        int radius;
        if (width < height) radius = (int) (width / 2.2);
        else radius = (int) (height / 2.2);
        //radius-= 4;
        radius = (int) (radius / getResources().getDisplayMetrics().density);

        waveDrawable = new WaveDrawable(getResources().getColor(R.color.app_green), width - 250);

        request_receive_dialog_layout = findViewById(R.id.rrdlayout);
        linearTimerView = findViewById(R.id.linearTimer);
        map_snap = findViewById(R.id.map_snap);

        ViewTreeObserver vto = linearTimerView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                linearTimerView.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight1 = linearTimerView.getMeasuredHeight();
                int finalWidth1 = linearTimerView.getMeasuredWidth();
                map_snap.getLayoutParams().height = (int) (finalHeight1 / 1.1);
                map_snap.getLayoutParams().width = (int) (finalWidth1 / 1.1);
                map_snap.requestLayout();
                return true;
            }
        });
        linearTimerView.setCircleRadiusInDp(radius);
        count = 1;

        linearTimer = new LinearTimer.Builder().linearTimerView(linearTimerView).duration(duration).timerListener(this).progressDirection(LinearTimer.COUNTER_CLOCK_WISE_PROGRESSION).preFillAngle(0).endingAngle(360).getCountUpdate(LinearTimer.COUNT_UP_TIMER, 1000).build();

        request_receive_dialog_layout.setBackgroundDrawable(waveDrawable);
        Interpolator interpolator = new LinearInterpolator();

        waveDrawable.setWaveInterpolator(interpolator);
        waveDrawable.startAnimation();

        try {
            linearTimerView.clearAnimation();
            linearTimerView.animate();
            linearTimerView.setAnimation(null);
            linearTimer.startTimer();
            linearTimerView.setVisibility(View.GONE);

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_ACCEPT_REQ:
                if (jsonResp.isSuccess()) {
                    onSuccessAccept(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    //commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();
                }
                break;
            case REQ_CANCEL_REQ:
                if (jsonResp.isSuccess()) {
                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();
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

    @Override
    public void onBackPressed() {
        //This Method is Empty Because we have to strict the backpress When Request Receive
    }

    /**
     * Accept on Success
     *
     * @param jsonResponse
     */

    public void onSuccessAccept(JsonResponse jsonResponse) {
        AcceptResultModel acceptResultModel = gson.fromJson(jsonResponse.getStrResponse(), AcceptResultModel.class);
        if (acceptResultModel != null) {
            AcceptDetailsModel acceptDetailsModel = acceptResultModel.getAcceptDetailsModel();
            if (acceptDetailsModel != null) {
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

                sessionManager.setDriverStatus(2);
                sessionManager.setTripStatus(1);
                sessionManager.setTripId(acceptDetailsModel.getOrderId());

                Intent requestaccept = new Intent(getApplicationContext(), RequestAcceptActivity.class);
                requestaccept.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(requestaccept);
                finish();
            }
        } else {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            finish();
        }
    }
}
