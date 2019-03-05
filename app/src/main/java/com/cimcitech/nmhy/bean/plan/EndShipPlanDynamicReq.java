package com.cimcitech.nmhy.bean.plan;

import java.util.List;

/**
 * Created by qianghe on 2019/1/22.
 */

public class EndShipPlanDynamicReq {
    private long voyagePlanId;
    private int bargeId;
    private String actualStopTime;
    private List<ShipFualDynamicInfosubBean> shipFualDynamicInfosub;
    private VoyageDynamicInfosBean voyageDynamicInfos;
    private String rentType;
    private String fullInclusion;
    private String voyageNo;

    public EndShipPlanDynamicReq(long voyagePlanId, int bargeId, String actualStopTime,
                                 List<ShipFualDynamicInfosubBean> shipFualDynamicInfosub,
                                 VoyageDynamicInfosBean voyageDynamicInfos,
                                 String rentType, String fullInclusion,String voyageNo) {
        this.voyagePlanId = voyagePlanId;
        this.bargeId = bargeId;
        this.actualStopTime = actualStopTime;
        this.shipFualDynamicInfosub = shipFualDynamicInfosub;
        this.voyageDynamicInfos = voyageDynamicInfos;
        this.rentType = rentType;
        this.fullInclusion = fullInclusion;
        this.voyageNo = voyageNo;
    }
}
