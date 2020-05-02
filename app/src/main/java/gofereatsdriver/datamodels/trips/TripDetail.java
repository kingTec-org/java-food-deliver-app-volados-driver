package gofereatsdriver.datamodels.trips;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.trips
 * @category TripDetail
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*****************************************************************
 TripDetail
 ****************************************************************/

public class TripDetail implements Serializable {

    @SerializedName("order_id")
    @Expose
    private String orderid;
    @SerializedName("total_fare")
    @Expose
    private String totalfare;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("vehicle_name")
    @Expose
    private String vehiclename;
    @SerializedName("map_image")
    @Expose
    private String mapimage;

    @SerializedName("trip_date")
    @Expose
    private String tripdate;

    @SerializedName("trip_amount")
    @Expose
    private String tripamount;
    @SerializedName("pickup_latitude")
    @Expose
    private String pickuplatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private String pickuplongitude;
    @SerializedName("pickup_location")
    @Expose
    private String pickuplocation;
    @SerializedName("drop_location")
    @Expose
    private String droplocation;
    @SerializedName("drop_latitude")
    @Expose
    private String droplatitude;
    @SerializedName("drop_longitude")
    @Expose
    private String droplongitude;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("duration_hour")
    @Expose
    private String duration_hour;
    @SerializedName("duration_min")
    @Expose
    private String duration_min;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("driver_payout")
    @Expose
    private String driverpayout;
    @SerializedName("trip_subtotal")
    @Expose
    private String tripsubtotal;
    @SerializedName("delivery_fee")
    @Expose
    private String deliveryfee;
    @SerializedName("promo_amount")
    @Expose
    private String promoAmount;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("wallet_amount")
    @Expose
    private String walletamount;
    @SerializedName("owe_amount")
    @Expose
    private String oweamount;
    @SerializedName("admin_payout")
    @Expose
    private String adminPayout;
    @SerializedName("distance_fare")
    @Expose
    private String distanceFare;
    @SerializedName("cash_collected")
    @Expose
    private String cashCollected;
    @SerializedName("pickup_fare")
    @Expose
    private String pickupFare;
    @SerializedName("drop_fare")
    @Expose
    private String dropFare;
    @SerializedName("driver_penality")
    @Expose
    private String driverPenalty;
    @SerializedName("notes")
    @Expose
    private String cancelNotes;
    @SerializedName("cancel_payout")
    @Expose
    private String CancelledPayout;

    @SerializedName("applied_owe")
    @Expose
    private String appliedOwe;

    @SerializedName("applied_penality")
    @Expose
    private String appliedPenality;

    public String getAdminPayout() {
        return adminPayout;
    }

    public void setAdminPayout(String adminPayout) {
        this.adminPayout = adminPayout;
    }

    public String getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(String distanceFare) {
        this.distanceFare = distanceFare;
    }

    public String getCashCollected() {
        return cashCollected;
    }

    public void setCashCollected(String cashCollected) {
        this.cashCollected = cashCollected;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getTotalfare() {
        return totalfare;
    }

    public void setTotalfare(String totalfare) {
        this.totalfare = totalfare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehiclename() {
        return vehiclename;
    }

    public void setVehiclename(String vehiclename) {
        this.vehiclename = vehiclename;
    }

    public String getMapimage() {
        return mapimage;
    }

    public void setMapimage(String mapimage) {
        this.mapimage = mapimage;
    }

    public String getTripdate() {
        return tripdate;
    }

    public void setTripdate(String tripdate) {
        this.tripdate = tripdate;
    }

    public String getTripamount() {
        return tripamount;
    }

    public void setTripamount(String tripamount) {
        this.tripamount = tripamount;
    }

    public String getPickuplatitude() {
        return pickuplatitude;
    }

    public void setPickuplatitude(String pickuplatitude) {
        this.pickuplatitude = pickuplatitude;
    }

    public String getPickuplongitude() {
        return pickuplongitude;
    }

    public void setPickuplongitude(String pickuplongitude) {
        this.pickuplongitude = pickuplongitude;
    }

    public String getPickuplocation() {
        return pickuplocation;
    }

    public void setPickuplocation(String pickuplocation) {
        this.pickuplocation = pickuplocation;
    }

    public String getDroplocation() {
        return droplocation;
    }

    public void setDroplocation(String droplocation) {
        this.droplocation = droplocation;
    }

    public String getDroplatitude() {
        return droplatitude;
    }

    public void setDroplatitude(String droplatitude) {
        this.droplatitude = droplatitude;
    }

    public String getDroplongitude() {
        return droplongitude;
    }

    public void setDroplongitude(String droplongitude) {
        this.droplongitude = droplongitude;
    }

    public String getDuration_hour() {
        return duration_hour;
    }

    public void setDuration_hour(String duration_hour) {
        this.duration_hour = duration_hour;
    }
    public String getDuration_min() {
        return duration_min;
    }

    public void setDuration_min(String duration_min) {
        this.duration_min = duration_min;
    }
   /* public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }*/

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDriverpayout() {
        return driverpayout;
    }

    public void setDriverpayout(String driverpayout) {
        this.driverpayout = driverpayout;
    }

    public String getTripsubtotal() {
        return tripsubtotal;
    }

    public void setTripsubtotal(String tripsubtotal) {
        this.tripsubtotal = tripsubtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getWalletamount() {
        return walletamount;
    }

    public void setWalletamount(String walletamount) {
        this.walletamount = walletamount;
    }

    public String getOweamount() {
        return oweamount;
    }

    public void setOweamount(String oweamount) {
        this.oweamount = oweamount;
    }

    public String getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(String deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public String getPromoAmount() {
        return promoAmount;
    }

    public void setPromoAmount(String promoAmount) {
        this.promoAmount = promoAmount;
    }

    public String getPickupFare() {
        return pickupFare;
    }

    public void setPickupFare(String pickupFare) {
        this.pickupFare = pickupFare;
    }

    public String getDropFare() {
        return dropFare;
    }

    public void setDropFare(String dropFare) {
        this.dropFare = dropFare;
    }


    public String getDriverPenalty() {
        return driverPenalty;
    }

    public void setDriverPenalty(String driverPenalty) {
        this.driverPenalty = driverPenalty;
    }

    public String getCancelNotes() {
        return cancelNotes;
    }

    public void setCancelNotes(String cancelNotes) {
        this.cancelNotes = cancelNotes;
    }

    public String getCancelledPayout() {
        return CancelledPayout;
    }

    public void setCancelledPayout(String cancelledPayout) {
        CancelledPayout = cancelledPayout;
    }

    public String getAppliedOwe() {
        return appliedOwe;
    }

    public void setAppliedOwe(String appliedOwe) {
        this.appliedOwe = appliedOwe;
    }

    public String getAppliedPenality() {
        return appliedPenality;
    }

    public void setAppliedPenality(String appliedPenality) {
        this.appliedPenality = appliedPenality;
    }
}
