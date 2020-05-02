package gofereatsdriver.adapters.main;
/**
 * @package com.trioangle.goferdriver.paymentstatement
 * @subpackage paymentstatement model
 * @category DailyEarnListAdapter
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.earnings.WeeklyDetailsList;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.views.main.paymentstatement.DailyEarningDetails;

/* ************************************************************
                DailyEarnListAdapter
Its used to view the list dailyearnlistadapter details
*************************************************************** */
public class DailyEarnListAdapter extends RecyclerView.Adapter<DailyEarnListAdapter.ViewHolder> {
    public Activity context;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    private List<WeeklyDetailsList> modelItems;

    public DailyEarnListAdapter(Activity context, List<WeeklyDetailsList> Items) {
        this.modelItems = Items;
        this.context = context;
    }

    @Override
    public DailyEarnListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.daily_earning_layout, viewGroup, false);
        AppController.getAppComponent().inject(DailyEarnListAdapter.this);

        return new ViewHolder(view);
    }

    /*
    * Driver earning bind data
    */
    @Override
    public void onBindViewHolder(DailyEarnListAdapter.ViewHolder viewHolder, final int i) {
        WeeklyDetailsList currentItem = getItem(i);
        viewHolder.dailytrip.setText(currentItem.getDay() + " " + currentItem.getFormat());
        viewHolder.dailyamount.setText(sessionManager.getCurrencySymbol() + currentItem.getDriverEarning());
        viewHolder.rellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paystatement_details = new Intent(context, DailyEarningDetails.class);
                paystatement_details.putExtra("Date", getItem(i).getDate());
                context.startActivity(paystatement_details);
                context.overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
        commonMethods.rotateArrowText(viewHolder.dailyearningarrow,context);
    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    private WeeklyDetailsList getItem(int position) {
        return modelItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rellay;
        private TextView dailytrip;
        private TextView dailyamount,dailyearningarrow;

        public ViewHolder(View view) {
            super(view);
            dailytrip = view.findViewById(R.id.dailytrip);
            dailyamount = view.findViewById(R.id.dailyamount);
            rellay = view.findViewById(R.id.rellay);
            dailyearningarrow=view.findViewById(R.id.dailyearningarrow);
        }
    }

}
