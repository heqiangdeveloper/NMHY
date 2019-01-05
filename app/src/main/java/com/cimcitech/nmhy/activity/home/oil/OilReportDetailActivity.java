package com.cimcitech.nmhy.activity.home.oil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.bean.oil.OilReportDetailReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilReportReq;
import com.cimcitech.nmhy.bean.oil.OilReq;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.utils.ShowValueWindow;
import com.cimcitech.nmhy.utils.ToastUtil;
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


public class OilReportDetailActivity extends AppCompatActivity {
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
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;

    @Bind(R.id.fuelKind_tv)
    TextView fuelKind_Tv;
    @Bind(R.id.unit_tv)
    TextView unit_Tv;
    @Bind(R.id.realStoreQty_tv)
    TextView realStoreQty_Tv;
    @Bind(R.id.realyAmount_tv)
    TextView realyAmount_Tv;
    @Bind(R.id.taxfreeRealyAmount_tv)
    TextView taxfreeRealyAmount_Tv;
    @Bind(R.id.addFuelQty_tv)
    TextView addFuelQty_Tv;
    @Bind(R.id.addAmount_tv)
    TextView addAmount_Tv;
    @Bind(R.id.taxfreeAddAmount_tv)
    TextView taxfreeAddAmount_Tv;

