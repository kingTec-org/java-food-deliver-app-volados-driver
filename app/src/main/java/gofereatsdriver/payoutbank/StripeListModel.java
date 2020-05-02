package gofereatsdriver.payoutbank;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage payoutBank
 * @category StripeListModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 StripeListModel
 Which Shows Stripe's Data
 *************************************************************** */

public class StripeListModel {


    @SerializedName("payout_id")
    @Expose
    private Integer payout_id;

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("payout_method")
    @Expose
    private String payout_method;
    @SerializedName("paypal_email")
    @Expose
    private String paypal_email;


    @SerializedName("set_default")
    @Expose
    private String set_default;


    public StripeListModel(Integer payout_id, String user_id, String payout_method, String paypal_email, String set_default) {
        this.payout_id = payout_id;
        this.user_id = user_id;
        this.payout_method = payout_method;
        this.paypal_email = paypal_email;
        this.set_default = set_default;
    }


    public Integer getPayoutid() {
        return payout_id;
    }

    public void setPayoutid(Integer payout_id) {
        this.payout_id = payout_id;
    }

    public String getUserid() {
        return user_id;
    }

    public void setUserid(String user_id) {
        this.user_id = user_id;
    }

    public String getPayoutmethod() {
        return payout_method;
    }

    public void setPayoutmethod(String payout_method) {
        this.payout_method = payout_method;
    }

    public String getPaypalemail() {
        return paypal_email;
    }

    public void setPaypalemail(String paypal_email) {
        this.paypal_email = paypal_email;
    }

    public String getSetdefault() {
        return set_default;
    }

    public void setSetdefault(String set_default) {
        this.set_default = set_default;
    }


}
