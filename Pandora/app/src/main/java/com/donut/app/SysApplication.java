package com.donut.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.android.volley.manager.RequestManager;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserBehaviour;
import com.donut.app.entity.UserInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * @author Qi
 */

public class SysApplication extends Application {
    private volatile static SysApplication instance;

    /**
     * 获取本app可用的Context
     *
     * @return 返回一个本类的context
     */
    public static SysApplication getInstance() {
        if (null == instance) {
            synchronized (SysApplication.class) {
                if (null == instance) {
                    instance = new SysApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUMShare();
        RequestManager.getInstance().init(this);
//        initImageLoader();
        getDb();

        if (BuildConfig.DEBUG) {
            // 开启线程模式
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            // 开启虚拟机模式
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build()
            );
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initUMShare() {
        Config.DEBUG = BuildConfig.DEBUG;
        PlatformConfig.setWeixin(Constant.WEIXIN_APPID, Constant.WEIXIN_SECRET);
        PlatformConfig.setQQZone(Constant.QQ_APPID, Constant.QQ_APPKEY);
        PlatformConfig.setSinaWeibo(Constant.SINA_APPID, Constant.SINA_SECRET,
                "http://sns.whalecloud.com");
        UMShareAPI.get(this);
    }

    private static DbUtils db;

    public static DbUtils getDb() {
        if (db == null) {
            synchronized (SysApplication.class) {
                if (null == db) {
                    db = DbUtils.create(instance, Constant.DEFAULT_DBNAME,
                            Constant.DEFAULT_DB_VERSION, dbUpgradeListener);
                    db.configAllowTransaction(true);// 开启事务
                }
            }
        }
        return db;
    }

    private static UserInfo userInfo;

    public static UserInfo getUserInfo() {

        if (null == userInfo) {
            synchronized (SysApplication.class) {
                if (null == userInfo) {
                    try {
                        userInfo = getDb().findFirst(UserInfo.class);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (null == userInfo) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        SysApplication.userInfo = userInfo;
        try {
            SysApplication.getDb().deleteAll(UserInfo.class);
            if (userInfo != null) {
                SysApplication.getDb().saveBindingId(userInfo);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

//    private void initImageLoader(){
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)// do not cache in memory
//                        // avoid bitmapOutOfMemory
//                .cacheInMemory(true).cacheOnDisk(true).build();
//
//        ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
//                getApplicationContext())
//                .defaultDisplayImageOptions(defaultOptions)
//                .threadPriority(Thread.NORM_PRIORITY)
//                .threadPoolSize(Thread.NORM_PRIORITY)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .diskCacheSize(50 * 1024 * 1024)
//                        // .writeDebugLogs()
//                .build();
//
//        ImageLoader.getInstance().init(localImageLoaderConfiguration);
//    }

    private static DbUtils.DbUpgradeListener dbUpgradeListener = new DbUtils.DbUpgradeListener() {
        @Override
        public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
            try {
                //oldVersion=1,newVersion=2
                if (oldVersion == 1) {
                    dbUtils.createTableIfNotExist(UserBehaviour.class);
                    dbUtils.execNonQuery("alter table com_donut_app_entity_UserBehaviour add memberStatus int");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}