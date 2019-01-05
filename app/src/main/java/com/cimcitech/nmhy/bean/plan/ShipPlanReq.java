package com.cimcitech.nmhy.bean.plan;

/**
 * Created by qianghe on 2019/1/3.
 */

public class ShipPlanReq {
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private VoyagePlanBean VoyagePlan;

    public ShipPlanReq(int pageNum, int pageSize, String orderBy, VoyagePlanBean voyagePlan) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        VoyagePlan = voyagePlan;
    }

    public static class VoyagePlanBean{
        private long voyagePlanId;
        private int bargeId;
        private String voyageNo;
        private String bargeBatchNo;
        private String fstatus;

        public VoyagePlanBean(long voyagePlanId, int bargeId, String voyageNo, String bargeBatchNo, String fstatus) {
            this.voyagePlanId = voyagePlanId;
            this.bargeId = bargeId;
            this.voyageNo = voyageNo;
            this.bargeBatchNo = bargeBatchNo;
            this.fstatus = fstatus;
        }
    }
}
