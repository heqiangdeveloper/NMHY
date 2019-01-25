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
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import com.bin.david.form.core.SmartTable;


public class ShipPlanDetailActivity2 extends AppCompatActivity {
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
    @Bind(R.id.commit_bt)
    Button commit_Bt;

    @Bind(R.id.sec1_ll)
    LinearLayout sec1_Ll;
    @Bind(R.id.port1_tv)
    TextView port1_Tv;
    @Bind(R.id.jobTypeValue1_tv)
    TextView jobTypeValue1_Tv;
    private SmartTable<ShipTableBean> table1;

    @Bind(R.id.sec2_ll)
    LinearLayout sec2_Ll;
    @Bind(R.id.port2_tv)
    TextView port2_Tv;
    @Bind(R.id.jobTypeValue2_tv)
    TextView jobTypeValue2_Tv;
    private SmartTable<ShipTableBean> table2;

    @Bind(R.id.sec3_ll)
    LinearLayout sec3_Ll;
    @Bind(R.id.port3_tv)
    TextView port3_Tv;
    @Bind(R.id.jobTypeValue3_tv)
    TextView jobTypeValue3_Tv;
    private SmartTable<ShipTableBean> table3;

    @Bind(R.id.sec4_ll)
    LinearLayout sec4_Ll;
    @Bind(R.id.port4_tv)
    TextView port4_Tv;
    @Bind(R.id.jobTypeValue4_tv)
    TextView jobTypeValue4_Tv;
    private SmartTable<ShipTableBean> table4;


    private Context mContext = ShipPlanDetailActivity2.this;
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
        setContentView(R.layout.activity_plan_detail);
        ButterKnife.bind(this);

