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

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.adapter.all.PopupWindowAdapter;
import com.cimcitech.nmhy.bean.oil.OilReportDetailReq;
import com.cimcitech.nmhy.bean.oil.OilReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilRequestDetailReq;
import com.cimcitech.nmhy.utils.Config;
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
    private CatLoadingView mCatLoadingView = null;

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
            titleName_Tv.setText(getResources().getString(R.string.add_oil_report_detail_label));
            showContent();
            commit_Bt.setVisibility(View.VISIBLE);
            addWatcher(fuelKind_Tv);addWatcher(unit_Tv);addWatcher(realStoreQty_Tv);
            addWatcher(realyAmount_Tv);addWatcher(taxfreeRealyAmount_Tv);addWatcher(addAmount_Tv);
            addWatcher(addFuelQty_Tv);addWatcher(taxfreeAddAmount_Tv);
            fuelKind_Tv.setText("");
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
                                    }
                                    initData(data.get(0));
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
//        if(oilData != null){
//            String fuelKindStr = oilData.getFuelKind();
//            if(fuelKindStr.startsWith("FP") && fuelKindStr.length() == 4){
//                fuelKindStr = EnumUtil.findValueByKeySS(Config.fuelTypeMap,fuelKindStr);
//            }
//            fuelKind_Tv.setText(fuelKindStr);
//            unit_Tv.setText(oilData.getUnit());
//            realStoreQty_Tv.setText(oilData.getRealStoreQty() + "");
//            realyAmount_Tv.setText(oilData.getRealyAmount() + "");
//            taxfreeRealyAmount_Tv.setText(oilData.getTaxfreeRealyAmount() + "");
//            addFuelQty_Tv.setText(oilData.getAddFuelQty() + "");
//            addAmount_Tv.setText(oilData.getAddAmount() + "");
//            taxfreeAddAmount_Tv.setText(oilData.getTaxfreeAddAmount() + "");
//        }
    }

    @OnClick({R.id.back_iv,R.id.fuelKind_tv,R.id.unit_tv,R.id.realStoreQty_tv,R.id
            .realyAmount_tv,R.id.taxfreeRealyAmount_tv,R.id.addAmount_tv,R.id.addFuelQty_tv,
            R.id.taxfreeAddAmount_tv,R.id.commit_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                if(isAdd && isAddInfo()){
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
                }else{
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
            case R.id.unit_tv:
                String unitTitle = mContext.getResources().getString(R.string.choice_label) +
                        mContext.getResources().getString(R.string.unit_label);
                List<String> unitList = new ArrayList<>();
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
            case R.id.commit_bt:
                commitData();
                break;
        }
    }

    public boolean isAddInfo(){
        if(fuelKind_Tv.getText().toString().trim().length() != 0 ||
                unit_Tv.getText().toString().trim().length() != 0 ||
                realyAmount_Tv.getText().toString().trim().length() != 0 ||
                realStoreQty_Tv.getText().toString().trim().length() != 0 ||
                taxfreeRealyAmount_Tv.getText().toString().trim().length() != 0 ||
                addAmount_Tv.getText().toString().trim().length() != 0 ||
                addFuelQty_Tv.getText().toString().trim().length() != 0 ||
                taxfreeAddAmount_Tv.getText().toString().trim().length() != 0){
            return true;
        }else{
            return false;
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

    private void commitData(){
        /*mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");

        String fuelKindStr = EnumUtil.findKeyByValueSS(Config.fuelTypeMap,fuelKind_Tv.getText().toString().trim());
        String unit = unit_Tv.getText().toString().trim();
        double realStoreQty = Double.parseDouble(realStoreQty_Tv.getText().toString().trim());
        double realyAmount = Double.parseDouble(realyAmount_Tv.getText().toString().trim());
        double taxfreeRealyAmount = Double.parseDouble(taxfreeRealyAmount_Tv.getText().toString().trim());
        double addFuelQty = Double.parseDouble(addFuelQty_Tv.getText().toString().trim());
        double addAmount = Double.parseDouble(addAmount_Tv.getText().toString().trim());
        double taxfreeAddAmount = Double.parseDouble(taxfreeAddAmount_Tv.getText().toString().trim());
        String json = new Gson().toJson(new OilReportDetailReq(dynamicinfoId,fuelKindStr,unit,realStoreQty,
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
                                fuelKind_Tv.setText("");unit_Tv.setText("");realStoreQty_Tv.setText("");
                                realyAmount_Tv.setText("");taxfreeRealyAmount_Tv.setText("");addAmount_Tv.setText("");
                                addFuelQty_Tv.setText("");taxfreeAddAmount_Tv.setText("");
                            }else{
                                ToastUtil.showToast(getResources().getString(R.string.commit_fail_msg));
                            }
                        }catch (JSONException e){

                        }
                    }
                });*/
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
}
