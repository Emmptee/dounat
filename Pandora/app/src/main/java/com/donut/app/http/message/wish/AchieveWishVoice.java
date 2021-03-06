package com.donut.app.http.message.wish;

import android.databinding.BaseObservable;

/**
 * AchieveWishVoice
 * Created by Qi on 2017/2/21.
 */
public class AchieveWishVoice extends BaseObservable {
    private String g02Id;

    private String achieveVoiceUrl;

    private String createTime;

    private long lastTime;

    private long listenTimes;

    public String getAchieveVoiceUrl() {
        return achieveVoiceUrl;
    }

    public void setAchieveVoiceUrl(String achieveVoiceUrl) {
        this.achieveVoiceUrl = achieveVoiceUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getG02Id() {
        return g02Id;
    }

    public void setG02Id(String g02Id) {
        this.g02Id = g02Id;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public long getListenTimes() {
        return listenTimes;
    }

    public void setListenTimes(long listenTimes) {
        this.listenTimes = listenTimes;
        notifyChange();
    }
}
