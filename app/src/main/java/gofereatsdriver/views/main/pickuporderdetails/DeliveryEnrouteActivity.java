package gofereatsdriver.views.main.pickuporderdetails;
/**
 * @package com.gofereats
 * @subpackage views.main.pickuporderdetails
 * @category DeliveryEnrouteActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.accept.AcceptDetailsModel;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.views.main.cancel.CancelActivity;

/*************************************************************
 DeliveryEnrouteActivity
 Its used to get the Details of the order restaurant Location and eater Location
 ****************************************************************/
public class DeliveryEnrouteActivity extends AppCompatActivity {

    public @Inject
    CommonMethods commonMethods;
    public @InjectView(R.id.rltCancel)
    RelativeLayout rltCancel;
    public @InjectView(R.id.rltContact)
    RelativeLayout rltContact;
    public @InjectView(R.id.rltInstructionlayout)
    RelativeLayout rltInstructionlayout;
    public @InjectView(R.id.tvPickfromtext)
    TextView tvPickfromtext;
    public @InjectView(R.id.tvIntructionNote)
    TextView tvIntructionNote;
    public @InjectView(R.id.tvPickRestaddress)
    TextView tvPickRestaddress;
    public @InjectView(R.id.tvLocationaddress)
    TextView tvLocationaddress;
    public @InjectView(R.id.tvOrderId)
    TextView tvOrderId;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public int orderId = 0;
    public int status = 0;
    public AcceptDetailsModel acceptDetailsModel = new AcceptDetailsModel();
    public @Inject
    SessionManager sessionManager;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Cancel Page
     */
    @OnClick(R.id.rltCancel)
    public void cancel() {
        Intent cancel = new Intent(getApplicationContext(), CancelActivity.class);
        cancel.putExtra("orderId", orderId);
        startActivity(cancel);
    }

    /**
     * Contact eater or restaurant
     */
    @OnClick(R.id.rltContact)
    public void contact() {
        Intent cancel = new Intent(getApplicationContext(), ContactActivity.class);
        cancel.putExtra("status", acceptDetailsModel.getStatus());
        cancel.putExtra("restaurantname", acceptDetailsModel.getRestaurantName());
        cancel.putExtra("restaurantnumber", acceptDetailsModel.getRestaurantMobileNumber());
        cancel.putExtra("eatername", acceptDetailsModel.getEaterName());
        cancel.putExtra("eaternumber", acceptDetailsModel.getEaterMobileNumber());
        startActivity(cancel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_enroute);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        commonMethods.rotateArrow(ivBack,this);
        getintent();
        loaddata();
    }

    /**
     * getting Datas From Intent
     */
    public void getintent() {
        Intent intent = this.getIntent();
        acceptDetailsModel = (AcceptDetailsModel) intent.getSerializableExtra("menu_item");
    }

    /**
     * Enable/Setting Views
     */
    public void loaddata() {
        if (acceptDetailsModel != null) {
            orderId = acceptDetailsModel.getOrderId();
            //status = acceptDetailsModel.getStatus();
            status = sessionManager.getTripStatus();
            if (status == 0 || status == 1) {
                rltInstructionlayout.setVisibility(View.GONE);
                tvPickfromtext.setText(R.string.pick_up_from);
                tvPickfromtext.setTextColor(getResources().getColor(R.color.apptheme));
                tvPickRestaddress.setText(acceptDetailsModel.getRestaurantName());
                tvLocationaddress.setText(acceptDetailsModel.getPickupLocation());
            } else if (status == 3 || status == 4) {
                if (acceptDetailsModel.getDeliveryNote() == null || acceptDetailsModel.getDeliveryNote().equals("")) {
                    rltInstructionlayout.setVisibility(View.GONE);
                } else {
                    rltInstructionlayout.setVisibility(View.VISIBLE);
                }
                tvPickfromtext.setText(R.string.deliverto);
                tvPickfromtext.setTextColor(getResources().getColor(R.color.ub__google_red));
                tvPickRestaddress.setText(acceptDetailsModel.getEaterName());
                tvLocationaddress.setText(acceptDetailsModel.getDropLocation());
                tvIntructionNote.setText(acceptDetailsModel.getDeliveryNote());
            }
            if("0".equalsIgnoreCase(getString(R.string.layout_direction)))
                tvOrderId.setText(getString(R.string.order_id)+" #" + " " + orderId);
            else
                tvOrderId.setText(getString(R.string.order_id) + " " + orderId+" #");

            if (status == 4) {
                rltCancel.setVisibility(View.GONE);
            }
        }
    }
}
