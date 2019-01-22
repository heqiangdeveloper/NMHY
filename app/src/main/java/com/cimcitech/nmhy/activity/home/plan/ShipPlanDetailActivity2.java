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
import java.util.List;

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

    private Context mContext = ShipPlanDetailActivity2.this;
    private ArrayList<ShipPlanVo.DataBean.VoyageDynamicInfosBean> data = null;

    private CatLoadingView mCatLoadingView = null;

    private SmartTable<ShipTableBean> table;
    Column<String> portName;
    Column<String> jobTypeValue;
    Column<String> voyageStatusDesc;
    Column<String> operate;
    List<String> name_selected = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        ButterKnife.bind(this);

        data = getIntent().getParcelableArrayListExtra("voyageDynamicInfosBean");
        initTitle();
        table = (SmartTable<ShipTableBean>)findViewById(R.id.table);
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

    @OnClick({R.id.back_iv,R.id.commit_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.commit_bt:
                Intent i = new Intent(mContext,AddShipPlanDetailActivity.class);
                ShipPlanVo.DataBean.VoyageDynamicInfosBean item = data.get(2);
                i.putExtra("item",item);
                startActivity(i);
                break;
        }
    }

    public void initData(){
        showView();
        List<String> portNameList = new ArrayList<>();
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

        tvList.add(portName1_Tv);
        tvList.add(portName2_Tv);
        tvList.add(portName3_Tv);
        if(sizen == 3){
            line3_Tv.setVisibility(View.GONE);
            portName4_Tv.setVisibility(View.GONE);
        }else if(sizen == 4){
            line3_Tv.setVisibility(View.VISIBLE);
            portName4_Tv.setVisibility(View.VISIBLE);
            tvList.add(portName4_Tv);
        }

        for(int i = 0; i < portNameList.size(); i++){
            TextView tv = (TextView) tvList.get(i);
            tv.setText(portNameList.get(i));
        }

        GradientDrawable mm1 = (GradientDrawable)portName1_Tv.getBackground();
        mm1.setColor(Color.RED);
        GradientDrawable mm2 = (GradientDrawable)portName2_Tv.getBackground();
        mm2.setColor(Color.GREEN);
        GradientDrawable mm3 = (GradientDrawable)portName3_Tv.getBackground();
        mm3.setColor(Color.GREEN);

        final List<ShipTableBean> codeList = new ArrayList<ShipTableBean>();
        ShipPlanVo.DataBean.VoyageDynamicInfosBean item = null;
        for(int i = 0; i < data.size();i++){
            item = data.get(i);
//            codeList.add(new ShipTableBean(item.getPortName(),item.getJobTypeValue(),item
//                    .getVoyageStatusDesc(),"汇报"));
            codeList.add(new ShipTableBean(item,"汇报"));
        }
//        codeList.add(new ShipTableBean("上海洋山1","空放1","航次开始1",false));

        portName = new Column<>("码头名称", "item.portName");
        jobTypeValue = new Column<>("作业类型", "item.jobTypeValue");
        voyageStatusDesc = new Column<>("航次状态", "item.voyageStatusDesc");
        int size = DensityUtils.dp2px(this,30);

//        isClicked = new Column<>("勾选", "isClicked", new ImageResDrawFormat<Boolean>(size,size) {    //设置"操作"这一列以图标显示 true、false 的状态
//            @Override
//            protected Context getContext() {
//                return mContext;
//            }
//            @Override
//            protected int getResourceID(Boolean isCheck, String value, int position) {
//                if(isCheck){
//                    return R.mipmap.check24;      //将图标提前放入 app/res/mipmap 目录下
//                }
//                return R.mipmap.uncheck24;
//            }
//        });
//        //isClicked.setComputeWidth(40);
//        isClicked.setOnColumnItemClickListener(new OnColumnItemClickListener<Boolean>() {
//            @Override
//            public void onClick(Column<Boolean> column, String value, Boolean bool, int position) {
//                if(isClicked.getDatas().get(position)){
//                    showName(position, false);
//                    isClicked.getDatas().set(position,false);
//                }else{
//                    showName(position, true);
//                    isClicked.getDatas().set(position,true);
//                }
//                table.refreshDrawableState();
//                table.invalidate();
//            }
//        });

        operate = new Column<>("操作", "operate");
        operate.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String bool, int position) {
                if(position == 2){

                }
            }
        });

        final TableData<ShipTableBean> tableData = new TableData<ShipTableBean>("测试标题",codeList,
                portName, jobTypeValue,voyageStatusDesc);
        table.getConfig().setShowTableTitle(false);
        //设置是否显示顶部序号列
        table.getConfig().setShowXSequence(false);
        //设置是否显示左侧序号列
        table.getConfig().setShowYSequence(false);

        //是否自动合并单元格
        //portName.setAutoMerge(true);
        //jobTypeValue.setAutoMerge(true);

        table.setTableData(tableData);
        FontStyle style = new FontStyle();
        style.setTextSize(40);
        style.setTextColor(Color.BLUE);
        table.getConfig().setContentStyle(style);
        //table.getConfig().setMinTableWidth(1024);       //设置表格最小宽度

        table.getConfig().setContentStyle(style);       //设置表格主题字体样式
        table.getConfig().setColumnTitleStyle(style);   //设置表格标题字体样式
        //设置table行的背景色
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {     //设置隔行变色
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
        //table.setZoom(true,3,1);
    }
}
