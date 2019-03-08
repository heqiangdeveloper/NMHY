package com.cimcitech.nmhy.bean.goods;

/**
 * Created by qianghe on 2019/3/8.
 */

public class SearchGoodsReq {
    private int pageNum;
    private int pageSize;
    private String orderBy;
    private CargoTransdemandDetail cargoTransdemandDetail;

    public SearchGoodsReq(int pageNum, int pageSize, String orderBy, CargoTransdemandDetail cargoTransdemandDetail) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        this.cargoTransdemandDetail = cargoTransdemandDetail;
    }

    public static class CargoTransdemandDetail{
        private String codeOrName;
        private String fstatus;

        public CargoTransdemandDetail(String codeOrName, String fstatus) {
            this.codeOrName = codeOrName;
            this.fstatus = fstatus;
        }
    }
}
