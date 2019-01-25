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

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.home.oil.OilReportActivity;
import com.cimcitech.nmhy.adapter.plan.ShipPlanAdapter;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.plan.ShipBean;
import com.cimcitech.nmhy.bean.plan.ShipPlanReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Layout;

    private static final String TAG = "shipPlanlog";
    private ShipPlanAdapter adapter = null;
    private Context mContext = ShipPlanActivity.this;
    private List<ShipPlanVo.DataBean> data = new ArrayList<>();
    private Handler handler = new Handler();
    private int pageNum = 1;
    private ShipPlanVo shipPlanVo = null;
    private CatLoadingView mCatLoadingView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(this);
        initTitle();

        getData();

    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_plan));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
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
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        OkHttpUtils
                .post()
                .url(Config.query_voyage_plan_url)
                .addParams("userId",10678 + "")
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                mCatLoadingView.dismiss();
                                ToastUtil.showNetError();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                mCatLoadingView.dismiss();
                                shipPlanVo = new Gson().fromJson(response, ShipPlanVo.class);
                                if (shipPlanVo != null) {
                                    if (shipPlanVo.isSuccess()) {
                                        if (shipPlanVo.getData() != null && shipPlanVo.getData().size() > 0) {
                                            for (int i = 0; i < shipPlanVo.getData().size(); i++) {
                                                data.add(shipPlanVo.getData().get(i));
                                            }
                                            initAdapter();
                                        }
                                        adapter.setNotMoreData(true);
                                        adapter.notifyDataSetChanged();
                                        adapter.notifyItemRemoved(adapter.getItemCount());
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
                ShipPlanVo.DataBean bean = data.get(position);
                ArrayList<ShipPlanVo.DataBean.VoyageDynamicInfosBean> voyageDynamicInfosBean =
                        bean.getVoyageDynamicInfos();
                Intent i = new Intent(mContext,ShipPlanDetailActivity4.class);
                i.putParcelableArrayListExtra("voyageDynamicInfosBean",voyageDynamicInfosBean);
                startActivity(i);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });
    }
}
