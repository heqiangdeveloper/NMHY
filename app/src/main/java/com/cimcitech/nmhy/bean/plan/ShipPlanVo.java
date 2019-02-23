package com.cimcitech.nmhy.bean.plan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianghe on 2019/1/3.
 */

public class ShipPlanVo {
    private List<DataBean> data;
    private int id;
    private String msg;
    private boolean success;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public static class DataBean{
        private String bargeBatchNo;//船批号
        private String cShipName;
        private String actualSailingTime;
        private String actualStopTime;
        private String estimatedStopTime;
        private String estimatedSailingTime;
        private String portTransportOrder;
        private int bargeId;
        private String voyageNo;
        private long voyagePlanId;
        private int routeId;//线路id
        private String routeName;//线路名
        private String fstatus;//本航次计划的状态
        private ArrayList<VoyageDynamicInfosBean> voyageDynamicInfos;

        public long getVoyagePlanId() {
            return voyagePlanId;
        }

        public void setVoyagePlanId(long voyagePlanId) {
            this.voyagePlanId = voyagePlanId;
        }

        public DataBean(long voyagePlanId) {
            this.voyagePlanId = voyagePlanId;
        }

        public static class VoyageDynamicInfosBean implements Parcelable{
            private int currPortId;
            private long dynamicId;
            private String estimatedTime;//预计时间
            private String jobType;
            private String jobTypeValue;
            private String portName;
            private long voyagePlanId;
            private long voyageStatusId;
            private String voyageStatus;
            private String voyageStatusDesc;
            private String reason;
            private String occurTime;
            private String reportTime;
            private String location;
            private double longitude;
            private double latitude;
            private double speed;
            private String weather;
            private String feedback;
            private String remark;

            protected VoyageDynamicInfosBean(Parcel in){
                currPortId = in.readInt();
                dynamicId = in.readLong();
                estimatedTime = in.readString();
                jobType = in.readString();
                jobTypeValue = in.readString();
                portName = in.readString();
                reportTime = in.readString();
                voyagePlanId = in.readLong();
                voyageStatusId = in.readLong();
                voyageStatus = in.readString();
                voyageStatusDesc = in.readString();
                reason = in.readString();
                occurTime = in.readString();
                location = in.readString();
                longitude = in.readDouble();
                latitude = in.readDouble();
                speed = in.readDouble();
                weather = in.readString();
                feedback = in.readString();
                remark = in.readString();
            }

            public static final Creator<VoyageDynamicInfosBean> CREATOR = new Creator<VoyageDynamicInfosBean>() {
                @Override
                public VoyageDynamicInfosBean createFromParcel(Parcel source) {
                    return new VoyageDynamicInfosBean(source);
                }

                @Override
                public VoyageDynamicInfosBean[] newArray(int size) {
                    return new VoyageDynamicInfosBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(currPortId);
                dest.writeLong(dynamicId);
                dest.writeString(estimatedTime);
                dest.writeString(jobType);
                dest.writeString(jobTypeValue);
                dest.writeString(portName);
                dest.writeString(reportTime);
                dest.writeLong(voyagePlanId);
                dest.writeLong(voyageStatusId);
                dest.writeString(voyageStatus);
                dest.writeString(voyageStatusDesc);
                dest.writeString(occurTime);
                dest.writeString(reason);
                dest.writeString(location);
                dest.writeDouble(longitude);
                dest.writeDouble(latitude);
                dest.writeDouble(speed);
                dest.writeString(weather);
                dest.writeString(feedback);
                dest.writeString(remark);
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getOccurTime() {
                return occurTime;
            }

            public void setOccurTime(String occurTime) {
                this.occurTime = occurTime;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getSpeed() {
                return speed;
            }

            public void setSpeed(double speed) {
                this.speed = speed;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getFeedback() {
                return feedback;
            }

            public void setFeedback(String feedback) {
                this.feedback = feedback;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getVoyageStatus() {
                return voyageStatus;
            }

            public void setVoyageStatus(String voyageStatus) {
                this.voyageStatus = voyageStatus;
            }

            public String getJobType() {
                return jobType;
            }

            public void setJobType(String jobType) {
                this.jobType = jobType;
            }

            public long getDynamicId() {
                return dynamicId;
            }

            public void setDynamicId(long dynamicId) {
                this.dynamicId = dynamicId;
            }

            public String getVoyageStatusDesc() {
                return voyageStatusDesc;
            }

            public void setVoyageStatusDesc(String voyageStatusDesc) {
                this.voyageStatusDesc = voyageStatusDesc;
            }

            public int getCurrPortId() {
                return currPortId;
            }

            public void setCurrPortId(int currPortId) {
                this.currPortId = currPortId;
            }

            public String getEstimatedTime() {
                return estimatedTime;
            }

            public void setEstimatedTime(String estimatedTime) {
                this.estimatedTime = estimatedTime;
            }

            public String getJobTypeValue() {
                return jobTypeValue;
            }

            public void setJobTypeValue(String jobTypeValue) {
                this.jobTypeValue = jobTypeValue;
            }

            public String getPortName() {
                return portName;
            }

            public void setPortName(String portName) {
                this.portName = portName;
            }

            public String getReportTime() {
                return reportTime;
            }

            public void setReportTime(String reportTime) {
                this.reportTime = reportTime;
            }

            public long getVoyagePlanId() {
                return voyagePlanId;
            }

            public void setVoyagePlanId(long voyagePlanId) {
                this.voyagePlanId = voyagePlanId;
            }

            public long getVoyageStatusId() {
                return voyageStatusId;
            }

            public void setVoyageStatusId(long voyageStatusId) {
                this.voyageStatusId = voyageStatusId;
            }
        }

        public String getcShipName() {
            return cShipName;
        }

        public void setcShipName(String cShipName) {
            this.cShipName = cShipName;
        }

        public String getActualSailingTime() {
            return actualSailingTime;
        }

        public void setActualSailingTime(String actualSailingTime) {
            this.actualSailingTime = actualSailingTime;
        }

        public String getActualStopTime() {
            return actualStopTime;
        }

        public void setActualStopTime(String actualStopTime) {
            this.actualStopTime = actualStopTime;
        }

        public String getEstimatedStopTime() {
            return estimatedStopTime;
        }

        public void setEstimatedStopTime(String estimatedStopTime) {
            this.estimatedStopTime = estimatedStopTime;
        }

        public String getEstimatedSailingTime() {
            return estimatedSailingTime;
        }

        public void setEstimatedSailingTime(String estimatedSailingTime) {
            this.estimatedSailingTime = estimatedSailingTime;
        }

        public String getPortTransportOrder() {
            return portTransportOrder;
        }

        public void setPortTransportOrder(String portTransportOrder) {
            this.portTransportOrder = portTransportOrder;
        }

        public String getFstatus() {
            return fstatus;
        }

        public void setFstatus(String fstatus) {
            this.fstatus = fstatus;
        }

        public String getVoyageNo() {
            return voyageNo;
        }

        public void setVoyageNo(String voyageNo) {
            this.voyageNo = voyageNo;
        }

        public String getBargeBatchNo() {
            return bargeBatchNo;
        }

        public void setBargeBatchNo(String bargeBatchNo) {
            this.bargeBatchNo = bargeBatchNo;
        }

        public int getBargeId() {
            return bargeId;
        }

        public void setBargeId(int bargeId) {
            this.bargeId = bargeId;
        }

        public int getRouteId() {
            return routeId;
        }

        public void setRouteId(int routeId) {
            this.routeId = routeId;
        }

        public String getRouteName() {
            return routeName;
        }

        public void setRouteName(String routeName) {
            this.routeName = routeName;
        }

        public ArrayList<VoyageDynamicInfosBean> getVoyageDynamicInfos() {
            return voyageDynamicInfos;
        }

        public void setVoyageDynamicInfos(ArrayList<VoyageDynamicInfosBean> voyageDynamicInfos) {
            this.voyageDynamicInfos = voyageDynamicInfos;
        }
    }
}
