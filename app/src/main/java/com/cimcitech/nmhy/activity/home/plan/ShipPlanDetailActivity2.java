package com.cimcitech.nmhy.activity.home.plan;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.IBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.adapter.plan.ShipPlanAdapter;
import com.cimcitech.nmhy.bean.plan.ShipPlanDetailReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanDetailVo;
import com.cimcitech.nmhy.bean.plan.ShipPlanReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.IconTreeItemHolder;
import com.cimcitech.nmhy.utils.JsonHelper;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.roger.catloadinglibrary.CatLoadingView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private List<ShipPlanDetailVo.DataBean.ListBean> data = new ArrayList<>();
    private int pageNum = 1;
    private ShipPlanDetailVo shipPlanDetailVo = null;
    private long voyagePlanId = -1;
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

        voyagePlanId = getIntent().getLongExtra("voyagePlanId",-1);
        initTitle();
        table = (SmartTable<ShipTableBean>)findViewById(R.id.table);
        hideView();
        getData();
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
                ShipPlanDetailVo.DataBean.ListBean item = data.get(2);
                i.putExtra("item",item);
                startActivity(i);
                break;
        }
    }

    public void getData() {
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        String json = new Gson().toJson(new ShipPlanDetailReq(pageNum,10,"",new ShipPlanDetailReq
                .VoyageDynamicInfoBean(voyagePlanId)));
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

        GradientDrawable mm= (GradientDrawable)portName1_Tv.getBackground();
        mm.setColor(Color.RED);

        final List<ShipTableBean> codeList = new ArrayList<ShipTableBean>();
        ShipPlanDetailVo.DataBean.ListBean item = null;
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

        portName.setAutoMerge(true);
        jobTypeValue.setAutoMerge(true);

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



//        List<ShipTableBean> list = new ArrayList<>();
//        Button button = new Button(this);
//        button.setWidth(30);
//        button.setHeight(15);
//        button.setText("编辑");
//        list.add(new ShipTableBean("上海洋山","空放","航次开始",false));
//        list.add(new ShipTableBean("上海洋山2","空放2","航次开始2",false));
//        list.add(new ShipTableBean("上海洋山3","空放3","航次开始3",true));
//        list.add(new ShipTableBean("上海洋山4","空放4","航次开始4",false));
//
//        table.setData(list);
//        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
//
//        int size1 = DensityUtils.dp2px(this,15); //指定图标大小
//        Column<Boolean> checkColumn = new Column<>("勾选", "isCheck",new ImageResDrawFormat<Boolean>(size1,size1) {
//            @Override
//            protected Context getContext() {
//                return mContext;
//            }
//
//            @Override
//            protected int getResourceID(Boolean isCheck, String value, int position) {
//                if(isCheck){
//                    return R.mipmap.check24;
//                }
//                return 0;
//            }
//        });
//
//        //Column groupColumn = new Column("组合",nameColumn,ageColumn);
//        TableData<ShipTableBean> tableData = new TableData<ShipTableBean>("用户表",null,null,
//                checkColumn);
    }

    public void showName(int position, boolean selectedState){
        List<String> rotorIdList = portName.getDatas();
        if(position >-1){
            String rotorTemp = rotorIdList.get(position);
            if(selectedState && !name_selected.contains(rotorTemp)){            //当前操作是选中，并且“所有选中的姓名的集合”中没有该记录，则添加进去
                name_selected.add(rotorTemp);
            }else if(!selectedState && name_selected.contains(rotorTemp)){     // 当前操作是取消选中，并且“所有选中姓名的集合”总含有该记录，则删除该记录
                name_selected.remove(rotorTemp);
            }
        }
        for(String s:name_selected){
            System.out.print(s + " -- ");
        }
    }

}
