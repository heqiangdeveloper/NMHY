package com.cimcitech.nmhy.activity.main;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.utils.DataGenerator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.bbl)
    BottomBarLayout bbL;

    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFragments = DataGenerator.getFragments();
        //默认关闭了滑动效果
        bbL.setSmoothScroll(false);

        bbL.showNotify(1);
        bbL.setMsg(2,"NEW");

        initBBL();
        bbL.getBottomItem(0).callOnClick();//选中消息页
    }

    public void initBBL(){
        bbL.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            Fragment mFragment = null;
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int i, int position) {
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
                bbL.getBottomItem(position).setStatus(true);
                if (mFragments != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container,
                            mFragment).commitAllowingStateLoss();
                }
            }
        });
    }
}
