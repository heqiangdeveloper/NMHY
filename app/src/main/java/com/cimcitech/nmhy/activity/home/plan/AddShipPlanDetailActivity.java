package com.cimcitech.nmhy.activity.home.plan;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.plan.ShipPlanDetailReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanDetailVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
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


public class AddShipPlanDetailActivity extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.portName_tv)
    TextView portName_Tv;
    @Bind(R.id.jobTypeValue_tv)
    TextView jobTypeValue_Tv;
    @Bind(R.id.voyageStatusDesc_tv)
    TextView voyageStatusDesc_Tv;
    @Bind(R.id.reason_tv)
    EditText reason_Tv;
    @Bind(R.id.estimatedTime_tv)
    TextView estimatedTime_Tv;
    @Bind(R.id.occurTime_tv)
    TextView occurTime_Tv;
    @Bind(R.id.reportTime_tv)
    TextView reportTime_Tv;
    @Bind(R.id.location_tv)
    TextView location_Tv;
    @Bind(R.id.longitude_tv)
    TextView longitude_Tv;
    @Bind(R.id.latitude_tv)
    TextView latitude_Tv;
    @Bind(R.id.speed_tv)
    EditText speed_Tv;
    @Bind(R.id.weather_tv)
    EditText weather_Tv;
    @Bind(R.id.feedback_tv)
    EditText feedback_Tv;
    @Bind(R.id.remark_tv)
    EditText remark_Tv;

    private Context mContext = AddShipPlanDetailActivity.this;
    private List<ShipPlanDetailVo.DataBean.ListBean> data = new ArrayList<>();
    private int pageNum = 1;
    private ShipPlanDetailVo shipPlanDetailVo = null;
    private ShipPlanDetailVo.DataBean.ListBean item = null;
    private CatLoadingView mCatLoadingView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ship_plan_detail);
        ButterKnife.bind(this);

        item = (ShipPlanDetailVo.DataBean.ListBean)getIntent().getSerializableExtra("item");
        initTitle();
        initData();
        //getData();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.ship_plan_detail_title));
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
        String json = new Gson().toJson(new ShipPlanDetailReq(pageNum,10,"",new ShipPlanDetailReq
                .VoyageDynamicInfoBean(-1)));
        OkHttpUtils
                .postString()
                .url(Config.query_voyage_plan_detail_url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
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
                                shipPlanDetailVo = new Gson().fromJson(response, ShipPlanDetailVo.class);
                                if (shipPlanDetailVo != null) {
                                    if (shipPlanDetailVo.isSuccess()) {
                                        if (shipPlanDetailVo.getData().getList() != null && shipPlanDetailVo.getData().getList().size() > 0) {
                                            for (int i = 0; i < shipPlanDetailVo.getData().getList().size(); i++) {
                                                data.add(shipPlanDetailVo.getData().getList().get(i));
                                            }
                                            initData();
                                        }
                                    }
                                } else {

                                }
                            }
                        }
                );
    }

    public void initData(){
        portName_Tv.setText(item.getPortName());
        jobTypeValue_Tv.setText(item.getJobTypeValue());
        voyageStatusDesc_Tv.setText(item.getVoyageStatusDesc());

    }
}
