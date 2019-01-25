package com.cimcitech.nmhy.activity.home.plan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.MyLinearLayout;
import com.cimcitech.nmhy.widget.MyPortLinearLayout;
import com.cimcitech.nmhy.widget.MyTopLinearLayout;
import com.roger.catloadinglibrary.CatLoadingView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail4);
        ButterKnife.bind(this);

        data = getIntent().getParcelableArrayListExtra("voyageDynamicInfosBean");
        initTitle();
        hideView();
        initData();
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
        String currentPortName = "";
        String commandStr = "";
        ShipPlanVo.DataBean.VoyageDynamicInfosBean currentItem = null;
        data.get(0).setEstimatedTime("2019-02-24");
        //data.get(3).setEstimatedTime("2019-08-21");
        //data.get(7).setEstimatedTime("2019-02-03");

        int disableColor = Color.rgb(255,127,80);
        int enableColor = Color.rgb(34,139,34);

        int currentPosition = 0;
        for(int w = 0 ; w < data.size(); w++){
            if(data.get(w).getEstimatedTime() == null || data.get(w).getEstimatedTime().length() == 0){
                currentPosition = w;
                break;
            }
        }
        final int cPosition = currentPosition;

        for(int i = 0; i < sizen; i++){
            currentPortName = portNameList.get(i);
            mpll = new MyPortLinearLayout(this);
            mpll.setText(currentPortName);
            if(sizen >= 2 && i == sizen-1) {
                mpll.setLineVisibility(false);
            }else {
                mpll.setLineVisibility(true);
            }
            mpll.setTextBackgroundColor(enableColor);
            if(i == 0) mpll.setTextBackgroundColor(disableColor);
            port_Ll.addView(mpll);

            mtll = new MyTopLinearLayout(this);
            mtll.setText("码头名称： " + currentPortName,"作业类型： " + jobTypeValueList.get(i));
            detail_Ll.addView(mtll);

            for(int k = 0; k < data.size(); k++){
                currentItem = data.get(k);
                if(currentPortName.equals(currentItem.getPortName())){
                    mll = new MyLinearLayout(this);
                    if(currentItem.getEstimatedTime() != null && currentItem.getEstimatedTime().length() != 0){
                        commandStr = getResources().getString(R.string.command_look_label);
                    }else{
                        commandStr = getResources().getString(R.string.command_report_label);
                    }

                    String time = currentItem.getEstimatedTime();
                    if(time != null && time.length() != 0){
                        time = time.substring(0,10);
                    }
                    mll.setText(currentItem.getVoyageStatusDesc(),time,commandStr, "报异常");

                    final TextView commandTv = mll.findCommandTv();
                    TextView exceptionTv = mll.findExceptionTv();
                    final int position = k;
                    commandTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(position <= cPosition){
                                ShipPlanVo.DataBean.VoyageDynamicInfosBean item = data.get(position);
                                Intent i = new Intent(mContext,AddShipPlanDetailActivity.class);
                                //汇报
                                if(commandTv.getText().toString().trim().equals(getResources()
                                        .getString(R.string.command_report_label))){
                                    i.putExtra("isAdd",true);
                                }else{//查看
                                    i.putExtra("isAdd",false);
                                }
                                i.putExtra("item",item);
                                startActivity(i);
                            }else {
                                ToastUtil.showToast("请先汇报之前的航次计划！");
                            }
                        }
                    });
                    exceptionTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showToast("Clicked!");
                        }
                    });
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

}
