package com.cimcitech.nmhy.bean.plan;

import java.util.List;

/**
 * Created by qianghe on 2019/1/22.
 */

public class StartShipPlanDynamicReq {
    private long voyagePlanId;
    private int bargeId;
    private String actualSailingTime;
    private int contractId;
    private List<ShipFualDynamicInfosubBean> shipFualDynamicInfosub;
    private VoyageDynamicInfosBean voyageDynamicInfos;

    public StartShipPlanDynamicReq(long voyagePlanId, int bargeId, String actualSailingTime,int
            contractId, List<ShipFualDynamicInfosubBean> shipFualDynamicInfosub, VoyageDynamicInfosBean voyageDynamicInfos) {
        this.voyagePlanId = voyagePlanId;
        this.bargeId = bargeId;
        this.actualSailingTime = actualSailingTime;
        this.contractId = contractId;
        this.shipFualDynamicInfosub = shipFualDynamicInfosub;
        this.voyageDynamicInfos = voyageDynamicInfos;
    }
}
