package com.cimcitech.nmhy.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public static final List<String> fStatusList = new ArrayList<String>(Arrays.asList
            ("计划中-不确定","计划中-确定","计划执行中","执行结束"));

    //币种
    public static final List<String> currencyList = new ArrayList<String>(Arrays.asList
            ("人民币","美元"));

    //单位
    public static final Map<String,Integer> unitMap = new LinkedHashMap<String,Integer>(){{
        put("桶",2);
        put("吨",8);
    }};

    //燃油供应商
    public static final List<String> supplyList = new ArrayList<String>(Arrays.asList
            ("中石油","中石化","中海油"));

    //租用类型
    public static final Map<String,String> rentTypeMap = new HashMap<String,String>(){{
        put("RT01","次租");
        put("RT02","期租");
        put("RT03","自有");
    }};

    //燃油类型
    public static final Map<String,String> fuelTypeMap = new LinkedHashMap<String,String>(){{
        put("FP01","重油");
        put("FP02","轻油");
        put("FP03","机油");
    }};

    //航次状态
    //这里使用LinkedHashMap，确保Map中的元素顺序与元素添加的先后顺序一致
    //若使用HashMap,则Map中的元素顺序与元素添加的先后顺序不一致
    //https://blog.csdn.net/qq_27093465/article/details/72676102
    public static final Map<String,Integer> voyageStatusMap = new LinkedHashMap<String,Integer>(){{
        put("抵锚地",1);
        put("航次开始",2);
        put("靠泊",3);
        put("装货",4);
        put("装货完工",5);
        put("卸货",6);
        put("卸货完工",7);
        put("起航",8);
        put("停航",9);
        put("航次结束",10);
    }};

    //智能
    //public static String IP = "http://192.168.137.1:8081/nmhy/";

    //public static String IP = "http://10.33.155.214:8081/nmhy/";

    //多式联运
    public static String IP = "http://192.168.137.1:8081/nmhy/";

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

    //新增 燃油申请 子表
    public static String add_oil_request_detail_url = IP + "fuelApplyDetail/addFuelApplyDetail";

    //新增 燃油动态  主表+子表 一起添加
    public static String add_oil_report_main_url = IP + "shipFualDynamicInfo/addShipFualDynamicInfos";

    //分页查询所有航次计划
    public static String query_voyage_plan_url = IP + "voyagePlan/queryByUserId";

    //分页查询指定的航次计划
    public static String query_voyage_plan_detail_url = IP + "voyageDynamicInfo/queryAllVoyageDynamicInfo";

    //保存航次动态
    public static String save_voyage_plan_dynamic_url = IP + "voyageDynamicInfo/saveVoyageDynamicInfo";

}
