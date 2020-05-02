package gofereatsdriver.datamodels.signinup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.signinup
 * @category SignInUpResultModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Sign in and sign up Result Model
 ****************************************************************/
public class SignInUpResultModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("access_token")
    @Expose
    private String token;

    @SerializedName("user_data")
    @Expose
    private UserDetailsModel userDetailsModels;
    @SerializedName("vehicle_type")
    @Expose
    private ArrayList<VehicleTypeModel> vehicleTypeModels;


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDetailsModel getUserDetailsModels() {
        return userDetailsModels;
    }

    public void setUserDetailsModels(UserDetailsModel userDetailsModels) {
        this.userDetailsModels = userDetailsModels;
    }

    public ArrayList<VehicleTypeModel> getVehicleTypeModels() {
        return vehicleTypeModels;
    }

    public void setVehicleTypeModels(ArrayList<VehicleTypeModel> vehicleTypeModels) {
        this.vehicleTypeModels = vehicleTypeModels;
    }
}
