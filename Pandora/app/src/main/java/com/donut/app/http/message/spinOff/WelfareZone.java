package com.donut.app.http.message.spinOff;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by HIQ on 2017/6/6.
 */

public class WelfareZone extends BaseObservable {
    private String name;
    private String voiceUrl;
    private Long lastTime;
    private Long browseTimes;
    private Integer type;
    private String actorId;
    private String headPic;
    private String actorName;
    private String b02Id;
    private Integer praiseStatus;
    private Long praiseTimes;
    private Long shareTimes;
    private Integer commentTimes;
    private String createTime;
    private List<ExpressionPics> expressionPics;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public Long getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(Long browseTimes) {
        this.browseTimes = browseTimes;
        notifyChange();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getB02Id() {
        return b02Id;
    }

    public void setB02Id(String b02Id) {
        this.b02Id = b02Id;
    }

    public Integer getPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(Integer praiseStatus) {
        this.praiseStatus = praiseStatus;
        notifyChange();
    }

    public Long getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(Long praiseTimes) {
        this.praiseTimes = praiseTimes;
        notifyChange();
    }

    public Long getShareTimes() {
        return shareTimes;
    }

    public void setShareTimes(Long shareTimes) {
        this.shareTimes = shareTimes;
        notifyChange();
    }

    public Integer getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(Integer commentTimes) {
        this.commentTimes = commentTimes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ExpressionPics> getExpressionPics() {
        return expressionPics;
    }

    public void setExpressionPics(List<ExpressionPics> expressionPics) {
        this.expressionPics = expressionPics;
    }
}
