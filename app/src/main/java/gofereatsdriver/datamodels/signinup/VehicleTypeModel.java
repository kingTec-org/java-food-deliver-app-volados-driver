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

import java.io.Serializable;

/*****************************************************************
 User Details Model
 ****************************************************************/
public class VehicleTypeModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer vehicleId;
    @SerializedName("name")
    @Expose
    private String vehicleName;

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
