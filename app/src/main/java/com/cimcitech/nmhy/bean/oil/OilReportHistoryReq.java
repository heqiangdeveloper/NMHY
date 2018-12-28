package com.cimcitech.nmhy.bean.oil;

/**
 * Created by qianghe on 2018/12/20.
 */

public class OilReportHistoryReq {
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private ShipFualDynamicInfoBean ShipFualDynamicInfo;

    public OilReportHistoryReq(int pageNum, int pageSize, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    public OilReportHistoryReq(int pageNum, int pageSize, String orderBy, ShipFualDynamicInfoBean shipFualDynamicInfo) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        ShipFualDynamicInfo = shipFualDynamicInfo;
    }

    public static class ShipFualDynamicInfoBean{
        private String beginTime;
        private String endTime;
        private Integer voyageStatus;

        public ShipFualDynamicInfoBean(String beginTime, String endTime, Integer voyageStatus) {
            this.beginTime = beginTime;
            this.endTime = endTime;
            this.voyageStatus = voyageStatus;
        }
    }
}
