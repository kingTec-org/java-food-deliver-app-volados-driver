package gofereatsdriver.views.main.paymentstatement;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.paymentstatement
 * @category DailyEarningDetails
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.adapters.main.TripEarnListAdapter;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.earnings.DailyEarningsModel;
import gofereatsdriver.datamodels.earnings.DailyEarningslist;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.datamodels.main.PayModel;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 DailyEarningDetails
 Its used to show Daily Earnings Details for the Driver
 ****************************************************************/

public class DailyEarningDetails extends AppCompatActivity implements ServiceListener {


    public @Inject
    SessionManager sessionManager;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    public String date;
    public DailyEarningsModel dailyEarningsModel;
    public List<DailyEarningslist> dailyEarningslists;
    public List<PayModel> searchlist;
    public TripEarnListAdapter adapter;
    public String Paydate[] = {"5.36 AM", "6.10 AM", "7.05 AM", "9.00AM", "1.00PM", "4.35PM", "6.00PM"};
    public String Payamount[] = {"$92.88", "$82.88", "$72.88", "$9002.88", "$902.88", "$92.88", "$892.88"};
    public @InjectView(R.id.tvDate)
    TextView tvDate;
    public @InjectView(R.id.tvDailyPrice)
    TextView tvDailyPrice;
    public @InjectView(R.id.tvBaseFare)
    TextView tvBaseFare;
    public @InjectView(R.id.tvAccess)
    TextView tvAccess;
    public @InjectView(R.id.tvTotalDriverEarning)
    TextView tvTotalDriverEarning;
    public @InjectView(R.id.tvCashcollectamount)
    TextView tvCashcollectamount;
    public @InjectView(R.id.tvBankDeposit)
    TextView tvBankDeposit;
    public @InjectView(R.id.tvOnlineTime)
    TextView tvOnlineTime;
    public @InjectView(R.id.tvCompletedTrips)
    TextView tvCompletedTrips;
    public @InjectView(R.id.rcView)
    RecyclerView rcView;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.rltBankDeposit)
    RelativeLayout rltBankDeposit;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_earning_details);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        commonMethods.rotateArrow(ivBack,this);
        Intent x = getIntent();
        date = x.getStringExtra("Date");
        tvTitle.setText(getResources().getString(R.string.dailyearning));
        vBottom.setVisibility(View.VISIBLE);
        searchlist = new ArrayList<>();
        rcView.setHasFixedSize(false);
        rcView.setNestedScrollingEnabled(true);
        rcView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //loadData();

        dailyDetails();      //Getting Daily Details

    }

    /**
     * Daily Details Api Call Function
     */
    private void dailyDetails() {

        commonMethods.showProgressDialog(this, customDialog);
        apiService.dailyDetail(sessionManager.getToken(), date).enqueue(new RequestCallback(this));
    }

    public void loadData() {
        for (int i = 0; i < Paydate.length; i++) {
            PayModel listdata = new PayModel();
            listdata.setDailyTrip(Paydate[i]);
            listdata.setTripAmount(Payamount[i]);
            searchlist.add(listdata);
            Log.d("search list.length", String.valueOf(searchlist.size()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }

    /**
     * Success Response Form APIIntent
     *
     * @param jsonResp JsonResponse
     * @param data     Request Data
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
     * Result After In Success
     *
     * @param jsonResponse Json Response  From API
     */

    public void onSuccess(JsonResponse jsonResponse) {

        dailyEarningsModel = gson.fromJson(jsonResponse.getStrResponse(), DailyEarningsModel.class);
        dailyEarningslists = dailyEarningsModel.getDailystatement();

        tvDate.setText(dailyEarningsModel.getDay() + " " + dailyEarningsModel.getFormatdate());
        tvDailyPrice.setText(sessionManager.getCurrencySymbol() + (dailyEarningsModel.getTotalFare()));
        tvBaseFare.setText(sessionManager.getCurrencySymbol() + dailyEarningsModel.getBaseFare());
        if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            tvAccess.setText("-" + sessionManager.getCurrencySymbol() + dailyEarningsModel.getAccessFee());
        else
            tvAccess.setText( sessionManager.getCurrencySymbol() + dailyEarningsModel.getAccessFee()+"-" );
        tvTotalDriverEarning.setText(sessionManager.getCurrencySymbol() + dailyEarningsModel.getTotalFare());
        tvCashcollectamount.setText(sessionManager.getCurrencySymbol() + dailyEarningsModel.getCashCollected());
        tvCompletedTrips.setText(Integer.toString(dailyEarningsModel.getCompletedtrips()));

        if (dailyEarningsModel.getBankDeposits() != null && !TextUtils.isEmpty(dailyEarningsModel.getBankDeposits()) && Float.valueOf(dailyEarningsModel.getBankDeposits()) > 0) {
            tvBankDeposit.setText(sessionManager.getCurrencySymbol() + dailyEarningsModel.getBankDeposits());
            rltBankDeposit.setVisibility(View.VISIBLE);
        } else {
            rltBankDeposit.setVisibility(View.GONE);
        }
        adapter = new TripEarnListAdapter(this, dailyEarningslists);// Setting Adapter
        rcView.setAdapter(adapter);

    }

}