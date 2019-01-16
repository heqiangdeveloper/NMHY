package com.cimcitech.nmhy.bean.plan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qianghe on 2019/1/3.
 */

public class ShipPlanDetailVo {
    private DataBean data;
    private int id;
    private String msg;
    private boolean success;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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
        private int endRow;
        private int firstPage;
        private boolean hasNextPage;
        private boolean hasPreviousPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private int lastPage;
        private int navigateFirstPage;
        private int navigateLastPage;
        private int navigatePages;
        private int nextPage;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int prePage;
        private int size;
        private int startRow;
        private int total;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

       public static class ListBean implements Serializable{
           private long dynamicId;
           private long voyagePlanId;
           private int currPortId;
           private String portName;
           private int dispNo;
           private String jobType;
           private String jobTypeValue;
           private String voyageStatus;
           private String voyageStatusDesc;
           private String reason;
           private  String location;

           public String getJobTypeValue() {
               return jobTypeValue;
           }

           public void setJobTypeValue(String jobTypeValue) {
               this.jobTypeValue = jobTypeValue;
           }

           public long getDynamicId() {
               return dynamicId;
           }

           public void setDynamicId(long dynamicId) {
               this.dynamicId = dynamicId;
           }

           public long getVoyagePlanId() {
               return voyagePlanId;
           }

           public void setVoyagePlanId(long voyagePlanId) {
               this.voyagePlanId = voyagePlanId;
           }

           public int getCurrPortId() {
               return currPortId;
           }

           public void setCurrPortId(int currPortId) {
               this.currPortId = currPortId;
           }

           public String getPortName() {
               return portName;
           }

           public void setPortName(String portName) {
               this.portName = portName;
           }

           public int getDispNo() {
               return dispNo;
           }

           public void setDispNo(int dispNo) {
               this.dispNo = dispNo;
           }

           public String getJobType() {
               return jobType;
           }

           public void setJobType(String jobType) {
               this.jobType = jobType;
           }

           public String getVoyageStatus() {
               return voyageStatus;
           }

           public void setVoyageStatus(String voyageStatus) {
               this.voyageStatus = voyageStatus;
           }

           public String getVoyageStatusDesc() {
               return voyageStatusDesc;
           }

           public void setVoyageStatusDesc(String voyageStatusDesc) {
               this.voyageStatusDesc = voyageStatusDesc;
           }

           public String getReason() {
               return reason;
           }

           public void setReason(String reason) {
               this.reason = reason;
           }

           public String getLocation() {
               return location;
           }

           public void setLocation(String location) {
               this.location = location;
           }
       }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isFirstPage() {
            return isFirstPage;
        }

        public void setFirstPage(boolean firstPage) {
            isFirstPage = firstPage;
        }

        public boolean isLastPage() {
            return isLastPage;
        }

        public void setLastPage(boolean lastPage) {
            isLastPage = lastPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public int getNavigateFirstPage() {
            return navigateFirstPage;
        }

        public void setNavigateFirstPage(int navigateFirstPage) {
            this.navigateFirstPage = navigateFirstPage;
        }

        public int getNavigateLastPage() {
            return navigateLastPage;
        }

        public void setNavigateLastPage(int navigateLastPage) {
            this.navigateLastPage = navigateLastPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }
    }
}
