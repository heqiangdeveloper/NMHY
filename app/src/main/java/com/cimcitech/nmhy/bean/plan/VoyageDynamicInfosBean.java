package com.cimcitech.nmhy.bean.plan;

/**
 * Created by qianghe on 2019/2/25.
 */

public class VoyageDynamicInfosBean {
    private long dynamicId;
    private long voyagePlanId;
    private int currPortId;
    private String estimatedTime;
    private String jobType;
    private String reportTime;
    private String occurTime;
    private int reportId;
    private String voyageStatus;
    private String voyageStatusDesc;
    private String reason;
    private String location;
    private double longitude;
    private double latitude;
    private double speed;
    private String weather;
    private String remark;
    private String fstatus;
    private String feedback;
    private int feedUserId;
    private String feedTime;
    private long voyageStatusId;

    public VoyageDynamicInfosBean(long dynamicId, long voyagePlanId, int currPortId, String estimatedTime, String jobType, String reportTime, String occurTime, int reportId, String voyageStatus, String voyageStatusDesc, String reason, String location, double longitude, double latitude, double speed, String weather, String remark, String fstatus, String feedback, int feedUserId, String feedTime, long voyageStatusId) {
        this.dynamicId = dynamicId;
        this.voyagePlanId = voyagePlanId;
        this.currPortId = currPortId;
        this.estimatedTime = estimatedTime;
        this.jobType = jobType;
        this.reportTime = reportTime;
        this.occurTime = occurTime;
        this.reportId = reportId;
        this.voyageStatus = voyageStatus;
        this.voyageStatusDesc = voyageStatusDesc;
        this.reason = reason;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.weather = weather;
        this.remark = remark;
        this.fstatus = fstatus;
        this.feedback = feedback;
        this.feedUserId = feedUserId;
        this.feedTime = feedTime;
        this.voyageStatusId = voyageStatusId;
    }

}
