package gofereatsdriver.datamodels.earnings;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.earnings
 * @category EarningDatelistModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*************************************************************
 EarningDatelistModel
 ****************************************************************/

public class EarningDatelistModel implements Serializable {

    @SerializedName("total_fare")
    @Expose
    private String totalFare;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("date")
    @Expose
    private String date;

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
