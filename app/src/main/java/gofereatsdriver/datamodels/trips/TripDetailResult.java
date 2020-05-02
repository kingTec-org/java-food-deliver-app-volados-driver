package gofereatsdriver.datamodels.trips;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.trips
 * @category TripDetailResult
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*****************************************************************
 TripDetailResult
 ****************************************************************/

public class TripDetailResult implements Serializable {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("trip_details")
    @Expose
    private TripDetail tripdetails;

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

    public TripDetail getTripdetails() {
        return tripdetails;
    }

    public void setTripdetails(TripDetail tripdetails) {
        this.tripdetails = tripdetails;
    }
}
