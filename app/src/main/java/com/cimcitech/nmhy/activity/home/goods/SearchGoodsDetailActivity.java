package com.cimcitech.nmhy.activity.home.goods;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.ApkApplication;
import com.cimcitech.nmhy.adapter.goods.SearchGoodsAdapter;
import com.cimcitech.nmhy.bean.goods.SearchGoodsDataBean;
import com.cimcitech.nmhy.bean.goods.SearchGoodsReq;
import com.cimcitech.nmhy.bean.goods.SearchGoodsVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.MyBaseActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by qianghe on 2019/1/23.
 */

public class SearchGoodsDetailActivity extends MyBaseActivity {
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_cl)
    CoordinatorLayout content_Cl;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;

    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;

    private Context mContext = SearchGoodsDetailActivity.this;
    private long cargoTransdemandDetailId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods_detail);
        ButterKnife.bind(this);
        initTitle();

        cargoTransdemandDetailId = getIntent().getLongExtra("cargoTransdemandDetailId",0);
        getData();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_search_goods_detail));
        if(NetWorkUtil.isConn(mContext)){//有网络
            empty_Rl.setVisibility(View.GONE);
            content_Cl.setVisibility(View.VISIBLE);
        }else{//没有网络
            empty_Rl.setVisibility(View.VISIBLE);
            content_Cl.setVisibility(View.GONE);
        }

        more_Tv.setVisibility(View.GONE);
        popup_menu_Ll.setVisibility(View.GONE);
    }

    public void getData() {
        OkHttpUtils
                .post()
                .url(Config.query_goods_detail_url)
                .addParams("cargoTransdemandDetailId",cargoTransdemandDetailId + "")
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.showNetError();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("heqint","response is: " + response);
                            }
                        }
                );
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
