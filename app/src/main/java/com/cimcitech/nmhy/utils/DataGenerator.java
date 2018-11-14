package com.cimcitech.nmhy.utils;

import android.support.v4.app.Fragment;
import com.cimcitech.nmhy.activity.home.HomeFragment;
import com.cimcitech.nmhy.activity.message.MessageFragment;
import com.cimcitech.nmhy.activity.user.UserFragment;

/**
 * Created by zhouwei on 17/4/23.
 */

public class DataGenerator {
    public static Fragment[] getFragments() {
        Fragment fragments[] = new Fragment[3];
        fragments[0] = new MessageFragment();//消息
        fragments[1] = new HomeFragment();//主页
        fragments[2] = new UserFragment();//我的
        return fragments;
    }
}
