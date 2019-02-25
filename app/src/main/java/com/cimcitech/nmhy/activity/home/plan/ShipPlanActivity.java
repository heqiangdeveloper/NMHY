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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.home.oil.OilReportActivity;
import com.cimcitech.nmhy.adapter.plan.ShipPlanAdapter;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.plan.ChangeVoyagePlanReq;
import com.cimcitech.nmhy.bean.plan.ShipBean;
import com.cimcitech.nmhy.bean.plan.ShipPlanReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.EventBusMessage;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;


public class ShipPlanActivity extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.content_cl)
    CoordinatorLayout content_Cl;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Layout;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.warn_tv)
    TextView warn_Tv;

    private static final String TAG = "shipPlanlog";
    private ShipPlanAdapter adapter = null;
    private Context mContext = ShipPlanActivity.this;
    private List<ShipPlanVo.DataBean> data = new ArrayList<>();
    private Handler handler = new Handler();
    private ShipPlanVo shipPlanVo = null;
    private CatLoadingView mCatLoadingView = null;
    private boolean isLoading;
    //是否有已经开始的航次计划
    private boolean isHasPlanStart = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan2);
        ButterKnife.bind(this);
        //注册EventBus订阅者
        EventBus.getDefault().register(this);
        initTitle();

        initViewData();
        updateData();
    }

    //订阅者 方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        if(event.getMessage().equals("addShipPlanSuc")){
            Log.d("addShipPlanlog","shipPlanActivity receive addShipPlanSuc...");
            getData();
        }else if(event.getMessage().equals("refreshShipPlan")){
            Log.d("refreshShipPlanlog","shipPlanActivity receive addShipPlanSuc...");
            finish();
        }
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_plan));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
    }

    public void initViewData() {
        if(!NetWorkUtil.isConn(mContext)){//没有网络
            empty_Rl.setVisibility(View.VISIBLE);
            warn_Tv.setText(getResources().getString(R.string.no_network_warn));
            content_Cl.setVisibility(View.GONE);
        }else{//有网络
            empty_Rl.setVisibility(View.GONE);
            content_Cl.setVisibility(View.VISIBLE);
            adapter = new ShipPlanAdapter(mContext, data);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //下拉刷新
                            adapter.notifyDataSetChanged();
                            data.clear(); //清除数据
                            isLoading = false;
                            getData();
                        }
                    }, 1000);
                }
            });
            final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                            ? 0 : recyclerView.getChildAt(0).getTop();
                    if (topRowVerticalPosition > 0) {
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                        if (isRefreshing) {
                            return;
                        }

                        if (!isLoading) {
                            isLoading = true;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //上拉加载
                                    getData();
                                    isLoading = false;
                                }
                            }, 1000);
                        }
                    }
                }
            });
            //给List添加点击事件
            adapter.setOnItemClickListener(new ShipPlanAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String statusStr = Config.fStatusList.get(2);
                    for(int k = 0; k < data.size(); k++){
                        if(data.get(k).getFstatus().equals(statusStr)){
                            isHasPlanStart = true;
                            break;
                        }
                    }
                    ShipPlanVo.DataBean bean = data.get(position);
                    ArrayList<ShipPlanVo.DataBean.VoyageDynamicInfosBean> voyageDynamicInfosBean =
                            bean.getVoyageDynamicInfos();
                    ChangeVoyagePlanReq req = new ChangeVoyagePlanReq(
                            bean.getVoyagePlanId(),
                            bean.getBargeBatchNo(),
                            bean.getcShipName(),
                            bean.getActualSailingTime(),
                            bean.getActualStopTime(),
                            bean.getEstimatedStopTime(),
                            bean.getEstimatedSailingTime(),
                            bean.getPortTransportOrder(),
                            bean.getBargeId(),
                            bean.getContractId(),
                            bean.getVoyageNo(),
                            bean.getRouteId(),
                            bean.getRouteName(),
                            bean.getFstatus()
                    );
                    Intent i = new Intent(mContext,ShipPlanDetailActivity4.class);
                    //每个节点的航次状态数据
                    i.putParcelableArrayListExtra("voyageDynamicInfosBean",voyageDynamicInfosBean);
                    i.putExtra("req",req);//更改本航次状态所需要的数据
                    i.putExtra("fstatus",bean.getFstatus());//本航次的航次状态
                    i.putExtra("isHasPlanStart",isHasPlanStart);//是否有已经开始的航次计划
                    startActivity(i);
                }

                @Override
                public void onItemLongClickListener(View view, int position) {
                    //长按
                }
            });
        }
    }

    //刷新数据
    private void updateData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        //清除数据
        if(adapter != null) adapter.notifyDataSetChanged();
        this.data.clear();
        getData(); //获取数据
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
        }
    }

    public void getData() {
        //mCatLoadingView = new CatLoadingView();
        //mCatLoadingView.show(getSupportFragmentManager(),"");
        OkHttpUtils
                .post()
                .url(Config.query_voyage_plan_url)
                .addParams("userId",Config.accountId + "")
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                //mCatLoadingView.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                                ToastUtil.showNetError();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                swipeRefreshLayout.setRefreshing(false);
                                shipPlanVo = new Gson().fromJson(response, ShipPlanVo.class);
                                if (shipPlanVo != null) {
                                    if (shipPlanVo.isSuccess()) {
                                        if (shipPlanVo.getData() != null && shipPlanVo.getData().size() > 0) {
                                            data.clear();
                                            for (int i = 0; i < shipPlanVo.getData().size(); i++) {
                                                data.add(shipPlanVo.getData().get(i));
                                            }
                                            //initAdapter();
                                        }else{
                                            empty_Rl.setVisibility(View.VISIBLE);
                                            warn_Tv.setText(getResources().getString(R.string.no_data_warn));
                                            content_Cl.setVisibility(View.GONE);
                                        }
                                        if(adapter != null){
                                            adapter.setNotMoreData(true);
                                            adapter.notifyDataSetChanged();
                                            adapter.notifyItemRemoved(adapter.getItemCount());
                                        }
                                    }else{
                                        empty_Rl.setVisibility(View.VISIBLE);
                                        warn_Tv.setText(getResources().getString(R.string.no_data_warn));
                                        content_Cl.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
    }

    public void initAdapter(){
        adapter = new ShipPlanAdapter(mContext,data);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ShipPlanAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                for(int k = 0; k < data.size(); k++){
                    if(data.get(k).getFstatus().equals(Config.fStatusList.get(2))){
                        isHasPlanStart = true;
                        break;
                    }
                }
                ShipPlanVo.DataBean bean = data.get(position);
                ArrayList<ShipPlanVo.DataBean.VoyageDynamicInfosBean> voyageDynamicInfosBean =
                        bean.getVoyageDynamicInfos();
                ChangeVoyagePlanReq req = new ChangeVoyagePlanReq(
                        bean.getVoyagePlanId(),
                        bean.getBargeBatchNo(),
                        bean.getcShipName(),
                        bean.getActualSailingTime(),
                        bean.getActualStopTime(),
                        bean.getEstimatedStopTime(),
                        bean.getEstimatedSailingTime(),
                        bean.getPortTransportOrder(),
                        bean.getBargeId(),
                        bean.getContractId(),
                        bean.getVoyageNo(),
                        bean.getRouteId(),
                        bean.getRouteName(),
                        bean.getFstatus()
                );
                Intent i = new Intent(mContext,ShipPlanDetailActivity4.class);
                //每个节点的航次状态数据
                i.putParcelableArrayListExtra("voyageDynamicInfosBean",voyageDynamicInfosBean);
                i.putExtra("req",req);//更改本航次状态所需要的数据
                i.putExtra("fstatus",bean.getFstatus());//本航次的航次状态
                i.putExtra("isHasPlanStart",isHasPlanStart);//是否有已经开始的航次计划
                startActivity(i);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
