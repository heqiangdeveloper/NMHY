package com.cimcitech.nmhy.activity.home.plan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.ApkApplication;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.bean.plan.ShipPlanDetailVo;
import com.cimcitech.nmhy.bean.plan.ShipPlanDynamicReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DateTool;
import com.cimcitech.nmhy.utils.EventBusMessage;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.DateTimePickDialog;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
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


public class StartVoyagePlanActivity extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.portName_tv)
    TextView portName_Tv;
    @Bind(R.id.jobTypeValue_tv)
    TextView jobTypeValue_Tv;
    @Bind(R.id.voyageStatusDesc_tv)
    TextView voyageStatusDesc_Tv;
    @Bind(R.id.reason_tv)
    EditText reason_Et;
    @Bind(R.id.estimatedTime_tv)
    TextView estimatedTime_Tv;
    @Bind(R.id.occurTime_tv)
    TextView occurTime_Tv;
    @Bind(R.id.reportTime_tv)
    TextView reportTime_Tv;
    @Bind(R.id.location_tv)
    TextView location_Tv;
    @Bind(R.id.longitude_tv)
    TextView longitude_Tv;
    @Bind(R.id.latitude_tv)
    TextView latitude_Tv;
    @Bind(R.id.speed_tv)
    EditText speed_Et;
    @Bind(R.id.weather_tv)
    EditText weather_Et;
    @Bind(R.id.feedback_tv)
    EditText feedback_Et;
    @Bind(R.id.remark_tv)
    EditText remark_Et;
    @Bind(R.id.content_sv)
    ScrollView contentSv;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.commit_bt)
    Button commitBt;

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
    private Context mContext = StartVoyagePlanActivity.this;
    private List<ShipPlanDetailVo.DataBean.ListBean> data = new ArrayList<>();
    private int pageNum = 1;
    private ShipPlanDetailVo shipPlanDetailVo = null;
    private ShipPlanVo.DataBean.VoyageDynamicInfosBean item = null;
    private CatLoadingView mCatLoadingView = null;
    private Calendar calendar;
    private SimpleDateFormat sdf;
    private String nowTimeStr = "";
    private boolean isAdd = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STARTTOLOCATING:
                    isFinishlocating = false;
                    dialog = new ProgressDialog(mContext);
                    dialog.setMessage("定位中…");
                    dialog.setCancelable(true);
                    dialog.show();
                    break;
                case LOCATESUCCESS:
                    isFinishlocating = true;
                    if(dialog.isShowing())
                        dialog.dismiss();
                    if(locSb != null && locSb.toString().length() != 0 && !locSb.toString().contains("null")){
                        location_Tv.setText(locSb.toString());
                        longitude_Tv.setText(longitude + "");
                        latitude_Tv.setText(latitude + "");
                    }
                    break;
                case LOCATEFAIL:
                    isFinishlocating = true;