    private PopupWindow pop = null;
    private final Context context = OilReportDetailActivity.this;
    private SharedPreferences sp;
    private final int realStoreQty_code = 1;
    private final int realyAmount_code = 2;
    private final int taxfreeRealyAmount_code = 3;
    private final int addFuelQty_code = 4;
    private final int addAmount_code = 5;
    private final int taxfreeAddAmount_code = 6;
    private CatLoadingView mLoadingView = null;
    private long dynamicinfoId = -1;
    private boolean isAdd = true;
    private List<OilReportHistoryDetailVo.DataBean.OilData> data = new ArrayList<>();
    private OilReportHistoryDetailVo oilReportHistoryDetailVo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_report_detail);
        ButterKnife.bind(this);
        dynamicinfoId = getIntent().getLongExtra("dynamicinfoId",-1);
        isAdd = getIntent().getBooleanExtra("isAdd",false);

        initTitle();
        initView();
    }

    public void initTitle(){
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
    }

    public void initView(){
        addWatcher(fuelKind_Tv);
        addWatcher(unit_Tv);
        addWatcher(realStoreQty_Tv);
        addWatcher(realyAmount_Tv);
        addWatcher(taxfreeRealyAmount_Tv);
        addWatcher(addFuelQty_Tv);
        addWatcher(addAmount_Tv);
        addWatcher(taxfreeAddAmount_Tv);
        if(isAdd){//新增
            titleName_Tv.setText(getResources().getString(R.string.add_oil_report_detail_label));
            showContent();
            commit_Bt.setVisibility(View.VISIBLE);
        }else {//查看
            titleName_Tv.setText(getResources().getString(R.string.query_oil_report_detail_label));
            showEmpty();
            commit_Bt.setVisibility(View.GONE);
            if(NetWorkUtil.isConn(OilReportDetailActivity.this)){
                mLoadingView = new CatLoadingView();
                mLoadingView.setCancelable(false);
                getData();
            }
        }
    }

    public void showEmpty(){
        empty_Rl.setVisibility(View.VISIBLE);
        content_Ll.setVisibility(View.GONE);
    }

    public void showContent(){
        empty_Rl.setVisibility(View.GONE);
        content_Ll.setVisibility(View.VISIBLE);
        //showWhiteIcon();
    }

    public void showWhiteIcon(){
        setDrawable(unit_Tv);
        setDrawable(realyAmount_Tv);setDrawable(realStoreQty_Tv);setDrawable(taxfreeRealyAmount_Tv);
        setDrawable(addAmount_Tv);setDrawable(addFuelQty_Tv);setDrawable(taxfreeAddAmount_Tv);
    }

    public void setDrawable(TextView tv){
        tv.setCompoundDrawables(null,null,null,null);
        tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.drawable_padding_size));
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
        if(fuelKind_Tv.getText().toString().trim().length() != 0 &&
                unit_Tv.getText().toString().trim().length() != 0 &&
                realStoreQty_Tv.getText().toString().trim().length() != 0 &&
                realyAmount_Tv.getText().toString().trim().length() != 0 &&
                taxfreeRealyAmount_Tv.getText().toString().trim().length() != 0 &&
                addFuelQty_Tv.getText().toString().trim().length() != 0 &&
                addAmount_Tv.getText().toString().trim().length() != 0 &&
                taxfreeAddAmount_Tv.getText().toString().trim().length() != 0 ){
            return false;
        }else{
            return true;
        }
    }

    @OnClick({R.id.back_iv,R.id.fuelKind_tv,R.id.unit_tv,R.id.realStoreQty_tv,
    R.id.realyAmount_tv,R.id.taxfreeRealyAmount_tv,R.id.addFuelQty_tv,R.id.addAmount_tv,
    R.id.taxfreeAddAmount_tv,R.id.commit_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;

            case R.id.commit_bt:
                commitData();
                break;
            case R.id.fuelKind_tv:
                List<String> fuelKindList = new ArrayList<>();
                fuelKindList.add("轻油（吨）");
                fuelKindList.add("重油（吨）");
                fuelKindList.add("机油（桶）");
                String fuelKindTitle = context.getResources().getString(R.string.choice_label) +
                        context.getResources().getString(R.string.fuelKind_label);
                ShowListValueWindow fuelKindWindow = new ShowListValueWindow
                        (OilReportDetailActivity.this,fuelKindTitle,fuelKindList,(TextView) view);
                fuelKindWindow.show();
                break;
            case R.id.unit_tv:
                String unitTitle = getResources().getString(R.string.unit_label);
                String unitValueTon = getResources().getString(R.string.unit_value_ton);
                String unitValueBucket = getResources().getString(R.string.unit_value_bucket);
                ShowValueWindow unitWindow = new ShowValueWindow(OilReportDetailActivity.this,
                        unitTitle,unitValueTon,unitValueBucket,(TextView) view);
                unitWindow.show();
                break;
            case R.id.realStoreQty_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.realStoreQty_label),
                        realStoreQty_Tv.getText().toString().trim(),realStoreQty_code);
                break;
            case R.id.realyAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.realyAmount_label),
                        realyAmount_Tv.getText().toString().trim(),realyAmount_code);
                break;
            case R.id.taxfreeRealyAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.taxfreeRealyAmount_label),
                        taxfreeRealyAmount_Tv.getText().toString().trim(),taxfreeRealyAmount_code);
                break;
            case R.id.addFuelQty_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.addFuelQty_label),
                        addFuelQty_Tv.getText().toString().trim(),addFuelQty_code);
                break;
            case R.id.addAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.addAmount_label),
                        addAmount_Tv.getText().toString().trim(),addAmount_code);
                break;
            case R.id.taxfreeAddAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.taxfreeAddAmount_label),
                        taxfreeAddAmount_Tv.getText().toString().trim(),taxfreeAddAmount_code);
                break;
        }
    }

    public void startEditActivity(String type,String title,String content,int requestCode){
        Intent intent2 = new Intent(OilReportDetailActivity.this, EditValueActivity.class);
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
                case realStoreQty_code:
                    realStoreQty_Tv.setText(result);
                    break;
                case realyAmount_code:
                    realyAmount_Tv.setText(result);
                    break;
                case taxfreeRealyAmount_code:
                    taxfreeRealyAmount_Tv.setText(result);
                    break;
                case addFuelQty_code:
                    addFuelQty_Tv.setText(result);
                    break;
                case addAmount_code:
                    addAmount_Tv.setText(result);
                    break;
                case taxfreeAddAmount_code:
                    taxfreeAddAmount_Tv.setText(result);
                    break;
            }
        }
    }

    public void getData(){
        mLoadingView.show(getSupportFragmentManager(),"");
        String json = new Gson().toJson(new OilReq(1,10, "",new OilReq.ShipFualDynamicInfosubBean(dynamicinfoId)));
        OkHttpUtils
                .postString()
                .url(Config.oil_report_history_detail_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoadingView.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mLoadingView.dismiss();
                        oilReportHistoryDetailVo =new Gson().fromJson(response, OilReportHistoryDetailVo.class);
                        if (oilReportHistoryDetailVo != null) {
                            if (oilReportHistoryDetailVo.isSuccess()) {
                                if (oilReportHistoryDetailVo.getData().getList() != null && oilReportHistoryDetailVo.getData().getList().size() > 0) {
                                    showContent();
                                    for (int i = 0; i < oilReportHistoryDetailVo.getData().getList().size(); i++) {
                                        data.add(oilReportHistoryDetailVo.getData().getList().get(i));
                                        initData(data.get(0));
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void initData(OilReportHistoryDetailVo.DataBean.OilData oilData){
        if(oilData != null){
            fuelKind_Tv.setText(oilData.getFuelKind());
            unit_Tv.setText(oilData.getUnit());
            realStoreQty_Tv.setText(oilData.getRealStoreQty() + "");
            realyAmount_Tv.setText(oilData.getRealyAmount() + "");
            taxfreeRealyAmount_Tv.setText(oilData.getTaxfreeRealyAmount() + "");
            addFuelQty_Tv.setText(oilData.getAddFuelQty() + "");
            addAmount_Tv.setText(oilData.getAddAmount() + "");
            taxfreeAddAmount_Tv.setText(oilData.getTaxfreeAddAmount() + "");
        }

    }

    public void commitData(){
        mLoadingView = new CatLoadingView();
        mLoadingView.show(getSupportFragmentManager(),"");

        String fuelKind = fuelKind_Tv.getText().toString().trim();
        String unit = unit_Tv.getText().toString().trim();
        final double realStoreQty = Double.parseDouble(realStoreQty_Tv.getText().toString().trim());
        final long realyAmount = Long.parseLong(realyAmount_Tv.getText().toString().trim());
        long taxfreeRealyAmount = Long.parseLong(taxfreeRealyAmount_Tv.getText().toString().trim());
        final double addFuelQty = Double.parseDouble(addFuelQty_Tv.getText().toString().trim());
        long addAmount = Long.parseLong(addAmount_Tv.getText().toString().trim());
        double taxfreeAddAmount = Double.parseDouble(taxfreeAddAmount_Tv.getText().toString().trim());
        String json = new Gson().toJson(new OilReportDetailReq(dynamicinfoId,fuelKind,unit,realStoreQty,
                realyAmount,taxfreeRealyAmount,addFuelQty,addAmount,taxfreeAddAmount));
        OkHttpUtils
                .postString()
                .url(Config.add_oil_report_detail_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoadingView.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mLoadingView.dismiss();
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                ToastUtil.showToast(getResources().getString(R.string.commit_success_msg));
                                fuelKind_Tv.setText("");unit_Tv.setText("");realStoreQty_Tv.setText("");
                                realyAmount_Tv.setText("");taxfreeRealyAmount_Tv.setText("");addAmount_Tv.setText("");
                                addFuelQty_Tv.setText("");taxfreeAddAmount_Tv.setText("");
                            }else{
                                ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                            }
                        }catch (JSONException e){

                        }
                    }
                });

    }
}
