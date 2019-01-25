package com.cimcitech.nmhy.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2019/1/24.
 */

public class MyLinearLayout extends LinearLayout {
    private TextView  tv1,tv2,tv3,tv4;
    private LinearLayout ll;
    public MyLinearLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mylinear_layout, this);
        ll= (LinearLayout) findViewById(R.id.linearLayout);
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        tv4 = (TextView)findViewById(R.id.textView4);
    }

    public TextView findCommandTv(){
        return tv3;
    }

    public TextView findExceptionTv(){
        return tv4;
    }

    public void setText(String str1,String str2,String str3,String str4){
        tv1.setText(str1);
        tv2.setText(str2);
        tv3.setText(str3);
        tv4.setText(str4);
    }

    public void setBackground(int color){
        ll.setBackgroundColor(color);
    }
}
