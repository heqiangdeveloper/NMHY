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

public class OilReportHistoryDetailActivity extends MyBaseActivity {
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
    @Bind(R.id.realStoreQty_tv)
    TextView realStoreQty_Tv;
    @Bind(R.id.realyAmount_tv)
    TextView realyAmount_Tv;
    @Bind(R.id.taxfreeRealyAmount_tv)
    TextView taxfreeRealyAmount_Tv;
    @Bind(R.id.addFuelQty_tv)
    TextView addFuelQty_Tv;
    @Bind(R.id.addAmount_tv)
    TextView addAmount_Tv;
    @Bind(R.id.taxfreeAddAmount_tv)
    TextView taxfreeAddAmount_Tv;

    private Context mContext = OilReportHistoryDetailActivity.this;
    private long dynamicinfoId = 0;
    private List<OilReportHistoryDetailVo.DataBean.OilData> data = new ArrayList<>();
    private OilReportHistoryDetailVo oilReportHistoryDetailVo = null;
    private PopupWindow pop = null;
    private CatLoadingView mView = null;//Loading dialog
    private boolean isAdd = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_report_detail);
        ButterKnife.bind(this);

        dynamicinfoId = getIntent().getLongExtra("dynamicinfoId",-1);
        isAdd = getIntent().getBooleanExtra("isAdd",false);
        initTitle();
        initView();
    }

    public void initTitle(){
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
        if(isAdd){//新增
            titleName_Tv.setText(getResources().getString(R.string.add_oil_report_detail_label));
            showContent();
            commit_Bt.setVisibility(View.VISIBLE);
        }else {//查看
            titleName_Tv.setText(getResources().getString(R.string.query_oil_report_detail_label));
            showEmpty();
            commit_Bt.setVisibility(View.GONE);
            if(NetWorkUtil.isConn(OilReportHistoryDetailActivity.this)){
                mView = new CatLoadingView();
                mView.setCancelable(false);
                getData();
            }
        }
    }

    public void initView(){

    }

    public void showWhiteIcon(){
        setDrawable(unit_Tv);
        setDrawable(realyAmount_Tv);setDrawable(realStoreQty_Tv);setDrawable(taxfreeRealyAmount_Tv);
        setDrawable(addAmount_Tv);setDrawable(addFuelQty_Tv);setDrawable(taxfreeAddAmount_Tv);
    }

    public void setDrawable(TextView tv){
        tv.setCompoundDrawables(null,null,null,null);
        tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.drawable_padding_size));
    }

    public void getData(){
        mView.show(getSupportFragmentManager(),"");
        String json = new Gson().toJson(new OilReq(1,10, "",new OilReq.ShipFualDynamicInfosubBean(dynamicinfoId)));
        OkHttpUtils
                .postString()
                .url(Config.oil_report_history_detail_url)
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
                        oilReportHistoryDetailVo =new Gson().fromJson(response, OilReportHistoryDetailVo.class);
                        if (oilReportHistoryDetailVo != null) {
                            if (oilReportHistoryDetailVo.isSuccess()) {
                                if (oilReportHistoryDetailVo.getData().getList() != null && oilReportHistoryDetailVo.getData().getList().size() > 0) {
                                    showContent();
                                    for (int i = 0; i < oilReportHistoryDetailVo.getData().getList().size(); i++) {
                                        data.add(oilReportHistoryDetailVo.getData().getList().get(i));
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

    public void initData(OilReportHistoryDetailVo.DataBean.OilData oilData){
        if(oilData != null){
            fuelKind_Tv.setText(oilData.getFuelKind());
            unit_Tv.setText(oilData.getUnit());
            realStoreQty_Tv.setText(oilData.getRealStoreQty() + "");
            realyAmount_Tv.setText(oilData.getRealyAmount() + "");
            taxfreeRealyAmount_Tv.setText(oilData.getTaxfreeRealyAmount() + "");
            addFuelQty_Tv.setText(oilData.getAddFuelQty() + "");
            addAmount_Tv.setText(oilData.getAddAmount() + "");
            taxfreeAddAmount_Tv.setText(oilData.getTaxfreeAddAmount() + "");
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
