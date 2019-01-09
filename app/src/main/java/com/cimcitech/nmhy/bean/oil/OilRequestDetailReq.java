package com.cimcitech.nmhy.bean.oil;

import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by qianghe on 2019/1/8.
 */

public class OilRequestDetailReq {
    private long applyId;
    private String fuelKind;
    private String taxId;
    private double taxRate;
    private String unit;
    private double estimateQty;
    private double estimatePrice;
    private double estimateAmount;

    public OilRequestDetailReq(long applyId, String fuelKind, String taxId, double taxRate, String unit, double estimateQty, double estimatePrice, double estimateAmount) {
        this.applyId = applyId;
        this.fuelKind = fuelKind;
        this.taxId = taxId;
        this.taxRate = taxRate;
        this.unit = unit;
        this.estimateQty = estimateQty;
        this.estimatePrice = estimatePrice;
        this.estimateAmount = estimateAmount;
    }
}
