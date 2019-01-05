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

    //新增 燃油申请 主表
    private FuelApply FuelApplyBean;

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

    //新增 燃油申请 主表
    public OilReq(FuelApply fuelApplyBean) {
        FuelApplyBean = fuelApplyBean;
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

    public static class FuelApply{
        private String applyTime;
        private String bargeName;
        private int voyagePlanId;
        private String applyReason;
        private int owerCompId;
        private int supplierId;
        private String paymentMethod;
        private String currency;
        private String ifPrepaid;
        private double pepaidAmount;

        public FuelApply(String applyTime, String bargeName, int voyagePlanId, String applyReason, int owerCompId, int supplierId, String paymentMethod, String currency, String ifPrepaid, double pepaidAmount) {
            this.applyTime = applyTime;
            this.bargeName = bargeName;
            this.voyagePlanId = voyagePlanId;
            this.applyReason = applyReason;
            this.owerCompId = owerCompId;
            this.supplierId = supplierId;
            this.paymentMethod = paymentMethod;
            this.currency = currency;
            this.ifPrepaid = ifPrepaid;
            this.pepaidAmount = pepaidAmount;
        }
    }
}
