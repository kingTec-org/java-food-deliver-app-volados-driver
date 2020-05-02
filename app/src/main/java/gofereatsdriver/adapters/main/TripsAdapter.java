package gofereatsdriver.adapters.main;
/**
 * @package com.trioangle.goferdriver.paymentstatement
 * @subpackage paymentstatement model
 * @category TripsAdapter
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.trips.TripListModel;
import gofereatsdriver.utils.ImageUtils;
import gofereatsdriver.views.main.home.RequestAcceptActivity;
import gofereatsdriver.views.main.tripdetails.TripDetails;

/* ************************************************************
                TripsAdapter
Its used to view the trip  details function
*************************************************************** */
public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    public @Inject
    SessionManager sessionManager;

    public ArrayList<TripListModel> tripdetailarraylist;
    public Context context;

    public TripsAdapter(ArrayList<TripListModel> tripdetailarraylist, Context context) {
        this.tripdetailarraylist = tripdetailarraylist;
        this.context = context;
        AppController.getAppComponent().inject(this);
    }

    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trips_item_layout, viewGroup, false);
        ButterKnife.inject(this, view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tv_country.setText(context.getResources().getString(R.string.trip_id) + "" + tripdetailarraylist.get(i).getTripId());
        viewHolder.carname.setText(tripdetailarraylist.get(i).getVehicleName());
        int status = 2;
        if (tripdetailarraylist.get(i).getTripStatus() != null) {
            status = tripdetailarraylist.get(i).getTripStatus();
        }
        if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))){
            viewHolder.amountcard.setText(sessionManager.getCurrencySymbol() + " " + tripdetailarraylist.get(i).getTotalFare());
        }
        else
        {
            viewHolder.amountcard.setText(tripdetailarraylist.get(i).getTotalFare() + " " + sessionManager.getCurrencySymbol());
        }


        if (status == 2 || status == 6) {
            viewHolder.status.setText(context.getResources().getString(R.string.canceled));
            viewHolder.amountcard.setVisibility(View.GONE);
        } else if (status == 0) {
            viewHolder.status.setText(context.getResources().getString(R.string.accepted));
            viewHolder.amountcard.setVisibility(View.GONE);
        } else if (status == 1) {
            viewHolder.status.setText(context.getResources().getString(R.string.confirmed));
            viewHolder.amountcard.setVisibility(View.GONE);
        } else if (status == 3) {
            viewHolder.status.setText(context.getResources().getString(R.string.picked));
            viewHolder.amountcard.setVisibility(View.GONE);
        } else if (status == 4) {
            viewHolder.status.setText(context.getResources().getString(R.string.delivered));
            viewHolder.amountcard.setVisibility(View.GONE);
        } else if (status == 5) {
            viewHolder.status.setText(context.getResources().getString(R.string.completed));
            viewHolder.amountcard.setVisibility(View.VISIBLE);
        } else {
            viewHolder.status.setText(context.getResources().getString(R.string.pending));
            viewHolder.amountcard.setVisibility(View.GONE);
        }

        ImageUtils imageUtils = new ImageUtils();
        imageUtils.loadImage(context, viewHolder.imageView, tripdetailarraylist.get(i).getMapImage());

        /**
         * @TripStatus 'pending' => 0, Driver accept the request
         * 'confirmed' => 1, Driver confirmed the  list and rating for restaurant
         * 'declined' => 2,  Driver declined the order list
         * 'started' => 3,  Driver start the trip (pickup the order)
         * 'delivered'  => 4, Driver drop off or delivered the order and rating for eater
         * 'completed' => 5, Driver complete the trip
         * 'cancelled' => 6, Driver or restaurant cancel the trip
         */
        viewHolder.relLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = 2;
                if (tripdetailarraylist.get(i).getTripStatus() != null) {
                    status = tripdetailarraylist.get(i).getTripStatus();
                }
                if (status == 2 || status == 6 || status == 5) {
                    Intent intent = new Intent(context, TripDetails.class);
                    intent.putExtra("tripid", tripdetailarraylist.get(i).getTripId());
                    context.startActivity(intent);
                } else {
                    sessionManager.setDriverStatus(2);
                    sessionManager.setTripStatus(status);
                    sessionManager.setTripId(tripdetailarraylist.get(i).getTripId());

                    Intent requestaccept = new Intent(context, RequestAcceptActivity.class);
                    requestaccept.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(requestaccept);
                    ((Activity) context).finish();

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return tripdetailarraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relLay;
        private TextView tv_country;
        private TextView carname;
        private TextView status;
        private TextView amountcard;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tv_country = view.findViewById(R.id.datetime);
            carname = view.findViewById(R.id.carname);
            status = view.findViewById(R.id.status);
            amountcard = view.findViewById(R.id.amountcard);
            imageView = view.findViewById(R.id.imageView);
            relLay = view.findViewById(R.id.relLay);
        }
    }
}
