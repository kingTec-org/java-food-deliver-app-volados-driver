package gofereatsdriver.views.main.paymentstatement;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.paymentstatement
 * @category PayStatementDetails
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
import com.obs.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.adapters.main.DailyEarnListAdapter;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.earnings.WeeklyDetailsList;
import gofereatsdriver.datamodels.earnings.WeeklyDetailsModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.datamodels.main.PayModel;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 PayStatementDetails
 Its used to show Payment Details for the Driver
 ****************************************************************/


public class PayStatementDetails extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    public @Inject
    SessionManager sessionManager;

    public @InjectView(R.id.rcView)
    RecyclerView rcView;
    public @InjectView(R.id.basefare_amount)
    CustomTextView basefare_amount;
    public @InjectView(R.id.accessfee_amount)
    CustomTextView accessfee_amount;
    public @InjectView(R.id.cash_collected)
    CustomTextView cash_collected;
    public @InjectView(R.id.bank_deposit)
    CustomTextView bank_deposit;
    public @InjectView(R.id.online_time)
    CustomTextView online_time;
    public @InjectView(R.id.completed_trips)
    CustomTextView completed_trips;
    public @InjectView(R.id.tvTripDate)
    CustomTextView tvTripDate;
    public @InjectView(R.id.tvTripAmount)
    CustomTextView tvTripAmount;
    public @InjectView(R.id.tvTotalDriverEarning)
    CustomTextView tvTotalDriverEarning;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.rltBankDeposit)
    RelativeLayout rltBankDeposit;

    public String Paydate[] = {"Sunday 03/06", "Monday 04/06 ", "Tuesday 04/06", "Wednesday 05/06", "Thursday 06/06", "Friday 07/06", "Saturday 08/06"};
    public String Payamount[] = {"$92.88", "$82.88", "$72.88", "$9002.88", "$902.88", "$92.88", "$892.88"};
    public WeeklyDetailsModel weeklyDetailsModel;
    public List<WeeklyDetailsList> weeklyDetailsLists;
    public String date;
    public List<PayModel> searchlist;
    public DailyEarnListAdapter adapter;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paystatement_details);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        commonMethods.rotateArrow(ivBack,this);
        Intent x = getIntent();
        date = x.getStringExtra("weekDate");
        tvTitle.setText(getResources().getString(R.string.weeklystatement));
        vBottom.setVisibility(View.VISIBLE);
        searchlist = new ArrayList<>();
        rcView.setHasFixedSize(false);
        rcView.setNestedScrollingEnabled(true);
        rcView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        // loadData();
        weeklyDetails();

    }

    /**
     * Weekly Details Api Calling Method
     */
    private void weeklyDetails() {
        commonMethods.showProgressDialog(this, customDialog);

        apiService.weeklyDetail(sessionManager.getToken(), date).enqueue(new RequestCallback(this));
    }

    //Data Loading Function
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

    /**
     * ON Success Result From API
     *
     * @param jsonResp JsonResponse from API
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
     * Result After success From API
     *
     * @param jsonResponse Success JsonResponse
     */
    public void onSuccess(JsonResponse jsonResponse) {

        weeklyDetailsModel = gson.fromJson(jsonResponse.getStrResponse(), WeeklyDetailsModel.class);
        tvTripDate.setText(weeklyDetailsModel.getFormatdate());
        tvTripAmount.setText(sessionManager.getCurrencySymbol() + weeklyDetailsModel.getTotalFare());
        basefare_amount.setText(sessionManager.getCurrencySymbol() + weeklyDetailsModel.getBaseFare());
        tvTotalDriverEarning.setText(sessionManager.getCurrencySymbol() + weeklyDetailsModel.getTotalFare());
        cash_collected.setText(sessionManager.getCurrencySymbol() + weeklyDetailsModel.getCashFollected());
        completed_trips.setText(Integer.toString(weeklyDetailsModel.getCompletedtrips()));
        weeklyDetailsLists = weeklyDetailsModel.getStatement();
        if (weeklyDetailsModel.getBankDeposits() != null && !TextUtils.isEmpty(weeklyDetailsModel.getBankDeposits()) && Float.valueOf(weeklyDetailsModel.getBankDeposits()) > 0) {
            bank_deposit.setText(sessionManager.getCurrencySymbol() + weeklyDetailsModel.getBankDeposits());
            rltBankDeposit.setVisibility(View.VISIBLE);
        } else {
            rltBankDeposit.setVisibility(View.GONE);
        }
        if (weeklyDetailsModel.getAccessFee() != null && !TextUtils.isEmpty(weeklyDetailsModel.getAccessFee()) && Float.valueOf(weeklyDetailsModel.getAccessFee()) > 0) {
            if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
                accessfee_amount.setText("-" + sessionManager.getCurrencySymbol() + weeklyDetailsModel.getAccessFee());
            else
                accessfee_amount.setText(sessionManager.getCurrencySymbol() + weeklyDetailsModel.getAccessFee()+"-");
            rltBankDeposit.setVisibility(View.VISIBLE);
        } else {
            rltBankDeposit.setVisibility(View.GONE);
        }

        adapter = new DailyEarnListAdapter(this, weeklyDetailsLists);
        rcView.setAdapter(adapter);

    }
}