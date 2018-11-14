package com.cimcitech.nmhy.utils;
import com.cimcitech.nmhy.bean.message.MessageData;

import java.util.Comparator;

/**
 * Created by lyw on 2018/6/19.
 */
//对日期进行先后排序,like “2018-02-12” ， “06:18”
public class MessageDataSortClass implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        int flag = 0;
        MessageData msgData1 = (MessageData)o1;
        MessageData msgData2 = (MessageData)o2;

        String time1 = msgData1.getTime();
        String time2 = msgData2.getTime();

        flag = time1.compareTo(time2);
        return flag;
    }
}
