package com.cimcitech.nmhy.activity.home.goods;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ScrollView;
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
import com.cimcitech.nmhy.utils.ShowValueWindow;
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

public class SearchGoodsActivity extends MyBaseActivity {
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_cl)
    CoordinatorLayout content_Cl;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.search_bt)
    Button search_Bt;
    @Bind(R.id.search_et)
    EditText search_Et;
    @Bind(R.id.status_bt)
    Button status_Bt;

    private Context mContext = SearchGoodsActivity.this;
    private SearchGoodsAdapter adapter;
    private Handler handler = new Handler();
    private boolean isLoading = false;
    private int pageNum = 1;
    private SearchGoodsVo searchGoodsVo;
    private List<SearchGoodsDataBean.ListBean> data = new ArrayList<>();
    private String status = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods);
        ButterKnife.bind(this);
        initTitle();
        initViewData();
        updateData();
    }
    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_search_goods));
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

    public void initViewData() {
        /*
        *   货物运输状态：0 未开始；2 正在运； 3 已运完
        * */
        String statusText = status_Bt.getText().toString().trim();
        status = Config.goodsStatusMap.get(statusText);
        adapter = new SearchGoodsAdapter(mContext, data);
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
                        getData(); //获取数据
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
                /*int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }*/
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
                                if (searchGoodsVo.getData().isHasNextPage()) {
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
        adapter.setOnItemClickListener(new SearchGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchGoodsDataBean.ListBean listBean = (SearchGoodsDataBean.ListBean) adapter.getAll().get(position);
                String fstatus = Config.goodsStatusMap.get(status_Bt.getText().toString().trim());
                Intent intent = new Intent(mContext, SearchGoodsDetailActivity2.class);
                intent.putExtra("fStatus",fstatus);
                intent.putExtra("cargoTransdemandDetailId", listBean.getCargoTransdemandDetailId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {
            }
        });
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
        getData();
    }

    @OnClick({R.id.back_iv,R.id.search_bt,R.id.status_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_bt:
                updateData();
                ApkApplication.imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.status_bt:
                String unitTitle = mContext.getResources().getString(R.string.choice_goods_status_label) ;
                List<String> goodsStatusList = new ArrayList<>();
                goodsStatusList.add(mContext.getResources().getString(R.string.unLoad_label));
                goodsStatusList.add(mContext.getResources().getString(R.string.loading_label));
                goodsStatusList.add(mContext.getResources().getString(R.string.loaded_label));
                ShowListValueWindow unitWindow = new ShowListValueWindow(mContext,unitTitle, goodsStatusList,status_Bt);
                unitWindow.show();
                break;
        }
    }

    public void getData() {
        //模糊查询： 捆包号 和 牌号
        String codeOrName = search_Et.getText().toString().trim();
        String fstatus = Config.goodsStatusMap.get(status_Bt.getText().toString().trim());
        String json = new Gson().toJson(new SearchGoodsReq(1,10,"",new SearchGoodsReq.CargoTransdemandDetail(codeOrName,fstatus)));
        OkHttpUtils
                .postString()
                .url(Config.query_goods_url)
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
                                Log.d("heqint","response is: " + response);
                                swipeRefreshLayout.setRefreshing(false);
                                searchGoodsVo = new Gson().fromJson(response, SearchGoodsVo.class);
                                if (searchGoodsVo != null) {
                                    if (searchGoodsVo.isSuccess()) {
                                        if (searchGoodsVo.getData().getList() != null && searchGoodsVo.getData().getList().size() > 0) {
                                            for (int i = 0; i < searchGoodsVo.getData().getList().size(); i++) {
                                                data.add(searchGoodsVo.getData().getList().get(i));
                                            }
                                        }
                                        if (searchGoodsVo.getData().isHasNextPage()) {
                                            adapter.setNotMoreData(false);
                                        } else {
                                            adapter.setNotMoreData(true);
                                        }
                                        adapter.notifyDataSetChanged();
                                        swipeRefreshLayout.setRefreshing(false);
                                        adapter.notifyItemRemoved(adapter.getItemCount());
                                    }
                                } else {
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                );
    }
}
