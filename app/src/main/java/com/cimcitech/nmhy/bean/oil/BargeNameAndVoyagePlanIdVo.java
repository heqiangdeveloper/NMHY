package com.cimcitech.nmhy.bean.oil;

import java.util.List;

/**
 * Created by qianghe on 2019/2/21.
 */

public class BargeNameAndVoyagePlanIdVo {
    private List<DataBean> data;
    private boolean success;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
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
        private String portTransportOrder;
        private String cShipName;

        public int getVoyagePlanId() {
            return voyagePlanId;
        }

        public void setVoyagePlanId(int voyagePlanId) {
            this.voyagePlanId = voyagePlanId;
        }

        public String getPortTransportOrder() {
            return portTransportOrder;
        }

        public void setPortTransportOrder(String portTransportOrder) {
            this.portTransportOrder = portTransportOrder;
        }

        public String getcShipName() {
            return cShipName;
        }

        public void setcShipName(String cShipName) {
            this.cShipName = cShipName;
        }
    }
}