//                    locationTv.setText("测试地1");
//                    longitudeTv.setText(0 + "");
//                    latitudeTv.setText(0 + "");
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(mContext,"定位失败！请检查网络或GPS",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_voyage_plan);
        ButterKnife.bind(this);

        isAdd = getIntent().getBooleanExtra("isAdd",false);
        item = (ShipPlanVo.DataBean.VoyageDynamicInfosBean)getIntent().getParcelableExtra("item");

        initTitle();
        calendar = Calendar.getInstance();
        //getData();
    }

    public void initTitle(){
        titleName_Tv.setText(isAdd?getResources().getString(R.string.add_voyage_plan_label) :
                getResources().getString(R.string.voyage_plan_detail_label));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
        if(!NetWorkUtil.isConn(mContext)){
            empty_Rl.setVisibility(View.VISIBLE);
            contentSv.setVisibility(View.GONE);
        }else{
            empty_Rl.setVisibility(View.GONE);
            contentSv.setVisibility(View.VISIBLE);

//            addWatcher(timeTv);
//            addWatcher(voyageStatusTv);
//            addWatcher(locationTv);
//            addWatcher(latitudeTv);
//            addWatcher(longitudeTv);
//            addWatcher(oilAmount1Et); addWatcher(oilAmount2Et); addWatcher(oilAmount3Et);
            if(isAdd){//新增
                commitBt.setVisibility(View.VISIBLE);
                initData();
                initContent();
                startLocationService();
            }else {//查看
                commitBt.setVisibility(View.GONE);
                initDetailData();
            }
        }
    }

    public void initDetailData(){
        portName_Tv.setText(item.getPortName());
        jobTypeValue_Tv.setText(item.getJobTypeValue());
        voyageStatusDesc_Tv.setText(item.getPortName());
        reason_Et.setText(item.getReason());
        estimatedTime_Tv.setText(item.getEstimatedTime());
        occurTime_Tv.setText(item.getOccurTime());
        reportTime_Tv.setText(item.getReportTime());
        location_Tv.setText(item.getLocation());
        longitude_Tv.setText(item.getLongitude() + "");
        latitude_Tv.setText(item.getLatitude() + "");
        speed_Et.setText(item.getSpeed() + "");
        weather_Et.setText(item.getWeather());
        feedback_Et.setText(item.getFeedback());
        remark_Et.setText(item.getRemark());

        portName_Tv.setHint("");
        jobTypeValue_Tv.setHint("");
        voyageStatusDesc_Tv.setHint("");
        estimatedTime_Tv.setHint("");
        occurTime_Tv.setHint("");
        reportTime_Tv.setHint("");
        location_Tv.setHint("");
        longitude_Tv.setHint("");

        //设置编辑框 为不可编辑状态
        reason_Et.setEnabled(false);reason_Et.setHint("");
        speed_Et.setEnabled(false);speed_Et.setHint("");
        weather_Et.setEnabled(false);weather_Et.setHint("");
        feedback_Et.setEnabled(false);feedback_Et.setHint("");
        remark_Et.setEnabled(false);remark_Et.setHint("");
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
//        if(timeTv.getText().toString().trim().length() != 0 &&
//                voyageStatusTv.getText().toString().trim().length() != 0 &&
//                locationTv.getText().toString().trim().length() != 0 &&
//                latitudeTv.getText().toString().trim().length() != 0 &&
//                longitudeTv.getText().toString().trim().length() != 0 &&
//                oilAmount1Et.getText().toString().trim().length() != 0 &&
//                oilAmount2Et.getText().toString().trim().length() != 0 &&
//                oilAmount3Et.getText().toString().trim().length() != 0){
//            return false;
//        }else{
//            return true;
//        }
        return true;
    }

    public void initContent(){
        nowTimeStr = DateTool.getSystemDate();
        //nowTimeStr = nowTimeStr.substring(0,10);
        estimatedTime_Tv.setText(nowTimeStr);
        occurTime_Tv.setText(nowTimeStr);
        reportTime_Tv.setText(nowTimeStr);
    }

    private void showSelectDateDialog2() {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour= 0;
        int minute = 0;

        String str = occurTime_Tv.getText().toString();
        String strDate = str.split(" ")[0];
        String strTime = str.split(" ")[1];
        year = Integer.parseInt(strDate.substring(0,4));//年
        month = Integer.parseInt(strDate.substring(5,7)) - 1;//月
        day = Integer.parseInt(strDate.substring(8,10));//日

        hour = Integer.parseInt(strTime.split(":")[0]);//时
        minute = Integer.parseInt(strTime.split(":")[1]);//分

        String title = getResources().getString(R.string.select_time_label);
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
                                calendar.set(Calendar.SECOND,0);

                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String selectTimeStr = sdf.format(calendar.getTime());
                                if(selectTimeStr.compareTo(nowTimeStr) <= 0){
                                    occurTime_Tv.setText(selectTimeStr);
                                }else {
                                    ToastUtil.showToast("发生时间必须在报告时间之前！");
                                }
                            }
                        });
    }

    @OnClick({R.id.back_iv,R.id.location_tv,R.id.commit_bt,R.id.occurTime_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.location_tv:
                if(isAdd && location_Tv.getText().toString().trim().length() == 0){
                    getLocation();
                }
                break;
            case R.id.commit_bt:
                new AlertDialog.Builder(mContext)
                        //.setTitle("提示")
                        .setMessage(mContext.getResources().getString(R.string.add_oil_dialog_title))
                        .setCancelable(true)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                commitData();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();

                break;
            case R.id.occurTime_tv:
                showSelectDateDialog2();
                break;
        }
    }

    public void commitData() {
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        long dynamicId = item.getDynamicId();
        long voyagePlanId = item.getVoyagePlanId();
        int currPortId = item.getCurrPortId();
        String estimatedTime = estimatedTime_Tv.getText().toString().trim();
        String jobType = item.getJobType();
        String reportTime = reportTime_Tv.getText().toString().trim();
        String occurTime = occurTime_Tv.getText().toString().trim();
        final int reportId = Config.accountId;
        String voyageStatus = item.getVoyageStatus();
        String voyageStatusDesc = item.getVoyageStatusDesc();
        String reason =reason_Et.getText().toString().trim();
        String location = location_Tv.getText().toString().trim();
        String longitudeStr = longitude_Tv.getText().toString().trim();
        String latitudeStr = latitude_Tv.getText().toString().trim();
        double longitude = Double.parseDouble(longitudeStr.length() == 0 ? "0": longitudeStr);
        double latitude = Double.parseDouble(latitudeStr.length() == 0 ? "0": latitudeStr);
        String speedStr = speed_Et.getText().toString().trim();
        double speed = Double.parseDouble(speedStr.length() == 0 ? "0": speedStr);
        String weather = weather_Et.getText().toString().trim();
        String remark = remark_Et.getText().toString().trim();
        String fstatus = "";
        String feedback = feedback_Et.getText().toString().trim();
        long voyageStatusId = item.getVoyageStatusId();

        String json = new Gson().toJson(new ShipPlanDynamicReq(dynamicId,
                voyagePlanId,
                currPortId,
                estimatedTime,
                jobType,
                reportTime,
                occurTime,
                reportId,
                voyageStatus,
                voyageStatusDesc,
                reason,
                location,
                longitude,
                latitude,
                speed,
                weather,
                remark,
                fstatus,
                feedback,
                reportId,
                reportTime,
                voyageStatusId));
        OkHttpUtils
                .postString()
                .url(Config.save_voyage_plan_dynamic_url)
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
                                        EventBus.getDefault().post(new EventBusMessage("addShipPlanSuc"));

                                        ToastUtil.showToast(getResources().getString(R.string.commit_success_msg));
                                        location_Tv.setText("");
                                        finish();
                                    }else{
                                        ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                                    }
                                }catch (JSONException e){
                                    ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                                }
                            }
                        }
                );
    }

    public void initData(){
        portName_Tv.setText(item.getPortName());
        jobTypeValue_Tv.setText(item.getJobTypeValue());
        voyageStatusDesc_Tv.setText(item.getVoyageStatusDesc());
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
            ActivityCompat.requestPermissions(StartVoyagePlanActivity.this,permissions, LOCATION_REQUESTCODE);
        }else {
            getLocation();
        }
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

    public void startLocationService(){
        if(location_Tv.getText().toString().trim().length() == 0){
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
}
