package gofereatsdriver.views.main.fragments;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage view.main.fragments
 * @category EarningFragment
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.earnings.EarningDatelistModel;
import gofereatsdriver.datamodels.earnings.EarningListModel;
import gofereatsdriver.datamodels.earnings.EarningsResultModel;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ActivityListener;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.BarView;
import gofereatsdriver.views.customize.CustomDialog;
import gofereatsdriver.views.main.MainActivity;
import gofereatsdriver.views.main.paymentstatement.PayStatementDetails;
import gofereatsdriver.views.main.paymentstatement.PaymentStatementActivity;
import gofereatsdriver.views.main.tripdetails.YourTrips;

/*************************************************************
 EarningFragment
 Driver Earnings Detail  Page
 *************************************************************** */
public class EarningFragment extends Fragment implements ServiceListener {

    public @InjectView(R.id.thlayout)
    RelativeLayout thlayout;
    public @InjectView(R.id.rltPaystatement)
    RelativeLayout pslayout;
    public @InjectView(R.id.bar_view)
    BarView barView;
    public @InjectView(R.id.tvValuebottom)
    CustomTextView tvValuebottom;
    public @InjectView(R.id.tvValuetop)
    CustomTextView tvValuetop;
    public @InjectView(R.id.tvValuemid)
    CustomTextView tvValuemid;
    public @InjectView(R.id.tvNextsearch)
    CustomTextView tvNextsearch;
    public @InjectView(R.id.tvBeforesearch)
    CustomTextView tvBeforesearch;
    public @InjectView(R.id.tvShowdate)
    CustomTextView tvShowdate;
    public @InjectView(R.id.tvWeeklyfare)
    CustomTextView tvWeeklyfare;
    public @InjectView(R.id.tvLasttripamount)
    CustomTextView tvLasttripamount;
    public @InjectView(R.id.tvMostresentpayout)
    CustomTextView tvMostresentpayout;
    public @InjectView(R.id.rltScrollview)
    RelativeLayout rltScrollview;
    public @InjectView(R.id.tvEmpty)
    CustomTextView tvEmpty;
    public @InjectView(R.id.tvTotalPayout)
    CustomTextView tvTotalPayout;

    public @InjectView(R.id.triparrow)
    CustomTextView triparrow;
    public @InjectView(R.id.payarrow)
    CustomTextView payarrow;
    public ArrayList<Integer> farelist = new ArrayList<Integer>();
    public String day[], dates[];
    public String[] days = new String[7];
    public double fare[];
    public double max;
    public Calendar now;
    public String current_date;
    public String total_week_amount = "";
    public int recent_payout = 0, last_trip = 0;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public EarningListModel earningListModel;
    public ArrayList<EarningDatelistModel> earningDatelistModels;
    private View view;
    private ActivityListener listener;
    private MainActivity mActivity;
    private AlertDialog dialog;
    private String newDate = "";

    public static EarningFragment newInstance() {
        EarningFragment fragment = new EarningFragment();
        return fragment;
    }

    /**
     * Your trips details
     */
    @OnClick(R.id.thlayout)
    public void yourTrips() {
        Intent main = new Intent(mActivity, YourTrips.class);
        startActivity(main);
    }

    public void paystatement() {
        Intent main = new Intent(mActivity, YourTrips.class);
        startActivity(main);
    }

    /**
     * Get your Payment Statement details
     */
    @OnClick(R.id.rltPaystatement)
    public void payment() {
        Intent main = new Intent(mActivity, PaymentStatementActivity.class);
        startActivity(main);
    }

    /**
     * get Earnings next list
     */
    @OnClick(R.id.tvNextsearch)
    public void nextsearch() {
        nextlist();
    }

    /**
     * get Earnings before List
     */
    @OnClick(R.id.tvBeforesearch)
    public void beforesearch() {
        previouslist();
    }

