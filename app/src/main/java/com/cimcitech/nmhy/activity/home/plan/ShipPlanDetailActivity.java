package com.cimcitech.nmhy.activity.home.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.home.oil.OilReportActivity;
import com.cimcitech.nmhy.adapter.plan.ShipPlanAdapter;
import com.cimcitech.nmhy.bean.plan.ShipPlanReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.EnumUtil;
import com.cimcitech.nmhy.utils.ToastUtil;
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


public class ShipPlanDetailActivity extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Layout;
    @Bind(R.id.voyageNo_tv)
    TextView voyageNo_Tv;
    @Bind(R.id.routeName_tv)
    TextView routeName_Tv;
    @Bind(R.id.portTransportOrder_tv)
    TextView portTransportOrder_Tv;
    @Bind(R.id.owerCompName_tv)
    TextView owerCompName_Tv;
    @Bind(R.id.shipCompName_tv)
    TextView shipCompName_Tv;
    @Bind(R.id.rentType_tv)
    TextView rentType_Tv;
    @Bind(R.id.distance_tv)
    TextView distance_Tv;

    private static final String TAG = "shipPlanlog";
    private ShipPlanVo.DataBean.ListBean shipDetailData = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_plan_detail);
        ButterKnife.bind(this);

        shipDetailData = (ShipPlanVo.DataBean.ListBean)getIntent().getParcelableExtra("shipDetailData");
        initTitle();
        initContent();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.ship_plan_detail_title));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
        popup_menu_Layout.setVisibility(View.GONE);
    }

    public void initContent(){
        if(null != shipDetailData){
            voyageNo_Tv.setText(shipDetailData.getVoyageNo() + "");
            routeName_Tv.setText(shipDetailData.getRouteName() + "");
            portTransportOrder_Tv.setText(shipDetailData.getPortTransportOrder() + "");
            owerCompName_Tv.setText(shipDetailData.getOwerCompName() + "");
            shipCompName_Tv.setText(shipDetailData.getShipCompName() + "");
            String rentTypeValue = "未知类型";
            rentTypeValue = EnumUtil.findValueByKeySS(Config.rentTypeMap,shipDetailData.getRentType());
            rentType_Tv.setText(rentTypeValue);
            distance_Tv.setText(shipDetailData.getDistance() + "");
        }
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
