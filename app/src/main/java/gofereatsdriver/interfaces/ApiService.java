package gofereatsdriver.interfaces;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage interfaces
 * @category ApiService
 * @author Trioangle Product Team
 * @version 1.0
 **/

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*****************************************************************
 ApiService
 ****************************************************************/

public interface ApiService {

    //Login
    @GET("login")
    Call<ResponseBody> login(@Query("type") String type, @QueryMap HashMap<String, String> hashMap);

    // Check user Mobile Number
    @GET("number_validation")
    Call<ResponseBody> numberValidation(@Query("type") String type, @Query("mobile_number") String mobilenumber, @Query("country_code") String countrycode,@Query("language") String language) ;

    // Register
    @GET("register")
    Call<ResponseBody> register(@Query("type") String type, @QueryMap HashMap<String, String> hashMap,@Query("language") String language);

    // Register
    @GET("vehicle_details")
    Call<ResponseBody> addVehicle(@Query("type") String type, @Query("vehicle_type") Integer vehicleId, @Query("vehicle_name") String vehicleName, @Query("vehicle_number") String vehicleNumber, @Query("token") String token);

    //forgot Password
    @GET("forgot_password")
    Call<ResponseBody> forgotPassword(@Query("type") String type, @Query("mobile_number") String mobilenumber, @Query("country_code") String countrycode,@Query("language") String language);

    //Reset Password
    @GET("reset_password")
    Call<ResponseBody> resetPassword(@Query("type") String type, @Query("mobile_number") String mobilenumber, @Query("country_code") String countrycode, @Query("password") String password);

    // Update Device ID for Push notification
    @GET("update_device_id")
    Call<ResponseBody> updateDeviceId(@Query("type") Integer type, @Query("token") String token, @Query("device_type") String device_type, @Query("device_id") String device_id);

    // Update location with lat,lng and driverStatus
    @GET("update_driver_location")
    Call<ResponseBody> updateLocation(@QueryMap HashMap<String, String> hashMap);

    // Upload Documents image
    @POST("document_upload")
    Call<ResponseBody> uploadDocumentImage(@Body RequestBody RequestBody, @Query("token") String token);

    // Driver accept the request
    @GET("accept_request")
    Call<ResponseBody> acceptRequest(@Query("order_id") Integer order_id, @Query("request_id") Integer requestId, @Query("token") String token);

    // Driver not accept the request
    @GET("cancel_request")
    Call<ResponseBody> notAcceptRequest(@Query("order_id") Integer order_id, @Query("request_id") Integer requestId, @Query("token") String token);

    // Get accepted order details
    @GET("driver_order_details")
    Call<ResponseBody> getOrderDetails(@Query("order_id") Integer order_id, @Query("token") String token);


    //Get Driver Profile
    @GET("get_driver_profile")
    Call<ResponseBody> getDriverProfile(@Query("token") String token);

    //Upload Profile Image
    @POST("upload_image")
    Call<ResponseBody> uploadImage(@Body RequestBody RequestBody, @Query("token") String token, @Query("type") String type);

    @POST("add_payout_perference")
    Call<ResponseBody> uploadStripe(@Body RequestBody RequestBody, @Query("token") String token);

    //Update Driver Profile
    @GET("update_driver_profile")
    Call<ResponseBody> updateProfile(@QueryMap HashMap<String, String> hashMap, @Query("token") String token);

    // Log out Driver
    @GET("logout")
    Call<ResponseBody> logOut(@Query("token") String token);

    @GET("payout_details")
    Call<ResponseBody> stripeList(@Query("token") String token);


    @GET("payout_changes")
    Call<ResponseBody> payoutChange(@Query("token") String token, @Query("type") String type, @Query("payout_id") String id);

    // Drop off Reasons Driver
    @GET("pickup_data")
    Call<ResponseBody> pickupData(@Query("token") String token, @Query("order_id") Integer orderId);

    // Drop off Reasons Driver
    @GET("dropoff_data")
    Call<ResponseBody> dropOffData(@Query("token") String token, @Query("order_id") Integer orderId);

    // Cancel selected order
    @GET("get_cancel_reason")
    Call<ResponseBody> cancelOrdersReason(@Query("type") String userType, @Query("token") String token);

    // Cancel selected order
    @GET("cancel_order_delivery")
    Call<ResponseBody> cancelOrders(@Query("cancel_reason") Integer cancelReason, @Query("cancel_message") String cancelMessage, @Query("order_id") Integer orderId, @Query("token") String token);

    // Confirms the order items
    @GET("confirm_order_delivery")
    Call<ResponseBody> confirmOrder(@Query("token") String token, @Query("order_id") Integer orderId, @Query("is_thumbs") Integer isthumbs, @Query("issues") String issue);

    // Starts the trip
    @GET("start_order_delivery")
    Call<ResponseBody> startTrip(@Query("token") String token, @Query("order_id") Integer orderId);

    // Drops off
    @GET("drop_off_delivery")
    Call<ResponseBody> dropOff(@Query("token") String token, @Query("order_id") Integer orderId, @Query("recipient") Integer recipient, @Query("is_thumbs") Integer isthumbs, @Query("issues") String issue);

    // Completes delivery
    @POST("complete_order_delivery")
    Call<ResponseBody> completeDelivery(@Body RequestBody RequestBody, @Query("token") String token);

    // Get Card
    @GET("get_card_details")
    Call<ResponseBody> viewCard(@Query("token") String token);


    // Add to cart
    @GET("add_card_details")
    Call<ResponseBody> addCard(@Query("stripe_id") String stripeId, @Query("token") String token);

    // Send OwnAmount
    @GET("pay_to_admin")
    Call<ResponseBody> payToAdmin(@Query("amount") String amount, @Query("token") String token);

    // Send OwnAmount
    @GET("earning_list")
    Call<ResponseBody> earningList(@Query("token") String token, @Query("type") String type, @Query("start_date") String startdate);

    // Get Order history
    @GET("order_delivery_history")
    Call<ResponseBody> orderHistory(@Query("token") String token);

    // Get Order history
    @GET("particular_order")
    Call<ResponseBody> orderDetail(@Query("token") String token, @Query("order_id") Integer tripid);

    // Change Mobile Number
    @GET("change_mobile")
    Call<ResponseBody> changeMobile(@Query("token") String token, @Query("type") String userType, @Query("mobile_number") String mobileNumber, @Query("country_code") String countrtyCode);

    // Get Weekly Flow
    @GET("weekly_trip")
    Call<ResponseBody> weeklyList(@Query("token") String token);

    // Get Weekly Details
    @GET("weekly_statement")
    Call<ResponseBody> weeklyDetail(@Query("token") String token, @Query("date") String date);


    // Get Weekly Details
    @GET("daily_statement")
    Call<ResponseBody> dailyDetail(@Query("token") String token, @Query("date") String date);

    // Get Weekly Details
    @GET("check_status")
    Call<ResponseBody> checkStatus(@Query("token") String token,@Query("language") String language);

    //Language Update
    @GET("language")
    Call<ResponseBody> language(@Query("type") String type,@Query("language") String languageCode, @Query("token") String token);

}
