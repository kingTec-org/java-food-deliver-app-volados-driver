package gofereatsdriver.datamodels.main;
/**
 * @package com.trioangle.goferdriver.tripsdetails
 * @subpackage tripsdetails model
 * @category GetDriverTripDetails
 * @author Trioangle Product Team
 * @version 1.0
 */

import java.io.Serializable;

/*************************************************************
 GetDriverTripDetails
 Its used to stored the GetDriverTripDetails Information
 *************************************************************** */
public class GetDriverTripDetails implements Serializable {

    private String type;
    private String mapimage;
    private String trip_id;
    private String user_id;
    private String pickup_latitude;
    private String pickup_longitude;
    private String drop_latitude;
    private String drop_longitude;
    private String pickup_location;
    private String drop_location;
    private String car_id;
    private String request_id;
    private String driver_id;
    private String total_time;
    private String total_km;

    private String time_fare;
    private String distance_fare;
    private String base_fare;
    private String total_fare;
    private String access_fee;
    private String status;
    private String driver_payout;
    private String created_at;
    private String updated_at;
    private String vehicle_name;
    private String deleted_at;
    private String driver_name;
    private String driver_thumb_image;
    private String owe_amount;
    private String remaining_owe_amount;
    private String applied_owe_amount;
    private String wallet_amount;
    private String promo_amount;
    private String admin_amount;
    private String payment_method;



    /*
     *  Get driver details model
     */
    /*public GetDriverTripDetails(String type, String mapimage, String user_id, String trip_id, String pickup_latitude, String pickup_longitude, String drop_latitude, String drop_longitude, String pickup_location, String drop_location, String car_id, String request_id, String driver_id, String total_time, String total_km, String time_fare, String distance_fare, String base_fare, String total_fare, String access_fee, String status, String created_at, String updated_at, String vehicle_name, String deleted_at, String driver_name, String driver_payout, String owe_amount, String remaining_owe_amount, String applied_owe_amount, String wallet_amount, String promo_amount, String admin_amount, String payment_method, String driver_thumb_image) {

        this.type = type;
        this.mapimage = mapimage;
        this.user_id = user_id;
        this.trip_id = trip_id;
        this.pickup_latitude = pickup_latitude;
        this.pickup_longitude = pickup_longitude;
        this.drop_latitude = drop_latitude;
        this.drop_longitude = drop_longitude;
        this.pickup_location = pickup_location;
        this.drop_location = drop_location;
        this.car_id = car_id;
        this.request_id = request_id;
        this.driver_id = driver_id;
        this.total_time = total_time;
        this.total_km = total_km;
        this.time_fare = time_fare;
        this.distance_fare = distance_fare;
        this.base_fare = base_fare;
        this.total_fare = total_fare;
        this.access_fee = access_fee;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.vehicle_name = vehicle_name;
        this.deleted_at = deleted_at;
        this.driver_name = driver_name;
        this.owe_amount = owe_amount;
        this.remaining_owe_amount = remaining_owe_amount;
        this.applied_owe_amount = applied_owe_amount;
        this.wallet_amount = wallet_amount;
        this.promo_amount = promo_amount;
        this.payment_method = payment_method;
        this.admin_amount = admin_amount;
        this.driver_payout = driver_payout;
        this.driver_thumb_image = driver_thumb_image;

    }*/

    /*
     *  Getter and setter for driver trips details
     */
    public String getDriverpayout() {
        return driver_payout;
    }

    public void setDriverpayout(String driver_payout) {
        this.driver_payout = driver_payout;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return user_id;
    }

    public void setUserid(String user_id) {
        this.user_id = user_id;
    }

    public String getTripid() {
        return trip_id;
    }

