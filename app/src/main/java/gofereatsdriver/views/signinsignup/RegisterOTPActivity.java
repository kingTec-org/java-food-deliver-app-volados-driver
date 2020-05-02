package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.signinsignup
 * @category RegisterOTPActivity
 * @author Trioangle Product Team
 * @version 1.0
 */


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.Constants;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.driverprofile.DriverModel;
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
import gofereatsdriver.views.main.profile.DriverProfile;

import static gofereatsdriver.utils.Enums.REQ_CHANGE_NUMBER;
import static gofereatsdriver.utils.Enums.REQ_RESEND_OTP;
import static gofereatsdriver.utils.Enums.REQ_SIGNUP;

/*************************************************************
 RegisterOTPActivity
 Its used to get the register mobile number OTP detail function
 *************************************************************** */
public class RegisterOTPActivity extends AppCompatActivity implements ServiceListener {

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
    public @InjectView(R.id.edtOne)
    EditText edtOne;
    public @InjectView(R.id.edtTwo)
    EditText edtTwo;
    public @InjectView(R.id.edtThree)
    EditText edtThree;
    public @InjectView(R.id.edtFour)
    EditText edtFour;
    public @InjectView(R.id.tvResend_timer)
    TextView tvResend_timer;
    public @InjectView(R.id.tvCodetext)
    CustomTextView tvCodetext;

