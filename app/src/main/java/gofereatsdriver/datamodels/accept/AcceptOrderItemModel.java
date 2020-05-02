package gofereatsdriver.datamodels.accept;

/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage datamodels.accept
 * @category AcceptOrderItemModel
 * @author Trioangle Product Team
 * @version 1.0
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/*************************************************************
 AcceptOrderItemModel
 Its used to get the order items in accepted order
 *************************************************************** */

public class AcceptOrderItemModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer itemId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
