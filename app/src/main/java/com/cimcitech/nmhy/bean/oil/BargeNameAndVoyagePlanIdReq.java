package com.cimcitech.nmhy.bean.oil;

/**
 * Created by qianghe on 2019/2/21.
 */

public class BargeNameAndVoyagePlanIdReq {
    private int fstatus;
    private int userId;

    public BargeNameAndVoyagePlanIdReq(int fstatus, int userId) {
        this.fstatus = fstatus;
        this.userId = userId;
    }
}
