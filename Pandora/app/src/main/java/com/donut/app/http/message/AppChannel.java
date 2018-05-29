package com.donut.app.http.message;

/**
 * AppChannel
 * Created by Qi on 2017/3/2.
 */

public class AppChannel {

    private String uuid;

    private String name;

    private int type;

    private String picUrl;

    private String content;

    private int jumpStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getJumpStatus() {
        return jumpStatus;
    }

    public void setJumpStatus(int jumpStatus) {
        this.jumpStatus = jumpStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}