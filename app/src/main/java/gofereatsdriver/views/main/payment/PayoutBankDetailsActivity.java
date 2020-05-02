package gofereatsdriver.views.main.payment;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.main.payment
 * @category PayoutBankDetailsActivity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomEditText;
import com.obs.CustomTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.backgroundtask.ImageCompressAsyncTask;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.Constants;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.CountryModels;
import gofereatsdriver.datamodels.StripeModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ImageListener;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.payoutbank.PayoutCountryAdapter;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static gofereatsdriver.utils.Enums.REQ_UPDATE_PROFILE;

/*************************************************************
 PayoutBankDetailsActivity
 Its used to Add Bank Details for the Driver
 ****************************************************************/
public class PayoutBankDetailsActivity extends AppCompatActivity implements ImageListener, ServiceListener {

    public static android.app.AlertDialog alertDialog;
    public String[] countryname = {"Austria", "Australia", "Belgium", "Canada", "Denmark", "Finland", "France", "Germany", "Hong Kong", "Ireland", "Italy", "Japan", "Luxembourg", "Netherlands", "New Zealand", "Norway", "Portugal", "Singapore", "Spain", "Sweden", "Switzerland", "United Kingdom", "United States", "Other"};
    public String[] currencyname = {"EUR", "DKK", "GBP", "NOK", "SEK", "USD", "CHF"};
    public String[] currencynamecanada = {"CAD", "USD"};
    public String[] genders = {"Male", "Female"};
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    ApiService apiService;
    public @Inject
    RunTimePermission runTimePermission;
    public RecyclerView recyclerView1;
    public PayoutCountryAdapter countryListAdapter;
    public Bitmap thumbnail = null;
    public int docType = 7;          //By Default Set 7 for Profile Image
    public RequestBody requestbody;
    public String addresskanji1names, addresskanji2names, kanjicitynames, kanjistatenames, kanjipostalcodenames, addresskana1names, addresskana2names, kanacitynames, kanastatenames, kanapostalcodenames, gendernames, Phonenumbernames, accountownernames, banknames, branchnames, branch_code_names, bank_code_names, transitnonames, routing_number_names, ssn_names, institutenonames, bsbnames, sort_codenames, clearingcodenames, Ac_nonames, CountryNames, currencynames, Ibannames, accountholdernmaes, address1names, address2names, citynames, statenames, postalcodenames, companyaddressnames, bankaddressnames, swiftnames;
    public String image = null;
    public List<CountryModels> countryList;
    public @InjectView(R.id.payoutaddress_country)
    CustomEditText payoutaddress_country;
    public @InjectView(R.id.payoutaddress_currency)
    CustomEditText payoutaddress_currency;
    public @InjectView(R.id.etAccountName)
    CustomEditText etAccountName;
    public @InjectView(R.id.gender)
    CustomEditText gender;
    public @InjectView(R.id.legal_doc)
    CustomTextView legal_doc;
    public @InjectView(R.id.payout_submit)
    CustomButton payout_submit;
    public @InjectView(R.id.tvTitle)
    CustomTextView tvTitle;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.bsb)
    CustomEditText bsb;
    public @InjectView(R.id.Ac_no)
    CustomEditText Ac_no;
    public @InjectView(R.id.Ac_no_other)
    CustomEditText Ac_no_other;
    public @InjectView(R.id.bank_name)
    CustomEditText bank_name;
    public @InjectView(R.id.branch_name)
    CustomEditText branch_name;
    public @InjectView(R.id.Ac_owner_name)
    CustomEditText Ac_owner_name;
    public @InjectView(R.id.addresskana2)
    CustomEditText addresskana2;
    public @InjectView(R.id.kanacity)
    CustomEditText kanacity;
    public @InjectView(R.id.kanastate)
    CustomEditText kanastate;
    public @InjectView(R.id.kanapostalcode)
    CustomEditText kanapostalcode;
    public @InjectView(R.id.ph_no)
    CustomEditText ph_no;
    public @InjectView(R.id.transit_no)
    CustomEditText transit_no;
    public @InjectView(R.id.institute_no)
    CustomEditText institute_no;
    public @InjectView(R.id.routing_number)
    CustomEditText routing_number;
    public @InjectView(R.id.ssn_number)
    CustomEditText ssn_number;
    public @InjectView(R.id.clearing_code)
    CustomEditText clearing_code;
    public @InjectView(R.id.bank_code)
    CustomEditText bank_code;
    public @InjectView(R.id.addresskana_msg)
    CustomTextView addresskana_msg;
    public @InjectView(R.id.addresskanji_msg)
    CustomTextView addresskanji_msg;
    public @InjectView(R.id.branch_code)
    CustomEditText branch_code;
    public @InjectView(R.id.sort_code)
    CustomEditText sort_code;
    public @InjectView(R.id.IbanSwift)
    CustomEditText IbanSwift;
    public @InjectView(R.id.Iban_no)
    CustomEditText Iban_no;
    public @InjectView(R.id.Ac_holder_name)
    CustomEditText Ac_holder_name;
    public @InjectView(R.id.address1)
    CustomEditText address1;
    public @InjectView(R.id.address2)
    CustomEditText address2;
    public @InjectView(R.id.city)
    CustomEditText city;
    public @InjectView(R.id.state)
    CustomEditText state;
    public @InjectView(R.id.addresskanji1)
    CustomEditText addresskanji1;
    public @InjectView(R.id.postalcode)
    CustomEditText postalcode;
    public @InjectView(R.id.addresskanji2)
    CustomEditText addresskanji2;
    public @InjectView(R.id.kanjicity)
    CustomEditText kanjicity;
    public @InjectView(R.id.kanjistate)
    CustomEditText kanjistate;
    public @InjectView(R.id.kanjipostalcode)
    CustomEditText kanjipostalcode;
    public @InjectView(R.id.addresskana1)
    CustomEditText addresskana1;
    public @InjectView(R.id.addresskanji_linear)
    LinearLayout addresskanji_linear;
    public @InjectView(R.id.addresskana_linear)
    LinearLayout addresskana_linear;
    public @InjectView(R.id.etCompanyAddress)
    CustomEditText companyAddress;
    private AlertDialog dialog;
    private int SELECT_FILE = 1, REQUEST_CAMERA = 0;
    private String imagePath = "";
    private String accountNames;
    private String Ac_no_othernames;

    /**
     * payment Country list
     */
    @OnClick(R.id.payoutaddress_country)
    public void changeProfile() {
        countryList("country");
    }

    /**
     * payment currency list
     */
    @OnClick(R.id.payoutaddress_currency)
    public void currency() {
        countryList("currency");
    }

    /**
     * payment gender list
     */
    @OnClick(R.id.gender)
    public void gender() {
        countryList("gender");
    }

    /**
     * payment select Image
     */
    @OnClick(R.id.legal_doc)
    public void imagePick() {
        selectImage();
    }

    /**
     * Submit payment
     */
    @OnClick(R.id.payout_submit)
    public void submit() {
        CountryNames = payoutaddress_country.getText().toString();
        dialog = commonMethods.getAlertDialog(this);

        if ("Austria".equals(CountryNames) || "Belgium".equals(CountryNames) || "Denmark".equals(CountryNames) || "Finland".equals(CountryNames) || "France".equals(CountryNames) || "Germany".equals(CountryNames) || "Ireland".equals(CountryNames) || "Italy".equals(CountryNames) || "Luxembourg".equals(CountryNames) || "Norway".equals(CountryNames) || "Portugal".equals(CountryNames) || "Spain".equals(CountryNames) || "Sweden".equals(CountryNames) || "Switzerland".equals(CountryNames) || "Belgium".equals(CountryNames) || "Netherlands".equals(CountryNames)) {
            Ibannames = Iban_no.getText().toString();
            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();
            image = getStringImage(thumbnail);

            imageObject.put("address1", address1names);
            imageObject.put("address2", address2names);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("account_holder_name", accountholdernmaes);
            imageObject.put("iban", Ibannames);


            if (("").equals(currencynames)) {
                commonMethods.showMessage(this, dialog,getResources().getString(R.string.please_choose_currency));
            } else if (("").equals(Ibannames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_iban));
            } else if (("").equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog,getResources().getString(R.string.please_enter_account_holder_name));
            } else if (("").equals(address1names)) {
                commonMethods.showMessage(this, dialog,getResources().getString(R.string.please_enter_address1));
            } else if (("").equals(address2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2));
            } else if (("").equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if (("").equals(statenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_state));
            } else if (("").equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else if (("").equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else if (requestbody == null) {
                commonMethods.showMessage(this, dialog,getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);
            }

            //image = getStringImage(thumbnail);
        } else if (("Australia").equals(CountryNames)) {

            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();
            bsbnames = bsb.getText().toString();
            Ac_nonames = Ac_no.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();
            //image = getStringImage(thumbnail);

            imageObject.put("address1", address1names);
            imageObject.put("address2", address2names);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("account_holder_name", accountholdernmaes);
            imageObject.put("bsb", bsbnames);
            imageObject.put("account_number", Ac_nonames);

            if (("").equals(currencynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_currency));
            } else if (("").equals(bsbnames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_bsb));
            } else if (("").equals(Ac_nonames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_number));
            } else if (("").equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_holder_name));
            } else if (("").equals(address1names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address1));
            } else if (("").equals(address2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2));
            } else if (("").equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if (("").equals(statenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_state));
            } else if ("".equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else if ("".equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);
                //updateProfile(imageObject);
            }

        } else if ("Canada".equals(CountryNames)) {
            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();
            transitnonames = transit_no.getText().toString();
            institutenonames = institute_no.getText().toString();
            Ac_nonames = Ac_no.getText().toString();

        } else if ("New Zealand".equals(CountryNames)) {
            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();
            routing_number_names = routing_number.getText().toString();
            Ac_nonames = Ac_no.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();
            //image = getStringImage(thumbnail);

            imageObject.put("address1", address1names);
            imageObject.put("address2", address2names);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("account_holder_name", accountholdernmaes);
            imageObject.put("routing_number", routing_number_names);
            //imageObject.put("document",image);
            imageObject.put("account_number", Ac_nonames);


            if ("".equals(currencynames)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_choose_currency));
            } else if ("".equals(routing_number_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_routing_number));
            } else if ("".equals(Ac_nonames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_number));
            } else if ("".equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_holder_name));
            } else if ("".equals(address1names)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_address1));
            } else if ("".equals(address2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2));
            } else if ("".equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if ("".equals(statenames)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_state));
            } else if ("".equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else if ("".equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);
                //updateProfile(imageObject);
            }

        } else if ("Singapore".equals(CountryNames)) {
            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();
            bank_code_names = bank_code.getText().toString();
            branch_code_names = branch_code.getText().toString();
            Ac_nonames = Ac_no.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();
            //image = getStringImage(thumbnail);

            imageObject.put("address1", address1names);
            imageObject.put("address2", address2names);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("bank_code", bank_code_names);
            imageObject.put("branch_code", branch_code_names);
            //imageObject.put("document",image);
            imageObject.put("account_number", Ac_nonames);
            imageObject.put("account_holder_name", accountholdernmaes);

            if ("".equals(currencynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_currency));
            } else if ("".equals(bank_code_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_bank_code));
            } else if ("".equals(branch_code_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_branch_code));
            } else if ("".equals(Ac_nonames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_number));
            } else if ("".equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_holder_name));
            } else if ("".equals(address1names)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_address1));
            } else if ("".equals(address2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2));
            } else if ("".equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if ("".equals(statenames)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_state));
            } else if ("".equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else if ("".equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);
                //updateProfile(imageObject);
            }

        } else if ("United Kingdom".equals(CountryNames)) {
            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();
            sort_codenames = sort_code.getText().toString();
            Ac_nonames = Ac_no.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();
            //image = getStringImage(thumbnail);

            imageObject.put("address1", address1names);
            imageObject.put("address2", address2names);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("sort_code", sort_codenames);
            //imageObject.put("document",image);
            imageObject.put("account_number", Ac_nonames);
            imageObject.put("account_holder_name", accountholdernmaes);


            if ("".equals(currencynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_currency));
            } else if ("".equals(sort_codenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_sort_code));
            } else if ("".equals(Ac_nonames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_number));
            } else if ("".equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_holder_name));
            } else if ("".equals(address1names)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_address1));
            } else if ("".equals(address2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2));
            } else if ("".equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if ("".equals(statenames)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_state));
            } else if ("".equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else if ("".equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);
                // updateProfile(imageObject);
            }

        } else if ("United States".equals(CountryNames)) {
            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();
            routing_number_names = routing_number.getText().toString();
            ssn_names = ssn_number.getText().toString();
            Ac_nonames = Ac_no.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();

            imageObject.put("address1", address1names);
            imageObject.put("address2", address2names);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("routing_number", routing_number_names);
            imageObject.put("ssn_last_4", ssn_names);
            imageObject.put("account_holder_name", accountholdernmaes);
            imageObject.put("account_number", Ac_nonames);


            if ("".equals(currencynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_currency));
            } else if ("".equals(ssn_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_ssn));
            } else if ("".equals(routing_number_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_routing_number));
            } else if ("".equals(Ac_nonames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_number));
            } else if ("".equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_holder_name));
            } else if ("".equals(address1names)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_address1));
            } else if ("".equals(address2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2));
            } else if ("".equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if ("".equals(statenames)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_state));
            } else if ("".equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else if ("".equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);

            }

        } else if ("Hong Kong".equals(CountryNames)) {
            currencynames = payoutaddress_currency.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            address1names = address1.getText().toString();
            address2names = address2.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            postalcodenames = postalcode.getText().toString();
            clearingcodenames = clearing_code.getText().toString();
            branch_code_names = branch_code.getText().toString();
            Ac_nonames = Ac_no.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();

            imageObject.put("address1", address1names);
            imageObject.put("address2", address2names);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("clearing_code", clearingcodenames);
            imageObject.put("account_holder_name", accountholdernmaes);
            imageObject.put("branch_code", branch_code_names);
            imageObject.put("account_number", Ac_nonames);


            if ("".equals(currencynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_currency));
            } else if ("".equals(clearingcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_clearing_code));
            } else if ("".equals(branch_code_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_branch_code));
            } else if ("".equals(Ac_nonames)) {
                commonMethods.showMessage(this, dialog,getResources().getString(R.string.please_enter_account_number) );
            } else if ("".equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_holder_name));
            } else if ("".equals(address1names)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_address1));
            } else if ("".equals(address2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2));
            } else if ("".equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if ("".equals(statenames)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_state));
            } else if ("".equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else if ("".equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);

            }

        } else if ("Japan".equals(CountryNames)) {

            currencynames = payoutaddress_currency.getText().toString();
            banknames = bank_name.getText().toString();
            branchnames = branch_name.getText().toString();
            bank_code_names = bank_code.getText().toString();
            branch_code_names = branch_code.getText().toString();
            Ac_nonames = Ac_no.getText().toString();
            accountownernames = Ac_owner_name.getText().toString();
            Phonenumbernames = ph_no.getText().toString();
            gendernames = gender.getText().toString();

            if ("Male".equals(gendernames)) {
                gendernames = "male";
            } else {
                gendernames = "female";
            }

            addresskana1names = addresskana1.getText().toString();
            accountholdernmaes = Ac_holder_name.getText().toString();
            addresskana2names = addresskana2.getText().toString();
            kanacitynames = kanacity.getText().toString();
            kanastatenames = kanastate.getText().toString();
            kanapostalcodenames = kanapostalcode.getText().toString();
            addresskanji1names = addresskanji1.getText().toString();
            addresskanji2names = addresskanji2.getText().toString();
            kanjicitynames = kanjicity.getText().toString();
            kanjistatenames = kanjistate.getText().toString();
            kanjipostalcodenames = kanjipostalcode.getText().toString();

            HashMap<String, String> imageObject = new HashMap<String, String>();

            imageObject.put("payout_method", "stripe");
            imageObject.put("currency", currencynames);
            imageObject.put("account_number", Ac_nonames);
            imageObject.put("address1", addresskana1names);
            imageObject.put("address2", addresskana2names);
            imageObject.put("city", kanacitynames);
            imageObject.put("state", kanastatenames);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", kanapostalcodenames);
            imageObject.put("bank_code", bank_code_names);
            imageObject.put("bank_name", banknames);
            imageObject.put("branch_code", branch_code_names);
            imageObject.put("branch_name", branchnames);
            imageObject.put("account_holder_name", accountholdernmaes);
            imageObject.put("account_owner_name", accountownernames);
            imageObject.put("phone_number", Phonenumbernames);
            imageObject.put("kanji_address1", addresskanji1names);
            imageObject.put("kanji_address2", addresskanji2names);
            imageObject.put("kanji_city", kanjicitynames);
            imageObject.put("kanji_state", kanjistatenames);
            imageObject.put("kanji_postal_code", kanjipostalcodenames);
            imageObject.put("gender", gendernames);


            if ("".equals(currencynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_currency));
            } else if ("".equals(banknames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_bank_name));
            } else if ("".equals(branchnames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_branch_name));
            } else if ("".equals(bank_code_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_bank_code));
            } else if ("".equals(branch_code_names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_branch_code));
            } else if ("".equals(Ac_nonames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_number));
            } else if ("".equals(accountownernames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_owner_name));
            } else if ("".equals(Phonenumbernames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_phone_number));
            } else if ("".equals(accountholdernmaes)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_holder_name));
            } else if ("".equals(gender)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_gender));
            } else if ("".equals(addresskana1names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address1_of_kana));
            } else if ("".equals(addresskana2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2_of_kana));
            } else if ("".equals(kanacitynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city_of_kana));
            } else if ("".equals(kanastatenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_state_of_kana));
            } else if ("".equals(kanapostalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postalcode_of_kana));
            } else if ("".equals(addresskanji1names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address1_of_kanji));
            } else if ("".equals(addresskanji2names)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address2_of_kanji));
            } else if ("".equals(kanjicitynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city_of_kanji));
            } else if ("".equals(kanjistatenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_state_of_kanji));
            } else if ("".equals(kanjipostalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postalcode_of_kanji));
            } else if ("".equals(legal_doc.getText().toString())) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_upload_legal_doc));
            } else {
                imageCalling(imageObject);
            }
        } else if ("Other".equals(CountryNames)) {

            companyaddressnames = companyAddress.getText().toString();
            banknames = bank_name.getText().toString();
            swiftnames = IbanSwift.getText().toString();
            Ac_no_othernames = Ac_no_other.getText().toString();
            branch_code_names = branch_code.getText().toString();
            branchnames = branch_name.getText().toString();
            accountNames = etAccountName.getText().toString();
            postalcodenames = postalcode.getText().toString();
            citynames = city.getText().toString();
            statenames = state.getText().toString();
            HashMap<String, String> imageObject = new HashMap<String, String>();

            imageObject.put("payout_method", "manual");
            imageObject.put("account_holder_name", accountNames);
            imageObject.put("token", sessionManager.getToken());
            imageObject.put("city", citynames);
            imageObject.put("state", statenames);
            imageObject.put("country", CountryNames);
            imageObject.put("postal_code", postalcodenames);
            imageObject.put("bank_name", banknames);
            imageObject.put("account_number", Ac_no_othernames);
            imageObject.put("address", companyaddressnames);
            imageObject.put("routing_number", swiftnames);
            imageObject.put("branch_code", branch_code_names);
            imageObject.put("branch_name", branchnames);


            if ("".equals(banknames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_bank_name));
            } else if ("".equals(Ac_no_othernames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_number));
            } else if ("".equals(companyaddressnames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_address_name));
            } else if ("".equals(accountNames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_account_name));
            }/* else if ("".equals(branch_code_names)) {
                commonMethods.showMessage(this, dialog, "Please enter branch code ");
            }*/ else if ("".equals(branchnames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_branch_name));
            } else if ("".equals(citynames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_city));
            } else if ("".equals(statenames)) {
                commonMethods.showMessage(this, dialog,  getResources().getString(R.string.please_enter_state));
            } else if ("".equals(postalcodenames)) {
                commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_enter_postal_code));
            } else {
                imageCalling(imageObject);
            }
        } else {
            commonMethods.showMessage(this, dialog, getResources().getString(R.string.please_choose_country));
        }
    }

    /**
     * Api calling method based on country type
     *
     * @param imageObject hash Map Datas Based on Country Type
     */
    private void imageCalling(HashMap<String, String> imageObject) {
        commonMethods.showProgressDialog(this, customDialog);
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        File file = null;
        try {
            file = new File(imagePath);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            if (!("".equals(imagePath) && "Other".equals(CountryNames))) {
                multipartBody.addFormDataPart("document", "IMG_" + timeStamp + ".jpg", RequestBody.create(MediaType.parse("image/png"), file));
            }
            /*if (imagePath.equals("") && CountryNames.equals("Other")) {

            } else {
                multipartBody.addFormDataPart("document", "IMG_" + timeStamp + ".jpg", RequestBody.create(MediaType.parse("image/png"), file));
            }*/

            for (String key : imageObject.keySet()) {
                multipartBody.addFormDataPart(key, imageObject.get(key).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody formBody = multipartBody.build();
        apiService.uploadStripe(formBody, sessionManager.getToken()).enqueue(new RequestCallback(REQ_UPDATE_PROFILE, this));

    }

    @OnClick(R.id.ivBack)
    public void title() {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_bank_details);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        commonMethods.rotateArrow(ivBack,this);
        tvTitle.setText(R.string.payouts);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sessionManager.setCountry("");
        sessionManager.setCurrency("");
    }

    /**
     * Alert dialog calling function and setting adapter based on country
     *
     * @param type For Country currency and gender
     */
    public void countryList(String type) {

        recyclerView1 = new RecyclerView(PayoutBankDetailsActivity.this);
        countryList = new ArrayList<>();
        countryListAdapter = new PayoutCountryAdapter(this, countryList, type);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(countryListAdapter);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.currency_header, null);
        TextView title = view.findViewById(R.id.header);

        if ("country".equals(type)) {
            setCountry();
            title.setText(R.string.select_country);
        } else if ("currency".equals(type)) {
            title.setText(R.string.select_currency);
            if (sessionManager.getCountry() != null) {
                String CountryName = sessionManager.getCountry();
                setCurrency(CountryName);
            }
        } else {
            setGender();
        }


        alertDialog = new android.app.AlertDialog.Builder(PayoutBankDetailsActivity.this).setCustomTitle(title).setView(recyclerView1).setCancelable(true).show();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

                String CountryName = sessionManager.getCountry();

                String Currencyname = sessionManager.getCurrency();

                String Genders = sessionManager.getGender();


                if (CountryName != null && !CountryName.equals("")) {
                    setCurrency(CountryName);
                    payoutaddress_currency.setVisibility(View.VISIBLE);
                    payoutaddress_country.setText(CountryName);
                } else {
                    payoutaddress_currency.setVisibility(View.GONE);
                    payoutaddress_country.setText("");
                }

                if (Currencyname != null) {
                    payoutaddress_currency.setText(Currencyname);
                } else {
                    payoutaddress_currency.setText("");
                }

                if (Genders != null) {
                    gender.setText(Genders);
                } else {
                    gender.setText("");
                }

                String CountryCurrencyType = sessionManager.getCountryCurrencyType();


                if (CountryCurrencyType != null && "country".equals(CountryCurrencyType)) {
                    payoutaddress_currency.setText("");
                }


                if (!"".equals(CountryName) && CountryName != null) {

                    if ("Austria".equals(CountryName) || "Belgium".equals(CountryName) || "Denmark".equals(CountryName) || "Finland".equals(CountryName) || "France".equals(CountryName) || "Germany".equals(CountryName) || "Ireland".equals(CountryName) || "Italy".equals(CountryName) || "Luxembourg".equals(CountryName) || "Norway".equals(CountryName) || "Portugal".equals(CountryName) || "Spain".equals(CountryName) || "Sweden".equals(CountryName) || "Switzerland".equals(CountryName) || "Belgium".equals(CountryName) || "Netherlands".equals(CountryName)) {

                        enableDefault();
                    } else if ("Australia".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        bsb.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);

                    } else if ("Canada".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        transit_no.setVisibility(View.VISIBLE);
                        institute_no.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);

                    } else if ("New Zealand".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        routing_number.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);

                    } else if ("Singapore".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        bank_code.setVisibility(View.VISIBLE);
                        branch_code.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);

                    } else if ("United Kingdom".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        sort_code.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);

                    } else if ("United States".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        routing_number.setVisibility(View.VISIBLE);
                        ssn_number.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);

                    } else if ("Hong Kong".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        clearing_code.setVisibility(View.VISIBLE);
                        branch_code.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);

                    } else if ("Japan".equals(CountryName)) {
                        enableDefault();
                        Iban_no.setVisibility(View.GONE);
                        address1.setVisibility(View.GONE);
                        address2.setVisibility(View.GONE);
                        city.setVisibility(View.GONE);
                        state.setVisibility(View.GONE);
                        postalcode.setVisibility(View.GONE);
                        Ac_holder_name.setVisibility(View.VISIBLE);
                        bank_name.setVisibility(View.VISIBLE);
                        bank_code.setVisibility(View.VISIBLE);
                        branch_name.setVisibility(View.VISIBLE);
                        branch_code.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.VISIBLE);
                        Ac_owner_name.setVisibility(View.VISIBLE);
                        ph_no.setVisibility(View.VISIBLE);
                        gender.setVisibility(View.VISIBLE);
                        addresskana_msg.setVisibility(View.VISIBLE);
                        addresskana_linear.setVisibility(View.VISIBLE);
                        addresskanji_msg.setVisibility(View.VISIBLE);
                        addresskanji_linear.setVisibility(View.VISIBLE);

                    } else if ("Other".equals(CountryName)) {
                        enableDefault();
                        address1.setVisibility(View.GONE);
                        payoutaddress_currency.setVisibility(View.GONE);
                        Ac_holder_name.setVisibility(View.GONE);
                        address2.setVisibility(View.GONE);
                        city.setVisibility(View.VISIBLE);
                        state.setVisibility(View.VISIBLE);
                        Iban_no.setVisibility(View.GONE);
                        postalcode.setVisibility(View.VISIBLE);
                        Ac_no.setVisibility(View.GONE);
                        Ac_no_other.setVisibility(View.VISIBLE);
                        etAccountName.setVisibility(View.VISIBLE);
                        companyAddress.setVisibility(View.VISIBLE);
                        branch_code.setVisibility(View.GONE);
                        branch_name.setVisibility(View.VISIBLE);
                        bank_name.setVisibility(View.VISIBLE);
                        IbanSwift.setVisibility(View.VISIBLE);


                    }
                }
            }
        });

    }

    /**
     * Image selecting function for image pick
     */
    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from_library), getResources().getString(R.string.Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result = Utility.checkPermission(PayoutBankDetailsActivity.this);

                if (getResources().getString(R.string.take_photo).equals(items[item])) {
                    if (isPermission(Constants.PERMISSIONS_PHOTO)) cameraIntent();

                } else if (getResources().getString(R.string.choose_from_library).equals(items[item])) {
                    if (isPermission(Constants.PERMISSIONS_PHOTO)) galleryIntent();

                } else if (getResources().getString(R.string.Cancel).equals(items[item])) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Image pick from gallery
     */
    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_FILE);
    }

    /**
     * Image pick from camera
     */
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * onActivity result for picking image
     *
     * @param requestCode Request code For Image
     * @param resultCode  Result For While select From gallery or camera
     * @param data        Intent data from payout
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) onCaptureImageResult(data);
        }
    }

    /**
     * Getting image uri from bitmap
     *
     * @param inContext Activity
     * @param inImage   Image In BitMap
     * @return path
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Select from camera results
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");


        Uri selectedImage = getImageUri(getApplicationContext(), thumbnail);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        imagePath = picturePath;
        new ImageCompressAsyncTask(docType, PayoutBankDetailsActivity.this, imagePath, this, 0).execute();

        legal_doc.setText(System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select Images from gallery results
     *
     * @param data
     */
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        try {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);
            File f = new File(picturePath);
            String imageName = f.getName();
            cursor.close();
            legal_doc.setText(imageName);

            imagePath = picturePath;

            new ImageCompressAsyncTask(docType, PayoutBankDetailsActivity.this, imagePath, this, 0).execute();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            InputStream inputStream = getContentResolver().openInputStream(data.getData());

            thumbnail = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to set the country values to the adapter
     */
    private void setCountry() {

        for (int i = 0; i < countryname.length; i++) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(i));
            listdata.setCountryname(countryname[i]);
            countryList.add(listdata);
        }
        countryListAdapter.notifyDataSetChanged();
    }

    /**
     * Method to set the gender values to the adapter
     */
    private void setGender() {
        for (int i = 0; i < genders.length; i++) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(i));
            listdata.setCountryname(genders[i]);
            countryList.add(listdata);
        }
        countryListAdapter.notifyDataSetChanged();
    }

    /**
     * Method to set the currency values to the adapter
     *
     * @param CountryName setting Currency Name
     */
    private void setCurrency(String CountryName) {

        if ("Austria".equals(CountryName) || "Belgium".equals(CountryName) || "Denmark".equals(CountryName) || "Finland".equals(CountryName) || "France".equals(CountryName) || "Germany".equals(CountryName) || "Ireland".equals(CountryName) || "Italy".equals(CountryName) || "Luxembourg".equals(CountryName) || "Norway".equals(CountryName) || "Portugal".equals(CountryName) || "Spain".equals(CountryName) || "Sweden".equals(CountryName) || "Switzerland".equals(CountryName) || "Belgium".equals(CountryName) || "Netherlands".equals(CountryName) || "United Kingdom".equals(CountryName)) {
            for (int i = 0; i < currencyname.length; i++) {
                CountryModels listdata = new CountryModels();
                listdata.setCountryid(Integer.toString(i));
                listdata.setCountryname(currencyname[i]);
                countryList.add(listdata);
            }

            countryListAdapter.notifyDataSetChanged();
        } else if ("United States".equals(CountryName)) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(0));
            listdata.setCountryname("USD");
            countryList.add(listdata);
            countryListAdapter.notifyDataSetChanged();
        } else if ("Australia".equals(CountryName)) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(0));
            listdata.setCountryname("AUD");
            countryList.add(listdata);
            countryListAdapter.notifyDataSetChanged();
        } else if ("Canada".equals(CountryName)) {
            for (int i = 0; i < currencynamecanada.length; i++) {
                CountryModels listdata = new CountryModels();
                listdata.setCountryid(Integer.toString(i));
                listdata.setCountryname(currencynamecanada[i]);
                countryList.add(listdata);
            }

            countryListAdapter.notifyDataSetChanged();
        } else if ("New Zealand".equals(CountryName)) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(0));
            listdata.setCountryname("NZD");
            countryList.add(listdata);
            countryListAdapter.notifyDataSetChanged();
        } else if ("Singapore".equals(CountryName)) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(0));
            listdata.setCountryname("SGD");
            countryList.add(listdata);
            countryListAdapter.notifyDataSetChanged();
        } else if ("United States".equals(CountryName)) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(0));
            listdata.setCountryname("USD");
            countryList.add(listdata);
            countryListAdapter.notifyDataSetChanged();
        } else if ("Hong Kong".equals(CountryName)) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(0));
            listdata.setCountryname("HKD");
            countryList.add(listdata);
            countryListAdapter.notifyDataSetChanged();
        } else if ("Japan".equals(CountryName)) {
            CountryModels listdata = new CountryModels();
            listdata.setCountryid(Integer.toString(0));
            listdata.setCountryname("JPY");
            countryList.add(listdata);
            countryListAdapter.notifyDataSetChanged();
        }

    }


    public String getStringImage(Bitmap bmp) {
        String encodedImage = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } catch (Exception e) {

        }
        return encodedImage;
    }

    /**
     * Enableing  Default Views
     */
    private void enableDefault() {
        Iban_no.setVisibility(View.VISIBLE);
        Ac_holder_name.setVisibility(View.VISIBLE);
        address1.setVisibility(View.VISIBLE);
        address2.setVisibility(View.VISIBLE);
        city.setVisibility(View.VISIBLE);
        state.setVisibility(View.VISIBLE);
        postalcode.setVisibility(View.VISIBLE);

        companyAddress.setVisibility(View.GONE);

        IbanSwift.setVisibility(View.GONE);
        etAccountName.setVisibility(View.GONE);
        Ac_no_other.setVisibility(View.GONE);

        bsb.setVisibility(View.GONE);
        Ac_no.setVisibility(View.GONE);
        transit_no.setVisibility(View.GONE);
        institute_no.setVisibility(View.GONE);
        routing_number.setVisibility(View.GONE);
        ssn_number.setVisibility(View.GONE);
        clearing_code.setVisibility(View.GONE);
        bank_code.setVisibility(View.GONE);
        branch_code.setVisibility(View.GONE);
        sort_code.setVisibility(View.GONE);
        bank_name.setVisibility(View.GONE);
        branch_name.setVisibility(ViewPager.GONE);
        Ac_owner_name.setVisibility(View.GONE);
        ph_no.setVisibility(View.GONE);
        addresskana_msg.setVisibility(View.GONE);
        addresskanji_msg.setVisibility(View.GONE);
        addresskana_linear.setVisibility(View.GONE);
        addresskanji_linear.setVisibility(View.GONE);
        gender.setVisibility(View.GONE);
    }

    /**
     * Success Response For API
     *
     * @param jsonResp JsonResp FroM API
     * @param data     Request Data
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        StripeModel weeklymodel;
        weeklymodel = gson.fromJson(jsonResp.getStrResponse(), StripeModel.class);

        commonMethods.hideProgressDialog();
        if (jsonResp.isSuccess()) {
            //commonMethods.showMessage(this, dialog, weeklymodel.getStatusMessage());
            sessionManager.setCountry("");
            sessionManager.setCurrency("");

            finish();
        } else {
            commonMethods.showMessage(this, dialog, weeklymodel.getStatusMessage());

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
    }

    /**
     * Getting the Compressed Image
     *
     * @param filePath    Image path
     * @param requestBody Image Data
     */
    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        requestbody = requestBody;
        imagePath = filePath;
    }

    /**
     * Enable App Permission
     */

    private boolean isPermission(String[] permission) {

        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(PayoutBankDetailsActivity.this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(PayoutBankDetailsActivity.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(PayoutBankDetailsActivity.this, permission, 150);
            }
            return false;
        } else {
            return true;
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
     * Get Permission Result
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            isPermission(dsf);
        } else {
            selectImage();
        }
    }
}