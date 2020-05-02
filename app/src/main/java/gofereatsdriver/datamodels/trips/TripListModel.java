package gofereatsdriver.datamodels.trips;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.trips
 * @category TripListModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*****************************************************************
 TripListModel
 ****************************************************************/

public class TripListModel implements Serializable {

    @SerializedName("status")
    @Expose
    private Integer tripStatus;
    @SerializedName("id")
    @Expose
    private Integer tripId;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("map_image")
    @Expose
    private String mapImage;
    @SerializedName("total_fare")
    @Expose
    private String totalFare;

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getMapImage() {
        return mapImage;
    }

    public void setMapImage(String mapImage) {
        this.mapImage = mapImage;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(Integer tripStatus) {
        this.tripStatus = tripStatus;
    }
}
