package gofereatsdriver.views.main.fragments;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage view.main.fragments
 * @category AccountFragment
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.driverprofile.DriverModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ActivityListener;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.payoutbank.PayoutEmailListActivity;
import gofereatsdriver.service.GPS_Service;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;
import gofereatsdriver.views.main.paytoadmin.PayToAdminActivity;
import gofereatsdriver.views.main.profile.DriverProfile;
import gofereatsdriver.views.main.profile.VehicleInformation;
import gofereatsdriver.views.signinsignup.CurrencyModel;
import gofereatsdriver.views.signinsignup.DocHomeActivity;
import gofereatsdriver.views.signinsignup.LanguageAdapter;
import gofereatsdriver.views.signinsignup.SigninActivity;

import static gofereatsdriver.utils.Enums.REQ_GET_DRIVER_PROFILE;
import static gofereatsdriver.utils.Enums.REQ_LANGUAGE;
import static gofereatsdriver.utils.Enums.REQ_LOGOUT;

/*************************************************************
 AccountFragment
 Driver Profile and Payout Detail  Page
 *************************************************************** */

public class AccountFragment extends Fragment implements ServiceListener {

    public DriverModel driverModel;
    public @InjectView(R.id.drlayout)
    RelativeLayout drlayout;
    public @InjectView(R.id.vhlayout)
    RelativeLayout vhlayout;
    public @InjectView(R.id.dclayout)
    RelativeLayout dclayout;
    public @InjectView(R.id.paylayout)
    RelativeLayout paylayout;
    public @InjectView(R.id.ivDriverProfile)
    CircleImageView ivDriverProfile;
    public @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    public @InjectView(R.id.progressBarciv)
    ProgressBar progressBarciv;
    public @InjectView(R.id.tvDrivername)
    TextView tvDrivername;
    public @InjectView(R.id.tvCarname)
    TextView tvCarname;
    public @InjectView(R.id.tvCarnumber)
    TextView tvCarnumber;
    public @InjectView(R.id.rltPayTo)
    RelativeLayout rltPayTo;
    public @InjectView(R.id.rltSignout)
    RelativeLayout rltSignout;
    public @InjectView(R.id.civVehicle)
    CircleImageView civVehicle;
    public @InjectView(R.id.arrarowtwo)
    TextView arrarowtwo;
    public @InjectView(R.id.arrarowthree)
    TextView arrarowthree;
    public @InjectView(R.id.tvArrow)
    TextView tvArrow;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @InjectView(R.id.currency_code)
    TextView currencyCode;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    private View view;
    private ActivityListener listener;
    private MainActivity mActivity;
    private AlertDialog dialog;
    public static Boolean langclick = false;
    public static android.app.AlertDialog alertDialogStores1;
    public static android.app.AlertDialog alertDialogStores2;
    public RecyclerView languageView;
    public ArrayList<CurrencyModel> currencyList;
    public String langocde;
    public List<CurrencyModel> languagelist;
    public gofereatsdriver.views.signinsignup.LanguageAdapter LanguageAdapter;
    public @InjectView(R.id.languagelayout)
    RelativeLayout languagelayout;
    public @InjectView(R.id.language)
    TextView language;
    public String Language;
    private Context mContext;

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    /**
     * Get Driver Profile
     */
    @OnClick(R.id.drlayout)
    public void driverProfile() {
        if (driverModel != null) {
            Intent main = new Intent(mActivity, DriverProfile.class);
            main.putExtra("name", driverModel.getDriverDetailsModel().getName());
            main.putExtra("email", driverModel.getDriverDetailsModel().getEmail());
            main.putExtra("mobile", driverModel.getDriverDetailsModel().getMobilenumber());
            main.putExtra("countrycode", driverModel.getDriverDetailsModel().getCountrycode());
            main.putExtra("street", driverModel.getDriverDetailsModel().getStreet());
            main.putExtra("area", driverModel.getDriverDetailsModel().getArea());
            main.putExtra("city", driverModel.getDriverDetailsModel().getCity());
            main.putExtra("state", driverModel.getDriverDetailsModel().getState());
            main.putExtra("postalcode", driverModel.getDriverDetailsModel().getPostalcode());
            main.putExtra("country", driverModel.getDriverDetailsModel().getCountry());
            main.putExtra("profile", driverModel.getDriverDetailsModel().getProfileimage());
            startActivity(main);
        }
    }

