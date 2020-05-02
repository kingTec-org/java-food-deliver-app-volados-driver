package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage gofereatsdriver.views.signinsignup
 * @category DocumentHomeActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
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
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.views.main.MainActivity;


/*************************************************************
 DocumentHome Activity
 Its used to driver details information upload Home screen function
 *************************************************************** */
public class DocHomeActivity extends AppCompatActivity {

    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/

    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.rltLicenceBack)
    RelativeLayout rltLicenceBack;
    public @InjectView(R.id.rltLicenceFront)
    RelativeLayout rltLicenceFront;
    public @InjectView(R.id.rltInsurance)
    RelativeLayout rltInsurance;
    public @InjectView(R.id.rltRegisteration)
    RelativeLayout rltRegisteration;
    public @InjectView(R.id.rltCarriage)
    RelativeLayout rltCarriage;
    public @InjectView(R.id.tvVerify)
    TextView tvVerify;
    public @InjectView(R.id.ivArrow1)
    ImageView ivArrow1;
    public @InjectView(R.id.ivArrow2)
    ImageView ivArrow2;
    public @InjectView(R.id.ivArrow3)
    ImageView ivArrow3;
    public @InjectView(R.id.ivArrow4)
    ImageView ivArrow4;
    public @InjectView(R.id.ivArrow5)
    ImageView ivArrow5;


    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Licence Back
     */
    @OnClick(R.id.rltLicenceBack)
    public void licenceBack() {
        callActivity(1, getResources().getString(R.string.driverlicense_back));
    }

    /**
     * Licence Front
     */
    @OnClick(R.id.rltLicenceFront)
    public void licenceFront() {
        callActivity(2, getResources().getString(R.string.driverlicense_front));
    }

    /**
     * Insurance Doc
     */
    @OnClick(R.id.rltInsurance)
    public void insurance() {
        callActivity(3, getResources().getString(R.string.motor_insurance));
    }

    /**
     * RC DOC
     */
    @OnClick(R.id.rltRegisteration)
    public void registeration() {
        callActivity(4, getResources().getString(R.string.registeration));
    }

    /**
     * carriage doc
     */
    @OnClick(R.id.rltCarriage)
    public void carriage() {
        callActivity(5, getResources().getString(R.string.contract_carriage));
    }

    /**
     * verify
     */
    @OnClick(R.id.tvVerify)
    public void next() {
        sessionManager.setDriverStatus(3);
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_home);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        tvTitle.setText(getResources().getString(R.string.documents));
        vBottom.setVisibility(View.VISIBLE);
        commonMethods.rotateArrow(ivBack,this);
        commonMethods.rotateArrow(ivArrow1,this);
        commonMethods.rotateArrow(ivArrow2,this);
        commonMethods.rotateArrow(ivArrow3,this);
        commonMethods.rotateArrow(ivArrow4,this);
        commonMethods.rotateArrow(ivArrow5,this);
        if (getIntent().getStringExtra("check").equals("view")) {
            tvVerify.setVisibility(View.GONE);
        } else {
            tvVerify.setVisibility(View.VISIBLE);
        }
        checkDocuments();

    }

    private void checkDocuments() {
        if (!TextUtils.isEmpty(sessionManager.getDoc1())) {
            ivArrow1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.apptheme), PorterDuff.Mode.SRC_IN);
            ivArrow1.setImageDrawable(getResources().getDrawable(R.drawable.checked));
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            commonMethods.rotateArrowInverse(ivArrow1,this);
        }
        if (!TextUtils.isEmpty(sessionManager.getDoc2())) {
            ivArrow2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.apptheme), PorterDuff.Mode.SRC_IN);
            ivArrow2.setImageDrawable(getResources().getDrawable(R.drawable.checked));
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            commonMethods.rotateArrowInverse(ivArrow2,this);
        }
        if (!TextUtils.isEmpty(sessionManager.getDoc3())) {
            ivArrow3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.apptheme), PorterDuff.Mode.SRC_IN);
            ivArrow3.setImageDrawable(getResources().getDrawable(R.drawable.checked));
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            commonMethods.rotateArrowInverse(ivArrow3,this);
        }
        if (!TextUtils.isEmpty(sessionManager.getDoc4())) {
            ivArrow4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.apptheme), PorterDuff.Mode.SRC_IN);
            ivArrow4.setImageDrawable(getResources().getDrawable(R.drawable.checked));
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            commonMethods.rotateArrowInverse(ivArrow4,this);
        }
        if (!TextUtils.isEmpty(sessionManager.getDoc5())) {
            ivArrow5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.apptheme), PorterDuff.Mode.SRC_IN);
            ivArrow5.setImageDrawable(getResources().getDrawable(R.drawable.checked));
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            commonMethods.rotateArrowInverse(ivArrow5,this);
        }
    }

    /**
     * @param type  1 for licence back,
     *              2 for licence front,
     *              3 for insurance
     *              4 for registeration
     *              5 for carriage
     * @param title Document title
     */
    public void callActivity(int type, String title) {
        Intent documentUpload = new Intent(getApplicationContext(), DocumentUploadActivity.class);
        documentUpload.putExtra("type", type);
        documentUpload.putExtra("title", title);
        startActivity(documentUpload);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(sessionManager.getDoc1()) && !TextUtils.isEmpty(sessionManager.getDoc2()) && !TextUtils.isEmpty(sessionManager.getDoc3()) && !TextUtils.isEmpty(sessionManager.getDoc4()) && !TextUtils.isEmpty(sessionManager.getDoc5()) && !getIntent().getStringExtra("check").equals("view")) {
            tvVerify.setVisibility(View.VISIBLE);
        } else {
            tvVerify.setVisibility(View.GONE);
        }

        checkDocuments();
    }
}
