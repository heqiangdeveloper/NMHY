package com.cimcitech.nmhy.bean.oil;

import java.util.List;

/**
 * Created by qianghe on 2019/1/11.
 */

public class OilReportMainReq {
    private int bargeId;
    private long voyagePlanId;
    private long voyageStatusId;
    private String location;
    private double longitude;
    private double latitude;
    private String reportTime;
    private int reporterId;
    private int currPortId;
    private List<ShipFualDynamicInfosubsBean> ShipFualDynamicInfosubs;

    public OilReportMainReq(int currPortId,String reportTime,int reporterId,int bargeId, long
            voyagePlanId, long
            voyageStatusId, String location, double longitude, double latitude, List<ShipFualDynamicInfosubsBean> shipFualDynamicInfosubs) {
        this.reportTime = reportTime;
        this.reporterId = reporterId;
        this.bargeId = bargeId;
        this.voyagePlanId = voyagePlanId;
        this.voyageStatusId = voyageStatusId;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        ShipFualDynamicInfosubs = shipFualDynamicInfosubs;
    }

    public static class ShipFualDynamicInfosubsBean{
        private int fuelKindId;
        private double realStoreQty;

        public ShipFualDynamicInfosubsBean(int fuelKindId,double realStoreQty) {
            this.fuelKindId = fuelKindId;
            this.realStoreQty = realStoreQty;
        }

        public int getFuelKindId() {
            return fuelKindId;
        }

        public void setFuelKindId(int fuelKindId) {
            this.fuelKindId = fuelKindId;
        }

        public double getRealStoreQty() {
            return realStoreQty;
        }

        public void setRealStoreQty(double realStoreQty) {
            this.realStoreQty = realStoreQty;
        }
    }
}
