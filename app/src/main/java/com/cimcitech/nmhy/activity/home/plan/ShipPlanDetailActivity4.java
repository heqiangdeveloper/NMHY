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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.cimcitech.nmhy.R;
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
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;


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
    @Bind(R.id.start_voyage_bt)
    Button start_voyage_Bt;
    @Bind(R.id.end_voyage_bt)
    Button end_voyage_Bt;

    @Bind(R.id.detail_ll)
    LinearLayout detail_Ll;

    private Context mContext = ShipPlanDetailActivity4.this;
    private ArrayList<ShipPlanVo.DataBean.VoyageDynamicInfosBean> data = null;
    private String fstatus = "";
    private ChangeVoyagePlanReq req;
    private Calendar calendar;
    private SimpleDateFormat sdf;
    private CatLoadingView mCatLoadingView;
    final String nowStr = DateTool.getSystemDate();
    private boolean isHasPlanStart = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail4);
        ButterKnife.bind(this);

        calendar = Calendar.getInstance();
        //注册EventBus订阅者
        EventBus.getDefault().register(this);

        data = getIntent().getParcelableArrayListExtra("voyageDynamicInfosBean");
        fstatus = getIntent().getStringExtra("fstatus");
        req = (ChangeVoyagePlanReq)getIntent().getSerializableExtra("req");
        isHasPlanStart = getIntent().getBooleanExtra("isHasPlanStart",false);
        setButtonBackground();

        initTitle();
        hideView();
        initData();
    }

    public void setButtonBackground(){
        //"0"  计划中-不确定
        //"1"  计划中-确定
        //"2"  计划执行中
        //"3"  执行结束
        if(fstatus.equals(Config.fStatusList.get(2)) || fstatus.equals(Config.fStatusList.get(3))){
            start_voyage_Bt.setText(getResources().getString(R.string.already_start_voyage_label));
            start_voyage_Bt.setBackground(getResources().getDrawable(R.drawable.shape_start_voyage_button_on));
        }else{
            start_voyage_Bt.setText(getResources().getString(R.string.start_voyage_label));
            start_voyage_Bt.setBackground(getResources().getDrawable(R.drawable.shape_voyage_button_off));
        }

        if(fstatus.equals(Config.fStatusList.get(3))){//航次已结束
            end_voyage_Bt.setText(getResources().getString(R.string.already_end_voyage_label));
            end_voyage_Bt.setBackground(getResources().getDrawable(R.drawable.shape_end_voyage_button_on));
        }else{//点击结束航次
            end_voyage_Bt.setText(getResources().getString(R.string.end_voyage_label));
            end_voyage_Bt.setBackground(getResources().getDrawable(R.drawable.shape_voyage_button_off));
        }
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

    @OnClick({R.id.back_iv,R.id.start_voyage_bt,R.id.end_voyage_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.start_voyage_bt:
                if(fstatus.equals(Config.fStatusList.get(2)) || fstatus.equals(Config.fStatusList.get(3))){
                    ToastUtil.showToast(getResources().getString(R.string.start_voyage_button_warning));
                }else if(!isHasPlanStart){//没有已经开始的航次，就可以开始本航次
                    ShipPlanVo.DataBean.VoyageDynamicInfosBean item = data.get(0);
                    Intent i = new Intent(mContext,StartVoyagePlanActivity.class);
                    i.putExtra("isAdd",true);
                    i.putExtra("item",item);
                    startActivity(i);
                    //showSelectDateDialog2();
                }else{
                    ToastUtil.showToast(getResources().getString(R.string.start_voyage_button_warning2));
                }
                break;
            case R.id.end_voyage_bt:
                String lastReportTime = data.get(data.size()-1).getReportTime();
                if(fstatus.equals(Config.fStatusList.get(3))){
                    ToastUtil.showToast(getResources().getString(R.string.end_voyage_button_warning));
                }else if(fstatus.equals(Config.fStatusList.get(2)) && (lastReportTime == null ||
                        lastReportTime.length() == 0)){
                    ToastUtil.showToast(getResources().getString(R.string.end_voyage_button_warning2));
                }else if(fstatus.equals(Config.fStatusList.get(2)) && (lastReportTime != null &&
                        lastReportTime.length() != 0)){
                    endVoyage(nowStr);
                }else{
                    ToastUtil.showToast(getResources().getString(R.string.end_voyage_button_warning3));
                }
                break;
        }
    }

    private void showSelectDateDialog2() {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour= 0;
        int minute = 0;

        String strDate = nowStr.split(" ")[0];
        String strTime = nowStr.split(" ")[1];
        year = Integer.parseInt(strDate.substring(0,4));//年
        month = Integer.parseInt(strDate.substring(5,7)) - 1;//月
        day = Integer.parseInt(strDate.substring(8,10));//日

        hour = Integer.parseInt(strTime.split(":")[0]);//时
        minute = Integer.parseInt(strTime.split(":")[1]);//分

        String title = getResources().getString(R.string.select_voyage_start_time_label);
        new DateTimePickDialog(mContext,title,year,month,day,hour,minute)
                .setOnDateTimeSetListener
                        (new DateTimePickDialog.OnDateTimeSetListener()
                        {//给定Calendar c,就能将日期和时间进行初始化
                            @Override
                            public void onDateTimeSet(DatePicker dp, TimePicker tp, int year,
                                                      int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
                                // 保存选择后时间
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND,59);

                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String selectTimeStr = sdf.format(calendar.getTime());
                                if(selectTimeStr.compareTo(nowStr) >= 0){
                                    startVoyage(selectTimeStr);
                                }else {
                                    ToastUtil.showToast("开始航行时间必须在当前时间之后！");
                                }
                            }
                        });
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
        ShipPlanVo.DataBean.VoyageDynamicInfosBean currentItem = null;
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
                    //报告时间不为0，只能查看
                    if(fstatus.equals(Config.fStatusList.get(2)) &&
                            currentItem.getReportTime() != null && currentItem.getReportTime().length() != 0){
                        mll.setCommandImageViewSrc(getResources().getDrawable(R.mipmap.eye32));
                        mll.setImageTag(0);
                    }else if(fstatus.equals(Config.fStatusList.get(2)) && (k == cPosition)){
                        mll.setCommandImageViewSrc(getResources().getDrawable(R.mipmap.write32));
                        mll.setImageTag(1);
                    }
                    else{//否则操作按钮不显示，在下面再处理 当前航次状态可以汇报的逻辑
                        mll.isCommandImageVisible(false);
                        mll.setImageTag(-1);
                    }

                    //时间精确至 时分秒
                    String time = currentItem.getReportTime();
//                    if(time != null && time.length() != 0){
//                        time = time.substring(0,10);
//                    }
                    mll.setText(currentItem.getVoyageStatusDesc(),time);

                    final ImageView commandIv = mll.findCommandImageView();

                    final int position = k;
                    commandIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //当前航次状态之前的，已经汇报过的，只能查看记录
                            ShipPlanVo.DataBean.VoyageDynamicInfosBean item = data.get(position);
                            Intent i = new Intent(mContext,AddShipPlanDetailActivity.class);
                            switch ((int)commandIv.getTag()){
                                case 0:
                                    i.putExtra("isAdd",false);
                                    i.putExtra("item",item);
                                    startActivity(i);
                                    break;
                                case 1:
                                    i.putExtra("isAdd",true);
                                    i.putExtra("item",item);
                                    startActivity(i);
                                    break;
                                case -1:
                                    break;
                            }
                        }
                    });
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

    public void startVoyage(String time){
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        //更改航次计划的状态
        //规则是： 开始航次计划时，提交 实际开始时间;  结束航次计划时，提交 实际结束时间

        //注意加上token
        req.setActualSailingTime(time);
        String json = new Gson().toJson(req);
        OkHttpUtils
                .postString()
                .url(Config.change_voyagePlan_url)
                .addHeader("Check_Token",Config.token)
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
                                try{
                                    JSONObject jo = new JSONObject(response);
                                    if(jo.getBoolean("success")){
                                        // 发布事件
                                        EventBus.getDefault().post(new EventBusMessage("refreshShipPlan"));

                                        ToastUtil.showToast(getResources().getString(R.string.already_start_voyage_label));
                                        finish();
                                    }else{
                                        ToastUtil.showToast(jo.getString("msg"));
                                    }
                                }catch (JSONException e){
                                    ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                                }
                            }
                        }
                );
    }

    public void endVoyage(String time){
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        //更改航次计划的状态
        //规则是： 开始航次计划时，提交 实际开始时间;  结束航次计划时，提交 实际结束时间

        //注意加上token
        req.setActualStopTime(time);
        String json = new Gson().toJson(req);
        OkHttpUtils
                .postString()
                .url(Config.change_voyagePlan_url)
                .addHeader("Check_Token",Config.token)
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
                                try{
                                    JSONObject jo = new JSONObject(response);
                                    if(jo.getBoolean("success")){
                                        // 发布事件
                                        EventBus.getDefault().post(new EventBusMessage("refreshShipPlan"));

                                        ToastUtil.showToast(getResources().getString(R.string.already_end_voyage_label));
                                        finish();
                                    }else{
                                        ToastUtil.showToast(jo.getString("msg"));
                                    }
                                }catch (JSONException e){
                                    ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                                }
                            }
                        }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
