package com.cimcitech.nmhy.bean.oil;

/**
 * Created by qianghe on 2019/2/20.
 */
/*
    *  {
      "code": 0,
      "data": {
        "voyagePlanId": 29,
        "voyageStatusDesc": "卸货完工",
        "voyageStatusId": 70
      },
      "id": 0,
      "msg": "",
      "success": true
    }
 */

public class VoyageStatusVo {
    private DataBean data;
    private boolean success;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean{
        private int voyagePlanId;
        private String voyageStatusDesc;
        private int voyageStatusId;

        public int getVoyagePlanId() {
            return voyagePlanId;
        }

        public void setVoyagePlanId(int voyagePlanId) {
            this.voyagePlanId = voyagePlanId;
        }

        public String getVoyageStatusDesc() {
            return voyageStatusDesc;
        }

        public void setVoyageStatusDesc(String voyageStatusDesc) {
            this.voyageStatusDesc = voyageStatusDesc;
        }

        public int getVoyageStatusId() {
            return voyageStatusId;
        }

        public void setVoyageStatusId(int voyageStatusId) {
            this.voyageStatusId = voyageStatusId;
        }
    }
}
