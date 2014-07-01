package com.lightreader.bzz.Activity;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;



import org.json.JSONException;
import org.json.JSONObject;

import com.lightreader.bzz.sqlite.MessageBoxDao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    public static ArrayList<HashMap<String, Object>> tempObjectsList = new ArrayList<HashMap<String, Object>>();//全局的临时变量List
    private static MyApplication instance = null;
    private static MessageBoxDao messageBoxDao = null;//SQLite数据库操作


    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }


    public static MessageBoxDao getMessageBoxDao() {
        // 初始化的时候创建一个数据库的表对象
        if (messageBoxDao == null) {
            messageBoxDao = new MessageBoxDao(getInstance().getApplicationContext());
        }
        return messageBoxDao;
    }



    /**
     * 初始化图片缓存组件
     */
    private void initImageLoader() {
        /*File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiscCache(cacheDir))
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(200)
//                .writeDebugLogs()
                .build();


        ImageLoader.getInstance().init(config);*/
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 友盟升级检查
     */
    private void umengUpdate() {
        //UmengUpdateAgent.setUpdateOnlyWifi(false); // 仅Wifi下更新
        //UmengUpdateAgent.update(this); // 自动更新
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        //refreshUser();
        //refreshIntegral();
    }
}
