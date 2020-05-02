package gofereatsdriver.datamodels.earnings;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.earnings
 * @category EarningListModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*************************************************************
 EarningListModel
 ****************************************************************/

public class EarningListModel implements Serializable {

    @SerializedName("total_fare")
    @Expose
    private String totalfare;
    @SerializedName("total_week_amount")
    @Expose
    private String totaWeekAmount;
    @SerializedName("last_trip")
    @Expose
    private int lastTrip;
    @SerializedName("most_recent")
    @Expose
    private int mostRecent;
    @SerializedName("recent_payout")
    @Expose
    private String recentPayout;
    @SerializedName("date_list")
    @Expose
    private ArrayList<EarningDatelistModel> dateList;

    public ArrayList<EarningDatelistModel> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<EarningDatelistModel> dateList) {
        this.dateList = dateList;
    }

    public String getTotalfare() {
        return totalfare;
    }

    public void setTotalfare(String totalfare) {
        this.totalfare = totalfare;
    }

    public String getTotaWeekAmount() {
        return totaWeekAmount;
    }

    public void setTotaWeekAmount(String totaWeekAmount) {
        this.totaWeekAmount = totaWeekAmount;
    }

    public String getRecentPayout() {
        return recentPayout;
    }

    public void setRecentPayout(String recentPayout) {
        this.recentPayout = recentPayout;
    }

    public int getLastTrip() {
        return lastTrip;
    }

    public void setLastTrip(int lastTrip) {
        this.lastTrip = lastTrip;
    }

    public int getMostRecent() {
        return mostRecent;
    }

    public void setMostRecent(int mostRecent) {
        this.mostRecent = mostRecent;
    }
}
