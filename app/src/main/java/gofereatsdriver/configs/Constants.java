package gofereatsdriver.configs;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage configs
 * @category Constants
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;

/*****************************************************************
 Constants
 ****************************************************************/
public class Constants {

    // Test server IP address Full

    public static final String APP_NAME = "GoferEats";
    public static final String FILE_NAME = "GoferEats";
    public static final String CAMERA_ROLL = "Camera Roll";
    public static final String ALL_IMAGES = "All Images";
    public static final String ALL_VIDEOS = "All Videos";
    public static final String[] SUPPORTED_VIDEO_FORMAT = {"MP4", "3GP", "AVI", "FLV", "MPEG-2"};

    public static final String VP_LOGIN_SLIDER = "Login_Slider";
    public static final String PROFILEIMAGE = "profile_image";
    public static final String STATUS_MSG = "status_message";
    public static final String STATUS_CODE = "status_code";
    public static final String OTP = "otp";

    public static final String REFRESH_ACCESS_TOKEN = "refresh_token";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final int REQUEST_CODE_GALLERY = 5;

    public static final String[] PERMISSIONS_RESTORAGE = {
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final String[] PERMISSIONS_STORAGE = {
            //Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public static final String[] PERMISSIONS_PHOTO = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    public static final TimeZone utcTZ = TimeZone.getTimeZone("UTC");
    public static final SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    public static final SimpleDateFormat chatUtcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    public static final SimpleDateFormat galleryDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
    public static long mLastClickTime = 0;
}
