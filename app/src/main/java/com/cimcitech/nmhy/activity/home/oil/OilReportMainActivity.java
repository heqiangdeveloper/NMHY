package com.cimcitech.nmhy.activity.home.oil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.ApkApplication;
import com.cimcitech.nmhy.adapter.oil.OilReportAdapter;
import com.cimcitech.nmhy.adapter.oil.OilReportHistoryAdapter;
import com.cimcitech.nmhy.adapter.plan.ShipPlanAdapter;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.bean.oil.BargeNameAndVoyagePlanIdReq;
import com.cimcitech.nmhy.bean.oil.BargeNameAndVoyagePlanIdVo;
import com.cimcitech.nmhy.bean.oil.OilReportData;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.oil.OilReportMainReq;
import com.cimcitech.nmhy.bean.oil.VoyageStatusVo;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DateTool;
import com.cimcitech.nmhy.utils.EnumUtil;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.MyBaseActivity;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by qianghe on 2019/1/10.
 */


public class OilReportMainActivity extends MyBaseActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.content_sv)
    ScrollView contentSv;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.warn_tv)
    TextView warn_Tv;
    @Bind(R.id.item_current_tv)
    TextView itemCurrentTv;
    @Bind(R.id.item_history_tv)
    TextView itemHistoryTv;

    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.bargeName_tv)
    TextView bargeName_Tv;
    @Bind(R.id.portTransportOrder_tv)
    TextView portTransportOrder_Tv;
    @Bind(R.id.voyageStatus_tv)
    TextView voyageStatus_Tv;
    @Bind(R.id.location_tv)
    TextView locationTv;
    @Bind(R.id.longitude_tv)
    TextView longitudeTv;
    @Bind(R.id.latitude_tv)
    TextView latitudeTv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;

    @Bind(R.id.commit_bt)
    Button commitBt;
    @Bind(R.id.oilType1_tv)
    TextView oilType1Tv;
    @Bind(R.id.oilType2_tv)
    TextView oilType2Tv;
    @Bind(R.id.oilType3_tv)
    TextView oilType3Tv;
    @Bind(R.id.oilUnit1_tv)
    TextView oilUnit1Tv;
    @Bind(R.id.oilUnit2_tv)
    TextView oilUnit2Tv;
    @Bind(R.id.oilUnit3_tv)
    TextView oilUnit3Tv;
    @Bind(R.id.oilAmount1_et)
    EditText oilAmount1_Et;
    @Bind(R.id.oilAmount2_et)
    EditText oilAmount2_Et;
    @Bind(R.id.oilAmount3_et)
    EditText oilAmount3_Et;
    @Bind(R.id.label_tv)
    TextView label_Tv;

    //已报
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.content_cl)
    CoordinatorLayout contentCl;

    private LinearLayoutManager manager;
    private OilReportAdapter adapter;
    private Context mContext = OilReportMainActivity.this;
    private List<OilReportData> data = new ArrayList<>();
    private LocationService locationService;
    private ArrayList<Poi> pois = new ArrayList<>(); //获取到的定位位置的对象
    private ProgressDialog dialog = null;
    private static final int STARTTOLOCATING = 1;
    private static final int ENDTOLOCATING = 2;
    private static final int LOCATESUCCESS = 3;
    private static final int LOCATEFAIL = 4;

    private StringBuffer locSb ;
    private double longitude = 0;
    private double latitude = 0;
    private static final int requestLocTime = 7000;
    private boolean isFinishlocating = false;
    private final int LOCATION_REQUESTCODE = 1;
    private String unitTitleStr = "";
    private ShowListValueWindow window = null;
    private CatLoadingView mCatLoadingView = null;
    private boolean isAdd = true;
    private OilReportHistoryAdapter historyAdapter = null;
    private List<OilReportHistoryVo.DataBean.ListBean> historyData = new ArrayList<>();
    private int pageNum = 1;
    private boolean isLoading;
    private OilReportHistoryVo oilReportHistoryVo = null;
    private BargeNameAndVoyagePlanIdVo bargeNameAndVoyagePlanIdVo = null;
    private ShipPlanVo shipPlanVo = null;
    private long voyagePlanId = -1;
    private int bargeId = -1;
    private long voyageStatusId = -1;
    private int currPortId = -1;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STARTTOLOCATING:
                    isFinishlocating = false;
