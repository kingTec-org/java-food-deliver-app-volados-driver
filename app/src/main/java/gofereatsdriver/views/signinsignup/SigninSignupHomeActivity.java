package gofereatsdriver.views.signinsignup;

/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage views.signinsignup
 * @category SigninSignupHomeActivity
 * @author Trioangle Product Team
 * @version 1.0
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;

import static gofereatsdriver.views.main.fragments.AccountFragment.langclick;


/**************************************************************
 SigninSignupHomeActivity
 Its used to show the signin and register screen to call the function
 ****************************************************************/
public class SigninSignupHomeActivity extends AppCompatActivity {

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/

    public @InjectView(R.id.tvSignin)
    TextView tvSignin;
    public @InjectView(R.id.tvSignup)
    TextView tvSignup;
    public @InjectView(R.id.tvRiderApp)
    TextView tvRiderApp;
    public @InjectView(R.id.languagetext)
    TextView LanguageText;
    public @InjectView(R.id.language)
    TextView language;
    public RecyclerView languageView;
    public LanguageAdapter LanguageAdapter;
    public static android.app.AlertDialog alertDialogStores;
    public List<CurrencyModel> languagelist;

    public @Inject
    SessionManager sessionManager;

    /**
     * Driver Sign in
     */
    @OnClick(R.id.tvSignin)
    public void signin() {
        Intent register = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(register);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    /**
     * Driver Sign up
     */
    @OnClick(R.id.tvSignup)
    public void signup() {
        Intent register = new Intent(getApplicationContext(), Register.class);
        startActivity(register);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    /**
     * Check Rider APP
     */
    @OnClick(R.id.tvRiderApp)
    public void riderLink() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        PackageManager managerclock = getPackageManager();
        i = managerclock.getLaunchIntentForPackage(getResources().getString(R.string.package_name_eater));
        if (i == null) {
            // Open play store package link
            i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getResources().getString(R.string.package_name_eater)));
            //Toast.makeText(this, "No Application Name", Toast.LENGTH_LONG).show();
        } else {
            // Open rider application
            i.addCategory(Intent.CATEGORY_LAUNCHER);

        }
        startActivity(i);
    }
    /**
     * Change Language
     * */
    @OnClick(R.id.languagetext)
    public void languagetext() {
        languagelist();
        language.setClickable(false);
        LanguageText.setClickable(false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup_home);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);
        String lang = sessionManager.getLanguage();
        language.setText(lang);


        LanguageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languagelist(); // Show curtency list
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }
    public void languagelist() {

        languageView = new RecyclerView(this);
        languagelist = new ArrayList<>();
        loadlang();

        LanguageAdapter = new LanguageAdapter(this, languagelist);
        languageView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        languageView.setAdapter(LanguageAdapter);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.currency_header, null);
        TextView T = (TextView) view.findViewById(R.id.header);
        T.setText(getString(R.string.selectlanguage));
        alertDialogStores = new android.app.AlertDialog.Builder(SigninSignupHomeActivity.this)
                .setCustomTitle(view)
                .setView(languageView)
                .setCancelable(true)
                .show();
        language.setClickable(true);
        LanguageText.setClickable(true);

        alertDialogStores.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (langclick) {
                    langclick = false;
                    String langocde = sessionManager.getLanguageCode();
                    String lang = sessionManager.getLanguage();
                    language.setText(lang);
                    //new UpdateLanguage().execute();
                    setLocale(langocde);
                    recreate();
                    Intent intent = new Intent(SigninSignupHomeActivity.this, SigninSignupHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                LanguageText.setClickable(true);

            }
        });
    }

    public void loadlang() {

        String[] languages;
        String[] langCode;
        languages = getResources().getStringArray(R.array.language);
        langCode = getResources().getStringArray(R.array.languageCode);
        for (int i = 0; i < languages.length; i++) {
            CurrencyModel listdata = new CurrencyModel();
            listdata.setCurrencyName(languages[i]);
            listdata.setCurrencySymbol(langCode[i]);
            languagelist.add(listdata);

        }
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }


    public void setLocale() {
        String lang = sessionManager.getLanguage();
        if (!lang.equals("")) {
            String langC = sessionManager.getLanguageCode();
            Locale locale = new Locale(langC);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            SigninSignupHomeActivity.this.getResources().updateConfiguration(config, SigninSignupHomeActivity.this.getResources().getDisplayMetrics());
        } else {
            sessionManager.setLanguage("English");
            sessionManager.setLanguageCode("en");
            setLocale();
            recreate();
            Intent intent = new Intent(SigninSignupHomeActivity.this, SigninSignupHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }

}