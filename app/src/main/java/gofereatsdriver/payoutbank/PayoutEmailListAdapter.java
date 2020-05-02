package gofereatsdriver.payoutbank;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage payoutBank
 * @category PayoutEmailListAdapter
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import java.util.List;

import javax.inject.Inject;

import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.main.JsonResponse;
import gofereatsdriver.interfaces.ApiService;
import gofereatsdriver.interfaces.ServiceListener;
import gofereatsdriver.utils.CommonMethods;
import gofereatsdriver.utils.RequestCallback;
import gofereatsdriver.views.customize.CustomDialog;

/*************************************************************
 PayoutEmailListAdapter
 Which Shows the Email List In Payout
 ****************************************************************/

public class PayoutEmailListAdapter extends RecyclerView.Adapter<PayoutEmailListAdapter.ViewHolder> implements ServiceListener {

    public @Inject
    Gson gson;
    public StripeModelClass stripeModelRes;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public Context context;
    public String payoutid;
    public String type;
    public String userid;
    public int selected = -1;
    public int unselected = -1;
    public List<StripeListModel> stripeModel;
    public Activity activity;

    public PayoutEmailListAdapter(Context context, Activity activity, List<StripeListModel> stripeModel) {
        AppController.getAppComponent().inject(this);
        this.context = context;
        this.stripeModel = stripeModel;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PayoutEmailListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_payout_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.paypal_email.setText(stripeModel.get(position).getPaypalemail());
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        AppController.getAppComponent().inject(this);

        if (stripeModel.get(position).getSetdefault().equals("No")) {
            holder.payout_default.setVisibility(View.GONE);
        } else {
            holder.payout_default.setVisibility(View.VISIBLE);
        }

        if (selected == position) {
            holder.paypalemailmore.setClickable(true);
            holder.paypalemailid.animate().translationX(-(screenWidth / 2 + 100)).setDuration(200);
        } else if (unselected == position) {
            holder.paypalemailmore.setClickable(false);
            holder.paypalemailid.animate().translationX(0).setDuration(200);
        }
        holder.paypalemailmore.setClickable(false);
        if (!"No".equals(stripeModel.get(position).getSetdefault())) {
            holder.paypalemailid.animate().translationX(0).setDuration(200);

        }
        holder.paypalemailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stripeModel.get(position).getSetdefault().equals("No")) {
                    if (!holder.paypalemailmore.isClickable()) {
                        holder.paypalemailmore.setClickable(true);
                        if (selected == position) {
                            selected = -1;
                            unselected = position;
                            holder.paypalemailmore.setClickable(false);
                            holder.paypalemailid.animate().translationX(0).setDuration(200);
                        } else {
                            holder.paypalemailmore.setClickable(true);
                            holder.paypalemailid.animate().translationX(-(screenWidth / 2 + 100)).setDuration(200);
                            selected = position;
                        }
                        // notifyDataSetChanged();
                    } else {
                        holder.paypalemailmore.setClickable(false);
                        holder.paypalemailid.animate().translationX(0).setDuration(200);
                        //paypalemailid.animate().translationX(0).setDuration(200);
                        selected = -1;
                    }
                }
            }
        });

        //delete button click action

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //requestData = (context != null && !isOnline()) ? context.getResources().getString(R.string.network_failure) : t.getMessage();
                userid = sessionManager.getToken();
                payoutid = stripeModel.get(position).getPayoutid().toString();
                type = "delete";
                if (selected == position) {
                    selected = -1;
                }
                commonMethods.showProgressDialog((AppCompatActivity) context, customDialog);
                apiService.payoutChange(sessionManager.getToken(), type, payoutid).enqueue(new RequestCallback(PayoutEmailListAdapter.this));

                //	payoutemail.payoutOption(view,position,0); // // 0  delete and 1 to set default
                holder.paypalemailmore.setClickable(false);
                holder.paypalemailid.animate().translationX(0).setDuration(200);
                //notifyItemRemoved(getAdapterPosition());

            }
        });

        //default button click action

        holder.makedefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid = sessionManager.getToken();
                payoutid = stripeModel.get(position).getPayoutid().toString();
                type = "default";

                commonMethods.showProgressDialog((AppCompatActivity) context, customDialog);
                apiService.payoutChange(sessionManager.getToken(), type, payoutid).enqueue(new RequestCallback(PayoutEmailListAdapter.this));

                holder.paypalemailmore.setClickable(false);
                holder.paypalemailid.animate().translationX(0).setDuration(200);

            }
        });


   /*     holder.rltImagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setRestaurantId(pastOrders.get(position).getRestaurantid());
                Intent detail=new Intent(context, RestaurantDetailActivity.class);
                context.startActivity(detail);
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return stripeModel.size();
    }

    /**
     * Success Response From API
     *
     * @param jsonResp Json Response from Api
     * @param data     Request data (optional)
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {


            stripeModelRes = gson.fromJson(jsonResp.getStrResponse(), StripeModelClass.class);

            stripeModel = stripeModelRes.getPayoutDetails();

            notifyDataSetChanged();
            commonMethods.hideProgressDialog();
        } else {
            commonMethods.hideProgressDialog();

        }
    }

    /**
     * Failure On API
     */
    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();

    }

    /**
     * View Holder Class For Adapter
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public CustomTextView paypal_email;
        public CustomTextView payout_default;
        public RelativeLayout paypalemailmore;
        public RelativeLayout paypalemailid;
        public CustomButton makedefault, delete;


        public ViewHolder(View itemView) {
            super(itemView);
            paypal_email = itemView.findViewById(R.id.paypal_email);
            payout_default = itemView.findViewById(R.id.paypal_ready);
            paypalemailid = itemView.findViewById(R.id.paypalemailid);
            paypalemailmore = itemView.findViewById(R.id.paypalemailmore);
            delete = itemView.findViewById(R.id.paypal_delete);
            makedefault = itemView.findViewById(R.id.paypal_default);
        }
    }
}

