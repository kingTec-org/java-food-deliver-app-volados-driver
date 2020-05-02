package gofereatsdriver.datamodels.signinup;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.signinup
 * @category UserDetailsModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 User Details Model
 ****************************************************************/
public class UserDetailsModel {

    @SerializedName("id")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String userName;
    @SerializedName("email")
    @Expose
    private String userEmail;
    @SerializedName("mobile_number")
    @Expose
    private String MobileNumber;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("status_text")
    @Expose
    private String status;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
