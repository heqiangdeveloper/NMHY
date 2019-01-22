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
        private int bargeId;
        private String voyageNo;
        private int routeId;//线路id
        private String routeName;//线路名
        private ArrayList<VoyageDynamicInfosBean> voyageDynamicInfos;

        public static class VoyageDynamicInfosBean implements Parcelable{
            private int currPortId;
            private long dynamicId;
            private String estimatedTime;
            private String jobType;
            private String jobTypeValue;
            private String portName;
            private String reportTime;
            private long voyagePlanId;
            private long voyageStatusId;
            private String voyageStatus;
            private String voyageStatusDesc;

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
