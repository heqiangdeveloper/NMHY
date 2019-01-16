package com.cimcitech.nmhy.bean.oil;

import java.util.List;

/**
 * Created by qianghe on 2019/1/11.
 */

public class OilReportMainReq {
    private long bargeId;
    private long voyagePlanId;
    private int voyageStatus;
    private String location;
    private double longitude;
    private double latitude;
    private List<ShipFualDynamicInfosubsBean> ShipFualDynamicInfosubs;

    public OilReportMainReq(long bargeId, long voyagePlanId, int voyageStatus, String location, double longitude, double latitude, List<ShipFualDynamicInfosubsBean> shipFualDynamicInfosubs) {
        this.bargeId = bargeId;
        this.voyagePlanId = voyagePlanId;
        this.voyageStatus = voyageStatus;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        ShipFualDynamicInfosubs = shipFualDynamicInfosubs;
    }

    public static class ShipFualDynamicInfosubsBean{
        private String fuelKind;
        private int unit;
        private double addFuelQty;

        public ShipFualDynamicInfosubsBean(String fuelKind, int unit, double addFuelQty) {
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

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }

        public double getAddFuelQty() {
            return addFuelQty;
        }

        public void setAddFuelQty(double addFuelQty) {
            this.addFuelQty = addFuelQty;
        }
    }
}
