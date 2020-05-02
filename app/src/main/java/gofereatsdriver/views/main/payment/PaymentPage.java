package gofereatsdriver.views.main.payment;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.payment
 * @category PaymentPage
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;

/*************************************************************
 PaymentPageActivity
 Its used to Add The Payment Details for the Driver
 ****************************************************************/

public class PaymentPage extends AppCompatActivity {

    public @InjectView(R.id.payment)
    RelativeLayout payment;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;

    /**
     * Add Card Page
     */
    @OnClick(R.id.payment)
    public void addPayment() {
        Intent main = new Intent(this, AddPayment.class);
        startActivity(main);
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        tvTitle.setText(getResources().getString(R.string.payment));
        vBottom.setVisibility(View.VISIBLE);
    }

}