//                    dialog = new ProgressDialog(mContext);
//                    dialog.setMessage("定位中…");
//                    dialog.setCancelable(true);
//                    dialog.show();
                    break;
                case LOCATESUCCESS:
                    isFinishlocating = true;
//                    if(dialog.isShowing())
//                    dialog.dismiss();
                    if(locSb != null && locSb.toString().length() != 0 && !locSb.toString().contains("null")){
                        locationTv.setText(locSb.toString());
                        longitudeTv.setText(longitude + "");
                        latitudeTv.setText(latitude + "");
                    }
                    break;
                case LOCATEFAIL:
                   isFinishlocating = true;
                   locationTv.setText("测试地1");
                    longitudeTv.setText(113.21 + "");
                    latitudeTv.setText(20.03 + "");
//                    if(dialog.isShowing())
//                        dialog.dismiss();
                    Toast.makeText(mContext,"定位失败！请检查网络或GPS",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_report_main2);
        ButterKnife.bind(this);

//        locationTv.setText("测试目的地");
//        longitudeTv.setText(23.032+"");
//        latitudeTv.setText(112.032+"");
        initTitle();
        initView();
    }

    public void initTitle(){
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
        itemCurrentTv.setText(getResources().getString(R.string.add_oil_data_label));
        itemHistoryTv.setText(getResources().getString(R.string.history_oil_data_label));
        if(isAdd){//待报
            titleName_Tv.setText(getResources().getString(R.string.add_oil_data_label));
        }else {//已报
            titleName_Tv.setText(getResources().getString(R.string.history_oil_data_label));
        }
    }

    public void initView(){
        if(!NetWorkUtil.isConn(mContext)){
            empty_Rl.setVisibility(View.VISIBLE);
            warn_Tv.setText(getResources().getString(R.string.no_network_warn));
            contentSv.setVisibility(View.GONE);
            contentCl.setVisibility(View.GONE);
        }else{
            empty_Rl.setVisibility(View.GONE);
            if(isAdd){//待报
                contentSv.setVisibility(View.VISIBLE);
                contentCl.setVisibility(View.GONE);

                addWatcher(timeTv);
                addWatcher(voyageStatus_Tv);
                addWatcher(locationTv);
                addWatcher(latitudeTv);
                addWatcher(longitudeTv);
                addWatcher(oilAmount1_Et); addWatcher(oilAmount2_Et); addWatcher(oilAmount3_Et);
                initContent();
                //获取船名和航次id
                //getBargeNameAndVoyagePlanId();
                getVoyageStatus();
                startLocationService();
            }else {//已报
                contentSv.setVisibility(View.GONE);
                contentCl.setVisibility(View.VISIBLE);
                stopLocationService();
                initViewData();
                updateData();
            }
        }
    }

    public void getBargeNameAndVoyagePlanId(){
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        //查询正在航行中的航次计划，对应的fstatus = 2
        String fstatus = "2";
        String json = new Gson().toJson(new BargeNameAndVoyagePlanIdReq(fstatus,Config.accountId));
        OkHttpUtils
                .postString()
                .url(Config.get_bargeName_voyagePlanId_url)
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
                                Log.d(TAG,"response is: " + response);
                                bargeNameAndVoyagePlanIdVo = new Gson().fromJson(response, BargeNameAndVoyagePlanIdVo.class);
                                if(bargeNameAndVoyagePlanIdVo != null && bargeNameAndVoyagePlanIdVo.isSuccess()){
                                    if (bargeNameAndVoyagePlanIdVo.getData() != null && bargeNameAndVoyagePlanIdVo.getData().size() > 0) {
                                        BargeNameAndVoyagePlanIdVo.DataBean item = bargeNameAndVoyagePlanIdVo.getData().get(0);
                                        voyagePlanId = item.getVoyagePlanId();
                                        bargeName_Tv.setText(item.getcShipName());
                                        portTransportOrder_Tv.setText(item.getPortTransportOrder());
                                        //获取当前的航次状态
                                        getVoyageStatus();
                                    }else{
                                        mCatLoadingView.dismiss();
                                    }
                                }else{
                                    mCatLoadingView.dismiss();
                                }
                            }
                        }
                );
    }

    public void getVoyageStatus(){
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        OkHttpUtils
                .post()
                .url(Config.get_current_voyagePlan_info_url)
                .addParams("userId",Config.accountId + "")
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                mCatLoadingView.dismiss();
                                ToastUtil.showNetError();
                                label_Tv.setVisibility(View.VISIBLE);
                                commitBt.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                mCatLoadingView.dismiss();
                                Log.d(TAG,"response is: " + response);
                                shipPlanVo = new Gson().fromJson(response,ShipPlanVo.class);
                                if(shipPlanVo != null && shipPlanVo.isSuccess()){
                                    if (shipPlanVo.getData() != null && shipPlanVo.getData().size
                                            () > 0 && shipPlanVo.getData().get(0).getVoyageDynamicInfos().size() > 0) {
                                        label_Tv.setVisibility(View.GONE);
                                        commitBt.setVisibility(View.VISIBLE);

                                        bargeId = shipPlanVo.getData().get(0).getBargeId();
                                        voyagePlanId = shipPlanVo.getData().get(0).getVoyagePlanId();
                                        voyageStatusId = shipPlanVo.getData().get(0).getVoyageDynamicInfos().get(0).getVoyageStatusId();
                                        currPortId = shipPlanVo.getData().get(0).getVoyageDynamicInfos().get(0).getCurrPortId();

                                        bargeName_Tv.setText(shipPlanVo.getData().get(0).getcShipName());
                                        portTransportOrder_Tv.setText(shipPlanVo.getData().get(0).getPortTransportOrder());
                                        voyageStatus_Tv.setText(shipPlanVo.getData().get(0).getVoyageDynamicInfos().get(0).getVoyageStatusDesc());
                                    }else{
                                        label_Tv.setVisibility(View.VISIBLE);
                                        commitBt.setVisibility(View.GONE);
                                    }
                                }else{//当前没有可报的航次计划
                                    label_Tv.setVisibility(View.VISIBLE);
                                    commitBt.setVisibility(View.GONE);
                                }
                            }
                        }
                );
    }

    public void initViewData() {
        historyAdapter = new OilReportHistoryAdapter(mContext, historyData);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //下拉刷新
                        historyAdapter.notifyDataSetChanged();
                        historyData.clear(); //清除数据
                        pageNum = 1;
                        isLoading = false;
                        getData();
                    }
                }, 1000);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                if (topRowVerticalPosition > 0) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        return;
                    }

                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //上拉加载
                                if (oilReportHistoryVo.getData().isHasNextPage()) {
                                    pageNum++;
                                    getData();//添加数据
                                }
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
        //给List添加点击事件
        historyAdapter.setOnItemClickListener(new OilReportHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OilReportHistoryVo.DataBean.ListBean bean = (OilReportHistoryVo.DataBean.ListBean)
                        historyAdapter.getAll().get(position);
                Intent intent = new Intent(mContext, OilReportMainDetailActivity.class);
                intent.putExtra("oilData",bean);
                startActivity(intent);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {
                //长按
            }
        });
    }

    //刷新数据
    private void updateData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        //清除数据
        historyAdapter.notifyDataSetChanged();
        this.historyData.clear();
        pageNum = 1;
        getData(); //获取数据
    }

    public void initContent(){
        timeTv.setText(DateTool.getSystemDate());

        //初始化 品种
//        List<String> fuelTypeList = new ArrayList<>();
//        for(String key : Config.fuelTypeMap.keySet()){
//            fuelTypeList.add(Config.fuelTypeMap.get(key));
//        }
//        oilType1Tv.setText(fuelTypeList.get(0));
//        oilType2Tv.setText(fuelTypeList.get(1));
//        oilType3Tv.setText(fuelTypeList.get(2));

        //初始化 单位
//        oilUnit1Tv.setText("吨");
//        oilUnit2Tv.setText("吨");
//        oilUnit3Tv.setText("吨");
    }

    public void initLocation(){
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(mContext,Manifest.permission
                .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(mContext,Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(OilReportMainActivity.this,permissions, LOCATION_REQUESTCODE);
        }else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUESTCODE:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(mContext,"必须同意所有的权限才能使用定位",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    getLocation();
                }else {
                    Toast.makeText(mContext,"发送未知错误",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void addWatcher(TextView tv){
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isEmpty()){
                    commitBt.setClickable(true);
                    commitBt.setBackground(getResources().getDrawable(R.drawable.shape_login_button_on));
                }else{
                    commitBt.setClickable(false);
                    commitBt.setBackground(getResources().getDrawable(R.drawable.shape_login_button_off));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isEmpty(){
        if(timeTv.getText().toString().trim().length() != 0 &&
                voyageStatus_Tv.getText().toString().trim().length() != 0 &&
                locationTv.getText().toString().trim().length() != 0 &&
                latitudeTv.getText().toString().trim().length() != 0 &&
                longitudeTv.getText().toString().trim().length() != 0 &&
                oilAmount1_Et.getText().toString().trim().length() != 0 &&
                oilAmount2_Et.getText().toString().trim().length() != 0 &&
                oilAmount3_Et.getText().toString().trim().length() != 0){
            return false;
        }else{
            return true;
        }
    }

    @OnClick({R.id.back_iv,R.id.more_tv,R.id.voyageStatus_tv,R.id.location_tv,R.id
            .add_barge_cabin_iv,R.id.oilUnit1_tv,R.id.oilUnit2_tv,R.id.oilUnit3_tv,R.id.commit_bt,
             R.id.item_current_tv,R.id.item_history_tv})
    public void onclick(View view) {
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.more_tv:
                popup_menu_Ll.setVisibility(View.VISIBLE);
                break;
            case R.id.voyageStatus_tv:
                /*String title = getResources().getString(R.string.choice_label) +
                        getResources().getString(R.string.voyageStatus_label);
                List<String> voyageStatusNameList = new ArrayList<String>();
                for(String key : Config.voyageStatusMap.keySet()){
                    voyageStatusNameList.add(key);
                }
                ShowListValueWindow window1 = new ShowListValueWindow(mContext,title, voyageStatusNameList, voyageStatusTv);
                window1.show();*/
                break;
            case R.id.location_tv:
                if(locationTv.getText().toString().trim().length() == 0){
                    getLocation();
                }
                break;
            case R.id.oilUnit1_tv:
                ShowUnitWindow(oilUnit1Tv);
                break;
            case R.id.oilUnit2_tv:
                ShowUnitWindow(oilUnit2Tv);
                break;
            case R.id.oilUnit3_tv:
                ShowUnitWindow(oilUnit3Tv);
                break;
            case R.id.commit_bt:
                if(!NetWorkUtil.isConn(mContext)){
                    ToastUtil.showNetError();
                }else {
                    String time = timeTv.getText().toString().trim();
                    String bargeName = bargeName_Tv.getText().toString().trim();
                    String portTransportOrder = portTransportOrder_Tv.getText().toString().trim();
                    String status = voyageStatus_Tv.getText().toString().trim();
                    String location = locationTv.getText().toString().trim();
                    String longitude = longitudeTv.getText().toString().trim();
                    String latitude = latitudeTv.getText().toString().trim();
                    String oilType1 = oilType1Tv.getText().toString().trim();
                    String oilUnit1 = oilUnit1Tv.getText().toString().trim();
                    String oilAmount1 = oilAmount1_Et.getText().toString().trim();
                    String oilType2 = oilType2Tv.getText().toString().trim();
                    String oilUnit2 = oilUnit2Tv.getText().toString().trim();
                    String oilAmount2 = oilAmount2_Et.getText().toString().trim();
                    String oilType3 = oilType3Tv.getText().toString().trim();
                    String oilUnit3 = oilUnit3Tv.getText().toString().trim();
                    String oilAmount3 = oilAmount3_Et.getText().toString().trim();

                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view1 = inflater.inflate(R.layout.add_oil_dailog_content,null);
                    final Dialog dialog = new AlertDialog.Builder(mContext)
                            //.setTitle("提示")
                            .setView(view1)
                            //.setMessage(mContext.getResources().getString(R.string
                            //.add_oil_dialog_title))
                            .setCancelable(false)
                            .create();
                    dialog.show();

                    TextView title1 = view1.findViewById(R.id.title_item_tv);
                    TextView time_item_Tv = view1.findViewById(R.id.time_item_tv);

                    TextView bargeName_Tv = view1.findViewById(R.id.bargeName_tv);
                    TextView portTransportOrder_Tv = view1.findViewById(R.id.portTransportOrder_tv);
                    TextView status_item_Tv = view1.findViewById(R.id.status_item_tv);
                    TextView location_item_Tv = view1.findViewById(R.id.location_item_tv);
                    TextView longitude_item_Tv = view1.findViewById(R.id.longitude_item_tv);
                    TextView latitude_item_Tv = view1.findViewById(R.id.latitude_item_tv);
                    TextView oil1_item_Tv = view1.findViewById(R.id.oil1_item_tv);
                    TextView oil2_item_Tv = view1.findViewById(R.id.oil2_item_tv);
                    TextView oil3_item_Tv = view1.findViewById(R.id.oil3_item_tv);
                    TextView cancelTv = view1.findViewById(R.id.cancel_tv);
                    TextView sureTv = view1.findViewById(R.id.sure_tv);

                    title1.setText(getResources().getString(R.string.add_oil_dialog_title));
                    time_item_Tv.setText(getResources().getString(R.string.time_label) + ": " + time);
                    bargeName_Tv.setText(getResources().getString(R.string.ship_name_label) + ": " + bargeName);
                    portTransportOrder_Tv.setText(getResources().getString(R.string
                            .portTransportOrder_label) + ":" + portTransportOrder);
                    status_item_Tv.setText(getResources().getString(R.string.voyageStatus_label) + ": " + status);
                    location_item_Tv.setText(getResources().getString(R.string.location_label) + ": " + location);
                    longitude_item_Tv.setText(getResources().getString(R.string.longitude_label) + ": " + longitude);
                    latitude_item_Tv.setText(getResources().getString(R.string.latitude_label) + ": " + latitude);

                    oil1_item_Tv.setText(oilType1 + ": " + oilAmount1);
                    oil2_item_Tv.setText(oilType2 + ": " + oilAmount2);
                    oil3_item_Tv.setText(oilType3 + ": " + oilAmount3);

                    cancelTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    sureTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commitData();
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.item_current_tv://待报
                if(!isAdd){
                    isAdd = true;
                    initTitle();
                    initView();
                }else{
                    popup_menu_Ll.setVisibility(View.GONE);
                }
                break;
            case R.id.item_history_tv://已报
                if(isAdd){
                    isAdd = false;
                    initTitle();
                    initView();
                }else {
                    popup_menu_Ll.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void ShowUnitWindow(TextView tv){
        unitTitleStr = mContext.getResources().getString(R.string.choice_label) +
                mContext.getResources().getString(R.string.unit_label);
        List<String> unitList = new ArrayList<>();
        for(String key : Config.unitMap.keySet()){
            unitList.add(key);
        }
        ShowListValueWindow window = new ShowListValueWindow(mContext,unitTitleStr,unitList, tv);
        window.show();
    }

    public void commitData(){
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");

        String location = locationTv.getText().toString().trim();
        double longitude = Double.parseDouble(longitudeTv.getText().toString().trim());
        double latitude = Double.parseDouble(latitudeTv.getText().toString().trim());

        //构建子表数据
        //现有油量
        double realStoreQty1 = Double.parseDouble(oilAmount1_Et.getText().toString().trim());
        double realStoreQty2 = Double.parseDouble(oilAmount2_Et.getText().toString().trim());

        //String fuelKind3 = EnumUtil.findKeyByValueSS(Config.fuelTypeMap,oilType3Tv.getText()
               // .toString().trim());
        double realStoreQty3 = Double.parseDouble(oilAmount3_Et.getText().toString().trim());

        //重油 30，轻油 31
        List<OilReportMainReq.ShipFualDynamicInfosubsBean> list = new ArrayList<>();
        OilReportMainReq.ShipFualDynamicInfosubsBean bean1 = new OilReportMainReq
                .ShipFualDynamicInfosubsBean(30,realStoreQty1);
        OilReportMainReq.ShipFualDynamicInfosubsBean bean2 = new OilReportMainReq
                .ShipFualDynamicInfosubsBean(31,realStoreQty2);
        OilReportMainReq.ShipFualDynamicInfosubsBean bean3 = new OilReportMainReq
                .ShipFualDynamicInfosubsBean(32,realStoreQty3);

        list.add(bean1);
        list.add(bean2);
        list.add(bean3);

        String reportTime = timeTv.getText().toString().trim();
        int reporterId = Config.accountId;
        String json = new Gson().toJson(new OilReportMainReq(currPortId,reportTime,reporterId,
                bargeId, voyagePlanId, voyageStatusId, location,longitude,latitude,list));
        OkHttpUtils
                .postString()
                .url(Config.add_oil_report_main_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
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
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                ToastUtil.showToast(getResources().getString(R.string.commit_success_msg));
                                oilAmount1_Et.setText("");
                                oilAmount2_Et.setText("");
                                oilAmount3_Et.setText("");
                            }else{
                                ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                            }
                        }catch (JSONException e){

                        }
                    }
                });
    }

    //获取已报的历史燃油数据
    public void getData() {
        String json = new Gson().toJson(new OilReportHistoryReq(pageNum, 10, "",
                new OilReportHistoryReq.ShipFualDynamicInfoBean(null,null,null)));

        Log.e(TAG, "oilreporthistory request: " + json);
        OkHttpUtils
                .postString()
                .url(Config.oil_report_history_url)
                //.addHeader("checkTokenKey", Config.TOKEN)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                swipeRefreshLayout.setRefreshing(false);
                                ToastUtil.showNetError();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG,"oilreporthistory response is: " + response);

                                oilReportHistoryVo = new Gson().fromJson(response, OilReportHistoryVo.class);
                                if (oilReportHistoryVo != null) {
                                    if (oilReportHistoryVo.isSuccess()) {
                                        if (oilReportHistoryVo.getData().getList() != null && oilReportHistoryVo.getData().getList().size() > 0) {
                                            for (int i = 0; i < oilReportHistoryVo.getData().getList().size(); i++) {
                                                historyData.add(oilReportHistoryVo.getData().getList().get(i));
                                            }
                                        }
                                        if (oilReportHistoryVo.getData().isHasNextPage()) {
                                            historyAdapter.setNotMoreData(false);
                                        } else {
                                            historyAdapter.setNotMoreData(true);
                                        }
                                        historyAdapter.notifyDataSetChanged();
                                        historyAdapter.notifyItemRemoved(historyAdapter.getItemCount());
                                    }
                                } else {
                                    historyAdapter.notifyDataSetChanged();
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                );
    }

    public void getLocation(){
        //added by heqiang
        //locationService = ((ApkApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        //locationService.registerListener(mListener);
        //注册监听
        locationService = ((ApkApplication) getApplication()).locationService;
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.registerListener(mListener);
        locationService.start();// 定位SDK
        isFinishlocating = false;
        sendMsg(STARTTOLOCATING);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(requestLocTime);
                    if(!isFinishlocating)
                        sendMsg(LOCATEFAIL);
                }catch (Exception e){
                    sendMsg(LOCATEFAIL);
                }
            }
        }).start();

    }

    public void startLocationService(){
        if(locationTv.getText().toString().trim().length() == 0){
            initLocation();
        }
    }

    public void stopLocationService(){
        if(locationService != null){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        stopLocationService();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //location.getLocType() = 161 表示定位成功
            Log.d("loclog","loc status is: " + location.getLocType());
            if (null != location && location.getLocType() == BDLocation.TypeNetWorkLocation) {
                locationService.unregisterListener(mListener);
                locationService.stop();
                StringBuffer sb = new StringBuffer(256);
                sb.append("\nlatitude : ");// 纬度
                latitude = location.getLatitude();
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                longitude = location.getLongitude();
                sb.append(location.getLongitude());
                //latitude = location.getLatitude();
                //longitude = location.getLongitude();
                /*if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    pois.clear();
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        pois.add(poi);
                        sb.append(poi.getName() + ";");
                    }
                }*/
                if(location.getAddress() != null && location.getCity() != null){
                    Log.d("loclog","loc is :" + location.getAddrStr());
                    locSb = new StringBuffer();
//                    locSb.append(location.getCity());
//                    locSb.append(location.getDistrict());
//                    locSb.append(location.getStreet());
//                    locSb.append(location.getStreetNumber());
                    locSb.append(location.getAddrStr());
                    sendMsg(LOCATESUCCESS);
                }else{
                    sendMsg(LOCATEFAIL);
                }
            }else{
                sendMsg(LOCATEFAIL);
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    /***********地图定位相关end************/

    public void sendMsg(int flag){
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();

            if (null != popup_menu_Ll && popup_menu_Ll.getVisibility() == View.VISIBLE) {
                Rect hitRect = new Rect();
                popup_menu_Ll.getGlobalVisibleRect(hitRect);
                if (!hitRect.contains(x, y)) {
                    popup_menu_Ll.setVisibility(View.GONE);
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
