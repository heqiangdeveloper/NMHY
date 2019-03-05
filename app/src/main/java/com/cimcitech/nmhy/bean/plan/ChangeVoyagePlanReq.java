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
    private int contractId;
    private String voyageNo;
    private long voyagePlanId;
    private int routeId;//线路id
    private String routeName;//线路名
    private String fstatus;//本航次计划的状态
    private String rentType;//船类型
    private String fullInclusion;//是否全包 0-我们供油 1-全包

    public ChangeVoyagePlanReq(long voyagePlanId,String bargeBatchNo, String cShipName, String
            actualSailingTime, String actualStopTime, String estimatedStopTime, String estimatedSailingTime, String portTransportOrder,
                               int bargeId,int contractId, String voyageNo, int routeId, String
                                       routeName, String fstatus,String rentType,String fullInclusion) {
        this.voyagePlanId = voyagePlanId;
        this.bargeBatchNo = bargeBatchNo;
        this.cShipName = cShipName;
        this.actualSailingTime = actualSailingTime;
        this.actualStopTime = actualStopTime;
        this.estimatedStopTime = estimatedStopTime;
        this.estimatedSailingTime = estimatedSailingTime;
        this.portTransportOrder = portTransportOrder;
        this.bargeId = bargeId;
        this.contractId = contractId;
        this.voyageNo = voyageNo;
        this.routeId = routeId;
        this.routeName = routeName;
        this.fstatus = fstatus;
        this.rentType = rentType;
        this.fullInclusion = fullInclusion;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public String getFullInclusion() {
        return fullInclusion;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public void setFullInclusion(String fullInclusion) {
        this.fullInclusion = fullInclusion;
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

    public int getBargeId() {
        return bargeId;
    }

    public void setBargeId(int bargeId) {
        this.bargeId = bargeId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }
}
