package gofereatsdriver.datamodels.dropoffrating;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.dropoffrating
 * @category DropoffissuesList
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*************************************************************
 DropoffissuesList
 It is Model Class to get the Issues from API
 *************************************************************** */
public class DropoffissuesList {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("issue")
    @Expose
    private String issue;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
