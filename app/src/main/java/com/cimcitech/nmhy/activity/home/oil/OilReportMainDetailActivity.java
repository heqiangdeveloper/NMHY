package com.cimcitech.nmhy.activity.home.oil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.ApkApplication;
import com.cimcitech.nmhy.adapter.oil.OilReportAdapter;
import com.cimcitech.nmhy.adapter.oil.OilReportHistoryAdapter;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.bean.oil.OilReportData;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryDetailVo;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryReq;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.oil.OilReportMainReq;
import com.cimcitech.nmhy.bean.oil.OilReq;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DateTool;
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
 * Created by qianghe on 2019/1/10.
 */


public class OilReportMainDetailActivity extends MyBaseActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.content_sv)
    ScrollView contentSv;
    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_cl)
    CoordinatorLayout contentCl;

    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.voyageStatus_tv)
    TextView voyageStatusTv;
    @Bind(R.id.location_tv)
    TextView locationTv;
    @Bind(R.id.longitude_tv)
    TextView longitudeTv;
    @Bind(R.id.latitude_tv)
    TextView latitudeTv;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;

    @Bind(R.id.oilType1_tv)
    TextView oilType1Tv;
    @Bind(R.id.oilType2_tv)
    TextView oilType2Tv;
    @Bind(R.id.oilType3_tv)
    TextView oilType3Tv;
    @Bind(R.id.oilUnit1_tv)
    TextView oilUnit1Tv;
    @Bind(R.id.oilUnit2_tv)
    TextView oilUnit2Tv;
    @Bind(R.id.oilUnit3_tv)
    TextView oilUnit3Tv;
    @Bind(R.id.oilAmount1_et)
    EditText oilAmount1Et;
    @Bind(R.id.oilAmount2_et)
    EditText oilAmount2Et;
    @Bind(R.id.oilAmount3_et)
    EditText oilAmount3Et;
    @Bind(R.id.commit_bt)
    Button commitBt;

    private LinearLayoutManager manager;
    private OilReportAdapter adapter;
    private Context mContext = OilReportMainDetailActivity.this;
    private List<OilReportHistoryDetailVo.DataBean.OilData> data = new ArrayList<>();

    private String unitTitleStr = "";
    private ShowListValueWindow window = null;
    private CatLoadingView mCatLoadingView = null;
    private boolean isAdd = true;
    private OilReportHistoryAdapter historyAdapter = null;
    private List<OilReportHistoryVo.DataBean.ListBean> historyData = new ArrayList<>();
    private int pageNum = 1;
    private OilReportHistoryDetailVo oilReportHistoryDetailVo = null;
    private OilReportHistoryVo.DataBean.ListBean bean = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_report_main);
        ButterKnife.bind(this);

        bean = (OilReportHistoryVo.DataBean.ListBean)getIntent().getSerializableExtra("oilData");
        initTitle();
        initView();
    }

    public void initTitle(){
        more_Tv.setVisibility(View.GONE);
        popup_menu_Ll.setVisibility(View.GONE);
        titleName_Tv.setText(getResources().getString(R.string.history_oil_data_detail_label));
    }

    public void initView(){
        contentCl.setVisibility(View.GONE);
        empty_Rl.setVisibility(View.VISIBLE);
        commitBt.setVisibility(View.GONE);
        if(!NetWorkUtil.isConn(mContext)){
            contentSv.setVisibility(View.GONE);
        }else{
            contentSv.setVisibility(View.GONE);
            getData();
        }
    }

    public void initContent(){
        timeTv.setText(bean.getReportTime());
        voyageStatusTv.setText(bean.getStatusName());
        locationTv.setText(bean.getLocation());
        longitudeTv.setText(bean.getLongitude() + "");
        latitudeTv.setText(bean.getLatitude() + "");

        voyageStatusTv.setClickable(false);
        voyageStatusTv.setCompoundDrawables(null,null,null,null);
        voyageStatusTv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.drawable_padding_size));

        String fuelKind = "";
        String unitName = "";
        double addFuelQty = 0;
        for(int i = 0; i < data.size(); i++){
            fuelKind = data.get(i).getFuelKindName();
            unitName = data.get(i).getUnitName();
            addFuelQty = data.get(i).getAddFuelQty();
            switch (i){
                case 0:
                    oilType1Tv.setText(fuelKind);
                    oilUnit1Tv.setText(unitName);
                    oilAmount1Et.setText(addFuelQty + "");
                    oilAmount1Et.setEnabled(false);
                    break;
                case 1:
                    oilType2Tv.setText(fuelKind);
                    oilUnit2Tv.setText(unitName);
                    oilAmount2Et.setText(addFuelQty + "");
                    oilAmount2Et.setEnabled(false);
                    break;
                case 2:
                    oilType3Tv.setText(fuelKind);
                    oilUnit3Tv.setText(unitName);
                    oilAmount3Et.setText(addFuelQty + "");
                    oilAmount3Et.setEnabled(false);
                    break;
            }
        }
    }

    @OnClick({R.id.back_iv})
    public void onclick(View view) {
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
        }
    }

    public void getData() {
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getSupportFragmentManager(),"");
        String json = new Gson().toJson(new OilReq(1,10, "",new OilReq.ShipFualDynamicInfosubBean(bean.getDynamicinfoId())));
        OkHttpUtils
                .postString()
                .url(Config.oil_report_history_detail_url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mCatLoadingView.dismiss();
                        Log.d(TAG,"exception: " + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mCatLoadingView.dismiss();
                        oilReportHistoryDetailVo =new Gson().fromJson(response, OilReportHistoryDetailVo.class);
                        if (oilReportHistoryDetailVo != null) {
                            if (oilReportHistoryDetailVo.isSuccess()) {
                                if (oilReportHistoryDetailVo.getData().getList() != null && oilReportHistoryDetailVo.getData().getList().size() > 0) {
                                    empty_Rl.setVisibility(View.GONE);
                                    contentSv.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < oilReportHistoryDetailVo.getData().getList().size(); i++) {
                                        data.add(oilReportHistoryDetailVo.getData().getList().get(i));
                                    }
                                    initContent();
                                }
                            }
                        }
                    }
                });
    }
}
