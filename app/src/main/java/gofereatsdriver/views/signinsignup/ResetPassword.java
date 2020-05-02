package gofereatsdriver.views.signinsignup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.signinsignup
 * @category ResetPassword
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;


/*************************************************************
 ResetPassword
 Its used to get the reset password detail function
 *************************************************************** */
public class ResetPassword extends AppCompatActivity implements ServiceListener {

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
    public @InjectView(R.id.edtPassword)
    EditText edtPassword;
    public @InjectView(R.id.edtConfirmPassword)
    EditText edtConfirmPassword;
    public @InjectView(R.id.input_layout_password)
    TextInputLayout input_layout_password;
    public @InjectView(R.id.input_layout_confirmpassword)
    TextInputLayout input_layout_confirmpassword;
    public @InjectView(R.id.rltNext)
    RelativeLayout rltNext;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Reset Password
     */
    @OnClick(R.id.rltNext)
    public void next() {
        if (!validateFirst()) {
            return;
        } else if (!validateconfrom()) {
            return;
        } else {
            String input_password_str = edtPassword.getText().toString();
            String input_password_confirmstr = edtConfirmPassword.getText().toString();
            if (input_password_str.length() > 5 && input_password_confirmstr.length() > 5 && input_password_confirmstr.equals(input_password_str)) {

                resetPassword(input_password_str);

            } else {
                if (!input_password_confirmstr.equals(input_password_str)) {
                    snackBar(getString(R.string.Passwordmismatch), "", false, 2);
                } else {
                    snackBar(getString(R.string.InvalidPassword), "", false, 2);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);
        tvTitle.setText(getResources().getString(R.string.resetpasswords));
        vBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }


    /**
     * Reset password Api
     */
    public void resetPassword(String password) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.resetPassword(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(), password).enqueue(new RequestCallback(this));
    }

    /*
     *   Validate password field
     */
    private boolean validateFirst() {
        if (edtPassword.getText().toString().trim().isEmpty()) {
            input_layout_password.setError(getString(R.string.Enteryourpassword));
            requestFocus(edtPassword);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }

    /*
     *   Validate Confirm password field
     */
    private boolean validateconfrom() {
        if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
            input_layout_confirmpassword.setError(getString(R.string.Confirmyourpassword));
            requestFocus(edtConfirmPassword);
            return false;
        } else {
            input_layout_confirmpassword.setErrorEnabled(false);
        }

        return true;
    }

    /*
     *   Focus edit text field
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /*
     *  Snack bar to show error or information
     */
    public void snackBar(String message, String buttonmessage, boolean buttonvisible, int duration) {
        // Create the Snackbar
        Snackbar snackbar;
        RelativeLayout snackbar_background;
        TextView snack_button;
        TextView snack_message;

        // Snack bar visible duration
        if (duration == 1) snackbar = Snackbar.make(rltNext, "", Snackbar.LENGTH_INDEFINITE);
        else if (duration == 2) snackbar = Snackbar.make(rltNext, "", Snackbar.LENGTH_LONG);
        else snackbar = Snackbar.make(rltNext, "", Snackbar.LENGTH_SHORT);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        snackbar_background = snackView.findViewById(R.id.snackbar);
        snack_button = snackView.findViewById(R.id.snack_button);
        snack_message = snackView.findViewById(R.id.snack_message);

        snackbar_background.setBackgroundColor(getResources().getColor(R.color.black)); // Background Color

        if (buttonvisible) // set Right side button visible or gone
            snack_button.setVisibility(View.VISIBLE);
        else snack_button.setVisibility(View.GONE);

        //snack_button.setTextColor(getResources().getColor(R.color.bluelight)); // set right side button text color
        snack_button.setText(buttonmessage); // set right side button text

        snack_message.setTextColor(getResources().getColor(R.color.white)); // set left side main message text color
        snack_message.setText(message);  // set left side main message text

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        // Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.black));
        snackbar.show();
    }


    /**
     * Success Result From API
     *
     * @param jsonResp
     * @param data
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
            onSuccessResetLogin();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    /**
     * Reset Password and login to Map page
     */
    private void onSuccessResetLogin() {
        Intent restlogin = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(restlogin);
        finishAffinity();
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }
}
