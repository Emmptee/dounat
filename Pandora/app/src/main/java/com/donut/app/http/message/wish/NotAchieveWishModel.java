package com.donut.app.http.message.wish;

import android.databinding.BaseObservable;

/**
 * NotAchieveWishModel
 * Created by Qi on 2017/2/21.
 */

public class NotAchieveWishModel extends BaseObservable {
    private String g01Id;

    private String b02Id;

    private String fkA01;

    private String fkB03;

    private String createTime;

    private String nickName;

    private String headPic;

    private String starName;

    private long praiseTimes;

    private long commentTimes;

    private int praiseStatus;

    private String description;

    private int achieveStatus;

    private String starUserId;

    private String imgUrl;

    private int mediaType;

    private String playUrl;

    private String lastTime;

    private int wishType;

    private int isMember;

    private int shareTimes;

    private int browseTimes;

    public String getG01Id() {
        return g01Id;
    }

    public void setG01Id(String g01Id) {
        this.g01Id = g01Id;
    }

    public String getB02Id() {
        return b02Id;
    }

    public void setB02Id(String b02Id) {
        this.b02Id = b02Id;
    }

    public String getFkA01() {
        return fkA01;
    }

    public void setFkA01(String fkA01) {
        this.fkA01 = fkA01;
    }

    public String getFkB03() {
        return fkB03;
    }

    public void setFkB03(String fkB03) {
        this.fkB03 = fkB03;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public long getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(long praiseTimes) {
        this.praiseTimes = praiseTimes;
        notifyChange();
    }

    public long getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(long commentTimes) {
        this.commentTimes = commentTimes;
        notifyChange();
    }

    public int getPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(int praiseStatus) {
        this.praiseStatus = praiseStatus;
        notifyChange();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAchieveStatus() {
        return achieveStatus;
    }

    public void setAchieveStatus(int achieveStatus) {
        this.achieveStatus = achieveStatus;
    }

    public String getStarUserId() {
        return starUserId;
    }

    public void setStarUserId(String starUserId) {
        this.starUserId = starUserId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getWishType() {
        return wishType;
    }

    public void setWishType(int wishType) {
        this.wishType = wishType;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public int getShareTimes() {
        return shareTimes;
    }

    public void setShareTimes(int shareTimes) {
        this.shareTimes = shareTimes;
        notifyChange();
    }

    public int getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(int browseTimes) {
        this.browseTimes = browseTimes;
        notifyChange();
    }
}
