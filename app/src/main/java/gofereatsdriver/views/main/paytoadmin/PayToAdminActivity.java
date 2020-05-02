package gofereatsdriver.views.main.paytoadmin;
/**
 * @package com.gofereats
 * @subpackage views.main.paytoadmin
 * @category PayToAdminActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.CustomButton;
import com.obs.CustomEditText;
import com.obs.CustomTextView;

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
 PayToAdminActivity
 Its used to pay Owe amount to admin by driver
 ****************************************************************/
public class PayToAdminActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @InjectView(R.id.tvOweAmount)
    CustomTextView tvOweAmount;
    public @InjectView(R.id.btnAddAmount)
    CustomButton btnAddAmount;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public DialogInject dialogInject;
    private BottomSheetDialog dialog;
    private AlertDialog alertDialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }


    /**
     * Add AMount to pay to Admin
     */
    @OnClick(R.id.btnAddAmount)
    public void addToWallet() {
        showAddWallet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_to_admin);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);

        tvTitle.setText(getResources().getString(R.string.pay_to));
        alertDialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack,this);

        if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            tvOweAmount.setText(sessionManager.getCurrencySymbol() + " " + sessionManager.getOweAmount());
        else
            tvOweAmount.setText(sessionManager.getOweAmount() + " " + sessionManager.getCurrencySymbol());

        if (sessionManager.getOweAmount().equals("0") || sessionManager.getOweAmount().equals("0.00")) {
            btnAddAmount.setEnabled(false);
            btnAddAmount.setBackgroundColor(getResources().getColor(R.color.black_alpha_20));
        } else {
            btnAddAmount.setEnabled(true);
            btnAddAmount.setBackground(getResources().getDrawable(R.drawable.background_black_full));
        }

    }

    /**
     * Dialog to add Amount in wallet
     */
    private void showAddWallet() {

        dialog = new BottomSheetDialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.add_wallet_amount);
        dialogInject = new DialogInject();
        // 5. We bind the elements of the included layouts.
        ButterKnife.inject(dialogInject, dialog);
        dialogInject.setMethod();
        dialog.show();

    }

    /**
     * Pay owe amount to admin
     */
    public void addStripeToWallet(CustomEditText edtOweAmount) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.payToAdmin(edtOweAmount.getText().toString().trim(), sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    /**
     * Success On API
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, alertDialog, data);
            return;
        }
        if (jsonResp.isSuccess()) {
            onSuccessGiveOweAmount(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, alertDialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    /**
     * Result After Success
     */
    private void onSuccessGiveOweAmount(JsonResponse jsonResp) {
        String oweAmount = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "owe_amount", String.class);

        sessionManager.setOweAmount(oweAmount);
        if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            tvOweAmount.setText(sessionManager.getCurrencySymbol() + " " + sessionManager.getOweAmount());
        else
            tvOweAmount.setText(sessionManager.getOweAmount() + " " + sessionManager.getCurrencySymbol());


        if (sessionManager.getOweAmount().equals("0") || sessionManager.getOweAmount().equals("0.00")) {
            btnAddAmount.setEnabled(false);
            btnAddAmount.setBackgroundColor(getResources().getColor(R.color.black_alpha_20));
        } else {
            btnAddAmount.setEnabled(true);
            btnAddAmount.setBackground(getResources().getDrawable(R.drawable.background_black_full));
        }
    }

    /**
     * To show error or information
     */
    public void snackBar(String message, String buttonmessage, boolean buttonvisible, int duration) {
        // Create the Snackbar
        final Snackbar snackbar;
        RelativeLayout snackbar_background;
        TextView snack_button;
        TextView snack_message;

        // Snack bar visible duration
        if (duration == 1) snackbar = Snackbar.make(tvOweAmount, "", Snackbar.LENGTH_INDEFINITE);
        else if (duration == 2) snackbar = Snackbar.make(tvOweAmount, "", Snackbar.LENGTH_LONG);
        else snackbar = Snackbar.make(tvOweAmount, "", Snackbar.LENGTH_SHORT);

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

        snack_button.setTextColor(getResources().getColor(R.color.white)); // set right side button text color
        snack_button.setText(buttonmessage); // set right side button text
        snack_button.setOnClickListener(new View.OnClickListener() {  //set right side button onclick
            @Override
            public void onClick(View v) {
                snackbar.dismiss();

            }
        });

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
     * Annotation  using ButterKnife lib to Injection and OnClick for Accept or pause dialog
     **/
    public class DialogInject {

        public @InjectView(R.id.edtOweAmount)
        CustomEditText edtOweAmount;
        public @InjectView(R.id.tvChange)
        CustomTextView tvChange;
        public @InjectView(R.id.ivPaymentImage)
        ImageView ivPaymentImage;
        public @InjectView(R.id.tvPaymentMethod)
        CustomTextView tvPaymentMethod;
        public @InjectView(R.id. currency_symbol_wallet)
        CustomTextView currencySymbol;
        public @InjectView(R.id.btnAddMoney)
        CustomButton btnAddMoney;

        @OnClick(R.id.tvChange)
        public void changePaymentMethod() {
            Intent change = new Intent(getApplicationContext(), PaymentActivity.class);
            startActivity(change);
            dialog.dismiss();
        }

        @OnClick(R.id.btnAddMoney)
        public void addMoney() {
            dialog.dismiss();
            edtOweAmount.setSelection(edtOweAmount.getText().length());
            if (edtOweAmount.getText().toString().length() > 0) {

                if (sessionManager.getWalletCard() == 1) {
                    //Stripe
                    Float enterAmount = Float.parseFloat(edtOweAmount.getText().toString());
                    Float oweAmount = Float.parseFloat(sessionManager.getOweAmount());
                    if (enterAmount > oweAmount) {

                        commonMethods.showMessage(PayToAdminActivity.this, alertDialog, getString(R.string.entered_amt_msg));

                    } else {
                        addStripeToWallet(edtOweAmount);

                    }
                } /*else {
                    // For Others
                }*/
            } else {
                snackBar(getString(R.string.enter_amount_empty), "", false, 3);
            }
        }

        public void setMethod() {
            if("1".equalsIgnoreCase(getString(R.string.layout_direction))) {
                edtOweAmount.setGravity(Gravity.END);
                edtOweAmount.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            }
            currencySymbol.setText(sessionManager.getCurrencySymbol());
            edtOweAmount.setText(sessionManager.getOweAmount());
            edtOweAmount.setSelection(edtOweAmount.getText().length());
            if (sessionManager.getWalletCard() == 0) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.credit_card));
                tvPaymentMethod.setText(getResources().getString(R.string.add_card));
                btnAddMoney.setEnabled(false);
                btnAddMoney.setBackgroundColor(getResources().getColor(R.color.black_alpha_20));
            } else if (sessionManager.getWalletCard() == 1) {
                setCardImage(sessionManager.getCardBrand());
                tvPaymentMethod.setText("•••• " + sessionManager.getCardValue());
                btnAddMoney.setEnabled(true);
                btnAddMoney.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_black_full));
            }
        }

        /**
         * Set card images
         *
         * @param brand Type of a card
         */
        public void setCardImage(String brand) {
            if (brand.contains("Visa")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_visa));
            } else if (brand.contains("MasterCard")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_master));
            } else if (brand.contains("Discover")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_discover));
            } else if (brand.contains("Amex")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_amex));
            } else if (brand.contains("JCP")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_jcp));
            } else if (brand.contains("Diner")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_diner));
            } else if (brand.contains("Union")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_unionpay));
            } else {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_basic));
            }
        }
    }
}