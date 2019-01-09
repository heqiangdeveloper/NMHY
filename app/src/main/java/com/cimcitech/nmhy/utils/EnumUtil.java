package com.cimcitech.nmhy.utils;

import java.util.Map;

/**
 * Created by qianghe on 2019/1/8.
 */

public class EnumUtil {
    //Map<String,String>
    //Map<String,Integer>
    //SS 指Map<String,String> 中的2个String
    //SI 指Map<String,Integer> 中的String 和 Integer
    public static String findKeyByValueSS(Map<String,String> map,String value){
        for(String key : map.keySet()){
            if(value.equals(map.get(key))){
                return key;
            }
        }
        return null;
    }

    public static String findKeyByValueSI(Map<String,Integer> map,int value){
        for(String key : map.keySet()){
            if(value == map.get(key)){
                return key;
            }
        }
        return null;
    }

    public static String findValueByKeySS(Map<String,String> map,String key){
        return map.get(key);
    }

    public static int findValueByKeySI(Map<String,Integer> map,String key){
        return map.get(key);
    }
}
