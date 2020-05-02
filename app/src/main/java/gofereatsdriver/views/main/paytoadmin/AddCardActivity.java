package gofereatsdriver.views.main.paytoadmin;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.paytoadmin
 * @category AddCardActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.obs.CustomButton;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 AddCardActivity
 Its used to Add Stripe Card details for the Driver
 ****************************************************************/
public class AddCardActivity extends AppCompatActivity {

    private static int REQUEST_CODE_PAYMENT = 1;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    ImageUtils imageUtils;
    public @InjectView(R.id.stripe)
    CardMultilineWidget stripe;
    public @InjectView(R.id.ccp)
    CountryCodePicker countryCodePicker;
    public @InjectView(R.id.btnAddCard)
    CustomButton btnAddCard;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    private String stripePublishKey;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Save Card
     */
    @OnClick(R.id.btnAddCard)
    public void addCard() {
        saveCard();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        countryCodePicker.setAutoDetectedCountry(true);
        tvTitle.setText(getResources().getString(R.string.add_credit_card));
        commonMethods.rotateArrow(ivBack,this);
        stripePublishKey = getIntent().getStringExtra("stripePublishKey");
    }

    /**
     * Save the card
     */
    public void saveCard() {
        Card cardToSave = stripe.getCard();
        if (cardToSave == null) {
            Toast.makeText(getApplicationContext(), "Card error", Toast.LENGTH_LONG).show();
        } else {
            commonMethods.showProgressDialog(AddCardActivity.this, customDialog);
            final Stripe stripe = new Stripe(getApplicationContext(), stripePublishKey);
            stripe.createToken(cardToSave, new TokenCallback() {
                public void onSuccess(Token token) {
                    commonMethods.hideProgressDialog();
                    // Send token to your server
                    //Toast.makeText(getApplicationContext(),"Token "+token.getId().toString(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("token", token.getId());
                    setResult(REQUEST_CODE_PAYMENT, intent);
                    finish();//finishing activity
                }

                public void onError(Exception error) {
                    // Show localized error message
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("token", "");
        setResult(REQUEST_CODE_PAYMENT, intent);
        finish();
    }
}
