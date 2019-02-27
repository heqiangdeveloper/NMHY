package com.cimcitech.nmhy.activity.home.oil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.adapter.all.PopupWindowAdapter;
import com.cimcitech.nmhy.bean.oil.OilReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilRequestDetailReq;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.EnumUtil;
import com.cimcitech.nmhy.utils.EventBusMessage;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.utils.ToastUtil;
import com.cimcitech.nmhy.utils.WhiteIcon;
import com.cimcitech.nmhy.widget.MyBaseActivity;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by qianghe on 2018/12/24.
 */

public class OilRequestHistoryDetailActivity extends MyBaseActivity {
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
    @Bind(R.id.estimateQty_tv)
    TextView estimateQty_Tv;
    @Bind(R.id.estimatePrice_tv)
    TextView estimatePrice_Tv;
    @Bind(R.id.estimateAmount_tv)
    TextView estimateAmount_Tv;

    @Bind(R.id.taxId_ll)
    LinearLayout taxId_Ll;
    @Bind(R.id.taxRate_ll)
    LinearLayout taxRate_Ll;
    @Bind(R.id.taxId_tv)
    TextView taxId_Tv;
    @Bind(R.id.taxRate_tv)
    TextView taxRate_Tv;

    private Context mContext = OilRequestHistoryDetailActivity.this;
    private List<OilRequestHistoryDetailVo.DataBean.OilData> data = new ArrayList<>();
    private OilRequestHistoryDetailVo oilRequestHistoryDetailVo = null;
    private PopupWindow pop = null;
    private CatLoadingView mView = null;//Loading dialog
    private int applyId = -1;
    private boolean isAdd = true;

