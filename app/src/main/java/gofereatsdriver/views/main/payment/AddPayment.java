package gofereatsdriver.views.main.payment;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.payment
 * @category AddPayment
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.obs.CustomButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;

/*************************************************************
 AddPaymentActivity
 Its used to Add The Payment Details for the Driver
 ****************************************************************/


public class AddPayment extends AppCompatActivity {

    public @InjectView(R.id.save)
    CustomButton save;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;

    /**
     * Save card Details
     */
    @OnClick(R.id.save)
    public void savepayment() {
        finish();
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        tvTitle.setText(getResources().getString(R.string.addpayment));
        vBottom.setVisibility(View.VISIBLE);
    }
}
