package com.donut.app.http.message.spinOff;

import com.donut.app.http.message.BaseResponse;

import java.util.List;


public class ExpressionPicsDetailResponse extends BaseResponse {
    private List<ExpressionPics> expressionPics;

    private String actorName;

    private String actorId;

    private String trailersPic;

    private String name;

    private Long praiseTimes;

    private Long shareTimes;

    private int praiseStatus;

    private int commentTimes;

    public List<ExpressionPics> getExpressionPics() {
        return expressionPics;
    }

    public void setExpressionPics(List<ExpressionPics> expressionPics) {
        this.expressionPics = expressionPics;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getTrailersPic() {
        return trailersPic;
    }

    public void setTrailersPic(String trailersPic) {
        this.trailersPic = trailersPic;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(int praiseStatus) {
        this.praiseStatus = praiseStatus;
        notifyChange();
    }

    public int getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(int commentTimes) {
        this.commentTimes = commentTimes;
        notifyChange();
    }
}