    private final int TAXID_CODE = 1;
    private final int TAXRATE_CODE = 2;
    private final int UNIT_CODE = 3;
    private final int ESTIMATEQTY_CODE = 4;
    private final int ESTIMATEPRICE_CODE = 5;
    private final int ESTIMATEAMOUNT_CODE = 6;
    private CatLoadingView mCatLoadingView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_request_detail);
        ButterKnife.bind(this);

        applyId = getIntent().getIntExtra("applyId",-1);
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
        if(isAdd){//新增
            titleName_Tv.setText(getResources().getString(R.string.add_oil_request_detail_label));
            showContent();
            commit_Bt.setVisibility(View.VISIBLE);

            addWatcher(fuelKind_Tv);
            addWatcher(unit_Tv);
            addWatcher(taxId_Tv);
            addWatcher(taxRate_Tv);
            addWatcher(estimateQty_Tv);
            addWatcher(estimatePrice_Tv);
            addWatcher(estimateAmount_Tv);
            fuelKind_Tv.setText("");//防止以上的addWatcher()未被触发

            //去掉estimateAmount_Tv右边的“更多”图片
            WhiteIcon wi = new WhiteIcon(mContext,estimateAmount_Tv);
            wi.setWhiteIcon();
        }else {//查看
            titleName_Tv.setText(getResources().getString(R.string.query_oil_request_detail_label));
            showEmpty();
            commit_Bt.setVisibility(View.GONE);
            if(NetWorkUtil.isConn(mContext)){
                mView = new CatLoadingView();
                mView.setCancelable(true);
                getData();
            }
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
        if(fuelKind_Tv.getText().toString().trim().length() != 0 &&
                unit_Tv.getText().toString().trim().length() != 0 &&
                taxId_Tv.getText().toString().trim().length() != 0 &&
                taxRate_Tv.getText().toString().trim().length() != 0 &&
                estimateQty_Tv.getText().toString().trim().length() != 0 &&
                estimatePrice_Tv.getText().toString().trim().length() != 0 &&
                estimateAmount_Tv.getText().toString().trim().length() != 0){
            return false;
        }else{
            return true;
        }
    }

    public void showWhiteIcon(){
        setDrawable(unit_Tv);setDrawable(taxId_Tv);setDrawable(taxRate_Tv);
        setDrawable(estimateAmount_Tv);setDrawable(estimatePrice_Tv);setDrawable(estimateQty_Tv);
    }

    public void setDrawable(TextView tv){
        tv.setClickable(false);
        tv.setCompoundDrawables(null,null,null,null);
        tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.drawable_padding_size));
    }

    public void getData(){
        mView.show(getSupportFragmentManager(),"");
        String json = new Gson().toJson(new OilReq(1,10, "",new OilReq.FuelApplyDetailBean(applyId)));
        OkHttpUtils
                .postString()
                .url(Config.oil_request_history_detail_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.dismiss();
                        Log.d(TAG,"exception: " + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mView.dismiss();
                        oilRequestHistoryDetailVo =new Gson().fromJson(response, OilRequestHistoryDetailVo.class);
                        if (oilRequestHistoryDetailVo != null) {
                            if (oilRequestHistoryDetailVo.isSuccess()) {
                                if (oilRequestHistoryDetailVo.getData().getList() != null && oilRequestHistoryDetailVo.getData().getList().size() > 0) {
                                    showContent();
                                    showWhiteIcon();
                                    for (int i = 0; i < oilRequestHistoryDetailVo.getData().getList().size(); i++) {
                                        data.add(oilRequestHistoryDetailVo.getData().getList().get(i));
                                        initData(data.get(0));
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void showEmpty(){
        empty_Rl.setVisibility(View.VISIBLE);
        content_Ll.setVisibility(View.GONE);
    }

    public void showContent(){
        empty_Rl.setVisibility(View.GONE);
        content_Ll.setVisibility(View.VISIBLE);
    }

    public void initData(OilRequestHistoryDetailVo.DataBean.OilData oilData){
//        if(oilData != null){
//            String fuelKindStr = oilData.getFuelKind();
//            if(fuelKindStr.startsWith("FP") && fuelKindStr.length() == 4){
//                fuelKindStr = EnumUtil.findValueByKeySS(Config.fuelTypeMap,fuelKindStr);
//            }
//            fuelKind_Tv.setText(fuelKindStr);
//            unit_Tv.setText(oilData.getUnit());
//            estimateAmount_Tv.setText(oilData.getEstimateAmount() + "");
//            estimatePrice_Tv.setText(oilData.getEstimatePrice() + "");
//            estimateQty_Tv.setText(oilData.getEstimateQty() + "");
//        }
    }

    @OnClick({R.id.back_iv,R.id.fuelKind_tv,R.id.taxId_tv,R.id.taxRate_tv,R.id.unit_tv,R.id.estimateQty_tv,
            R.id.estimatePrice_tv,R.id.commit_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                if(isAdd && isAddInfo()){//当新增时，如有未提交的信息，提示用户是否要退出
                    new AlertDialog.Builder(mContext)
                            //.setTitle("提示")
                            .setMessage(mContext.getResources().getString(R.string.ask_if_exit))
                            .setCancelable(true)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }else {
                    finish();
                }
                break;
            case R.id.fuelKind_tv:
//                String fuelKindTitle = mContext.getResources().getString(R.string.choice_label) +
//                        mContext.getResources().getString(R.string.fuelKind_label);
//                List<String> contentList = new ArrayList<>();
//                if(isAdd){//新增
//                    for(String key : Config.fuelTypeMap.keySet()){
//                        contentList.add(Config.fuelTypeMap.get(key));
//                    }
//                    ShowListValueWindow window = new ShowListValueWindow(mContext,fuelKindTitle, contentList, fuelKind_Tv);
//                    window.show();
//                }else{//查看
//                    for(int i = 0; i < data.size(); i++){
//                        String fuelTypeStr = data.get(i).getFuelKind();
//                        contentList.add(EnumUtil.findValueByKeySS(Config.fuelTypeMap,fuelTypeStr));
//                    }
//                    ShowWindow(mContext,fuelKindTitle, contentList, fuelKind_Tv);
//                }
                break;
            case R.id.taxId_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.taxId_label),
                        taxId_Tv.getText().toString().trim(),TAXID_CODE);
                break;
            case R.id.taxRate_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.taxRate_label),
                        taxRate_Tv.getText().toString().trim(),TAXRATE_CODE);
                break;
            case R.id.unit_tv:
                String unitTitle = mContext.getResources().getString(R.string.choice_label) +
                        mContext.getResources().getString(R.string.unit_label);
                List<String> unitList = new ArrayList<>();
                unitList.add("吨");
                unitList.add("桶");
                ShowListValueWindow unitWindow = new ShowListValueWindow(mContext,unitTitle,unitList,unit_Tv);
                unitWindow.show();
                break;
            case R.id.estimateQty_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.estimateQty_label),
                        estimateQty_Tv.getText().toString().trim(),ESTIMATEQTY_CODE);
                break;
            case R.id.estimatePrice_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.estimatePrice_label),
                        estimatePrice_Tv.getText().toString().trim(),ESTIMATEPRICE_CODE);
                break;
            case R.id.commit_bt:
                commitData();
                break;
        }
    }

    private void commitData(){
        /*mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");

        String fuelKindStr = EnumUtil.findKeyByValueSS(Config.fuelTypeMap,fuelKind_Tv.getText().toString().trim());
        String taxId = taxId_Tv.getText().toString().trim();
        double taxRate = Double.parseDouble(taxRate_Tv.getText().toString().trim());
        String unit = unit_Tv.getText().toString().trim();
        double estimateQty = Double.parseDouble(estimateQty_Tv.getText().toString().trim());
        double estimatePrice = Double.parseDouble(estimatePrice_Tv.getText().toString().trim());
        final double estimateAmount = Double.parseDouble(estimateAmount_Tv.getText().toString().trim());
        String json = new Gson().toJson(new OilRequestDetailReq(applyId,fuelKindStr,taxId,
                taxRate,unit,estimateQty,estimatePrice,estimateAmount));
        OkHttpUtils
                .postString()
                .url(Config.add_oil_request_detail_url)
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
                                fuelKind_Tv.setText("");
                                estimateAmount_Tv.setText("");
                                estimatePrice_Tv.setText("");
                                estimateQty_Tv.setText("");
                            }else{
                                ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                            }
                        }catch (JSONException e){

                        }
                    }
                });*/
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
                case TAXID_CODE:
                    taxId_Tv.setText(result);
                    break;
                case TAXRATE_CODE:
                    taxRate_Tv.setText(result);
                    break;
                case ESTIMATEQTY_CODE://预计数量
                    estimateQty_Tv.setText(result);
                    setEstimateAmount();
                    break;
                case ESTIMATEPRICE_CODE://预计单价
                    estimatePrice_Tv.setText(result);
                    setEstimateAmount();
                    break;
            }
        }
    }

    public void setEstimateAmount(){
        String estimateQty = estimateQty_Tv.getText().toString();
        String estimatePrice = estimatePrice_Tv.getText().toString();
        if(estimateQty.length() != 0 && estimatePrice.length() != 0){
            double quantity = Double.parseDouble(estimateQty);
            double price = Double.parseDouble(estimatePrice);
            DecimalFormat df = new DecimalFormat("#.00");
            String estimateAmount = df.format(quantity * price);
            estimateAmount_Tv.setText(estimateAmount);
            Log.d("estimateLog","estimateAmount is: " + estimateAmount);
        }
    }

    public void ShowWindow(Context context, String title, final List<String> list, TextView tv) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_add_client_view, null);
        view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        View pop_reward_view = view.findViewById(R.id.pop_reward_view);
        TextView title_tv = view.findViewById(R.id.title_tv);
        title_tv.setText(title);
        final PopupWindowAdapter adapter = new PopupWindowAdapter(context, list);
        ListView listView = view.findViewById(R.id.listContent);
        listView.setAdapter(adapter);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop_reward_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initData(data.get(position));
                pop.dismiss();
            }
        });
        pop.showAtLocation(tv, Gravity.CENTER, 0, 0);
    }

    public boolean isAddInfo(){
        if(fuelKind_Tv.getText().toString().trim().length() != 0 ||
                taxId_Tv.getText().toString().trim().length() != 0 ||
                taxRate_Tv.getText().toString().trim().length() != 0 ||
                unit_Tv.getText().toString().trim().length() != 0 ||
                estimateQty_Tv.getText().toString().trim().length() != 0 ||
                estimatePrice_Tv.getText().toString().trim().length() != 0){
            return true;
        }else{
            return false;
        }
    }
}
