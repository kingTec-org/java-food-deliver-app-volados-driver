package gofereatsdriver.datamodels.dropoffrating;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.dropoffrating
 * @category DropoffModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*************************************************************
 DropoffModel
 It is Main Model Class Get its DropOff List and issues List
 *************************************************************** */

public class DropoffModel {

    @SerializedName("issues")
    @Expose
    public ArrayList<DropoffissuesList> driverRestaurantIssues;
    @SerializedName("dropoff_options")
    @Expose
    public ArrayList<DropoffList> dropoff_options;
    @SerializedName("status_message")
    @Expose
    private String statusmessage;
    @SerializedName("status_code")
    @Expose
    private String statuscode;

    public String getStatusmessage() {
        return statusmessage;
    }

    public void setStatusmessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public ArrayList getDriverRestaurantIssues() {
        return driverRestaurantIssues;
    }

    public void setDriverRestaurantIssues(ArrayList driverRestaurantIssues) {
        this.driverRestaurantIssues = driverRestaurantIssues;
    }

    public ArrayList<DropoffList> getDropoffOptions() {
        return dropoff_options;
    }

    public void setDropoffOptions(ArrayList<DropoffList> dropoff_options) {
        this.dropoff_options = dropoff_options;
    }
}
