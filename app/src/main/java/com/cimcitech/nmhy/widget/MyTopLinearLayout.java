package com.cimcitech.nmhy.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2019/1/24.
 */

public class MyTopLinearLayout extends LinearLayout {
    private View divide_line_view;
    private TextView  port_Tv,workType_Tv,tv4,tv5;
    private ImageView status_Iv;
    private LinearLayout ll;
    public MyTopLinearLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mytoplinear_layout2, this);
        ll= (LinearLayout) findViewById(R.id.linearLayout);
        port_Tv = (TextView)findViewById(R.id.port_tv);
        workType_Tv = (TextView)findViewById(R.id.workType_tv);
        tv4 = (TextView)findViewById(R.id.textView4);
        tv5 = (TextView)findViewById(R.id.textView5);
        status_Iv = (ImageView) findViewById(R.id.status_iv);
        divide_line_view = findViewById(R.id.devide_line);
    }
    public void setStatusImage(Drawable drawable){
        status_Iv.setImageDrawable(drawable);
    }

    public void setText(String str1,String str2){
        port_Tv.setText(Html.fromHtml(str1));
        workType_Tv.setText(Html.fromHtml(str2));
    }

    public void setWorkTypeBg(Drawable drawable){
        workType_Tv.setBackground(drawable);
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
        }else{
            tv5.setVisibility(View.GONE);
        }
    }

    public void isDividelineVisible(boolean b){
        divide_line_view.setVisibility(b? View.VISIBLE : View.GONE);
    }
}