    @OnClick(R.id.tvWeeklyfare)
    public void getWeeklyFare() {
        if (!TextUtils.isEmpty(newDate)) {
            Intent paystatement_details = new Intent(mActivity, PayStatementDetails.class);
            paystatement_details.putExtra("weekDate", newDate);
            startActivity(paystatement_details);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Earning must implement ActivityListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        AppController.getAppComponent().inject(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_earnings, container, false);
            dialog = commonMethods.getAlertDialog(mActivity);
        }
        ButterKnife.inject(this, view);
        //commonMethods.rotateArrowText(tvWeeklyfare,getContext());
        commonMethods.rotateArrowText(triparrow,getContext());
        commonMethods.rotateArrowText(payarrow,getContext());

        initiallist();
        return view;
    }

    private void init() {
        if (listener == null) return;
        now = Calendar.getInstance();

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

    /**
     * Set Bars As per Earnings
     *
     * @param barView Bars to set Earnings
     */
    private void randomSet(BarView barView) {
        ArrayList<String> test = new ArrayList<String>();
        for (int i = 0; i < 1; i++) {
            test.add("M");
            test.add("TU");
            test.add("W");
            test.add("TH");
            test.add("F");
            test.add("SA");
            test.add("SU");
            if (mActivity.getResources().getString(R.string.layout_direction).equals("1"))
                Collections.reverse(test);
        }
        barView.setBottomTextList(test);
        barView.setDataList(farelist, (int) max);
    }

    /**
     * Call API to get Earning list for Barview
     */
    public void earninglist() {

        commonMethods.showProgressDialog(mActivity, customDialog);
        apiService.earningList(sessionManager.getToken(), "week", days[0]).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (jsonResp.isSuccess()) {
            onSuccess(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Initial List  For Earnings
     */
    public void initiallist() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            now.add(Calendar.DATE, -2);
        }

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        current_date = days[0];
        earninglist();
    }

    /**
     * Shows the previous List
     */
    public void previouslist() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = dateFormat.parse(days[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now.setTime(date);
        now.add(Calendar.DATE, -2);
        String yesterdayAsString = dateFormat.format(now.getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        earninglist();
    }

    /**
     * After Previous list
     */
    public void nextlist() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = dateFormat.parse(days[days.length - 1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now.setTime(date);
        now.add(Calendar.DATE, 1);
        String nextday = dateFormat.format(now.getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        earninglist();
    }

    /**
     * After getting the Response Form API
     *
     * @param jsonResponse Data from JSon Response
     */
    public void onSuccess(JsonResponse jsonResponse) {
        EarningsResultModel earningResultModel = gson.fromJson(jsonResponse.getStrResponse(), EarningsResultModel.class);
        if (earningResultModel != null) {
            earningListModel = earningResultModel.getEarningList();
            if (earningListModel != null) {
                earningDatelistModels = earningListModel.getDateList();
                /**
                 * Get the date(dd-MM-yyyy) From API and Converting into yyyy-MM-dd
                 */
                String oldDate = earningDatelistModels.get(0).getDate();
                Date dateZero = null;
                try {
                    dateZero = new SimpleDateFormat("dd-MM-yyyy").parse(oldDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Use SimpleDateFormat to format a Date into a String in a certain pattern.
                newDate = new SimpleDateFormat("yyyy-MM-dd").format(dateZero);
                total_week_amount = earningListModel.getTotalfare();
                recent_payout = earningListModel.getMostRecent();
                last_trip = earningListModel.getLastTrip();
                farelist.clear();
                if (Float.valueOf(total_week_amount.replaceAll(",", "")) > 0) {
                    rltScrollview.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    tvTotalPayout.setVisibility(View.VISIBLE);
                    tvWeeklyfare.setVisibility(View.VISIBLE);
                    day = new String[earningDatelistModels.size()];
                    dates = new String[earningDatelistModels.size()];
                    fare = new double[earningDatelistModels.size()];

                    for (int i = 0; i < earningDatelistModels.size(); i++) {
                        day[i] = earningDatelistModels.get(i).getDay();
                        dates[i] = earningDatelistModels.get(i).getDate();
                        fare[i] = Double.valueOf(earningDatelistModels.get(i).getTotalFare().replaceAll(",", ""));
                        farelist.add((int) Math.round(Double.valueOf(earningDatelistModels.get(i).getTotalFare().replaceAll(",", ""))));
                    }
                    max = fare[0];

                    for (int i = 1; i < fare.length; i++) {
                        if (fare[i] > max) {
                            max = fare[i];
                        }
                    }

                    int high = (int) ((max / 10) + max);
                    int mid = high / 2;
                    int bottom = mid / 2;
                    tvValuemid.setText(sessionManager.getCurrencySymbol() + String.valueOf(mid));
                    tvValuebottom.setText(sessionManager.getCurrencySymbol() + String.valueOf(bottom));
                    tvValuetop.setText(sessionManager.getCurrencySymbol() + String.valueOf(high));

                    randomSet(barView);
                    if("0".equalsIgnoreCase(mActivity.getResources().getString(R.string.layout_direction))) {
                        Drawable img = mActivity.getResources().getDrawable(R.drawable.next_white);
                        Drawable wrappedDrawable = DrawableCompat.wrap(img);
                        DrawableCompat.setTint(wrappedDrawable, Color.WHITE);
                        img.setBounds(0, 0, 60, 60);
                        tvWeeklyfare.setCompoundDrawables(null, null, img, null);

                        tvWeeklyfare.setText(sessionManager.getCurrencySymbol() + "" + total_week_amount);
                    }
                    else
                    {
                        Drawable img = mActivity.getResources().getDrawable(R.drawable.next_white_rtl);
                        img.setBounds(0, 0, 60, 60);
                        tvWeeklyfare.setCompoundDrawables(img, null, null, null);

                        tvWeeklyfare.setText(sessionManager.getCurrencySymbol() + "" + total_week_amount);
                    }
                    if (mActivity != null) {
                        if (last_trip > 0) {
                            tvLasttripamount.setText(mActivity.getResources().getString(R.string.last_trip) + "" + sessionManager.getCurrencySymbol() + "" + last_trip);
                            tvLasttripamount.setVisibility(View.VISIBLE);
                        } else {
                            tvLasttripamount.setVisibility(View.GONE);
                        }
                        if (recent_payout > 0) {
                            tvMostresentpayout.setVisibility(View.VISIBLE);
                            tvMostresentpayout.setText(mActivity.getResources().getString(R.string.most_recent) + "" + sessionManager.getCurrencySymbol() + "" + recent_payout);
                        } else {
                            tvMostresentpayout.setVisibility(View.GONE);
                        }
                    }


                } else {
                    rltScrollview.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvTotalPayout.setVisibility(View.INVISIBLE);
                    tvWeeklyfare.setVisibility(View.INVISIBLE);
                }
                if (days[0].equals(current_date)) {
                    tvShowdate.setText(mActivity.getResources().getString(R.string.thisweek));
                    tvNextsearch.setVisibility(View.GONE);
                } else {
                    Date date = null;
                    Date date1 = null;
                    String startdate = "";
                    String endate = "";
                    DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    //if("0".equalsIgnoreCase(mActivity.getResources().getString(R.string.layout_direction)))
                         /// targetFormat=  new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    //else
                        //targetFormat=  new SimpleDateFormat("dd MMM yyyy",new Locale("ar"));
                    try {
                        date = originalFormat.parse(days[0]);
                        date1 = originalFormat.parse(days[days.length - 1]);
                        startdate = targetFormat.format(date);
                        endate = targetFormat.format(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tvShowdate.setText(startdate + " - " + endate);
                    tvNextsearch.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    /*private Drawable rotate(int degree)
    {
        Bitmap iconBitmap = ((BitmapDrawable)originalDrawable).getBitmap();

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap targetBitmap = Bitmap.createBitmap(iconBitmap, 0, 0, iconBitmap.getWidth(), iconBitmap.getHeight(), matrix, true);

        return new BitmapDrawable(getResources(), targetBitmap);
    }*/
}
