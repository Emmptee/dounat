package com.donut.app.mvp.shakestar.video.record.preview;

public class ShakeStarPreviewRequest {
    private String g03Id;      //素材ID

    private String g04Id;       //歌曲id

    private Integer contentType;   //内容类型

    private String description; // 描述

    private String thumbnail; // 视频缩略图

    private String playUrl; // 视频播放地址
    private Long lastTime;    //视频时长


    public String getG03Id() {
        return g03Id;
    }

    public void setG03Id(String g03Id) {
        this.g03Id = g03Id;
    }

    public String getG04Id() {
        return g04Id;
    }

    public void setG04Id(String g04Id) {
        this.g04Id = g04Id;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }


    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }


}
