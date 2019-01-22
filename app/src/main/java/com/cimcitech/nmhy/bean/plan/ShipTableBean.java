package com.cimcitech.nmhy.bean.plan;

import android.widget.Button;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;

/**
 * Created by qianghe on 2019/1/15.
 */
public class ShipTableBean implements Serializable{
//    public ShipTableBean(String portName, String jobTypeValue, String voyageStatusDesc, String oprate) {
//        this.portName = portName;
//        this.jobTypeValue = jobTypeValue;
//        this.voyageStatusDesc = voyageStatusDesc;
//        this.oprate = oprate;
//    }

    private ShipPlanVo.DataBean.VoyageDynamicInfosBean item;
    private String operate;
    public ShipTableBean(ShipPlanVo.DataBean.VoyageDynamicInfosBean item,String operate){
        this.item = item;
        this.operate = operate;
    }

    public ShipPlanVo.DataBean.VoyageDynamicInfosBean getItem() {
        return item;
    }

    public void setItem(ShipPlanVo.DataBean.VoyageDynamicInfosBean item) {
        this.item = item;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
