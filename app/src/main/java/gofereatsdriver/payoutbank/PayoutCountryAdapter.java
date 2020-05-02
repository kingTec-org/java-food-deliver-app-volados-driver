package gofereatsdriver.payoutbank;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage payoutBank
 * @category PayoutCountryAdapter
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.obs.CustomTextView;

import java.util.List;

import javax.inject.Inject;

import gofereatsdriver.R;
import gofereatsdriver.configs.AppController;
import gofereatsdriver.configs.SessionManager;
import gofereatsdriver.datamodels.CountryModels;

import static gofereatsdriver.views.main.payment.PayoutBankDetailsActivity.alertDialog;

/*************************************************************
 PayoutCountryAdapter
 Which Shows CountryList For Payout
 *************************************************************** */
public class PayoutCountryAdapter extends RecyclerView.Adapter<PayoutCountryAdapter.ViewHolder> {

    public @Inject
    SessionManager sessionManager;

    public Context context;
    public Activity activity;
    public List<CountryModels> countryName;
    public String type;


    public PayoutCountryAdapter(Context context, List<CountryModels> pastOrders, String type) {
        this.context = context;
        this.countryName = pastOrders;
        this.type = type;
    }

    @NonNull
    @Override
    public PayoutCountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_country_list, parent, false);
        AppController.getAppComponent().inject(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayoutCountryAdapter.ViewHolder holder, final int position) {

        holder.order_status.setText(countryName.get(position).getCountryname());

        holder.country_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();

                //sessionManager.setCountry(countryName.get(position).getCountryname());

                if ("country".equals(type)) {
                    sessionManager.setCountry(countryName.get(position).getCountryname());

                } else if ("currency".equals(type)) {
                    sessionManager.setCurrency(countryName.get(position).getCountryname());


                } else {
                    sessionManager.setGender(countryName.get(position).getCountryname());
                }

                if (alertDialog != null) {
                    sessionManager.setCountryCurrencyType(countryName.get(position).getCountryname());
                    alertDialog.cancel();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return countryName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView order_status;
        public RelativeLayout country_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            order_status = itemView.findViewById(R.id.countryname);
            //preparing_text=(CustomTextView)itemView.findViewById(R.id.preparing_text);
            country_layout = itemView.findViewById(R.id.country_layout);
        }
    }
}