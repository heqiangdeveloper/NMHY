package com.cimcitech.nmhy.bean.goods;

/**
 * Created by qianghe on 2019/3/8.
 */

public class SearchGoodsVo {
    /*
    *   {
          "code": 0,
          "data": {
            "endRow": 2,
            "firstPage": 1,
            "hasNextPage": false,
            "hasPreviousPage": false,
            "isFirstPage": true,
            "isLastPage": true,
            "lastPage": 1,
            "list": [
              {
                "baleNum": "28251932302",
                "brandNum": "BJDC+Z",
                "cargoId": 11,
                "cargoName": "热镀锌卷",
                "cargoTransdemandDetailId": 1001,
                "consigneeUnit": "宝钢湛江钢铁有限公司",
                "contractNo": "M8B0001960",
                "custDemandId": 116,
                "isLoad": "N",
                "linkman": "韩震",
                "loadPort": "九江南鲲码头",
                "loadPortId": 52,
                "netWeight": 1.11,
                "numbers": 10,
                "phone": "1.75358982E10",
                "resourcesNum": "",
                "roughWeight": 1.11,
                "spec": "0.88*1219*C",
                "unloadPort": "宝钢湛江成品码头",
                "unloadPortId": 42
              },
              {
                "baleNum": "28251932301",
                "brandNum": "BJDC+Z",
                "cargoId": 11,
                "cargoName": "热镀锌卷",
                "cargoTransdemandDetailId": 1000,
                "consigneeUnit": "宝钢湛江钢铁有限公司",
                "contractNo": "M8B0001960",
                "custDemandId": 115,
                "isLoad": "N",
                "linkman": "成",
                "loadPort": "九江南鲲码头",
                "loadPortId": 52,
                "netWeight": 1.11,
                "numbers": 10,
                "phone": "1.3646282888E10",
                "resourcesNum": "",
                "roughWeight": 1.11,
                "spec": "0.88*1219*C",
                "unloadPort": "宝钢湛江成品码头",
                "unloadPortId": 42
              }
            ],
            "navigateFirstPage": 1,
            "navigateLastPage": 1,
            "navigatePages": 8,
            "navigatepageNums": [
              1
            ],
            "nextPage": 0,
            "pageNum": 1,
            "pageSize": 10,
            "pages": 1,
            "prePage": 0,
            "size": 2,
            "startRow": 1,
            "total": 2
          },
          "id": 0,
          "msg": "",
          "success": true
        }
    */
    private String msg;
    private boolean success;
    private SearchGoodsDataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SearchGoodsDataBean getData() {
        return data;
    }

    public void setData(SearchGoodsDataBean data) {
        this.data = data;
    }
}
