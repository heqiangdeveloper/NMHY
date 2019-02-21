package com.cimcitech.nmhy.bean.oil;

import java.util.List;

/**
 * Created by qianghe on 2019/1/11.
 */

public class OilReportMainReq {
    private long bargeId;
    private long voyagePlanId;
    private int voyageStatusId;
    private String location;
    private double longitude;
    private double latitude;
    private List<ShipFualDynamicInfosubsBean> ShipFualDynamicInfosubs;

    public OilReportMainReq(long bargeId, long voyagePlanId, int voyageStatusId, String location, double longitude, double latitude, List<ShipFualDynamicInfosubsBean> shipFualDynamicInfosubs) {
        this.bargeId = bargeId;
        this.voyagePlanId = voyagePlanId;
        this.voyageStatusId = voyageStatusId;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        ShipFualDynamicInfosubs = shipFualDynamicInfosubs;
    }

    public static class ShipFualDynamicInfosubsBean{
        private String fuelKind;
        private int unit;
        private double realStoreQty;

        public ShipFualDynamicInfosubsBean(String fuelKind, int unit, double realStoreQty) {
            this.fuelKind = fuelKind;
            this.unit = unit;
            this.realStoreQty = realStoreQty;
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

        public double getRealStoreQty() {
            return realStoreQty;
        }

        public void setRealStoreQty(double realStoreQty) {
            this.realStoreQty = realStoreQty;
        }
    }
}
