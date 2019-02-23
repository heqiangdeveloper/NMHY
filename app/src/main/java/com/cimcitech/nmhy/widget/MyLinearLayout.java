package com.cimcitech.nmhy.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2019/1/24.
 */

public class MyLinearLayout extends LinearLayout {
    private TextView  tv1,tv2;
    private ImageView command_Iv;
    private LinearLayout ll;
    public MyLinearLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mylinear_layout, this);
        ll= (LinearLayout) findViewById(R.id.linearLayout);
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        command_Iv = (ImageView) findViewById(R.id.command_iv);
    }

    public ImageView findCommandImageView(){
        return command_Iv;
    }

    public void setText(String str1,String str2){
        tv1.setText(str1);
        tv2.setText(str2);
    }

    public void setCommandImageViewSrc(Drawable  drawable){
        command_Iv.setImageDrawable(drawable);
    }

    public void setBackground(int color){
        ll.setBackgroundColor(color);
    }

    public void isCommandImageVisible(boolean b){
        command_Iv.setVisibility(b?View.VISIBLE:View.INVISIBLE);
    }

    //设置当前的操作状态： 0 查看，1 汇报
    public void setImageTag(int flag){
        command_Iv.setTag(flag);
    }
}
