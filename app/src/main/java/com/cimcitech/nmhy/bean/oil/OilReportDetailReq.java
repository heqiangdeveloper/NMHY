package com.cimcitech.nmhy.bean.oil;

/**
 * Created by qianghe on 2018/12/25.
 */

public class OilReportDetailReq {
    private long dynamicinfoId;
    private String fuelKind;
    private String unit;
    private double realStoreQty;
    private long realyAmount;
    private long taxfreeRealyAmount;
    private double addFuelQty;
    private long addAmount;
    private double taxfreeAddAmount;

    public OilReportDetailReq(long dynamicinfoId, String fuelKind, String unit, double realStoreQty, long realyAmount, long taxfreeRealyAmount, double addFuelQty, long addAmount, double taxfreeAddAmount) {
        this.dynamicinfoId = dynamicinfoId;
        this.fuelKind = fuelKind;
        this.unit = unit;
        this.realStoreQty = realStoreQty;
        this.realyAmount = realyAmount;
        this.taxfreeRealyAmount = taxfreeRealyAmount;
        this.addFuelQty = addFuelQty;
        this.addAmount = addAmount;
        this.taxfreeAddAmount = taxfreeAddAmount;
    }
}