    public void setTripid(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getMapImage() {
        return mapimage;
    }

    public void setMapImage(String mapimage) {
        this.mapimage = mapimage;
    }

    public String getPickuplatitude() {
        return pickup_latitude;
    }

    public void setPickuplatitude(String pickup_latitude) {
        this.pickup_latitude = pickup_latitude;
    }

    public String getPickuplongitude() {
        return pickup_longitude;
    }

    public void setPickuplongitude(String pickup_longitude) {
        this.pickup_longitude = pickup_longitude;
    }

    public String getDroplatitude() {
        return drop_latitude;
    }

    public void setDroplatitude(String drop_latitude) {
        this.drop_latitude = drop_latitude;
    }

    public String getDroplongitude() {
        return drop_longitude;
    }

    public void setDroplongitude(String drop_longitude) {
        this.drop_longitude = drop_longitude;
    }

    public String getPickuplocation() {
        return pickup_location;
    }

    public void setPickuplocation(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getDroplocation() {
        return drop_location;
    }

    public void setDroplocation(String drop_location) {
        this.drop_location = drop_location;
    }

    public String getCarId() {
        return car_id;
    }

    public void setCarId(String car_id) {
        this.car_id = car_id;
    }

    public String getRequestid() {
        return request_id;
    }

    public void setRequestid(String request_id) {
        this.request_id = request_id;
    }

    public String getDriverid() {
        return driver_id;
    }

    public void setDriverid(String driver_id) {
        this.driver_id = driver_id;
    }


    public String getTotaltime() {
        return total_time;
    }

    public void setTotaltime(String total_time) {
        this.total_time = total_time;
    }

    public String getTotalkm() {
        return total_km;
    }

    public void setTotalkm(String total_km) {
        this.total_km = total_km;
    }

    public String getTimefare() {
        return time_fare;
    }

    public void setTimefare(String time_fare) {
        this.time_fare = time_fare;
    }

    public String getDistancefare() {
        return distance_fare;
    }

    public void setDistancefare(String distance_fare) {
        this.distance_fare = distance_fare;
    }

    public String getBasefare() {
        return base_fare;
    }

    public void setBasefare(String base_fare) {
        this.base_fare = base_fare;
    }

    public String getTotalfare() {
        return total_fare;
    }

    public void setTotalfare(String total_fare) {
        this.total_fare = total_fare;
    }


    public String getAccessfee() {
        return access_fee;
    }

    public void setAccessfee(String access_fee) {
        this.access_fee = access_fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedat() {
        return created_at;
    }

    public void setCreatedat(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdatedat() {
        return updated_at;
    }

    public void setUpdatedat(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getVehiclename() {
        return vehicle_name;
    }

    public void setVehiclename(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }


    public String getDeletedat() {
        return deleted_at;
    }

    public void setDeletedat(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getDrivername() {
        return driver_name;
    }

    public void setDrivername(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriverimage() {
        return driver_thumb_image;
    }

    public void setDriverimage(String driver_thumb_image) {
        this.driver_thumb_image = driver_thumb_image;
    }

    public String getOweAmount() {
        return owe_amount;
    }

    public void setOweAmount(String owe_amount) {
        this.owe_amount = owe_amount;
    }

    public String getRemainingOweAmount() {
        return remaining_owe_amount;
    }

    public void setRemainingOweAmount(String remaining_owe_amount) {
        this.remaining_owe_amount = remaining_owe_amount;
    }

    public String getAppliedOweAmount() {
        return applied_owe_amount;
    }

    public void setAppliedOweAmount(String applied_owe_amount) {
        this.applied_owe_amount = applied_owe_amount;
    }

    public String getWalletAmount() {
        return wallet_amount;
    }

    public void setWalletAmount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getPromoAmount() {
        return promo_amount;
    }

    public void setPromoAmount(String promo_amount) {
        this.promo_amount = promo_amount;
    }

    public String getAdminAmount() {
        return admin_amount;
    }

    public void setAdminAmount(String admin_amount) {
        this.admin_amount = admin_amount;
    }

    public String getPaymentMethod() {
        return payment_method;
    }

    public void setPaymentMethod(String payment_method) {
        this.payment_method = payment_method;
    }

}
