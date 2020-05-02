package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.signinsignup
 * @category Register
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

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
import gofereatsdriver.views.customize.CustomRecyclerView;


/*************************************************************
 Register
 Its used to get the driver register detail function
 *************************************************************** */
public class Register extends AppCompatActivity implements ServiceListener {


    public AlertDialog dialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
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
    public @InjectView(R.id.edtFirstName)
    EditText edtFirstName;
    public @InjectView(R.id.edtLastName)
    EditText edtLastName;
    public @InjectView(R.id.edtEmail)
    EditText edtEmail;
    public @InjectView(R.id.edtMobile)
    EditText edtMobile;
    public @InjectView(R.id.edtPassword)
    EditText edtPassword;
    public @InjectView(R.id.edtCity)
    EditText edtCity;

    public @InjectView(R.id.tilFirstName)
    TextInputLayout tilFirstName;
    public @InjectView(R.id.tilLastName)
    TextInputLayout tilLastName;
    public @InjectView(R.id.tilEmail)
    TextInputLayout tilEmail;
    public @InjectView(R.id.tilPassword)
    TextInputLayout tilPassword;
    public @InjectView(R.id.tilCity)
    TextInputLayout tilCity;

    public @InjectView(R.id.ccMobile)
    CountryCodePicker ccMobile;
    public @InjectView(R.id.rltMobileError)
    RelativeLayout rltMobileError;
    public @InjectView(R.id.rvCitySearchList)
    CustomRecyclerView rvCitySearchList;
    public @InjectView(R.id.tvPrivacylink)
    TextView tvPrivacylink;


    public @InjectView(R.id.btnSignup)
    Button btnSignup;
    public @InjectView(R.id.tvLogin)
    TextView tvLogin;
    public Boolean validatedFirstName = false;
    public Boolean validatedLastName = false;
    public Boolean validatedEmail = false;
    public Boolean validatedMobile = false;
    public Boolean validatedPassword = false;
    //public Boolean validatedCity = false;

    /*
    *   Check email is valid or not
    */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Sign Up
     */
    @OnClick(R.id.btnSignup)
    public void signup() {

        sessionManager.setFirstName(edtFirstName.getText().toString().trim());
        sessionManager.setLastName(edtLastName.getText().toString().trim());
        sessionManager.setEmail(edtEmail.getText().toString().trim());
        sessionManager.setPhoneNumber(edtMobile.getText().toString().trim());
        sessionManager.setPassword(edtPassword.getText().toString().trim());
        sessionManager.setCountryCode(ccMobile.getSelectedCountryCode());
        //sessionManager.setCity(edtCity.getText().toString().trim());

        saveUserDetails();
    }

