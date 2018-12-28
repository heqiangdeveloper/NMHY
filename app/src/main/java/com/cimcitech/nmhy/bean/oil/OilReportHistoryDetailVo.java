package com.cimcitech.nmhy.bean.oil;

import java.util.List;

/**
 * Created by qianghe on 2018/12/20.
 */

public class OilReportHistoryDetailVo {
    private DataBean data;
    private String msg;
    private boolean success;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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
        private List<OilData> list;
        private List<Integer> navigatepageNums;

        public static class OilData{
            private String fuelKind;
            private String unit;
            private double realStoreQty;
            private long realyAmount;
            private long taxfreeRealyAmount;
            private double addFuelQty;
            private long addAmount;
            private double taxfreeAddAmount;
            private double consumeQuantity;
            private double taxfreeConsumeQuantity;
            private long consumeAmount;

            public String getFuelKind() {
                return fuelKind;
            }

            public void setFuelKind(String fuelKind) {
                this.fuelKind = fuelKind;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public double getRealStoreQty() {
                return realStoreQty;
            }

            public void setRealStoreQty(double realStoreQty) {
                this.realStoreQty = realStoreQty;
            }

            public long getRealyAmount() {
                return realyAmount;
            }

            public void setRealyAmount(long realyAmount) {
                this.realyAmount = realyAmount;
            }

            public long getTaxfreeRealyAmount() {
                return taxfreeRealyAmount;
            }

            public void setTaxfreeRealyAmount(long taxfreeRealyAmount) {
                this.taxfreeRealyAmount = taxfreeRealyAmount;
            }

            public double getAddFuelQty() {
                return addFuelQty;
            }

            public void setAddFuelQty(double addFuelQty) {
                this.addFuelQty = addFuelQty;
            }

            public long getAddAmount() {
                return addAmount;
            }

            public void setAddAmount(long addAmount) {
                this.addAmount = addAmount;
            }

            public double getTaxfreeAddAmount() {
                return taxfreeAddAmount;
            }

            public void setTaxfreeAddAmount(double taxfreeAddAmount) {
                this.taxfreeAddAmount = taxfreeAddAmount;
            }

            public double getConsumeQuantity() {
                return consumeQuantity;
            }

            public void setConsumeQuantity(double consumeQuantity) {
                this.consumeQuantity = consumeQuantity;
            }

            public double getTaxfreeConsumeQuantity() {
                return taxfreeConsumeQuantity;
            }

            public void setTaxfreeConsumeQuantity(double taxfreeConsumeQuantity) {
                this.taxfreeConsumeQuantity = taxfreeConsumeQuantity;
            }

            public long getConsumeAmount() {
                return consumeAmount;
            }

            public void setConsumeAmount(long consumeAmount) {
                this.consumeAmount = consumeAmount;
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

        public List<OilData> getList() {
            return list;
        }

        public void setList(List<OilData> list) {
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
