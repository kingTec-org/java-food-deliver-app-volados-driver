package gofereatsdriver.views.splash;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage view.main
 * @category SplashActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.Locale;

import javax.inject.Inject;

import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.views.main.MainActivity;
import gofereatsdriver.views.signinsignup.SigninSignupHomeActivity;

/*****************************************************************
 Application splash screen
 ****************************************************************/
public class SplashActivity extends AppCompatActivity {

    public @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppController.getAppComponent().inject(this);
        sessionManager.setType("2");
        //sessionManager.setCurrencySymbol("$");
        getIntentValues();
        //checkForUpdates();
        setLocale();
    }


    private void getIntentValues() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goIntent();
            }
        }, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        //checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

    public void goIntent() {
        if (TextUtils.isEmpty(sessionManager.getToken()) && sessionManager.getDriverStatus() == -1) {
            try {
                Intent x = new Intent(getApplicationContext(), SigninSignupHomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.cb_fade_in, R.anim.cb_face_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        } else {
            try{
            Intent x = new Intent(getApplicationContext(), MainActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.cb_fade_in, R.anim.cb_face_out).toBundle();
            startActivity(x, bndlanimation);
            finish();}
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    public void setLocale() {
        String lang = sessionManager.getLanguage();

        if (!lang.equals("")) {
            String langC = sessionManager.getLanguageCode();
            Locale locale = new Locale(langC);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            SplashActivity.this.getResources().updateConfiguration(config, SplashActivity.this.getResources().getDisplayMetrics());
        } else {
            sessionManager.setLanguage("English");
            sessionManager.setLanguageCode("en");
        }


    }
}
