package gofereatsdriver.datamodels;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels
 * @category WeeklyModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import gofereatsdriver.datamodels.trips.WeeklyListModel;

/*****************************************************************
 WeeklyModel
 ****************************************************************/

public class WeeklyModel {
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("trip_week_details")
    @Expose
    private List<WeeklyListModel> trip_week_details;

    public WeeklyModel(String statusMessage, String statusCode, List<WeeklyListModel> trip_week_details) {
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
        this.trip_week_details = trip_week_details;
    }


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

    public List<WeeklyListModel> getTripWeekDetails() {
        return trip_week_details;
    }

    public void setTripWeekDetails(List<WeeklyListModel> trip_week_details) {
        this.trip_week_details = trip_week_details;
    }
}

