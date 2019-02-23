package com.cimcitech.nmhy.bean.plan;

import java.io.Serializable;

/**
 * Created by qianghe on 2019/2/22.
 */

public class ChangeVoyagePlanReq implements Serializable{
    private String bargeBatchNo;//船批号
    private String cShipName;
    private String actualSailingTime;
    private String actualStopTime;
    private String estimatedStopTime;
    private String estimatedSailingTime;
    private String portTransportOrder;
    private int bargeId;
    private String voyageNo;
    private long voyagePlanId;
    private int routeId;//线路id
    private String routeName;//线路名
    private String fstatus;//本航次计划的状态

    public ChangeVoyagePlanReq(long voyagePlanId,String bargeBatchNo, String cShipName, String
            actualSailingTime, String actualStopTime, String estimatedStopTime, String estimatedSailingTime, String portTransportOrder, int bargeId, String voyageNo, int routeId, String routeName, String fstatus) {
        this.voyagePlanId = voyagePlanId;
        this.bargeBatchNo = bargeBatchNo;
        this.cShipName = cShipName;
        this.actualSailingTime = actualSailingTime;
        this.actualStopTime = actualStopTime;
        this.estimatedStopTime = estimatedStopTime;
        this.estimatedSailingTime = estimatedSailingTime;
        this.portTransportOrder = portTransportOrder;
        this.bargeId = bargeId;
        this.voyageNo = voyageNo;
        this.routeId = routeId;
        this.routeName = routeName;
        this.fstatus = fstatus;
    }

    public String getActualSailingTime() {
        return actualSailingTime;
    }

    public void setActualSailingTime(String actualSailingTime) {
        this.actualSailingTime = actualSailingTime;
    }

    public String getActualStopTime() {
        return actualStopTime;
    }

    public void setActualStopTime(String actualStopTime) {
        this.actualStopTime = actualStopTime;
    }
}
