package com.cimcitech.nmhy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by qianghe on 2018/7/6.
 */

public class MessageTime {
    private int hour;
    private int day;
    private Date date;
    private DateFormat df;
    private String pointText;
    private Long time;
    private Long now;

    public MessageTime(){
        hour=60*60*1000;
        day=(60*60*24)*1000;
        time=28800L;
        now=new Date().getTime();
        pointText="1970-01-01";
    }

    //获得当天0点时间
    public static Long getTimesmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (Long) cal.getTimeInMillis();
    }

    //获取星期几
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //获取时间点
    public String getTimePoint(Long time) {
        //现在时间
        now = new Date().getTime();
        //实际时间
        date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTimeStr = sdf.format(new Date());
        int currentYear = Integer.parseInt(currentTimeStr.substring(0, 4));

        String actualTimeStr = sdf.format(date);
        int actualYear = Integer.parseInt(actualTimeStr.substring(0, 4));

        //时间点比当天零点早
        if (time <= now && time != null) {
            if (time < getTimesmorning()) {
                if (time >= getTimesmorning() - day) {//比昨天零点晚
                    pointText = "昨天";
                    return pointText;
                } else {//比昨天零点早
                    if (time >= getTimesmorning() - 6 * day) {//比七天前的零点晚，显示星期几
                        return getWeekOfDate(date);
                    } else if (actualYear == currentYear) {//比当前年份早
                        int month = Integer.parseInt(actualTimeStr.substring(5, 7));
                        int day = Integer.parseInt(actualTimeStr.substring(8, 10));
                        pointText = month + "月" + day + "日";
                        return pointText;
                    } else {//显示具体日期
                        df = new SimpleDateFormat("yyyy-MM-dd");
                        pointText = df.format(date);
                        return pointText;
                    }
                }
            }else {//无日期时间,当天内具体时间
                df = new SimpleDateFormat("HH:mm");
                pointText = df.format(date);
                return pointText;
            }
        }
        return pointText;
    }

}
