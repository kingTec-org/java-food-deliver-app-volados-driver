package gofereatsdriver.datamodels.earnings;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.earnings
 * @category WeeklyDetailsModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*************************************************************
 WeeklyDetailsModel
 ****************************************************************/

public class WeeklyDetailsModel {


    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("statement")
    @Expose
    private List<WeeklyDetailsList> statement;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("format_date")
    @Expose
    private String format_date;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("total_fare")
    @Expose
    private String totalFare;
    @SerializedName("base_fare")
    @Expose
    private String baseFare;
    @SerializedName("access_fee")
    @Expose
    private String accessFee;
    @SerializedName("cash_collected")
    @Expose
    private String cashFollected;
    @SerializedName("completed_trips")
    @Expose
    private int completed_trips;
    @SerializedName("bank_deposits")
    @Expose
    private String bankDeposits;


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

    public List<WeeklyDetailsList> getStatement() {
        return statement;
    }

    public void setStatement(List<WeeklyDetailsList> statement) {
        this.statement = statement;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getAccessFee() {
        return accessFee;
    }

    public void setAccessFee(String accessFee) {
        this.accessFee = accessFee;
    }

    public String getCashFollected() {
        return cashFollected;
    }

    public void setCashFollected(String cashFollected) {
        this.cashFollected = cashFollected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormatdate() {
        return format_date;
    }

    public void setFormatdate(String format_date) {
        this.format_date = format_date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public int getCompletedtrips() {
        return completed_trips;
    }

    public void setCompletedtrips(int completed_trips) {
        this.completed_trips = completed_trips;
    }

    public String getBankDeposits() {
        return bankDeposits;
    }

    public void setBankDeposits(String bankDeposits) {
        this.bankDeposits = bankDeposits;
    }
}

