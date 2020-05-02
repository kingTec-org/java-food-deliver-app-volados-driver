package gofereatsdriver.datamodels.earnings;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.earnings
 * @category DailyEarningslist
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 DailyEarningslist
 ****************************************************************/

public class DailyEarningslist {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("total_fare")
    @Expose
    private String total_fare;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("driver_earning")
    @Expose
    private String driverEarning;

    public DailyEarningslist(int id, String total_fare, String time) {
        this.id = id;
        this.total_fare = total_fare;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotalFare() {
        return total_fare;
    }

    public void setTotalfare(String total_fare) {
        this.total_fare = total_fare;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDriverEarning() {
        return driverEarning;
    }

    public void setDriverEarning(String driverEarning) {
        this.driverEarning = driverEarning;
    }
}
