package gofereatsdriver.utils;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage utils
 * @category Enums
 * @author Trioangle Product Team
 * @version 1.0
 **/

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*****************************************************************
 Enums
 ****************************************************************/

public class Enums {

    public static final int REQ_SIGNUP = 101;
    public static final int REQ_RESEND_OTP = 102;
    public static final int REQ_UPDATE_DEVICE_ID = 103;
    public static final int REQ_GET_DRIVER_PROFILE = 104;
    public static final int REQ_UPDATE_LOCATION = 105;
    public static final int REQ_UPLOAD_PROFILE_IMG = 106;
    public static final int REQ_UPDATE_PROFILE = 107;
    public static final int REQ_ACCEPT_REQ = 108;
    public static final int REQ_CANCEL_REQ = 109;
    public static final int REQ_GET_TRIP = 110;
    public static final int REQ_LOGOUT = 111;
    public static final int REQ_LANGUAGE = 112;


    public static final int REQ_REASON = 113;
    public static final int REQ_GET_CANCEL_REASONS = 114;
    public static final int REQ_CANCEL_ORDER = 115;
    public static final int REQ_PICKUP_ISSUES = 116;
    public static final int REQ_CONFIRM_ORDER = 117;
    public static final int REQ_START_TRIP = 118;
    public static final int REQ_COMPLETE_TRIP = 119;
    public static final int REQ_STRIPE = 120;
    public static final int REQ_CHECK_STATUS = 121;


    public static final int REQ_ADD_CARD = 125;
    public static final int REQ_EDIT_MOBILE = 130;
    public static final int REQ_FORGOT_PASSWORD = 131;
    public static final int REQ_CHANGE_NUMBER = 132;
    public static final int REQ_VIEW_PAYMENT = 133;


    @Retention(RetentionPolicy.SOURCE)
    public @interface Frag {
    }

    @IntDef({REQ_SIGNUP, REQ_RESEND_OTP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequestType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MatchType {
    }
}
