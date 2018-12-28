package com.cimcitech.nmhy.bean.oil;

/**
 * Created by qianghe on 2018/12/25.
 */

public class OilReportReq {
    private long bargeId;
    private long voyagePlanId;
    private int voyageStatus;
    private String location;
    private double longitude;
    private double latitude;

    public OilReportReq(long bargeId, long voyagePlanId, int voyageStatus, String location, double longitude, double latitude) {
        this.bargeId = bargeId;
        this.voyagePlanId = voyagePlanId;
        this.voyageStatus = voyageStatus;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
