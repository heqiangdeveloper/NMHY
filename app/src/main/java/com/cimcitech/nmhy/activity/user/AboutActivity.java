package com.cimcitech.nmhy.activity.user;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.utils.ApkUpdateUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.check_version_tv)
    TextView checkVersionTv;
    @Bind(R.id.more_tv)
    TextView more_Tv;
    @Bind(R.id.titleName_tv)
    TextView titleName_Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initTitle();

        String version = "V" + ApkUpdateUtil.getVersionName(AboutActivity.this);
        String type = isApkInDebug(AboutActivity.this)?getResources().getString(R.string.debug_version)
                :getResources().getString(R.string.release_version);
        //like V2.04 正式版
        checkVersionTv.setText(version + " " + type);
        //checkVersionTv.setText(version);
    }

    public void initTitle(){
        more_Tv.setVisibility(View.GONE);
        titleName_Tv.setText("关于");
    }

    //判断当前应用是否是debug状态
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_iv:
                finish();
                break;
        }
    }

}
