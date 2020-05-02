package gofereatsdriver.views.main.rating;
/**
 * @package com.gofereats
 * @subpackage views.main.profile
 * @category RatingStep1Activity
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomEditText;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.adapters.main.ReasonListAdapter;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.dropoffrating.DropoffList;
import gofereatsdriver.datamodels.dropoffrating.DropoffModel;
import gofereatsdriver.datamodels.dropoffrating.DropoffissuesList;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.Enums;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/************************************************************
 RatingStep1Activity
 Its used to give Rating for Receiptant where he Place the order
 ****************************************************************/
public class RatingStep1Activity extends AppCompatActivity implements ServiceListener {

    public static ArrayList<DropoffList> dropoffListArrayList = new ArrayList<>();
    public static ArrayList<DropoffissuesList> dropoffissuesListArrayList = new ArrayList<>();
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
    public DropoffModel dropoffModel;
    public ReasonListAdapter reasonListAdapter;
    public @InjectView(R.id.rvReasonList)
    RecyclerView rvReasonList;
    public @InjectView(R.id.btnNext)
    CustomButton btnNext;
    public @InjectView(R.id.btnLeaveComments)
    CustomButton btnLeaveComments;
    public @InjectView(R.id.edtLeaveAddtionalComments)
    CustomEditText edtLeaveAddtionalComments;
    public @InjectView(R.id.ivClosepage)
    ImageView ivClosepage;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.rltAllViews)
    RelativeLayout rltAllViews;
    public int getId = -1;
    public int orderId = 0;
    private AlertDialog dialog;

    @OnClick(R.id.ivClosepage)
    public void close() {
        onBackPressed();
    }

    @OnClick(R.id.ivBack)
    public void back() {
        onBackPressed();
    }

    /**
     * Leave Extra Comments
     */
    @OnClick(R.id.btnLeaveComments)
    public void leaveComments() {
        btnLeaveComments.setVisibility(View.GONE);
        edtLeaveAddtionalComments.setVisibility(View.VISIBLE);
    }

    /**
     * Save the Dropp off Status
     */
    @OnClick(R.id.btnNext)
    public void rating() {
        Intent rating = new Intent(getApplicationContext(), RatingStep2Activity.class);
        rating.putExtra("comments", btnLeaveComments.getText().toString().trim());
        rating.putExtra("dropoffid", getId);
        finish();
        startActivity(rating);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_step1);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        commonMethods.rotateArrow(ivBack,this);
        rltAllViews.setVisibility(View.GONE);
        dialog = commonMethods.getAlertDialog(this);
        orderId = getIntent().getIntExtra("orderId", 0);
        rvReasonList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReasonList.setLayoutManager(layoutManager);

        getReason();
        btnNext.setEnabled(false);
        btnNext.setBackground(getResources().getDrawable(R.drawable.background_dim_blue));

    }

    /**
     * Get Reasons For Rating
     */
    private void getReason() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.dropOffData(sessionManager.getToken(), orderId).enqueue(new RequestCallback(Enums.REQ_REASON, this));
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
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_REASON:
                if (jsonResp.isSuccess()) {
                    onSuccessDropOffList(jsonResp);
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
     * Getting Reasons
     */
    private void onSuccessDropOffList(JsonResponse jsonResp) {
        dropoffModel = gson.fromJson(jsonResp.getStrResponse(), DropoffModel.class);
        if (dropoffModel.getDropoffOptions() != null) {
            dropoffListArrayList.clear();
            dropoffListArrayList.addAll(dropoffModel.getDropoffOptions());

            reasonListAdapter = new ReasonListAdapter(dropoffListArrayList);
            reasonListAdapter.setOnItemClickListener(new ReasonListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, int positionz) {
                    getId = id;
                    if (getId >= 0) {
                        btnNext.setEnabled(true);
                        btnNext.setBackground(getResources().getDrawable(R.drawable.background_blue));
                    }
                }
            });
            rvReasonList.setAdapter(reasonListAdapter);
            rltAllViews.setVisibility(View.VISIBLE);
            reasonListAdapter.notifyDataSetChanged();
        }
        if (dropoffModel.getDriverRestaurantIssues() != null) {
            dropoffissuesListArrayList.clear();
            dropoffissuesListArrayList.addAll(dropoffModel.getDriverRestaurantIssues());
        }

    }

    /**
     * Save those Reasons in a ArrayList
     *
     * @return dropoffissuesListArrayList
     */
    public ArrayList<DropoffissuesList> getIssues() {
        return dropoffissuesListArrayList;
    }

}
