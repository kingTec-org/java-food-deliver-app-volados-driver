package gofereatsdriver.interfaces;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage interfaces
 * @category YourTripsListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.res.Resources;

import gofereatsdriver.views.main.tripdetails.YourTrips;

/*****************************************************************
 YourTripsListener
 ****************************************************************/

public interface YourTripsListener {

    Resources getRes();

    YourTrips getInstance();
}
