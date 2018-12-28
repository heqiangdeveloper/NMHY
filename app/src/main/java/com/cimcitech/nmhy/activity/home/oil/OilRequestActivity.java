package com.cimcitech.nmhy.activity.home.oil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.baidu.location.Poi;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.adapter.all.PopupWindowAdapter;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DataCleanManager;
import com.cimcitech.nmhy.utils.DateTool;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;
import com.cimcitech.nmhy.utils.ShowValueWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OilRequestActivity extends AppCompatActivity {
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

    public DataCleanManager manager = null;

    private SharedPreferences sp;

    public static final String CALL_FINISH = "com.cimcitech.lyt.mainactivity.finish";
    private final int REQUESTCODE_BARGENAME = 1;
    private final int REQUESTCODE_VOYAGEPLANID = 2;
    private final int REQUESTCODE_APPLYREASON = 3;
    private final int REQUESTCODE_OWERCOMPID = 4;
    private final int REQUESTCODE_PEPAIDAMOUNT = 5;
    private NetWorkUtil netWorkUtil = null;
    private LocationService locationService;
    private ArrayList<Poi> pois = new ArrayList<>(); //获取到的定位位置的对象

    private StringBuffer locSb ;
    private static final int requestLocTime = 7000;
    private boolean isFinishlocating = false;
    private final int LOCATION_REQUESTCODE = 1;

    private final Context context = OilRequestActivity.this;
    private PopupWindow pop = null;
    private int currentItem = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_request);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_oil_request));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
        popup_menu_Ll.setVisibility(View.GONE);
    }

    public void initView(){
        if(NetWorkUtil.isConn(OilRequestActivity.this)){
            empty_Rl.setVisibility(View.GONE);
            content_Ll.setVisibility(View.VISIBLE);
            addWatcher(applyTime_Tv);
            addWatcher(bargeName_Tv);
            addWatcher(voyagePlanId_Tv);
            addWatcher(applyReason_Tv);
            addWatcher(owerCompId_Tv);
            addWatcher(supplierId_Tv);
            addWatcher(paymentMethod_Tv);
            addWatcher(currency_Tv);
            addWatcher(ifPrepaid_Tv);
            addWatcher(pepaidAmount_Tv);
            initContent();
        }else {
            empty_Rl.setVisibility(View.VISIBLE);
            content_Ll.setVisibility(View.GONE);
        }
    }

    public void initContent(){
        applyTime_Tv.setText(DateTool.getSystemDate());
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
                bargeName_Tv.getText().toString().trim().length() != 0 &&
                voyagePlanId_Tv.getText().toString().trim().length() != 0 &&
                applyReason_Tv.getText().toString().trim().length() != 0 &&
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

    @OnClick({R.id.back_iv,R.id.bargeName_tv,R.id.voyagePlanId_tv,R.id.applyReason_tv,R.id.owerCompId_tv,
    R.id.supplierId_tv,R.id.paymentMethod_tv,R.id.currency_tv,R.id.ifPrepaid_tv,R.id.pepaidAmount_tv,
    R.id.commit_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.bargeName_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.ship_name_label),
                        bargeName_Tv.getText().toString().trim(),REQUESTCODE_BARGENAME);
                break;
            case R.id.voyagePlanId_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.ship_voyage_no_label),
                        voyagePlanId_Tv.getText().toString().trim(),REQUESTCODE_VOYAGEPLANID);
                break;
            case R.id.applyReason_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.apply_reason_label),
                        applyReason_Tv.getText().toString().trim(),REQUESTCODE_APPLYREASON);
                break;
            case R.id.owerCompId_tv:
                startEditActivity(Config.TEXT_TYPE_STR,getResources().getString(R.string.apply_company_label),
                        owerCompId_Tv.getText().toString().trim(),REQUESTCODE_OWERCOMPID);
                break;
            case R.id.supplierId_tv:
                currentItem = 1;
                List<String> currencyList1 = new ArrayList<>();
                currencyList1.add("中石油");
                currencyList1.add("中海油");
                currencyList1.add("中石化");
                String title1 = context.getResources().getString(R.string.choice_label) + context
                        .getResources().getString(R.string.supply_name_label);
                //showContactUsPopWin(context,title1,currencyList1);
                //pop.showAtLocation(view, Gravity.CENTER, 0, 0);
                ShowListValueWindow supply_name_window = new ShowListValueWindow(OilRequestActivity.this,
                        title1,currencyList1,(TextView) view);
                supply_name_window.show();
                break;
            case R.id.currency_tv:
                currentItem = 2;
                List<String> currencyList2 = new ArrayList<>();
                currencyList2.add("人民币");
                currencyList2.add("美元");
                String title2 = context.getResources().getString(R.string.choice_label) + context
                        .getResources().getString(R.string.money_type_label);
                showContactUsPopWin(context,title2,currencyList2);
                pop.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
            case R.id.ifPrepaid_tv:
                String choiceTitle = context.getResources().getString(R.string.is_advance_label);
                //showContactUsPopWin(context,choiceTitle);
                String str1 = getResources().getString(R.string.yes_label);
                String str2 = getResources().getString(R.string.no_label);
                ShowValueWindow ifPrepaid_window = new ShowValueWindow(OilRequestActivity.this,
                        choiceTitle,str1,str2,(TextView) view);
                ifPrepaid_window.show();
                break;
            case R.id.pepaidAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.apply_company_label),
                        owerCompId_Tv.getText().toString().trim(),REQUESTCODE_PEPAIDAMOUNT);
                break;
            case R.id.commit_bt:
                Intent i = new Intent(OilRequestActivity.this,OilRequestDetailActivity.class);
                startActivity(i);
                break;
        }
    }

    public void showContactUsPopWin(Context context, String title, final List<String> list) {
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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (currentItem == 1) {//供应商
                    supplierId_Tv.setText(list.get(position));
                    pop.dismiss();
                }else if(currentItem == 2){
                    currency_Tv.setText(list.get(position));
                    pop.dismiss();
                }
            }
        });
    }

    public void showContactUsPopWin(Context context,String title) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_choice_view, null);
        view.getBackground().setAlpha(230);//0-255,0完全透明
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        TextView title_Tv = view.findViewById(R.id.pop_title_tv);
        TextView yes_type_Tv = view.findViewById(R.id.yes_type_tv);
        TextView no_type_Tv = view.findViewById(R.id.no_type_tv);
        TextView cancel_Tv = view.findViewById(R.id.cancel_tv);
        title_Tv.setText(title);
        yes_type_Tv.setOnClickListener(new TextClickListener());
        no_type_Tv.setOnClickListener(new TextClickListener());
        cancel_Tv.setOnClickListener(new TextClickListener());
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
    }

    class TextClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.yes_type_tv:
                    pop.dismiss();
                    ifPrepaid_Tv.setText(getResources().getString(R.string.yes_label));
                    break;
                case R.id.no_type_tv:
                    pop.dismiss();
                    ifPrepaid_Tv.setText(getResources().getString(R.string.no_label));
                    break;
                case R.id.cancel_tv:
                    pop.dismiss();
                    break;
            }
        }
    }

    public void startEditActivity(String type,String title,String content,int requestCode){
        Intent intent2 = new Intent(OilRequestActivity.this, EditValueActivity.class);
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
                case REQUESTCODE_BARGENAME:
                    bargeName_Tv.setText(result);
                    break;
                case REQUESTCODE_VOYAGEPLANID:
                    voyagePlanId_Tv.setText(result);
                    break;
                case REQUESTCODE_APPLYREASON:
                    applyReason_Tv.setText(result);
                    break;
                case REQUESTCODE_OWERCOMPID:
                    owerCompId_Tv.setText(result);
                    break;
                case REQUESTCODE_PEPAIDAMOUNT:
                    pepaidAmount_Tv.setText(result);
                    break;
            }
        }
    }
}
