package gofereatsdriver.views.main.paytoadmin;
/**
 * @package com.gofereats
 * @subpackage views.main.paytoadmin
 * @category Payment Activity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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
import gofereatsdriver.utils.Enums;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 PaymentActivity
 Its used to show the Payment details for the Driver
 ****************************************************************/

public class PaymentActivity extends AppCompatActivity implements ServiceListener {


    private static int REQUEST_CODE_PAYMENT = 1;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CustomDialog customDialog1;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    ImageUtils imageUtils;
    public @InjectView(R.id.ivCardTick)
    ImageView ivCardTick;
    public @InjectView(R.id.ivCard)
    ImageView ivCard;
    public @InjectView(R.id.tvCard)
    CustomTextView tvCard;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.rltCard)
    RelativeLayout rltCard;
    public @InjectView(R.id.rltAddCard)
    RelativeLayout rltAddCard;
    private String stripePublishKey;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Set payment as Card
     */
    @OnClick(R.id.rltCard)
    public void cardClick() {
        sessionManager.setPaymentMethod(1);
        ivCardTick.setVisibility(View.VISIBLE);
    }

    /**
     * goes to Add Card Page
     */
    @OnClick(R.id.rltAddCard)
    public void addCardClick() {
        Intent stripe = new Intent(getApplicationContext(), AddCardActivity.class);
        stripe.putExtra("stripePublishKey", stripePublishKey);
        startActivityForResult(stripe, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);

        dialog = commonMethods.getAlertDialog(this);
        /**
         *  View Card Details
         */
        commonMethods.showProgressDialog(this, customDialog);
        commonMethods.rotateArrow(ivBack,this);
        apiService.viewCard(sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_VIEW_PAYMENT, this));

    }

    /**
     * Result form Add card
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {

            paymentCompleted(data.getStringExtra("token"));
        }
    }

    /**
     * After Stripe payment
     */
    public void paymentCompleted(String payKey) {
        //commonMethods.showProgressDialog(PaymentActivity.this, customDialog);
        if (!TextUtils.isEmpty(payKey))
            apiService.addCard(payKey, sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_ADD_CARD, this));
    }

    /**
     * On Success From API
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_ADD_CARD:
                if (jsonResp.isSuccess()) {

                    String brand = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "brand", String.class);
                    String last4 = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "last4", String.class);

                    if (!TextUtils.isEmpty(last4)) {
                        rltCard.setVisibility(View.VISIBLE);
                        setCardImage(brand);
                        tvCard.setText("•••• " + last4);
                        ivCardTick.setVisibility(View.VISIBLE);
                        sessionManager.setPaymentMethod(1);
                        sessionManager.setWalletCard(1);

                        sessionManager.setCardValue(last4);
                        sessionManager.setCardBrand(brand);
                    } else {
                        rltCard.setVisibility(View.GONE);
                    }
                    // onBackPressed();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()) && !"Trying to get property of non-object".equals(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case Enums.REQ_VIEW_PAYMENT:
                if (jsonResp.isSuccess()) {
                    String brand = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "brand", String.class);
                    String last4 = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "last4", String.class);

                    if (!TextUtils.isEmpty(last4)) {
                        rltCard.setVisibility(View.VISIBLE);
                        setCardImage(brand);
                        tvCard.setText("•••• " + last4);
                        sessionManager.setCardValue(last4);
                        sessionManager.setCardBrand(brand);
                        sessionManager.setWalletCard(1);
                        ivCardTick.setVisibility(View.VISIBLE);
                    } else {
                        rltCard.setVisibility(View.GONE);
                    }
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()) && !"Trying to get property of non-object".equals(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                stripePublishKey = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "stripe_publish_key", String.class);
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

    /**
     * Set Card Images
     */
    public void setCardImage(String brand) {
        if ("Visa".contains(brand)) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_visa));
        } else if ("MasterCard".contains(brand)) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_master));
        } else if ("Discover".contains(brand)) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_discover));
        } else if (brand.contains("Amex") || brand.contains("American Express")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_amex));
        } else if (brand.contains("JCB") || brand.contains("JCP")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_jcp));
        } else if (brand.contains("Diner") || brand.contains("Diners")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_diner));
        } else if ("Union".contains(brand) || "UnionPay".contains(brand)) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_unionpay));
        } else {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_basic));
        }
    }
}