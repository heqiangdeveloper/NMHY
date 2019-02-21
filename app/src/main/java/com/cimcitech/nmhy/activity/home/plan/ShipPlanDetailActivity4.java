package com.cimcitech.nmhy.activity.home.plan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
import com.cimcitech.nmhy.utils.EventBusMessage;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.MyLinearLayout;
import com.cimcitech.nmhy.widget.MyPortLinearLayout;
import com.cimcitech.nmhy.widget.MyTopLinearLayout;
import com.roger.catloadinglibrary.CatLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ShipPlanDetailActivity4 extends AppCompatActivity {
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

    @Bind(R.id.detail_ll)
    LinearLayout detail_Ll;

    private Context mContext = ShipPlanDetailActivity4.this;
    private ArrayList<ShipPlanVo.DataBean.VoyageDynamicInfosBean> data = null;
    private String fstatusStr = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail4);
        ButterKnife.bind(this);

        //注册EventBus订阅者
        EventBus.getDefault().register(this);

        data = getIntent().getParcelableArrayListExtra("voyageDynamicInfosBean");
        fstatusStr = getIntent().getStringExtra("fstatus");
        initTitle();
        hideView();
        initData();
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

    public void initData(){
        showView();
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
            if(portIdi != portId0 && !portNameList.contains(portNameStr)){
                    portNameList.add(portNameStr);
            }
        }
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

        MyLinearLayout mll = null;
        MyTopLinearLayout mtll = null;
        MyPortLinearLayout mpll = null;
        String mPortNameStr = "";
        String mJobTypeStr = "";
        String currentPortName = "";
        String commandStr = "";
        String exceptionStr = "";
        ShipPlanVo.DataBean.VoyageDynamicInfosBean currentItem = null;
        data.get(0).setReportTime("2019-02-24");
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
        int disableColor = Color.rgb(255,127,80);//浅红
        int currentColor = Color.rgb(34,139,34);//浅绿
        int enableColor = Color.rgb(120,196,236);//浅蓝
        for(int i = 0; i < sizen; i++){
            currentPortName = portNameList.get(i);
            mpll = new MyPortLinearLayout(this);
            mpll.setText(currentPortName);
            if(sizen >= 2 && i == sizen-1) {
                mpll.setLineVisibility(false);
            }else {
                mpll.setLineVisibility(true);
            }
            if(i < curPositionInPortNameList){
                mpll.setTextBackgroundColor(disableColor);
            }else if(i == curPositionInPortNameList){
                mpll.setTextBackgroundColor(currentColor);
            }else {
                mpll.setTextBackgroundColor(enableColor);
            }

            //添加顶部的港口名称
            port_Ll.addView(mpll);

            mtll = new MyTopLinearLayout(this);
            mPortNameStr = getResources().getString(R.string.portName_label2) + (i+1) +": " +
                    "<font color='#666666'>" + currentPortName + "</font>";
            mJobTypeStr = getResources().getString(R.string.jobTypeValue_label) + ": " +
                    "<font color='#666666'>" + jobTypeValueList.get(i) + "</font>";

            mtll.setText(mPortNameStr,mJobTypeStr);
            if(i == 0){
                mtll.isDividelineVisible(false);
            }
            if(fstatusStr.equals("2")){//计划正在执行中
                mtll.isCommandAndExceptionTvVisible(true);
                mtll.setTimeLabel(getResources().getString(R.string.reportTime_label));
            }else{
                mtll.isCommandAndExceptionTvVisible(false);
                mtll.setTimeLabel(getResources().getString(R.string.estimatedTime_label));
            }
            //添加每个码头明细的头部
            detail_Ll.addView(mtll);

            ImageView exceptionIv;  //exceptionIv.setTag("ss");
            for(int k = 0; k < data.size(); k++){
                currentItem = data.get(k);
                if(currentPortName.equals(currentItem.getPortName())){
                    mll = new MyLinearLayout(this);
                    //exceptionIv = mll.findExceptionTv();
                    if(fstatusStr.equals("2")){//计划正在执行中
                        mll.isCommandAndExceptionTvVisible(true);
                    }else{
                        mll.isCommandAndExceptionTvVisible(false);
                    }
                    if(currentItem.getReportTime() != null && currentItem.getReportTime().length() != 0){
                        mll.setCommandImageViewSrc(getResources().getDrawable(R.mipmap.eye32));
                        mll.isExceptionImageVisible(false);
                    }else{

                    }

                    String time = currentItem.getReportTime();
//                    if(time != null && time.length() != 0){
//                        time = time.substring(0,10);
//                    }
                    mll.setText(currentItem.getVoyageStatusDesc(),time);

                    //final ImageView commandIv = mll.findCommandTv();

                    final int position = k;
                    ///commandTv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(position <= cPosition){
//                                ShipPlanVo.DataBean.VoyageDynamicInfosBean item = data.get(position);
//                                Intent i = new Intent(mContext,AddShipPlanDetailActivity.class);
//                                //汇报
//                                if(commandTv.getText().toString().trim().equals(getResources()
//                                        .getString(R.string.command_report_label))){
//                                    i.putExtra("isAdd",true);
//                                }else{//查看
//                                    i.putExtra("isAdd",false);
//                                }
//                                i.putExtra("item",item);
//                                startActivity(i);
//                            }else {
//                                ToastUtil.showToast("请先汇报之前的航次计划！");
//                            }
//                        }
//                    });
//                    exceptionTv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(position <= cPosition){
//                                if(!commandTv.getText().toString().trim().equals(getResources()
//                                        .getString(R.string.command_look_label))){
//                                    ToastUtil.showToast("Clicked!");
//                                }
//                            }else{
//                                ToastUtil.showToast("请先汇报之前的航次计划！");
//                            }
//                        }
//                    });
                    //添加每个港口的具体明细数据
                    detail_Ll.addView(mll);
                }
            }
        }
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
