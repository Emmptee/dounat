package com.donut.app.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtil
{
    /**
     * 获取软件版本号
     * 
     * @param context
     * @return versionCode
     */
    public static int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        }
        catch(NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取软件版本名称
     * 
     * @param context
     * @return versionName
     */
    public static String getVersion(Context context)
    {
        String versionName = "";
        try
        {
            // 获取版本名称
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        }
        catch(NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionName;
    }
}
