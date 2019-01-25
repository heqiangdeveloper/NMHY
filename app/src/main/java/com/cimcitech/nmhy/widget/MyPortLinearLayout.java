package com.cimcitech.nmhy.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2019/1/24.
 */

public class MyPortLinearLayout extends LinearLayout {
    private TextView  portNameTv,lineTv;
    private LinearLayout ll;
    public MyPortLinearLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.myport_layout, this);
        ll= (LinearLayout) findViewById(R.id.linearLayout);
        portNameTv = (TextView)findViewById(R.id.portName1_tv);
        lineTv = (TextView)findViewById(R.id.line1_tv);
    }

    public void setText(String str1){
        portNameTv.setText(str1);
    }

    public void setTextBackgroundColor(int color){
        GradientDrawable mm1 = (GradientDrawable) portNameTv.getBackground();
        mm1.setColor(color);
//        GradientDrawable mm2 = (GradientDrawable) lineTv.getBackground();
//        mm2.setColor(color);
    }

    public void setLineVisibility(boolean isVisible){
        lineTv.setVisibility(isVisible?View.VISIBLE:View.GONE);
    }
}
