package gofereatsdriver.datamodels.trips;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.trips
 * @category WeeklyListModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 WeeklyListModel
 ****************************************************************/

public class WeeklyListModel {


    @SerializedName("week")
    @Expose
    private String week;
    @SerializedName("total_fare")
    @Expose
    private String total_fare;
    @SerializedName("year")
    @Expose
    private int year;
    @SerializedName("date")
    @Expose
    private String date;

    public WeeklyListModel(String week, String total_fare, int year, String date) {
        this.week = week;
        this.total_fare = total_fare;
        this.year = year;
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTotalFare() {
        return total_fare;
    }

    public void setTotalFare(String total_fare) {
        this.total_fare = total_fare;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