        data = getIntent().getParcelableArrayListExtra("voyageDynamicInfosBean");
        initTitle();
        table1 = (SmartTable<ShipTableBean>)findViewById(R.id.table1);
        table2 = (SmartTable<ShipTableBean>)findViewById(R.id.table2);
        table3 = (SmartTable<ShipTableBean>)findViewById(R.id.table3);
        table4 = (SmartTable<ShipTableBean>)findViewById(R.id.table4);
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
        commit_Bt.setVisibility(View.GONE);
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
//            case R.id.commit_bt:
//                Intent i = new Intent(mContext,AddShipPlanDetailActivity.class);
//                ShipPlanVo.DataBean.VoyageDynamicInfosBean item = data.get(2);
//                i.putExtra("item",item);
//                startActivity(i);
//                break;
        }
    }

    public void initData(){
        showView();
        //码头列表
        List<String> portNameList = new ArrayList<>();
        //作业类型列表
        List<String> jobTypeValueList = new ArrayList<>();
        int portId0 = data.get(0).getCurrPortId();
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
        List<SmartTable<ShipTableBean>> tableList = new ArrayList<>();
        final List<ShipTableBean> codeList = new ArrayList<ShipTableBean>();
        ShipPlanVo.DataBean.VoyageDynamicInfosBean item = null;
        for(int i = 0; i < data.size();i++){
            item = data.get(i);
            codeList.add(new ShipTableBean(item,"汇报"));
        }

        if(sizen == 2){
            tvList.add(portName1_Tv);
            tvList.add(portName2_Tv);

            //初始化明细表
            sec1_Ll.setVisibility(View.VISIBLE);
            sec2_Ll.setVisibility(View.VISIBLE);
            sec3_Ll.setVisibility(View.GONE);
            sec4_Ll.setVisibility(View.GONE);

            //初始化港口
            line2_Tv.setVisibility(View.GONE);
            line3_Tv.setVisibility(View.GONE);
            portName3_Tv.setVisibility(View.GONE);
            portName4_Tv.setVisibility(View.GONE);

            portNameTvList.add(port1_Tv);
            portNameTvList.add(port2_Tv);
            jobTypeValueTvList.add(jobTypeValue1_Tv);
            jobTypeValueTvList.add(jobTypeValue2_Tv);

            tableList.add(table1);
            tableList.add(table2);
        } else if(sizen == 3){
            line3_Tv.setVisibility(View.GONE);
            portName4_Tv.setVisibility(View.GONE);
            //初始化明细表
            sec1_Ll.setVisibility(View.VISIBLE);
            sec2_Ll.setVisibility(View.VISIBLE);
            sec3_Ll.setVisibility(View.VISIBLE);
            sec4_Ll.setVisibility(View.GONE);

            tvList.add(portName1_Tv);
            tvList.add(portName2_Tv);
            tvList.add(portName3_Tv);

            portNameTvList.add(port1_Tv);
            portNameTvList.add(port2_Tv);
            portNameTvList.add(port3_Tv);
            jobTypeValueTvList.add(jobTypeValue1_Tv);
            jobTypeValueTvList.add(jobTypeValue2_Tv);
            jobTypeValueTvList.add(jobTypeValue3_Tv);

            tableList.add(table1);
            tableList.add(table2);
            tableList.add(table3);
        }else if(sizen == 4){
            line3_Tv.setVisibility(View.VISIBLE);
            portName4_Tv.setVisibility(View.VISIBLE);

            //初始化明细表
            sec1_Ll.setVisibility(View.VISIBLE);
            sec2_Ll.setVisibility(View.VISIBLE);
            sec3_Ll.setVisibility(View.VISIBLE);
            sec4_Ll.setVisibility(View.VISIBLE);

            tvList.add(portName1_Tv);
            tvList.add(portName2_Tv);
            tvList.add(portName3_Tv);
            tvList.add(portName4_Tv);

            portNameTvList.add(port1_Tv);
            portNameTvList.add(port2_Tv);
            portNameTvList.add(port3_Tv);
            portNameTvList.add(port4_Tv);
            jobTypeValueTvList.add(jobTypeValue1_Tv);
            jobTypeValueTvList.add(jobTypeValue2_Tv);
            jobTypeValueTvList.add(jobTypeValue3_Tv);
            jobTypeValueTvList.add(jobTypeValue4_Tv);

            tableList.add(table1);
            tableList.add(table2);
            tableList.add(table3);
            tableList.add(table4);
        }
        initTableData(portNameList,tableList,codeList,portNameTvList);
        initPortName(portNameList,tvList,portNameTvList);
        String portNameStr1 = "";
        String portNameStr2 = "";
        a:for(int q =0; q < portNameList.size(); q++){
            portNameStr1 = portNameList.get(q);
            b:for(int r =0; r < codeList.size(); r++){
                if(codeList.get(r).getItem().getPortName().equals(portNameStr1)){
                    jobTypeValueList.add(codeList.get(r).getItem().getJobTypeValue());
                    break b;
                }
            }
        }
        initJobTypeValue(jobTypeValueTvList,jobTypeValueList);
    }

    public void initPortName(List<String> portNameList,List<TextView> tvList,List<TextView> portNameTvList){
        TextView tv,portNameTv;
        String str = getResources().getString(R.string.portName_label);
        for(int k = 0; k < portNameList.size(); k++){
            tv = (TextView) tvList.get(k);
            portNameTv = portNameTvList.get(k);
            tv.setText(portNameList.get(k));
            portNameTv.setText(str + ": " + portNameList.get(k));
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
        for(int m = 0; m < codeListItems.size(); m++){
            //initTable(tableList.get(m),codeListItems.get(m));
            initTable(tableList.get(m),codeListItems.get(m));
        }
    }
    public void initTable(SmartTable<ShipTableBean> mTable,List<ShipTableBean> codeList){
        portName = new Column<>("码头名称", "item.portName");
        jobTypeValue = new Column<>("作业类型", "item.jobTypeValue");
        voyageStatusDesc = new Column<>("航次状态", "item.voyageStatusDesc");
        estimatedTime = new Column<>("预计时间", "item.estimatedTime");
        operate = new Column<>("操作", "operate");

        operate.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String bool, int position) {
                Intent i = new Intent(mContext,AddShipPlanDetailActivity.class);
                ShipPlanVo.DataBean.VoyageDynamicInfosBean item = data.get(2);
                i.putExtra("item",item);
                startActivity(i);
            }
        });

        final TableData<ShipTableBean> tableData = new TableData<ShipTableBean>("测试标题",codeList,
                voyageStatusDesc,estimatedTime,operate);
        mTable.getConfig().setShowTableTitle(false);
        //设置是否显示顶部序号列
        mTable.getConfig().setShowXSequence(false);
        //设置是否显示左侧序号列
        mTable.getConfig().setShowYSequence(false);

        //是否自动合并单元格
        //portName.setAutoMerge(true);
        //jobTypeValue.setAutoMerge(true);

        mTable.setTableData(tableData);
        FontStyle style = new FontStyle();
        style.setTextSize(50);
        style.setTextColor(getResources().getColor(R.color.table_text_color));
        //mTable.getConfig().setContentStyle(style);
        //table.getConfig().setMinTableWidth(1024);       //设置表格最小宽度

        //mTable.getConfig().setContentStyle(style);       //设置表格主题字体样式
        //mTable.getConfig().setColumnTitleStyle(style);   //设置表格标题字体样式
        //设置table行的背景色
        mTable.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {     //设置隔行变色
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
//                if(cellInfo.row%2 ==1) {
//                    return ContextCompat.getColor(mContext, R.color.tableBackground);
//                }else{
//                    return TableConfig.INVALID_COLOR;
//                }
                if(cellInfo.row == 2){
                    return ContextCompat.getColor(mContext, R.color.white);
                }else{
                    return ContextCompat.getColor(mContext, R.color.tableBackground);
                }
            }
        });
        mTable.setZoom(false);
        mTable.notifyDataChanged();
    }
}