    @OnClick(R.id.tvLogin)
    /*
     * Redirct To Login Pages
     */ public void login() {
        Intent signin = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(signin);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        customTextView(tvPrivacylink);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        ccMobile.setAutoDetectedCountry(true);
        String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)) {
            ccMobile.changeDefaultLanguage(CountryCodePicker.Language.ARABIC);
            ccMobile.setCurrentTextGravity(CountryCodePicker.TextGravity.RIGHT);
            ccMobile.setGravity(Gravity.START);
        }
        tvTitle.setText(getResources().getString(R.string.register));
        vBottom.setVisibility(View.VISIBLE);
        edtFirstName.requestFocus();
        edtFirstName.addTextChangedListener(new textWatcher(edtFirstName));
        edtLastName.addTextChangedListener(new textWatcher(edtLastName));
        edtEmail.addTextChangedListener(new textWatcher(edtEmail));
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            edtMobile.setGravity(Gravity.END);
            edtMobile.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        edtMobile.addTextChangedListener(new textWatcher(edtMobile));
        edtPassword.addTextChangedListener(new textWatcher(edtPassword));
        sessionManager.setCountryCode(ccMobile.getDefaultCountryCode());
        ccMobile.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                ccMobile.getSelectedCountryName();

            }
        });
        //edtCity.addTextChangedListener(new textWatcher(edtCity));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }

    /**
     * Save Details and Check The Number is already Registered
     */
    private void saveUserDetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.numberValidation(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(),sessionManager.getLanguageCode()).enqueue(new RequestCallback(this));
    }

    /**
     * Success Resp From API
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        if (jsonResp.isSuccess()) {
            onSuccessOTP(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Failure Resp
     */
    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    /**
     * Success To Get OTP
     */
    private void onSuccessOTP(JsonResponse jsonResponse) {
        final int otp = (int) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.OTP, Integer.class);
        Intent getotp = new Intent(getApplicationContext(), RegisterOTPActivity.class);
        getotp.putExtra("otp", otp);
        getotp.putExtra("resetpassword", false);
        getotp.putExtra("hashmap", getParams());
        startActivity(getotp);
    }

    /**
     * Gettting User Details in Hashmap
     */
    public HashMap<String, String> getParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("first_name", edtFirstName.getText().toString().trim());
        hashMap.put("last_name", edtLastName.getText().toString().trim());
        hashMap.put("mobile_number", edtMobile.getText().toString().trim());
        hashMap.put("country_code", ccMobile.getSelectedCountryCode().trim());
        hashMap.put("email", edtEmail.getText().toString().trim());
        hashMap.put("password", edtPassword.getText().toString().trim());
        return hashMap;
    }

    public void validate(View view) {
        switch (view.getId()) {
            case R.id.edtFirstName:
                if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
                    //requestFocus(edtMobile);
                    tilFirstName.setError(getResources().getString(R.string.error_msg_firstname));
                    tilFirstName.setErrorEnabled(true);
                    validatedFirstName = false;
                } else {
                    validatedFirstName = true;
                    tilFirstName.setErrorEnabled(false);
                }
                break;
            case R.id.edtLastName:
                if (TextUtils.isEmpty(edtLastName.getText().toString())) {
                    //requestFocus(edtMobile);
                    tilLastName.setError(getResources().getString(R.string.error_msg_lastname));
                    tilLastName.setErrorEnabled(true);
                    validatedLastName = false;
                } else {
                    validatedLastName = true;
                    tilLastName.setErrorEnabled(false);
                }
                break;
            case R.id.edtEmail:
                if (TextUtils.isEmpty(edtEmail.getText().toString()) || !isValidEmail(edtEmail.getText().toString())) {
                    //requestFocus(edtMobile);
                    tilEmail.setError(getResources().getString(R.string.error_msg_email));
                    tilEmail.setErrorEnabled(true);
                    validatedEmail = false;
                } else {
                    validatedEmail = true;
                    tilEmail.setErrorEnabled(false);
                }
                break;
            case R.id.edtMobile:
                if (edtMobile.getText().toString().trim().isEmpty() || edtMobile.getText().length() < 6) {
                    //requestFocus(edtMobile);
                    validatedMobile = false;
                    rltMobileError.setVisibility(View.VISIBLE);
                } else {
                    validatedMobile = true;
                    rltMobileError.setVisibility(View.GONE);
                }
                break;
            case R.id.edtPassword:
                if (edtPassword.getText().toString().trim().isEmpty() || edtPassword.getText().length() < 6) {
                    //requestFocus(edtPassword);
                    tilPassword.setError(getResources().getString(R.string.error_msg_password));
                    tilPassword.setErrorEnabled(true);
                    validatedPassword = false;
                } else {
                    validatedPassword = true;
                    tilPassword.setErrorEnabled(false);
                }
                break;
            /*case R.id.edtCity:
                if (edtCity.getText().toString().trim().isEmpty()) {
                    //requestFocus(edtPassword);
                    tilCity.setError(getResources().getString(R.string.error_msg_city));
                    tilCity.setErrorEnabled(true);
                    validatedCity = false;
                } else {
                    validatedCity = true;
                    tilCity.setErrorEnabled(false);
                }
                break;*/
            default:
                break;
        }

        if (validatedFirstName && validatedLastName && validatedEmail && validatedMobile && validatedPassword /*&& validatedCity*/) {
            btnSignup.setEnabled(true);
        } else {
            btnSignup.setEnabled(false);
        }

        if (edtMobile.getText().toString().startsWith("0")) {
            edtMobile.setText("");
        }
    }

    /*
    *   focus edit text
    */
    /*private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }*/

    /**
     * Custom textview to show with link
     */
    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(getResources().getString(R.string.sign_term1));
        spanTxt.append(" ");
        spanTxt.append(getResources().getString(R.string.sign_term2));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String terms = getResources().getString(R.string.siteurl) + getResources().getString(R.string.terms_url)+"?token="+sessionManager.getAccessToken();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(terms));
                startActivity(browserIntent);
            }
        }, spanTxt.length() - getResources().getString(R.string.sign_term2).length(), spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_green)), spanTxt.length() - getResources().getString(R.string.sign_term2).length(), spanTxt.length(), 0);
        spanTxt.append(" ");
        spanTxt.append(getResources().getString(R.string.sign_term3));
        spanTxt.append(" ");
        spanTxt.append(getResources().getString(R.string.sign_term_4));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String privacy = getResources().getString(R.string.siteurl) + getResources().getString(R.string.privacy_url)+"?token="+sessionManager.getAccessToken();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacy));
                startActivity(browserIntent);
            }
        }, spanTxt.length() - getResources().getString(R.string.sign_term_4).length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_green)), spanTxt.length() - getResources().getString(R.string.sign_term_4).length(), spanTxt.length(), 0);
        spanTxt.append(getResources().getString(R.string.sign_term5));
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
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
            if (edtMobile.getText().toString().startsWith("0")) {
                edtMobile.setText("");
            }
            validate(view);
        }

        public void afterTextChanged(Editable editable) {
            //validate(view);
        }
    }
}