package gofereatsdriver.datamodels.driverprofile;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.driverprofile
 * @category DriverModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 DriverModel
 It is  main model Class to get the Cancel Reason for API
 *************************************************************** */

public class DriverModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("driver_profile")
    @Expose
    private DriverDetailsModel driverDetailsModel;

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

    public DriverDetailsModel getDriverDetailsModel() {
        return driverDetailsModel;
    }

    public void setDriverDetailsModel(DriverDetailsModel driverDetailsModel) {
        this.driverDetailsModel = driverDetailsModel;
    }
}