    /**
     * Get vehicle details
     */
    @OnClick(R.id.vhlayout)
    public void vehicleLayout() {
        if (driverModel != null) {
            Intent main = new Intent(mActivity, VehicleInformation.class);
            main.putExtra("vehiclename", driverModel.getDriverDetailsModel().getVehiclename());
            main.putExtra("vehiclenum", driverModel.getDriverDetailsModel().getVehiclenumber());
            main.putExtra("vehicletype", driverModel.getDriverDetailsModel().getVehicletypename());
            main.putExtra("vehicleimage", driverModel.getDriverDetailsModel().getVehicleImage());
            startActivity(main);
        }
    }
    /**
     * Language List
     */
    @OnClick(R.id.languagelayout)
    public void language() {
        languagelist();
        languagelayout.setClickable(false);
    }

    /**
     * Get documnets details
     */
    @OnClick(R.id.dclayout)
    public void docAct() {
        Intent main = new Intent(mActivity, DocHomeActivity.class);
        main.putExtra("check", "view");
        startActivity(main);
    }

    /**
     * Get Payout details
     */
    @OnClick(R.id.paylayout)
    public void paymentpage() {
        Intent main = new Intent(mActivity, PayoutEmailListActivity.class);
        startActivity(main);
    }

    /**
     * SignOut
     */
    @OnClick(R.id.rltSignout)
    public void signOut() {
        showAlert();
    }

