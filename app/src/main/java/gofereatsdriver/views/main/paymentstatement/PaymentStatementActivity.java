package gofereatsdriver.views.main.paymentstatement;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.paymentstatement
 * @category PaymentStatementActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import gofereatsdriver.adapters.main.PayAdapter;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.WeeklyModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.datamodels.trips.WeeklyListModel;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 PaymentStatementActivity
 Its used to show PaymentStatement Details for the Driver
 ****************************************************************/
public class PaymentStatementActivity extends AppCompatActivity implements ServiceListener {

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
    public WeeklyModel weeklymodel;
    public PayAdapter adapter;
    public List<WeeklyListModel> weeklylistmodels;
    public @InjectView(R.id.tvlistempty)
    CustomTextView tvlistempty;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.rcViews)
    RecyclerView rcView;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_statement);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        commonMethods.rotateArrow(ivBack,this);
        initviews();
        weeklyApiCall();
    }

    /**
     * Calling Weekly Api Call
     */
    private void weeklyApiCall() {

        commonMethods.showProgressDialog(this, customDialog);
        apiService.weeklyList(sessionManager.getToken()).enqueue(new RequestCallback(this));

    }

    /**
     * After Getting Result From API
     *
     * @param jsonResponse JsonResponse After Success
     */
    public void onSuccess(JsonResponse jsonResponse) {
        weeklymodel = gson.fromJson(jsonResponse.getStrResponse(), WeeklyModel.class);
        weeklylistmodels = new ArrayList<>();
        weeklylistmodels = weeklymodel.getTripWeekDetails();

        if (weeklylistmodels.size() > 0) {
            tvlistempty.setVisibility(View.GONE);
            adapter = new PayAdapter(this,this, weeklylistmodels);
            rcView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            tvlistempty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * API ON SUCCESS
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

    public void initviews() {
        dialog = commonMethods.getAlertDialog(this);
        tvTitle.setText(getResources().getString(R.string.paystatement));
        vBottom.setVisibility(View.VISIBLE);
        rcView.setHasFixedSize(false);
        rcView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }
}
