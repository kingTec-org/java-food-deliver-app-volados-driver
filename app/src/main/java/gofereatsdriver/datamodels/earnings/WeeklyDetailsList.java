package gofereatsdriver.datamodels.earnings;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.earnings
 * @category WeeklyDetailsList
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 WeeklyDetailsList
 ****************************************************************/

public class WeeklyDetailsList {

    @SerializedName("total_fare")
    @Expose
    private String totalFare;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("driver_earning")
    @Expose
    private String driverEarning;


    public WeeklyDetailsList(String total_fare, String day, String format, String date) {
        this.totalFare = total_fare;
        this.day = day;
        this.format = format;
        this.date = date;
    }


    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriverEarning() {
        return driverEarning;
    }

    public void setDriverEarning(String driverEarning) {
        this.driverEarning = driverEarning;
    }
}
