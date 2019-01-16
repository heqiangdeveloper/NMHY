package com.cimcitech.nmhy.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.user.SettingsActivity;
import com.cimcitech.nmhy.utils.DataGenerator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.bbl)
    BottomBarLayout bbL;

    private Fragment[] mFragments;
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        registerBroadcast();
        mFragments = DataGenerator.getFragments();
        //默认关闭了滑动效果
        bbL.setSmoothScroll(false);

        bbL.showNotify(1);
        bbL.setMsg(2,"NEW");

        initBBL();
        bbL.getBottomItem(1).callOnClick();//选中消息页
    }

    public void initBBL(){
        bbL.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            Fragment mFragment = null;
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int i, int position) {
                //底部tab的个数,先全部失效（未选中）
                for(int n = 0; n < 3; n++){
                    bbL.getBottomItem(n).setStatus(false);
                }
                if(position == 0){//消息
                    mFragment = mFragments[0];
                }else if(position == 1){//主页
                    mFragment = mFragments[1];
                }else if(position == 2){//我的
                    mFragment = mFragments[2];
                }
                //激活当前的tab
                bbL.getBottomItem(position).setStatus(true);
                if (mFragments != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container,
                            mFragment).commitAllowingStateLoss();
                }
            }
        });
    }

    public void registerBroadcast(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(SettingsActivity.CALL_FINISH);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,filter);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SettingsActivity.CALL_FINISH)){
                MainActivity.this.finish();
            }
        }
    };

    public void unregisterBroadcast(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }
}
