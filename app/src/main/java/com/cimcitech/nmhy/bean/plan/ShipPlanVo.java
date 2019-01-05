package com.cimcitech.nmhy.bean.plan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qianghe on 2019/1/3.
 */

public class ShipPlanVo {
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

        public static class ListBean implements Parcelable{
            private String actualSailingTime;
            private String actualStopTime;
            private String bargeBatchNo;
            private int bargeId;
            private int companyId;
            private int contractId;
            private String contractName;
            private double distance;
            private String estimatedSailingTime;
            private String estimatedStopTime;
            private String fstatus;
            private int owerCompId;
            private String owerCompName;
            private String portTransportOrder;
            private String rentType;
            private int routeId;
            private String routeName;
            private ShipBean ship;
            private String shipCompName;
            private String voyageNo;
            private long voyagePlanId;
            protected ListBean(Parcel in){
                actualSailingTime = in.readString();
                actualStopTime = in.readString();
                bargeBatchNo = in.readString();
                bargeId = in.readInt();
                companyId = in.readInt();

                contractId = in.readInt();
                contractName = in.readString();
                distance = in.readDouble();
                estimatedSailingTime = in.readString();
                estimatedStopTime = in.readString();

                fstatus = in.readString();
                owerCompId = in.readInt();
                owerCompName = in.readString();
                portTransportOrder = in.readString();
                rentType = in.readString();

                routeId = in.readInt();
                routeName = in.readString();
                //ship = in.readsh();
                shipCompName = in.readString();
                voyageNo = in.readString();
                voyagePlanId = in.readLong();
            }

            public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
                @Override
                public ListBean createFromParcel(Parcel source) {
                    return new ListBean(source);
                }

                @Override
                public ListBean[] newArray(int size) {
                    return new ListBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(actualSailingTime);
                dest.writeString(actualStopTime);
                dest.writeString(bargeBatchNo);
                dest.writeInt(bargeId);
                dest.writeInt(companyId);

                dest.writeInt(contractId);
                dest.writeString(contractName);
                dest.writeDouble(distance);
                dest.writeString(estimatedSailingTime);
                dest.writeString(estimatedStopTime);

                dest.writeString(fstatus);
                dest.writeInt(owerCompId);
                dest.writeString(owerCompName);
                dest.writeString(portTransportOrder);
                dest.writeString(rentType);

                dest.writeInt(routeId);
                dest.writeString(routeName);
                dest.writeString(shipCompName);
                dest.writeString(voyageNo);
                dest.writeLong(voyagePlanId);
            }

            public int getOwerCompId() {
                return owerCompId;
            }

            public void setOwerCompId(int owerCompId) {
                this.owerCompId = owerCompId;
            }

            public String getOwerCompName() {
                return owerCompName;
            }

            public void setOwerCompName(String owerCompName) {
                this.owerCompName = owerCompName;
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

            public int getCompanyId() {
                return companyId;
            }

            public void setCompanyId(int companyId) {
                this.companyId = companyId;
            }

            public int getContractId() {
                return contractId;
            }

            public void setContractId(int contractId) {
                this.contractId = contractId;
            }

            public String getContractName() {
                return contractName;
            }

            public void setContractName(String contractName) {
                this.contractName = contractName;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getEstimatedSailingTime() {
                return estimatedSailingTime;
            }

            public void setEstimatedSailingTime(String estimatedSailingTime) {
                this.estimatedSailingTime = estimatedSailingTime;
            }

            public String getEstimatedStopTime() {
                return estimatedStopTime;
            }

            public void setEstimatedStopTime(String estimatedStopTime) {
                this.estimatedStopTime = estimatedStopTime;
            }

            public String getFstatus() {
                return fstatus;
            }

            public void setFstatus(String fstatus) {
                this.fstatus = fstatus;
            }

            public String getPortTransportOrder() {
                return portTransportOrder;
            }

            public void setPortTransportOrder(String portTransportOrder) {
                this.portTransportOrder = portTransportOrder;
            }

            public String getRentType() {
                return rentType;
            }

            public void setRentType(String rentType) {
                this.rentType = rentType;
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

            public ShipBean getShip() {
                return ship;
            }

            public void setShip(ShipBean ship) {
                this.ship = ship;
            }

            public String getShipCompName() {
                return shipCompName;
            }

            public void setShipCompName(String shipCompName) {
                this.shipCompName = shipCompName;
            }

            public String getVoyageNo() {
                return voyageNo;
            }

            public void setVoyageNo(String voyageNo) {
                this.voyageNo = voyageNo;
            }

            public long getVoyagePlanId() {
                return voyagePlanId;
            }

            public void setVoyagePlanId(long voyagePlanId) {
                this.voyagePlanId = voyagePlanId;
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
