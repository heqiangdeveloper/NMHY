package com.cimcitech.nmhy.activity.home.oil;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.adapter.all.PopupWindowAdapter;
import com.cimcitech.nmhy.bean.oil.OilReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryDetailVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.widget.MyBaseActivity;
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

/**
 * Created by qianghe on 2018/12/24.
 */

public class OilRequestHistoryDetailActivity extends MyBaseActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;

    @Bind(R.id.commit_bt)
    Button commit_Bt;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_ll)
    LinearLayout content_Ll;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;

    @Bind(R.id.fuelKind_tv)
    TextView fuelKind_Tv;
    @Bind(R.id.unit_tv)
    TextView unit_Tv;
    @Bind(R.id.estimateQty_tv)
    TextView estimateQty_Tv;
    @Bind(R.id.estimatePrice_tv)
    TextView estimatePrice_Tv;
    @Bind(R.id.estimateAmount_tv)
    TextView estimateAmount_Tv;

    @Bind(R.id.taxId_ll)
    LinearLayout taxId_Ll;
    @Bind(R.id.taxRate_ll)
    LinearLayout taxRate_Ll;

    private Context mContext = OilRequestHistoryDetailActivity.this;
    private int applyId = 0;
    private List<OilRequestHistoryDetailVo.DataBean.OilData> data = new ArrayList<>();
    private OilRequestHistoryDetailVo oilRequestHistoryDetailVo = null;
    private PopupWindow pop = null;
    private CatLoadingView mView = null;//Loading dialog

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_request_detail);
        ButterKnife.bind(this);

        applyId = getIntent().getIntExtra("applyId",-1);
        initTitle();
        initView();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_oil_request_history_detail));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
    }

    public void initView(){
        commit_Bt.setVisibility(View.GONE);
        taxId_Ll.setVisibility(View.GONE);
        taxRate_Ll.setVisibility(View.GONE);
        showEmpty();
        if(NetWorkUtil.isConn(mContext)){//有网络
            mView = new CatLoadingView();
            mView.setCancelable(false);
            getData();
        }
    }

    public void showWhiteIcon(){
        setDrawable(unit_Tv);
        setDrawable(estimateAmount_Tv);setDrawable(estimatePrice_Tv);setDrawable(estimateQty_Tv);
    }

    public void setDrawable(TextView tv){
        tv.setCompoundDrawables(null,null,null,null);
        tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.drawable_padding_size));
    }

    public void getData(){
        mView.show(getSupportFragmentManager(),"");
        String json = new Gson().toJson(new OilReq(1,10, "",new OilReq.FuelApplyDetailBean(applyId)));
        OkHttpUtils
                .postString()
                .url(Config.oil_request_history_detail_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.dismiss();
                        Log.d(TAG,"exception: " + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mView.dismiss();
                        oilRequestHistoryDetailVo =new Gson().fromJson(response, OilRequestHistoryDetailVo.class);
                        if (oilRequestHistoryDetailVo != null) {
                            if (oilRequestHistoryDetailVo.isSuccess()) {
                                if (oilRequestHistoryDetailVo.getData().getList() != null && oilRequestHistoryDetailVo.getData().getList().size() > 0) {
                                    showContent();
                                    for (int i = 0; i < oilRequestHistoryDetailVo.getData().getList().size(); i++) {
                                        data.add(oilRequestHistoryDetailVo.getData().getList().get(i));
                                        initData(data.get(0));
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void showEmpty(){
        empty_Rl.setVisibility(View.VISIBLE);
        content_Ll.setVisibility(View.GONE);
    }

    public void showContent(){
        empty_Rl.setVisibility(View.GONE);
        content_Ll.setVisibility(View.VISIBLE);
        showWhiteIcon();
    }

    public void initData(OilRequestHistoryDetailVo.DataBean.OilData oilData){
        if(oilData != null){
            fuelKind_Tv.setText(oilData.getFuelKind());
            unit_Tv.setText(oilData.getUnit());
            estimateAmount_Tv.setText(oilData.getEstimateAmount() + "");
            estimatePrice_Tv.setText(oilData.getEstimatePrice() + "");
            estimateQty_Tv.setText(oilData.getEstimateQty() + "");
        }
    }

    @OnClick({R.id.back_iv,R.id.fuelKind_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.fuelKind_tv:
                String fuelKindTitle = mContext.getResources().getString(R.string.choice_label) +
                        mContext.getResources().getString(R.string.fuelKind_label);
                List<String> contentList = new ArrayList<>();
                for(int i = 0; i < data.size(); i++){
                    contentList.add(data.get(i).getFuelKind());
                }
                showPopWindow(fuelKindTitle,contentList);
                pop.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
        }
    }

    public void showPopWindow(String title,List<String> list){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_add_client_view, null);
        view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        View pop_reward_view = view.findViewById(R.id.pop_reward_view);
        TextView title_tv = view.findViewById(R.id.title_tv);
        title_tv.setText(title);
        final PopupWindowAdapter adapter = new PopupWindowAdapter(mContext, list);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initData(data.get(position));
                pop.dismiss();
            }
        });
    }
}
