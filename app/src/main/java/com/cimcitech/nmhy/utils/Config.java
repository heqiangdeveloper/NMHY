package com.cimcitech.nmhy.utils;

/**
 * Created by qianghe on 2018/11/12.
 */

public class Config {
    public static String sharedPreferenceName = "nmhy_sp";
    public static int accountId;
    public static String accountNo = "";//账号
    public static String accountType = "";//角色名
    public static String userName = "";//用户名
    public static String token = "";
    public static String password = "";

    public static String IP = "http://192.168.1.111:8080/nmhy/";

    //登录
    public static String login_url = IP + "user/login";

}
