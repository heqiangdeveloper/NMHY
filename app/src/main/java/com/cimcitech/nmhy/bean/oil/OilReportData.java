package com.cimcitech.nmhy.bean.oil;

/**
 * Created by qianghe on 2019/1/10.
 */

public class OilReportData {
    //燃油 品种，单位，数量
    private String fuelKind;
    private String unit;
    private double addFuelQty;

    public OilReportData(String fuelKind, String unit, double addFuelQty) {
        this.fuelKind = fuelKind;
        this.unit = unit;
        this.addFuelQty = addFuelQty;
    }

    public String getFuelKind() {
        return fuelKind;
    }

    public void setFuelKind(String fuelKind) {
        this.fuelKind = fuelKind;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAddFuelQty() {
        return addFuelQty;
    }

    public void setAddFuelQty(double addFuelQty) {
        this.addFuelQty = addFuelQty;
    }
}
