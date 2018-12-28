package com.cimcitech.nmhy.utils;

import android.content.Context;

/**
 * Created by qianghe on 2018/11/12.
 */

public class Config {
    public static Context context;
    public static String sharedPreferenceName = "nmhy_sp";
    public static int accountId;
    public static String accountNo = "";//账号
    public static String accountType = "";//角色名
    public static String userName = "";//用户名
    public static String token = "";
    public static String password = "";
    public static final String TEXT_TYPE_INT = "int";//int
    public static final String TEXT_TYPE_NUM = "num";//number
    public static final String TEXT_TYPE_STR = "str";//String

    //public static String IP = "http://192.168.1.119:8080/nmhy/";

    //public static String IP = "http://10.34.10.101:8081/nmhy/";

    public static String IP = "http://192.168.1.159:8087/nmhy/";

    //登录
    public static String login_url = IP + "user/login";

    //燃油上报历史 主表
    public static String oil_report_history_url = IP + "shipFualDynamicInfo/queryShipFualDynamicInfo";

    //燃油上报历史 子表
    public static String oil_report_history_detail_url = IP + "shipFualDynamicInfosub/queryShipFualDynamicInfosub";

    //新增燃油上报 主表
    public static String add_oil_report_url = IP + "shipFualDynamicInfo/addShipFualDynamicInfo";

    //新增燃油上报 子表
    public static String add_oil_report_detail_url = IP + "shipFualDynamicInfosub/addShipFualDynamicInfosub";

    //燃油申请通过历史 主表
    public static String oil_request_history_url = IP + "fuelApply/queryFuelApplyApproved";

    //燃油申请通过历史 子表
    public static String oil_request_history_detail_url = IP + "fuelApplyDetail/queryFuelApplyDetail";

}
