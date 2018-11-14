package com.cimcitech.nmhy.bean.message;

/**
 * Created by qianghe on 2018/7/4.
 */

public class MessageContent {
    private String contentDesc;
    private String where;
    private String startTime;
    private String endTime;

    public MessageContent(String contentDesc, String where, String startTime, String endTime) {
        this.contentDesc = contentDesc;
        this.where = where;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
