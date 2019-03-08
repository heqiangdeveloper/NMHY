package com.cimcitech.nmhy.bean.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qianghe on 2019/3/8.
 */

public class SearchGoodsDataBean {
//    "data": {
//        "endRow": 2,
//                "firstPage": 1,
//                "hasNextPage": false,
//                "hasPreviousPage": false,
//                "isFirstPage": true,
//                "isLastPage": true,
//                "lastPage": 1,
//                "list": [
//        {
//            "baleNum": "28251932302",
//                "brandNum": "BJDC+Z",
//                "cargoId": 11,
//                "cargoName": "热镀锌卷",
//                "cargoTransdemandDetailId": 1001,
//                "consigneeUnit": "宝钢湛江钢铁有限公司",
//                "contractNo": "M8B0001960",
//                "custDemandId": 116,
//                "isLoad": "N",
//                "linkman": "韩震",
//                "loadPort": "九江南鲲码头",
//                "loadPortId": 52,
//                "netWeight": 1.11,
//                "numbers": 10,
//                "phone": "1.75358982E10",
//                "resourcesNum": "",
//                "roughWeight": 1.11,
//                "spec": "0.88*1219*C",
//                "unloadPort": "宝钢湛江成品码头",
//                "unloadPortId": 42
//        },
//        {
//            "baleNum": "28251932301",
//                "brandNum": "BJDC+Z",
//                "cargoId": 11,
//                "cargoName": "热镀锌卷",
//                "cargoTransdemandDetailId": 1000,
//                "consigneeUnit": "宝钢湛江钢铁有限公司",
//                "contractNo": "M8B0001960",
//                "custDemandId": 115,
//                "isLoad": "N",
//                "linkman": "成",
//                "loadPort": "九江南鲲码头",
//                "loadPortId": 52,
//                "netWeight": 1.11,
//                "numbers": 10,
//                "phone": "1.3646282888E10",
//                "resourcesNum": "",
//                "roughWeight": 1.11,
//                "spec": "0.88*1219*C",
//                "unloadPort": "宝钢湛江成品码头",
//                "unloadPortId": 42
//        }
//    ],
//        "navigateFirstPage": 1,
//                "navigateLastPage": 1,
//                "navigatePages": 8,
//                "navigatepageNums": [
//        1
//    ],
//        "nextPage": 0,
//                "pageNum": 1,
//                "pageSize": 10,
//                "pages": 1,
//                "prePage": 0,
//                "size": 2,
//                "startRow": 1,
//                "total": 2
//    }

    private int endRow;
    private int firstPage;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private int lastPage;
    private List<ListBean> list;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int navigatePages;
    private int pageNum;
    private int pageSize;
    private int pages;
    private int prePage;
    private int size;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
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

    public static class ListBean implements Serializable{
        private String baleNum;
        private String brandNum;
        private int cargoId;
        private String cargoName;
        private int cargoTransdemandDetailId;
        private String consigneeUnit;
        private String contractNo;
        private int custDemandId;
        private String isLoad;
        private String linkman;
        private String loadPort;
        private int loadPortId;
        private double netWeight;
        private int numbers;
        private String phone;
        private String resourcesNum;
        private double roughWeight;
        private String spec;
        private String unloadPort;
        private int unloadPortId;

        public String getBaleNum() {
            return baleNum;
        }

        public void setBaleNum(String baleNum) {
            this.baleNum = baleNum;
        }

        public String getBrandNum() {
            return brandNum;
        }

        public void setBrandNum(String brandNum) {
            this.brandNum = brandNum;
        }

        public int getCargoId() {
            return cargoId;
        }

        public void setCargoId(int cargoId) {
            this.cargoId = cargoId;
        }

        public String getCargoName() {
            return cargoName;
        }

        public void setCargoName(String cargoName) {
            this.cargoName = cargoName;
        }

        public int getCargoTransdemandDetailId() {
            return cargoTransdemandDetailId;
        }

        public void setCargoTransdemandDetailId(int cargoTransdemandDetailId) {
            this.cargoTransdemandDetailId = cargoTransdemandDetailId;
        }

        public String getConsigneeUnit() {
            return consigneeUnit;
        }

        public void setConsigneeUnit(String consigneeUnit) {
            this.consigneeUnit = consigneeUnit;
        }

        public String getContractNo() {
            return contractNo;
        }

        public void setContractNo(String contractNo) {
            this.contractNo = contractNo;
        }

        public int getCustDemandId() {
            return custDemandId;
        }

        public void setCustDemandId(int custDemandId) {
            this.custDemandId = custDemandId;
        }

        public String getIsLoad() {
            return isLoad;
        }

        public void setIsLoad(String isLoad) {
            this.isLoad = isLoad;
        }

        public String getLinkman() {
            return linkman;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public String getLoadPort() {
            return loadPort;
        }

        public void setLoadPort(String loadPort) {
            this.loadPort = loadPort;
        }

        public int getLoadPortId() {
            return loadPortId;
        }

        public void setLoadPortId(int loadPortId) {
            this.loadPortId = loadPortId;
        }

        public double getNetWeight() {
            return netWeight;
        }

        public void setNetWeight(double netWeight) {
            this.netWeight = netWeight;
        }

        public int getNumbers() {
            return numbers;
        }

        public void setNumbers(int numbers) {
            this.numbers = numbers;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getResourcesNum() {
            return resourcesNum;
        }

        public void setResourcesNum(String resourcesNum) {
            this.resourcesNum = resourcesNum;
        }

        public double getRoughWeight() {
            return roughWeight;
        }

        public void setRoughWeight(double roughWeight) {
            this.roughWeight = roughWeight;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public String getUnloadPort() {
            return unloadPort;
        }

        public void setUnloadPort(String unloadPort) {
            this.unloadPort = unloadPort;
        }

        public int getUnloadPortId() {
            return unloadPortId;
        }

        public void setUnloadPortId(int unloadPortId) {
            this.unloadPortId = unloadPortId;
        }
    }


}
