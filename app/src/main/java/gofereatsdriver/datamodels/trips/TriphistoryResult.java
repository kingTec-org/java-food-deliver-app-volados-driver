package gofereatsdriver.datamodels.trips;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.trips
 * @category TriphistoryResult
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************
 TriphistoryResult
 ****************************************************************/

public class TriphistoryResult implements Serializable {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;
    @SerializedName("past_delivery")
    @Expose
    private ArrayList<TripListModel> pastDelivery;
    @SerializedName("today_delivery")
    @Expose
    private ArrayList<TripListModel> totalDelivery;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public ArrayList<TripListModel> getPastDelivery() {
        return pastDelivery;
    }

    public void setPastDelivery(ArrayList<TripListModel> pastDelivery) {
        this.pastDelivery = pastDelivery;
    }

    public ArrayList<TripListModel> getTotalDelivery() {
        return totalDelivery;
    }

    public void setTotalDelivery(ArrayList<TripListModel> totalDelivery) {
        this.totalDelivery = totalDelivery;
    }
}
