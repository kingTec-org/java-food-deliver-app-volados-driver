package gofereatsdriver.drawpolyline;

/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage drawpolyline
 * @category PolylineOptions Interface
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/*************************************************************
 Interface for draw route
 *************************************************************** */
public interface PolylineOptionsInterface {
    void getPolylineOptions(PolylineOptions output, ArrayList points);
}
