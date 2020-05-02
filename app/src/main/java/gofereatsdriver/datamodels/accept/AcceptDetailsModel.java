package gofereatsdriver.datamodels.accept;

/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.accept
 * @category AcceptDetailsModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*************************************************************
 AcceptDetailsModel
 Its used to accepted driver details screen
 *************************************************************** */

public class AcceptDetailsModel implements Serializable {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;

    @SerializedName("eater_name")
    @Expose
    private String eaterName;
    @SerializedName("eater_mobile_number")
    @Expose
    private String eaterMobileNumber;
    @SerializedName("eater_thumb_image")
    @Expose
    private String eaterImage;

    @SerializedName("store_name")
    @Expose
    private String restaurantName;
    @SerializedName("store_mobile_number")
    @Expose
    private String restaurantMobileNumber;
    @SerializedName("store_thumb_image")
    @Expose
    private String restaurantImage;


    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("pickup_location")
    @Expose
    private String pickupLocation;
    @SerializedName("drop_location")
    @Expose
    private String dropLocation;
    @SerializedName("pickup_latitude")
    @Expose
    private String pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private String pickupLongitude;
    @SerializedName("drop_latitude")
    @Expose
    private String dropLatitude;
    @SerializedName("drop_longitude")
    @Expose
    private String dropLongitude;
    @SerializedName("delivery_note")
    @Expose
    private String deliveryNote;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("order_items")
    @Expose
    private ArrayList<AcceptOrderItemModel> order_items;
    @SerializedName("collect_cash")
    @Expose
    private String collectCash;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getEaterName() {
        return eaterName;
    }

    public void setEaterName(String eaterName) {
        this.eaterName = eaterName;
    }

    public String getEaterMobileNumber() {
        return eaterMobileNumber;
    }

    public void setEaterMobileNumber(String eaterMobileNumber) {
        this.eaterMobileNumber = eaterMobileNumber;
    }

    public String getEaterImage() {
        return eaterImage;
    }

    public void setEaterImage(String eaterImage) {
        this.eaterImage = eaterImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantMobileNumber() {
        return restaurantMobileNumber;
    }

    public void setRestaurantMobileNumber(String restaurantMobileNumber) {
        this.restaurantMobileNumber = restaurantMobileNumber;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getDropLatitude() {
        return dropLatitude;
    }

    public void setDropLatitude(String dropLatitude) {
        this.dropLatitude = dropLatitude;
    }

    public String getDropLongitude() {
        return dropLongitude;
    }

    public void setDropLongitude(String dropLongitude) {
        this.dropLongitude = dropLongitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<AcceptOrderItemModel> getOrderItems() {
        return order_items;
    }

    public void setOrderItems(ArrayList<AcceptOrderItemModel> order_items) {
        this.order_items = order_items;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getCollectCash() {
        return collectCash;
    }

    public void setCollectCash(String collectCash) {
        this.collectCash = collectCash;
    }
}
