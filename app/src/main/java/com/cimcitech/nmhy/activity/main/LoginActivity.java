package com.cimcitech.nmhy.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.login.LoginVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.widget.MyBaseActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends MyBaseActivity {
    @Bind(R.id.register_tv)
    TextView register_Tv;
    @Bind(R.id.user_et)
    EditText user_Et;
    @Bind(R.id.password_et)
    EditText password_Et;
    @Bind(R.id.login_bt)
    Button login_Bt;
    @Bind(R.id.forget_password_tv)
    TextView forget_password_Tv;
    @Bind(R.id.password_cb)
    CheckBox password_cB;

    private SharedPreferences sp;

    //17620465672 12345678
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = this.getSharedPreferences(Config.sharedPreferenceName, MODE_PRIVATE);

        password_cB.setOnCheckedChangeListener(new PasswordChange());
        addWatcher(user_Et);
        addWatcher(password_Et);
        initView();
    }

    class PasswordChange implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                //密码由不可见变为可见
                password_Et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                //密码由可见变为不可见
                password_Et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
    public void initView(){
        user_Et.setText("17620465672");
        password_Et.setText("12345678");
    }

    public void addWatcher(TextView tv){
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(user_Et.getText().toString().trim().length() != 0 &&
                        password_Et.getText().toString().trim().length() != 0){
                    login_Bt.setClickable(true);
                    login_Bt.setBackground(getResources().getDrawable(R.drawable.shape_login_button_on));
                }else{
                    login_Bt.setClickable(false);
                    login_Bt.setBackground(getResources().getDrawable(R.drawable.shape_login_button_off));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.login_bt,R.id.register_tv})
    public void onclick(View view){
        switch (view.getId()){
            case R.id.login_bt:
                if(login_Bt.isClickable()){
                    login();
                }
                break;
            case R.id.register_tv:
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    public void login(){
        mCommittingDialog.show();
        String telNo = user_Et.getText().toString().trim();
        String password = password_Et.getText().toString().trim();
        OkHttpUtils
                .post()
                .addParams("telNo",telNo)
                .addParams("password",password)
                .url(Config.login_url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mCommittingDialog.dismiss();
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.network_error),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mCommittingDialog.dismiss();
                        LoginVo loginVo = new Gson().fromJson(response,LoginVo.class);
                        if(null != loginVo && loginVo.isSuccess()){
                            int accountId = loginVo.getData().getAccountId();
                            String accountNo = loginVo.getData().getAccountNo();
                            String accountType = loginVo.getData().getAccountType();
                            String userName = loginVo.getData().getUserName();
                            String token = loginVo.getData().getToken();

                            //保存用户信息
                            saveUserInfo(accountId,accountNo,accountType,userName,token);
                            saveConfigs(accountId,accountNo,accountType,userName,token);

                            Intent i = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,loginVo.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveUserInfo(int accountId,String accountNo,String accountType,String userName,String token){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("accountId",accountId);
        editor.putString("accountNo",accountNo);
        editor.putString("accountType",accountType);
        editor.putString("userName",userName);
        editor.putString("token",token);
        editor.commit();
    }

    public void saveConfigs(int accountId,String accountNo,String accountType,String userName,String token){
        Config.accountId = accountId;
        Config.accountNo = accountNo;
        Config.accountType = accountType;
        Config.userName = userName;
        Config.token = token;
    }

}
