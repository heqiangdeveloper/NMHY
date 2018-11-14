package com.cimcitech.nmhy.activity.home.plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.main.LoginActivity;
import com.cimcitech.nmhy.utils.DataCleanManager;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlanActivity extends AppCompatActivity {
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;
    @Bind(R.id.back_iv)
    ImageView back_Iv;
    @Bind(R.id.more_tv)
    TextView more_Tv;

    public DataCleanManager manager = null;
    private final Context context = PlanActivity.this;
    private SharedPreferences sp;

    public static final String CALL_FINISH = "com.cimcitech.lyt.mainactivity.finish";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(this);
        //sp = getSharedPreferences(Config.KEY_LOGIN_AUTO, MODE_PRIVATE);
        initTitle();
    }

    public void initTitle(){
        titleName_Tv.setText(getResources().getString(R.string.item_plan));
        back_Iv.setVisibility(View.VISIBLE);
        more_Tv.setVisibility(View.GONE);
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
        }
    }

}
