package com.cimcitech.nmhy.activity.home.oil;

import com.cimcitech.nmhy.adapter.oil.OilRequestHistoryAdapter;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryVo;
import com.cimcitech.nmhy.widget.MyBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.adapter.oil.OilReportHistoryAdapter;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.utils.Config;
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

/**
 * Created by qianghe on 2018/12/20.
 */

public class OilRequestHistoryActivity extends MyBaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view_layout)
    CoordinatorLayout recyclerViewLayout;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Layout;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;

    private int pageNum = 1;
    private OilRequestHistoryAdapter adapter;
    private boolean isLoading;
    private List<OilRequestHistoryVo.DataBean.ListBean> data = new ArrayList<>();
    private Handler handler = new Handler();
    private String TAG = "oillog";
    private OilRequestHistoryVo oilRequestHistoryVo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_report_history);
        ButterKnife.bind(this);
        initTitle();
        initPopupMenu();
        initViewData();
        updateData();
    }

    public void initTitle(){
        more_Tv.setVisibility(View.VISIBLE);
        titleName_Tv.setText(getResources().getString(R.string.item_oil_request_history));
    }

    public void initPopupMenu(){
        popup_menu_Layout.setVisibility(View.GONE);
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
        adapter.notifyDataSetChanged();
        this.data.clear();
        pageNum = 1;
        getData(); //获取数据
    }

    @OnClick({R.id.back_iv, R.id.add_ib})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.add_ib:
                break;
        }
    }

    public void initViewData() {
        adapter = new OilRequestHistoryAdapter(OilRequestHistoryActivity.this, data);
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
                        pageNum = 1;
                        isLoading = false;
                        getData();
                    }
                }, 1000);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(OilRequestHistoryActivity.this);
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
                                if (oilRequestHistoryVo.getData().isHasNextPage()) {
                                    pageNum++;
                                    getData();//添加数据
                                }
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
        //给List添加点击事件
        adapter.setOnItemClickListener(new OilRequestHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OilRequestHistoryVo.DataBean.ListBean bean = (OilRequestHistoryVo.DataBean.ListBean) adapter.getAll().get(position);
                Intent intent = new Intent(OilRequestHistoryActivity.this, OilRequestHistoryDetailActivity.class);
                intent.putExtra("applyId", bean.getApplyId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {
                //长按
            }
        });
    }
    public void getData() {
        String json = new Gson().toJson(new OilReportHistoryReq(pageNum, 10, ""));

        Log.e(TAG, "oilreporthistory request: " + json);
        OkHttpUtils
                .postString()
                .url(Config.oil_request_history_url)
                //.addHeader("checkTokenKey", Config.TOKEN)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                swipeRefreshLayout.setRefreshing(false);
                                ToastUtil.showNetError();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG,"oilreporthistory response is: " + response);

                                oilRequestHistoryVo = new Gson().fromJson(response, OilRequestHistoryVo.class);
                                if (oilRequestHistoryVo != null) {
                                    if (oilRequestHistoryVo.isSuccess()) {
                                        if (oilRequestHistoryVo.getData().getList() != null && oilRequestHistoryVo.getData().getList().size() > 0) {
                                            for (int i = 0; i < oilRequestHistoryVo.getData().getList().size(); i++) {
                                                data.add(oilRequestHistoryVo.getData().getList().get(i));
                                            }
                                        }
                                        if (oilRequestHistoryVo.getData().isHasNextPage()) {
                                            adapter.setNotMoreData(false);
                                        } else {
                                            adapter.setNotMoreData(true);
                                        }
                                        adapter.notifyDataSetChanged();
                                        adapter.notifyItemRemoved(adapter.getItemCount());
                                    }
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                );
    }
}
