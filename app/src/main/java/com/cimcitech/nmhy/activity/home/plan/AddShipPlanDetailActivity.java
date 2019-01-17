package com.cimcitech.nmhy.activity.home.plan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.tv.TvTrackInfo;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.home.oil.OilReportMainActivity;
import com.cimcitech.nmhy.activity.main.ApkApplication;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.bean.plan.ShipPlanDetailReq;
import com.cimcitech.nmhy.bean.plan.ShipPlanDetailVo;
import com.cimcitech.nmhy.bean.plan.ShipTableBean;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;


public class AddShipPlanDetailActivity extends AppCompatActivity {
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
    EditText reason_Tv;
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
    EditText speed_Tv;
    @Bind(R.id.weather_tv)
    EditText weather_Tv;
    @Bind(R.id.feedback_tv)
    EditText feedback_Tv;
    @Bind(R.id.remark_tv)
    EditText remark_Tv;
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
    private Context mContext = AddShipPlanDetailActivity.this;
    private List<ShipPlanDetailVo.DataBean.ListBean> data = new ArrayList<>();
    private int pageNum = 1;
    private ShipPlanDetailVo shipPlanDetailVo = null;
    private ShipPlanDetailVo.DataBean.ListBean item = null;
    private CatLoadingView mCatLoadingView = null;

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
        setContentView(R.layout.activity_add_ship_plan_detail);
        ButterKnife.bind(this);

        item = (ShipPlanDetailVo.DataBean.ListBean)getIntent().getSerializableExtra("item");
        initTitle();
        //getData();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.ship_plan_detail_title));
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
            initData();
            initContent();
            startLocationService();
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

    }

    @OnClick({R.id.back_iv,R.id.location_tv,R.id.commit_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.location_tv:
                if(location_Tv.getText().toString().trim().length() == 0){
                    getLocation();
                }
                break;
            case R.id.commit_bt:
                break;
        }
    }

    public void getData() {
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        String json = new Gson().toJson(new ShipPlanDetailReq(pageNum,10,"",new ShipPlanDetailReq
                .VoyageDynamicInfoBean(-1)));
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
            ActivityCompat.requestPermissions(AddShipPlanDetailActivity.this,permissions, LOCATION_REQUESTCODE);
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
