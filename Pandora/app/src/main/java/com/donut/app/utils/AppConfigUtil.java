package com.donut.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.donut.app.SysApplication;
import com.donut.app.config.Constant;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Qi on 2016/8/3.
 */
public class AppConfigUtil {

    public AppConfigUtil(Context context) {
        sp_config = SysApplication.getInstance().getSharedPreferences(Constant.SP_CONFIG,
                Context.MODE_MULTI_PROCESS);
    }

    private SharedPreferences sp_config;

    private static final String STAR_CODE = "STAR_CODE", STAR_TIME = "STAR_TIME", OUT_TIME = "OUT_TIME";

    public String getStarCode() {
        String starCode = sp_config.getString(STAR_CODE, "");
        if ("".equals(starCode)) {
            starCode = UUID.randomUUID().toString();
            setStarCode(starCode);
        }
        return starCode;
    }

    public void setStarCode(String starCode) {
        sp_config.edit().putString(STAR_CODE, starCode).commit();
    }

    public Date getStarTime() {
        return new Date(sp_config.getLong(STAR_TIME, 0));
    }

    public void setStarTime(long starTime) {
        sp_config.edit().putLong(STAR_TIME, starTime).commit();
    }

    public Date getOutTime() {
        return new Date(sp_config.getLong(OUT_TIME, 0));
    }

    public void setOutTime(long outTime) {
        sp_config.edit().putLong(OUT_TIME, outTime).commit();
    }

    public static String getBehaviourHeader() {
        Context context = SysApplication.getInstance();
        int versionCode = VersionUtil.getVersionCode(context.getApplicationContext());
        String channel = "1";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Object obj = info.metaData.get("InstallChannel");
            if (obj instanceof Integer) {
                channel = String.valueOf((Integer) obj);
            } else {
                channel = (String) obj;
            }
            if (channel == null) {
                channel = "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 改成自动获取~~~
        //header = "001011"
        return "0" + String.valueOf(versionCode).substring(4) + channel;
    }
}
