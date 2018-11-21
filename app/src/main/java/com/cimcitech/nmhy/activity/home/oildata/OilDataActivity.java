package com.cimcitech.nmhy.activity.home.oildata;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.ApkApplication;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.activity.main.LoginActivity;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.utils.DataCleanManager;
import com.cimcitech.nmhy.utils.DateTool;
import com.cimcitech.nmhy.utils.NetWorkUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OilDataActivity extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.light_oil_tv)
    TextView light_oil_Tv;
    @Bind(R.id.heavy_oil_tv)
    TextView heavy_oil_Tv;
    @Bind(R.id.machine_oil_tv)
    TextView machine_oil_Tv;
    @Bind(R.id.commit_bt)
    Button commit_Bt;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_ll)
    LinearLayout content_Ll;
    @Bind(R.id.time_tv)
    TextView time_Tv;
    @Bind(R.id.location_tv)
    TextView location_Tv;
    @Bind(R.id.ship_name_tv)
    TextView ship_name_Tv;
    @Bind(R.id.ship_type_tv)
    TextView ship_type_Tv;
    @Bind(R.id.ship_voyageNo_tv)
    TextView ship_voyageNo_Tv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;

    public DataCleanManager manager = null;
    private final Context context = OilDataActivity.this;
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
    private static final int requestLocTime = 7000;
    private boolean isFinishlocating = false;
    private final int LOCATION_REQUESTCODE = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STARTTOLOCATING:
                    isFinishlocating = false;
                    dialog = new ProgressDialog(OilDataActivity.this);
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
                    }
                    break;
                case LOCATEFAIL:
                    isFinishlocating = true;
                    location_Tv.setText("");
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(OilDataActivity.this,"定位失败！请检查网络或GPS",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_data);
        ButterKnife.bind(this);
        initTitle();

        locationService = ((ApkApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        initView();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_oil));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
    }

    public void initView(){
        if(NetWorkUtil.isConn(OilDataActivity.this)){
            empty_Rl.setVisibility(View.GONE);
            content_Ll.setVisibility(View.VISIBLE);
            addWatcher(light_oil_Tv);
            addWatcher(heavy_oil_Tv);
            addWatcher(machine_oil_Tv);
            light_oil_Tv.setText("12.22");
            heavy_oil_Tv.setText("12.22");
            machine_oil_Tv.setText("12.22");
            initContent();
            initLocation();
        }else {
            empty_Rl.setVisibility(View.VISIBLE);
            content_Ll.setVisibility(View.GONE);
        }
    }

    public void initContent(){
        time_Tv.setText(DateTool.getSystemDate());
    }

    public void sendMsg(int flag){
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    public void initLocation(){
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(OilDataActivity.this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(OilDataActivity.this,Manifest.permission
                .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(OilDataActivity.this,Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(OilDataActivity.this,permissions,LOCATION_REQUESTCODE);
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
                if(light_oil_Tv.getText().toString().trim().length() != 0 &&
                        heavy_oil_Tv.getText().toString().trim().length() != 0 &&
                        machine_oil_Tv.getText().toString().trim().length() != 0){
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

    @OnClick({R.id.back_iv,R.id.light_oil_tv,R.id.heavy_oil_tv,R.id.machine_oil_tv,R.id.commit_bt,
              R.id.more_tv,R.id.location_tv,R.id.item_current_tv,R.id.item_history_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.light_oil_tv:
                startEditActivity("num",getResources().getString(R.string.type_light_oil),
                        light_oil_Tv.getText().toString().trim(),REQUESTCODE_LIGHT_OIL);
                break;
            case R.id.heavy_oil_tv:
                startEditActivity("num",getResources().getString(R.string.type_heavy_oil),
                        heavy_oil_Tv.getText().toString().trim(),REQUESTCODE_HEAVY_OIL);
                break;
            case R.id.machine_oil_tv:
                startEditActivity("num",getResources().getString(R.string.type_machine_oil),
                        machine_oil_Tv.getText().toString().trim(),REQUESTCODE_MACHINE_OIL);
                break;
            case R.id.commit_bt:

                break;
            case R.id.more_tv:
                popup_menu_Ll.setVisibility(View.VISIBLE);
                break;
            case R.id.location_tv:
                if(location_Tv.getText().toString().trim().equals("")){
                    getLocation();
                }
                break;
            case R.id.item_current_tv:
                popup_menu_Ll.setVisibility(View.GONE);
                break;
            case R.id.item_history_tv:
                popup_menu_Ll.setVisibility(View.GONE);
                break;
        }
    }

    public void startEditActivity(String type,String title,String content,int requestCode){
        Intent intent2 = new Intent(OilDataActivity.this, EditValueActivity.class);
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
                case REQUESTCODE_LIGHT_OIL:
                    light_oil_Tv.setText(result);
                    break;
                case REQUESTCODE_HEAVY_OIL:
                    heavy_oil_Tv.setText(result);
                    break;
                case REQUESTCODE_MACHINE_OIL:
                    machine_oil_Tv.setText(result);
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
                            Toast.makeText(OilDataActivity.this,"必须同意所有的权限才能使用定位",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    getLocation();
                }else {
                    Toast.makeText(OilDataActivity.this,"发送未知错误",Toast.LENGTH_SHORT).show();
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
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
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
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
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
                    locSb = new StringBuffer();
                    locSb.append(location.getCity());
                    locSb.append(location.getDistrict());
                    locSb.append(location.getStreet());
                    locSb.append(location.getStreetNumber());
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
}
