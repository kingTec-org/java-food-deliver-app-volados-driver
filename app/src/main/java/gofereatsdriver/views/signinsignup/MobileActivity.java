package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver.signinsignup
 * @subpackage views.signinsignup
 * @category MobileActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.Constants;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

import static gofereatsdriver.utils.Enums.REQ_EDIT_MOBILE;
import static gofereatsdriver.utils.Enums.REQ_FORGOT_PASSWORD;

/*************************************************************
 MobileActivity
 Its used to get the mobile number detail function
 *************************************************************** */

public class MobileActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
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
    public @InjectView(R.id.tvMobileError)
    TextView tvMobileError;
    public @InjectView(R.id.rltNext)
    RelativeLayout rltNext;
    public @InjectView(R.id.ccMobile)
    CountryCodePicker ccMobile;
    public boolean editMob;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Check Mobile Number
     */
    @OnClick(R.id.rltNext)
    public void next() {
        if (validate()) {

            if (editMob) {
                //Change  Mobile Number
                String editMob = edtMobile.getText().toString();
                sessionManager.setPhoneNumber(editMob);
                sessionManager.setCountryCode(ccMobile.getSelectedCountryCode().trim());
                editMobNumber(editMob);
            } else {
                String mobile = edtMobile.getText().toString();
                sessionManager.setPhoneNumber(mobile);
                sessionManager.setCountryCode(ccMobile.getSelectedCountryCode().trim());
                forgotPassword(mobile);
            }
            /*Intent otp = new Intent(getApplicationContext(), RegisterOTPActivity.class);
            startActivity(otp);
            */
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilenumber);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)) {
            ccMobile.changeDefaultLanguage(CountryCodePicker.Language.ARABIC);
            ccMobile.setCurrentTextGravity(CountryCodePicker.TextGravity.RIGHT);
            ccMobile.setGravity(Gravity.START);
        }
        editMob = getIntent().getBooleanExtra("isChange", false);
        updateview();
    }


    /**
     * Check Number Availabe or Not
     */
    public void editMobNumber(String mobile) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.numberValidation(sessionManager.getType(), mobile, ccMobile.getSelectedCountryCode().trim(),sessionManager.getLanguageCode()).enqueue(new RequestCallback(REQ_EDIT_MOBILE, this));
    }

    private void forgotPassword(String mobile) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.forgotPassword(sessionManager.getType(), mobile, ccMobile.getSelectedCountryCode().trim(),sessionManager.getLanguageCode()).enqueue(new RequestCallback(REQ_FORGOT_PASSWORD, this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }

    /**
     * Response on Success
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
            case REQ_EDIT_MOBILE:
                if (jsonResp.isSuccess()) {
                    onSuccessChangeMobile(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (editMob) {
                        commonMethods.showMessage(this, dialog,getResources().getString(R.string.mobile_validation) );
                    } else {
                        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    }
                }
                break;
            case REQ_FORGOT_PASSWORD:
                if (jsonResp.isSuccess()) {
                    onSuccessForgotPassword(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;

        }
    }

    /**
     * Respon Failure
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

    private void onSuccessChangeMobile(JsonResponse jsonResp) {
        final int otp = (int) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.OTP, Integer.class);
        Intent changeMob = new Intent(getApplicationContext(), RegisterOTPActivity.class);
        changeMob.putExtra("otp", otp);
        changeMob.putExtra("resetpassword", false);
        changeMob.putExtra("isChange", true);
        startActivity(changeMob);
        finish();
    }

    /**
     * Get Otp From Forgot Password
     */
    private void onSuccessForgotPassword(JsonResponse jsonResp) {
        final int otp = (int) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.OTP, Integer.class);
        Intent resetotp = new Intent(getApplicationContext(), RegisterOTPActivity.class);
        resetotp.putExtra("otp", otp);
        resetotp.putExtra("resetpassword", true);
        startActivity(resetotp);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    public boolean validate() {
        if (edtMobile.getText().toString().startsWith("0")) {
            edtMobile.setText("");
        }
        if (edtMobile.getText().toString().trim().isEmpty() || edtMobile.getText().length() <= 5) {
            tvMobileError.setVisibility(View.VISIBLE);
            return false;
        } else {
            tvMobileError.setVisibility(View.GONE);
            return true;
        }
    }

    public void updateview() {
        ccMobile.setCountryForNameCode(sessionManager.getCountryCode());
        ccMobile.setAutoDetectedCountry(true);

        tvTitle.setVisibility(View.GONE);
        vBottom.setVisibility(View.GONE);
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            edtMobile.setGravity(Gravity.END);
            edtMobile.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        edtMobile.addTextChangedListener(new textWatcher(edtMobile));
    }

    /*
    *   Text watcher for validate signin fields
    */
    private class textWatcher implements TextWatcher {

        private View view;

        private textWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //validate();
            tvMobileError.setVisibility(View.GONE);
        }

        public void afterTextChanged(Editable editable) {
            //validate(view);
        }
    }
}