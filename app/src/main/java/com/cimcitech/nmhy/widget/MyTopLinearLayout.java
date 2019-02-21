package com.cimcitech.nmhy.widget;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2019/1/24.
 */

public class MyTopLinearLayout extends LinearLayout {
    private View divide_line_view;
    private TextView  tv1,tv2,tv4,tv5,tv6;
    private LinearLayout ll;
    public MyTopLinearLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mytoplinear_layout, this);
        ll= (LinearLayout) findViewById(R.id.linearLayout);
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv4 = (TextView)findViewById(R.id.textView4);
        tv5 = (TextView)findViewById(R.id.textView5);
        tv6 = (TextView)findViewById(R.id.textView6);
        divide_line_view = findViewById(R.id.devide_line);
    }

    public void setText(String str1,String str2){
        tv1.setText(Html.fromHtml(str1));
        tv2.setText(Html.fromHtml(str2));
    }

    public void setTimeLabel(String str1){
        tv4.setText(str1);
    }

    public void setBackground(int color){
        ll.setBackgroundColor(color);
    }

    public void isCommandAndExceptionTvVisible(boolean b){
        if(b){
            tv5.setVisibility(View.VISIBLE);
            tv6.setVisibility(View.VISIBLE);
        }else{
            tv5.setVisibility(View.GONE);
            tv6.setVisibility(View.GONE);
        }
    }

    public void isDividelineVisible(boolean b){
        divide_line_view.setVisibility(b? View.VISIBLE : View.GONE);
    }
}
