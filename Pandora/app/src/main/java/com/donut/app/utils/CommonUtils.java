package com.donut.app.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import com.donut.app.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wujiaojiao on 2016/5/31.
 */
public class CommonUtils
{
    public static String formatLongToTimeStr(int l)
    {
        int hour = 0;
        int minute = 0;
        int second = 0;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        second = l / 1000;

        if (second > 60)
        {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60)
        {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (decimalFormat.format(hour) + ":" + decimalFormat.format(minute)
                + ":" + decimalFormat.format(second));
    }

    public static String setName(Context context, String name)
    {
        if (name == null || TextUtils.isEmpty(name.trim()))
        {
            return context.getString(R.string.default_name);
        }
        return name;
    }

    public static String setTitle(Context context, String title)
    {
        if (title == null || TextUtils.isEmpty(title.trim()))
        {
            return context.getString(R.string.default_title);
        }
        return title;
    }

    public static String setContent(Context context, String title)
    {
        if (title == null || TextUtils.isEmpty(title.trim()))
        {
            return context.getString(R.string.no_setting);
        }
        return title;
    }

    public static String setSex(Context context, String sex)
    {
        if (sex != null)
        {
            return Integer.parseInt(sex) == 0 ? "男" : "女";
        } else
        {
            return context.getString(R.string.no_setting);
        }
    }

    public static String setStar(Context context, Integer star)
    {
        if (star == null)
        {
            return context.getString(R.string.no_setting);
        }
        String name=context.getString(R.string.no_setting);
        switch (star)
        {
            case 1:
                name= "水瓶座";
            break;
            case 2:
                name= "双鱼座";
            break;
            case 3:
                name= "白羊座";
            break;
            case 4:
                name= "金牛座";
            break;
            case 5:
                name= "双子座";
            break;
            case 6:
                name= "巨蟹座";
            break;
            case 7:
                name= "狮子座";
            break;
            case 8:
                name= "处女座";
            break;
            case 9:
                name= "天秤座";
            break;
            case 10:
                name= "天蝎座";
            break;
            case 11:
                name= "射手座";
            break;
            case 12:
                name= "摩羯座";
            break;
        }
        return name;
    }

    public static String getStatus(Integer status){

        String str="";
        switch (status){
            case 0:
                str="心愿审核中";
                break;
            case 1:
                str="心愿审核不通过";
                break;
            case 2:
                str="心愿审核通过";
                break;
        }
        return str;
    }


    public static boolean isRunningForeground (Context context)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if(!TextUtils.isEmpty(currentPackageName) && "com.donut.app".equals(currentPackageName))
        {
            return true ;
        }
        return false ;
    }

    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

}
