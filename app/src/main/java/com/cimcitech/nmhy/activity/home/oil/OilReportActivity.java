package com.cimcitech.nmhy.activity.home.oil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.ApkApplication;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.oil.OilReportReq;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DataCleanManager;
import com.cimcitech.nmhy.utils.DateTool;
import com.cimcitech.nmhy.utils.EnumUtil;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.utils.WhiteIcon;
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
import okhttp3.OkHttpClient;


public class OilReportActivity extends MyBaseActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;

    @Bind(R.id.commit_bt)
    Button commit_Bt;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_ll)
    LinearLayout content_Ll;
    @Bind(R.id.time_tv)
    TextView time_Tv;
    @Bind(R.id.voyageStatus_tv)
    TextView voyageStatus_Tv;
    @Bind(R.id.location_tv)
    TextView location_Tv;
    @Bind(R.id.longitude_tv)
    TextView longitude_Tv;
    @Bind(R.id.latitude_tv)
    TextView latitude_Tv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;
    @Bind(R.id.add_ib)
    ImageButton add_Ib;

    public DataCleanManager manager = null;
    private final Context mContext = OilReportActivity.this;
    private SharedPreferences sp;

    public static final String CALL_FINISH = "com.cimcitech.lyt.mainactivity.finish";
    private final int REQUESTCODE_LIGHT_OIL = 1;
    private final int REQUESTCODE_HEAVY_OIL = 2;
    private final int REQUESTCODE_MACHINE_OIL = 3;
    private NetWorkUtil netWorkUtil = null;
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
    private CatLoadingView mLoadingView = null;
    private boolean isAdd = true;
    private OilReportHistoryVo.DataBean.ListBean oilData = null;
    private String TAG = "oillog";

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
                    location_Tv.setText("测试地1");
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(mContext,"定位失败！请检查网络或GPS",Toast.LENGTH_SHORT).show();
                    break;
            }
            addWatcher(voyageStatus_Tv);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_report);
        ButterKnife.bind(this);

        oilData = (OilReportHistoryVo.DataBean.ListBean)getIntent().getSerializableExtra("oilData");
        if(oilData == null){
            isAdd = true;
        }else{
            isAdd = false;
        }

        initTitle();

        initView();
        getData();
    }

    public void initTitle(){
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
        if(isAdd){
            add_Ib.setVisibility(View.GONE);
            commit_Bt.setText(getResources().getString(R.string.btn_commit_label));
            titleName_Tv.setText(getResources().getString(R.string.add_oil_report_label));
            voyageStatus_Tv.setClickable(true);
            locationService = ((ApkApplication) getApplication()).locationService;
            locationService.registerListener(mListener);
        }else{
            add_Ib.setVisibility(View.VISIBLE);
            commit_Bt.setText(getResources().getString(R.string.query_oil_report_detail_label));
            titleName_Tv.setText(getResources().getString(R.string.query_oil_report_label));
            WhiteIcon voyageStatusWhiteIcon = new WhiteIcon(mContext,voyageStatus_Tv);
            voyageStatusWhiteIcon.setWhiteIcon();
        }
    }

    public void initView(){
        addWatcher(time_Tv);
        addWatcher(voyageStatus_Tv);
        addWatcher(location_Tv);
        addWatcher(longitude_Tv);
        addWatcher(latitude_Tv);
        if(isAdd){
            if(NetWorkUtil.isConn(mContext)){
                empty_Rl.setVisibility(View.GONE);
                content_Ll.setVisibility(View.VISIBLE);
                initContent();
                initLocation();
            }else {
                empty_Rl.setVisibility(View.VISIBLE);
                content_Ll.setVisibility(View.GONE);
            }
        }else {
            empty_Rl.setVisibility(View.GONE);
            content_Ll.setVisibility(View.VISIBLE);
            time_Tv.setText(oilData.getReportTime() + "");
//            voyageStatus_Tv.setText(EnumUtil.findKeyByValueSI(Config.voyageStatusMap,Integer
//                    .parseInt(oilData.getVoyageStatus())));
            location_Tv.setText(oilData.getLocation() + "");
            longitude_Tv.setText(oilData.getLongitude() + "");
            latitude_Tv.setText(oilData.getLatitude() + "");
        }
    }

    public void getData(){
        int voyagePlanId = 101;
        String json = "";
        OkHttpUtils
                .postString()
                .url(Config.get_current_voyagePlan_info_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.showNetError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                ToastUtil.showToast(getResources().getString(R.string.commit_success_msg));
                                int dynamicinfoId = object.getInt("id");
                                Intent i = new Intent(mContext,OilReportHistoryActivity.class);
                                startActivity(i);
                            }else{
                                ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                            }
                        }catch (JSONException e){

                        }
                    }
                });
    }

    public void initContent(){
        time_Tv.setText(DateTool.getSystemDate());
        commit_Bt.setClickable(false);
    }

    public void sendMsg(int flag){
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
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
            ActivityCompat.requestPermissions(OilReportActivity.this,permissions,LOCATION_REQUESTCODE);
        }else {
            getLocation();
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
                    commit_Bt.setClickable(true);
                    commit_Bt.setBackground(getResources().getDrawable(R.drawable.shape_login_button_on));
                }else{
                    commit_Bt.setClickable(false);
                    commit_Bt.setBackground(getResources().getDrawable(R.drawable.shape_login_button_off));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isEmpty(){
        if(voyageStatus_Tv.getText().toString().trim().length() != 0 &&
                location_Tv.getText().toString().trim().length() != 0 &&
                longitude_Tv.getText().toString().trim().length() != 0 &&
                latitude_Tv.getText().toString().trim().length() != 0){
            return false;
        }else{
            return true;
        }
    }

    @OnClick({R.id.back_iv,R.id.voyageStatus_tv,R.id.commit_bt,R.id.location_tv,R.id.add_ib})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.commit_bt:
                if(isAdd){//提交 新的燃油动态
                    commitData();
                }else {//查看 燃油动态明细
                    Intent i = new Intent(mContext,OilReportHistoryDetailActivity.class);
                    i.putExtra("dynamicinfoId",oilData.getDynamicinfoId());
                    i.putExtra("isAdd",false);
                    startActivity(i);
                }
                break;
            case R.id.location_tv:
                if(location_Tv.getText().toString().trim().equals("")){
                    getLocation();
                }
                break;
            case R.id.voyageStatus_tv:
//                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.voyageStatus_label),
//                        voyageStatus_Tv.getText().toString().trim(),1);

                String title = getResources().getString(R.string.choice_label) +
                        getResources().getString(R.string.voyageStatus_label);
                List<String> voyageStatusNameList = new ArrayList<String>();
                for(String key : Config.voyageStatusMap.keySet()){
                    voyageStatusNameList.add(key);
                }
                ShowListValueWindow window = new ShowListValueWindow(mContext,title,voyageStatusNameList, voyageStatus_Tv);
                window.show();
                break;
            case R.id.add_ib://新增  燃油动态
                Intent i = new Intent(mContext,OilReportHistoryDetailActivity.class);
                i.putExtra("dynamicinfoId",oilData.getDynamicinfoId());
                i.putExtra("isAdd",true);
                startActivity(i);
                break;
        }
    }

    public void startEditActivity(String type,String title,String content,int requestCode){
        Intent intent2 = new Intent(mContext, EditValueActivity.class);
        intent2.putExtra("type",type);
        intent2.putExtra("title",title);
        intent2.putExtra("content",content);
        startActivityForResult(intent2,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            String result = data.getStringExtra("result");
            switch (requestCode){
                case 1:
                    voyageStatus_Tv.setText(result);
                    break;
            }
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

    public void getLocation(){
        //added by heqiang
        //locationService = ((ApkApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        //locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
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

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if(locationService != null){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
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

    public void commitData(){
        mLoadingView = new CatLoadingView();
        mLoadingView.show(getSupportFragmentManager(),"");
        long bargeId = 5;
        long voyagePlanId = 101;
        int voyageStatus = EnumUtil.findValueByKeySI(Config.voyageStatusMap,voyageStatus_Tv
                .getText().toString().trim());
        Log.d(TAG,"voyageStatus is: " + voyageStatus);
        String location = location_Tv.getText().toString().trim();

        location = location.length() == 0 ? "测试地址1":location;
        location_Tv.setText(location);
        String json = new Gson().toJson(new OilReportReq(bargeId,voyagePlanId,voyageStatus,
               location,longitude,latitude));
        OkHttpUtils
                .postString()
                .url(Config.add_oil_report_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoadingView.dismiss();
                        ToastUtil.showNetError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mLoadingView.dismiss();
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                ToastUtil.showToast(getResources().getString(R.string.commit_success_msg));
                                int dynamicinfoId = object.getInt("id");
                                Intent i = new Intent(mContext,OilReportHistoryActivity.class);
                                startActivity(i);
                            }else{
                                ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                            }
                        }catch (JSONException e){

                        }
                    }
                });

    }
}
