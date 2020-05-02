package gofereatsdriver.payoutbank;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage payoutBank
 * @category PayoutEmailListActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.payment.PayoutBankDetailsActivity;

import static gofereatsdriver.utils.Enums.REQ_STRIPE;

/*************************************************************
 PayoutEmailListActivity
 *************************************************************** */
public class PayoutEmailListActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    Gson gson, gson1;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    SessionManager sessionManager;
    public StripeModelClass stripeModel;
    public List<StripeListModel> stripeModelList;
    public @InjectView(R.id.tvTitle)
    CustomTextView tvTitle;
    public @InjectView(R.id.payoutmain_title)
    CustomTextView payoutmain_title;
    public @InjectView(R.id.payoutmain_msg)
    CustomTextView payoutmain_msg;
    public @InjectView(R.id.payoutmain_link)
    CustomTextView payoutmain_link;
    public @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    public PayoutEmailListAdapter adapter;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.payout_stripe)
    CustomButton payout_stripe;

    @OnClick(R.id.ivBack)
    public void title() {
        onBackPressed();
    }

    @OnClick(R.id.payout_stripe)
    public void stripe() {
        Intent main = new Intent(this, PayoutBankDetailsActivity.class);
        startActivity(main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_email_list);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        tvTitle.setText(R.string.payouts);
        commonMethods.rotateArrow(ivBack,this);

    }

    /**
     * Api call for stripe listing
     */
    public void stripeListApi() {

        commonMethods.showProgressDialog(this, customDialog);
        apiService.stripeList(sessionManager.getToken()).enqueue(new RequestCallback(REQ_STRIPE, this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        stripeListApi();

    }

    /**
     * Success Response From API
     *
     * @param jsonResp Json Response from Api
     * @param data     Request data (optional)
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {

            commonMethods.hideProgressDialog();

            stripeModel = gson.fromJson(jsonResp.getStrResponse(), StripeModelClass.class);

            stripeModelList = stripeModel.getPayoutDetails();

            payoutmain_msg.setVisibility(View.GONE);
            payoutmain_title.setVisibility(View.GONE);
            payoutmain_link.setVisibility(View.GONE);
            payout_stripe.setVisibility(View.VISIBLE);


            adapter = new PayoutEmailListAdapter(this, PayoutEmailListActivity.this, stripeModelList);
            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerview.setAdapter(adapter);

        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.hideProgressDialog();
            payoutmain_msg.setVisibility(View.VISIBLE);
            payoutmain_title.setVisibility(View.VISIBLE);
            payoutmain_link.setVisibility(View.GONE);
            payout_stripe.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();

    }
}
