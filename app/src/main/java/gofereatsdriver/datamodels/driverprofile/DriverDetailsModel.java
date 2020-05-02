package gofereatsdriver.datamodels.driverprofile;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.driverprofile
 * @category DriverDetailsModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 DriverDetailsModel
 It is  Class to get the Cancel Reason for API
 *************************************************************** */

public class DriverDetailsModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mobile_number")
    @Expose
    private String mobile_number;

    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("user_image_url")
    @Expose
    private String profileimage;

    @SerializedName("street")
    @Expose
    private String street;

    @SerializedName("first_address")
    @Expose
    private String area;

    @SerializedName("city")
    @Expose
    private String city;


    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("postal_code")
    @Expose
    private String postalcode;


    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("vehicle_name")
    @Expose
    private String vehiclename;


    @SerializedName("vehicle_number")
    @Expose
    private String vehiclenumber;

    @SerializedName("vehicle_type_name")
    @Expose
    private String vehicletypename;

    @SerializedName("driver_licence_back")
    @Expose
    private String doc1_Driverlicenceback;

    @SerializedName("driver_licence_front")
    @Expose
    private String doc2driverlicencefront;

    @SerializedName("driver_insurance")
    @Expose
    private String doc3Driverinsurance;

    @SerializedName("driver_registeration_certificate")
    @Expose
    private String doc4DriverRegisterationcertificate;

    @SerializedName("driver_motor_certiticate")
    @Expose
    private String doc5Drivermotorcertiticate;

    @SerializedName("owe_amount")
    @Expose
    private String oweAmount;
    @SerializedName("vehicle_image")
    @Expose
    private String vehicleImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilenumber() {
        return mobile_number;
    }

    public void setMobilenumber(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCountrycode() {
        return country_code;
    }

    public void setCountrycode(String country_code) {
        this.country_code = country_code;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVehiclename() {
        return vehiclename;
    }

    public void setVehiclename(String vehiclename) {
        this.vehiclename = vehiclename;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public void setVehiclenumber(String vehiclenumber) {
        this.vehiclenumber = vehiclenumber;
    }

    public String getVehicletypename() {
        return vehicletypename;
    }

    public void setVehicletypename(String vehicletypename) {
        this.vehicletypename = vehicletypename;
    }

    public String getDoc1Driverlicenceback() {
        return doc1_Driverlicenceback;
    }

    public void setDoc1Driverlicenceback(String doc1_Driverlicenceback) {
        this.doc1_Driverlicenceback = doc1_Driverlicenceback;
    }

    public String getDoc2driverlicencefront() {
        return doc2driverlicencefront;
    }

    public void setDoc2driverlicencefront(String doc2driverlicencefront) {
        this.doc2driverlicencefront = doc2driverlicencefront;
    }

    public String getDoc3Driverinsurance() {
        return doc3Driverinsurance;
    }

    public void setDoc3Driverinsurance(String doc3Driverinsurance) {
        this.doc3Driverinsurance = doc3Driverinsurance;
    }

    public String getDoc4DriverRegisterationcertificate() {
        return doc4DriverRegisterationcertificate;
    }

    public void setDoc4DriverRegisterationcertificate(String doc4DriverRegisterationcertificate) {
        this.doc4DriverRegisterationcertificate = doc4DriverRegisterationcertificate;
    }

    public String getDoc5Drivermotorcertiticate() {
        return doc5Drivermotorcertiticate;
    }

    public void setDoc5Drivermotorcertiticate(String doc5Drivermotorcertiticate) {
        this.doc5Drivermotorcertiticate = doc5Drivermotorcertiticate;
    }

    public String getOweAmount() {
        return oweAmount;
    }

    public void setOweAmount(String oweAmount) {
        this.oweAmount = oweAmount;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }
}
