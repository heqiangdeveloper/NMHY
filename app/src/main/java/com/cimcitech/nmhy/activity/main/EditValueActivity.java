package com.cimcitech.nmhy.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.widget.MyBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditValueActivity extends MyBaseActivity {
    @Bind(R.id.string_content_et)
    EditText string_content_Et;
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.save_tv)
    TextView save_Tv;

    //public static final String [] TYPE = {"num","str","int"};
    public static final String [] TYPE = {Config.TEXT_TYPE_NUM,Config.TEXT_TYPE_STR,Config.TEXT_TYPE_INT};
    public String type = "";
    private InputMethodManager imm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value);
        ButterKnife.bind(this);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        type = getIntent().getStringExtra("type");

        setInputType();
        setTextWatcher(string_content_Et);
        initView(title,content,type);
        //弹出软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public void setInputType(){
        if(type.equals(EditValueActivity.TYPE[0])){//num
            //[已解决]EditText 代码中不能够添加小数点的属性 android:inputType="numberDecimal"
            //详见https://blog.csdn.net/rodulf/article/details/52289412
            string_content_Et.setInputType(8194);
        }else if(type.equals(EditValueActivity.TYPE[1])){//string
            string_content_Et.setInputType(InputType.TYPE_CLASS_TEXT);//普通文本
        }else{//int
            string_content_Et.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    public void setTextWatcher(EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChangedAction();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void textChangedAction(){
        if(string_content_Et.getText().toString().trim().length() != 0 ){
            save_Tv.setClickable(true);
            save_Tv.setTextColor(getResources().getColor(R.color.white));
        }else{
            save_Tv.setClickable(false);
            save_Tv.setTextColor(Color.GRAY);
        }
    }

    public void initView(String title, String content,String type){
        titleName_Tv.setText(title);
        string_content_Et.setText(content);
        string_content_Et.setHint("请输入" + title);
        string_content_Et.setSelection(content.length());
        string_content_Et.setFocusable(true);
    }

    @OnClick({R.id.save_tv,R.id.back_iv})
    public void onclick(View view) {
        switch (view.getId()){
            case R.id.save_tv:
                //收回软键盘
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken( ) , 0 );
                }
                Intent i = new Intent();
                i.putExtra("result",string_content_Et.getText().toString().trim());
                setResult(RESULT_OK,i);
                finish();
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
