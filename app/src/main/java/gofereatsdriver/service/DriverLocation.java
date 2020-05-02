package gofereatsdriver.service;

/**
 * @author Trioangle Product Team
 * @version 1.0
 * @package com.trioangle.goferdriver
 * @subpackage service
 * @category DriverLocation
 */

public class DriverLocation {
    public String lat;
    public String lng;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public DriverLocation() {
    }

    public DriverLocation(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
