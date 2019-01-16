package com.cimcitech.nmhy.bean.plan;

/**
 * Created by qianghe on 2019/1/3.
 */

public class ShipPlanDetailReq {
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private VoyageDynamicInfoBean voyageDynamicInfo;

    public ShipPlanDetailReq(int pageNum, int pageSize, String orderBy, VoyageDynamicInfoBean voyageDynamicInfo) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        this.voyageDynamicInfo = voyageDynamicInfo;
    }

    public static class VoyageDynamicInfoBean{
        private long voyagePlanId;//航次计划id

        public VoyageDynamicInfoBean(long voyagePlanId) {
            this.voyagePlanId = voyagePlanId;
        }
    }
}
