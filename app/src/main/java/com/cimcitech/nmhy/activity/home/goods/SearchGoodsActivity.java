package com.cimcitech.nmhy.activity.home.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.widget.MyBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianghe on 2019/1/23.
 */

public class SearchGoodsActivity extends MyBaseActivity {
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_sv)
    ScrollView content_Sv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;

    private Context mContext = SearchGoodsActivity.this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods);
        ButterKnife.bind(this);
        initTitle();
    }
    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_search_goods));
        if(NetWorkUtil.isConn(mContext)){//有网络
            empty_Rl.setVisibility(View.GONE);
            content_Sv.setVisibility(View.VISIBLE);
        }else{//没有网络
            empty_Rl.setVisibility(View.VISIBLE);
            content_Sv.setVisibility(View.GONE);
        }

        more_Tv.setVisibility(View.GONE);
        popup_menu_Ll.setVisibility(View.GONE);
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
