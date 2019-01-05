package com.cimcitech.nmhy.activity.home.oil;

import android.content.Context;
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

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.adapter.all.PopupWindowAdapter;
import com.cimcitech.nmhy.bean.oil.OilReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.widget.MyBaseActivity;
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
            addWatcher(fuelKind_Tv);
            addWatcher(unit_Tv);
            addWatcher(estimateQty_Tv);
            addWatcher(estimatePrice_Tv);
            addWatcher(estimateAmount_Tv);

            titleName_Tv.setText(getResources().getString(R.string.add_oil_request_detail_label));
            showContent();
            commit_Bt.setVisibility(View.VISIBLE);
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
        if(oilData != null){
            fuelKind_Tv.setText(oilData.getFuelKind());
            unit_Tv.setText(oilData.getUnit());
            estimateAmount_Tv.setText(oilData.getEstimateAmount() + "");
            estimatePrice_Tv.setText(oilData.getEstimatePrice() + "");
            estimateQty_Tv.setText(oilData.getEstimateQty() + "");
        }
    }

    @OnClick({R.id.back_iv,R.id.fuelKind_tv,R.id.taxId_tv,R.id.taxRate_tv,R.id.unit_tv,R.id.estimateQty_tv,
            R.id.estimatePrice_tv,R.id.estimateAmount_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.fuelKind_tv:
                String fuelKindTitle = mContext.getResources().getString(R.string.choice_label) +
                        mContext.getResources().getString(R.string.fuelKind_label);
                List<String> contentList = new ArrayList<>();
                if(isAdd){//新增
                    contentList.add("轻油");
                    contentList.add("重油");
                    contentList.add("机油");
                }else{
                    for(int i = 0; i < data.size(); i++){
                        contentList.add(data.get(i).getFuelKind());
                    }
                }
                ShowListValueWindow window = new ShowListValueWindow(mContext,fuelKindTitle, contentList,fuelKind_Tv);
                window.show();
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
            case R.id.estimateAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.estimateAmount_label),
                        estimateAmount_Tv.getText().toString().trim(),ESTIMATEAMOUNT_CODE);
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
                case TAXID_CODE:
                    taxId_Tv.setText(result);
                    break;
                case TAXRATE_CODE:
                    taxRate_Tv.setText(result);
                    break;
                case ESTIMATEQTY_CODE:
                    estimateQty_Tv.setText(result);
                    break;
                case ESTIMATEPRICE_CODE:
                    estimatePrice_Tv.setText(result);
                    break;
                case ESTIMATEAMOUNT_CODE:
                    estimateAmount_Tv.setText(result);
                    break;
            }
        }
    }
}
