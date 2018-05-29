package com.donut.app.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.donut.app.SysApplication;
import com.donut.app.entity.UserBehaviour;
import com.donut.app.entity.UserInfo;
import com.donut.app.utils.AppConfigUtil;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.Date;

public class SaveH5BehaviourDataService extends IntentService {

    private static final String ACTION = "com.donut.app.service.action.SaveH5BehaviourData";

    private static final String FUN_CODE = "FUN_CODE";
    private static final String JSON_DATA = "JSON_DATA";

    public SaveH5BehaviourDataService() {
        super("SaveH5BehaviourDataService");
    }

    public static void startH5Action(Context context, String funCode, String requestJson) {
        Intent intent = new Intent(context, SaveH5BehaviourDataService.class);
        intent.setAction(ACTION);
        intent.putExtra(FUN_CODE, AppConfigUtil.getBehaviourHeader() + funCode);
        if (requestJson != null) {
            intent.putExtra(JSON_DATA, requestJson);
        }
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String funCode = intent.getStringExtra(FUN_CODE);
            final String jsonRequest = intent.getStringExtra(JSON_DATA);
            try {
                saveData(funCode, jsonRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveData(String funCode, String jsonRequest) throws Exception {

        UserInfo userInfo = SysApplication.getUserInfo();

        UserBehaviour behaviour = new UserBehaviour();
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
                String deviceId = tm.getDeviceId();
                if ("000000000000000".equals(deviceId)) {
                    behaviour.setDeviceId(android_id);
                } else {
                    behaviour.setDeviceId(tm.getDeviceId());
                }
            } else {
                behaviour.setDeviceId(android_id);
            }
        } catch (Exception e) {
            behaviour.setDeviceId(android_id);
        }
        if (userInfo.getUserId() != null) {
            behaviour.setFkA01(userInfo.getUserId());
            behaviour.setLoginStatus(1);
            behaviour.setMemberStatus(userInfo.getMemberStatus());
        } else {
            behaviour.setLoginStatus(0);
        }
        behaviour.setNetStatus(getCurrentNetType());
        behaviour.setStarCode(new AppConfigUtil(this).getStarCode());
        behaviour.setRequestJson(jsonRequest);
        behaviour.setFunCode(funCode);

        Date nowTime = new Date(System.currentTimeMillis());
        if ("xx".equals(funCode.substring(funCode.length() - 2))) {
            behaviour.setOutTime(nowTime);
            UserBehaviour dbFirst = SysApplication.getDb().findFirst(
                    Selector.from(UserBehaviour.class)
                            .expr("funCode", "=", funCode.substring(0, funCode.length() - 2) + "00")
                            .orderBy("createTime", true));
            if (dbFirst != null) {
                behaviour.setFunCode(dbFirst.getFunCode());
                behaviour.setCreateTime(dbFirst.getCreateTime());
                behaviour.setInTime(dbFirst.getInTime());
                behaviour.setId(dbFirst.getId());
                SysApplication.getDb().update(behaviour);
            }
        } else if ("88888".equals(funCode.substring(funCode.length() - 5))) {
            behaviour.setInTime(new AppConfigUtil(this).getStarTime());
            behaviour.setOutTime(nowTime);
            behaviour.setCreateTime(nowTime);
            SysApplication.getDb().save(behaviour);
            new AppConfigUtil(this).setOutTime(nowTime.getTime());
            startService(new Intent(this, SendBehaviourDataService.class));
        } else {
            behaviour.setCreateTime(nowTime);
            behaviour.setInTime(nowTime);
            boolean flag = "99999".equals(funCode.substring(funCode.length() - 5));
            if (flag) {
                behaviour.setOutTime(new AppConfigUtil(this).getOutTime());
            }
            SysApplication.getDb().save(behaviour);
            if (flag) {
                startService(new Intent(this, SendBehaviourDataService.class));
            }
        }
    }

    /**
     * 得到当前的手机网络类型
     *
     * @return
     */
    public int getCurrentNetType() {
        int netStatus = 4;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        String strNetworkType = "";

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
                netStatus = 3;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        netStatus = 0;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        netStatus = 1;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        netStatus = 2;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if ("TD-SCDMA".equalsIgnoreCase(_strSubTypeName) || "WCDMA".equalsIgnoreCase(_strSubTypeName) || "CDMA2000".equalsIgnoreCase(_strSubTypeName)) {
                            strNetworkType = "3G";
                            netStatus = 1;
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }

            }
        }
        return netStatus;
    }

}
