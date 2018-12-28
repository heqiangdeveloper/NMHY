package com.cimcitech.nmhy.activity.home.oil;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.EditValueActivity;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.NetWorkUtil;
import com.cimcitech.nmhy.utils.ShowListValueWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OilRequestDetailActivity extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.fuelKind_tv)
    TextView fuelKind_Tv;
    @Bind(R.id.taxId_tv)
    TextView taxId_Tv;
    @Bind(R.id.taxRate_tv)
    TextView taxRate_Tv;
    @Bind(R.id.unit_tv)
    TextView unit_Tv;
    @Bind(R.id.estimateQty_tv)
    TextView estimateQty_Tv;
    @Bind(R.id.estimatePrice_tv)
    TextView estimatePrice_Tv;
    @Bind(R.id.estimateAmount_tv)
    TextView estimateAmount_Tv;
//    @Bind(R.id.actualQty_tv)
//    TextView actualQty_Tv;
//    @Bind(R.id.actualPrice_tv)
//    TextView actualPrice_Tv;
//    @Bind(R.id.actualAmount_tv)
//    TextView actualAmount_Tv;
//    @Bind(R.id.settleId_tv)
//    TextView settleId_Tv;
//    @Bind(R.id.tax_tv)
//    TextView tax_Tv;
//    @Bind(R.id.taxFreeAmount_tv)
//    TextView taxFreeAmount_Tv;

    @Bind(R.id.empty_rl)
    RelativeLayout empty_Rl;
    @Bind(R.id.content_ll)
    LinearLayout content_Ll;
    @Bind(R.id.popup_menu_layout)
    LinearLayout popup_menu_Ll;
    @Bind(R.id.commit_bt)
    Button commit_Bt;

    private final Context context = OilRequestDetailActivity.this;
    private PopupWindow pop = null;
    private final int REQUESTCODE_ESTIMATEQTY = 1;
    private final int REQUESTCODE_ESTIMATEPRICE  = 2;
    private final int REQUESTCODE_ESTIMATEAMOUNT = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_request_detail);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_oil_request_detail));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
        popup_menu_Ll.setVisibility(View.GONE);
    }

    public void initView(){
        if(NetWorkUtil.isConn(OilRequestDetailActivity.this)){
            empty_Rl.setVisibility(View.GONE);
            content_Ll.setVisibility(View.VISIBLE);
            addWatcher(fuelKind_Tv);
            addWatcher(taxId_Tv);
            addWatcher(taxRate_Tv);
            addWatcher(unit_Tv);
            addWatcher(estimateAmount_Tv);
            addWatcher(estimatePrice_Tv);
            addWatcher(estimateQty_Tv);
            initContent();
        }else {
            empty_Rl.setVisibility(View.VISIBLE);
            content_Ll.setVisibility(View.GONE);
        }
    }

    public void initContent(){
        //applyTime_Tv.setText(DateTool.getSystemDate());
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
                taxId_Tv.getText().toString().trim().length() != 0 &&
                taxRate_Tv.getText().toString().trim().length() != 0 &&
                unit_Tv.getText().toString().trim().length() != 0 &&
                estimateAmount_Tv.getText().toString().trim().length() != 0 &&
                estimatePrice_Tv.getText().toString().trim().length() != 0 &&
                estimateQty_Tv.getText().toString().trim().length() != 0){
            return false;
        }else{
            return true;
        }
    }

    @OnClick({R.id.back_iv,R.id.fuelKind_tv,R.id.estimateAmount_tv,R.id.estimatePrice_tv,R.id.estimateQty_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.fuelKind_tv:
                String choiceTitle = context.getResources().getString(R.string.fuelKind_label);
                List<String> fuelKindList = new ArrayList<>();
                fuelKindList.add("轻油（吨）");
                fuelKindList.add("重油（吨）");
                fuelKindList.add("机油（桶）");
                String fuelKindTitle = context.getResources().getString(R.string.choice_label) +
                        context.getResources().getString(R.string.fuelKind_label);
                ShowListValueWindow fuelKindWindow = new ShowListValueWindow
                        (OilRequestDetailActivity.this,fuelKindTitle,fuelKindList,(TextView) view);
                fuelKindWindow.show();
                break;
            case R.id.estimateAmount_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.estimateAmount_label),
                        estimateAmount_Tv.getText().toString().trim(),REQUESTCODE_ESTIMATEAMOUNT);
                break;
            case R.id.estimatePrice_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.estimatePrice_label),
                        estimatePrice_Tv.getText().toString().trim(),REQUESTCODE_ESTIMATEPRICE);
                break;
            case R.id.estimateQty_tv:
                startEditActivity(Config.TEXT_TYPE_NUM,getResources().getString(R.string.estimateQty_label),
                        estimateQty_Tv.getText().toString().trim(),REQUESTCODE_ESTIMATEQTY);
                break;
        }
    }

    public void startEditActivity(String type,String title,String content,int requestCode){
        Intent intent2 = new Intent(OilRequestDetailActivity.this, EditValueActivity.class);
        intent2.putExtra("type",type);
        intent2.putExtra("title",title);
        intent2.putExtra("content",content);
        startActivityForResult(intent2,requestCode);
    }

    public void showContactUsPopWin(Context context,String title) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_oil_type_view, null);
        view.getBackground().setAlpha(230);//0-255,0完全透明
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        TextView title_Tv = view.findViewById(R.id.pop_title_tv);
        TextView type_light_oil_Tv = view.findViewById(R.id.type_light_oil_tv);
        TextView type_heavy_oil_Tv = view.findViewById(R.id.type_heavy_oil_tv);
        TextView type_machine_oil_Tv = view.findViewById(R.id.type_machine_oil_tv);
        TextView cancel_Tv = view.findViewById(R.id.cancel_tv);
        title_Tv.setText(title);
        type_light_oil_Tv.setOnClickListener(new Listener());
        type_heavy_oil_Tv.setOnClickListener(new Listener());
        type_machine_oil_Tv.setOnClickListener(new Listener());
        cancel_Tv.setOnClickListener(new Listener());
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
    }

    class Listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.type_light_oil_tv:
                    pop.dismiss();
                    fuelKind_Tv.setText(getResources().getString(R.string.type_light_oil));
                    break;
                case R.id.type_heavy_oil_tv:
                    pop.dismiss();
                    fuelKind_Tv.setText(getResources().getString(R.string.type_heavy_oil));
                    break;
                case R.id.type_machine_oil_tv:
                    pop.dismiss();
                    fuelKind_Tv.setText(getResources().getString(R.string.type_machine_oil));
                    break;
                case R.id.cancel_tv:
                    pop.dismiss();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            String result = data.getStringExtra("result");
            switch (requestCode){
                case REQUESTCODE_ESTIMATEAMOUNT:
                    estimateAmount_Tv.setText(result);
                    break;
                case REQUESTCODE_ESTIMATEPRICE:
                    estimatePrice_Tv.setText(result);
                    break;
                case REQUESTCODE_ESTIMATEQTY:
                    estimateQty_Tv.setText(result);
                    break;
            }
        }
    }
}
