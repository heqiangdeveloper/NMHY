package com.cimcitech.nmhy.activity.home.oil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.oil.OilReportReq;
import com.cimcitech.nmhy.bean.oil.OilReq;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DataCleanManager;
import com.cimcitech.nmhy.utils.DateTool;
import com.cimcitech.nmhy.utils.EventBusMessage;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.utils.ShowValueWindow;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.widget.MyBaseActivity;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;


public class OilRequestActivity2 extends MyBaseActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.applyTime_tv)
    TextView applyTime_Tv;
    @Bind(R.id.bargeName_tv)
    TextView bargeName_Tv;
    @Bind(R.id.voyagePlanId_tv)
    TextView voyagePlanId_Tv;
    @Bind(R.id.applyReason_tv)
    TextView applyReason_Tv;
    @Bind(R.id.owerCompId_tv)
    TextView owerCompId_Tv;
    @Bind(R.id.supplierId_tv)
    TextView supplierId_Tv;
    @Bind(R.id.paymentMethod_tv)
    TextView paymentMethod_Tv;
    @Bind(R.id.currency_tv)
    TextView currency_Tv;
    @Bind(R.id.ifPrepaid_tv)
    TextView ifPrepaid_Tv;
    @Bind(R.id.pepaidAmount_tv)
    TextView pepaidAmount_Tv;

    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_ll)
    LinearLayout content_Ll;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;
    @Bind(R.id.commit_bt)
    Button commit_Bt;
    @Bind(R.id.add_ib)
    ImageButton add_Ib;

    public DataCleanManager manager = null;

    private SharedPreferences sp;

    public static final String CALL_FINISH = "com.cimcitech.lyt.mainactivity.finish";
    private final int APPLYREASON_CODE = 1;
    private final int BARGENAME_CODE = 2;
    private final int VOYAGEPLANID_CODE = 3;
    private final int OWERCOMPID_CODE = 4;
    private final int SUPPLIERID_CODE = 5;
    private final int PAYMENTMETHOD_CODE = 6;
    private final int CURRENCY_CODE = 7;
    private final int IFPREPAID_CODE = 8;
    private final int PEPAIDAMOUNT_CODE = 9;
    private NetWorkUtil netWorkUtil = null;

    private final Context mContext = OilRequestActivity2.this;
    private PopupWindow pop = null;
    private int currentItem = 1;
    private CatLoadingView mLoadingView = null;
    private boolean isAdd = true;
    private OilRequestHistoryVo.DataBean.ListBean oilData = null;
    private String TAG = "oillog";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_request);
        ButterKnife.bind(this);

        oilData = (OilRequestHistoryVo.DataBean.ListBean)getIntent().getSerializableExtra("oilData");
        if(oilData == null){
            isAdd = true;
        }else{
            isAdd = false;
        }

        initTitle();
        initView();
    }

    public void initTitle(){
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
        if(isAdd){
            add_Ib.setVisibility(View.GONE);
            commit_Bt.setText(getResources().getString(R.string.btn_commit_label));
            titleName_Tv.setText(getResources().getString(R.string.add_oil_request_label));
        }else{
            add_Ib.setVisibility(View.VISIBLE);
            commit_Bt.setText(getResources().getString(R.string.query_oil_request_detail_label));
            titleName_Tv.setText(getResources().getString(R.string.query_oil_request_label));
        }
    }

    public void initView(){
        if(isAdd){
            addWatcher(applyTime_Tv);
            addWatcher(applyReason_Tv);
            addWatcher(bargeName_Tv);
            addWatcher(pepaidAmount_Tv);
            if(NetWorkUtil.isConn(mContext)){
                empty_Rl.setVisibility(View.GONE);
                content_Ll.setVisibility(View.VISIBLE);
                initContent();
            }else {
                empty_Rl.setVisibility(View.VISIBLE);
                content_Ll.setVisibility(View.GONE);
            }
        }else {//查看
            commit_Bt.setClickable(true);
            commit_Bt.setBackground(getResources().getDrawable(R.drawable.shape_login_button_on));

            empty_Rl.setVisibility(View.GONE);
            content_Ll.setVisibility(View.VISIBLE);
            applyTime_Tv.setText(oilData.getApplyTime() + "");
            applyReason_Tv.setText(oilData.getApplyReason() + "");
            bargeName_Tv.setText(oilData.getBargeName() + "");
            pepaidAmount_Tv.setText(oilData.getPepaidAmount() + "");
            currency_Tv.setText(oilData.getCurrency() + "");

            String yesLabel = getResources().getString(R.string.yes_label);//"是"
            String noLabel = getResources().getString(R.string.no_label);//"否"
            String ifPrepaidStr = oilData.getIfPrepaid().equals("0") ? noLabel : yesLabel;
            ifPrepaid_Tv.setText(ifPrepaidStr);

            setDrawable(applyTime_Tv);setDrawable(bargeName_Tv);setDrawable(voyagePlanId_Tv);setDrawable(applyReason_Tv);
            setDrawable(owerCompId_Tv);setDrawable(supplierId_Tv);setDrawable(paymentMethod_Tv);setDrawable(currency_Tv);
            setDrawable(ifPrepaid_Tv);setDrawable(pepaidAmount_Tv);
        }
    }

    public void setDrawable(TextView tv){
        tv.setClickable(false);
        tv.setCompoundDrawables(null,null,null,null);
        tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.drawable_padding_size));
    }

    public void initContent(){
        applyTime_Tv.setText(DateTool.getSystemDate());
        //commit_Bt.setClickable(false);
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
        if(applyTime_Tv.getText().toString().trim().length() != 0 &&
                applyReason_Tv.getText().toString().trim().length() != 0 &&
                bargeName_Tv.getText().toString().trim().length() != 0 &&
                voyagePlanId_Tv.getText().toString().trim().length() != 0 &&
                owerCompId_Tv.getText().toString().trim().length() != 0 &&
                supplierId_Tv.getText().toString().trim().length() != 0 &&
                paymentMethod_Tv.getText().toString().trim().length() != 0 &&
                currency_Tv.getText().toString().trim().length() != 0 &&
                ifPrepaid_Tv.getText().toString().trim().length() != 0 &&
                pepaidAmount_Tv.getText().toString().trim().length() != 0){
            return false;
        }else{
            return true;
        }
    }

    @OnClick({R.id.back_iv,R.id.commit_bt,R.id.add_ib,R.id.applyReason_tv,R.id.bargeName_tv,R.id
            .voyagePlanId_tv,R.id.owerCompId_tv,R.id.supplierId_tv,R.id.paymentMethod_tv,R.id
            .currency_tv,R.id.ifPrepaid_tv,R.id.pepaidAmount_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.commit_bt:
                if(isAdd){//提交 新的燃油申请
                    commitData();
                }else {//查看 燃油申请明细
                    Intent i = new Intent(mContext,OilRequestHistoryDetailActivity.class);
                    i.putExtra("applyId",oilData.getApplyId());
                    i.putExtra("isAdd",false);
                    startActivity(i);
                }
                break;
            case R.id.add_ib://新增  燃油申请明细
                Intent i = new Intent(mContext,OilRequestHistoryDetailActivity.class);
                i.putExtra("applyId",oilData.getApplyId());
                i.putExtra("isAdd",true);
                startActivity(i);
                break;
            case R.id.applyReason_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.apply_reason_label),
                        applyReason_Tv.getText().toString().trim(),APPLYREASON_CODE);
                break;
            case R.id.bargeName_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.ship_name_label),
                        bargeName_Tv.getText().toString().trim(),BARGENAME_CODE);
                break;
            case R.id.voyagePlanId_tv:
                //默认 “101”
                break;
            case R.id.owerCompId_tv://申请公司
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.apply_company_label),
                        owerCompId_Tv.getText().toString().trim(),OWERCOMPID_CODE);
                break;
            case R.id.supplierId_tv:
                String supplyTitle = getResources().getString(R.string.choice_label) +
                        getResources().getString(R.string.supply_name_label);
                ShowListValueWindow supplyWindow = new ShowListValueWindow(mContext,
                        supplyTitle,Config.supplyList,supplierId_Tv);
                supplyWindow.show();
                break;
            case R.id.paymentMethod_tv:
                //默认“现付”
                break;
            case R.id.currency_tv:
                String currenyTitle = getResources().getString(R.string.choice_label) +
                        getResources().getString(R.string.money_type_label);
                ShowListValueWindow currencyWindow = new ShowListValueWindow(mContext,
                        currenyTitle,Config.currencyList,currency_Tv);
                currencyWindow.show();
                break;
            case R.id.ifPrepaid_tv://是否预付款
                String ifPrepaidTitle = getResources().getString(R.string.is_advance_label);
                String str1 = getResources().getString(R.string.yes_label);
                String str2 = getResources().getString(R.string.no_label);
                ShowValueWindow ifPrepaidWindow = new ShowValueWindow(mContext,ifPrepaidTitle, str1,str2,ifPrepaid_Tv);
                ifPrepaidWindow.show();
                break;
            case R.id.pepaidAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.advance_money_label),
                        pepaidAmount_Tv.getText().toString().trim(),PEPAIDAMOUNT_CODE);
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
                case APPLYREASON_CODE:
                    applyReason_Tv.setText(result);
                    break;
                case BARGENAME_CODE:
                    bargeName_Tv.setText(result);
                    break;
                case OWERCOMPID_CODE:
                    owerCompId_Tv.setText(result);
                    break;
                case PEPAIDAMOUNT_CODE:
                    pepaidAmount_Tv.setText(result);
                    break;
            }
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

    public void commitData(){
        mLoadingView = new CatLoadingView();
        mLoadingView.show(getSupportFragmentManager(),"");

        String applyTime = applyTime_Tv.getText().toString().trim();
        final String bargeName = bargeName_Tv.getText().toString().trim();
        int voyagePlanId = 101;
        String applyReason = applyReason_Tv.getText().toString().trim();
        int owerCompId = 6;
        int supplierId = 2;
        String paymentMethod = paymentMethod_Tv.getText().toString().trim();
        String currency = currency_Tv.getText().toString().trim();
        String yesLabel = getResources().getString(R.string.yes_label);//"是"
        String ifPrepaid = ifPrepaid_Tv.getText().toString().trim().equals(yesLabel) ? "1" : "0";
        double pepaidAmount =Double.parseDouble(pepaidAmount_Tv.getText().toString().trim());

        String json = new Gson().toJson(new OilReq(applyTime,bargeName,
                voyagePlanId,applyReason,owerCompId,supplierId,paymentMethod,currency,ifPrepaid, pepaidAmount));
        OkHttpUtils
                .postString()
                .url(Config.add_oil_request_url)
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
                        // 发布事件
                        EventBus.getDefault().post(new EventBusMessage("addNewOilRequest"));
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                ToastUtil.showToast(getResources().getString(R.string.commit_success_msg));
                                //int dynamicinfoId = object.getInt("id");
                                bargeName_Tv.setText("");
                                applyReason_Tv.setText("");
                            }else{
                                ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                            }
                        }catch (JSONException e){

                        }
                    }
                });

    }

    public void returnToMainPage(){
        Intent i = new Intent(mContext,OilRequestHistoryActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_Iv.callOnClick();
    }
}
