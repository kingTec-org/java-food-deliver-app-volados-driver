package gofereatsdriver.datamodels.dropoffrating;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.dropoffrating
 * @category DropoffList
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 DropoffList
 It is Model Class Get its DropOff List
 *************************************************************** */

public class DropoffList {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
