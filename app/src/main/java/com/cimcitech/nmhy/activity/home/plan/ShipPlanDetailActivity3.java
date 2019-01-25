package com.cimcitech.nmhy.activity.home.plan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.MyLinearLayout;
import com.cimcitech.nmhy.widget.MyTopLinearLayout;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ShipPlanDetailActivity3 extends AppCompatActivity {
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

    @Bind(R.id.portName1_tv)
    TextView portName1_Tv;
    @Bind(R.id.portName2_tv)
    TextView portName2_Tv;
    @Bind(R.id.portName3_tv)
    TextView portName3_Tv;
    @Bind(R.id.portName4_tv)
    TextView portName4_Tv;
    @Bind(R.id.line1_tv)
    TextView line1_Tv;
    @Bind(R.id.line2_tv)
    TextView line2_Tv;
    @Bind(R.id.line3_tv)
    TextView line3_Tv;

    @Bind(R.id.detail_ll)
    LinearLayout detail_Ll;


    private Context mContext = ShipPlanDetailActivity3.this;
    private ArrayList<ShipPlanVo.DataBean.VoyageDynamicInfosBean> data = null;

    private CatLoadingView mCatLoadingView = null;


    Column<String> portName;
    Column<String> jobTypeValue;
    Column<String> voyageStatusDesc;
    Column<String> estimatedTime;
    Column<String> operate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail3);
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

        if(sizen == 2){
            portNameTvList.add(portName1_Tv);
            portNameTvList.add(portName2_Tv);
            //初始化港口
            line2_Tv.setVisibility(View.GONE);
            line3_Tv.setVisibility(View.GONE);
            portName3_Tv.setVisibility(View.GONE);
            portName4_Tv.setVisibility(View.GONE);

            MyLinearLayout mll = null;
            MyTopLinearLayout mtll = null;
            String currentPortName = "";
            String commandStr = "";
            String exceptionStr = "";

            for(int i = 0; i < sizen; i++){
                currentPortName = portNameList.get(i);
                mtll = new MyTopLinearLayout(this);
                mtll.setText(currentPortName,jobTypeValueList.get(i));
                detail_Ll.addView(mtll);
                for(int k = 0; k < data.size(); k++){
                    if(currentPortName.equals(data.get(k).getPortName())){
                        mll = new MyLinearLayout(this);
                        mll.setText(data.get(k).getVoyageStatusDesc(),"2019-02-26","汇报","报异常");
                        detail_Ll.addView(mll);
                    }
                }
            }
        } else if(sizen == 3){
            line3_Tv.setVisibility(View.GONE);
            portName4_Tv.setVisibility(View.GONE);
            portNameTvList.add(portName1_Tv);
            portNameTvList.add(portName2_Tv);
            portNameTvList.add(portName3_Tv);

            MyLinearLayout mll = null;
            MyTopLinearLayout mtll = null;
            String currentPortName = "";
            String commandStr = "";
            ShipPlanVo.DataBean.VoyageDynamicInfosBean currentItem = null;
            data.get(2).setEstimatedTime("2019-02-24");
            data.get(5).setEstimatedTime("2019-08-21");
            data.get(9).setEstimatedTime("2019-02-03");
            for(int i = 0; i < sizen; i++){
                currentPortName = portNameList.get(i);
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

                        mll.setText(currentItem.getVoyageStatusDesc(),currentItem.getEstimatedTime(),commandStr, "报异常");
                        detail_Ll.addView(mll);
                        final TextView commandTv = mll.findCommandTv();
                        TextView exceptionTv = mll.findExceptionTv();
                        final int position = k;
                        commandTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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
                            }
                        });
                        exceptionTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showToast("Clicked!");
                            }
                        });
                    }
                }
            }
        }else if(sizen == 4){
            line3_Tv.setVisibility(View.VISIBLE);
            portName4_Tv.setVisibility(View.VISIBLE);
            portNameTvList.add(portName1_Tv);
            portNameTvList.add(portName2_Tv);
            portNameTvList.add(portName3_Tv);
            portNameTvList.add(portName4_Tv);

            MyLinearLayout mll = null;
            for(int i = 0; i < sizen; i++){
                mll = new MyLinearLayout(this);
                mll.setText(portNameList.get(i),portNameList.get(i),portNameList.get(i),portNameList.get(i));
                detail_Ll.addView(mll);
            }
        }

        //1.先初始化头部的各个港口名称
        initPortName(portNameList,portNameTvList);
        //2.再初始化各个港口中的节点：港口名称 和 作业类型
        //initJobTypeValue(jobTypeValueTvList,jobTypeValueList);




    }

    public void initPortName(List<String> portNameList,List<TextView> portNameTvList){
        TextView portNameTv;
        for(int k = 0; k < portNameList.size(); k++){
            portNameTv = (TextView) portNameTvList.get(k);
            portNameTv.setText(portNameList.get(k));
        }

        GradientDrawable mm1 = (GradientDrawable)portName1_Tv.getBackground();
        mm1.setColor(Color.rgb(255,127,80));
        GradientDrawable mm2 = (GradientDrawable)portName2_Tv.getBackground();
        mm2.setColor(Color.rgb(34,139,34));
        GradientDrawable mm3 = (GradientDrawable)portName3_Tv.getBackground();
        mm3.setColor(Color.rgb(34,139,34));
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
