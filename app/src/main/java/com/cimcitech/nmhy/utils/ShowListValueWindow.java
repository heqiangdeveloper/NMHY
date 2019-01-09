package com.cimcitech.nmhy.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.adapter.all.PopupWindowAdapter;
import com.cimcitech.nmhy.adapter.oil.OilReportHistoryAdapter;

import java.util.List;

/**
 * Created by qianghe on 2018/12/19.
 */

public class ShowListValueWindow implements AdapterView.OnItemClickListener{
    private PopupWindow pop = null;
    private TextView content_Tv;
    private TextView title_Tv;
    private List<String> mList;
    private int index = -1;
    /*
    *   各参数的含义：
    *   context：上下文
    *   title: 标题
    *   list: 传入的List数组,用于显示
    *   tv: 被点击的对象
    * */
    public ShowListValueWindow(Context context, String title, final List<String> list, TextView tv) {
        content_Tv = tv;
        mList = list;
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_add_client_view, null);
        view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        View pop_reward_view = view.findViewById(R.id.pop_reward_view);
        TextView title_tv = view.findViewById(R.id.title_tv);
        title_tv.setText(title);
        final PopupWindowAdapter adapter = new PopupWindowAdapter(context, list);
        ListView listView = view.findViewById(R.id.listContent);
        listView.setAdapter(adapter);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop_reward_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        content_Tv.setText(mList.get(position));
        index = position;
        pop.dismiss();
    }

    //对话框显示的位置
    public void show(){
        pop.showAtLocation(content_Tv, Gravity.CENTER, 0, 0);
    }
}
