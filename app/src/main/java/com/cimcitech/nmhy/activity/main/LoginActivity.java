package com.cimcitech.nmhy.activity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        addWatcher(user_Et);
        addWatcher(password_Et);
        initView();
    }

    public void initView(){
        user_Et.setText("");
        password_Et.setText("");
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
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.register_tv:
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    public void login(){

    }

}
