package gofereatsdriver.views.main.tripdetails;
/**
 * @package com.gofereats
 * @subpackage views.main.tripdetails
 * @category TripDetails
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.datamodels.trips.TripDetail;
import gofereatsdriver.datamodels.trips.TripDetailResult;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;

/************************************************************
 TripDetails
 Its shows the Drivers Trips Detailly
 ****************************************************************/

public class TripDetails extends AppCompatActivity implements ServiceListener {


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

    public int dayOfWeek;
    public TripDetail tripDetailmodel;
    public Integer tripId;
    public @InjectView(R.id.tvTripAmount)
    CustomTextView tvTripAmount;
    public @InjectView(R.id.tvTripDate)
    CustomTextView tvTripDate;
    public @InjectView(R.id.tvPickupaddress)
    CustomTextView tvPickupaddress;
    public @InjectView(R.id.tvDropaddress)
    CustomTextView tvDropaddress;
    public @InjectView(R.id.tvDistancefare)
    CustomTextView tvDistancefare;
    public @InjectView(R.id.tvTimefare)
    CustomTextView tvTimefare;
    public @InjectView(R.id.tvTotalamount)
    CustomTextView tvTotalamount;
    public @InjectView(R.id.tvAdminamount)
    CustomTextView tvAdminamount;
    public @InjectView(R.id.tvOweamount)
    CustomTextView tvOweamount;
    public @InjectView(R.id.tvDriverpayout)
    CustomTextView tvDriverpayout;
    public @InjectView(R.id.tvDeliveryfee)
    CustomTextView tvDeliveryfee;
    public @InjectView(R.id.tvCashcollectamount)
    CustomTextView tvCashcollectamount;
    public @InjectView(R.id.tvTripduration)
    CustomTextView tvTripduration;
    public @InjectView(R.id.tvTripkm)
    CustomTextView tvTripkm;
    public @InjectView(R.id.amountLay)
    LinearLayout amountLay;
    public @InjectView(R.id.route_image)
    ImageView rimageView;
    public @InjectView(R.id.cancelledImg)
    ImageView cancelledImg;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.adminamountlayout)
    RelativeLayout adminamountlayout;
    public @InjectView(R.id.driverpayoutlayout)
    RelativeLayout driverpayoutlayout;
    public @InjectView(R.id.tvDropFare)
    TextView tvDropFare;
    public @InjectView(R.id.tvPickUpFare)
    TextView tvPickUpFare;
    public @InjectView(R.id.rltPickupFare)
    RelativeLayout rltPickupFare;
    public @InjectView(R.id.rltDropFare)
    RelativeLayout rltDropFare;
    public @InjectView(R.id.rltPenalty)
    RelativeLayout rltPenalty;
    public @InjectView(R.id.lltCancelAmount)
    LinearLayout lltCancelAmount;
    public @InjectView(R.id.tvPenaltyamount)
    TextView tvPenaltyamount;
    public @InjectView(R.id.tvCancelDriverPayoutPrice)
    TextView tvCancelDriverPayoutPrice;
    public @InjectView(R.id.rltCancelDriverPayout)
    RelativeLayout rltCancelDriverPayout;
    public @InjectView(R.id.tvCancelNotes)
    TextView tvCancelNotes;
    public @InjectView(R.id.rltCancelAppliedOwe)
    RelativeLayout rltCancelAppliedOwe;
    public @InjectView(R.id.tvCancelAppliedOweamount)
    TextView tvCancelAppliedOweamount;
    public @InjectView(R.id.rltAppliedOwe)
    RelativeLayout rltAppliedOwe;
    public @InjectView(R.id.tvAppliedOweamount)
    TextView tvAppliedOweamount;
    public @InjectView(R.id.tvTripID)
    TextView tvTripID;
    public @InjectView(R.id.cashcollectamountlayout)
    RelativeLayout cashcollectamountlayout;
    public @InjectView(R.id.view1)
    View view1;
    public @InjectView(R.id.view2)
    View view2;
    public @InjectView(R.id.totalpayoutlayout)
    RelativeLayout totalpayoutlayout;


    public @InjectView(R.id.distancelayout)
    RelativeLayout distancelayout;
    public @InjectView(R.id.oweamountlayout)
    RelativeLayout oweamountlayout;
    private AlertDialog dialog;
    private int tripid;
    private int type;
    private int tripStatus = 2;
    String mins;
    String hrs;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        commonMethods.rotateArrow(ivBack,this);
        getintent();
        tripdetails();
        mins=getResources().getString(R.string.mins);
        hrs=getResources().getString(R.string.hrs);

    }

    /**
     * Call API to get Trip details list for
     */
    public void tripdetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.orderDetail(sessionManager.getToken(), (tripid)).enqueue(new RequestCallback(this));
    }

    /**
     * Get Datas From Intent
     */
    public void getintent() {
        tripid = getIntent().getIntExtra("tripid", 0);
        type = getIntent().getIntExtra("type", 0);
    }

    /**
     * onbackPressed
     */
    @Override
    public void onBackPressed() {
        if (type == 1) {
            Intent back = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(back);
            finish();
        } else {
            super.onBackPressed();
        }

    }

    /**
     * Success Response From API
     *
     * @param jsonResp Json Response from Api
     * @param data     Request data (optional)
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (jsonResp.isSuccess()) {
            onSuccess(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Getting Result From Response
     *
     * @param jsonResponse Response
     */
    public void onSuccess(JsonResponse jsonResponse) {
        TripDetailResult TripResultModel = gson.fromJson(jsonResponse.getStrResponse(), TripDetailResult.class);
        if (TripResultModel != null) {
            tripDetailmodel = TripResultModel.getTripdetails();
            updateview();
        }
    }

    /**
     * Update and set Views
     */
    public void updateview() {
        if (tripDetailmodel != null) {
            if (tripDetailmodel.getTotalfare().equals("")) {
                tvTotalamount.setText(sessionManager.getCurrencySymbol() + "0.00");
            } else {
                tvTotalamount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getTotalfare());
            }
            tvTripAmount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getDriverpayout());
            tvTripDate.setText(tripDetailmodel.getTripdate());
            tvPickupaddress.setText(tripDetailmodel.getPickuplocation());
            tvDropaddress.setText(tripDetailmodel.getDroplocation());


            if (tripDetailmodel.getStatus() != null) {
                tripStatus = Integer.parseInt(tripDetailmodel.getStatus());
            }

            if (tripStatus == 2 || tripStatus == 6) {
                amountLay.setVisibility(View.VISIBLE);
                //lltCancelAmount.setVisibility(View.VISIBLE);
                tvTripAmount.setVisibility(View.GONE);
                cancelledImg.setVisibility(View.VISIBLE);
            } else {
                tvTripAmount.setVisibility(View.VISIBLE);
                amountLay.setVisibility(View.VISIBLE);
                //lltCancelAmount.setVisibility(View.GONE);
                cancelledImg.setVisibility(View.GONE);

            }

            if (tripDetailmodel.getDistance().equals("")) {
                tvTripkm.setText("0 "+getResources().getString(R.string.km));
            } else {
                tvTripkm.setText(tripDetailmodel.getDistance() + " "+getResources().getString(R.string.km));
            }

            if(tripDetailmodel.getDuration_min().equals("1")||tripDetailmodel.getDuration_min().equals("0"))
            {
                mins=getResources().getString(R.string.min);
            }

            if(tripDetailmodel.getDuration_hour().equals("1")||tripDetailmodel.getDuration_hour().equals("0"))
            {
                hrs=getResources().getString(R.string.hr);
            }
            if (tripDetailmodel.getDuration_hour().equals("0")&& tripDetailmodel.getDuration_min().equals("0")) {
                tvTripduration.setText("0 "+mins);
            }
            else if(tripDetailmodel.getDuration_min().equals("0"))
            {
               tvTripduration.setText(tripDetailmodel.getDuration_hour()+" "+hrs);
            }
            else if(tripDetailmodel.getDuration_hour().equals("0")){
                tvTripduration.setText(tripDetailmodel.getDuration_min()+" "+mins);
            }
            else{
                tvTripduration.setText(tripDetailmodel.getDuration_hour()+" "+hrs+" "+tripDetailmodel.getDuration_min()+" "+mins);
            }
            if (tripDetailmodel.getOweamount() != null && !TextUtils.isEmpty(tripDetailmodel.getOweamount()) && Float.valueOf(tripDetailmodel.getOweamount()) > 0) {
                tvOweamount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getOweamount());
            } else {
                oweamountlayout.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getDistanceFare() != null && !TextUtils.isEmpty(tripDetailmodel.getDistanceFare()) && Float.valueOf(tripDetailmodel.getDistanceFare()) > 0) {
                tvDistancefare.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getDistanceFare());
            } else {
                distancelayout.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getAdminPayout() != null && !TextUtils.isEmpty(tripDetailmodel.getAdminPayout()) && Float.valueOf(tripDetailmodel.getAdminPayout()) > 0) {
                if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
                    tvAdminamount.setText("-" + sessionManager.getCurrencySymbol() + tripDetailmodel.getAdminPayout());
                else
                tvAdminamount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getAdminPayout()+"-");
            } else {
                adminamountlayout.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getDriverpayout() != null && !TextUtils.isEmpty(tripDetailmodel.getDriverpayout()) && Float.valueOf(tripDetailmodel.getDriverpayout()) > 0) {
                tvDriverpayout.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getDriverpayout());
            } else {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                driverpayoutlayout.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getDeliveryfee() != null && !TextUtils.isEmpty(tripDetailmodel.getDeliveryfee()) && Float.valueOf(tripDetailmodel.getDeliveryfee()) > 0) {
                tvDeliveryfee.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getDeliveryfee());
            } else {
                driverpayoutlayout.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getPickupFare() != null && !TextUtils.isEmpty(tripDetailmodel.getPickupFare()) && Float.valueOf(tripDetailmodel.getPickupFare()) > 0) {
                tvPickUpFare.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getPickupFare());
            } else {
                rltPickupFare.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getDropFare() != null && !TextUtils.isEmpty(tripDetailmodel.getDropFare()) && Float.valueOf(tripDetailmodel.getDropFare()) > 0) {
                tvDropFare.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getDropFare());
            } else {
                rltDropFare.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getAppliedOwe() != null && !TextUtils.isEmpty(tripDetailmodel.getAppliedOwe()) && Float.valueOf(tripDetailmodel.getAppliedOwe()) > 0) {
                if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
                    tvAppliedOweamount.setText("-" + sessionManager.getCurrencySymbol() + tripDetailmodel.getAppliedOwe());
                else
                    tvAppliedOweamount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getAppliedOwe()+"-" );
            } else {
                rltAppliedOwe.setVisibility(View.GONE);
            }

            if (tripDetailmodel.getTotalfare() != null && !TextUtils.isEmpty(tripDetailmodel.getTotalfare()) && Float.valueOf(tripDetailmodel.getTotalfare()) > 0) {
                tvTotalamount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getTotalfare());
            } else {
                totalpayoutlayout.setVisibility(View.GONE);
            }


            
            /**
             * These Amounts Will Show Only When Cancel Status
             */
            if (tripDetailmodel.getCancelledPayout() != null && !TextUtils.isEmpty(tripDetailmodel.getCancelledPayout()) && (tripStatus == 2 || tripStatus == 6)) {
                tvCancelDriverPayoutPrice.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getCancelledPayout());
            } else {
                rltCancelDriverPayout.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getDriverPenalty() != null && !TextUtils.isEmpty(tripDetailmodel.getDriverPenalty()) && Float.valueOf(tripDetailmodel.getDriverPenalty()) > 0) {
                tvPenaltyamount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getDriverPenalty());
            } else {
                rltPenalty.setVisibility(View.GONE);
            }
            if (tripDetailmodel.getAppliedPenality() != null && !TextUtils.isEmpty(tripDetailmodel.getAppliedPenality()) && Float.valueOf(tripDetailmodel.getAppliedPenality()) > 0) {
                tvCancelAppliedOweamount.setText("-" + sessionManager.getCurrencySymbol() + tripDetailmodel.getAppliedPenality());
            } else {
                rltCancelAppliedOwe.setVisibility(View.GONE);
            }


            if (tripDetailmodel.getCancelNotes() != null && !TextUtils.isEmpty(tripDetailmodel.getCancelNotes())) {
                SpannableStringBuilder spanTxt = new SpannableStringBuilder(getResources().getString(R.string.notes));
                spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_red)), spanTxt.length() - getResources().getString(R.string.notes).length(), spanTxt.length(), 0);
                spanTxt.append(" ");
                spanTxt.append(tripDetailmodel.getCancelNotes());
                tvCancelNotes.setMovementMethod(LinkMovementMethod.getInstance());
                tvCancelNotes.setText(spanTxt, TextView.BufferType.SPANNABLE);
                //tvCancelNotes.setText(getResources().getString(R.string.notes) + ": " + tripDetailmodel.getCancelNotes());
            } else {
                tvCancelNotes.setVisibility(View.GONE);
            }

            if (tripDetailmodel.getCashCollected() != null && !TextUtils.isEmpty(tripDetailmodel.getCashCollected()) && Float.valueOf(tripDetailmodel.getCashCollected()) > 0) {
                tvCashcollectamount.setText(sessionManager.getCurrencySymbol() + tripDetailmodel.getCashCollected());
            } else {
                cashcollectamountlayout.setVisibility(View.GONE);
            }

            tvTripID.setText(getResources().getString(R.string.trip_id) + tripDetailmodel.getOrderid());

            imageUtils.loadImage(this, rimageView, tripDetailmodel.getMapimage());
        }
        tvTitle.setText(getResources().getString(R.string.tripsdetails));
        vBottom.setVisibility(View.VISIBLE);
    }

}