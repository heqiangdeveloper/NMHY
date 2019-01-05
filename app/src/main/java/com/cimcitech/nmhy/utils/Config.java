package com.cimcitech.nmhy.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    //航次状态
    public static final List<String> voyageStatusList = new ArrayList<String>(Arrays.asList
            ("计划中-不确定","计划中-确定","计划执行中","执行结束"));

    public static final List<String> currencyList = new ArrayList<String>(Arrays.asList
            ("人民币","美元"));

    public static final List<String> supplyList = new ArrayList<String>(Arrays.asList
            ("中石油","中石化","中海油"));

    //智能
    public static String IP = "http://192.168.1.120:8087/nmhy/";

    //public static String IP = "http://10.33.88.206:8080/nmhy/";

    //多式联运
    //public static String IP = "http://10.33.176.239:8087/nmhy/";

    //public static String IP = "http://192.168.1.159:8086/nmhy/";

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

    //燃油申请历史 主表
    public static String oil_request_history_url = IP + "fuelApply/queryFuelApply";

    //燃油申请历史 子表
    public static String oil_request_history_detail_url = IP + "fuelApplyDetail/queryFuelApplyDetail";

    //新增 燃油申请 主表
    public static String add_oil_request_url = IP + "fuelApply/addFuelApply";

    //分页查询所有航次计划
    public static String query_voyage_plan_url = IP + "voyagePlan/queryAllVoyagePlan";

}
