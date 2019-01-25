package com.cimcitech.nmhy.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2019/1/24.
 */

public class MyTopLinearLayout extends LinearLayout {
    private TextView  tv1,tv2;
    private LinearLayout ll;
    public MyTopLinearLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mytoplinear_layout, this);
        ll= (LinearLayout) findViewById(R.id.linearLayout);
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
    }

    public void setText(String str1,String str2){
        tv1.setText(str1);
        tv2.setText(str2);
    }

    public void setBackground(int color){
        ll.setBackgroundColor(color);
    }

    public void setLinearMarginTop(int dimen){

    }
}
