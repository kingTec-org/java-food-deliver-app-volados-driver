package gofereatsdriver.views.main.pickuporderdetails;
/**
 * @package com.gofereats
 * @subpackage views.main.pickuporderdetails
 * @category ContactActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 ContactActivity
 Its used to Contact Restaurant and Eater through Message and call
 ****************************************************************/
public class ContactActivity extends AppCompatActivity {


    public static final Integer CALL = 0x2;
    public static final Integer SMS = 300;
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
    public @Inject
    RunTimePermission runTimePermission;
    public @InjectView(R.id.rltContactResturant)
    RelativeLayout rltContactResturant;
    public @InjectView(R.id.rltContactRecipient)
    RelativeLayout rltContactRecipient;
    public @InjectView(R.id.lltResturanrtCall)
    LinearLayout lltResturanrtCall;
    public @InjectView(R.id.lltRestuarantMessage)
    LinearLayout lltRestuarantMessage;
    public @InjectView(R.id.lltRecipientCall)
    LinearLayout lltRecipientCall;
    public @InjectView(R.id.lltRecipientMessage)
    LinearLayout lltRecipientMessage;
    public @InjectView(R.id.tvProjectName)
    CustomTextView tvProjectName;
    public @InjectView(R.id.tvRecipientName)
    CustomTextView tvRecipientName;
    public @InjectView(R.id.tvResturantNumber)
    CustomTextView tvResturantNumber;
    public @InjectView(R.id.tvRecipientNumber)
    CustomTextView tvRecipientNumber;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public String recipientName;
    public String restaurantName;
    public String recipientNumber;
    public String restaurantNumber;
    public int status;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    /**
     * Contact Restaurant by Message
     */
    @OnClick(R.id.lltRestuarantMessage)
    public void smsRestaurant() {
        sendSms(restaurantNumber);
    }

    /**
     * Contact recipient by Message
     */
    @OnClick(R.id.lltRecipientMessage)
    public void smsRecipient() {
        sendSms(recipientNumber);
    }

    /**
     * Contact Restaurant by call
     */
    @OnClick(R.id.lltResturanrtCall)
    public void callResturant() {
        phoneCall(tvResturantNumber);
    }

    /**
     * Contact recipient by Message
     */
    @OnClick(R.id.lltRecipientCall)
    public void callRecipient() {
        phoneCall(tvRecipientNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        commonMethods.rotateArrow(ivBack,this);
        getintent();
        loaddata();
    }

    /**
     * Call optionregister
     *
     * @param phonenumber
     */
    public void phoneCall(CustomTextView phonenumber) {
        String callnumber = phonenumber.getText().toString();
        // askForPermission(Manifest.permission.CALL_PHONE, CALL);
        String[] perm = {Manifest.permission.CALL_PHONE};
        //checkAllPermission(perm);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + callnumber));
        /*if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        startActivity(intent);
    }

    /**
     * Call permission
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ContactActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ContactActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ContactActivity.this, new String[]{permission}, requestCode);
            }
        }
    }

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(ContactActivity.this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {

            boolean isBlocked = runTimePermission.isPermissionBlocked(ContactActivity.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(ContactActivity.this, permission, 150);
            }
        }
    }

    /**
     * If any one or more permission is deny or block show the enable permission dialog
     */
    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * Open permission dialog
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Message option
     *
     * @param numb
     */
    public void sendSms(String numb) {
        //askForPermission(Manifest.permission.SEND_SMS, SMS);
        //String[] perm = {Manifest.permission.SEND_SMS};
        //checkAllPermission(perm);
        Uri uri = Uri.parse("smsto:" + numb);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
       /* smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", numb);*/
        smsIntent.putExtra("sms_body", "");
       /* if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        startActivity(smsIntent);
    }

    /**
     * Getting Details Form the Intents
     */
    public void getintent() {
        status = getIntent().getIntExtra("status", 0);
        restaurantName = getIntent().getStringExtra("restaurantname");
        restaurantNumber = getIntent().getStringExtra("restaurantnumber");
        recipientName = getIntent().getStringExtra("eatername");
        recipientNumber = getIntent().getStringExtra("eaternumber");
    }

    /**
     * Enabling views
     */
    public void loaddata() {
        if (status == 0 || status == 1) {
            rltContactRecipient.setVisibility(View.GONE);
        } else {
            rltContactResturant.setVisibility(View.GONE);
        }
        tvTitle.setText(getResources().getString(R.string.contact_C));
        vBottom.setVisibility(View.VISIBLE);
        tvRecipientName.setText(recipientName);
        tvProjectName.setText(restaurantName);
        tvRecipientNumber.setText(recipientNumber);
        tvResturantNumber.setText(restaurantNumber);
    }
}
