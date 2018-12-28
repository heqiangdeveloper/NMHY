package com.cimcitech.nmhy.bean.oil;

/**
 * Created by qianghe on 2018/12/20.
 */

public class OilReq {

    //查询燃油申请的主表   历史
    private int pageNum;
    private int pageSize;
    private String orderBy;

    //查询燃油申请的子表  历史
    private FuelApplyDetailBean FuelApplyDetail;

    //查询燃油动态的主表   历史
    private ShipFualDynamicInfoBean ShipFualDynamicInfo;

    //查询燃油动态的子表  历史
    private ShipFualDynamicInfosubBean ShipFualDynamicInfosub;

    //查询燃油申请的主表   历史
    public OilReq(int pageNum, int pageSize, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    //查询燃油申请的子表  历史
    public OilReq(int pageNum, int pageSize, String orderBy, FuelApplyDetailBean fuelApplyDetail) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        FuelApplyDetail = fuelApplyDetail;
    }

    //查询燃油动态的主表   历史
    public OilReq(int pageNum, int pageSize, String orderBy, ShipFualDynamicInfoBean shipFualDynamicInfo) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        ShipFualDynamicInfo = shipFualDynamicInfo;
    }

    //查询燃油动态的子表  历史
    public OilReq(int pageNum, int pageSize, String orderBy, ShipFualDynamicInfosubBean shipFualDynamicInfosub) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        ShipFualDynamicInfosub = shipFualDynamicInfosub;
    }

    public static class ShipFualDynamicInfoBean{
        private String beginTime;
        private String endTime;
        private int voyageStatus;

        public ShipFualDynamicInfoBean(String beginTime, String endTime, int voyageStatus) {
            this.beginTime = beginTime;
            this.endTime = endTime;
            this.voyageStatus = voyageStatus;
        }
    }

    public static class ShipFualDynamicInfosubBean{
        private long dynamicinfoId;

        public ShipFualDynamicInfosubBean(long dynamicinfoId) {
            this.dynamicinfoId = dynamicinfoId;
        }
    }

    public static class FuelApplyDetailBean{
        private int applyId;

        public FuelApplyDetailBean(int applyId) {
            this.applyId = applyId;
        }
    }
}
