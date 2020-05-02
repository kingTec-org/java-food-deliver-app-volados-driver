package gofereatsdriver.views.main.tripdetails;
/**
 * @package com.gofereats
 * @subpackage views.main.tripdetails
 * @category YourTrips
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.res.Resources;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.adapters.main.ViewPagerAdapter;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.datamodels.trips.TripListModel;
import gofereatsdriver.datamodels.trips.TriphistoryResult;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.interfaces.YourTripsListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/************************************************************
 YourTrips
 Its shows the Drivers Past and Upcoming Details Fragments
 ****************************************************************/

public class YourTrips extends AppCompatActivity implements YourTripsListener, TabLayout.OnTabSelectedListener, ServiceListener {

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

    public @InjectView(R.id.tabLayout)
    TabLayout tabLayout;
    public @InjectView(R.id.vpager)
    ViewPager vpager;

    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public ArrayList<TripListModel> pastDeliveryhistory = new ArrayList<>();
    public ArrayList<TripListModel> todayDeliveryhistory = new ArrayList<>();
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_trips);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        tripHistory();
        commonMethods.rotateArrow(ivBack,this);
        tvTitle.setText(getResources().getString(R.string.triphistory));
    }

    /**
     * initalize Fragments
     */
    private void init() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Upcoming(), getResources().getString(R.string.todaytrip));
        adapter.addFragment(new Past(), getResources().getString(R.string.pasttrip));
        vpager.setAdapter(adapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(vpager);

            }
        });
    }

    @Override
    public Resources getRes() {
        return YourTrips.this.getResources();
    }

    @Override
    public YourTrips getInstance() {
        return YourTrips.this;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public ArrayList<TripListModel> getTodayTripDetailsList() {
        return todayDeliveryhistory;
    }

    public ArrayList<TripListModel> getPastDeliveryhistory() {
        return pastDeliveryhistory;
    }


    /**
     * Call API to get Trip history list for
     */
    public void tripHistory() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.orderHistory(sessionManager.getToken()).enqueue(new RequestCallback(this));
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

    }

    /**
     * On Success JsonResponse From API
     */
    public void onSuccess(JsonResponse jsonResp) {
        TriphistoryResult triphistoryResult = gson.fromJson(jsonResp.getStrResponse(), TriphistoryResult.class);
        if (triphistoryResult != null) {
            pastDeliveryhistory = triphistoryResult.getPastDelivery();
            todayDeliveryhistory = triphistoryResult.getTotalDelivery();
            init();
        }
    }
}
