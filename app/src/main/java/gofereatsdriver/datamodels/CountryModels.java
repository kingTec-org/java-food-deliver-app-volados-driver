package gofereatsdriver.datamodels;

/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels
 * @category CountryModels
 * @author Trioangle Product Team
 * @version 1.0
 **/

/*****************************************************************
 WeeklyListModel
 ****************************************************************/
public class CountryModels {

    private String countryname, countryid;

    public CountryModels() {
    }

    public CountryModels(String countryname, String countryid) {
        this.countryname = countryname;
        this.countryid = countryid;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }


}
