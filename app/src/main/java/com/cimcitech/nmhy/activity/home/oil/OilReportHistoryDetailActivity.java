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

public class OilReportHistoryDetailActivity extends MyBaseActivity {
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

    private Context mContext = OilReportHistoryDetailActivity.this;
    private long dynamicinfoId = 0;
    private List<OilReportHistoryDetailVo.DataBean.OilData> data = new ArrayList<>();
    private OilReportHistoryDetailVo oilReportHistoryDetailVo = null;
    private PopupWindow pop = null;
    private CatLoadingView mView = null;//Loading dialog
    private boolean isAdd = true;

    private final int REALSTOREQTY_CODE = 1;
    private final int REALYAMOUNT_CODE = 2;
    private final int TAXFREEREALYAMOUNT_CODE = 3;
    private final int ADDFUELQTY_CODE = 4;
    private final int ADDAMOUNT_CODE = 5;
    private final int TAXFREEADDAMOUNT_CODE = 6;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_report_detail);
        ButterKnife.bind(this);

        dynamicinfoId = getIntent().getLongExtra("dynamicinfoId",-1);
        isAdd = getIntent().getBooleanExtra("isAdd",false);
        initTitle();
    }

    public void initTitle(){
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.VISIBLE);
        popup_menu_Ll.setVisibility(View.GONE);
        if(isAdd){//新增
            addWatcher(fuelKind_Tv);addWatcher(unit_Tv);addWatcher(realStoreQty_Tv);
            addWatcher(realyAmount_Tv);addWatcher(taxfreeRealyAmount_Tv);addWatcher(addAmount_Tv);
            addWatcher(addFuelQty_Tv);addWatcher(taxfreeAddAmount_Tv);
            titleName_Tv.setText(getResources().getString(R.string.add_oil_report_detail_label));
            showContent();
            commit_Bt.setVisibility(View.VISIBLE);
        }else {//查看
            titleName_Tv.setText(getResources().getString(R.string.query_oil_report_detail_label));
            showEmpty();
            commit_Bt.setVisibility(View.GONE);
            if(NetWorkUtil.isConn(OilReportHistoryDetailActivity.this)){
                mView = new CatLoadingView();
                mView.setCancelable(false);
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
                realStoreQty_Tv.getText().toString().trim().length() != 0 &&
                realyAmount_Tv.getText().toString().trim().length() != 0 &&
                taxfreeRealyAmount_Tv.getText().toString().trim().length() != 0 &&
                addAmount_Tv.getText().toString().trim().length() != 0 &&
                addFuelQty_Tv.getText().toString().trim().length() != 0 &&
                taxfreeAddAmount_Tv.getText().toString().trim().length() != 0){
            return false;
        }else{
            return true;
        }
    }

    public void showWhiteIcon(){
        setDrawable(unit_Tv);
        setDrawable(realyAmount_Tv);setDrawable(realStoreQty_Tv);setDrawable(taxfreeRealyAmount_Tv);
        setDrawable(addAmount_Tv);setDrawable(addFuelQty_Tv);setDrawable(taxfreeAddAmount_Tv);
    }

    public void setDrawable(TextView tv){
        tv.setClickable(false);
        tv.setCompoundDrawables(null,null,null,null);
        tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.drawable_padding_size));
    }

    public void getData(){
        mView.show(getSupportFragmentManager(),"");
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
                        mView.dismiss();
                        Log.d(TAG,"exception: " + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mView.dismiss();
                        oilReportHistoryDetailVo =new Gson().fromJson(response, OilReportHistoryDetailVo.class);
                        if (oilReportHistoryDetailVo != null) {
                            if (oilReportHistoryDetailVo.isSuccess()) {
                                if (oilReportHistoryDetailVo.getData().getList() != null && oilReportHistoryDetailVo.getData().getList().size() > 0) {
                                    showContent();
                                    showWhiteIcon();
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

    public void showEmpty(){
        empty_Rl.setVisibility(View.VISIBLE);
        content_Ll.setVisibility(View.GONE);
    }

    public void showContent(){
        empty_Rl.setVisibility(View.GONE);
        content_Ll.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.back_iv,R.id.fuelKind_tv,R.id.unit_tv,R.id.realStoreQty_tv,R.id
            .realyAmount_tv,R.id.taxfreeRealyAmount_tv,R.id.addAmount_tv,R.id.addFuelQty_tv,
            R.id.taxfreeAddAmount_tv})
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
                }else{//查看
                    for(int i = 0; i < data.size(); i++){
                        contentList.add(data.get(i).getFuelKind());
                    }
                }
                ShowListValueWindow window = new ShowListValueWindow(mContext,fuelKindTitle,contentList,fuelKind_Tv);
                window.show();
                break;
            case R.id.unit_tv:
                String unitTitle = mContext.getResources().getString(R.string.choice_label) +
                        mContext.getResources().getString(R.string.unit_label);
                List<String> unitList = new ArrayList<>();
//                for(int i = 0; i < data.size(); i++){
//                    contentList.add(data.get(i).getFuelKind());
//                }
                unitList.add("吨");
                unitList.add("桶");
                ShowListValueWindow unitWindow = new ShowListValueWindow(mContext,unitTitle,unitList,unit_Tv);
                unitWindow.show();
                break;
            case R.id.realStoreQty_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.realStoreQty_label),
                        realStoreQty_Tv.getText().toString().trim(),REALSTOREQTY_CODE);
                break;
            case R.id.realyAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.realyAmount_label),
                        realyAmount_Tv.getText().toString().trim(),REALYAMOUNT_CODE);
                break;
            case R.id.taxfreeRealyAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.taxfreeRealyAmount_label),
                        taxfreeRealyAmount_Tv.getText().toString().trim(),TAXFREEREALYAMOUNT_CODE);
                break;
            case R.id.addFuelQty_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.addFuelQty_label),
                        addFuelQty_Tv.getText().toString().trim(),ADDFUELQTY_CODE);
                break;
            case R.id.addAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.addAmount_label),
                        addAmount_Tv.getText().toString().trim(),ADDAMOUNT_CODE);
                break;
            case R.id.taxfreeAddAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.taxfreeAddAmount_label),
                        taxfreeAddAmount_Tv.getText().toString().trim(),TAXFREEADDAMOUNT_CODE);
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
                case REALSTOREQTY_CODE:
                    realStoreQty_Tv.setText(result);
                    break;
                case REALYAMOUNT_CODE:
                    realyAmount_Tv.setText(result);
                    break;
                case TAXFREEREALYAMOUNT_CODE:
                    taxfreeRealyAmount_Tv.setText(result);
                    break;
                case ADDFUELQTY_CODE:
                    addFuelQty_Tv.setText(result);
                    break;
                case ADDAMOUNT_CODE:
                    addAmount_Tv.setText(result);
                    break;
                case TAXFREEADDAMOUNT_CODE:
                    taxfreeAddAmount_Tv.setText(result);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_Iv.callOnClick();
    }
}
