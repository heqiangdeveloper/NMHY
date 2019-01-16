package com.cimcitech.nmhy.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.activity.home.oil.OilReportMainActivity;
import com.cimcitech.nmhy.activity.home.oil.OilRequestHistoryActivity;
import com.cimcitech.nmhy.activity.home.oil.OilReportHistoryActivity;
import com.cimcitech.nmhy.activity.home.plan.ShipPlanActivity;
import com.cimcitech.nmhy.activity.home.ship.ShipActivity;
import com.cimcitech.nmhy.adapter.home.HomeGridAdapter;
import com.cimcitech.nmhy.widget.MyGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements OnBannerListener {
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.homeGrid)
    MyGridView homeGrid;

    private HomeGridAdapter gridAdapter;
    private Context mContext;
    private ArrayList<Integer> list_path;
    private ArrayList<String> list_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        initViewData();
        return view;
    }

    public void initViewData() {
        if(null != mContext){
            //listContent.setOnItemClickListener(new listContentItemListener());
            //String appAuthStr = Config.AppAuthStr;
            //gridAdapter = new HomeGridAdapter(mContext,appAuthStr);
            gridAdapter = new HomeGridAdapter(mContext);
            homeGrid.setAdapter(gridAdapter);
            homeGrid.setSelector(R.drawable.hide_listview_yellow_selector);
            homeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.tv_logo);
                    //String itemName = textView.getText().toString();
                    switch(position){
//                        case 0://燃油申请
//                            startActivity(new Intent(mContext, OilRequestActivity.class));
//                            break;
//                        case 1://燃油上报
//                            startActivity(new Intent(mContext, OilReportActivity.class));
//                            break;
//                        case 0://燃油申请历史
//                            startActivity(new Intent(mContext, OilRequestHistoryActivity.class));
//                            break;
//                        case 2://船舶动态
//                        startActivity(new Intent(mContext, ShipActivity.class));
//                        break;
                        case 0://燃油上报历史
                            startActivity(new Intent(mContext, OilReportMainActivity.class));
                            break;
                        case 1://航次计划
                            startActivity(new Intent(mContext, ShipPlanActivity.class));
                            break;
                    }
                }
            });
        }
    }

    private void initView() {
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add(R.mipmap.bigtop1);
        list_path.add(R.mipmap.bigtop2);
        list_path.add(R.mipmap.bigtop3);
        list_title.add("");
        list_title.add("");
        list_title.add("");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //不管是加载本地图片还是网络图片都不能少，设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        //如果不想显示标题，在xml中设置title_height="0dp"
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }
    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        //Log.i("tag", "你点了第"+position+"张轮播图");
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
