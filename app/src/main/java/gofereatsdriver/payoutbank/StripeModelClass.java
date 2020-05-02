package gofereatsdriver.payoutbank;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage payoutBank
 * @category StripeModelClass
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*************************************************************
 StripeModelClass
 Used To get and set the Valuess From API
 *************************************************************** */

public class StripeModelClass {

    @SerializedName("status_message")
    @Expose
    private String status_message;
    @SerializedName("status_code")
    @Expose
    private Integer status_code;
    @SerializedName("payout_details")
    @Expose
    private List<StripeListModel> payout_details;


    public StripeModelClass(String status_message, Integer status_code, List<StripeListModel> payout_details) {
        this.status_message = status_message;
        this.status_code = status_code;
        this.payout_details = payout_details;
    }

    public String getStatusmessage() {
        return status_message;
    }

    public void setStatusmessage(String status_message) {
        this.status_message = status_message;
    }

    public Integer getStatuscode() {
        return status_code;
    }

    public void setStatuscode(Integer status_code) {
        this.status_code = status_code;
    }

    public List<StripeListModel> getPayoutDetails() {
        return payout_details;
    }

    public void setPayoutDetails(List<StripeListModel> payout_details) {
        this.payout_details = payout_details;
    }
}
