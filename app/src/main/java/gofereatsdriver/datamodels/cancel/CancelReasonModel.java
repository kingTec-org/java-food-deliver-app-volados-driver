package gofereatsdriver.datamodels.cancel;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.cancel
 * @category CancelReasonModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 CancelReasonModel
 Its used to get the Cancel Reason for API
 *************************************************************** */

public class CancelReasonModel {

    @SerializedName("id")
    @Expose
    private Integer cancelId;
    @SerializedName("reason")
    @Expose
    private String reason;

    public Integer getCancelId() {
        return cancelId;
    }

    public void setCancelId(Integer cancelId) {
        this.cancelId = cancelId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
