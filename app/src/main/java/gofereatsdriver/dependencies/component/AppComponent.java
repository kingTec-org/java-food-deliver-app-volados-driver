package gofereatsdriver.dependencies.component;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage dependencies.component
 * @category AppComponent
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.widget.Adapter;

import javax.inject.Singleton;

import dagger.Component;
import gofereatsdriver.adapters.main.DailyEarnListAdapter;
import gofereatsdriver.adapters.main.PayAdapter;
import gofereatsdriver.adapters.main.TripEarnListAdapter;
import gofereatsdriver.adapters.main.TripsAdapter;
import gofereatsdriver.backgroundtask.ImageCompressAsyncTask;
import gofereatsdriver.configs.RunTimePermission;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.dependencies.module.AppContainerModule;
import gofereatsdriver.dependencies.module.ApplicationModule;
import gofereatsdriver.dependencies.module.NetworkModule;
import gofereatsdriver.payoutbank.PayoutCountryAdapter;
import gofereatsdriver.payoutbank.PayoutEmailListActivity;
import gofereatsdriver.payoutbank.PayoutEmailListAdapter;
import gofereatsdriver.pushnotification.MyFirebaseInstanceIDService;
import gofereatsdriver.pushnotification.MyFirebaseMessagingService;
import gofereatsdriver.service.GPS_Service;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.DateTimeUtility;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.utils.WebServiceUtils;
import gofereatsdriver.views.main.MainActivity;
import gofereatsdriver.views.main.cancel.CancelActivity;
import gofereatsdriver.views.main.fragments.AccountFragment;
import gofereatsdriver.views.main.fragments.EarningFragment;
import gofereatsdriver.views.main.fragments.HomeFragment;
import gofereatsdriver.views.main.fragments.RatingFragment;
import gofereatsdriver.views.main.home.RequestAcceptActivity;
import gofereatsdriver.views.main.home.RequestReceiveActivity;
import gofereatsdriver.views.main.payment.AddPayment;
import gofereatsdriver.views.main.payment.PaymentPage;
import gofereatsdriver.views.main.payment.PayoutBankDetailsActivity;
import gofereatsdriver.views.main.paymentstatement.DailyEarningDetails;
import gofereatsdriver.views.main.paymentstatement.PayStatementDetails;
import gofereatsdriver.views.main.paymentstatement.PaymentStatementActivity;
import gofereatsdriver.views.main.paytoadmin.AddCardActivity;
import gofereatsdriver.views.main.paytoadmin.PayToAdminActivity;
import gofereatsdriver.views.main.paytoadmin.PaymentActivity;
import gofereatsdriver.views.main.pickuporderdetails.ContactActivity;
import gofereatsdriver.views.main.pickuporderdetails.DeliveryEnrouteActivity;
import gofereatsdriver.views.main.pickuporderdetails.PickupOrderActivity;
import gofereatsdriver.views.main.profile.DriverProfile;
import gofereatsdriver.views.main.profile.VehicleInformation;
import gofereatsdriver.views.main.rating.RatingStep1Activity;
import gofereatsdriver.views.main.rating.RatingStep2Activity;
import gofereatsdriver.views.main.tripdetails.Past;
import gofereatsdriver.views.main.tripdetails.TripDetails;
import gofereatsdriver.views.main.tripdetails.Upcoming;
import gofereatsdriver.views.main.tripdetails.YourTrips;
import gofereatsdriver.views.signinsignup.DocHomeActivity;
import gofereatsdriver.views.signinsignup.DocumentUploadActivity;
import gofereatsdriver.views.signinsignup.LanguageAdapter;
import gofereatsdriver.views.signinsignup.MobileActivity;
import gofereatsdriver.views.signinsignup.Register;
import gofereatsdriver.views.signinsignup.RegisterCarDetailsActivity;
import gofereatsdriver.views.signinsignup.RegisterOTPActivity;
import gofereatsdriver.views.signinsignup.ResetPassword;
import gofereatsdriver.views.signinsignup.SigninActivity;
import gofereatsdriver.views.signinsignup.SigninSignupHomeActivity;
import gofereatsdriver.views.splash.SplashActivity;


/*****************************************************************
 App Component
 ****************************************************************/
@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {
    // SERVICE
    void inject(GPS_Service gps_service);

    void inject(TripEarnListAdapter tripEarnListAdapter);

    void inject(DailyEarnListAdapter dailyEarnListAdapter);

    // ACTIVITY
    void inject(SigninSignupHomeActivity signupHomeActivity);

    void inject(SplashActivity splashActivity);

    void inject(PayAdapter payAdapter);


    void inject(PayoutEmailListAdapter payoutEmailListAdapter);


    void inject(SigninActivity signinActivity);

    void inject(Register register);

    void inject(MobileActivity mobileActivity);

    void inject(RegisterCarDetailsActivity registerCarDetailsActivity);

    void inject(ResetPassword resetPassword);

    void inject(RegisterOTPActivity registerOTPActivity);

    void inject(DocHomeActivity docHomeActivity);

    void inject(PayoutCountryAdapter payoutCountryAdapter);

    void inject(MainActivity mainActivity);

    void inject(DriverProfile driverProfile);

    void inject(VehicleInformation vehicleInformation);

    void inject(AddPayment addPayment);

    void inject(PaymentPage paymentPage);

    void inject(YourTrips yourTrips);

    void inject(RequestReceiveActivity receiveActivity);

    void inject(RequestAcceptActivity requestAcceptActivity);

    void inject(TripDetails tripDetails);

    void inject(PaymentStatementActivity paymentStatementActivity);

    void inject(PayStatementDetails payStatementDetails);

    void inject(DailyEarningDetails dailyEarningDetails);

    void inject(PickupOrderActivity pickupOrderActivity);

    void inject(DeliveryEnrouteActivity deliveryEnrouteActivity);

    void inject(ContactActivity contactActivity);

    void inject(PayoutBankDetailsActivity payoutBankDetailsActivity);

    void inject(PayoutEmailListActivity payoutEmailListActivity);


    void inject(RatingStep1Activity ratingStep1Activity);

    void inject(RatingStep2Activity ratingStep2Activity);

    void inject(CancelActivity cancelActivity);

    void inject(PayToAdminActivity payToAdminActivity);

    void inject(PaymentActivity paymentActivity);

    void inject(AddCardActivity addCardActivity);


    // Fragments
    void inject(HomeFragment homeFragment);

    void inject(AccountFragment accountFragment);

    void inject(RatingFragment ratingFragment);

    void inject(EarningFragment earningFragment);

    void inject(Past past);

    void inject(Upcoming upcoming);

    void inject(DocumentUploadActivity documentUploadActivity);


    // Utilities
    void inject(RunTimePermission runTimePermission);

    void inject(SessionManager sessionManager);

    void inject(ImageUtils imageUtils);

    void inject(CommonMethods commonMethods);


    void inject(RequestCallback requestCallback);

    void inject(DateTimeUtility dateTimeUtility);

    void inject(WebServiceUtils webServiceUtils);

    // Adapters
    void inject(TripsAdapter tripsAdapter);

   /* void inject(ChatConversationListAdapter chatConversationListAdapter);*/

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);


    // AsyncTask
    void inject(ImageCompressAsyncTask imageCompressAsyncTask);
    void inject(LanguageAdapter languageAdapter);

}
