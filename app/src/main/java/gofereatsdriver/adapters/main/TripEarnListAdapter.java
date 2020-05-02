package gofereatsdriver.adapters.main;
/**
 * @package com.trioangle.goferdriver.paymentstatement
 * @subpackage paymentstatement model
 * @category TripEarnListAdapter
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
import gofereatsdriver.datamodels.earnings.DailyEarningslist;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.views.main.tripdetails.TripDetails;

/* ************************************************************
                TripEarnListAdapter
Its used to view the trip earn list details function
*************************************************************** */
public class TripEarnListAdapter extends RecyclerView.Adapter<TripEarnListAdapter.ViewHolder> {
    public Activity context;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    /*public @InjectView(R.id.dailyarrow)
    TextView dailyarrow;*/
    private List<DailyEarningslist> modelItems;

    public TripEarnListAdapter(Activity context, List<DailyEarningslist> Items) {
        this.modelItems = Items;
        this.context = context;
    }

    @Override
    public TripEarnListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_earning_layout, viewGroup, false);
        AppController.getAppComponent().inject(this);
        //commonMethods.rotateArrowText(dailyarrow,context);
        return new ViewHolder(view);
    }

    /*
    *  Driver earning list bind data
    */
    @Override
    public void onBindViewHolder(TripEarnListAdapter.ViewHolder viewHolder, final int i) {
        DailyEarningslist currentItem = getItem(i);
        viewHolder.dailytrip.setText(currentItem.getTime());
        viewHolder.dailyamount.setText(sessionManager.getCurrencySymbol() + currentItem.getDriverEarning());
        viewHolder.rellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paystatement_details = new Intent(context, TripDetails.class);
                paystatement_details.putExtra("tripid", getItem(i).getId());
                context.startActivity(paystatement_details);
                context.overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
        commonMethods.rotateArrowText(viewHolder.dailyarrow,context);
    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    private DailyEarningslist getItem(int position) {
        return modelItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rellay;
        private TextView dailytrip, dailyamount,dailyarrow;

        public ViewHolder(View view) {
            super(view);
            dailytrip = view.findViewById(R.id.dailytrip);
            dailyamount = view.findViewById(R.id.dailyamount);
            rellay = view.findViewById(R.id.rellay);
            dailyarrow=view.findViewById(R.id.dailyarrow);
        }
    }

}
