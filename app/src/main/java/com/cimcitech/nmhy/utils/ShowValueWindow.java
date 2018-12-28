package com.cimcitech.nmhy.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2018/12/19.
 */

public class ShowValueWindow implements View.OnClickListener{
    private PopupWindow pop = null;
    private TextView content_Tv;
    private TextView title_Tv;
    private TextView yes_type_Tv;
    private TextView no_type_Tv;
    private TextView cancel_Tv;
    /*
    *   各参数的含义：
    *   context：上下文
    *   title: 标题
    *   str1：第一个item
    *   str2: 第二个item
    *   tv: 被点击的对象
    * */
    public ShowValueWindow(Context context, String title,String str1,String str2,TextView tv) {
        content_Tv = tv;
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_choice_view, null);
        view.getBackground().setAlpha(230);//0-255,0完全透明
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        title_Tv = view.findViewById(R.id.pop_title_tv);
        yes_type_Tv = view.findViewById(R.id.yes_type_tv);
        no_type_Tv = view.findViewById(R.id.no_type_tv);
        cancel_Tv = view.findViewById(R.id.cancel_tv);
        title_Tv.setText(title);
        yes_type_Tv.setText(str1);
        no_type_Tv.setText(str2);
        yes_type_Tv.setOnClickListener(this);
        no_type_Tv.setOnClickListener(this);
        cancel_Tv.setOnClickListener(this);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes_type_tv:
                pop.dismiss();
                content_Tv.setText(yes_type_Tv.getText().toString());
                break;
            case R.id.no_type_tv:
                pop.dismiss();
                content_Tv.setText(no_type_Tv.getText().toString());
                break;
            case R.id.cancel_tv:
                pop.dismiss();
                break;
        }
    }

    //对话框显示的位置
    public void show(){
        pop.showAtLocation(content_Tv, Gravity.CENTER, 0, 0);
    }

}
