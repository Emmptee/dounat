package com.donut.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.volley.manager.RequestManager;
import com.donut.app.SysApplication;
import com.donut.app.entity.UserBehaviour;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.UserBehaviourRequest;
import com.donut.app.utils.JsonUtils;
import com.lidroid.xutils.exception.DbException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SendBehaviourDataService extends Service {
    public SendBehaviourDataService() {
    }

    private List<UserBehaviour> userBehaviours;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            userBehaviours = SysApplication.getDb().findAll(UserBehaviour.class);
            if (userBehaviours == null || userBehaviours.size() <= 0) {
                return super.onStartCommand(intent, flags, START_REDELIVER_INTENT);
            }
            SysApplication.getDb().deleteAll(userBehaviours);

            List<com.donut.app.http.message.UserBehaviour> sss = new ArrayList<>();
            for (UserBehaviour behaviour : userBehaviours){
                com.donut.app.http.message.UserBehaviour b = new com.donut.app.http.message.UserBehaviour();
                b.setDeviceId(behaviour.getDeviceId());
                b.setFunCode(behaviour.getFunCode());
                b.setFkA01(behaviour.getFkA01());
                b.setMemberStatus(behaviour.getMemberStatus());
                b.setLoginStatus(behaviour.getLoginStatus());
                b.setIpAddress(behaviour.getIpAddress());
                b.setCity(behaviour.getCity());
                b.setNetStatus(behaviour.getNetStatus());
                b.setInTime(dateFormat(behaviour.getInTime()));
                b.setOutTime(dateFormat(behaviour.getOutTime()));
                b.setCreateTime(dateFormat(behaviour.getCreateTime()));
                b.setStarCode(behaviour.getStarCode());
                b.setRequestJson(behaviour.getRequestJson());
                sss.add(b);
            }
            UserBehaviourRequest request = new UserBehaviourRequest();
            request.setUserBehaviours(sss);
            request.setSystemTime(System.currentTimeMillis());
            SendNetRequestManager manager = new SendNetRequestManager(requestListener);
            manager.sendNetRequest(request, HeaderRequest.USER_BEHAVIOURS);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, START_REDELIVER_INTENT);
    }

    private RequestManager.RequestListener requestListener = new RequestManager.RequestListener() {

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
            if (!"0000".equals(res.getCode())) {
                try {
                    SysApplication.getDb().saveOrUpdateAll(userBehaviours);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
            stopSelf();
        }

        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long l, long l1, String s) {
        }

        @Override
        public void onError(String arg0, String arg1, int arg2) {
            try {
                SysApplication.getDb().saveOrUpdateAll(userBehaviours);
            } catch (DbException e) {
                e.printStackTrace();
            }
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static String dateFormat(Date date) {
        String formatDate = "";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
        if (date != null) {
            formatDate = format.format(date);
        }
        return formatDate;
    }
}
