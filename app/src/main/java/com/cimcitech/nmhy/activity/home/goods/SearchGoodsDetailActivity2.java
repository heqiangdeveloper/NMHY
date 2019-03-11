package com.cimcitech.nmhy.activity.home.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bin.david.form.core.SmartTable;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.goods.SearchGoodsDetailVo;
import com.cimcitech.nmhy.bean.plan.ChangeVoyagePlanReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DateTool;
import com.cimcitech.nmhy.utils.EventBusMessage;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.DateTimePickDialog;
import com.cimcitech.nmhy.widget.MyLinearLayout;
import com.cimcitech.nmhy.widget.MyPortLinearLayout;
import com.cimcitech.nmhy.widget.MyTopLinearLayout;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;


public class SearchGoodsDetailActivity2 extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Layout;
    @Bind(R.id.port_ll)
    LinearLayout port_Ll;
    @Bind(R.id.start_voyage_bt)
    Button start_voyage_Bt;
    @Bind(R.id.end_voyage_bt)
    Button end_voyage_Bt;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.warn_tv)
    TextView warn_Tv;
    @Bind(R.id.content_cl)
    CoordinatorLayout content_Cl;

    @Bind(R.id.detail_ll)
    LinearLayout detail_Ll;

    private ArrayList<SearchGoodsDetailVo.DataBean.VoyageDynamicInfosBean> data = new ArrayList<>();
    private CatLoadingView mCatLoadingView;
    private int cargoTransdemandDetailId;
    private SearchGoodsDetailVo searchGoodsDetailVo;
    private String fstatus = "2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail4);
        ButterKnife.bind(this);
        cargoTransdemandDetailId = getIntent().getIntExtra("cargoTransdemandDetailId",-1);
        fstatus = getIntent().getStringExtra("fStatus");
        //注册EventBus订阅者
        EventBus.getDefault().register(this);

        initTitle();
        hideView();
        setButtonVisibility();
        getData();
    }

    public void setButtonVisibility(){
        start_voyage_Bt.setVisibility(View.GONE);
        end_voyage_Bt.setVisibility(View.GONE);
    }

    //订阅者 方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        if(event.getMessage().equals("addShipPlanSuc")){
            Log.d("addShipPlanlog","shipplandetailactivity receive addShipPlanSuc...");
            finish();
        }
    }

    public void hideView(){
        port_Ll.setVisibility(View.GONE);
    }

    public void showView(){
        port_Ll.setVisibility(View.VISIBLE);
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_search_goods_detail));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
    }

    @OnClick({R.id.back_iv,R.id.start_voyage_bt,R.id.end_voyage_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.start_voyage_bt:

                break;
            case R.id.end_voyage_bt:
                break;
        }
    }

    public void getData(){
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        OkHttpUtils
                .post()
                .url(Config.query_goods_detail_url)
                .addParams("detailId",cargoTransdemandDetailId+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mCatLoadingView.dismiss();
                        ToastUtil.showNetError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mCatLoadingView.dismiss();
                        searchGoodsDetailVo = new Gson().fromJson(response, SearchGoodsDetailVo.class);
                        if (searchGoodsDetailVo != null) {
                            if (searchGoodsDetailVo.isSuccess()) {
                                if (searchGoodsDetailVo.getData() != null) {
                                    data = searchGoodsDetailVo.getData().getVoyageDynamicInfos();
                                } else {
                                    empty_Rl.setVisibility(View.VISIBLE);
                                    warn_Tv.setText(getResources().getString(R.string.no_data_warn));
                                    content_Cl.setVisibility(View.GONE);
                                }

                            } else {
                                empty_Rl.setVisibility(View.VISIBLE);
                                warn_Tv.setText(getResources().getString(R.string.no_data_warn));
                                content_Cl.setVisibility(View.GONE);
                            }
                        }
                        initData();
                    }
                });

    }

    public void initData(){
        if(data == null || data.size() == 0){
            empty_Rl.setVisibility(View.VISIBLE);
            warn_Tv.setText(getResources().getString(R.string.no_data_warn));
            content_Cl.setVisibility(View.GONE);
        }else{
            //码头列表
            List<String> portNameList = new ArrayList<>();
            //作业类型列表
            List<String> jobTypeValueList = new ArrayList<>();
            final int portId0 = data.get(0).getCurrPortId();
            portNameList.add(data.get(0).getPortName());

            int portIdi = -1;
            String portNameStr = "";
            for(int i=0;i<data.size();i++){
                portIdi = data.get(i).getCurrPortId();
                portNameStr = data.get(i).getPortName();
                //如果返回的数据中，有为空的港口名称，则显示异常页面
                if(portNameStr == null || portNameStr.length() == 0){
                    empty_Rl.setVisibility(View.VISIBLE);
                    warn_Tv.setText(getResources().getString(R.string.error_data_warn));
                    content_Cl.setVisibility(View.GONE);
                    return;
                }
                if(portIdi != portId0 && !portNameList.contains(portNameStr)){
                    portNameList.add(portNameStr);
                }
            }

            //如果返回的数据中，没有为空的港口名称，则显示正常页面
            empty_Rl.setVisibility(View.GONE);
            content_Cl.setVisibility(View.VISIBLE);
            showView();

            int sizen = portNameList.size();
            List<TextView> tvList = new ArrayList<>();
            List<TextView> portNameTvList = new ArrayList<>();
            List<TextView> jobTypeValueTvList = new ArrayList<>();
            ShipPlanVo.DataBean.VoyageDynamicInfosBean item = null;

            List<ShipPlanVo.DataBean.VoyageDynamicInfosBean> itemList = new ArrayList<>();

            String portNameStr1 = "";
            String portNameStr2 = "";
            a:for(int q =0; q < portNameList.size(); q++){
                portNameStr1 = portNameList.get(q);
                b:for(int r =0; r < data.size(); r++){
                    if(data.get(r).getPortName().equals(portNameStr1)){
                        jobTypeValueList.add(data.get(r).getJobTypeValue());
                        break b;
                    }
                }
            }

            //表内容部分
            MyLinearLayout mll = null;
            //码头名称，作业类型，表头部分
            MyTopLinearLayout mtll = null;
            //最顶部港口顺序部分
            MyPortLinearLayout mpll = null;
            String mPortNameStr = "";
            String mJobTypeStr = "";
            String currentPortName = "";
            String commandStr = "";
            String exceptionStr = "";
            SearchGoodsDetailVo.DataBean.VoyageDynamicInfosBean currentItem = null;
            //data.get(0).setReportTime("2019-02-24 12:23:59");
            //data.get(3).setEstimatedTime("2019-08-21");
            //data.get(7).setEstimatedTime("2019-02-03");

            //获取当前可以汇报的行号cPosition： 指的是在data中的位置
            int currentPosition = 0;
            for(int w = 0 ; w < data.size(); w++){
                if(data.get(w).getReportTime() == null || data.get(w).getReportTime().length() == 0){
                    currentPosition = w;
                    break;
                }
            }
            final int cPosition = currentPosition;

            //获取当前可以汇报的行号cPosition 在portNameList中的位置curPositionInPortNameList
            int curPositionInPortNameList = 0;
            for(int x =0; x < portNameList.size(); x++){
                if(portNameList.get(x).equals(data.get(cPosition).getPortName())){
                    curPositionInPortNameList = x;
                }
            }

            //已离港 ，设置顶部的的港口名文本背景色为disableColor
            //正在该港口下，设置顶部的的港口名文本背景色为 currentColor
            //待驶入的港口，设置顶部的的港口名文本背景色为 enableColor
            int disableColor = Color.rgb(255,127,80);//浅红  #FF7F50
            int currentColor = Color.rgb(34,139,34);//浅绿  #228B22
            int enableColor = Color.rgb(120,196,236);//浅蓝 #79C4EC
            for(int i = 0; i < sizen; i++){
                currentPortName = portNameList.get(i);
                mpll = new MyPortLinearLayout(this);
                mpll.setText(currentPortName);
                if(sizen >= 2 && i == sizen-1) {
                    mpll.setLineVisibility(false);
                }else {
                    mpll.setLineVisibility(true);
                }

                //添加顶部的港口名称
                port_Ll.addView(mpll);

                mtll = new MyTopLinearLayout(this);
                //mPortNameStr = getResources().getString(R.string.portName_label2) + (i+1) +": " + currentPortName;
                //mJobTypeStr = getResources().getString(R.string.jobTypeValue_label) + ": " + jobTypeValueList.get(i);

                mPortNameStr = currentPortName;
                mJobTypeStr = jobTypeValueList.get(i);

                Drawable drawable = null;
                //航次已开始
                if(fstatus.equals(Config.fStatusList.get(2))){
                    if(i < curPositionInPortNameList){//浅红色
                        mpll.setTextBackgroundColor(disableColor);
                        mPortNameStr = "<font color='#FF7F50'>" + mPortNameStr + "</font>";
                        //mJobTypeStr = "<font color='#fff'>" + mJobTypeStr + "</font>";
                        drawable = getResources().getDrawable(R.drawable
                                .shape_voyage_dynamic_text_bg_red);
                        //当前节点的状态： 0 已经过  1 进行中 2 未到达
                        mtll.setStatusImage(getStatusImageViewDrawable(i,0));
                    }else if(i == curPositionInPortNameList){//浅绿色
                        mpll.setTextBackgroundColor(currentColor);
                        mPortNameStr = "<font color='#228B22'>" + mPortNameStr + "</font>";
                        //mJobTypeStr = "<font color='#fff'>" + mJobTypeStr + "</font>";
                        drawable = getResources().getDrawable(R.drawable
                                .shape_voyage_dynamic_text_bg_green);
                        //当前节点的状态： 0 已经过  1 进行中 2 未到达
                        mtll.setStatusImage(getStatusImageViewDrawable(i,1));
                    }else {//浅蓝色
                        mpll.setTextBackgroundColor(enableColor);
                        mPortNameStr = "<font color='#79C4EC'>" + mPortNameStr + "</font>";
                        //mJobTypeStr = "<font color='#fff'>" + mJobTypeStr + "</font>";
                        drawable = getResources().getDrawable(R.drawable
                                .shape_voyage_dynamic_text_bg_blue);
                        //当前节点的状态： 0 已经过  1 进行中 2 未到达
                        mtll.setStatusImage(getStatusImageViewDrawable(i,2));
                    }
                }else{//航次未开始
                    mpll.setTextBackgroundColor(enableColor);
                    mPortNameStr = "<font color='#79C4EC'>" + mPortNameStr + "</font>";
                    //mJobTypeStr = "<font color='#fff'>" + mJobTypeStr + "</font>";
                    drawable = getResources().getDrawable(R.drawable
                            .shape_voyage_dynamic_text_bg_blue);
                    mtll.setStatusImage(getStatusImageViewDrawable(i,2));
                }

                mtll.setText(mPortNameStr,mJobTypeStr);
                mtll.setWorkTypeBg(drawable);
                //第一条分割线 不显示
//                if(i == 0){
//                    mtll.isDividelineVisible(false);
//                }
//            if(fstatusStr.equals("2")){//计划正在执行中
//                mtll.isCommandAndExceptionTvVisible(true);
//                mtll.setTimeLabel(getResources().getString(R.string.reportTime_label));
//            }else{
//                mtll.isCommandAndExceptionTvVisible(false);
//                mtll.setTimeLabel(getResources().getString(R.string.estimatedTime_label));
//            }
                //添加每个码头明细的头部
                detail_Ll.addView(mtll);

                ImageView exceptionIv;  //exceptionIv.setTag("ss");
                for(int k = 0; k < data.size(); k++){
                    currentItem = data.get(k);
                    if(currentPortName.equals(currentItem.getPortName())){
                        mll = new MyLinearLayout(this);
                        //操作按钮 隐藏
                        mll.isCommandImageVisible(false);

                        //时间精确至 时分秒
                        String time = currentItem.getReportTime();
//                    if(time != null && time.length() != 0){
//                        time = time.substring(0,10);
//                    }
                        mll.setText(currentItem.getVoyageStatusDesc(),time);

                        final ImageView commandIv = mll.findCommandImageView();

                        final int position = k;
                        //添加每个港口的具体明细数据
                        detail_Ll.addView(mll);
                    }
                }
            }
        }
    }

    public Drawable getStatusImageViewDrawable(int i,int status){
        Drawable drawable = null;

        if(status == 0){//已经过的港口
            drawable = getResources().getDrawable(R.mipmap.ok32);
        }else if(status == 1){//正在作业的港口
            switch (i) {
                case 0:
                    drawable = getResources().getDrawable(R.mipmap.no1_32);
                    break;
                case 1:
                    drawable = getResources().getDrawable(R.mipmap.no2_32);
                    break;
                case 2:
                    drawable = getResources().getDrawable(R.mipmap.no3_32);
                    break;
                case 3:
                    drawable = getResources().getDrawable(R.mipmap.no4_32);
                    break;
            }
        }else{//待到达的港口
            switch (i) {
                case 0:
                    drawable = getResources().getDrawable(R.mipmap.no1_32_off);
                    break;
                case 1:
                    drawable = getResources().getDrawable(R.mipmap.no2_32_off);
                    break;
                case 2:
                    drawable = getResources().getDrawable(R.mipmap.no3_32_off);
                    break;
                case 3:
                    drawable = getResources().getDrawable(R.mipmap.no4_32_off);
                    break;
            }
        }
        return drawable;
    }

    public void initJobTypeValue(List<TextView> jobTypeValueTvList,List<String> jobTypeValueList){
        TextView tv;
        String str = getResources().getString(R.string.jobTypeValue_label);
        for(int k = 0; k < jobTypeValueTvList.size(); k++){
            tv = (TextView) jobTypeValueTvList.get(k);
            tv.setText(str + ": " + jobTypeValueList.get(k));
        }
    }

    public void initTableData(List<String> portNameList,List<SmartTable<ShipTableBean>>
            tableList,List<ShipTableBean> codeList,List<TextView> portNameTvList){
        List<ShipTableBean> codeListItem = new ArrayList<ShipTableBean>();
        List<List<ShipTableBean>> codeListItems = new ArrayList<>();
        String portName = "";
        String portName2 = "";
        int sizen = portNameList.size();
        for(int i = 0 ; i < sizen; i++){
            codeListItem.clear();
            portName = portNameList.get(i);
            for(int j = 0; j < codeList.size(); j++){
                portName2 = codeList.get(j).getItem().getPortName();
                if(portName2.equals(portName)){
                    codeListItem.add(codeList.get(j));
                }
            }
            if(codeListItem.size() != 0){
                codeListItems.add(codeListItem);
            }
        }

        //注意：初始化table是一个耗时操作
//        for(int m = 0; m < codeListItems.size(); m++){
//            //initTable(tableList.get(m),codeListItems.get(m));
//            initTable(tableList.get(m),codeListItems.get(m));
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
