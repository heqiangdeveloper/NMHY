package com.cimcitech.nmhy.activity.main;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.view.inputmethod.InputMethodManager;

import com.baidu.mapapi.SDKInitializer;
import com.cimcitech.nmhy.baidu.LocationService;
import com.cimcitech.nmhy.utils.Config;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by cimcitech on 2017/7/31.
 */

public class ApkApplication extends Application {

    public LocationService locationService;
    public Vibrator mVibrator;
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static InputMethodManager imm;

    @Override
    public void onCreate() {
        super.onCreate();
        Config.context = getApplicationContext();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        // 图片缓存配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).discCacheFileCount(100)
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging().build();
        imageLoader.init(config);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

        //检测Activity的泄露
        //LeakCanary.install(this);
    }
}
