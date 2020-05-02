package gofereatsdriver.datamodels.accept;

/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.accept
 * @category AcceptDetailsModel
 * @author Trioangle Product Team
 * @version 1.0
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 Accept Result Details Model
 Its used to accepted driver details screen
 *************************************************************** */
public class AcceptResultModel {
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("order_details")
    @Expose
    private AcceptDetailsModel acceptDetailsModel;

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

    public AcceptDetailsModel getAcceptDetailsModel() {
        return acceptDetailsModel;
    }

    public void setAcceptDetailsModel(AcceptDetailsModel acceptDetailsModel) {
        this.acceptDetailsModel = acceptDetailsModel;
    }
}
