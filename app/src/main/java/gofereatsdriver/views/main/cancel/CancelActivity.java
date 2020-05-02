package gofereatsdriver.views.main.cancel;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage view.main.cancel
 * @category CancelActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.cancel.CancelReasonModel;
import gofereatsdriver.datamodels.cancel.CancelResultModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;

import static gofereatsdriver.utils.Enums.REQ_CANCEL_ORDER;
import static gofereatsdriver.utils.Enums.REQ_GET_CANCEL_REASONS;

/*************************************************************
 CancelActivity
 Its used to  Cancel Your Trip
 *************************************************************** */

public class CancelActivity extends AppCompatActivity implements ServiceListener {

    public ArrayList<CancelReasonModel> cancelReasonModels = new ArrayList<>();
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
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.edtCancelReason)
    EditText edtCancelReason;
    public @InjectView(R.id.spinner)
    Spinner spinner;
    public @InjectView(R.id.btnCancelReason)
    CustomButton btnCancelReason;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public int orderId = 0;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnCancelReason)
    public void onCancel() {
        cancelOrders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        tvTitle.setText(getString(R.string.cancel_order));


        /**
         * Getting Order Id From Order details
         */
        orderId = getIntent().getIntExtra("orderId", 0);

        getCancelReason();
    }


    /**
     * Call Api to get Reasons
     */
    private void getCancelReason() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.cancelOrdersReason(sessionManager.getType(), sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_CANCEL_REASONS, this));
    }

    /**
     * Call API to cancel order
     */
    public void cancelOrders() {

        String cancelMessage = edtCancelReason.getText().toString();

        int position = spinner.getSelectedItemPosition();
        if (position > 0) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.cancelOrders(cancelReasonModels.get(position).getCancelId(), cancelMessage, orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_CANCEL_ORDER, this));
        } else {
            commonMethods.showMessage(this, dialog, getString(R.string.select_reason));
        }
    }

    /**
     * Success Response For API
     *
     * @param jsonResp Json Response
     * @param data     Message Data
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_GET_CANCEL_REASONS:
                if (jsonResp.isSuccess()) {
                    onSuccessCancelReason(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_CANCEL_ORDER:
                if (jsonResp.isSuccess()) {
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

    /**
     * Failure response
     *
     * @param jsonResp
     * @param data
     */
    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * On getting the Reasons
     */
    public void onSuccessCancelReason(JsonResponse jsonResponse) {
        CancelResultModel cancelResultModel = gson.fromJson(jsonResponse.getStrResponse(), CancelResultModel.class);
        if (cancelResultModel != null) {
            CancelReasonModel cancelReasonModel = new CancelReasonModel();
            cancelReasonModel.setCancelId(0);
            cancelReasonModel.setReason(getString(R.string.select_reason));
            cancelReasonModels.add(cancelReasonModel);
            cancelReasonModels.addAll(cancelResultModel.getCancelReasonModels());

            String[] cancelReason = new String[cancelReasonModels.size()];

            for (int i = 0; i < cancelReasonModels.size(); i++) {
                cancelReason[i] = cancelReasonModels.get(i).getReason();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancel_reason_layout, cancelReason);

            spinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            /**
             *  Cancel trip reasons in spinner
             */
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }
    }
}
