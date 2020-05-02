package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.signinsignup
 * @category SigninActivity
 * @author Trioangle Product Team
 * @version 1.0
 */


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.datamodels.signinup.SignInUpResultModel;
import gofereatsdriver.datamodels.signinup.UserDetailsModel;
import gofereatsdriver.datamodels.signinup.VehicleTypeModel;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;


/*************************************************************
 Sign in Activity
 Its used to  get the sign in detail function
 *************************************************************** */
public class SigninActivity extends AppCompatActivity implements ServiceListener {

    public AlertDialog dialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/

    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.edtMobile)
    EditText edtMobile;
    public @InjectView(R.id.tilMobile)
    TextInputLayout tilMobile;
    public @InjectView(R.id.edtPassword)
    EditText edtPassword;
    public @InjectView(R.id.tilPassword)
    TextInputLayout tilPassword;
    public @InjectView(R.id.ccp)
    CountryCodePicker ccp;


    public @InjectView(R.id.btnSignIn)
    Button btnSignIn;
    public @InjectView(R.id.btnForgotPassword)
    Button btnForgotPassword;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * API For Driver Login
     */
    @OnClick(R.id.btnSignIn)
    public void signin() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.login(sessionManager.getType(), getDriverData()).enqueue(new RequestCallback(this));
    }

    @OnClick(R.id.btnForgotPassword)
        /*
         * Forgot Password Action Flow Starts
         */ public void forgotPassword() {
        Intent intent = new Intent(getApplicationContext(), MobileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        ccp.setAutoDetectedCountry(true);
        String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)||Locale.getDefault().getLanguage().equals("ar")) {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ARABIC);
            ccp.setCurrentTextGravity(CountryCodePicker.TextGravity.RIGHT);
            ccp.setGravity(Gravity.START);
            tilPassword.setGravity(Gravity.END);
        }

        sessionManager.setCountryCode(ccp.getDefaultCountryCode());


        tvTitle.setText(getResources().getString(R.string.signin));
        vBottom.setVisibility(View.VISIBLE);
        sessionManager.setType("2");
        //sessionManager.setCurrencySymbol("$");

        edtMobile.addTextChangedListener(new textWatcher(edtMobile));
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            edtMobile.setGravity(Gravity.END);
            edtMobile.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        edtPassword.addTextChangedListener(new textWatcher(edtPassword));
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                sessionManager.setCountryCode(ccp.getSelectedCountryCode());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }


    /**
     * Getting Driver Login details
     */

    private HashMap<String, String> getDriverData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile_number", edtMobile.getText().toString().trim());
        hashMap.put("country_code", ccp.getSelectedCountryCode().trim());
        hashMap.put("password", edtPassword.getText().toString().trim());
        hashMap.put("language", sessionManager.getLanguageCode());
        return hashMap;
    }

    /**
     * Response Form Api
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        /**
         * Check Is Online
         */
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        /**
         * Check Is success Or Failure
         */
        if (jsonResp.isSuccess()) {
            onSuccessLogin(jsonResp);
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
     * Response From API OnSuccess and get Response
     */
    private void onSuccessLogin(JsonResponse jsonResponse) {

        SignInUpResultModel signInUpResultModel = gson.fromJson(jsonResponse.getStrResponse(), SignInUpResultModel.class);
        if (signInUpResultModel != null) {

            sessionManager.setToken(signInUpResultModel.getToken());
            UserDetailsModel userDetailsModel = signInUpResultModel.getUserDetailsModels();
            if (userDetailsModel != null) {
                if (userDetailsModel.getStatus().equalsIgnoreCase("active")) {
                    Intent login = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(login);
                    finish();
                    overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
                } else if (userDetailsModel.getStatus().equalsIgnoreCase("vehicle_details")) {
                    ArrayList<VehicleTypeModel> vehicleTypeModels = signInUpResultModel.getVehicleTypeModels();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vehicleType", vehicleTypeModels);

                    Intent registerCar = new Intent(getApplicationContext(), RegisterCarDetailsActivity.class);
                    registerCar.putExtras(bundle);
                    startActivity(registerCar);
                    //finish();
                    overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
                } else if (userDetailsModel.getStatus().equalsIgnoreCase("document_details")) {
                    sessionManager.setDriverStatus(3);
                    Intent documents = new Intent(getApplicationContext(), DocHomeActivity.class);
                    documents.putExtra("check", "register");
                    startActivity(documents);
                    //finish();
                    overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
                } else {
                    sessionManager.setDriverStatus(3);
                    Intent login = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(login);
                    finish();
                    overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
                }
            }
        }
    }

    public void validate(View view) {
        Boolean validated = false;
        Boolean validatedMob = false;
        switch (view.getId()) {
            case R.id.edtMobile:
            case R.id.edtPassword:
                if (edtMobile.getText().toString().trim().isEmpty() || edtMobile.getText().length() < 6) {
                    //requestFocus(edtMobile);
                    validatedMob = false;
                } else {
                    validatedMob = true;
                    tilMobile.setErrorEnabled(false);
                }
                if (edtPassword.getText().toString().trim().isEmpty() || edtPassword.getText().length() < 6) {
                    //requestFocus(edtPassword);
                    validated = false;
                } else {
                    validated = true;
                    tilPassword.setErrorEnabled(false);
                }
                break;
            default:
                break;
        }

        if (validated && validatedMob) {
            btnSignIn.setEnabled(true);
        } else {
            btnSignIn.setEnabled(false);
        }
        if (edtMobile.getText().toString().startsWith("0")) {
            edtMobile.setText("");
        }
    }

    /**
     * focus edit text
     */
  /*  private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }*/

    /**
     * Text watcher for validate sign in fields
     */
    private class textWatcher implements TextWatcher {

        private View view;

        private textWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            validate(view);
        }

        public void afterTextChanged(Editable editable) {
            //validate(view);
        }
    }
}