    /**
     * Getting PayToadminActivity
     */
    @OnClick(R.id.rltPayTo)
    public void payto() {
        Intent payto = new Intent(mActivity, PayToAdminActivity.class);
        startActivity(payto);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("AccountFragment must implement ActivityListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        AppController.getAppComponent().inject(this);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            dialog = commonMethods.getAlertDialog(mActivity);

        }
        ButterKnife.inject(this, view);
        commonMethods.rotateArrowText(arrarowtwo,getContext());
        commonMethods.rotateArrowText(arrarowthree,getContext());
        commonMethods.rotateArrowText(tvArrow,getContext());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        if (listener == null) return;

        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (MainActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        if (listener != null) listener = null;
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        Language = sessionManager.getLanguage();
        if (Language != null) {
            language.setText(Language);
        } else {
            language.setText("English");
        }
        System.out.println("Context Value"+getContext());
        getDriverProfile();
    }

    /**
     * Alert to Logout
     */
    public void showAlert() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mActivity);
        if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            builder.setMessage(R.string.logout).setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(getContext().getResources().getString(R.string.signout), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logOut();
                }
            }).show();
        }
        else
        {
            builder.setMessage(R.string.logout).setPositiveButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setNegativeButton(getContext().getResources().getString(R.string.signout), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    logOut();
                }
            }).show();

        }

    }

    /**
     * Logout API
     */
    public void logOut() {
        commonMethods.showProgressDialog(mActivity, customDialog);
        apiService.logOut(sessionManager.getToken()).enqueue(new RequestCallback(REQ_LOGOUT, this));
    }

    /**
     * Getting Driver profile API
     */
    private void getDriverProfile() {
        commonMethods.showProgressDialog(mActivity, customDialog);
        apiService.getDriverProfile(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_DRIVER_PROFILE, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(mActivity, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_GET_DRIVER_PROFILE:
                if (jsonResp.isSuccess()) {
                    onSuccessGetDriverProfile(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    sessionManager.setDriverStatus(3);
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_LOGOUT:
                if (jsonResp.isSuccess()) {
                    onSuccessLogOut();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_LANGUAGE:
                if (jsonResp.isSuccess()) {
                    setLocale(langocde);
                    getActivity().recreate();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(getActivity(), dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * SuccessFully Log out
     */
    private void onSuccessLogOut() {
        String lang = sessionManager.getLanguage();
        String langCode = sessionManager.getLanguageCode();




        stopGPSService();
        sessionManager.clearToken();
        sessionManager.clearAll();
        sessionManager.setLanguage(lang);
        sessionManager.setLanguageCode(langCode);
        mActivity.finishAffinity();
        Intent intent = new Intent(mActivity, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        mActivity.finish();

    }
    /**
     * Language List
     */
    public void languagelist() {

        languageView = new RecyclerView(mActivity);
        languagelist = new ArrayList<>();
        loadlang();

        LanguageAdapter = new LanguageAdapter(mActivity, languagelist);
        languageView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        languageView.setAdapter(LanguageAdapter);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.currency_header, null);
        TextView T = (TextView) view.findViewById(R.id.header);
        T.setText(getString(R.string.selectlanguage));
        alertDialogStores2 = new android.app.AlertDialog.Builder(mActivity).setCustomTitle(view).setView(languageView).setCancelable(true).show();
        languagelayout.setClickable(true);

        alertDialogStores2.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (langclick) {
                    langclick = false;
                    langocde = sessionManager.getLanguageCode();
                    String lang = sessionManager.getLanguage();
                    language.setText(lang);
                    updateLanguage();
                }
                languagelayout.setClickable(true);

            }
        });
    }

    public void loadlang() {

        String[] languages;
        String[] langCode;
        languages = mActivity.getResources().getStringArray(R.array.language);
        langCode = mActivity.getResources().getStringArray(R.array.languageCode);
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
       /* if (Build.VERSION.SDK_INT >= 17) {
            conf.setLayoutDirection(myLocale);
        }*/
        res.updateConfiguration(conf, dm);
    }
    /**
     * Update Language
     */
    public void updateLanguage() {
        commonMethods.showProgressDialog(mActivity, customDialog);
        apiService.language(sessionManager.getType(),sessionManager.getLanguageCode(), sessionManager.getToken()).enqueue(new RequestCallback(REQ_LANGUAGE, this));
    }


    /**
     * Driver Details
     *
     * @param jsonResp gets the driver details
     */
    private void onSuccessGetDriverProfile(JsonResponse jsonResp) {
        driverModel = gson.fromJson(jsonResp.getStrResponse(), DriverModel.class);
        if (driverModel != null) {
            setDriverDetails();
        }
    }

    /**
     * Setting Driver details into views
     */
    private void setDriverDetails() {
        if(mActivity!=null&&!mActivity.isDestroyed()) {
            try {
                Glide.with(mActivity).load(driverModel.getDriverDetailsModel().getProfileimage()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(ivDriverProfile);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            Glide.with(mActivity).load(driverModel.getDriverDetailsModel().getVehicleImage()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    progressBarciv.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBarciv.setVisibility(View.GONE);
                    return false;
                }
            }).into(civVehicle);
        }

        tvDrivername.setText(driverModel.getDriverDetailsModel().getName());
        tvCarname.setText(driverModel.getDriverDetailsModel().getVehiclename());
        tvCarnumber.setText(driverModel.getDriverDetailsModel().getVehiclenumber());
        sessionManager.setOweAmount(driverModel.getDriverDetailsModel().getOweAmount());
        sessionManager.setDoc1(driverModel.getDriverDetailsModel().getDoc1Driverlicenceback());
        sessionManager.setDoc2(driverModel.getDriverDetailsModel().getDoc2driverlicencefront());
        sessionManager.setDoc3(driverModel.getDriverDetailsModel().getDoc3Driverinsurance());
        sessionManager.setDoc4(driverModel.getDriverDetailsModel().getDoc4DriverRegisterationcertificate());
        sessionManager.setDoc5(driverModel.getDriverDetailsModel().getDoc5Drivermotorcertiticate());
    }

    /**
     * Stop service based on driver status
     **/
    public void stopGPSService() {
        if (isMyServiceRunning(GPS_Service.class)) {
            Intent GPSservice = new Intent(mActivity, GPS_Service.class);
            mActivity.stopService(GPSservice);
        }
    }

    /**
     * Check service is running or not
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}