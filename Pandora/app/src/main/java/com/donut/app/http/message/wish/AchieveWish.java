package com.donut.app.http.message.wish;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.donut.app.BR;

import java.util.List;

/**
 * AchieveWish
 * Created by Qi on 2017/2/21.
 */

public class AchieveWish extends BaseObservable {
    private String g01Id;

    private String b02Id;

    private String fkA01;

    private String fkB03;

    private String createTime;

    private String achieveTime;

    private String nickName;

    private String headPic;

    private String starName;

    private String starHeadPic;

    private String description;

    private int commentTimes;

    private int collectionStatus;

    private int praiseStatus;

    private long praiseTimes;

    private long videoPlayTimes;

    private long shareTimes;

    private long collectTimes;

    private String achievePicUrl;

    private String achieveVideoUrl;

    private List<AchieveWishVoice> achieveVoiceList;

    private String achieveText;

    /**
     * 心愿发起者是否为会员: 0:非,1:是
     */
    private int isMember;

    /**
     * 明星是否为会员: 0:非,1:是
     */
    private int starIsMember;

    private int wishType;

    private String imgUrl;

    private int mediaType;

    private String playUrl;

    private long lastTime;

    private int browseTimes;

    public int getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(int browseTimes) {
        this.browseTimes = browseTimes;
        notifyChange();
    }

    /**
     * @return g01Id
     */
    public String getG01Id() {
        return g01Id;
    }

    /**
     * @param g01Id the g01Id to set
     */
    public void setG01Id(String g01Id) {
        this.g01Id = g01Id;
    }

    /**
     * @return b02Id
     */
    public String getB02Id() {
        return b02Id;
    }

    /**
     * @param b02Id the b02Id to set
     */
    public void setB02Id(String b02Id) {
        this.b02Id = b02Id;
    }

    /**
     * @return fkA01
     */
    public String getFkA01() {
        return fkA01;
    }

    /**
     * @param fkA01 the fkA01 to set
     */
    public void setFkA01(String fkA01) {
        this.fkA01 = fkA01;
    }

    /**
     * @return fkB03
     */
    public String getFkB03() {
        return fkB03;
    }

    /**
     * @param fkB03 the fkB03 to set
     */
    public void setFkB03(String fkB03) {
        this.fkB03 = fkB03;
    }

    /**
     * @return createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return achieveTime
     */
    public String getAchieveTime() {
        return achieveTime;
    }

    /**
     * @param achieveTime the achieveTime to set
     */
    public void setAchieveTime(String achieveTime) {
        this.achieveTime = achieveTime;
    }

    /**
     * @return nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickName the nickName to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return starName
     */
    public String getStarName() {
        return starName;
    }

    /**
     * @param starName the starName to set
     */
    public void setStarName(String starName) {
        this.starName = starName;
    }

    /**
     * @return starHeadPic
     */
    public String getStarHeadPic() {
        return starHeadPic;
    }

    /**
     * @param starHeadPic the starHeadPic to set
     */
    public void setStarHeadPic(String starHeadPic) {
        this.starHeadPic = starHeadPic;
    }

    @Bindable
    public long getPraiseTimes() {
        return praiseTimes;
    }

    /**
     * @param praiseTimes the praiseTimes to set
     */
    public void setPraiseTimes(long praiseTimes) {
        this.praiseTimes = praiseTimes;
        notifyPropertyChanged(BR.wish);
    }

    public long getVideoPlayTimes() {
        return videoPlayTimes;
    }

    public void setVideoPlayTimes(long videoPlayTimes) {
        this.videoPlayTimes = videoPlayTimes;
        notifyChange();
    }

    /**
     * @return shareTimes
     */
    public long getShareTimes() {
        return shareTimes;
    }

    /**
     * @param shareTimes the shareTimes to set
     */
    public void setShareTimes(long shareTimes) {
        this.shareTimes = shareTimes;
        notifyChange();
    }

    /**
     * @return collectTimes
     */
    public long getCollectTimes() {
        return collectTimes;
    }

    /**
     * @param collectTimes the collectTimes to set
     */
    public void setCollectTimes(long collectTimes) {
        this.collectTimes = collectTimes;
        notifyChange();
    }

    /**
     * @return achievePicUrl
     */
    public String getAchievePicUrl() {
        return achievePicUrl;
    }

    /**
     * @param achievePicUrl the achievePicUrl to set
     */
    public void setAchievePicUrl(String achievePicUrl) {
        this.achievePicUrl = achievePicUrl;
    }

    /**
     * @return achieveVideoUrl
     */
    public String getAchieveVideoUrl() {
        return achieveVideoUrl == null ? "" : achieveVideoUrl;
    }

    /**
     * @param achieveVideoUrl the achieveVideoUrl to set
     */
    public void setAchieveVideoUrl(String achieveVideoUrl) {
        this.achieveVideoUrl = achieveVideoUrl;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public List<AchieveWishVoice> getAchieveVoiceList() {
        return achieveVoiceList;
    }

    public void setAchieveVoiceList(List<AchieveWishVoice> achieveVoiceList) {
        this.achieveVoiceList = achieveVoiceList;
    }

    /**
     * @return achieveText
     */
    public String getAchieveText() {
        return achieveText == null ? "" : achieveText;
    }

    /**
     * @param achieveText the achieveText to set
     */
    public void setAchieveText(String achieveText) {
        this.achieveText = achieveText;
    }

    public int getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(int commentTimes) {
        this.commentTimes = commentTimes;
        notifyChange();
    }

    public int getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(int collectionStatus) {
        this.collectionStatus = collectionStatus;
        notifyChange();
    }

    public int getPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(int praiseStatus) {
        this.praiseStatus = praiseStatus;
        notifyChange();
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public int getStarIsMember() {
        return starIsMember;
    }

    public void setStarIsMember(int starIsMember) {
        this.starIsMember = starIsMember;
    }

    public int getWishType() {
        return wishType;
    }

    public void setWishType(int wishType) {
        this.wishType = wishType;
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

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