    public @InjectView(R.id.rltNext)
    RelativeLayout rltNext;
    public @InjectView(R.id.tvResend)
    TextView tvResend;
    public HashMap<String, String> hashMap;
    public int otp;
    public CountDownTimer countDownTimer;
    public boolean isResetpassword, isChange;
    public String accesstoken;
    public String[] otpNumbers;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * verify OTP
     */
    @OnClick(R.id.rltNext)
    public void next() {
        String emtytextone = edtOne.getText().toString().trim();
        String emtytexttwo = edtTwo.getText().toString().trim();
        String emtytextthree = edtThree.getText().toString().trim();
        String emtytextfour = edtFour.getText().toString().trim();

        if (emtytextone.isEmpty() || emtytexttwo.isEmpty() || emtytextthree.isEmpty() || emtytextfour.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.otp_emty), Toast.LENGTH_SHORT).show();
        } else {
            String otpcode = edtOne.getText().toString() + "" + edtTwo.getText().toString() + "" + edtThree.getText().toString() + "" + edtFour.getText().toString() + "";

            if (otpcode.equals(String.valueOf(otp))) {
                countDownTimer.cancel();
                if (isResetpassword) {
                    /**
                     * Reset Your Password
                     */
                    Intent reset = new Intent(getApplicationContext(), ResetPassword.class);
                    startActivity(reset);
                    finish();
                    overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
                } else if (isChange) {
                    changeNumber();
                } else {
                    registerDriver();       //Driver Register
                }

            } else {
                Toast.makeText(this, getResources().getString(R.string.otp_mismatch), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.tvResend)
    public void resendOTP() {
        edtOne.getText().clear();
        edtTwo.getText().clear();
        edtThree.getText().clear();
        edtFour.getText().clear();
        edtOne.requestFocus();
        resendOtp();
        countdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerotp);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);


        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashmap");
        isResetpassword = intent.getBooleanExtra("resetpassword", false);
        isChange = intent.getBooleanExtra("isChange", false);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        otp = getIntent().getIntExtra("otp", 0);
        //Toast.makeText(this, "Your OTP is " + otp, Toast.LENGTH_LONG).show();
        //ivBack.setColorFilter(ContextCompat.getColor(this, R.color.app_text_black), android.graphics.PorterDuff.Mode.MULTIPLY);

        tvTitle.setVisibility(View.GONE);
        vBottom.setVisibility(View.GONE);

        String str1 = getString(R.string.enter4digit) + " " + sessionManager.getPhoneNumber();
        int str = str1.length();
        int strmi = 10;
        strmi = sessionManager.getPhoneNumber().length();
        int start = str1.length() - strmi;
        //TO set edit text select all
        edtOne.setSelectAllOnFocus(true);
        edtTwo.setSelectAllOnFocus(true);
        edtThree.setSelectAllOnFocus(true);
        edtFour.setSelectAllOnFocus(true);

        //Setting the edit text cursor at Start
        int position1 = edtOne.getSelectionStart();
        int position2 = edtTwo.getSelectionStart();
        int position3 = edtThree.getSelectionStart();
        int position4 = edtFour.getSelectionStart();

        edtOne.setSelection(position1);
        edtTwo.setSelection(position2);
        edtThree.setSelection(position3);
        edtFour.setSelection(position4);

        setKeyListener(edtOne);
        setKeyListener(edtTwo);
        setKeyListener(edtThree);
        setKeyListener(edtFour);
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            edtOne.setGravity(Gravity.END);
            edtOne.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            edtTwo.setGravity(Gravity.END);
            edtTwo.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            edtThree.setGravity(Gravity.END);
            edtThree.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            edtFour.setGravity(Gravity.END);
            edtFour.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        edtOne.addTextChangedListener(new textWatcher(edtOne));
        edtTwo.addTextChangedListener(new textWatcher(edtTwo));
        edtThree.addTextChangedListener(new textWatcher(edtThree));
        edtFour.addTextChangedListener(new textWatcher(edtFour));

        /**
         * Splitting Opt And Set on its Edit text
         */
        otpNumbers = String.valueOf(otp).split("(?!^)");

        //Toast.makeText(this, "Your OTP is " + otp, Toast.LENGTH_LONG).show();

     /*   edtOne.setText(otpNumbers[0]);
        edtTwo.setText(otpNumbers[1]);
        edtThree.setText(otpNumbers[2]);
        edtFour.setText(otpNumbers[3]);
        edtFour.setSelection(edtFour.getText().length());*/

        countdown();
        final SpannableStringBuilder str3 = new SpannableStringBuilder(str1);
        str3.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, str, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCodetext.setText(str3);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }

    /**
     * Reset your Mobile Number
     */
    public void changeNumber() {

        commonMethods.showProgressDialog(this, customDialog);
        apiService.changeMobile(sessionManager.getToken(), sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode()).enqueue(new RequestCallback(REQ_CHANGE_NUMBER, this));

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {

            // Resend OTP
            case REQ_RESEND_OTP:
                if (jsonResp.isSuccess()) {
                    onSuccessResendOTP(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;

            //Register Driver
            case REQ_SIGNUP:
                if (jsonResp.isSuccess()) {
                    onSuccessRegisterDriverDetails(jsonResp);
                }
                break;
            case REQ_CHANGE_NUMBER:
                if (jsonResp.isSuccess()) {
                    onSuccessChangeNumber(jsonResp);
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

    private void onSuccessChangeNumber(JsonResponse jsonResp) {

        DriverModel driverModel = gson.fromJson(jsonResp.getStrResponse(), DriverModel.class);

        Intent changeNumber = new Intent(getApplicationContext(), DriverProfile.class);
        changeNumber.putExtra("name", driverModel.getDriverDetailsModel().getName());
        changeNumber.putExtra("email", driverModel.getDriverDetailsModel().getEmail());
        changeNumber.putExtra("mobile", driverModel.getDriverDetailsModel().getMobilenumber());
        changeNumber.putExtra("countrycode", driverModel.getDriverDetailsModel().getCountrycode());
        changeNumber.putExtra("street", driverModel.getDriverDetailsModel().getStreet());
        changeNumber.putExtra("area", driverModel.getDriverDetailsModel().getArea());
        changeNumber.putExtra("city", driverModel.getDriverDetailsModel().getCity());
        changeNumber.putExtra("state", driverModel.getDriverDetailsModel().getState());
        changeNumber.putExtra("postalcode", driverModel.getDriverDetailsModel().getPostalcode());
        changeNumber.putExtra("country", driverModel.getDriverDetailsModel().getCountry());
        changeNumber.putExtra("profile", driverModel.getDriverDetailsModel().getProfileimage());
        startActivity(changeNumber);
        finish();
        Toast.makeText(this, "Mobile Number Changed Successfully", Toast.LENGTH_SHORT).show();

    }


    /**
     * Calling Api For Driver Register
     */
    public void registerDriver() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.register(sessionManager.getType(), hashMap,sessionManager.getLanguageCode()).enqueue(new RequestCallback(REQ_SIGNUP, this));
    }

    /**
     * Getting  Resended OTP
     */
    private void onSuccessResendOTP(JsonResponse jsonResp) {

        otp = (int) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.OTP, Integer.class);

        /**
         * Splitting Opt And Set on its Edit text
         */
        otpNumbers = String.valueOf(otp).split("(?!^)");

        //Toast.makeText(this, "Your OTP is " + otp, Toast.LENGTH_LONG).show();

       /* edtOne.setText(otpNumbers[0]);
        edtTwo.setText(otpNumbers[1]);
        edtThree.setText(otpNumbers[2]);
        edtFour.setText(otpNumbers[3]);
        edtFour.setSelection(edtFour.getText().length());*/
        //Toast.makeText(this, "Your OTP is " + otp, Toast.LENGTH_LONG).show();
    }

    /**
     * On Success Of Register API and getting the Access Token
     */
    private void onSuccessRegisterDriverDetails(JsonResponse jsonResponse) {

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
                    Intent login = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(login);
                    finish();
                    overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
                }
            }
        }
    }

    public void validate(View view) {
        switch (view.getId()) {
            case R.id.edtOne:
                if (edtOne.getText().toString().length() == 1)     //size as per your requirement
                {
                    edtTwo.requestFocus();
                }
                break;
            case R.id.edtTwo:
                if (edtTwo.getText().toString().length() == 1)     //size as per your requirement
                {
                    edtThree.requestFocus();
                }
                break;
            case R.id.edtThree:
                if (edtThree.getText().toString().length() == 1)     //size as per your requirement
                {
                    edtFour.requestFocus();
                }
                break;
            case R.id.edtFour:
                if (edtThree.getText().toString().length() == 1)     //size as per your requirement
                {
                    edtFour.requestFocus();
                }
                break;
            default:
                break;
        }
    }

    public void setKeyListener(View view) {
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (v.getId()) {
                    case R.id.edtOne:
                       /* if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {
                            //one.requestFocus();
                        }*/
                        break;
                    case R.id.edtTwo:
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {

                            edtTwo.requestFocus();
                        }
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL) && edtTwo.getText().toString().length() == 0) {

                            edtOne.requestFocus();
                            edtOne.getText().clear();
                        }
                        break;
                    case R.id.edtThree:
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {

                            edtThree.requestFocus();
                        }
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL) && edtThree.getText().toString().length() == 0) {

                            edtTwo.requestFocus();
                            edtTwo.getText().clear();
                        }
                        break;
                    case R.id.edtFour:
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {
                            edtFour.requestFocus();
                        }
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL) && edtFour.getText().toString().length() == 0) {
                            edtThree.requestFocus();
                            edtThree.getText().clear();
                        }
                        break;
                    default:
                        break;

                }
                return false;
            }
        });
    }

    /**
     * Timer  method
     */
    public void countdown() {
        tvResend.setEnabled(false);
        tvResend_timer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                tvResend_timer.setText("00:" + f.format(millisUntilFinished / 1000));
            }

            public void onFinish() {
                tvResend.setEnabled(true);
                tvResend_timer.setText("00:00");
                tvResend_timer.setVisibility(View.INVISIBLE);
                //resend_timer.setText("done!");
            }
        }.start();
    }

    /**
     * Resend OTP API
     */
    private void resendOtp() {
        if (isResetpassword) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.forgotPassword(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(),sessionManager.getLanguageCode()).enqueue(new RequestCallback(REQ_RESEND_OTP, this));
        } else {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.numberValidation(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(),sessionManager.getLanguageCode()).enqueue(new RequestCallback(REQ_RESEND_OTP, this));
        }
    }

    /*
     *   Edit text, Text watcher
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

        }
    }
}